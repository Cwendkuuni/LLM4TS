package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage inputImage = new ByteInterleavedImage(10, 10, ByteType.BYTE);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5, 10);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                inputImage.set(x, y, (byte) Math.random());
            }
        }

        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.min(Math.max((int) Math.random(), 5), 10);
                int actual = ((Integer) outputImage.get(x, y)).intValue();
                assertEquals("x: " + x + ", y: " + y, expected, actual);
            }
        }
    }

    @Test
    public void testClipIntIntWithEmptyImage() {
        GrayImage inputImage = new ByteInterleavedImage(0, 0, ByteType.BYTE);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5, 10);

        Image outputImage = clip.apply(inputImage, roi);

        assertEquals(0, outputImage.width());
        assertEquals(0, outputImage.height());
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage inputImage = new ByteInterleavedFloatImage(10, 10, ByteType.BYTE);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 10.5f);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                inputImage.set(x, y, (float) Math.random());
            }
        }

        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.min(Math.max((float) Math.random(), 5.5f), 10.5f);
                float actual = ((Float) outputImage.get(x, y)).floatValue();
                assertEquals("x: " + x + ", y: " + y, expected, actual, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloatWithEmptyImage() {
        RealGrayImage inputImage = new ByteInterleavedFloatImage(0, 0, ByteType.BYTE);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 10.5f);

        Image outputImage = clip.apply(inputImage, roi);

        assertEquals(0, outputImage.width());
        assertEquals(0, outputImage.height());
    }

    @Test
    public void testClipIntIntWithColorImage() {
        ColorImage inputImage = new RGBImage(10, 10, new byte[][]{
                {(byte) 0, (byte) 0, (byte) 0},
                {(byte) 255, (byte) 255, (byte) 255},
                {(byte) 128, (byte) 128, (byte) 128},
                // ...
        });

        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5, 10);

        Image outputImage = clip.apply(inputImage, roi);

        for (int plane = 0; plane < 3; plane++) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    int expectedR = Math.min(Math.max((int) Math.random(), 5), 10);
                    int expectedG = Math.min(Math.max((int) Math.random(), 5), 10);
                    int expectedB = Math.min(Math.max((int) Math.random(), 5), 10);
                    int actualR = ((Integer) outputImage.getPlane(plane).get(x, y)).intValue();
                    int actualG = ((Integer) outputImage.getPlane(plane).get(x, y)).intValue();
                    int actualB = ((Integer) outputImage.getPlane(plane).get(x, y)).intValue();
                    assertEquals("x: " + x + ", y: " + y + ", plane: " + plane, expectedR, actualR);
                    assertEquals("x: " + x + ", y: " + y + ", plane: " + plane, expectedG, actualG);
                    assertEquals("x: " + x + ", y: " + y + ", plane: " + plane, expectedB, actualB);
                }
            }
        }
    }

    @Test
    public void testClipFloatFloatWithColorImage() {
        ColorImage inputImage = new RGBImage(10, 10, new float[][]{
                {0.0f, 0.0f, 0.0f},
                {1.0f, 1.0f, 1.0f},
                {0.5f, 0.5f, 0.5f},
                // ...
        });

        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(5.5f, 10.5f);

        Image outputImage = clip.apply(inputImage, roi);

        for (int plane = 0; plane < 3; plane++) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    float expectedR = Math.min(Math.max((float) Math.random(), 5.5f), 10.5f);
                    float expectedG = Math.min(Math.max((float) Math.random(), 5.5f), 10.5f);
                    float expectedB = Math.min(Math.max((float) Math.random(), 5.5f), 10.5f);
                    float actualR = ((Float) outputImage.getPlane(plane).get(x, y)).floatValue();
                    float actualG = ((Float) outputImage.getPlane(plane).get(x, y)).floatValue();
                    float actualB = ((Float) outputImage.getPlane(plane).get(x, y)).floatValue();
                    assertEquals("x: " + x + ", y: " + y + ", plane: " + plane, expectedR, actualR, 0.001f);
                    assertEquals("x: " + x + ", y: " + y + ", plane: " + plane, expectedG, actualG, 0.001f);
                    assertEquals("x: " + x + ", y: " + y + ", plane: " + plane, expectedB, actualB, 0.001f);
                }
            }
        }
    }
}