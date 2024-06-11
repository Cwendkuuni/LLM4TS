import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testValidValues() {
        Chronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 12); // December

        MutableDateTime date = new MutableDateTime();
        for (int i = 1; i <= 12; i++) {
            date.setField(DateTimeFieldType.monthOfYear(), i);
            assertEquals("Expected month of year to be set correctly", i, field.get(date.getMillis()));
        }

        for (int i = 1; i <= 12; i++) {
            date.setField(DateTimeFieldType.monthOfYear(), i);
            long newMillis = field.add(date.getMillis(), 1);
            assertEquals("Expected month to increment correctly", i + 1, field.get(newMillis));
        }

        // Test with negative numbers
        for (int i = 1; i >= 0; i--) {
            date.setField(DateTimeFieldType.monthOfYear(), 12);
            long newMillis = field.add(date.getMillis(), -i);
            assertEquals("Expected month to decrement correctly", 12 - i, field.get(newMillis));
        }

        // Test max value
        date.setField(DateTimeFieldType.monthOfYear(), chronology.getMaxMonth());
        long newMillis = field.add(date.getMillis(), 1);
        assertEquals("Expected max month to wrap around to minimum value", chronology.getMinMonth(), field.get(newMillis));
    }

    @Test
    public void testLeapYear() {
        Chronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        MutableDateTime date = new MutableDateTime();
        for (int i = 1; i <= 12; i++) {
            date.setField(DateTimeFieldType.monthOfYear(), i);
            long newMillis = field.add(date.getMillis(), 1);
            assertEquals("Expected month to increment correctly", i + 1, field.get(newMillis));

            if (i == 2) {
                assertTrue("Expected February to be a leap month in a leap year", field.isLeap(newMillis));
                assertEquals("Expected February to have 29 days in a leap year", 29, chronology.getDaysInYearMonth(FieldUtils.getYear(newMillis), FieldUtils.getMonthOfYear(newMillis)));
            }
        }

        date.setField(DateTimeFieldType.year, 2000);
        date.setField(DateTimeFieldType.monthOfYear, 2);
        long newMillis = field.add(date.getMillis(), -1);
        assertFalse("Expected February to not be a leap month in a non-leap year", field.isLeap(newMillis));
    }

    @Test
    public void testAddLong() {
        Chronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 12); // December

        MutableDateTime date = new MutableDateTime();
        date.setField(DateTimeFieldType.year, 2021);
        date.setField(DateTimeFieldType.monthOfYear, 11);
        date.setField(DateTimeFieldType.dayOfMonth, 31);

        long newMillis = field.add(date.getMillis(), 12 * 12L);
        MutableLocalDateTime result = new MutableLocalDateTime(chronology, new LocalDate(newMillis));
        LocalDateTime expected = new LocalDateTime(2022, 1, 31, 0, 0);

        assertEquals("Expected date to be incremented by 12 months", expected.toString(), result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLongInvalid() {
        Chronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 12); // December

        MutableDateTime date = new MutableDateTime();
        date.setField(DateTimeFieldType.year, 2021);
        date.setField(DateTimeFieldType.monthOfYear, 11);
        date.setField(DateTimeFieldType.dayOfMonth, 31);

        long newMillis = field.add(date.getMillis(), Long.MAX_VALUE + 1);
        assertEquals("Expected IllegalArgumentException to be thrown", Long.MAX_VALUE + 1, FieldUtils.getMillisecondsBetween(date.getMillis(), newMillis));
    }
}