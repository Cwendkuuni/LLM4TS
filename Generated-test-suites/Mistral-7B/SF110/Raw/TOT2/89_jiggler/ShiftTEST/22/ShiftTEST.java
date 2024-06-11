import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @Test
    public void testConstructorWithIntXIntYIntWrap() {
        Shift shift = new Shift(2, 3, 1); // xoffset = 2, yoffset = 3, wrap = true
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertTrue(shift.isWrap());
    }
}