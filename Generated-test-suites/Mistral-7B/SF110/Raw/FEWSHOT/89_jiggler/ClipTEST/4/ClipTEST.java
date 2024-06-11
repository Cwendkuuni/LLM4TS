package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Test;

public class ClipTest {

    @Test
    public void testConstructorWithIntMinIntMax() {
        Clip clip = new Clip(5, 10);
        assertTrue(clip.int_min == 5);
        assertTrue(clip.int_max == 10);
        assertFalse(clip.float_min == 0.0f);
        assertFalse(clip.float_max == 0.0f);
    }

    @Test
    public void testConstructorWithFloatMinFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertTrue(clip.float_min == 2.5f);
        assertTrue(clip.float_max == 7.5f);
        assertFalse(clip.int_min == 0);
        assertFalse(clip.int_max == 0);
    }

    @Test
    public void testApplyGrayImageWithIntMinIntMax() {
        GrayImage grayImage = new GrayImage(new int[1][1], new int[]{1, 1}, new int[]{1, 1}, 1, 1, null);
        Clip clip = new Clip(5, 10);

        assertSame(grayImage, clip.apply(grayImage));

        int[] expectedValues = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        int[] values = new int[grayImage.X() * grayImage.Y()];

        for (int y = 0; y < grayImage.Y(); ++y) {
            for (int x = 0; x < grayImage.X(); ++x) {
                values[y * grayImage.X() + x] = grayImage.get(x, y).intValue();
            }
        }

        clip.apply(grayImage, new ROI(0, 0, grayImage.X() - 1, grayImage.Y() - 1));

        for (int y = 0; y < grayImage.Y(); ++y) {
            for (int x = 0; x < grayImage.X(); ++x) {
                int value = grayImage.get(x, y).intValue();
                assertTrue(value >= 5 && value <= 10);
                assertEquals(value, values[y * grayImage.X() + x]);
            }
        }
    }

    @Test
    public void testApplyGrayImageWithFloatMinFloatMax() {
        GrayImage grayImage = new GrayImage(new int[1][1], new int[]{1, 1}, new int[]{1, 1}, 1, 1, null);
        Clip clip = new Clip(2.5f, 7.5f);

        assertSame(grayImage, clip.apply(grayImage));

        int[] expectedValues = {2, 3, 3, 4, 4, 4, 5, 5, 5, 5};
        int[] values = new int[grayImage.X() * grayImage.Y()];

        for (int y = 0; y < grayImage.Y(); ++y) {
            for (int x = 0; x < grayImage.X(); ++x) {
                values[y * grayImage.X() + x] = grayImage.get(x, y).intValue();
            }
        }

        clip.apply(grayImage, new ROI(0, 0, grayImage.X() - 1, grayImage.Y() - 1));

        for (int y = 0; y < grayImage.Y(); ++y) {
            for (int x = 0; x < grayImage.X(); ++x) {
                int value = grayImage.get(x, y).intValue();
                assertTrue(value >= 2.5f && value <= 7.5f);
                assertEquals(value, values[y * grayImage.X() + x]);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageWithIntMinIntMax() {
        RealGrayImage realGrayImage = new RealGrayImage(new float[]{1.0f, 2.0f, 3.0f}, new int[]{3, 3}, new int[]{1, 1}, 1, 1, null);
        Clip clip = new Clip(5, 10);

        assertSame(realGrayImage, clip.apply(realGrayImage));

        float[] expectedValues = {5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f};
        float[] values = new float[realGrayImage.X() * realGrayImage.Y()];

        for (int y = 0; y < realGrayImage.Y(); ++y) {
            for (int x = 0; x < realGrayImage.X(); ++x) {
                values[y * realGrayImage.X() + x] = realGrayImage.get(x, y);
            }
        }

        clip.apply(realGrayImage, new ROI(0, 0, realGrayImage.X() - 1, realGrayImage.Y() - 1));

        for (int y = 0; y < realGrayImage.Y(); ++y) {
            for (int x = 0; x < realGrayImage.X(); ++x) {
                float value = realGrayImage.get(x, y);
                assertTrue(value >= 5.0f && value <= 10.0f);
                assertEquals(value, values[y * realGrayImage.X() + x], 0.001f);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageWithFloatMinFloatMax() {
        RealGrayImage realGrayImage = new RealGrayImage(new float[]{1.0f, 2.0f, 3.0f}, new int[]{3, 3}, new int[]{1, 1}, 1, 1, null);
        Clip clip = new Clip(2.5f, 7.5f);

        assertSame(realGrayImage, clip.apply(realGrayImage));

        float[] expectedValues = {2.5f, 3.5f, 3.5f, 4.5f, 4.5f, 4.5f, 5.5f, 5.5f, 5.5f, 5.5f};
        float[] values = new float[realGrayImage.X() * realGrayImage.Y()];

        for (int y = 0; y < realGrayImage.Y(); ++y) {
            for (int x = 0; x < realGrayImage.X(); ++x) {
                values[y * realGrayImage.X() + x] = realGrayImage.get(x, y);
            }
        }

        clip.apply(realGrayImage, new ROI(0, 0, realGrayImage.X() - 1, realGrayImage.Y() - 1));

        for (int y = 0; y < realGrayImage.Y(); ++y) {
            for (int x = 0; x < realGrayImage.X(); ++x) {
                float value = realGrayImage.get(x, y);
                assertTrue(value >= 2.5f && value <= 7.5f);
                assertEquals(value, values[y * realGrayImage.X() + x], 0.001f);
            }
        }
    }

    @Test
    public void testApplyColorImageWithIntMinIntMax() {
        ColorImage colorImage = new ColorImage(new int[][]{
                new int[]{1, 1, 1},
                new int[]{2, 2, 2},
                new int[]{3, 3, 3}},
                new int[]{3, 3},
                new int[]{3, 3},
                3, 3, null);

        Clip clip = new Clip(5, 10);

        assertSame(colorImage, clip.apply(colorImage));

        for (int plane = 0; plane < ColorImage.PLANES; ++plane) {
            GrayImage planeImage = (GrayImage) colorImage.plane(plane);
            int[] expectedValues = new int[planeImage.X() * planeImage.Y()];

            for (int y = 0; y < planeImage.Y(); ++y) {
                for (int x = 0; x < planeImage.X(); ++x) {
                    expectedValues[y * planeImage.X() + x] = planeImage.get(x, y).intValue();
                }
            }

            clip.apply(planeImage, new ROI(0, 0, planeImage.X() - 1, planeImage.Y() - 1));

            for (int y = 0; y < planeImage.Y(); ++y) {
                for (int x = 0; x < planeImage.X(); ++x) {
                    int value = planeImage.get(x, y).intValue();
                    assertTrue(value >= 5 && value <= 10);
                    assertEquals(value, expectedValues[y * planeImage.X() + x]);
                }
            }
        }
    }

    @Test
    public void testApplyColorImageWithFloatMinFloatMax() {
        ColorImage colorImage = new ColorImage(new int[][]{
                new int[]{1, 1, 1},
                new int[]{2, 2, 2},
                new int[]{3, 3, 3}},
                new int[]{3, 3},
                new int[]{3, 3},
                3, 3, null);

        Clip clip = new Clip(2.5f, 7.5f);

        assertSame(colorImage, clip.apply(colorImage));

        for (int plane = 0; plane < ColorImage.PLANES; ++plane) {
            RealGrayImage planeImage = (RealGrayImage) colorImage.plane(plane);
            float[] expectedValues = new float[planeImage.X() * planeImage.Y()];

            for (int y = 0; y < planeImage.Y(); ++y) {
                for (int x = 0; x < planeImage.X(); ++x) {
                    expectedValues[y * planeImage.X() + x] = planeImage.get(x, y);
                }
            }

            clip.apply(planeImage, new ROI(0, 0, planeImage.X() - 1, planeImage.Y() - 1));

            for (int y = 0; y < planeImage.Y(); ++y) {
                for (int x = 0; x < planeImage.X(); ++x) {
                    float value = planeImage.get(x, y);
                    assertTrue(value >= 2.5f && value <= 7.5f);
                    assertEquals(value, expectedValues[y * planeImage.X() + x], 0.001f);
                }
            }
        }
    }

    @Test
    public void testMain() {
        String[] argv = new String[4];
        argv[0] = "test";
        argv[1] = "5.5";
        argv[2] = "input.jpg";
        argv[3] = "output.jpg";

        try {
            Clip.main(argv);
        } catch (Exception e) {
            assertNull(e);
        }
    }
}