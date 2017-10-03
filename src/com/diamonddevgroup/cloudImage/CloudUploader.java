package com.diamonddevgroup.cloudImage;

import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.util.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author Diamond
 */
public class CloudUploader {

    private String URL = null;

    public CloudUploader url(String url) {
        this.URL = url;
        return this;
    }

    public Map upload(Object file, Map<String, Object> options) throws IOException {
        return upload(file, options, false);
    }

    IOException err = new IOException("Network error! Please check your connection and try again.");

    public Map upload(Object file, Map<String, Object> options, boolean background) throws IOException {
        EncodedImage enc = null;
        if (file instanceof String) {
            Image img = Image.createImage((String) file);
            enc = EncodedImage.createFromImage(img, false);
        } else if (file instanceof byte[]) {
            enc = EncodedImage.create((byte[]) file);
        } else if (file instanceof Image) {
            enc = EncodedImage.createFromImage((Image) file, false);
        } else {
            throw new IllegalArgumentException("Unsurported image file.");
        }

        MultipartRequest req = new MultipartRequest() {
            @Override
            protected void handleException(Exception er) {
                err = new IOException("Network error! Please check your connection and try again.");
            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {
                if (code == 400 || code == 404 || code == 500) {
                    err = new IOException("There was a problem connecting to the server, try again in a while.");
                } else {
                    Dialog.show("Error", code + ": " + message, "Retry", "Cancel");
                }
            }
        };
        req.setUrl(this.URL);
        req.setPost(true);
        req.setHttpMethod("POST");
        req.addRequestHeader("Client-key", "");
        req.addRequestHeader("Client-url-date", Preferences.get(prefix() + "URL_Date", "2016-11-11"));
        req.addRequestHeader("App-name", Display.getInstance().getProperty("AppName", "App Name"));
        req.addRequestHeader("App-version", Display.getInstance().getProperty("AppVersion", "1.0"));
        req.addRequestHeader("Platform-name", Display.getInstance().getPlatformName());
        req.addRequestHeader("Os-version", Display.getInstance().getProperty("OSVer", "1.0"));
        req.addRequestHeader("Timezone", TimeZone.getDefault().getID());
        req.setDuplicateSupported(true);

        req.addData("file", enc.getImageData(), "image/jpg");
        req.setFilename("file", (String) options.get("filename"));

        for (Map.Entry<String, Object> entry : options.entrySet()) {
            req.addArgumentNoEncoding((String) entry.getKey(), entry.getValue().toString());
        }

        if (background) {
            NetworkManager.getInstance().addToQueue(req);
        } else {
            NetworkManager.getInstance().addToQueueAndWait(req);
        }

        if (req.getResponseCode() == 200) {
            Map<String, Object> out = new HashMap();
            Display.getInstance().invokeAndBlock(() -> {
                JSONParser p = new JSONParser();
                try (InputStreamReader r = new InputStreamReader(new ByteArrayInputStream(req.getResponseData()))) {
                    out.putAll(p.parseJSON(r));
                } catch (IOException ex) {
                    err = ex;
                }
            });
            if (!out.isEmpty()) {
                Map<String, Object> res = out;

                int code = (int) (double) res.get("code");
                switch (code) {
                    case 200:
                    case 201:
                        return res;
                    case 105:
                        throw new IOException("A reset is required, please sign out and sign back in to continue.");
                    case 999:
                        throw new IOException("An update is required to continue, please update the app in store.");
                    case 404:
                        Preferences.set(prefix() + "Url", res.get("url").toString());
                        Preferences.set(prefix() + "URL_Date", res.get("url_date").toString());
                        throw new IOException("Application server has been updated, try re-uploading again.");
                    default:
                        throw new IOException((String) res.get("message"));
                }
            } else {
                err = new IOException("We cannot reach the server at the moment, try re-uploading again.");
                throw err;
            }
        } else {
            throw err;
        }
    }

    public static String prefix() {
        char[] characterSet = " &!#*$)(.,`~@+=/\\\"?'][}{".toCharArray();
        String name = Display.getInstance().getProperty("AppName", "App Name");
        for (int i = 0; i < characterSet.length; i++) {
            char[] value = {characterSet[i]};
            name = StringUtil.replaceAll(name, new String(value), "_");
        }
        return name + "-";
    }

    public static String cleanUpString(String dirtyString, String cleaner) {
        char[] characterSet = " &!#*$)(.,`~@+=/\\\"?'][}{".toCharArray();
        String result = dirtyString;
        for (int i = 0; i < characterSet.length; i++) {
            char[] value = {characterSet[i]};
            result = StringUtil.replaceAll(result, new String(value), cleaner);
        }
        return result;
    }
}
