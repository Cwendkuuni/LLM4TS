import junit.framework.TestCase;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.exceptions.InvalidImageSizeException;

public class ShiftTest {

    @org.junit.Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertNotNull(shift);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertTrue(shift.isWrap());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorInvalidWrap() {
        new Shift(2, 3, 3);
    }

    @org.junit.Test
    public void testApplyGrayImagePositiveOffset() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        assertNotNull(result);
        assertEquals(X, result.X());
        assertEquals(Y, result.Y());
        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                int expected = image.get(x + 2, y + 3);
                int actual = result.get(x, y);
                assertEquals(expected, actual);
            }
        }
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageNegativeWrapXOffset() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(-2, 3, Shift.WRAP);
        GrayImage result = shift.apply(image);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageNegativeWrapYOffset() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(2, -3, Shift.WRAP);
        GrayImage result = shift.apply(image);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageNegativeNoWrapXOffset() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(-2, 3, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageNegativeNoWrapYOffset() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(2, -3, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
    }

    @org.junit.Test
    public void testApplyGrayImageZeroOffset() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(0, 0, Shift.WRAP);
        GrayImage result = shift.apply(image);
        assertNotNull(result);
        assertEquals(X, result.X());
        assertEquals(Y, result.Y());
        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                int expected = image.get(x, y);
                int actual = result.get(x, y);
                assertEquals(expected, actual);
            }
        }
    }

    @org.junit.Test
    public void testApplyRealGrayImagePositiveOffset() {
        int X = 5;
        int Y = 5;
        RealGrayImage image = new RealGrayImage(X, Y);
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        assertNotNull(result);
        assertEquals(X, result.X());
        assertEquals(Y, result.Y());
        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                float expected = image.get(x, y);
                float actual = result.get(x, y);
                assertEquals(expected, actual, 0.01f);
            }
        }
    }

    @org.junit.Test(expected = InvalidImageSizeException.class)
    public void testApplyRealGrayImageNegativeSize() {
        int X = -5;
        int Y = 5;
        RealGrayImage image = new RealGrayImage(X, Y);
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
    }
}