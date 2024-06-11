package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteGrayImage(new int[10][10], 10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5, 7);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max(image.get(x, y).intValue(), 5), 7);
                int actual = image.get(x, y).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testClipIntIntApplyGrayImage() {
        GrayImage image = new ByteGrayImage(new int[10][10], 10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5, 7);

        Image result = clip.apply(image);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max(image.get(x, y).intValue(), 5), 7);
                int actual = ((GrayImage) result).get(x, y).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testClipIntIntApplyRealGrayImage() {
        RealGrayImage image = new FloatGrayImage(new float[10][10], 10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 7.5f);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(image.get(x, y), 5.5f), 7.5f);
                float actual = ((RealGrayImage) result).get(x, y);
                assertEquals(expected, actual, 0.01f);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new FloatGrayImage(new float[10][10], 10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 7.5f);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max(image.get(x, y), 5.5f), 7.5f);
                float actual = image.get(x, y);
                assertEquals(expected, actual, 0.01f);
            }
        }
    }

    @Test
    public void testClipFloatFloatApplyColorImage() {
        ColorImage image = new RGBImage(new int[10][10][3], 10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 7.5f);

        clip.apply(image, roi);

        for (int plane = 0; plane < 3; ++plane) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    float expected = Math.min(Math.max(image.get(x, y, plane).floatValue(), 5.5f), 7.5f);
                    float actual = ((RealGrayImage) image.plane(plane)).get(x, y);
                    assertEquals(expected, actual, 0.01f);
                }
            }
        }
    }

    @Test
    public void testClipFloatFloatApplyRealColorImage() {
        RealColorImage image = new FloatRGBImage(new float[10][10][3], 10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 7.5f);

        Image result = clip.apply(image, roi);

        for (int plane = 0; plane < 3; ++plane) {
            RealGrayImage planeImage = (RealGrayImage) result.plane(plane);
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    float expected = Math.min(Math.max(image.get(x, y, plane), 5.5f), 7.5f);
                    float actual = planeImage.get(x, y);
                    assertEquals(expected, actual, 0.01f);
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNullInputImage() {
        Clip clip = new Clip(5.5f, 7.5f);
        clip.apply(null, new ROI(0, 0, 5, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNullROI() {
        GrayImage image = new ByteGrayImage(new int[10][10], 10, 10);
        Clip clip = new Clip(5, 7);
        clip.apply(image, null);
    }
}