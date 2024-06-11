package jigl.image.ops.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.exceptions.ImageDimensionMismatchException;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertNotNull(shift);
        assertNotNull(shift.xoffset);
        assertNotNull(shift.yoffset);
        assertNotNull(shift.wrap);
        assertNotNull(shift.apply(new GrayImage(10, 10)));
        assertNotNull(shift.apply(new RealGrayImage(10, 10)));
        assertNotNull(shift.apply(new ColorImage(10, 10)));
        assertNotNull(shift.apply(new RealColorImage(10, 10)));
    }

    @Test
    public void testApplyGrayImageWithPositiveOffsetAndNoWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage input = new GrayImage(10, 10);
        GrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                int expectedX = x + 2;
                int expectedY = y + 3;
                if (expectedX < 0) {
                    expectedX += input.X();
                }
                if (expectedY < 0) {
                    expectedY += input.Y();
                }
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY));
            }
        }
    }

    @Test
    public void testApplyGrayImageWithPositiveXOffsetAndPositiveYOffset() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 10);
        GrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                int expectedX = (x + 2) % input.X();
                int expectedY = (y + 3) % input.Y();
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY));
            }
        }
    }

    @Test
    public void testApplyGrayImageWithNegativeXOffsetAndPositiveYOffset() {
        Shift shift = new Shift(-2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 10);
        GrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                int expectedX = ((x - 2) % input.X() + input.X()) % input.X();
                int expectedY = (y + 3) % input.Y();
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY));
            }
        }
    }

    @Test
    public void testApplyGrayImageWithNegativeXOffsetAndNegativeYOffset() {
        Shift shift = new Shift(-2, -3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 10);
        GrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                int expectedX = ((x - 2) % input.X() + input.X()) % input.X();
                int expectedY = ((y - 3) % input.Y() + input.Y()) % input.Y();
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY));
            }
        }
    }

    @Test(expected = ImageDimensionMismatchException.class)
    public void testApplyGrayImageWithNegativeXOffsetAndImageWidthNotDivisibleByXOffset() {
        Shift shift = new Shift(-2, 3, Shift.WRAP);
        GrayImage input = new GrayImage(8, 10);
        shift.apply(input);
    }

    @Test(expected = ImageDimensionMismatchException.class)
    public void testApplyGrayImageWithNegativeYOffsetAndImageHeightNotDivisibleByYOffset() {
        Shift shift = new Shift(2, -3, Shift.WRAP);
        GrayImage input = new GrayImage(10, 8);
        shift.apply(input);
    }

    @Test
    public void testApplyRealGrayImageWithPositiveOffsetAndNoWrap() {
        Shift shift = new Shift(2.5f, 3, Shift.NOWRAP);
        RealGrayImage input = new RealGrayImage(10, 10);
        RealGrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                float expectedX = x + 2.5f;
                float expectedY = y + 3;
                if (expectedX < 0) {
                    expectedX += input.X();
                }
                if (expectedY < 0) {
                    expectedY += input.Y();
                }
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY), 0.001f);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageWithPositiveXOffsetAndPositiveYOffset() {
        Shift shift = new Shift(2.5f, 3.5f, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(10, 10);
        RealGrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                float expectedX = (x + 2.5f) % input.X();
                float expectedY = (y + 3.5f) % input.Y();
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY), 0.001f);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageWithNegativeXOffsetAndPositiveYOffset() {
        Shift shift = new Shift(-2.5f, 3.5f, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(10, 10);
        RealGrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                float expectedX = ((x - 2.5f) % input.X() + input.X()) % input.X();
                float expectedY = (y + 3.5f) % input.Y();
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY), 0.001f);
            }
        }
    }

    @Test
    public void testApplyRealGrayImageWithNegativeXOffsetAndNegativeYOffset() {
        Shift shift = new Shift(-2.5f, -3.5f, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(10, 10);
        RealGrayImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                float expectedX = ((x - 2.5f) % input.X() + input.X()) % input.X();
                float expectedY = ((y - 3.5f) % input.Y() + input.Y()) % input.Y();
                assertArrayEquals(input.get(x, y), output.get(expectedX, expectedY), 0.001f);
            }
        }
    }

    @Test(expected = ImageDimensionMismatchException.class)
    public void testApplyRealGrayImageWithNegativeXOffsetAndImageWidthNotDivisibleByXOffset() {
        Shift shift = new Shift(-2.5f, 3.5f, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(8, 10);
        shift.apply(input);
    }

    @Test(expected = ImageDimensionMismatchException.class)
    public void testApplyRealGrayImageWithNegativeYOffsetAndImageHeightNotDivisibleByYOffset() {
        Shift shift = new Shift(2.5f, -3.5f, Shift.WRAP);
        RealGrayImage input = new RealGrayImage(10, 8);
        shift.apply(input);
    }

    @Test
    public void testApplyColorImage() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        ColorImage input = new ColorImage(10, 10, new byte[3][10 * 10]);
        ColorImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                int r = input.getPlane(0)[y * input.X() + x];
                int g = input.getPlane(1)[y * input.X() + x];
                int b = input.getPlane(2)[y * input.X() + x];

                int expectedX = x + 2;
                int expectedY = y + 3;
                if (expectedX < 0) {
                    expectedX += input.X();
                }
                if (expectedY < 0) {
                    expectedY += input.Y();
                }

                int outputR = output.getPlane(0)[expectedY * output.X() + expectedX];
                int outputG = output.getPlane(1)[expectedY * output.X() + expectedX];
                int outputB = output.getPlane(2)[expectedY * output.X() + expectedX];

                assertArrayEquals(new int[]{r, g, b}, new int[]{outputR, outputG, outputB});
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        Shift shift = new Shift(2.5f, 3.5f, Shift.NOWRAP);
        RealColorImage input = new RealColorImage(10, 10, new float[3][10 * 10]);
        RealColorImage output = shift.apply(input);

        for (int y = 0; y < input.Y(); y++) {
            for (int x = 0; x < input.X(); x++) {
                float r = input.getPlane(0)[y * input.X() + x];
                float g = input.getPlane(1)[y * input.X() + x];
                float b = input.getPlane(2)[y * input.X() + x];

                float expectedX = x + 2.5f;
                float expectedY = y + 3.5f;
                if (expectedX < 0) {
                    expectedX += input.X();
                }
                if (expectedY < 0) {
                    expectedY += input.Y();
                }

                float outputR = output.getPlane(0)[expectedY * output.X() + expectedX];
                float outputG = output.getPlane(1)[expectedY * output.X() + expectedX];
                float outputB = output.getPlane(2)[expectedY * output.X() + expectedX];

                assertArrayEquals(new float[]{r, g, b}, new float[]{outputR, outputG, outputB}, 0.001f);
            }
        }
    }
}