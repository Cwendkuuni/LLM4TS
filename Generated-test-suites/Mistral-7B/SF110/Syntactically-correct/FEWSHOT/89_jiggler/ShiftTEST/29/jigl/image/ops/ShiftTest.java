package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftWithNoWrap() {
        final int X = 5;
        final int Y = 5;
        final int xoffset = 2;
        final int yoffset = 3;

        GrayImage image = new GrayImage(X, Y);
        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                image.set(x, y, (byte) x * y);
            }
        }

        Shift shift = new Shift(xoffset, yoffset, Shift.NOWRAP);
        GrayImage shifted = shift.apply(image);

        assertArrayEquals(new int[]{-2, -3, -1, 4, 5}, shifted.getRow(0));
        assertArrayEquals(new int[]{2, -2, 3, 4, 5}, shifted.getRow(1));
        assertArrayEquals(new int[]{1, 3, 2, 4, 5}, shifted.getRow(2));
        assertArrayEquals(new int[]{5, 5, 5, 4, 3}, shifted.getRow(3));
        assertArrayEquals(new int[]{3, 2, 1, 4, 5}, shifted.getRow(4));
    }

    @Test
    public void testShiftWithWrapXPositive() {
        final int X = 5;
        final int Y = 5;
        final int xoffset = 3;
        final int yoffset = 3;

        GrayImage image = new GrayImage(X, Y);
        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                image.set(x, y, (byte) x * y);
            }
        }

        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        assertArrayEquals(new int[]{2, 4, 1, 5, 3}, shifted.getRow(0));
        assertArrayEquals(new int[]{3, 5, 2, 1, 4}, shifted.getRow(1));
        assertArrayEquals(new int[]{4, 1, 3, 5, 2}, shifted.getRow(2));
        assertArrayEquals(new int[]{5, 3, 4, 1, 2}, shifted.getRow(3));
        assertArrayEquals(new int[]{3, 5, 4, 1, 2}, shifted.getRow(4));
    }

    @Test
    public void testShiftWithWrapXNegative() {
        final int X = 5;
        final int Y = 5;
        final int xoffset = -3;
        final int yoffset = 3;

        GrayImage image = new GrayImage(X, Y);
        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                image.set(x, y, (byte) x * y);
            }
        }

        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        assertArrayEquals(new int[]{3, 2, 4, 5, 1}, shifted.getRow(0));
        assertArrayEquals(new int[]{5, 1, 2, 4, 3}, shifted.getRow(1));
        assertArrayEquals(new int[]{4, 3, 5, 1, 2}, shifted.getRow(2));
        assertArrayEquals(new int[]{1, 5, 4, 3, 2}, shifted.getRow(3));
        assertArrayEquals(new int[]{2, 4, 1, 5, 3}, shifted.getRow(4));
    }

    @Test
    public void testShiftWithWrapYPositive() {
        final int X = 5;
        final int Y = 5;
        final int xoffset = 3;
        final int yoffset = 3;

        GrayImage image = new GrayImage(X, Y);
        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                image.set(x, y, (byte) x * y);
            }
        }

        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        assertArrayEquals(new int[]{3, 5, 2, 1, 4}, shifted.getRow(0));
        assertArrayEquals(new int[]{1, 2, 3, 5, 4}, shifted.getRow(1));
        assertArrayEquals(new int[]{4, 1, 2, 3, 5}, shifted.getRow(2));
        assertArrayEquals(new int[]{5, 4, 1, 2, 3}, shifted.getRow(3));
        assertArrayEquals(new int[]{2, 3, 4, 1, 5}, shifted.getRow(4));
    }

    @Test
    public void testShiftWithWrapYNegative() {
        final int X = 5;
        final int Y = 5;
        final int xoffset = 3;
        final int yoffset = -3;

        GrayImage image = new GrayImage(X, Y);
        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                image.set(x, y, (byte) x * y);
            }
        }

        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);
        GrayImage shifted = shift.apply(image);

        assertArrayEquals(new int[]{5, 4, 1, 2, 3}, shifted.getRow(0));
        assertArrayEquals(new int[]{3, 4, 1, 2, 5}, shifted.getRow(1));
        assertArrayEquals(new int[]{2, 3, 4, 1, 5}, shifted.getRow(2));
        assertArrayEquals(new int[]{1, 3, 4, 2, 4}, shifted.getRow(3));
        assertArrayEquals(new int[]{4, 1, 2, 3, 3}, shifted.getRow(4));
    }

    @Test
    public void testShiftOutOfBounds() {
        final int X = 5;
        final int Y = 5;
        final int xoffset = 10;
        final int yoffset = 10;

        GrayImage image = new GrayImage(X, Y);

        Shift shift = new Shift(xoffset, yoffset, Shift.NOWRAP);
        assertNull(shift.apply(image));

        shift = new Shift(xoffset, yoffset, Shift.WRAP);
        GrayImage shifted = shift.apply(image);
        assertNull(shifted);
    }
}