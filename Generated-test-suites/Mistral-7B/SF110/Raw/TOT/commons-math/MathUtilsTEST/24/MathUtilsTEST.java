import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // any valid hashcode
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testHashNullDouble() {
        double value = null;
        MathUtils.hash(value);
    }

    // other test cases for other methods can be added here

    // Test for complete JUnit 4 test file starts below
    

}