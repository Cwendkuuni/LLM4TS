import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;
import jigl.image.*;

public class ShiftTest {

    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertTrue(shift.isWrap());
    }

    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 255);
        inputImage.set(2, 2, (byte) 0);

        GrayImage outputImage = shift.apply(inputImage);
        assertNotNull(outputImage);
        assertEquals(5, outputImage.X());
        assertEquals(5, outputImage.Y());
        outputImage.set(1, 1, (byte) 0); // Testing the shift
        assertEquals(255, outputImage.get(1, 1));
        assertEquals(0, outputImage.get(4, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShiftConstructorNegativeValues() {
        Shift shift = new Shift(-2, 3, Shift.WRAP);
    }

    @Test
    public void testApplyGrayImageEmptyInput() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage emptyInputImage = new GrayImage(0, 0);
        GrayImage outputImage = shift.apply(emptyInputImage);
        assertNotNull(outputImage);
        assertEquals(0, outputImage.X());
        assertEquals(0, outputImage.Y());
    }

    @Test
    public void testApplyGrayImageNullInput() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage nullInputImage = null;
        try {
            GrayImage outputImage = shift.apply(nullInputImage);
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testApplyGrayImageInputWithLessDimensions() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        GrayImage inputImage = new GrayImage(3, 3);
        GrayImage outputImage = shift.apply(inputImage);
        assertNotNull(outputImage);
        assertEquals(3, outputImage.X());
        assertEquals(3, outputImage.Y());
    }

}