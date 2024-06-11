package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.*;
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
    public void testShiftApplyGrayImagePositiveOffsetsWithWrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                input.set(i, j, (byte) i);
            }
        }
        GrayImage output = shift.apply(input);
        byte expectedPixel = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ((i + 2) % 5 == 0 && (j + 3) % 5 == 0) {
                    expectedPixel = (byte) (i + 2);
                }
                assertEquals(expectedPixel, output.get(i, j));
            }
        }
    }

    @Test
    public void testShiftApplyGrayImagePositiveOffsetsWithoutWrap() {
        Shift shift = new Shift(2, 3);
        GrayImage input = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                input.set(i, j, (byte) i);
            }
        }
        GrayImage output = shift.apply(input);
        byte expectedPixel = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + 2 >= 0 && j + 3 >= 0 && i + 2 < 5 && j + 3 < 5) {
                    expectedPixel = (byte) (i + 2);
                }
                else {
                    expectedPixel = 0;
                }
                assertEquals(expectedPixel, output.get(i, j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorNegativeOffsets() {
        new Shift(-2, 3, Shift.WRAP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorNegativeWrap() {
        new Shift(2, -3, Shift.WRAP);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftApplyGrayImageNegativeOffsetsWithWrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                input.set(i, j, (byte) i);
            }
        }
        GrayImage output = shift.apply(input);
        byte expectedPixel = 4;
        assertEquals(expectedPixel, output.get(-1, 2)); // should throw IndexOutOfBoundsException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftApplyGrayImageNegativeOffsetsWithoutWrap() {
        Shift shift = new Shift(2, 3);
        GrayImage input = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                input.set(i, j, (byte) i);
            }
        }
        GrayImage output = shift.apply(input);
        byte expectedPixel = 4;
        assertEquals(expectedPixel, output.get(-1, 2)); // should throw IndexOutOfBoundsException
    }
}