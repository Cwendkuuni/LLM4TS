package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {
    @Test
    public void testShiftGrayImage() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage image = new GrayImage(10, 10);
        GrayImage expected = new GrayImage(10, 10);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                expected.set(x, y, image.get(x - xOffset, y - yOffset));
            }
        }
        GrayImage actual = shift.apply(image);
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testShiftRealGrayImage() {
        float xOffset = 2.5f;
        float yOffset = 3.5f;
        Shift shift = new Shift(0, 0, Shift.WRAP);
        RealGrayImage image = new RealGrayImage(10, 10);
        RealGrayImage expected = new RealGrayImage(10, 10);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                expected.set(x, y, image.get(x - (int) xOffset, y - (int) yOffset));
            }
        }
        RealGrayImage actual = shift.apply(image);
        assertArrayEquals(expected.getData(), actual.getData(), 0.01f);
    }

    @Test
    public void testShiftColorImage() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        ColorImage image = new ColorImage(10, 10, new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        ColorImage expected = new ColorImage(image.X() - xOffset, image.Y() - yOffset, image.getPlanes());
        ColorImage actual = shift.apply(image);
        assertArrayEquals(expected.getPlanes(), actual.getPlanes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftWithNegativeOffsets() {
        Shift shift = new Shift(-2, -3, Shift.NOWRAP);
    }

    @Test
    public void testShiftWithOutOfBoundsOffsets() {
        Shift shift = new Shift(12, 12, Shift.NOWRAP);
        GrayImage image = new GrayImage(5, 5);
        GrayImage actual = shift.apply(image);
        assertNotNull(actual);
        assertEquals(0, actual.X());
        assertEquals(0, actual.Y());
    }
}