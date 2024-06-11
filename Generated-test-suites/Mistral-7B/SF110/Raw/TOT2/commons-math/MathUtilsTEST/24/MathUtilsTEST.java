import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHashWithDouble() {
        double value = 1.2;
        int expected = 12345; // Replace this with the actual expected hashcode
        int result = MathUtils.hash(value);
        Assert.assertEquals(expected, result);
    }

    // Add test cases for other hash methods and public methods...
}