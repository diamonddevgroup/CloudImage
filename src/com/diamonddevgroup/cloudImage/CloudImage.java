package com.diamonddevgroup.cloudImage;

import com.codename1.ui.EncodedImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.diamonddevgroup.cloudImage.utils.CN1String;
import com.diamonddevgroup.cloudImage.utils.StringUtils;

/**
 *
 * @author Diamond
 */
public class CloudImage {

    String cloudURL = "";

    String format = "";
    String cacheName = "";
    String resourceType = "";
    boolean makeRound = false;
    int radius = 0;
    boolean isFacebookImage = false;
    Manipulator manipulator = null;
    private final int offset = -5;

    /**
     * CloudImage constructor with the cloud URL argument
     *
     * @param cloudURL the URL to the remote script
     */
    public CloudImage(String cloudURL) {
        this.cloudURL = cloudURL;
    }

    /**
     * Set the cloud URL
     *
     * @param cloudURL the URL to the remote script
     * @return CloudImage with cloud URL set
     */
    public CloudImage cloudURL(String cloudURL) {
        this.cloudURL = cloudURL;
        return this;
    }

    /**
     * Set the image border radius. The radius will be applied locally after the
     * image has been downloaded.
     *
     * @param radius the border radius to apply to downloaded image
     * @return CloudImage with radius set
     */
    public CloudImage makeRound(int radius) {
        this.makeRound = radius == 360;
        this.radius = radius;
        return this;
    }

    /**
     * Local storage name to save image to, the URL would be used as filename if
     * this is not set
     *
     * @param cacheName the local image cache name
     * @return CloudImage with cache name set
     */
    public CloudImage cacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    /**
     * Get the manipulator applied to the CloudImage.
     *
     * @return the manipulator applied
     */
    public Manipulator manipulator() {
        if (this.manipulator == null) {
            this.manipulator = new Manipulator();
        }
        return this.manipulator;
    }

    private String format(String imageName) {
        String[] strs = new CN1String(imageName).split('.');
        if (strs.length > 1) {
            return strs[strs.length - 1];
        } else {
            return "png";
        }
    }

    /**
     * Set the manipulator
     *
     * @param manipulator the URL to the remote script
     * @return CloudImage with manipulator set
     */
    public CloudImage manipulator(Manipulator manipulator) {
        this.manipulator = manipulator;
        return this;
    }

    public String generate() {
        return generate(null);
    }

    private String generate(String source) {
        this.format = "format=" + format(source);
        source = "image=" + source;
        String manipulatorStr = "param=" + manipulator().generate();
        return unsignedDownloadUrlPrefix(cloudURL) + new CN1String(StringUtils.join(new String[]{source, this.format, manipulatorStr}, "&")).replaceAll("([^:])\\/+", "$1/");
    }

    public String unsignedDownloadUrlPrefix(String cloudURL) {
        String protocol = "http://";
        if (cloudURL.startsWith("http://") || cloudURL.startsWith("https://")) {
            protocol = "";
        }
        String prefix = StringUtils.join(new String[]{protocol, cloudURL, "?"}, "");
        return prefix;
    }

    public Image image(Image placeholder, String source) {
        EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);

        Manipulator t = this.manipulator();
        if (t == null) {
            t = new Manipulator();
            this.manipulator(t);
        }
        t.width(encImage.getWidth() + offset);
        t.height(encImage.getHeight() + offset);

        String url = generate(source);
        String cache = this.cacheName == null ? url : this.cacheName;

        if (this.radius > 0) {
            Image roundMask = Image.createImage(encImage.getWidth(), encImage.getHeight(), 0xff000000);
            Graphics gr = roundMask.getGraphics();
            gr.setColor(0xffffff);

            if (this.makeRound) {
                gr.fillArc(0, 0, encImage.getWidth(), encImage.getHeight(), 0, this.radius);
            } else {
                gr.fillRoundRect(0, 0, encImage.getWidth(), encImage.getHeight(), this.radius, this.radius);
            }

            return URLImage.createToStorage(encImage, cache, url, URLImage.createMaskAdapter(roundMask));
        }
        return URLImage.createToStorage(encImage, cache, url);
    }
}
