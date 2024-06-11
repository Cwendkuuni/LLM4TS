package jigl.image.ops.Shift;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jigl.image.types.*;
import jigl.image.ops.Shift;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3);
        Shift shiftNoWrap = new Shift(2, 3, 0);
        Shift shiftWrap = new Shift(2, 3, 1);

        assertEquals(2, shift.xoffset);
        assertEquals(3, shift.yoffset);
        assertTrue(shift.wrap);

        assertEquals(2, shiftNoWrap.xoffset);
        assertEquals(3, shiftNoWrap.yoffset);
        assertFalse(shiftNoWrap.wrap);

        assertEquals(2, shiftWrap.xoffset);
        assertEquals(3, shiftWrap.yoffset);
        assertTrue(shiftWrap.wrap);
    }

    @Test
    public void testApplyGrayImageWithWrap() {
        GrayImage image = new GrayImage(5, 5, new byte[]{1, 2, 3, 4, 5, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1});
        Shift shift = new Shift(2, 2, 1);
        GrayImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final GrayImage expectedResult = new GrayImage(X, Y, new byte[]{3, 4, 5, 1, 2, 5, 4, 5, 1, 2, 3, 4, 5, 1, 3});

        assertArrayEquals(expectedResult.getData(), result.getData());
    }

    @Test
    public void testApplyGrayImageWithoutWrap() {
        GrayImage image = new GrayImage(5, 5, new byte[]{1, 2, 3, 4, 5, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1});
        Shift shift = new Shift(2, 2);
        GrayImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final GrayImage expectedResult = new GrayImage(X, Y, new byte[]{3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 3, 4, 5, 5, 5});

        assertArrayEquals(expectedResult.getData(), result.getData());
    }

    @Test
    public void testApplyRealGrayImageWithWrap() {
        RealGrayImage image = new RealGrayImage(5, 5, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 2.0f, 3.0f, 4.0f, 5.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 1.0f});
        Shift shift = new Shift(2, 2, 1);
        RealGrayImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final RealGrayImage expectedResult = new RealGrayImage(X, Y, new float[]{3.0f, 4.0f, 5.0f, 1.0f, 2.0f, 5.0f, 4.0f, 5.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 1.0f, 3.0f});

        assertArrayEquals(expectedResult.getData(), result.getData(), 0.01f);
    }

    @Test
    public void testApplyRealGrayImageWithoutWrap() {
        RealGrayImage image = new RealGrayImage(5, 5, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 2.0f, 3.0f, 4.0f, 5.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 1.0f});
        Shift shift = new Shift(2, 2);
        RealGrayImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final RealGrayImage expectedResult = new RealGrayImage(X, Y, new float[]{3.0f, 4.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 3.0f, 4.0f, 5.0f, 5.0f, 5.0f});

        assertArrayEquals(expectedResult.getData(), result.getData(), 0.01f);
    }

    @Test
    public void testApplyColorImageWithWrap() {
        ColorImage image = new ColorImage(5, 5, new byte[]{1, 2, 3, 4, 5}, new byte[]{1, 2, 3, 4, 5}, new byte[]{1, 2, 3, 4, 5});
        Shift shift = new Shift(2, 2, 1);
        ColorImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final ColorImage expectedResult = new ColorImage(X, Y, new byte[]{3, 4, 5, 1, 2}, new byte[]{3, 4, 5, 1, 2}, new byte[]{3, 4, 5, 1, 2});

        assertArrayEquals(expectedResult.plane(0).getData(), result.plane(0).getData());
        assertArrayEquals(expectedResult.plane(1).getData(), result.plane(1).getData());
        assertArrayEquals(expectedResult.plane(2).getData(), result.plane(2).getData());
    }

    @Test
    public void testApplyColorImageWithoutWrap() {
        ColorImage image = new ColorImage(5, 5, new byte[]{1, 2, 3, 4, 5}, new byte[]{1, 2, 3, 4, 5}, new byte[]{1, 2, 3, 4, 5});
        Shift shift = new Shift(2, 2);
        ColorImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final ColorImage expectedResult = new ColorImage(X, Y, new byte[]{3, 4, 5, 5, 5}, new byte[]{5, 5, 5, 5, 5}, new byte[]{5, 5, 5, 5, 5});

        assertArrayEquals(expectedResult.plane(0).getData(), result.plane(0).getData());
        assertArrayEquals(expectedResult.plane(1).getData(), result.plane(1).getData());
        assertArrayEquals(expectedResult.plane(2).getData(), result.plane(2).getData());
    }

    @Test
    public void testApplyRealColorImageWithWrap() {
        RealColorImage image = new RealColorImage(5, 5, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f});
        Shift shift = new Shift(2, 2, 1);
        RealColorImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final RealColorImage expectedResult = new RealColorImage(X, Y, new float[]{3.0f, 4.0f, 5.0f, 1.0f, 2.0f}, new float[]{3.0f, 4.0f, 5.0f, 1.0f, 2.0f}, new float[]{3.0f, 4.0f, 5.0f, 1.0f, 2.0f});

        assertArrayEquals(expectedResult.plane(0).getData(), result.plane(0).getData(), 0.01f);
        assertArrayEquals(expectedResult.plane(1).getData(), result.plane(1).getData(), 0.01f);
        assertArrayEquals(expectedResult.plane(2).getData(), result.plane(2).getData(), 0.01f);
    }

    @Test
    public void testApplyRealColorImageWithoutWrap() {
        RealColorImage image = new RealColorImage(5, 5, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f});
        Shift shift = new Shift(2, 2);
        RealColorImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final RealColorImage expectedResult = new RealColorImage(X, Y, new float[]{3.0f, 4.0f, 5.0f, 5.0f, 5.0f}, new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f});

        assertArrayEquals(expectedResult.plane(0).getData(), result.plane(0).getData(), 0.01f);
        assertArrayEquals(expectedResult.plane(1).getData(), result.plane(1).getData(), 0.01f);
        assertArrayEquals(expectedResult.plane(2).getData(), result.plane(2).getData(), 0.01f);
    }

    @Test
    public void testApplyWithNegativeOffsets() {
        GrayImage image = new GrayImage(5, 5, new byte[]{1, 2, 3, 4, 5, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1});
        Shift shift = new Shift(-2, -2, 1);
        GrayImage result = shift.apply(image);

        final int X = result.X();
        final int Y = result.Y();

        final GrayImage expectedResult = new GrayImage(X, Y, new byte[]{3, 4, 5, 1, 2, 5, 4, 5, 1, 2, 3, 4, 5, 1, 3});

        assertArrayEquals(expectedResult.getData(), result.getData());
    }

    @Test
    public void testApplyWithNullImage() {
        Shift shift = new Shift(2, 2);
        assertNull(shift.apply(null));
    }
}