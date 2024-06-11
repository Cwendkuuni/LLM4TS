package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImageWithNoWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage image = new GrayImage(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.set(x, y, (byte) x * y);
            }
        }

        GrayImage shiftedImage = shift.apply(image);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int expected = x + 2 < 10 ? x + 2 : x + 2 % 10;
                int actual = shiftedImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + "). Expected: " + expected + ", Actual: " + actual, expected, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapX() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage image = new GrayImage(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.set(x, y, (byte) x * y);
            }
        }

        GrayImage shiftedImage = shift.apply(image);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int expectedX = x + 2 < 0 ? 10 + (x + 2) % 10 : x + 2;
                int actual = shiftedImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + "). Expected: " + expectedX + ", Actual: " + actual, expectedX, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapY() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage image = new GrayImage(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.set(x, y, (byte) x * y);
            }
        }

        GrayImage shiftedImage = shift.apply(image);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int expectedY = y + 3 < 0 ? 10 + (y + 3) % 10 : y + 3;
                int actual = shiftedImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + "). Expected: " + expectedY + ", Actual: " + actual, expectedY, actual);
            }
        }
    }

    @Test
    public void testShiftGrayImageWithWrapXY() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage image = new GrayImage(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.set(x, y, (byte) x * y);
            }
        }

        GrayImage shiftedImage = shift.apply(image);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                int expectedX = x + 2 < 0 ? 10 + (x + 2) % 10 : x + 2;
                int expectedY = y + 3 < 0 ? 10 + (y + 3) % 10 : y + 3;
                int actual = shiftedImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + "). Expected: (" + expectedX + ", " + expectedY + "), Actual: (" + actual + ")",
                        expectedX, expectedY, actual);
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageWithNegativeOffsets() {
        Shift shift = new Shift(-2, -3, Shift.NOWRAP);
        GrayImage image = new GrayImage(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.set(x, y, (byte) x * y);
            }
        }

        shift.apply(image);
        fail("Expected IndexOutOfBoundsException but didn't get it");
    }
}