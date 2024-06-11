import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class ShiftTest {
    @Test
    public void testShiftGrayImage() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage input = new GrayImage(3, 3);
        input.set(0, 0, (byte) 50);
        input.set(1, 1, (byte) 100);
        input.set(2, 2, (byte) 150);

        GrayImage expected = new GrayImage(3, 3);
        expected.set(1, 1, (byte) 50); // No offset, no wrap
        expected.set(2, 2, (byte) 100); // x = 1, y = 1

        GrayImage output = shift.apply(input);
        assertArrayEquals("Shift test failed", expected.getPixels(), output.getPixels());
    }
}