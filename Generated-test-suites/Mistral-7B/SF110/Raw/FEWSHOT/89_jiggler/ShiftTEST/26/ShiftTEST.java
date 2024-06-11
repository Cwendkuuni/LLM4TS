package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImagePositiveXAndY() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        expectedOutputImage.set(1, 1, inputImage.get(x, y));

        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftGrayImageNegativeXAndY() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        expectedOutputImage.set(8, 7, inputImage.get(x, y));

        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftGrayImagePositiveXAndWrap() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        expectedOutputImage.set(2, 3, inputImage.get(x % 10, y % 10));

        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftGrayImageNegativeXAndWrap() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        expectedOutputImage.set(8, 7, inputImage.get(x % 10, y % 10));

        GrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftGrayImageNegativeXAndYOutOfBounds() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(2, 2);

        shift.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftGrayImagePositiveXAndYOutOfBounds() {
        int x = 2;
        int y = 2;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(2, 2);

        shift.apply(inputImage);
    }

    @Test
    public void testShiftRealGrayImagePositiveXAndY() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10);
        expectedOutputImage.set(1, 1, inputImage.get(x, y));

        RealGrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftRealGrayImageNegativeXAndY() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10);
        expectedOutputImage.set(8, 7, inputImage.get(x, y));

        RealGrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftRealGrayImagePositiveXAndWrap() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10);
        expectedOutputImage.set(2, 3, inputImage.get(x % 10, y % 10));

        RealGrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftRealGrayImageNegativeXAndWrap() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutputImage = new RealGrayImage(10, 10);
        expectedOutputImage.set(8, 7, inputImage.get(x % 10, y % 10));

        RealGrayImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftRealGrayImageNegativeXAndYOutOfBounds() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(2, 2);

        shift.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftRealGrayImagePositiveXAndYOutOfBounds() {
        int x = 2;
        int y = 2;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(2, 2);

        shift.apply(inputImage);
    }

    @Test
    public void testShiftColorImagePositiveXAndY() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        ColorImage expectedOutputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        expectedOutputImage.setPixel(1, 1, inputImage.getPixel(x, y));

        ColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftColorImageNegativeXAndY() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        ColorImage expectedOutputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        expectedOutputImage.setPixel(8, 7, inputImage.getPixel(x, y));

        ColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftColorImagePositiveXAndWrap() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        ColorImage inputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        ColorImage expectedOutputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        expectedOutputImage.setPixel(2, 3, inputImage.getPixel(x % 10, y % 10));

        ColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftColorImageNegativeXAndWrap() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        ColorImage inputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        ColorImage expectedOutputImage = new ColorImage(10, 10, new byte[10 * 10 * 3]);
        expectedOutputImage.setPixel(8, 7, inputImage.getPixel(x % 10, y % 10));

        ColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftColorImageNegativeXAndYOutOfBounds() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(2, 2, new byte[2 * 2 * 3]);

        shift.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftColorImagePositiveXAndYOutOfBounds() {
        int x = 2;
        int y = 2;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(2, 2, new byte[2 * 2 * 3]);

        shift.apply(inputImage);
    }

    @Test
    public void testShiftRealColorImagePositiveXAndY() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        RealColorImage expectedOutputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        expectedOutputImage.setPixel(1, 1, inputImage.getPixel(x, y));

        RealColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftRealColorImageNegativeXAndY() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        RealColorImage expectedOutputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        expectedOutputImage.setPixel(8, 7, inputImage.getPixel(x, y));

        RealColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftRealColorImagePositiveXAndWrap() {
        int x = 2;
        int y = 3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        RealColorImage expectedOutputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        expectedOutputImage.setPixel(2, 3, inputImage.getPixel(x % 10, y % 10));

        RealColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test
    public void testShiftRealColorImageNegativeXAndWrap() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.WRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        RealColorImage expectedOutputImage = new RealColorImage(10, 10, new float[10 * 10 * 3]);
        expectedOutputImage.setPixel(8, 7, inputImage.getPixel(x % 10, y % 10));

        RealColorImage outputImage = shift.apply(inputImage);
        assertImageEquals(expectedOutputImage, outputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftRealColorImageNegativeXAndYOutOfBounds() {
        int x = -2;
        int y = -3;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(2, 2, new float[2 * 2 * 3]);

        shift.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftRealColorImagePositiveXAndYOutOfBounds() {
        int x = 2;
        int y = 2;
        Shift shift = new Shift(x, y, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(2, 2, new float[2 * 2 * 3]);

        shift.apply(inputImage);
    }

    private void assertImageEquals(Image outputImage, Image expectedOutputImage) {
        for (int i = 0; i < outputImage.pixels().length; i++) {
            assertEquals(expectedOutputImage.pixels()[i], outputImage.pixels()[i]);
        }
    }
}