package jigl.image.ops.test;

import jigl.image.*;
import jigl.image.ops.Shift;
import org.junit.Assert;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        Assert.assertEquals(2, shift.getXoffset());
        Assert.assertEquals(3, shift.getYoffset());
        Assert.assertTrue(shift.isWrap());

        shift = new Shift(1, 1, Shift.NOWRAP);
        Assert.assertEquals(1, shift.getXoffset());
        Assert.assertEquals(1, shift.getYoffset());
        Assert.assertFalse(shift.isWrap());
    }

    @Test
    public void testApplyGrayImageWithWrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 1);
        inputImage.set(4, 4, (byte) 2);

        GrayImage outputImage = shift.apply(inputImage);

        // Check pixels at original position
        Assert.assertEquals(inputImage.get(2, 3), outputImage.get(2, 3));

        // Check pixels at shifted position
        Assert.assertEquals(inputImage.get(2 + shift.getXoffset(), 3 + shift.getYoffset()), outputImage.get(2, 3));
        Assert.assertEquals(inputImage.get(2, 3 + shift.getYoffset()), outputImage.get(2, 3));
        Assert.assertEquals(inputImage.get(2 + shift.getXoffset(), 3 + shift.getYoffset()), outputImage.get(2, 3));

        // Check pixels at edge cases
        Assert.assertEquals(inputImage.get(4 + shift.getXoffset() % inputImage.X(), 4 + shift.getYoffset() % inputImage.Y()), outputImage.get(2, 3));
        Assert.assertEquals(inputImage.get(0 + shift.getXoffset() % inputImage.X(), 0 + shift.getYoffset() % inputImage.Y()), outputImage.get(2, 3));
        Assert.assertEquals(inputImage.get(5 + shift.getXoffset() % inputImage.X(), 1 + shift.getYoffset() % inputImage.Y()), outputImage.get(2, 3));
        Assert.assertEquals(inputImage.get(0 + shift.getXoffset() % inputImage.X(), 5 + shift.getYoffset() % inputImage.Y()), outputImage.get(2, 3));
    }

    @Test
    public void testApplyGrayImageWithoutWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 1);
        inputImage.set(4, 4, (byte) 2);

        GrayImage outputImage = shift.apply(inputImage);

        // Check pixels at original position outside the image bounds
        Assert.assertEquals(inputImage.get(2 + shift.getXoffset()), outputImage.get(2 + shift.getXoffset(), 3 + shift.getYoffset()));
        Assert.assertEquals(inputImage.get(2), outputImage.get(2 + shift.getXoffset(), 3 + shift.getYoffset()));
        Assert.assertEquals(inputImage.get(2 + shift.getXoffset()), outputImage.get(2 + shift.getXoffset(), 3 + shift.getYoffset()));

        // Check pixels at edge cases
        Assert.assertEquals(inputImage.get(4), outputImage.get(2 + shift.getXoffset(), 4));
        Assert.assertEquals(inputImage.get(0), outputImage.get(2 + shift.getXoffset(), 0));
        Assert.assertEquals(inputImage.get(5), outputImage.get(2 + shift.getXoffset(), 1));
        Assert.assertEquals(inputImage.get(0), outputImage.get(2 + shift.getXoffset(), 5));

        // Check pixels at out of bounds
        Assert.assertEquals(0, outputImage.get(2 + shift.getXoffset(), 5 + shift.getYoffset()).intValue());
        Assert.assertEquals(0, outputImage.get(5 + shift.getXoffset(), 2 + shift.getYoffset()).intValue());
        Assert.assertEquals(0, outputImage.get(5 + shift.getXoffset(), 5 + shift.getYoffset()).intValue());
        Assert.assertEquals(0, outputImage.get(5 + shift.getXoffset(), 5 + shift.getYoffset()).intValue());
    }

    @Test(expected = ImageSizeMismatchException.class)
    public void testApplyRealGrayImageWithMismatchedSizes() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        RealGrayImage outputImage = new RealGrayImage(3, 3);

        shift.apply(inputImage, outputImage);
    }

    @Test(expected = ShiftOutOfBoundsException.class)
    public void testApplyGrayImageWithNegativeOffsets() {
        Shift shift = new Shift(-2, -3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);

        shift.apply(inputImage);
    }

    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        ColorImage inputImage = new ColorImage(5, 5);
        inputImage.setPlane(0, new GrayImage(5, 5));
        inputImage.setPlane(1, new GrayImage(5, 5));
        inputImage.setPlane(2, new GrayImage(5, 5));

        ColorImage outputImage = shift.apply(inputImage);

        // Check pixels at original position
        // This test assumes that the ColorImage's apply method is implemented correctly
        for (int plane = 0; plane < 3; plane++) {
            Assert.assertEquals(inputImage.plane(plane).get(2, 3), outputImage.plane(plane).get(2, 3));
        }

        // Check pixels at shifted position
        // This test assumes that the ColorImage's apply method is implemented correctly
        for (int plane = 0; plane < 3; plane++) {
            Assert.assertEquals(inputImage.plane(plane).get(2 + shift.getXoffset(), 3 + shift.getYoffset()), outputImage.plane(plane).get(2, 3));
        }

        // Check pixels at edge cases
        // This test assumes that the ColorImage's apply method is implemented correctly
        for (int plane = 0; plane < 3; plane++) {
            Assert.assertEquals(inputImage.plane(plane).get(4 + shift.getXoffset() % inputImage.X(), 4 + shift.getYoffset() % inputImage.Y()), outputImage.plane(plane).get(2, 3));
            Assert.assertEquals(inputImage.plane(plane).get(0 + shift.getXoffset() % inputImage.X(), 0 + shift.getYoffset() % inputImage.Y()), outputImage.plane(plane).get(2, 3));
            Assert.assertEquals(inputImage.plane(plane).get(5 + shift.getXoffset() % inputImage.X(), 1 + shift.getYoffset() % inputImage.Y()), outputImage.plane(plane).get(2, 3));
            Assert.assertEquals(inputImage.plane(plane).get(0 + shift.getXoffset() % inputImage.X(), 5 + shift.getYoffset() % inputImage.Y()), outputImage.plane(plane).get(2, 3));
        }
    }

    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        RealColorImage inputImage = new RealColorImage(5, 5);
        RealColorImage outputImage = new RealColorImage(5, 5);

        shift.apply(inputImage, outputImage);

        // Check pixels at original position
        for (int plane = 0; plane < 3; plane++) {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    Assert.assertEquals(inputImage.getPlane(plane).get(x, y), outputImage.getPlane(plane).get(x, y));
                }
            }
        }

        // Check pixels at shifted position
        for (int plane = 0; plane < 3; plane++) {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    if (x + shift.getXoffset() >= 0 && x + shift.getXoffset() < 5 && y + shift.getYoffset() >= 0 && y + shift.getYoffset() < 5) {
                        Assert.assertEquals(inputImage.getPlane(plane).get(x + shift.getXoffset(), y + shift.getYoffset()), outputImage.getPlane(plane).get(x, y));
                    }
                    else {
                        Assert.assertEquals(Float.valueOf(0.0f), outputImage.getPlane(plane).get(x, y));
                    }
                }
            }
        }

        // Check pixels at edge cases
        for (int plane = 0; plane < 3; plane++) {
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    if (x + shift.getXoffset() >= 0 && x + shift.getXoffset() < 5 && y + shift.getYoffset() >= 0 && y + shift.getYoffset() < 5) {
                        Assert.assertEquals(inputImage.getPlane(plane).get(4 + shift.getXoffset() % 5, 4 + shift.getYoffset() % 5), outputImage.getPlane(plane).get(x, y));
                    }
                    else {
                        Assert.assertEquals(Float.valueOf(0.0f), outputImage.getPlane(plane).get(x, y));
                    }
                }
            }
        }
    }

}