import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, 0);
        assertEquals(2, shift.getXoffset());
        assertEquals(3, shift.getYoffset());
        assertEquals(false, shift.isWrap());
    }
}