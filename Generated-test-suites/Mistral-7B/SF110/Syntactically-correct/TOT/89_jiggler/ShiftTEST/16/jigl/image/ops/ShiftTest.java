package jigl.image.ops.test;

import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.Shift;

import org.junit.Test;

public class ShiftTest {
    @Test
    public void testApplyGrayImagePositiveShift() {
        int xOffset = 1;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage expectedOutputImage = new GrayImage(5, 5);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expectedOutputImage.set(i, j, inputImage.get(i + xOffset, j + yOffset));
            }
        }

        GrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels());
    }

    @Test
    public void testApplyRealGrayImagePositiveShift() {
        int xOffset = 1;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage expectedOutputImage = new RealGrayImage(5, 5);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expectedOutputImage.set(i, j, inputImage.get(i + xOffset, j + yOffset));
            }
        }

        RealGrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels(), 0.001f);
    }

    @Test
    public void testApplyGrayImageNegativeShift() {
        int xOffset = -1;
        int yOffset = -1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage expectedOutputImage = new GrayImage(5, 5);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expectedOutputImage.set(i, j, inputImage.get(i + xOffset, j + yOffset));
            }
        }

        // Adjust for negative offsets
        expectedOutputImage.transpose();
        expectedOutputImage.flipHorizontal();

        GrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageInvalidWrap() {
        int xOffset = 5;
        int yOffset = 5;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);

        shift.apply(inputImage);
    }

    @Test
    public void testApplyRealGrayImageInvalidWrap() {
        int xOffset = 5;
        int yOffset = 5;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);

        shift.apply(inputImage);
    }

    @Test
    public void testApplyColorImagePositiveShift() {
        int xOffset = 1;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(5, 5, new byte[][] {new byte[5 * 3], new byte[5 * 3], new byte[5 * 3]});
        ColorImage expectedOutputImage = new ColorImage(5, 5, new byte[][] {new byte[5 * 3], new byte[5 * 3], new byte[5 * 3]});

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int r = inputImage.getRed(i, j);
                int g = inputImage.getGreen(i, j);
                int b = inputImage.getBlue(i, j);

                expectedOutputImage.setRed(i, j, r);
                expectedOutputImage.setGreen(i, j, g);
                expectedOutputImage.setBlue(i, j, b);

                expectedOutputImage.setRed(i + xOffset, j + yOffset, r);
                expectedOutputImage.setGreen(i + xOffset, j + yOffset, g);
                expectedOutputImage.setBlue(i + xOffset, j + yOffset, b);
            }
        }

        ColorImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels());
    }

    @Test
    public void testApplyRealColorImagePositiveShift() {
        int xOffset = 1;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(5, 5, new float[][] {new float[5 * 3], new float[5 * 3], new float[5 * 3]});
        RealColorImage expectedOutputImage = new RealColorImage(5, 5, new float[][] {new float[5 * 3], new float[5 * 3], new float[5 * 3]});

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float r = inputImage.getRed(i, j);
                float g = inputImage.getGreen(i, j);
                float b = inputImage.getBlue(i, j);

                expectedOutputImage.setRed(i, j, r);
                expectedOutputImage.setGreen(i, j, g);
                expectedOutputImage.setBlue(i, j, b);

                expectedOutputImage.setRed(i + xOffset, j + yOffset, r);
                expectedOutputImage.setGreen(i + xOffset, j + yOffset, g);
                expectedOutputImage.setBlue(i + xOffset, j + yOffset, b);
            }
        }

        RealColorImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels(), 0.001f);
    }

    @Test
    public void testApplyColorImageNegativeShift() {
        int xOffset = -1;
        int yOffset = -1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(5, 5, new byte[][] {new byte[5 * 3], new byte[5 * 3], new byte[5 * 3]});
        ColorImage expectedOutputImage = new ColorImage(5, 5, new byte[][] {new byte[5 * 3], new byte[5 * 3], new byte[5 * 3]});

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int r = inputImage.getRed(i, j);
                int g = inputImage.getGreen(i, j);
                int b = inputImage.getBlue(i, j);

                expectedOutputImage.setRed(i, j, r);
                expectedOutputImage.setGreen(i, j, g);
                expectedOutputImage.setBlue(i, j, b);

                expectedOutputImage.setRed(i - xOffset, j - yOffset, r);
                expectedOutputImage.setGreen(i - xOffset, j - yOffset, g);
                expectedOutputImage.setBlue(i - xOffset, j - yOffset, b);
            }
        }

        ColorImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyColorImageInvalidWrap() {
        int xOffset = 5;
        int yOffset = 5;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(10, 10, new byte[][] {new byte[10 * 3], new byte[10 * 3], new byte[10 * 3]});

        shift.apply(inputImage);
    }

    @Test
    public void testApplyRealColorImageInvalidWrap() {
        int xOffset = 5;
        int yOffset = 5;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, new float[][] {new float[10 * 3], new float[10 * 3], new float[10 * 3]});

        shift.apply(inputImage);
    }

    @Test
    public void testApplyWrap() {
        int xOffset = 5;
        int yOffset = 5;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                expectedOutputImage.set(i, j, inputImage.get((i + xOffset) % 10, (j + yOffset) % 10));
            }
        }

        GrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels());
    }

    @Test
    public void testApplyWrapRealGrayImage() {
        int xOffset = 5;
        int yOffset = 5;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                expectedOutputImage.set(i, j, inputImage.get((i + xOffset) % 10, (j + yOffset) % 10));
            }
        }

        RealGrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.pixels(), outputImage.pixels(), 0.001f);
    }
}