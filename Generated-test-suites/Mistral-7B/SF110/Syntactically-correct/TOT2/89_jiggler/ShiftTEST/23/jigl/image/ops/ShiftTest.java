import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @Test
    public void testShiftGrayImage() {
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage originalImage = new GrayImage(10, 10);
        GrayImage shiftedImage = shift.apply(originalImage);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int expected = originalImage.get(i, j);
                int actual = shiftedImage.get(i, j);
                assertEquals("Unexpected pixel value at [" + i + ", " + j + "]", expected, actual);
            }
        }
    }
}