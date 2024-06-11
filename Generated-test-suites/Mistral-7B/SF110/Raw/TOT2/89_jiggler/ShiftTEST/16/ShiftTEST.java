package jigl.image.ops.test;

import jigl.image.*;
import jigl.image.ops.Shift;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, 1);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertEquals(true, shift.isWrap());
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage input = new GrayImage(3, 3);
        Shift shift = new Shift(1, 1, 0);
        GrayImage output = shift.apply(input);

        int[] expected = {1, 2, 2, 2, 1, 2, 2, 2, 1};
        assertArrayEquals(expected, output.getPixels());
    }

    @Test
    public void testApplyNegativeOffsets() {
        GrayImage input = new GrayImage(3, 3);
        Shift shift = new Shift(-1, -1, 0);
        assertArrayEquals(new int[0], shift.apply(input).getPixels()); // Empty image
    }

    @Test
    public void testApplyOutOfImageBoundaries() {
        GrayImage input = new GrayImage(3, 3);
        Shift shift = new Shift(4, 4, 0);
        GrayImage expected = new GrayImage(3, 3);
        expected.fill(0); // Fills the image with zeros as a result of out-of-boundary access
        assertArrayEquals(expected.getPixels(), shift.apply(input).getPixels());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyIncorrectWrapFlag() {
        Shift shift = new Shift(1, 1, 2); // 2 represents an incorrect wrap flag value
        // Should throw IllegalArgumentException
    }
}