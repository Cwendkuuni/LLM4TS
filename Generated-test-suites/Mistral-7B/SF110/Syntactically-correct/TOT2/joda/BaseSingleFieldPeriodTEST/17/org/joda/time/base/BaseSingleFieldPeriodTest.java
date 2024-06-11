import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(5);
        int expectedValue = 5;
        int actualValue = testObject.getValue();
        assertEquals(expectedValue, actualValue);
    }

    // Test case for method 2 - between()

    // Test case for method 3 - getFieldType()

    @Test
    public void testEqualsWithNullObject() {
        BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(5);
        assertFalse(testObject.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        BaseSingleFieldPeriod testObject1 = new BaseSingleFieldPeriod(5);
        String stringTestObject = new String("Test");
        assertFalse(testObject1.equals(stringTestObject));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        BaseSingleFieldPeriod testObject1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testObject2 = new BaseSingleFieldPeriod(3);
        assertFalse(testObject1.equals(testObject2));
    }
}