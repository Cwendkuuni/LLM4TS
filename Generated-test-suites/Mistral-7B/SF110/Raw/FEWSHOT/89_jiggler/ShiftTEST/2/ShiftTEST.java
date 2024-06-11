package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.ImageProcessingException;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;

public class ShiftTest {

    @Test
    public void testShiftGrayImage() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);

        GrayImage inputImage = mock(GrayImage.class);
        when(inputImage.X()).thenReturn(5);
        when(inputImage.Y()).thenReturn(5);

        GrayImage expectedOutput = new GrayImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.set(x, y, (byte) 1);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.set(x, y, (byte) 2);
                } else {
                    expectedOutput.set(x, y, (byte) 0);
                }
            }
        }

        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues());

        shift = new Shift(xOffset, yOffset, Shift.WRAP);

        expectedOutput = new GrayImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.set(x, y, (byte) 3);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.set(x, y, (byte) 1);
                } else {
                    expectedOutput.set(x, y, (byte) 0);
                }
            }
        }

        outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues());
    }

    @Test
    public void testShiftRealGrayImage() {
        int xOffset = 2.5f;
        int yOffset = 3.0f;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);

        RealGrayImage inputImage = mock(RealGrayImage.class);
        when(inputImage.X()).thenReturn(5);
        when(inputImage.Y()).thenReturn(5);

        RealGrayImage expectedOutput = new RealGrayImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.set(x, y, 1.0f);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.set(x, y, 2.0f);
                } else {
                    expectedOutput.set(x, y, 0.0f);
                }
            }
        }

        RealGrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues(), 0.001f);

        shift = new Shift(xOffset, yOffset, Shift.WRAP);

        expectedOutput = new RealGrayImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.set(x, y, 3.0f);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.set(x, y, 1.0f);
                } else {
                    expectedOutput.set(x, y, 0.0f);
                }
            }
        }

        outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues(), 0.001f);
    }

    @Test
    public void testShiftColorImage() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);

        ColorImage inputImage = mock(ColorImage.class);
        when(inputImage.X()).thenReturn(5);
        when(inputImage.Y()).thenReturn(5);

        ColorImage expectedOutput = new ColorImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.setRGB(x, y, 1, 1, 1);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.setRGB(x, y, 2, 2, 2);
                } else {
                    expectedOutput.setRGB(x, y, 0, 0, 0);
                }
            }
        }

        ColorImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues());

        shift = new Shift(xOffset, yOffset, Shift.WRAP);

        expectedOutput = new ColorImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.setRGB(x, y, 3, 3, 3);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.setRGB(x, y, 1, 1, 1);
                } else {
                    expectedOutput.setRGB(x, y, 0, 0, 0);
                }
            }
        }

        outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues());
    }

    @Test
    public void testShiftRealColorImage() {
        float xOffset = 2.5f;
        float yOffset = 3.0f;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);

        RealColorImage inputImage = mock(RealColorImage.class);
        when(inputImage.X()).thenReturn(5);
        when(inputImage.Y()).thenReturn(5);

        RealColorImage expectedOutput = new RealColorImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.setRGB(x, y, 1, 1, 1);
                    expectedOutput.setGray(x, y, 1.0f);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.setRGB(x, y, 2, 2, 2);
                    expectedOutput.setGray(x, y, 2.0f);
                } else {
                    expectedOutput.setRGB(x, y, 0, 0, 0);
                    expectedOutput.setGray(x, y, 0.0f);
                }
            }
        }

        RealColorImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues(), 0.001f);
        assertEquals(expectedOutput.getGrayValue(2, 3), outputImage.getGrayValue(2, 3), 0.001f);

        shift = new Shift(xOffset, yOffset, Shift.WRAP);

        expectedOutput = new RealColorImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x < 2 && y < 3) {
                    expectedOutput.setRGB(x, y, 3, 3, 3);
                    expectedOutput.setGray(x, y, 3.0f);
                } else if (x >= 2 && y >= 3) {
                    expectedOutput.setRGB(x, y, 1, 1, 1);
                    expectedOutput.setGray(x, y, 1.0f);
                } else {
                    expectedOutput.setRGB(x, y, 0, 0, 0);
                    expectedOutput.setGray(x, y, 0.0f);
                }
            }
        }

        outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutput.getPixelValues(), outputImage.getPixelValues(), 0.001f);
        assertEquals(expectedOutput.getGrayValue(2, 3), outputImage.getGrayValue(2, 3), 0.001f);
    }

    @Test(expected = ImageProcessingException.class)
    public void testNegativeOffsets() {
        Shift shift = new Shift(-2, -3, Shift.NOWRAP);
        GrayImage inputImage = mock(GrayImage.class);
        when(inputImage.X()).thenReturn(5);
        when(inputImage.Y()).thenReturn(5);

        shift.apply(inputImage);
    }

    @Test(expected = ImageProcessingException.class)
    public void testNegativeWrapOffsets() {
        Shift shift = new Shift(-2, -3, Shift.WRAP);
        GrayImage inputImage = mock(GrayImage.class);
        when(inputImage.X()).thenReturn(5);
        when(inputImage.Y()).thenReturn(5);

        shift.apply(inputImage);
    }
}