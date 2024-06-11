import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    // Test normal usage
    @Test
    public void testgetValueNormalUsage() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(5);
        int expected = 5;
        int actual = testObject.getValue();
        assertEquals(expected, actual);
    }

    // Test exception when accessing a non-existing index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testgetValueIndexOutOfBounds() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(5);
        testObject.setValue(10); // Set a value for index 1
        int actual = testObject.getValue(1); // Try to access index 1
    }
}