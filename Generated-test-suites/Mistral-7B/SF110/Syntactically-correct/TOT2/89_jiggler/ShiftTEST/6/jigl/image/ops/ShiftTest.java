import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import jigl.image.*;
import jigl.image.ops.Shift;

public class ShiftTest {

    @Test
    public void testApplyGrayImage() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage input = new GrayImage(3, 3);
        input.setPixel(0, 0, 100);
        input.setPixel(1, 1, 200);
        input.setPixel(2, 2, 300);

        GrayImage output = shift.apply(input);

        assertThat(output.getPixel(0, 0).getValue(), is(100));
        assertThat(output.getPixel(1, 1).getValue(), is(200));
        assertThat(output.getPixel(2, 2).getValue(), is(300));
        assertThat(output.getPixel(1, 0).getValue(), is(100));
        assertThat(output.getPixel(0, 1).getValue(), is(200));
        assertThat(output.getPixel(2, 0).getValue(), is(300));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyNullGrayImage() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage input = null;
        shift.apply(input);
    }

    @Test
    public void testApplyGrayImageWithNegativeOffset() {
        Shift shift = new Shift(-1, -1, 0);
        GrayImage input = new GrayImage(3, 3);
        input.setPixel(0, 0, 100);
        input.setPixel(1, 1, 200);
        input.setPixel(2, 2, 300);

        GrayImage output = shift.apply(input);

        assertThat(output.getPixel(0, 0).getValue(), is(299));
        assertThat(output.getPixel(1, 1).getValue(), is(199));
        assertThat(output.getPixel(2, 2).getValue(), is(1));
        assertThat(output.getPixel(1, 0).getValue(), is(299));
        assertThat(output.getPixel(0, 1).getValue(), is(199));
        assertThat(output.getPixel(2, 0).getValue(), is(1));
    }

    @Test
    public void testApplyGrayImageWithImageSizeOffset() {
        Shift shift = new Shift(3, 3, 0);
        GrayImage input = new GrayImage(3, 3);
        input.setPixel(0, 0, 100);
        input.setPixel(1, 1, 200);
        input.setPixel(2, 2, 300);

        GrayImage output = shift.apply(input);

        // Output should be the same as the input image because of the offset
        assertThat(output, is(input));
    }

    @Test
    public void testApplyGrayImageWithWrapTrue() {
        Shift shift = new Shift(1, 1, 1);
        GrayImage input = new GrayImage(3, 3);
        input.setPixel(0, 0, 100);
        input.setPixel(1, 1, 200);
        input.setPixel(2, 2, 300);

        GrayImage output = shift.apply(input);

        // Pixels that are moved out of the image border should wrap back to the image
        assertThat(output.getPixel(1, 2).getValue(), is(100));
        assertThat(output.getPixel(2, 1).getValue(), is(200));
        assertThat(output.getPixel(3, 0).getValue(), is(300));
        assertThat(output.getPixel(4, 0).getValue(), is(100));
        assertThat(output.getPixel(3, 1).getValue(), is(200));
        assertThat(output.getPixel(2, 2).getValue(), is(300));
    }

    @Test
    public void testApplyGrayImageWithImageSizeOne() {
        Shift shift = new Shift(1, 1, 0);
        GrayImage input = new GrayImage(1, 1);
        input.setPixel(0, 0, 100);

        GrayImage output = shift.apply(input);

        // Output should be the same as the input image because the image size is 1
        assertThat(output, is(input));
    }
}