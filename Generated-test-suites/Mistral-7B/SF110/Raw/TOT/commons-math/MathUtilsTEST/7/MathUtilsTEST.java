import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 1316952537; // hashcode for 1.23
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
}