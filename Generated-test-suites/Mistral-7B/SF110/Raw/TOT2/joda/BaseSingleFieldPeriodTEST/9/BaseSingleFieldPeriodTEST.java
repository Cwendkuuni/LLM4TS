import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructorWithIntArgument() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(10);
        int expected = 10;
        int actual = testObject.getValue();
        assertEquals(expected, actual);
    }

    //Edge Case 1: Test with maximum value of int
    @Test
    public void testConstructorWithMaxIntValue() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(Integer.MAX_VALUE);
        int expected = Integer.MAX_VALUE;
        int actual = testObject.getValue();
        assertEquals(expected, actual);
    }

    //Exception Scenario: Test with negative value
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeValue() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(-5);
    }
}