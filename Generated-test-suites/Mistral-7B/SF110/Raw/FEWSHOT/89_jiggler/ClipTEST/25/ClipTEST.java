package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteImage(5, 5, new Gray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255);
        }

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(10, 200);

        GrayImage result = clip.apply(image);
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.max(10, Math.min(200, image.get(x, y).intValue()));
                assertEquals(expected, result.get(x, y).intValue());
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new ByteImage(5, 5, new RealGray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255 / 255.0f);
        }

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0.1f, 0.9f);

        RealGrayImage result = clip.apply(image);
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.max(0.1f, Math.min(0.9f, image.get(x, y).floatValue()));
                assertEquals(expected, result.get(x, y).floatValue(), 0.001f);
            }
        }
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new ByteImage(5, 5, new Gray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255);
        }

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(10, 200);

        GrayImage result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.max(10, Math.min(200, image.get(x, y).intValue()));
                assertEquals(expected, result.get(x, y).intValue());
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new ByteImage(5, 5, new RealGray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255 / 255.0f);
        }

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0.1f, 0.9f);

        RealGrayImage result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.max(0.1f, Math.min(0.9f, image.get(x, y).floatValue()));
                assertEquals(expected, result.get(x, y).floatValue(), 0.001f);
            }
        }
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new RGBImage(5, 5, new RGB_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.setPixel(i, (byte) i % 255, (byte) (i % 255 + 50), (byte) (i % 255 + 100));
        }

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(10.0f, 200.0f);

        ColorImage result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float r = Math.max(10.0f, Math.min(200.0f, image.getPixel(x, y).r));
                float g = Math.max(10.0f, Math.min(200.0f, image.getPixel(x, y).g));
                float b = Math.max(10.0f, Math.min(200.0f, image.getPixel(x, y).b));
                result.setPixel(x, y, new RGB(r, g, b));
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RGBImage(5, 5, new RealRGB_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.setPixel(i, (i % 255) / 255.0f, (i % 255 + 50.0f) / 255.0f, (i % 255 + 100.0f) / 255.0f);
        }

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(10.0f, 200.0f);

        RealColorImage result = clip.apply(image);
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float r = Math.max(10.0f, Math.min(200.0f, image.getPixel(x, y).r));
                float g = Math.max(10.0f, Math.min(255.0f, image.getPixel(x, y).g));
                float b = Math.max(10.0f, Math.min(255.0f, image.getPixel(x, y).b));
                result.setPixel(x, y, new RealRGB(r, g, b));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithZeroSizeROI() {
        GrayImage image = new ByteImage(5, 5, new Gray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255);
        }

        ROI roi = new ROI(-1, -1, 0, 0);
        Clip clip = new Clip(10, 200);
        clip.apply(image, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNegativeROI() {
        GrayImage image = new ByteImage(5, 5, new Gray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255);
        }

        ROI roi = new ROI(1, 1, -1, -1);
        Clip clip = new Clip(10, 200);
        clip.apply(image, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNonRectangularROI() {
        GrayImage image = new ByteImage(5, 5, new Gray16L_Type());
        for (int i = 0; i < image.X() * image.Y(); i++) {
            image.set(i, i % 255);
        }

        ROI roi = new ROI(0, 0, 3, 4);
        Clip clip = new Clip(10, 200);
        clip.apply(image, roi);
    }
}