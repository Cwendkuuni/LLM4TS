package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.Image;

public class ShiftTest {

    @Test
    public void testShiftGrayImagePositiveXAndY() {
        Shift shift = new Shift(2, 3);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage outputImage = shift.apply(inputImage);
        int expectedX = 2;
        int expectedY = 3;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int expectedValue = inputImage.get(i, j);
                int actualValue = outputImage.get(i - expectedX, j - expectedY);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageNegativeXAndY() {
        Shift shift = new Shift(-2, -3);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage outputImage = shift.apply(inputImage);
        int expectedX = 2;
        int expectedY = 3;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int expectedValue = inputImage.get(i, j);
                int actualValue = outputImage.get(i + expectedX, j + expectedY);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapXAndY() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage outputImage = shift.apply(inputImage);
        int expectedX = 2;
        int expectedY = 3;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int expectedValue = inputImage.get(i, j);
                int actualValue = outputImage.get((i + expectedX) % 5, (j + expectedY) % 5);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapXOnly() {
        Shift shift = new Shift(2, 0, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage outputImage = shift.apply(inputImage);
        int expectedX = 2;

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                int expectedValue = inputImage.get(i, j);
                int actualValue = outputImage.get((i + expectedX) % 5, j);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapYOnly() {
        Shift shift = new Shift(0, 2, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage outputImage = shift.apply(inputImage);
        int expectedY = 2;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int expectedValue = inputImage.get(i, j);
                int actualValue = outputImage.get(i, (j + expectedY) % 5);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testShiftGrayImageOutOfBounds() {
        Shift shift = new Shift(5, 5);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage outputImage = shift.apply(inputImage);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertNull("Pixel value should be null at (" + i + ", " + j + ")", outputImage.get(i, j));
            }
        }
    }

    @Test
    public void testShiftRealGrayImagePositiveXAndY() {
        Shift shift = new Shift(2.5f, 3.5f);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage outputImage = shift.apply(inputImage);
        float expectedX = 2.5f;
        float expectedY = 3.5f;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float expectedValue = inputImage.get(i, j);
                float actualValue = outputImage.get(i - expectedX, j - expectedY);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageNegativeXAndY() {
        Shift shift = new Shift(-2.5f, -3.5f);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage outputImage = shift.apply(inputImage);
        float expectedX = 2.5f;
        float expectedY = 3.5f;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float expectedValue = inputImage.get(i, j);
                float actualValue = outputImage.get(i + expectedX, j + expectedY);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWrapXAndY() {
        Shift shift = new Shift(2.5f, 3.5f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage outputImage = shift.apply(inputImage);
        float expectedX = 2.5f;
        float expectedY = 3.5f;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float expectedValue = inputImage.get(i, j);
                float actualValue = outputImage.get((i + expectedX) % 5, (j + expectedY) % 5);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWrapXOnly() {
        Shift shift = new Shift(2.5f, 0.0f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage outputImage = shift.apply(inputImage);
        float expectedX = 2.5f;

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                float expectedValue = inputImage.get(i, j);
                float actualValue = outputImage.get((i + expectedX) % 5, j);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testShiftRealGrayImageWrapYOnly() {
        Shift shift = new Shift(0.0f, 2.5f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage outputImage = shift.apply(inputImage);
        float expectedY = 2.5f;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float expectedValue = inputImage.get(i, j);
                float actualValue = outputImage.get(i, (j + expectedY) % 5);
                assertEquals("Incorrect pixel value at (" + i + ", " + j + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftRealGrayImageOutOfBoundsX() {
        Shift shift = new Shift(5.5f, 3.5f);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        shift.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftRealGrayImageOutOfBoundsY() {
        Shift shift = new Shift(3.5f, 5.5f);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        shift.apply(inputImage);
    }
}