import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        Assert.assertEquals(2, shift.getXoffset());
        Assert.assertEquals(3, shift.getYoffset());
        Assert.assertTrue(shift.isWrap());

        shift = new Shift(4, 5, Shift.NOWRAP);
        Assert.assertEquals(4, shift.getXoffset());
        Assert.assertEquals(5, shift.getYoffset());
        Assert.assertFalse(shift.isWrap());
    }

    @Test
    public void testApplyGrayImagePositiveOffset() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage input = new GrayImage(10, 10, new byte[10 * 10]);
        GrayImage expectedOutput = new GrayImage(8, 8, new byte[8 * 8]);
        for (int i = 0; i < expectedOutput.size(); i++) {
            expectedOutput.set(i, input.get(i + 2, i + 3));
        }

        GrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test
    public void testApplyGrayImageNegativeXOffset() {
        Shift shift = new Shift(-2, 3, Shift.NOWRAP);
        GrayImage input = new GrayImage(10, 10, new byte[10 * 10]);
        GrayImage expectedOutput = new GrayImage(12, 10, new byte[12 * 10]);

        for (int i = 0; i < expectedOutput.size(); i++) {
            expectedOutput.set(i, input.get(i - 2, i));
        }

        GrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test
    public void testApplyGrayImageNegativeYOffset() {
        Shift shift = new Shift(2, -3, Shift.NOWRAP);
        GrayImage input = new GrayImage(10, 10, new byte[10 * 10]);
        GrayImage expectedOutput = new GrayImage(10, 13, new byte[10 * 13]);

        for (int y = 0; y < 13; y++) {
            for (int x = 0; x < 10; x++) {
                expectedOutput.set(x, y, input.get(x, y + 3));
            }
        }

        GrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testApplyGrayImageOffsetOutOfBounds() {
        Shift shift = new Shift(10, 10, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5, new byte[5 * 5]);
        shift.apply(input);
    }

    @Test
    public void testApplyRealGrayImagePositiveOffset() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(10, 10, new float[10 * 10]);
        RealGrayImage expectedOutput = new RealGrayImage(8, 8, new float[8 * 8]);

        for (int i = 0; i < expectedOutput.size(); i++) {
            expectedOutput.set(i, input.get(i + 2, i + 3));
        }

        RealGrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test
    public void testApplyRealGrayImageNegativeXOffset() {
        Shift shift = new Shift(-2, 3, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(10, 10, new float[10 * 10]);
        RealGrayImage expectedOutput = new RealGrayImage(12, 10, new float[12 * 10]);

        for (int i = 0; i < expectedOutput.size(); i++) {
            expectedOutput.set(i, input.get(i - 2, i));
        }

        RealGrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test
    public void testApplyRealGrayImageNegativeYOffset() {
        Shift shift = new Shift(2, -3, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(10, 10, new float[10 * 10]);
        RealGrayImage expectedOutput = new RealGrayImage(10, 13, new float[10 * 13]);

        for (int y = 0; y < 13; y++) {
            for (int x = 0; x < 10; x++) {
                expectedOutput.set(x, y, input.get(x, y + 3));
            }
        }

        RealGrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testApplyRealGrayImageOffsetOutOfBounds() {
        Shift shift = new Shift(10, 10, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(5, 5, new float[5 * 5]);
        shift.apply(input);
    }

    @Test
    public void testApplyWrapX() {
        Shift shift = new Shift(3, 3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 10, new byte[10 * 10]);
        GrayImage expectedOutput = new GrayImage(10, 10, new byte[10 * 10]);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int newX = (x + 3) % 10;
                int newY = y;
                expectedOutput.set(newX, newY, input.get(x, y));
            }
        }

        GrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test
    public void testApplyWrapY() {
        Shift shift = new Shift(3, 3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 10, new byte[10 * 10]);
        GrayImage expectedOutput = new GrayImage(10, 10, new byte[10 * 10]);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int newX = x;
                int newY = (y + 3) % 10;
                expectedOutput.set(newX, newY, input.get(x, y));
            }
        }

        GrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }

    @Test
    public void testApplyWrapBoth() {
        Shift shift = new Shift(3, 3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 10, new byte[10 * 10]);
        GrayImage expectedOutput = new GrayImage(10, 10, new byte[10 * 10]);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int newX = (x + 3) % 10;
                int newY = (y + 3) % 10;
                expectedOutput.set(newX, newY, input.get(x, y));
            }
        }

        GrayImage output = shift.apply(input);
        Assert.assertSame(expectedOutput, output);
    }
}