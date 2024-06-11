package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Test;

import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest {

    @Test
    public void testShiftWithNoWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        RealGrayImage expected = new RealGrayImage(5, 5);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = i + 2;
                int y = j + 3;
                if (x >= 0 && x < 5 && y >= 0 && y < 5) {
                    expected.set(i, j, input.get(x, y));
                } else {
                    expected.set(i, j, 0);
                }
            }
        }

        RealGrayImage output = shift.apply(new RealGrayImage(5, 5));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(expected.get(i, j), output.get(i, j), 0.0);
            }
        }
    }

    @Test
    public void testShiftWithWrapX() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage expected = new GrayImage(5, 5);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int newX = x + 2;
                int newY = y + 3;
                if (newX < 5 && newY < 5) {
                    expected.set(newX % 5, newY % 5, input.get(x, y));
                } else {
                    expected.set(newX % 5, newY % 5, (byte) 0);
                }
            }
        }

        GrayImage output = shift.apply(new GrayImage(5, 5));

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                assertEquals(expected.get(x, y), output.get(x, y), 0);
            }
        }
    }

    @Test
    public void testShiftWithWrapXY() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        GrayImage expected = new GrayImage(5, 5);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int newX = x + 2;
                int newY = y + 3;
                if ((newX >= 0 && newX < 5) && (newY >= 0 && newY < 5)) {
                    expected.set(newX % 5, newY % 5, input.get(x, y));
                } else {
                    expected.set(newX % 5, newY % 5, (byte) 0);
                }
            }
        }

        GrayImage output = shift.apply(new GrayImage(5, 5));

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                assertEquals(expected.get(x, y), output.get(x, y), 0);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftWithNegativeOffsets() {
        Shift shift = new Shift(-2, -3, Shift.WRAP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftWithNegativeWrapFlag() {
        Shift shift = new Shift(2, 3, -1);
    }
}