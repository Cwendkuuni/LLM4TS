import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {
    @Test
    public void testClipWithIntMinMax() {
        // Given
        int min = 0;
        int max = 10;
        GrayImage inputImage = new GrayImage(3, 3);
        inputImage.set(0, 0, 5);
        inputImage.set(0, 1, 15);
        inputImage.set(0, 2, 3);
        inputImage.set(1, 0, 7);
        inputImage.set(1, 1, 12);
        inputImage.set(1, 2, 8);
        inputImage.set(2, 0, 2);
        inputImage.set(2, 1, 11);
        inputImage.set(2, 2, 9);

        // When
        Clip clip = new Clip(min, max);
        GrayImage outputImage = clip.apply(inputImage);

        // Then
        assertEquals(min, outputImage.get(0, 0).intValue());
        assertEquals(max, outputImage.get(0, 1).intValue());
        assertEquals(min, outputImage.get(1, 0).intValue());
        assertEquals(max, outputImage.get(1, 1).intValue());
        assertEquals(min, outputImage.get(2, 0).intValue());
        assertEquals(max, outputImage.get(2, 1).intValue());

        // Edge cases
        min = Integer.MIN_VALUE;
        max = Integer.MAX_VALUE;
        inputImage = new GrayImage(3, 3);
        inputImage.set(0, 0, Integer.MIN_VALUE);
        inputImage.set(0, 1, Integer.MIN_VALUE);
        inputImage.set(0, 2, Integer.MIN_VALUE);
        inputImage.set(1, 0, Integer.MIN_VALUE);
        inputImage.set(1, 1, Integer.MIN_VALUE);
        inputImage.set(1, 2, Integer.MIN_VALUE);
        inputImage.set(2, 0, Integer.MIN_VALUE);
        inputImage.set(2, 1, Integer.MIN_VALUE);
        inputImage.set(2, 2, Integer.MIN_VALUE);
        clip = new Clip(min, max);
        outputImage = clip.apply(inputImage);
        assertEquals(min, outputImage.get(0, 0).intValue());
        assertEquals(max, outputImage.get(0, 1).intValue());
        assertEquals(min, outputImage.get(1, 0).intValue());
        assertEquals(max, outputImage.get(1, 1).intValue());
        assertEquals(min, outputImage.get(2, 0).intValue());
        assertEquals(max, outputImage.get(2, 1).intValue());

        // Exception handling
        try {
            clip = new Clip(Integer.MAX_VALUE, Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}