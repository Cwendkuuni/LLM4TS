import org.junit.Assert;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.*;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        Assert.assertEquals(2, shift.xoffset);
        Assert.assertEquals(3, shift.yoffset);
        Assert.assertTrue(shift.wrap);
    }

    @Test
    public void testApplyGrayImagePositiveShiftNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        input.set(0, 0, (byte) 1);
        input.set(1, 1, (byte) 2);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, (byte) 2);
        expectedOutput.set(2, 1, (byte) 1);
        GrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyGrayImagePositiveShiftWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        input.set(0, 0, (byte) 1);
        input.set(1, 1, (byte) 2);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(3, 3, (byte) 2);
        expectedOutput.set(4, 3, (byte) 1);
        GrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyGrayImageNegativeShiftNoWrap() {
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        input.set(0, 0, (byte) 1);
        input.set(1, 1, (byte) 2);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(3, 3, (byte) 1);
        expectedOutput.set(4, 3, (byte) 2);
        GrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyGrayImageNegativeShiftWrap() {
        Shift shift = new Shift(-1, -1, Shift.WRAP);
        GrayImage input = new GrayImage(5, 5);
        input.set(0, 0, (byte) 1);
        input.set(1, 1, (byte) 2);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, (byte) 2);
        expectedOutput.set(0, 1, (byte) 1);
        GrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = ImageProcessingException.class)
    public void testApplyGrayImageInvalidShift() {
        Shift shift = new Shift(5, 5, Shift.NOWRAP);
        GrayImage input = new GrayImage(5, 5);
        input.set(0, 0, (byte) 1);
        shift.apply(input);
    }

    @Test
    public void testApplyRealGrayImagePositiveShiftNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(5, 5);
        input.set(0, 0, 1.0f);
        input.set(1, 1, 2.0f);
        RealGrayImage expectedOutput = new RealGrayImage(5, 5);
        expectedOutput.set(1, 1, 2.0f);
        expectedOutput.set(2, 1, 1.0f);
        RealGrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyRealGrayImagePositiveShiftWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(5, 5);
        input.set(0, 0, 1.0f);
        input.set(1, 1, 2.0f);
        RealGrayImage expectedOutput = new RealGrayImage(5, 5);
        expectedOutput.set(3, 3, 2.0f);
        expectedOutput.set(4, 3, 1.0f);
        RealGrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyRealGrayImageNegativeShiftNoWrap() {
        Shift shift = new Shift(-1, -1, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(5, 5);
        input.set(0, 0, 1.0f);
        input.set(1, 1, 2.0f);
        RealGrayImage expectedOutput = new RealGrayImage(5, 5);
        expectedOutput.set(3, 3, 1.0f);
        expectedOutput.set(4, 3, 2.0f);
        RealGrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyRealGrayImageNegativeShiftWrap() {
        Shift shift = new Shift(-1, -1, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(5, 5);
        input.set(0, 0, 1.0f);
        input.set(1, 1, 2.0f);
        RealGrayImage expectedOutput = new RealGrayImage(5, 5);
        expectedOutput.set(1, 1, 2.0f);
        expectedOutput.set(0, 1, 1.0f);
        RealGrayImage output = shift.apply(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = ImageProcessingException.class)
    public void testApplyRealGrayImageInvalidShift() {
        Shift shift = new Shift(5, 5, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(5, 5);
        input.set(0, 0, 1.0f);
        shift.apply(input);
    }

    @Test
    public void testApplyColorImagePositiveShiftNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage input = new ColorImage(5, 5, 3);
        input.setPlane(0, new Plane(5, 5, new byte[]{(byte) 1, (byte) 2, (byte) 3}));
        input.setPlane(1, new Plane(5, 5, new byte[]{(byte) 4, (byte) 5, (byte) 6}));
        input.setPlane(2, new Plane(5, 5, new byte[]{(byte) 7, (byte) 8, (byte) 9}));
        ColorImage expectedOutput = new ColorImage(input.X(), input.Y(), 3);
        ColorImage output = shift.apply(input);
        Assert.assertNotNull(output);
        for (int i = 0; i < 3; i++) {
            Plane planeOutput = output.plane(i);
            Plane planeExpectedOutput = expectedOutput.plane(i);
            for (int x = 0; x < planeExpectedOutput.width(); x++) {
                for (int y = 0; y < planeExpectedOutput.height(); y++) {
                    Assert.assertEquals(planeExpectedOutput.getSample(x, y), planeOutput.getSample(x, y));
                }
            }
        }
    }

    @Test
    public void testApplyColorImagePositiveShiftWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage input = new ColorImage(5, 5, 3);
        input.setPlane(0, new Plane(5, 5, new byte[]{(byte) 1, (byte) 2, (byte) 3}));
        input.setPlane(1, new Plane(5, 5, new byte[]{(byte) 4, (byte) 5, (byte) 6}));
        input.setPlane(2, new Plane(5, 5, new byte[]{(byte) 7, (byte) 8, (byte) 9}));
        ColorImage expectedOutput = new ColorImage(input.X(), input.Y(), 3);
        ColorImage output = shift.apply(input);
        Assert.assertNotNull(output);
        for (int i = 0; i < 3; i++) {
            Plane planeOutput = output.plane(i);
            Plane planeExpectedOutput = expectedOutput.plane(i);
            for (int x = 0; x < planeExpectedOutput.width(); x++) {
                for (int y = 0; y < planeExpectedOutput.height(); y++) {
                    if (x + 1 >= 0 && x + 1 < input.X() && y + 1 >= 0 && y + 1 < input.Y()) {
                        Assert.assertEquals(planeExpectedOutput.getSample(x, y), planeOutput.getSample(x + 1, y + 1));
                    }
                }
            }
        }
    }

    @Test
    public void testApplyRealColorImagePositiveShiftNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage input = new RealColorImage(5, 5, 3);
        input.setPlane(0, new Plane(5, 5, new float[]{1.0f, 2.0f, 3.0f}));
        input.setPlane(1, new Plane(5, 5, new float[]{4.0f, 5.0f, 6.0f}));
        input.setPlane(2, new Plane(5, 5, new float[]{7.0f, 8.0f, 9.0f}));
        RealColorImage expectedOutput = new RealColorImage(input.X(), input.Y(), 3);
        RealColorImage output = shift.apply(input);
        Assert.assertNotNull(output);
        for (int i = 0; i < 3; i++) {
            Plane planeOutput = output.plane(i);
            Plane planeExpectedOutput = expectedOutput.plane(i);
            for (int x = 0; x < planeExpectedOutput.width(); x++) {
                for (int y = 0; y < planeExpectedOutput.height(); y++) {
                    Assert.assertEquals(planeExpectedOutput.getSample(x, y), planeOutput.getSample(x, y), 0.01f);
                }
            }
        }
    }

    @Test
    public void testApplyRealColorImagePositiveShiftWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage input = new RealColorImage(5, 5, 3);
        input.setPlane(0, new Plane(5, 5, new float[]{1.0f, 2.0f, 3.0f}));
        input.setPlane(1, new Plane(5, 5, new float[]{4.0f, 5.0f, 6.0f}));
        input.setPlane(2, new Plane(5, 5, new float[]{7.0f, 8.0f, 9.0f}));
        RealColorImage expectedOutput = new RealColorImage(input.X(), input.Y(), 3);
        RealColorImage output = shift.apply(input);
        Assert.assertNotNull(output);
        for (int i = 0; i < 3; i++) {
            Plane planeOutput = output.plane(i);
            Plane planeExpectedOutput = expectedOutput.plane(i);
            for (int x = 0; x < planeExpectedOutput.width(); x++) {
                for (int y = 0; y < planeExpectedOutput.height(); y++) {
                    if (x + 1 >= 0 && x + 1 < input.X() && y + 1 >= 0 && y + 1 < input.Y()) {
                        Assert.assertEquals(planeExpectedOutput.getSample(x, y), planeOutput.getSample(x + 1, y + 1), 0.01f);
                    }
                }
            }
        }
    }

    @Test(expected = ImageProcessingException.class)
    public void testApplyColorImageInvalidShift() {
        Shift shift = new Shift(5, 5, Shift.NOWRAP);
        ColorImage input = new ColorImage(5, 5, 3);
        shift.apply(input);
    }

    @Test(expected = ImageProcessingException.class)
    public void testApplyRealColorImageInvalidShift() {
        Shift shift = new Shift(5, 5, Shift.NOWRAP);
        RealColorImage input = new RealColorImage(5, 5, 3);
        shift.apply(input);
    }
}