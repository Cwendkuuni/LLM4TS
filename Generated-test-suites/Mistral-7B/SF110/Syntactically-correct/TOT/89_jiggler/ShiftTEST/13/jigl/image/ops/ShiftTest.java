import org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.Shift;

public class ShiftTest {

    @Test
    public void testGrayShift() {
        // Given: A 3x3 GrayImage with values [1, 2, 3], [4, 5, 6], [7, 8, 9]
        GrayImage input = new GrayImage(3, 3);
        input.set(0, 0, (byte) 1);
        input.set(0, 1, (byte) 2);
        input.set(0, 2, (byte) 3);
        input.set(1, 0, (byte) 4);
        input.set(1, 1, (byte) 5);
        input.set(1, 2, (byte) 6);
        input.set(2, 0, (byte) 7);
        input.set(2, 1, (byte) 8);
        input.set(2, 2, (byte) 9);

        // When: Shift the image by (1, 1) without wrapping
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage output = shift.apply(input);

        // Then: The output image should have the same dimensions and values as the input image, but shifted by (1, 1)
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(0, 0, input.get(1, 1));
        expectedOutput.set(0, 1, input.get(2, 1));
        expectedOutput.set(0, 2, input.get(2, 2));
        expectedOutput.set(1, 0, input.get(2, 0));
        expectedOutput.set(1, 1, input.get(3, 1));
        expectedOutput.set(1, 2, input.get(3, 2));
        expectedOutput.set(2, 0, input.get(2, 2));
        expectedOutput.set(2, 1, input.get(2, 1));
        expectedOutput.set(2, 2, input.get(2, 0));
        assertArrayEquals(expectedOutput.toArray(), output.toArray());

        // When: Shift the image by (1, 1) with wrapping
        shift = new Shift(1, 1, Shift.WRAP);
        output = shift.apply(input);

        // Then: The output image should be the same as the input image (wrapping should not affect the result)
        assertArrayEquals(input.toArray(), output.toArray());
    }
}