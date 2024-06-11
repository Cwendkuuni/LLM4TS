package jigl.image.ops.test;

import org.junit.Test;
import org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(1, 2, 1);
        assertNotNull(shift);
    }

    @Test
    public void testShiftGrayImage() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage input = new GrayImage(5, 5);
        GrayImage expected = new GrayImage(5, 5);
        expected.set(1, 1, (byte) 127);
        input.set(1, 1, (byte) 127);

        GrayImage output = shift.apply(input);
        assertEquals(expected, output);
    }

    // Test constructor with negative arguments
    @Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorNegativeArguments() {
        new Shift(-1, 1, 0);
    }

    // Test constructor with invalid wrap value
    @Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorInvalidWrapValue() {
        new Shift(1, 1, 3);
    }

    // Test apply with negative image dimensions
    @Test(expected = IllegalArgumentException.class)
    public void testShiftNegativeImageDimensions() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage input = new GrayImage(-5, 5);
        assertFalse(shift.apply(input) instanceof GrayImage);
    }

    // Test apply with zero-dimensional image
    @Test
    public void testShiftZeroDimensionalImage() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage input = new GrayImage(0, 0);
        GrayImage output = shift.apply(input);
        assertTrue(output.isEmpty());
    }

    // Test apply with out-of-bounds offsets
    @Test
    public void testShiftOutOfBoundsOffsets() {
        Shift shift = new Shift(5, 5, 0);
        GrayImage input = new GrayImage(10, 10);
        GrayImage output = shift.apply(input);

        assertNotSame(input, output);
        boolean result = false;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x >= 5 && y >= 5 && output.get(x, y) != input.get(x - 5, y - 5)) {
                    result = true;
                    break;
                }
                if (x < 5 && y >= 5 && output.get(x, y) != input.get(x + 5, y - 5)) {
                    result = true;
                    break;
                }
                if (x >= 5 && y < 5 && output.get(x, y) != input.get(x - 5, y + 5)) {
                    result = true;
                    break;
                }
                if (x < 5 && y < 5 && output.get(x, y) != input.get(x + 5, y + 5)) {
                    result = true;
                    break;
                }
            }
            if (result) {
                break;
            }
            result = false;
        }

        assertTrue(result);
    }

    // Test apply with RealGrayImage
    @Test
    public void testShiftRealGrayImage() {
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage input = new RealGrayImage(5, 5);
        RealGrayImage expected = new RealGrayImage(5, 5);
        expected.set(1, 1, 0.5f);
        input.set(1, 1, 0.5f);

        RealGrayImage output = shift.apply(input);
        assertEquals(expected, output, 0.1f);
    }

    // Test apply with ColorImage
    @Test
    public void testShiftColorImage() {
        Shift shift = new Shift(1, 1, 0);
        ColorImage input = new ColorImage(5, 5);
        ColorImage expected = new ColorImage(5, 5);
        Color expectedPixel = new Color(127, 127, 127);
        input.setPixel(1, 1, expectedPixel);

        ColorImage output = shift.apply(input);
        assertEquals(expected, output);
    }

    // Test apply with RealColorImage
    @Test
    public void testShiftRealColorImage() {
        Shift shift = new Shift(1, 1, 0);
        RealColorImage input = new RealColorImage(5, 5);
        RealColorImage expected = new RealColorImage(5, 5);
        RealColor expectedPixel = new RealColor(1.0f, 1.0f, 1.0f);
        input.setPixel(1, 1, expectedPixel);

        RealColorImage output = shift.apply(input);
        assertEquals(expected, output, 0.1f);
    }

    // Test wrap mode
    @Test
    public void testShiftWrapMode() {
        Shift shift = new Shift(2, 2, 1);
        GrayImage input = new GrayImage(5, 5);
        GrayImage expected = new GrayImage(5, 5);
        expected.set(0, 0, (byte) 127);
        input.set(3, 3, (byte) 127);

        GrayImage output = shift.apply(input);
        assertEquals(expected, output);
    }
}

//