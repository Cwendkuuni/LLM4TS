import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @Test
    public void testShiftGrayImage_PositiveOffsets() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage originalImage = new GrayImage(5, 5);
        GrayImage shiftedImage = shift.apply(originalImage);

        int expectedX = 2;
        int expectedY = 3;
        for (int y = 0; y < originalImage.Y(); ++y) {
            for (int x = 0; x < originalImage.X(); ++x) {
                int actualX = shiftedImage.getX(x);
                int actualY = shiftedImage.getY(y);
                assertThat(actualX, is(expectedX + x));
                assertThat(actualY, is(expectedY + y));
            }
        }
    }

    @Test
    public void testShiftGrayImage_WrapOff() {
        Shift shift = new Shift(2, 3, Shift.NOWRAP);
        GrayImage originalImage = new GrayImage(5, 5);
        GrayImage shiftedImage = shift.apply(originalImage);

        int x = 0;
        int y = 0;
        int shiftedPixel = shiftedImage.get(x, y);
        int expectedPixel = originalImage.get(x, y);
        assertThat(shiftedPixel, is(expectedPixel));

        x = originalImage.X() - 1;
        y = originalImage.Y() - 1;
        shiftedPixel = shiftedImage.get(x, y);
        expectedPixel = originalImage.get(x, y);
        assertThat(shiftedPixel, is(expectedPixel));
    }
}