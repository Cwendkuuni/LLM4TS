package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(1, 2, 0);
        assertEquals(1, shift.getXoffset());
        assertEquals(2, shift.getYoffset());
        assertEquals(false, shift.isWrap());
    }

    @Test
    public void testApplyGrayImage() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        GrayImage expectedImage = new GrayImage(X, Y);
        Shift shift = new Shift(1, 1, 0);
        GrayImage resultImage = shift.apply(image);

        for (int y = 0; y < Y; ++y) {
            for (int x = 0; x < X; ++x) {
                assertEquals(image.get(x, y), resultImage.get(x + 1, y + 1));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageNegativeOffsets() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(-1, -1, 0);
        try {
            shift.apply(image);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageEmptyImage() {
        int X = 0;
        int Y = 0;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(1, 1, 0);
        try {
            shift.apply(image);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageNullImage() {
        int X = 5;
        int Y = 5;
        GrayImage image = null;
        Shift shift = new Shift(1, 1, 0);
        try {
            shift.apply(image);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = ImageOutOfBoundsException.class)
    public void testApplyGrayImageLargeWrap() {
        int X = 5;
        int Y = 5;
        GrayImage image = new GrayImage(X, Y);
        Shift shift = new Shift(X + 1, Y + 1, 1);
        try {
            shift.apply(image);
            fail();
        } catch (ImageOutOfBoundsException e) {
            // Expected exception
        }
    }
}