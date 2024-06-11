package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import jigl.image.types.*;
import jigl.image.exceptions.*;

public class ShiftTest {

    @Test
    public void testShiftGrayImageWithZeroOffsetsAndNoWrap() {
        Shift shift = new Shift(0, 0, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expectedOutputImage.set(i, j, inputImage.get(i, j));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithPositiveXOffsetAndNoWrap() {
        Shift shift = new Shift(3, 0, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ((i + 3) >= 0 && (i + 3) < 5 && (j) >= 0 && (j) < 5) {
                    expectedOutputImage.set(i, j, inputImage.get(i + 3, j));
                } else {
                    expectedOutputImage.set(i, j, (byte) 0);
                }
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithPositiveYOffsetAndNoWrap() {
        Shift shift = new Shift(0, 3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ((i) >= 0 && (i) < 5 && (j + 3) >= 0 && (j + 3) < 5) {
                    expectedOutputImage.set(i, j, inputImage.get(i, j + 3));
                } else {
                    expectedOutputImage.set(i, j, (byte) 0);
                }
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithNegativeXOffsetAndNoWrap() {
        Shift shift = new Shift(-3, 0, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ((i - 3) >= 0 && (i - 3) < 5 && (j) >= 0 && (j) < 5) {
                    expectedOutputImage.set(i, j, inputImage.get(i - 3, j));
                } else {
                    expectedOutputImage.set(i, j, (byte) 0);
                }
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithNegativeYOffsetAndNoWrap() {
        Shift shift = new Shift(0, -3, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ((i) >= 0 && (i) < 5 && (j - 3) >= 0 && (j - 3) < 5) {
                    expectedOutputImage.set(i, j, inputImage.get(i, j - 3));
                } else {
                    expectedOutputImage.set(i, j, (byte) 0);
                }
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithPositiveXOffsetAndWrap() {
        Shift shift = new Shift(3, 0, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = (i + 3) % 5;
                int y = j;
                expectedOutputImage.set(x, y, inputImage.get(i, j));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithPositiveYOffsetAndWrap() {
        Shift shift = new Shift(0, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = i;
                int y = (j + 3) % 5;
                expectedOutputImage.set(x, y, inputImage.get(i, j));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithNegativeXOffsetAndWrap() {
        Shift shift = new Shift(-3, 0, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = ((i - 3) + 5) % 5;
                int y = j;
                expectedOutputImage.set(x, y, inputImage.get(i, j));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test
    public void testShiftGrayImageWithNegativeYOffsetAndWrap() {
        Shift shift = new Shift(0, -3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = i;
                int y = ((j - 3) + 5) % 5;
                expectedOutputImage.set(x, y, inputImage.get(i, j));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertArrayEquals(expectedOutputImage.toArray(), outputImage.toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageWithInvalidOffsetsAndNoWrap() {
        Shift shift = new Shift(5, 5, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertNull(outputImage);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageWithInvalidOffsetsAndWrap() {
        Shift shift = new Shift(5, 5, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                inputImage.set(i, j, (byte) 127);
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertNull(outputImage);
    }
}