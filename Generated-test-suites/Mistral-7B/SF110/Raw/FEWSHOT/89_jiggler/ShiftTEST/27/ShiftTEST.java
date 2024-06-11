package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.types.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShift_PositiveXAndY_NoWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage output = shift.apply(input);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                int expectedPixel = input.get(x + 2, y + 3);
                int actualPixel = output.get(x, y);
                assertEquals("Mismatched pixel value at position [" + x + ", " + y + "]", expectedPixel, actualPixel);
            }
        }
    }

    @Test
    public void testShift_NegativeXAndY_NoWrap() {
        Shift shift = new Shift(-2, -3, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage output = shift.apply(input);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                int expectedPixel = input.get(x - 2, y - 3);
                int actualPixel = output.get(x, y);
                assertEquals("Mismatched pixel value at position [" + x + ", " + y + "]", expectedPixel, actualPixel);
            }
        }
    }

    @Test
    public void testShift_PositiveXAndY_Wrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage output = shift.apply(input);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                int expectedPixel = input.get((x + 2) % 5, (y + 3) % 5);
                int actualPixel = output.get(x, y);
                assertEquals("Mismatched pixel value at position [" + x + ", " + y + "]", expectedPixel, actualPixel);
            }
        }
    }

    @Test
    public void testShift_NegativeXAndY_Wrap() {
        Shift shift = new Shift(-2, -3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage output = shift.apply(input);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                int expectedPixel = input.get((x - 2 + 5) % 5, (y - 3 + 5) % 5);
                int actualPixel = output.get(x, y);
                assertEquals("Mismatched pixel value at position [" + x + ", " + y + "]", expectedPixel, actualPixel);
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShift_NegativeX_Wrap_XOutOfBounds() {
        Shift shift = new Shift(-5, 3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        shift.apply(input);
        fail("Expected IndexOutOfBoundsException");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShift_NegativeY_Wrap_YOutOfBounds() {
        Shift shift = new Shift(3, -5, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        shift.apply(input);
        fail("Expected IndexOutOfBoundsException");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShift_NegativeX_NoWrap_XOutOfBounds() {
        Shift shift = new Shift(-5, 3, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        shift.apply(input);
        fail("Expected IndexOutOfBoundsException");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShift_NegativeY_NoWrap_YOutOfBounds() {
        Shift shift = new Shift(3, -5, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        shift.apply(input);
        fail("Expected IndexOutOfBoundsException");
    }
}