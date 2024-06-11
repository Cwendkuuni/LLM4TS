package jigl.image.ops.test;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage expected = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expected.set(i, j, (byte) 100);
            }
        }

        GrayImage output = shift.apply(input);
        assertArrayEquals(expected.getPixels(), output.getPixels());
    }

    @Test
    public void testNegativeOffsets() {
        Shift shift = new Shift(-2, -2, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);

        try {
            GrayImage output = shift.apply(input);
            fail("Expected IllegalArgumentException with negative x and y offsets");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage input = null;

        shift.apply(input);
    }

    @Test
    public void testApplyGrayImageWithWrap() {
        Shift shift = new Shift(2, 2, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage expected = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expected.set(i, j, (byte) 100);
            }
        }

        GrayImage output = shift.apply(input);
        assertArrayEquals(expected.getPixels(), output.getPixels());
    }
}