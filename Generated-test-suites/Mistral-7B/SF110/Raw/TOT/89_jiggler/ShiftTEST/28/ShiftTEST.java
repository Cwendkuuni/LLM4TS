package jigl.image.ops;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    @Test
    public void testShiftGrayImage() {
        // Test case for Shift with wrap=true
        GrayImage originalImage = new GrayImage(3, 3);
        originalImage.set(0, 0, (byte) 10);
        originalImage.set(0, 1, (byte) 20);
        originalImage.set(0, 2, (byte) 30);
        originalImage.set(1, 0, (byte) 40);
        originalImage.set(1, 1, (byte) 50);
        originalImage.set(1, 2, (byte) 60);
        originalImage.set(2, 0, (byte) 70);
        originalImage.set(2, 1, (byte) 80);
        originalImage.set(2, 2, (byte) 90);

        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage shiftedImage = shift.apply(originalImage);
        byte[] expectedBytes = new byte[]{
                (byte) 21, (byte) 31, (byte) 41, // First row
                (byte) 32, (byte) 42, (byte) 52, // Second row
                (byte) 43, (byte) 53, (byte) 63, // Third row
                40, 50, 60, // Fourth row
                71, 81, 91, // Fifth row
                82, 92, 102, // Sixth row
                93, 103, 113 // Seventh row
        };
        assertArrayEquals(expectedBytes, shiftedImage.getData());

        // Test case for Shift with wrap=false
        GrayImage originalImage2 = new GrayImage(3, 3);
        originalImage2.set(0, 0, (byte) 10);
        originalImage2.set(0, 1, (byte) 20);
        originalImage2.set(0, 2, (byte) 30);
        originalImage2.set(1, 0, (byte) 40);
        originalImage2.set(1, 1, (byte) 50);
        originalImage2.set(1, 2, (byte) 60);
        originalImage2.set(2, 0, (byte) 70);
        originalImage2.set(2, 1, (byte) 80);
        originalImage2.set(2, 2, (byte) 90);

        Shift shift2 = new Shift(1, 1, Shift.NOWRAP);
        GrayImage shiftedImage2 = shift2.apply(originalImage2);
        byte[] expectedBytes2 = new byte[]{
                (byte) 0, (byte) 0, (byte) 0, // First row
                (byte) 0, (byte) 0, (byte) 0, // Second row
                (byte) 0, (byte) 0, (byte) 0, // Third row
                0, 0, 0, // Fourth row
                71, 81, 91, // Fifth row
                82, 92, 102, // Sixth row
                93, 103, 113 // Seventh row
        };
        assertArrayEquals(expectedBytes2, shiftedImage2.getData());
    }

    @Test(expected = ImageException.class)
    public void testShiftNegativeOffset() {
        // Test case for Shift with negative offsets
        GrayImage originalImage = new GrayImage(3, 3);
        originalImage.set(0, 0, (byte) 10);
        originalImage.set(0, 1, (byte) 20);
        originalImage.set(0, 2, (byte) 30);
        originalImage.set(1, 0, (byte) 40);
        originalImage.set(1, 1, (byte) 50);
        originalImage.set(1, 2, (byte) 60);
        originalImage.set(2, 0, (byte) 70);
        originalImage.set(2, 1, (byte) 80);
        originalImage.set(2, 2, (byte) 90);

        Shift shift = new Shift(-1, -1, Shift.WRAP);
        try {
            shift.apply(originalImage);
            fail("Expected ImageException");
        } catch (ImageException e) {
            // Expected
        }
    }

    @Test
    public void testShiftRealGrayImage() {
        // Given
        RealGrayImage originalImage = new RealGrayImage(3, 3);
        originalImage.set(0, 0, 10.0f);
        originalImage.set(0, 1, 20.0f);
        originalImage.set(0, 2, 30.0f);
        originalImage.set(1, 0, 40.0f);
        originalImage.set(1, 1, 50.0f);
        originalImage.set(1, 2, 60.0f);
        originalImage.set(2, 0, 70.0f);
        originalImage.set(2, 1, 80.0f);
        originalImage.set(2, 2, 90.0f);

        Shift shift = new Shift(1, 1, Shift.WRAP);

        // When
        RealGrayImage shiftedImage = shift.apply(originalImage);

        // Then
        float[] expectedValues = new float[]{
                21.0f, 31.0f, 41.0f, // First row
                32.0f, 42.0f, 52.0f, // Second row
                43.0f, 53.0f, 63.0f, // Third row
                40.0f, 50.0f, 60.0f, // Fourth row
                71.0f, 81.0f, 91.0f, // Fifth row
                82.0f, 92.0f, 102.0f, // Sixth row
                93.0f, 103.0f, 113.0f // Seventh row
        };
        assertArrayEquals(expectedValues, shiftedImage.getData(), 0.001f);

        // Test case for Shift with wrap=false
        RealGrayImage originalImage2 = new RealGrayImage(3, 3);
        originalImage2.set(0, 0, 10.0f);
        originalImage2.set(0, 1, 20.0f);
        originalImage2.set(0, 2, 30.0f);
        originalImage2.set(1, 0, 40.0f);
        originalImage2.set(1, 1, 50.0f);
        originalImage2.set(1, 2, 60.0f);
        originalImage2.set(2, 0, 70.0f);
        originalImage2.set(2, 1, 80.0f);
        originalImage2.set(2, 2, 90.0f);

        Shift shift2 = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage shiftedImage2 = shift2.apply(originalImage2);

        float[] expectedValues2 = new float[]{
                0.0f, 0.0f, 0.0f, // First row
                0.0f, 0.0f, 0.0f, // Second row
                0.0f, 0.0f, 0.0f, // Third row
                0.0f, 0.0f, 0.0f, // Fourth row
                71.0f, 81.0f, 91.0f, // Fifth row
                82.0f, 92.0f, 102.0f, // Sixth row
                93.0f, 103.0f, 113.0f // Seventh row
        };
        assertArrayEquals(expectedValues2, shiftedImage2.getData(), 0.001f);
    }

    @Test(expected = ImageException.class)
    public void testShiftNegativeOffsetRealGrayImage() {
        // Test case for Shift with negative offsets (RealGrayImage)
        RealGrayImage originalImage = new RealGrayImage(3, 3);
        originalImage.set(0, 0, 10.0f);
        originalImage.set(0, 1, 20.0f);
        originalImage.set(0, 2, 30.0f);
        originalImage.set(1, 0, 40.0f);
        originalImage.set(1, 1, 50.0f);
        originalImage.set(1, 2, 60.0f);
        originalImage.set(2, 0, 70.0f);
        originalImage.set(2, 1, 80.0f);
        originalImage.set(2, 2, 90.0f);

        Shift shift = new Shift(-1, -1, Shift.WRAP);
        try {
            shift.apply(originalImage);
            fail("Expected ImageException");
        } catch (ImageException e) {
            // Expected
        }
    }
}