import org.junit.Test;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;

public class ShiftTest {
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, Shift.WRAP);
        assert(shift.getXoffset() == 2);
        assert(shift.getYoffset() == 3);
        assert(shift.isWrap() == true);
    }
}