import org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {
    @Test
    public void testApplyGrayImage() {
        final int X = 5;
        final int Y = 3;
        GrayImage originalImage = new GrayImage(X, Y);
        int xOffset = 2;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage expectedImage = new GrayImage(X, Y);
        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                expectedImage.set(x, y, originalImage.get(x + xOffset, y + yOffset));
            }
        }
        GrayImage actualImage = shift.apply(originalImage);
        assertArrayEquals(expectedImage.getPixels(), actualImage.getPixels());
    }

    // Negative x offset test
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeXOffset() {
        final int X = 5;
        final int Y = 3;
        GrayImage originalImage = new GrayImage(X, Y);
        int xOffset = -2;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(originalImage);
    }

    // Negative y offset test
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeYOffset() {
        final int X = 5;
        final int Y = 3;
        GrayImage originalImage = new GrayImage(X, Y);
        int xOffset = 2;
        int yOffset = -1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(originalImage);
    }

    // Out-of-bounds x offset test
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsXOffset() {
        final int X = 5;
        final int Y = 3;
        GrayImage originalImage = new GrayImage(X, Y);
        int xOffset = X + 1;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(originalImage);
    }

    // Out-of-bounds y offset test
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsYOffset() {
        final int X = 5;
        final int Y = 3;
        GrayImage originalImage = new GrayImage(X, Y);
        int xOffset = 2;
        int yOffset = Y + 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(originalImage);
    }

    // Wrap test
    @Test
    public void testWrap() {
        final int X = 5;
        final int Y = 3;
        GrayImage originalImage = new GrayImage(X, Y);
        int xOffset = 2;
        int yOffset = 2;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage expectedImage = new GrayImage(X, Y);
        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                if (x + xOffset >= X) { // x should wrap
                    x = x % X;
                }
                if (y + yOffset >= Y) { // y should wrap
                    y = y % Y;
                }
                expectedImage.set(x, y, originalImage.get(x, y));
            }
        }
        GrayImage actualImage = shift.apply(originalImage);
        assertArrayEquals(expectedImage.getPixels(), actualImage.getPixels());
    }

    // Null image test
    @Test(expected = IllegalArgumentException.class)
    public void testNullImage() {
        GrayImage nullImage = null;
        int xOffset = 2;
        int yOffset = 1;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        shift.apply(nullImage);
    }
}