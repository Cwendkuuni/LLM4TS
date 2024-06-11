package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.Shift;

import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImageWithNoWrap() {
        int xOffset = 2;
        int yOffset = 3;

        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int value = i + j;
                expectedOutputImage.set(i, j, value);
            }
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage outputImage = shift.apply(inputImage);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int outputValue = outputImage.get(i, j);
                int expectedValue = expectedOutputImage.get(i - xOffset, j - yOffset);
                assertEquals(expectedValue, outputValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapXPositive() {
        int xOffset = 5;
        int yOffset = 3;

        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int value = i + j;
                expectedOutputImage.set(i, j, value);
            }
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage outputImage = shift.apply(inputImage);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int outputValue = outputImage.get(i, j);
                int expectedValue = expectedOutputImage.get((i + xOffset) % 10, (j + yOffset) % 10);
                assertEquals(expectedValue, outputValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapXNegative() {
        int xOffset = -2;
        int yOffset = 3;

        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int value = i + j;
                expectedOutputImage.set(i, j, value);
            }
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage outputImage = shift.apply(inputImage);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int outputValue = outputImage.get(i, j);
                int expectedValue = expectedOutputImage.get((i + xOffset + 10) % 10, (j + yOffset) % 10);
                assertEquals(expectedValue, outputValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapYPositive() {
        int xOffset = 3;
        int yOffset = 5;

        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int value = i + j;
                expectedOutputImage.set(i, j, value);
            }
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage outputImage = shift.apply(inputImage);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int outputValue = outputImage.get(i, j);
                int expectedValue = expectedOutputImage.get(i, (j + yOffset) % 10);
                assertEquals(expectedValue, outputValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapYNegative() {
        int xOffset = 3;
        int yOffset = -5;

        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int value = i + j;
                expectedOutputImage.set(i, j, value);
            }
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage outputImage = shift.apply(inputImage);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int outputValue = outputImage.get(i, j);
                int expectedValue = expectedOutputImage.get(i, (j + yOffset + 10) % 10);
                assertEquals(expectedValue, outputValue);
            }
        }
    }

    @Test(expected = ImageException.class)
    public void testShiftGrayImageInvalidOffsets() {
        int xOffset = 10;
        int yOffset = 10;

        GrayImage inputImage = new GrayImage(10, 10);

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(inputImage);

        fail("Expected ImageException");
    }

    @Test
    public void testShiftRealGrayImageWithNoWrap() {
        int xOffset = 2;
        int yOffset = 3;

        RealGrayImage inputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double outputValue = outputImage.get(i, j);
                double expectedValue = expectedOutputImage.get(i - xOffset, j - yOffset);
                assertEquals(expectedValue, outputValue, 0.001);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWithWrapXPositive() {
        int xOffset = 5;
        int yOffset = 3;

        RealGrayImage inputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double outputValue = outputImage.get(i, j);
                double expectedValue = expectedOutputImage.get((i + xOffset) % 10, (j + yOffset) % 10);
                assertEquals(expectedValue, outputValue, 0.001);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWithWrapXNegative() {
        int xOffset = -2;
        int yOffset = 3;

        RealGrayImage inputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double outputValue = outputImage.get(i, j);
                double expectedValue = expectedOutputImage.get((i + xOffset + 10) % 10, (j + yOffset) % 10);
                assertEquals(expectedValue, outputValue, 0.001);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWithWrapYPositive() {
        int xOffset = 3;
        int yOffset = 5;

        RealGrayImage inputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double outputValue = outputImage.get(i, j);
                double expectedValue = expectedOutputImage.get(i, (j + yOffset) % 10);
                assertEquals(expectedValue, outputValue, 0.001);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWithWrapYNegative() {
        int xOffset = 3;
        int yOffset = -5;

        RealGrayImage inputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double outputValue = outputImage.get(i, j);
                double expectedValue = expectedOutputImage.get(i, (j + yOffset + 10) % 10);
                assertEquals(expectedValue, outputValue, 0.001);
            }
        }
    }

    @Test(expected = ImageException.class)
    public void testShiftRealGrayImageInvalidOffsets() {
        int xOffset = 10;
        int yOffset = 10;

        RealGrayImage inputImage = new RealGrayImage(10, 10, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(inputImage);

        fail("Expected ImageException");
    }
}