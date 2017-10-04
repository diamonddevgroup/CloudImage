package com.diamonddevgroup.cloudImage;

import com.diamonddevgroup.cloudImage.utils.ObjectUtils;
import com.diamonddevgroup.cloudImage.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Diamond
 */
public class Manipulator {

    protected Map manipulator;
    protected List<Map> manipulators;

    public Manipulator(Manipulator manipulator) {
        this(dup(manipulator.manipulators));
    }

    // Warning: options will destructively updated!
    public Manipulator(List<Map> manipulators) {
        this.manipulators = manipulators;
        if (manipulators.isEmpty()) {
            chain();
        } else {
            this.manipulator = manipulators.get(manipulators.size() - 1);
        }
    }

    /**
     * Default manipulator to start creating chaining methods
     */
    public Manipulator() {
        this.manipulators = new ArrayList<>();
        chain();
    }

    // Warning: options will destructively updated!
    private Manipulator params(Map manipulator) {
        this.manipulator = manipulator;
        manipulators.add(manipulator);
        return this;
    }

    private Manipulator chain() {
        return params(new HashMap());
    }

    private Manipulator chainWith(Manipulator manipulator) {
        List<Map> manip = dup(this.manipulators);
        manip.addAll(dup(manipulator.manipulators));
        return new Manipulator(manip);
    }

    public Manipulator param(String key, Object value) {
        manipulator.put(key, value);
        return this;
    }

    /**
     * Set the image path. This is useful for images that are in sub-folders.
     * This path will be concatenated with the base URL before the image name
     *
     * @param path the path to a sub-folder or blank
     * @return Manipulator with path set
     */
    public Manipulator path(String path) {
        return param("path", path);
    }

    /**
     * Set the image size. This applies to square images.
     *
     * @param size the size of the image to be downloaded
     * @return Manipulator with size set
     */
    public Manipulator size(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size value cannot be less than 1");
        }
        return param("size", size);
    }

    /**
     * Set the image width.
     *
     * @param width the width of the image to be downloaded
     * @return Manipulator with width set
     */
    public Manipulator width(int width) {
        if (width < 1) {
            throw new IllegalArgumentException("Width value cannot be less than 1");
        }
        return param("width", width);
    }

    /**
     * Set the image height.
     *
     * @param height the width of the image to be downloaded
     * @return Manipulator with height set
     */
    public Manipulator height(int height) {
        if (height < 1) {
            throw new IllegalArgumentException("Height value cannot be less than 1");
        }
        return param("height", height);
    }

    /**
     * Set the image cropping type.
     *
     * @param crop one of fill, fit or free
     * @return Manipulator with crop applied
     */
    public Manipulator crop(String crop) {
        return param("crop", crop);
    }

    /**
     * Set the image border radius.
     *
     * @param radius the border radius to apply to downloaded image
     * @return Manipulator with radius set
     */
    public Manipulator radius(int radius) {
        if (radius < 0 || radius > 100) {
            throw new IllegalArgumentException("Radius value must be between 0 and 100");
        }
        return param("radius", radius);
    }

    /**
     * Set the image quality.
     *
     * @param quality the quality of the image to be downloaded
     * @return Manipulator with quality set
     */
    public Manipulator quality(int quality) {
        if (quality < 1 || quality > 100) {
            throw new IllegalArgumentException("Quality value must be between 1 and 100");
        }
        return param("quality", quality);
    }

    /**
     * Set the image background color. Useful for png images
     *
     * @param color the background color of the image to be downloaded
     * @return Manipulator with background color set
     */
    public Manipulator backgroundColor(String color) {
        return param("backgroundColor", color);
    }

    /**
     * Set if the image should contain shadow.
     *
     * @param shadow one of true or false
     * @return Manipulator with shadow applied
     */
    public Manipulator shadow(boolean shadow) {
        return param("shadow", shadow);
    }

    /**
     * Set the offset of the image.
     *
     * @param offset the offset of the image shadow
     * @return Manipulator with shadow offset set
     */
    public Manipulator shadowOffset(int offset) {
        if (offset < 1 || offset > 100) {
            throw new IllegalArgumentException("Shadow offset value must be between 1 and 100");
        }
        return param("shadowOffset", offset);
    }

    /**
     * Set the image frame width.
     *
     * @param width the width of the image frame
     * @return Manipulator with frame width set
     */
    public Manipulator frameWidth(int width) {
        if (width < 1 || width > 100) {
            throw new IllegalArgumentException("Frame width value must be between 1 and 100");
        }
        return param("frameWidth", width);
    }

    /**
     * Set the image frame color.
     *
     * @param color the color of the image frame
     * @return Manipulator with frame color set
     */
    public Manipulator frameColor(String color) {
        return param("frameColor", color);
    }

    /**
     * Set the image frame offset.
     *
     * @param offset the offset of the image frame
     * @return Manipulator with frame offset set
     */
    public Manipulator frameOffset(int offset) {
        if (offset < 1 || offset > 100) {
            throw new IllegalArgumentException("Frame offset value must be between 1 and 100");
        }
        return param("frameOffset", offset);
    }

    /**
     * Set the image binder ring.
     *
     * @param binder the binder ring applied to image to be downloaded
     * @return Manipulator with binder ring set
     */
    public Manipulator binderRing(boolean binder) {
        return param("binderRing", binder);
    }

    /**
     * Set the image binder ring spacing.
     *
     * @param spacing the binder ring spacing applied to image to be downloaded
     * @return Manipulator with binder ring spacing set
     */
    public Manipulator binderSpacing(int spacing) {
        if (spacing < 1 || spacing > 100) {
            throw new IllegalArgumentException("Binder spacing value must be between 1 and 100");
        }
        return param("binderSpacing", spacing);
    }

    /**
     * Set the image binder ring offset.
     *
     * @param offset the binder ring offset applied to image to be downloaded
     * @return Manipulator with binder ring offset set
     */
    public Manipulator binderOffset(int offset) {
        if (offset < 1 || offset > 100) {
            throw new IllegalArgumentException("Binder offset value must be between 1 and 100");
        }
        return param("binderOffset", offset);
    }

    /**
     * Set the image watermark.
     *
     * @param watermark the watermark applied to image to be downloaded
     * @return Manipulator with watermark applied
     */
    public Manipulator watermark(String watermark) {
        return param("watermark", watermark);
    }

    /**
     * Set the image watermark horizontal position.
     *
     * @param x the watermark horizontal position
     * @return Manipulator with watermark horizontal position set
     */
    public Manipulator watermarkXPosition(int x) {
        if (x < 1 || x > 100) {
            throw new IllegalArgumentException("Watermark horizontal position value must be between 1 and 100");
        }
        return param("watermarkXPosition", x);
    }

    /**
     * Set the image watermark vertical position.
     *
     * @param y the watermark vertical position
     * @return Manipulator with watermark vertical position set
     */
    public Manipulator watermarkYPosition(int y) {
        if (y < 1 || y > 100) {
            throw new IllegalArgumentException("Watermark vertical position value must be between 1 and 100");
        }
        return param("watermarkYPosition", y);
    }

    /**
     * Set the image watermark transparency.
     *
     * @param transparency the watermark transparency
     * @return Manipulator with watermark transparency set
     */
    public Manipulator watermarkTransparency(int transparency) {
        if (transparency < 0 || transparency > 255) {
            throw new IllegalArgumentException("Watermark transparency value must be between 0 and 255");
        }
        return param("watermarkTransparency", transparency);
    }

    /**
     * Set the image watermark transparency.
     *
     * @param angle the angle of the image rotation
     * @return Manipulator with rotation applied
     */
    public Manipulator rotate(int angle) {
        if (angle < 0 || angle > 360) {
            throw new IllegalArgumentException("Rotation value must be between 0 and 360");
        }
        return param("rotate", angle);
    }

    /**
     * Set the image to flip vertically.
     *
     * @param value one of true or false
     * @return Manipulator with vertical flipping set
     */
    public Manipulator flipX(boolean value) {
        return param("flipX", value);
    }

    /**
     * Set the image to flip horizontally.
     *
     * @param value one of true or false
     * @return Manipulator with horizontal flipping set
     */
    public Manipulator flipY(boolean value) {
        return param("flipY", value);
    }

    /**
     * Set the image brightness.
     *
     * @param brightness the brightness of the image to be downloaded
     * @return Manipulator with brightness set
     */
    public Manipulator brightness(int brightness) {
        if (brightness < 1 || brightness > 100) {
            throw new IllegalArgumentException("Brightness value must be between 1 and 100");
        }
        return param("brightness", brightness);
    }

    /**
     * Set the image contrast.
     *
     * @param contrast the contrast of the image to be downloaded
     * @return Manipulator with contrast set
     */
    public Manipulator contrast(int contrast) {
        if (contrast < 1 || contrast > 100) {
            throw new IllegalArgumentException("Contrast value must be between 1 and 100");
        }
        return param("contrast", contrast);
    }

    /**
     * Set the image gamma.
     *
     * @param gamma the gamma of the image to be downloaded
     * @return Manipulator with gamma set
     */
    public Manipulator gamma(int gamma) {
        if (gamma < 1 || gamma > 100) {
            throw new IllegalArgumentException("Gamma value must be between 1 and 100");
        }
        return param("gamma", gamma);
    }

    public String generate() {
        return generate(manipulator);
    }

    private String generate(Iterable<Map> optionsList) {
        List<String> components = new ArrayList<>();
        for (Map options : optionsList) {
            components.add(generate(options));
        }
        return StringUtils.join(components, "/");
    }

    private String generate(Map options) {
        List manip = ObjectUtils.asArray(options.get("manipulator"));
        boolean allNamed = true;
        for (Object baseManipulator : manip) {
            if (baseManipulator instanceof Map) {
                allNamed = false;
                break;
            }
        }
        String namedManipulator = null;
        if (allNamed) {
            namedManipulator = StringUtils.join(manip, ".");
            manip = new ArrayList();
        } else {
            List ts = manip;
            manip = new ArrayList();
            for (Object baseManipulator : ts) {
                String manipulatorString;
                if (baseManipulator instanceof Map) {
                    manipulatorString = generate((Map) baseManipulator);
                } else {
                    Map map = new HashMap();
                    map.put("manipulator", baseManipulator);
                    manipulatorString = generate(map);
                }
                manip.add(manipulatorString);
            }
        }

        String path = StringUtils.join(ObjectUtils.asArray(options.get("path")), ".");
        String size = StringUtils.join(ObjectUtils.asArray(options.get("size")), ".");
        String width = StringUtils.join(ObjectUtils.asArray(options.get("width")), ".");
        String height = StringUtils.join(ObjectUtils.asArray(options.get("height")), ".");
        String crop = (String) options.get("crop");
        String radius = StringUtils.join(ObjectUtils.asArray(options.get("radius")), ".");
        String quality = StringUtils.join(ObjectUtils.asArray(options.get("quality")), ".");
        String backgroundColor = (String) options.get("backgroundColor");
        String shadow = StringUtils.join(ObjectUtils.asArray(options.get("shadow")), ".");
        String shadowOffset = StringUtils.join(ObjectUtils.asArray(options.get("shadowOffset")), ".");
        String frameWidth = StringUtils.join(ObjectUtils.asArray(options.get("frameWidth")), ".");
        String frameColor = (String) options.get("frameColor");
        String frameOffset = StringUtils.join(ObjectUtils.asArray(options.get("frameOffset")), ".");
        String binderRing = StringUtils.join(ObjectUtils.asArray(options.get("binderRing")), ".");
        String binderSpacing = StringUtils.join(ObjectUtils.asArray(options.get("binderSpacing")), ".");
        String binderOffset = StringUtils.join(ObjectUtils.asArray(options.get("binderOffset")), ".");
        String watermark = (String) options.get("watermark");
        String watermarkXPosition = StringUtils.join(ObjectUtils.asArray(options.get("watermarkXPosition")), ".");
        String watermarkYPosition = StringUtils.join(ObjectUtils.asArray(options.get("watermarkYPosition")), ".");
        String watermarkTransparency = StringUtils.join(ObjectUtils.asArray(options.get("watermarkTransparency")), ".");
        String rotate = StringUtils.join(ObjectUtils.asArray(options.get("rotate")), ".");
        String flipX = StringUtils.join(ObjectUtils.asArray(options.get("flipX")), ".");
        String flipY = StringUtils.join(ObjectUtils.asArray(options.get("flipY")), ".");
        String brightness = StringUtils.join(ObjectUtils.asArray(options.get("brightness")), ".");
        String contrast = StringUtils.join(ObjectUtils.asArray(options.get("contrast")), ".");
        String gamma = StringUtils.join(ObjectUtils.asArray(options.get("gamma")), ".");

        SortedMap<String, String> params = new TreeMap<>();
        params.put("p", path);
        params.put("s", size);
        params.put("w", width);
        params.put("h", height);
        params.put("c", crop);
        params.put("r", radius);
        params.put("q", quality);
        params.put("bg", backgroundColor);
        params.put("sh", shadow);
        params.put("so", shadowOffset);
        params.put("fw", frameWidth);
        params.put("fc", frameColor);
        params.put("fo", frameOffset);
        params.put("bd", binderRing);
        params.put("bs", binderSpacing);
        params.put("bo", binderOffset);
        params.put("wm", watermark);
        params.put("wx", watermarkXPosition);
        params.put("wy", watermarkYPosition);
        params.put("wt", watermarkTransparency);
        params.put("rt", rotate);
        params.put("fx", flipX);
        params.put("fy", flipY);
        params.put("bt", brightness);
        params.put("ct", contrast);
        params.put("gm", gamma);
        params.put("m", namedManipulator);

        List<String> components = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (StringUtils.isNotBlank(param.getValue())) {
                components.add(param.getKey() + "_" + param.getValue());
            }
        }
        String raw_manipulator = (String) options.get("raw_manipulator");
        if (raw_manipulator != null) {
            components.add(raw_manipulator);
        }
        if (!components.isEmpty()) {
            manip.add(StringUtils.join(components, ","));
        }

        return StringUtils.join(manip, "/");
    }

    private static List<Map> dup(List<Map> manipulators) {
        List<Map> result = new ArrayList<>();
        for (Map params : manipulators) {
            result.add(new HashMap(params));
        }
        return result;
    }

}
