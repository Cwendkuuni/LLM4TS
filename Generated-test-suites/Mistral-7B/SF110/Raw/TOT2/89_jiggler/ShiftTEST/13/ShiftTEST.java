import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ShiftTest {
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(2, 3, 1);
        assertNotNull(shift);
    }
}