package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImagePositive() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected = image.get(x, y);
                int actual = shifted.get(x - 2, y - 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageNegativeX() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(-2, 3, Shift.NOWRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected = image.get(x, y);
                int actual = shifted.get(x + 2, y - 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageNegativeY() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(2, -3, Shift.NOWRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected = image.get(x, y);
                int actual = shifted.get(x - 2, y + 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapXPositive() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(3, 3, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected;
                if (x < 3) {
                    expected = image.get(x, y);
                } else {
                    expected = image.get(x % 5, y);
                }
                int actual = shifted.get(x - 3, y - 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapXNegative() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(-3, 3, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected;
                if (x >= 2) {
                    expected = image.get(x, y);
                } else {
                    expected = image.get(x + 5, y);
                }
                int actual = shifted.get(x + 3, y - 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapYPositive() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(3, 3, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected;
                if (y < 3) {
                    expected = image.get(x, y);
                } else {
                    expected = image.get(x, y % 5);
                }
                int actual = shifted.get(x - 3, y - 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapYNegative() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(3, -3, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int expected;
                if (y >= 2) {
                    expected = image.get(x, y);
                } else {
                    expected = image.get(x, y + 5);
                }
                int actual = shifted.get(x - 3, y + 3);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ") in shifted image", expected, actual);
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageOutOfBoundsXPositive() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(6, 3, Shift.NOWRAP);
        shift.apply(image);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageOutOfBoundsXNegative() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(-6, 3, Shift.NOWRAP);
        shift.apply(image);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageOutOfBoundsYPositive() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(3, 6, Shift.NOWRAP);
        shift.apply(image);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageOutOfBoundsYNegative() {
        GrayImage image = new GrayImage(5, 5);
        Shift shift = new Shift(3, -6, Shift.NOWRAP);
        shift.apply(image);
    }
}