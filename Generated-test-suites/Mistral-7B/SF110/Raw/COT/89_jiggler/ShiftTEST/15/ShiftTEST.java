package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.Shift;

import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertTrue(shift.isWrap());
    }

    @Test
    public void testApplyGrayImage_NoWrap() {
        int width = 5, height = 5, xoffset = 2, yoffset = 3;
        GrayImage image = new GrayImage(width, height);
        Shift shift = new Shift(xoffset, yoffset, Shift.NOWRAP);

        for (int i = 0; i < width * height; i++) {
            image.set(i % width, i % height, (byte) i % 256);
        }

        GrayImage output = shift.apply(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int expected = (x + xoffset) % width;
                int actual = output.get(x, y);
                assertEquals(expected, actual);
                expected += yoffset;
                expected = (expected % width) * height + y;
                assertEquals(image.get(expected), actual);
            }
        }
    }

    @Test
    public void testApplyGrayImage_Wrap() {
        int width = 5, height = 5, xoffset = 2, yoffset = 3;
        GrayImage image = new GrayImage(width, height);
        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);

        for (int i = 0; i < width * height; i++) {
            image.set(i % width, i % height, (byte) i % 256);
        }

        GrayImage output = shift.apply(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int expectedX = (x + xoffset) % width;
                int expectedY = (y + yoffset) % height;
                int actual = output.get(x, y);
                assertEquals(image.get(expectedX, expectedY), actual);
            }
        }
    }

    @Test(expected = ImageException.class)
    public void testApplyGrayImage_InvalidOffsets() {
        int width = 5, height = 5;
        GrayImage image = new GrayImage(width, height);
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        shift.apply(image);
        fail("Expected ImageException not thrown");
    }

    @Test
    public void testApplyRealGrayImage_NoWrap() {
        int width = 5, height = 5, xoffset = 2.5f, yoffset = 3.5f;
        RealGrayImage image = new RealGrayImage(width, height);
        Shift shift = new Shift(xoffset, yoffset, Shift.NOWRAP);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.set(x, y, x % 256 / 255f);
            }
        }

        RealGrayImage output = shift.apply(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float expected = (x + xoffset) % width;
                float actual = output.get(x, y);
                assertEquals(expected, actual, 0.01f);
                expected += yoffset;
                expected = (expected % width) * height + y;
                assertEquals(image.get(expected), actual, 0.01f);
            }
        }
    }

    @Test
    public void testApplyRealGrayImage_Wrap() {
        int width = 5, height = 5, xoffset = 2.5f, yoffset = 3.5f;
        RealGrayImage image = new RealGrayImage(width, height);
        Shift shift = new Shift(xoffset, yoffset, Shift.WRAP);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.set(x, y, x % 256 / 255f);
            }
        }

        RealGrayImage output = shift.apply(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float expectedX = (x + xoffset) % width;
                float expectedY = (y + yoffset) % height;
                float actual = output.get(x, y);
                assertEquals(image.get(expectedX, expectedY), actual, 0.01f);
            }
        }
    }

    @Test(expected = ImageException.class)
    public void testApplyRealGrayImage_InvalidOffsets() {
        int width = 5, height = 5;
        RealGrayImage image = new RealGrayImage(width, height);
        Shift shift = new Shift(2.5f, 3.5f, Shift.NOWRAP);
        shift.apply(image);
        fail("Expected ImageException not thrown");
    }
}