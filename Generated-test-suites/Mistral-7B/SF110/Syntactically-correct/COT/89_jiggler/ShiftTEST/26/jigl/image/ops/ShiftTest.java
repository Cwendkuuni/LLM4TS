import junit.framework.Assert;
import junit.framework.TestCase;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest extends TestCase {

    // Test constructor
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertFalse(shift.isWrap());
    }

    // Test constructor with wrap
    public void testShiftConstructorWithWrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertTrue(shift.isWrap());
    }

    // Test apply method with GrayImage input, no wrap
    public void testApplyGrayImageNoWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutput = new GrayImage(10, 10);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                int actualOutput = shift.apply(inputImage).get(x, y);
                int expectedOutputValue = inputImage.get(x + 2, y + 3);
                assertEquals(expectedOutputValue, actualOutput);
            }
        }
    }

    // Test apply method with GrayImage input, wrap
    public void testApplyGrayImageWrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutput = new GrayImage(10, 10);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                int actualOutput = shift.apply(inputImage).get(x, y);
                int expectedOutputValue;
                if (x + 2 >= 10) {
                    expectedOutputValue = inputImage.get(x % 10, y % 10);
                } else {
                    expectedOutputValue = inputImage.get(x + 2, y % 10);
                }
                if (y + 3 >= 10) {
                    expectedOutputValue = inputImage.get(x % 10, y % 10);
                } else {
                    expectedOutputValue = inputImage.get(x % 10, y + 3);
                }
                assertEquals(expectedOutputValue, actualOutput);
            }
        }
    }

    // Test apply method with RealGrayImage input, no wrap
    public void testApplyRealGrayImageNoWrap() {
        Shift shift = new Shift(2.0f, 3.0f, Shift.NOWRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutput = new RealGrayImage(10, 10);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                float actualOutput = shift.apply(inputImage).get(x, y);
                float expectedOutputValue = inputImage.get(x + 2, y + 3);
                assertEquals(expectedOutputValue, actualOutput, 0.01f);
            }
        }
    }

    // Test apply method with RealGrayImage input, wrap
    public void testApplyRealGrayImageWrap() {
        Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        RealGrayImage expectedOutput = new RealGrayImage(10, 10);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                float actualOutput = shift.apply(inputImage).get(x, y);
                float expectedOutputValue;
                if (x + 2 >= 10) {
                    expectedOutputValue = inputImage.get(x % 10, y % 10);
                } else {
                    expectedOutputValue = inputImage.get(x + 2, y % 10);
                }
                if (y + 3 >= 10) {
                    expectedOutputValue = inputImage.get(x % 10, y % 10);
                } else {
                    expectedOutputValue = inputImage.get(x % 10, y + 3);
                }
                assertEquals(expectedOutputValue, actualOutput, 0.01f);
            }
        }
    }

    // Test apply method with ColorImage input, no wrap
    public void testApplyColorImageNoWrap() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        ColorImage inputImage = new ColorImage(10, 10, 3);
        ColorImage expectedOutput = new ColorImage(10, 10, 3);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                ColorImage actualOutput = shift.apply(inputImage);
                ColorImage expectedOutputImage = new ColorImage(10, 10, 3);
                for (int plane = 0; plane < 3; ++plane) {
                    int actualOutputPlaneValue = actualOutput.plane(plane).get(x, y);
                    int expectedOutputPlaneValue = inputImage.plane(plane).get(x + 2, y + 3);
                    assertEquals(expectedOutputPlaneValue, actualOutputPlaneValue);
                }
            }
        }
    }

    // Test apply method with ColorImage input, wrap
    public void testApplyColorImageWrap() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        ColorImage inputImage = new ColorImage(10, 10, 3);
        ColorImage expectedOutput = new ColorImage(10, 10, 3);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                ColorImage actualOutput = shift.apply(inputImage);
                ColorImage expectedOutputImage = new ColorImage(10, 10, 3);
                for (int plane = 0; plane < 3; ++plane) {
                    int actualOutputPlaneValue;
                    int expectedOutputPlaneValue;
                    if (x + 2 >= 10) {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x % 10, y % 10);
                    } else {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x + 2, y % 10);
                    }
                    if (y + 3 >= 10) {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x % 10, y % 10);
                    } else {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x % 10, y + 3);
                    }
                    actualOutputPlaneValue = actualOutput.plane(plane).get(x, y);
                    assertEquals(expectedOutputPlaneValue, actualOutputPlaneValue);
                }
            }
        }
    }

    // Test apply method with RealColorImage input, no wrap
    public void testApplyRealColorImageNoWrap() {
        Shift shift = new Shift(2.0f, 3.0f, Shift.NOWRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, 3);
        RealColorImage expectedOutput = new RealColorImage(10, 10, 3);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                RealColorImage actualOutput = shift.apply(inputImage);
                RealColorImage expectedOutputImage = new RealColorImage(10, 10, 3);
                for (int plane = 0; plane < 3; ++plane) {
                    float actualOutputPlaneValue = actualOutput.plane(plane).get(x, y);
                    float expectedOutputPlaneValue = inputImage.plane(plane).get(x + 2, y + 3);
                    assertEquals(expectedOutputPlaneValue, actualOutputPlaneValue, 0.01f);
                }
            }
        }
    }

    // Test apply method with RealColorImage input, wrap
    public void testApplyRealColorImageWrap() {
        Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
        RealColorImage inputImage = new RealColorImage(10, 10, 3);
        RealColorImage expectedOutput = new RealColorImage(10, 10, 3);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                RealColorImage actualOutput = shift.apply(inputImage);
                RealColorImage expectedOutputImage = new RealColorImage(10, 10, 3);
                for (int plane = 0; plane < 3; ++plane) {
                    float actualOutputPlaneValue;
                    float expectedOutputPlaneValue;
                    if (x + 2 >= 10) {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x % 10, y % 10);
                    } else {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x + 2, y % 10);
                    }
                    if (y + 3 >= 10) {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x % 10, y % 10);
                    } else {
                        expectedOutputPlaneValue = inputImage.plane(plane).get(x % 10, y + 3);
                    }
                    actualOutputPlaneValue = actualOutput.plane(plane).get(x, y);
                    assertEquals(expectedOutputPlaneValue, actualOutputPlaneValue, 0.01f);
                }
            }
        }
    }

    // Test exception when xoffset and yoffset are negative
    public void testNegativeOffsets() {
        try {
            Shift shift = new Shift(-2, -3, Shift.NOWRAP);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test exception when xoffset is negative and wrap is true
    public void testNegativeOffsetWithWrap() {
        try {
            Shift shift = new Shift(-2, 3, Shift.WRAP);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}