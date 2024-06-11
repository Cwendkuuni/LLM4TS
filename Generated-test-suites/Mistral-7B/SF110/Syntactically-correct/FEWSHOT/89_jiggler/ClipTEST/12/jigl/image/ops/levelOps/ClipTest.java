package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteGrayImage(new byte[25 * 25], 25, 25);
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5, 10);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                image.set(x, y, (byte) (x + y));
            }
        }

        Image appliedImage = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expectedValue = Math.max(Math.min(x + y, 10), 5);
                int actualValue = ((ByteGrayImage) appliedImage).get(x, y).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new ByteGrayTypeImage(new byte[25 * 25], 25, 25, new ByteGrayType(0));
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5.5f, 10.5f);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                image.set(x, y, (byte) (x + y));
            }
        }

        Image appliedImage = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expectedValue = Math.max(Math.min(x + y, 10.5f), 5.5f);
                float actualValue = ((ByteGrayTypeImage) appliedImage).get(x, y).getFloatValue();
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipNullImage() {
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5, 10);

        Image appliedImage = clip.apply(null, roi);
        assertNull(appliedImage);
    }

    @Test
    public void testClipNullROI() {
        GrayImage image = new ByteGrayImage(new byte[25 * 25], 25, 25);
        Clip clip = new Clip(5, 10);

        Image appliedImage = clip.apply(image, null);
        assertNull(appliedImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipZeroIntMin() {
        GrayImage image = new ByteGrayImage(new byte[25 * 25], 25, 25);
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(0, 10);

        Image appliedImage = clip.apply(image, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipZeroIntMax() {
        GrayImage image = new ByteGrayImage(new byte[25 * 25], 25, 25);
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5, 0);

        Image appliedImage = clip.apply(image, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipZeroFloatMin() {
        RealGrayImage image = new ByteGrayTypeImage(new byte[25 * 25], 25, 25, new ByteGrayType(0));
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(0.0f, 10.5f);

        Image appliedImage = clip.apply(image, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipZeroFloatMax() {
        RealGrayImage image = new ByteGrayTypeImage(new byte[25 * 25], 25, 25, new ByteGrayType(0));
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5.5f, 0.0f);

        Image appliedImage = clip.apply(image, roi);
    }

    @Test
    public void testClipApplyColorImage() {
        ColorImage image = new RGBImage(new byte[3 * 25 * 25], 25, 25, 3);
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5.0f, 10.0f);

        Image appliedImage = clip.apply(image, roi);

        for (int plane = 0; plane < 3; ++plane) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    float expectedValue = Math.max(Math.min(x + y, 10.0f), 5.0f);
                    for (int channel = 0; channel < 3; ++channel) {
                        float actualValue = ((RGBImage) appliedImage).getPlane(channel).get(x, y).getFloatValue();
                        assertEquals(expectedValue, actualValue, 0.001f);
                    }
                }
            }
        }
    }

    @Test
    public void testClipApplyRealColorImage() {
        RealColorImage image = new FloatRGBTypeImage(new float[3 * 25 * 25], 25, 25, 3);
        ROI roi = new ROI(0, 0, 25, 25);
        Clip clip = new Clip(5.0f, 10.0f);

        Image appliedImage = clip.apply(image, roi);

        for (int plane = 0; plane < 3; ++plane) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    float expectedValue = Math.max(Math.min(x + y, 10.0f), 5.0f);
                    float actualValue = ((FloatRGBTypeImage) appliedImage).getPlane(plane).get(x, y);
                    assertEquals(expectedValue, actualValue, 0.001f);
                }
            }
        }
    }
}