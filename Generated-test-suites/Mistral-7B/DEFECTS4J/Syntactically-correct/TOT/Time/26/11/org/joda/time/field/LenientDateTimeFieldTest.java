import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneId;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        Chronology iso = DateTimeConstants.ISO_INSTANT;
        DateTimeField dayOfWeek = DateTimeFieldType.dayOfWeek();
        DateTimeField minute = DateTimeFieldType.minuteOfHour();

        // null check
        assertNull(LenientDateTimeField.getInstance(null, iso));

        // test with strict field
        DateTimeField field = new StrictLocalDateTimeField(dayOfWeek, iso);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, iso);
        assertNotSame(field, lenientField);
        assertTrue(lenientField.isLenient());

        // test with lenient field
        DateTimeField lenientField2 = new LenientLocalDateTimeField(minute, iso);
        DateTimeField lenientField3 = LenientDateTimeField.getInstance(lenientField2, iso);
        assertSame(lenientField2, lenientField3);
        assertTrue(lenientField3.isLenient());

        // test with invalid chronology
        try {
            LenientDateTimeField.getInstance(field, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        Chronology iso = DateTimeConstants.ISO_INSTANT;
        DateTimeField dayOfMonth = DateTimeFieldType.dayOfMonth();
        MutableDateTime dateTime = new MutableDateTime(0, iso);

        LenientDateTimeField lenientField = new LenientLocalDateTimeField(dayOfMonth, iso);

        // test valid values
        dateTime.set(2022, 1, 28);
        long initialInstant = dateTime.getMillis();
        long value = 29;
        long resultInstant = lenientField.set(initialInstant, value);
        assertEquals("Expected date with day 29", new MutableDateTime(2022, 2, 29, iso).getMillis(), resultInstant);

        // test out of bounds values
        value = 31;
        resultInstant = lenientField.set(initialInstant, value);
        assertEquals("Expected date with day 30", new MutableDateTime(2022, 2, 30, iso).getMillis(), resultInstant);

        // test out of bounds values for month that has only 30 days
        MutableLocalDateTime localDateTime = new MutableLocalDateTime(2022, 4, 31, 12, 0, 0, iso);
        long initialLocalInstant = localDateTime.toInstant().getMillis();
        value = 1;
        resultInstant = lenientField.set(initialLocalInstant, value);
        assertEquals("Expected date with day 1", new MutableLocalDateTime(2022, 4, 1, 12, 0, 0, iso).toInstant().getMillis(), resultInstant);

        value = DateTimeConstants.MAX_VALUE_DAY_OF_MONTH;
        resultInstant = lenientField.set(initialLocalInstant, value);
        assertEquals("Expected date with last day of the month", new MutableLocalDateTime(2022, 4, DateTimeConstants.LAST_DAY_OF_MONTH, 12, 0, 0, iso).toInstant().getMillis(), resultInstant);

        // test out of bounds values for month that has only 28 days
        MutableLocalDateTime localDateTime2 = new MutableLocalDateTime(2022, 2, DateTimeConstants.MAX_VALUE_DAY_OF_MONTH, 12, 0, 0, iso);
        long initialLocalInstant2 = localDateTime2.toInstant().getMillis();
        value = DateTimeConstants.MAX_VALUE_DAY_OF_MONTH + 1;
        resultInstant = lenientField.set(initialLocalInstant2, value);
        assertEquals("Expected date with last day of the previous month", new MutableLocalDateTime(2022, 1, DateTimeConstants.LAST_DAY_OF_MONTH, 12, 0, 0, iso).toInstant().getMillis(), resultInstant);

        // test values before minimum
        value = DateTimeConstants.MIN_VALUE_DAY_OF_MONTH - 1;
        resultInstant = lenientField.set(initialLocalInstant, value);
        assertEquals("Expected date with first day of the month", new MutableLocalDateTime(2022, 2, DateTimeConstants.MIN_VALUE_DAY_OF_MONTH, 12, 0, 0, iso).toInstant().getMillis(), resultInstant);

        // test values before minimum for month that has only 28 days
        localDateTime2 = new MutableLocalDateTime(2022, 2, DateTimeConstants.MIN_VALUE_DAY_OF_MONTH - 1, 12, 0, 0, iso);
        long initialLocalInstant3 = localDateTime2.toInstant().getMillis();
        resultInstant = lenientField.set(initialLocalInstant3, value);
        assertEquals("Expected date with first day of the previous month", new MutableLocalDateTime(2022, 1, DateTimeConstants.MIN_VALUE_DAY_OF_MONTH, 12, 0, 0, iso).toInstant().getMillis(), resultInstant);
    }
}