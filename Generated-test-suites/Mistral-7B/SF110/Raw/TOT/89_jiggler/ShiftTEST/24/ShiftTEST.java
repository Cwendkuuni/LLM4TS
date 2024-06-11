import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;
import jigl.image.*;

public class ShiftTest {
    @org.junit.Test
    public void testShiftGrayImage() {
        // Create input image
        GrayImage inputImage = new GrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); ++i) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) (i % 10));
        }

        // Create shift operator
        Shift shiftOperator = new Shift(2, 2, Shift.WRAP);

        // Apply shift operator to input image and check output
        GrayImage expectedOutput = new GrayImage(5, 5);
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                int actualOutput = shiftOperator.apply(inputImage).get(x, y);
                int expectedOutputValue = inputImage.get((x + 2) % 5, (y + 2) % 5);
                assertEquals("Invalid pixel value in output image at position [" + x + ", " + y + "]", expectedOutputValue, actualOutput);
            }
        }
    }

    @org.junit.Test
    public void testShiftRealGrayImage() {
        // Create input image
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); ++i) {
            inputImage.set(i % inputImage.X(), i % inputImage.Y(), i % 10.0f);
        }

        // Create shift operator
        Shift shiftOperator = new Shift(2, 2, Shift.WRAP);

        // Apply shift operator to input image and check output
        RealGrayImage expectedOutput = new RealGrayImage(5, 5);
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                float actualOutput = shiftOperator.apply(inputImage).get(x, y);
                float expectedOutputValue = inputImage.get((x + 2) % 5, (y + 2) % 5);
                assertEquals("Invalid pixel value in output image at position [" + x + ", " + y + "]", expectedOutputValue, actualOutput, 0.01f);
            }
        }
    }

    @org.junit.Test
    public void testShiftColorImage() {
        // Create input image
        ColorImage inputImage = new ColorImage(5, 5);
        inputImage.setPlane(0, new GrayImage(5, 5));
        inputImage.setPlane(1, new GrayImage(5, 5));
        inputImage.setPlane(2, new GrayImage(5, 5));
        for (int i = 0; i < inputImage.X() * inputImage.Y(); ++i) {
            for (int planeIndex = 0; planeIndex < 3; ++planeIndex) {
                GrayImage plane = inputImage.plane(planeIndex);
                for (int j = 0; j < plane.X() * plane.Y(); ++j) {
                    plane.set(j % plane.X(), j % plane.Y(), (byte) (j % 10));
                }
            }
        }

        // Create shift operator
        Shift shiftOperator = new Shift(2, 2, Shift.WRAP);

        // Apply shift operator to input image and check output
        ColorImage expectedOutput = new ColorImage(5, 5);
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                ColorImage actualOutput = shiftOperator.apply(inputImage);
                for (int planeIndex = 0; planeIndex < 3; ++planeIndex) {
                    GrayImage plane = actualOutput.plane(planeIndex);
                    int actualOutputValue = plane.get((x + 2) % 5, (y + 2) % 5);
                    GrayImage expectedOutputPlane = inputImage.plane((x + 2) % 5, (y + 2) % 5);
                    assertEquals("Invalid pixel value in output image plane [" + planeIndex + "] at position [" + x + ", " + y + "]", expectedOutputPlane.get(0, 0), actualOutputValue);
                }
            }
        }
    }
}