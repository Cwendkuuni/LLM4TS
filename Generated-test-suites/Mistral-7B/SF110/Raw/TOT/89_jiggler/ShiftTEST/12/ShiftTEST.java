import org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @org.junit.Test
    public void testGrayImageShift() {
        // Create input image
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) (i % 256));
        }

        // Create Shift operator
        Shift shift = new Shift(2, 2, Shift.WRAP);

        // Apply the operator and compare the results
        GrayImage outputImage = shift.apply(inputImage);
        for (int y = 0; y < outputImage.Y(); y++) {
            for (int x = 0; x < outputImage.X(); x++) {
                int expectedPixel = inputImage.get(x, y);
                int actualPixel = outputImage.get(x, y);
                assertEquals("Pixel value mismatch at (" + x + ", " + y + ")", expectedPixel, actualPixel);
            }
        }
    }
}