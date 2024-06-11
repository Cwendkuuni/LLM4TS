package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.types.*;

public class ShiftTest {

    @Test
    public void testShiftGrayImageWithNoWrap() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = 2;
        final int yOffset = 1;

        GrayImage inputImage = new GrayImage(X, Y, new byte[X * Y]);
        GrayImage expectedOutputImage = new GrayImage(X, Y, new byte[X * Y]);

        for (int i = 0; i < X * Y; i++) {
            expectedOutputImage.set(i % X, i % Y, inputImage.get(i % (X + xOffset), i % (Y + yOffset)));
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testShiftGrayImageWithWrapX() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = 2;
        final int yOffset = 1;

        GrayImage inputImage = new GrayImage(X, Y, new byte[X * Y]);

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage outputImage = shift.apply(inputImage);

        GrayImage expectedOutputImage = new GrayImage(X, Y, new byte[X * Y]);

        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                if (x + xOffset >= 0 && x + xOffset < X && y + yOffset >= 0 && y + yOffset < Y) {
                    expectedOutputImage.set(x, y, inputImage.get(x + xOffset, y + yOffset));
                } else {
                    expectedOutputImage.set(x, y, inputImage.get((x + xOffset) % X, (y + yOffset) % Y));
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testShiftGrayImageWithWrapY() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = 1;
        final int yOffset = 2;

        GrayImage inputImage = new GrayImage(X, Y, new byte[X * Y]);

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage outputImage = shift.apply(inputImage);

        GrayImage expectedOutputImage = new GrayImage(X, Y, new byte[X * Y]);

        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                if (x + xOffset >= 0 && x + xOffset < X && y + yOffset >= 0 && y + yOffset < Y) {
                    expectedOutputImage.set(x, y, inputImage.get(x + xOffset, y + yOffset));
                } else {
                    expectedOutputImage.set(x, y, inputImage.get((x + xOffset) % X, (y + yOffset) % Y));
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testShiftGrayImageWithNegativeOffsets() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = -2;
        final int yOffset = -1;

        GrayImage inputImage = new GrayImage(X, Y, new byte[X * Y]);

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(inputImage.getData(), outputImage.getData());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageWithOutOfBoundsOffsets() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = X + 1;
        final int yOffset = Y + 1;

        GrayImage inputImage = new GrayImage(X, Y, new byte[X * Y]);

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(inputImage);
    }

    @Test
    public void testShiftRealGrayImageWithNoWrap() {
        final int X = 5;
        final int Y = 3;
        final float xOffset = 2.5f;
        final float yOffset = 1.5f;

        RealGrayImage inputImage = new RealGrayImage(X, Y, new float[X * Y]);
        RealGrayImage expectedOutputImage = new RealGrayImage(X, Y, new float[X * Y]);

        for (int i = 0; i < X * Y; i++) {
            expectedOutputImage.set(i % X, i % Y, inputImage.get(i % (X + (int) xOffset), i % (Y + (int) yOffset)));
        }

        Shift shift = new Shift((int) xOffset, (int) yOffset, Shift.NOWRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
    }

    @Test
    public void testShiftRealGrayImageWithWrapX() {
        final int X = 5;
        final int Y = 3;
        final float xOffset = 2.5f;
        final float yOffset = 1.5f;

        RealGrayImage inputImage = new RealGrayImage(X, Y, new float[X * Y]);

        Shift shift = new Shift((int) xOffset, (int) yOffset, Shift.WRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        RealGrayImage expectedOutputImage = new RealGrayImage(X, Y, new float[X * Y]);

        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                if (x + (int) xOffset >= 0 && x + (int) xOffset < X && y + (int) yOffset >= 0 && y + (int) yOffset < Y) {
                    expectedOutputImage.set(x, y, inputImage.get(x + (int) xOffset, y + (int) yOffset));
                } else {
                    expectedOutputImage.set(x, y, inputImage.get((int) (x + xOffset) % X, (int) (y + yOffset) % Y));
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
    }

    @Test
    public void testShiftRealGrayImageWithWrapY() {
        final int X = 5;
        final int Y = 3;
        final float xOffset = 1.5f;
        final float yOffset = 2.5f;

        RealGrayImage inputImage = new RealGrayImage(X, Y, new float[X * Y]);

        Shift shift = new Shift((int) xOffset, (int) yOffset, Shift.WRAP);
        RealGrayImage outputImage = shift.apply(inputImage);

        RealGrayImage expectedOutputImage = new RealGrayImage(X, Y, new float[X * Y]);

        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                if (x + (int) xOffset >= 0 && x + (int) xOffset < X && y + (int) yOffset >= 0 && y + (int) yOffset < Y) {
                    expectedOutputImage.set(x, y, inputImage.get(x + (int) xOffset, y + (int) yOffset));
                } else {
                    expectedOutputImage.set(x, y, inputImage.get((int) (x + xOffset) % X, (int) (y + yOffset) % Y));
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
    }

    @Test
    public void testShiftColorImageWithNoWrap() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = 2;
        final int yOffset = 1;

        ColorImage inputImage = new ColorImage(X, Y);
        ColorImage expectedOutputImage = new ColorImage(X, Y);

        for (int channel = 0; channel < 3; channel++) {
            for (int i = 0; i < X * Y; i++) {
                expectedOutputImage.setPixel(i % X, i % Y, inputImage.getPixel(i % (X + xOffset), i % (Y + yOffset)).getChannel(channel));
            }
        }

        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        ColorImage outputImage = shift.apply(inputImage);

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testShiftColorImageWithWrapX() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = 2;
        final int yOffset = 1;

        ColorImage inputImage = new ColorImage(X, Y);

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        ColorImage outputImage = shift.apply(inputImage);

        ColorImage expectedOutputImage = new ColorImage(X, Y);

        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                if (x + xOffset >= 0 && x + xOffset < X && y + yOffset >= 0 && y + yOffset < Y) {
                    expectedOutputImage.setPixel(x, y, inputImage.getPixel(x + xOffset, y + yOffset));
                } else {
                    expectedOutputImage.setPixel(x, y, inputImage.getPixel((x + xOffset) % X, (y + yOffset) % Y));
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testShiftColorImageWithWrapY() {
        final int X = 5;
        final int Y = 3;
        final int xOffset = 1;
        final int yOffset = 2;

        ColorImage inputImage = new ColorImage(X, Y);

        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        ColorImage outputImage = shift.apply(inputImage);

        ColorImage expectedOutputImage = new ColorImage(X, Y);

        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                if (x + xOffset >= 0 && x + xOffset < X && y + yOffset >= 0 && y + yOffset < Y) {
                    expectedOutputImage.setPixel(x, y, inputImage.getPixel(x + xOffset, y + yOffset));
                } else {
                    expectedOutputImage.setPixel(x, y, inputImage.getPixel((x + xOffset) % X, (y + yOffset) % Y));
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }
}