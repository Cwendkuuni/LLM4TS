import org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @org.junit.Test
    public void testApplyGrayImagePositive() {
        // Given
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 50);
        inputImage.set(1, 1, (byte) 100);
        inputImage.set(2, 2, (byte) 150);
        inputImage.set(3, 3, (byte) 200);
        inputImage.set(4, 4, (byte) 250);

        // When
        GrayImage outputImage = shift.apply(inputImage);

        // Then
        byte expectedValue = (byte) 150; // calculation: (x + xOffset) % X = 2 + 2 = 4, (y + yOffset) % Y = 3 + 3 = 6, image.get(4, 6) = 150
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                if ((x + xOffset) % outputImage.X() == (4) && (y + yOffset) % outputImage.Y() == (6)) { // check if the output image's pixels match the expected values
                    assertEquals(expectedValue, outputImage.get(x, y));
                }
            }
        }
    }
}