import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        Assert.assertEquals(2, shift.getXoffset());
        Assert.assertEquals(3, shift.getYoffset());
        Assert.assertTrue(shift.isWrap());
    }

    @Test
    public void testShiftApplyGrayImageWithinBounds() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) i);
        }
        GrayImage outputImage = shift.apply(inputImage);
        GrayImage expectedImage = new GrayImage(inputImage.X(), inputImage.Y());
        for (int i = 0; i < expectedImage.X() * expectedImage.Y(); i++) {
            int x = i % expectedImage.X();
            int y = i % expectedImage.Y();
            expectedImage.set(x, y, inputImage.get(x + shift.getXoffset(), y + shift.getYoffset()));
        }
        Assert.assertArrayEquals(expectedImage.getPixels(), outputImage.getPixels());
    }

    @Test
    public void testShiftApplyGrayImageOutOfBoundsX() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) i);
        }
        GrayImage outputImage = shift.apply(inputImage);
        GrayImage expectedImage = new GrayImage(inputImage.X(), inputImage.Y());
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                if (x + shift.getXoffset() < 0 || x + shift.getXoffset() >= expectedImage.X()) {
                    expectedImage.set(x, y, (byte) 0);
                } else {
                    expectedImage.set(x, y, inputImage.get(x + shift.getXoffset(), y + shift.getYoffset()));
                }
            }
        }
        Assert.assertArrayEquals(expectedImage.getPixels(), outputImage.getPixels());
    }

    @Test
    public void testShiftApplyGrayImageOutOfBoundsY() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) i);
        }
        GrayImage outputImage = shift.apply(inputImage);
        GrayImage expectedImage = new GrayImage(inputImage.X(), inputImage.Y());
        for (int x = 0; x < inputImage.X(); x++) {
            for (int y = 0; y < inputImage.Y(); y++) {
                if (y + shift.getYoffset() < 0 || y + shift.getYoffset() >= expectedImage.Y()) {
                    expectedImage.set(x, y, (byte) 0);
                } else {
                    expectedImage.set(x, y, inputImage.get(x, y + shift.getYoffset()));
                }
            }
        }
        Assert.assertArrayEquals(expectedImage.getPixels(), outputImage.getPixels());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftApplyGrayImageOutOfBoundsBoth() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) i);
        }
        shift.apply(inputImage);
    }

    @Test
    public void testShiftApplyRealGrayImageWithinBounds() {
        Shift shift = new Shift(1.5f, 1.5f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), i / 10.0f);
        }
        RealGrayImage outputImage = shift.apply(inputImage);
        RealGrayImage expectedImage = new RealGrayImage(inputImage.X(), inputImage.Y());
        for (int x = 0; x < inputImage.X(); x++) {
            for (int y = 0; y < inputImage.Y(); y++) {
                if (x + shift.getXoffset() < 0 || x + shift.getXoffset() >= expectedImage.X()) {
                    expectedImage.set(x, y, 0.0f);
                } else {
                    expectedImage.set(x, y, inputImage.get(x + shift.getXoffset(), y + shift.getYoffset()));
                }
            }
        }
        Assert.assertEquals(expectedImage, outputImage);
    }

    @Test
    public void testShiftApplyRealGrayImageOutOfBoundsX() {
        Shift shift = new Shift(1.5f, 1.5f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), i / 10.0f);
        }
        RealGrayImage outputImage = shift.apply(inputImage);
        RealGrayImage expectedImage = new RealGrayImage(inputImage.X(), inputImage.Y());
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                if (x + shift.getXoffset() < 0) {
                    expectedImage.set(x, y, 0.0f);
                } else {
                    expectedImage.set(x, y, inputImage.get(x + shift.getXoffset(), y + shift.getYoffset()));
                }
            }
        }
        Assert.assertEquals(expectedImage, outputImage);
    }

    @Test
    public void testShiftApplyRealGrayImageOutOfBoundsY() {
        Shift shift = new Shift(1.5f, 1.5f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), i / 10.0f);
        }
        RealGrayImage outputImage = shift.apply(inputImage);
        RealGrayImage expectedImage = new RealGrayImage(inputImage.X(), inputImage.Y());
        for (int x = 0; x < inputImage.X(); x++) {
            for (int y = 0; y < inputImage.Y(); y++) {
                if (y + shift.getYoffset() < 0) {
                    expectedImage.set(x, y, 0.0f);
                } else {
                    expectedImage.set(x, y, inputImage.get(x, y + shift.getYoffset()));
                }
            }
        }
        Assert.assertEquals(expectedImage, outputImage);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testShiftApplyRealGrayImageOutOfBoundsBoth() {
        Shift shift = new Shift(1.5f, 1.5f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), i / 10.0f);
        }
        shift.apply(inputImage);
    }
}