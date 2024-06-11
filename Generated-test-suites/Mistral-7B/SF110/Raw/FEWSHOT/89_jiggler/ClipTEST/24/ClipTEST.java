package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        int min = 50;
        int max = 150;
        Clip clip = new Clip(min, max);

        GrayImage image = Image.randomGrayImage(10, 10, new GrayType());
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int value = image.get(x, y).intValue();
                assertTrue(value >= min && value <= max);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        float min = 0.1f;
        float max = 0.5f;
        Clip clip = new Clip(min, max);

        RealGrayImage image = Image.randomRealGrayImage(10, 10, new GrayType());
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float value = image.get(x, y).floatValue();
                assertTrue(value >= min && value <= max);
            }
        }
    }

    @Test
    public void testClipIntWithZeroBounds() {
        Clip clip = new Clip(0, 0);

        GrayImage image = Image.randomGrayImage(10, 10, new GrayType());
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int value = image.get(x, y).intValue();
                assertEquals(0, value);
            }
        }
    }

    @Test
    public void testClipFloatWithZeroBounds() {
        Clip clip = new Clip(0.0f, 0.0f);

        RealGrayImage image = Image.randomRealGrayImage(10, 10, new GrayType());
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float value = image.get(x, y).floatValue();
                assertEquals(0.0f, value, 0.001f);
            }
        }
    }

    @Test
    public void testClipColor() {
        int minR = 50, minG = 50, minB = 50;
        int maxR = 150, maxG = 150, maxB = 150;

        Clip clip = new Clip(minR, minG, minB, maxR, maxG, maxB);

        ColorImage image = Image.randomColorImage(10, 10, new RGBType());
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int r = image.get(x, y, 0).intValue();
                int g = image.get(x, y, 1).intValue();
                int b = image.get(x, y, 2).intValue();

                assertTrue(r >= minR && r <= maxR);
                assertTrue(g >= minG && g <= maxG);
                assertTrue(b >= minB && b <= maxB);
            }
        }
    }

    @Test
    public void testClipRealColor() {
        float minR = 0.1f, minG = 0.1f, minB = 0.1f;
        float maxR = 0.5f, maxG = 0.5f, maxB = 0.5f;

        Clip clip = new Clip(minR, minG, minB, maxR, maxG, maxB);

        RealColorImage image = Image.randomRealColorImage(10, 10, new RGBType());
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float r = image.get(x, y, 0).floatValue();
                float g = image.get(x, y, 1).floatValue();
                float b = image.get(x, y, 2).floatValue();

                assertTrue(r >= minR && r <= maxR);
                assertTrue(g >= minG && g <= maxG);
                assertTrue(b >= minB && b <= maxB);
            }
        }
    }
}