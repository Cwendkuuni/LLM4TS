import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 123; // Any expected hash code value
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
}