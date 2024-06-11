import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = 5381; // hashcode for 3.14
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
}