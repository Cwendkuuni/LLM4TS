import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;

/**
 * BaseSingleFieldPeriod Test Cases.
 */
public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_BasicFunctionality() {
        // Test creating an instance of BaseSingleFieldPeriod with a given value
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = testPeriod.getFieldType();

        // Check the period value
        int expectedValue = 5;
        int actualValue = testPeriod.getValue();
        assertEquals("Period value is not as expected", expectedValue, actualValue);

        // Check the period field type
        String expectedFieldTypeName = "Days";
        String actualFieldTypeName = fieldType.getName();
        assertEquals("Field type is not as expected", expectedFieldTypeName, actualFieldTypeName);

        // Test setting a new value for the period
        int newValue = 10;
        testPeriod.setValue(newValue);
        int newActualValue = testPeriod.getValue();
        assertEquals("New period value is not as expected", newValue, newActualValue);

        // Test getting the duration field value
        int expectedFieldValue = 5;
        int actualFieldValue = testPeriod.get(fieldType);
        assertEquals("Field value is not as expected", expectedFieldValue, actualFieldValue);

        // Test if the period is supported by the given duration field type
        boolean isSupported = testPeriod.isSupported(fieldType);
        assertTrue("Period is not supported by the field type", isSupported);

        // Test creating an immutable Period object from the BaseSingleFieldPeriod
        Period period = testPeriod.toPeriod();
        int periodValue = period.getSeconds();
        assertEquals("Period value is not as expected", expectedValue * 24 * 60 * 60, periodValue);

        // Test creating a MutablePeriod object from the BaseSingleFieldPeriod
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        mutablePeriod.setValue(newValue);
        int mutablePeriodValue = mutablePeriod.getValue(fieldType);
        assertEquals("Mutable period value is not as expected", newValue, mutablePeriodValue);
    }

    @Test
    public void testBaseSingleFieldPeriod_EdgeCasesAndExceptions() {
        // Test creating an instance of BaseSingleFieldPeriod with a negative value
        try {
            BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(-5);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating an instance of BaseSingleFieldPeriod with a zero value
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(0);
        int actualValue = testPeriod.getValue();
        assertEquals("Period value should be zero", 0, actualValue);

        // Test setting a negative value for the period
        BaseSingleFieldPeriod testPeriodNegative = new BaseSingleFieldPeriod(5);
        testPeriodNegative.setValue(-3);
        int newActualValueNegative = testPeriodNegative.getValue();
        assertEquals("New period value is not as expected", -3, newActualValueNegative);

        // Test getting a field value for an unsupported field type
        BaseSingleFieldPeriod testPeriodWithUnsupportedField = new BaseSingleFieldPeriod(5);
        DurationFieldType unsupportedFieldType = DurationFieldType.MINUTES;
        int actualValueUnsupportedField = testPeriodWithUnsupportedField.get(unsupportedFieldType);
        assertEquals("Field value should be zero for an unsupported field type", 0, actualValueUnsupportedField);
    }
}