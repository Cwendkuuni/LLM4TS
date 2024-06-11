package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import org.junit.Test;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.Image;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ColorImage;
import jigl.image.ops.RealColorImage;
import jigl.image.ops.ROI;

public class ClipTest {

    @Test
    public void testClipConstructorIntInt() {
        Clip clip = new Clip(1, 2);
        assertNotNull(clip);
        assertEquals(1, clip.int_min);
        assertEquals(2, clip.int_max);
    }

    @Test
    public void testClipConstructorFloatFloat() {
        Clip clip = new Clip(1.0f, 2.0f);
        assertNotNull(clip);
        assertEquals(1, clip.int_min);
        assertEquals(2, clip.int_max);
    }

    @Test
    public void testClipApplyGrayImage() {
        GrayImage image = new GrayImage(new int[5][5], new ImageMetadata());
        Clip clip = new Clip(1, 2);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int value = image.get(x, y).intValue();
                assertEquals(Math.min(Math.max(value, 1), 2), value);
            }
        }
    }

    @Test
    public void testClipApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(new float[5][5], new ImageMetadata());
        Clip clip = new Clip(1.0f, 2.0f);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float value = image.get(x, y).floatValue();
                assertEquals(Math.min(Math.max(value, 1.0f), 2.0f), value, 0.001f);
            }
        }
    }

    @Test
    public void testClipApplyColorImage() {
        ColorImage image = new ColorImage(new byte[5][5][3], new ImageMetadata());
        Clip clip = new Clip(1, 2);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                for (int plane = 0; plane < 3; ++plane) {
                    int value = image.get(x, y, plane).intValue();
                    assertEquals(Math.min(Math.max(value, 1), 2), value);
                }
            }
        }
    }

    @Test
    public void testClipApplyRealColorImage() {
        RealColorImage image = new RealColorImage(new float[5][5][3], new ImageMetadata());
        Clip clip = new Clip(1.0f, 2.0f);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                for (int plane = 0; plane < 3; ++plane) {
                    float value = image.get(x, y, plane).floatValue();
                    assertEquals(Math.min(Math.max(value, 1.0f), 2.0f), value, 0.001f);
                }
            }
        }
    }
}