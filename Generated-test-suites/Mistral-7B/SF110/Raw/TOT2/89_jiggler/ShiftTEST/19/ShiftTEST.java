import jigl.image.*;
import jigl.image.ops.Shift;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShiftTest {

    @Test
    public void testShiftGrayImage() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            expectedOutputImage.set(i % (inputImage.X() + xOffset), i % (inputImage.Y() + yOffset), inputImage.get(i));
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertEquals(expectedOutputImage, outputImage);
    }

    // ... Expert 1 and 2 test cases here ...

    @Test
    public void testShiftGrayImageNoWrap() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(10, 10);
        GrayImage expectedOutputImage = new GrayImage(10, 10);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            int x = i % inputImage.X();
            int y = i % inputImage.Y();
            if (x + xOffset < 0 || y + yOffset < 0 || x + xOffset >= inputImage.X() || y + yOffset >= inputImage.Y()) {
                expectedOutputImage.set(i, Integer.valueOf(0));
            } else {
                expectedOutputImage.set(i, inputImage.get(x + xOffset, y + yOffset));
            }
        }
        GrayImage outputImage = shift.apply(inputImage);
        assertEquals(expectedOutputImage, outputImage);
    }

    // ... Expert 1 and 2 test cases for edge cases and exceptions here ...
}