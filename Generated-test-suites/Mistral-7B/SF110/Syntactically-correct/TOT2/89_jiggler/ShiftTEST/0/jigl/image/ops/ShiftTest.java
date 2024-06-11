import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ShiftTest {
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(1, 2, 1);
        assertEquals(1, shift.getXoffset());
        assertEquals(2, shift.getYoffset());
        assertEquals(true, shift.isWrap());
    }
}