import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Period;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private static final Chronology UTC = Chronology.UTC;

    @Test
    public void testLenientDateTimeField_ValidValues() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);
        MutableDateTime dateTime = new MutableDateTime(0, 2022, 1, 1, 1, 1, 0, UTC);

        // Test setting valid values
        long setValue = 30;
        long expectedValue = dateTime.getMinuteOfHour() + setValue;
        long actualValue = lenientField.set(dateTime.getMillisecond(), expectedValue);
        assertEquals("Expected '30' but got '{}'", expectedValue, actualValue);
        assertEquals("Expected '30' minutes but got '{}'", new LocalTime(1, 1, 30, 0, UTC).getMinuteOfHour(), lenientField.get(actualValue));
    }

    @Test
    public void testLenientDateTimeField_OutOfBoundsValues() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);
        MutableDateTime dateTime = new MutableDateTime(0, 2022, 1, 1, 1, 1, 0, UTC);

        // Test setting out of bounds values
        long outOfBoundsValue = 60 * 24 * 30 + 1; // 1 month and 1 day over the limit
        long expectedValue = dateTime.getMinuteOfHour() + outOfBoundsValue;
        long actualValue = lenientField.set(dateTime.getMillisecond(), expectedValue);
        MutableDateTime resultDateTime = new MutableDateTime(actualValue, UTC);
        long resultValue = lenientField.get(resultDateTime.getMillisecond());

        // Check that the out of bounds value is interpreted as an addition
        assertEquals("Expected '{}' but got '{}'", expectedValue, actualValue);
        assertEquals("Expected '{}' minutes but got '{}'", new LocalTime(1, resultDateTime.getHourOfDay(), resultValue, 0, UTC).getMinuteOfHour(), lenientField.get(actualValue));
        // Check that the date and time components are adjusted accordingly
        assertEquals("Expected '{}' but got '{}'", resultDateTime.getYear(), dateTime.getYear() + 1);
        assertEquals("Expected '{}' but got '{}'", resultDateTime.getMonthOfYear(), dateTime.getMonthOfYear() + 1);
        assertEquals("Expected '{}' but got '{}'", resultDateTime.getDayOfMonth(), dateTime.getDayOfMonth() + 1);
        assertEquals("Expected '{}' but got '{}'", resultDateTime.getHourOfDay(), dateTime.getHourOfDay());
    }

    @Test
    public void testLenientDateTimeField_ErrorScenarios() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);

        // Test null input
        assertNull("Expected null but got '{}'", lenientField.set(null, 0));
        assertNull("Expected null but got '{}'", lenientField.set(0, null));

        // Test invalid input
        long invalidInput = Long.MIN_VALUE;
        long expectedValue = 0;
        long actualValue = lenientField.set(invalidInput, expectedValue);
        assertEquals("Expected '{}' but got '{}'", expectedValue, actualValue);

        // Test overflow
        long maxValue = Long.MAX_VALUE;
        long expectedValue = Long.MAX_VALUE;
        long actualValue = lenientField.set(maxValue, expectedValue);
        assertEquals("Expected '{}' but got '{}'", expectedValue, actualValue);
    }
}