import org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.*;

public class ClipTest {

    @Test
    public void testApplyGrayImageNormalValues() {
        GrayImage inputImage = new GrayImage(5, 5);
        int[] inputValues = {25, 50, 75, 100, 125};
        for (int i = 0; i < inputValues.length; i++) {
            inputImage.set(i / 5, i % 5, Integer.valueOf(inputValues[i]));
        }

        Clip clip = new Clip(50, 100);
        GrayImage expectedOutputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputValues.length; i++) {
            int value = inputValues[i];
            if (value > clip.int_max) value = clip.int_max;
            if (value < clip.int_min) value = clip.int_min;
            expectedOutputImage.set(i / 5, i % 5, Integer.valueOf(value));
        }

        GrayImage outputImage = new Clip().apply(inputImage);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                int actualValue = outputImage.get(x, y).intValue();
                int expectedValue = expectedOutputImage.get(x, y).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    // Add test cases for edge cases and exceptions here

    // ...
}