import junit.framework.TestCase;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {

    @org.junit.Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertTrue(shift.isWrap());
    }

    @org.junit.Test
    public void testApplyGrayImagePositiveOffsets() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutput = new GrayImage(8, 8);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                expectedOutput.set(x, y, inputImage.get(x + 2, y + 3));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test
    public void testApplyGrayImageNegativeXOffset() {
        Shift shift = new Shift(-2, 3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutput = new GrayImage(12, 10);
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 10; y++) {
                expectedOutput.set(x, y, inputImage.get(x - 2, y + 3));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test
    public void testApplyGrayImageNegativeYOffset() {
        Shift shift = new Shift(2, -3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutput = new GrayImage(10, 13);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 13; y++) {
                expectedOutput.set(x, y, inputImage.get(x + 2, y - 3));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test
    public void testApplyGrayImageWrapXPositive() {
        Shift shift = new Shift(5, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage expectedOutput = new GrayImage(5, 5);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                expectedOutput.set(x, y, inputImage.get((x + 5) % 5, y + 3));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test
    public void testApplyGrayImageWrapXNegative() {
        Shift shift = new Shift(-5, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage expectedOutput = new GrayImage(5, 5);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                expectedOutput.set(x, y, inputImage.get((x - 5 + 5) % 5, y + 3));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test
    public void testApplyGrayImageWrapYPositive() {
        Shift shift = new Shift(5, 5, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage expectedOutput = new GrayImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                expectedOutput.set(x, y, inputImage.get(x + 5, (y + 5) % 5));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test
    public void testApplyGrayImageWrapYNegative() {
        Shift shift = new Shift(5, -5, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        GrayImage expectedOutput = new GrayImage(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                expectedOutput.set(x, y, inputImage.get(x + 5, (y - 5 + 5) % 5));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutput, outputImage);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageOffsetsOutOfImageBoundsPositive() {
        Shift shift = new Shift(15, 15, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        shift.apply(inputImage);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageOffsetsOutOfImageBoundsNegative() {
        Shift shift = new Shift(-15, -15, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        shift.apply(inputImage);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorWithInvalidWrapValue() {
        Shift shift = new Shift(2, 3, 2);
    }

    // Add similar test cases for other apply methods (RealGrayImage, ColorImage, RealColorImage)
    // Add test cases for edge cases with other input image types and sizes

    private void assertImageEquals(Image image1, Image image2) {
        for (int plane = 0; plane < Image.PLANES; plane++) {
            for (int x = 0; x < image1.X(); x++) {
                for (int y = 0; y < image1.Y(); y++) {
                    assertEquals(image1.get(plane, x, y), image2.get(plane, x, y));
                }
            }
        }
    }
}