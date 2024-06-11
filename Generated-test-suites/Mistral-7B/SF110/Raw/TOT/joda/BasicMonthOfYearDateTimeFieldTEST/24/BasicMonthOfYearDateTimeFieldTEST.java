import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime dateTime = new LocalDateTime(2022, 2, 28);
        long millis = dateTime.toMillis();

        int actual = field.get(millis);
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddMethodWithValidArguments() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime dateTime = new LocalDateTime(2022, 2, 28);
        long millis = dateTime.toMillis();

        long newMillis = field.add(millis, 1);
        LocalDateTime newDateTime = new LocalDateTime(newMillis);
        int expected = 3;
        assertEquals(expected, newDateTime.getMonthOfYear());
    }

    @Test
    public void testAddMethodWithInvalidArguments() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime dateTime = new LocalDateTime(2022, 2, 28);
        long millis = dateTime.toMillis();

        long newMillis = field.add(millis, Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> new LocalDateTime(newMillis));

        long newMillis2 = field.add(millis, Integer.MIN_VALUE);
        LocalDateTime newDateTime2 = new LocalDateTime(newMillis2);
        int expected = 12;
        assertEquals(expected, newDateTime2.getMonthOfYear());
    }

    @Test
    public void testIsLeapMethod() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime dateTime = new LocalDateTime(2022, 2, 28);
        long millis = dateTime.toMillis();

        boolean actual = field.isLeap(millis);
        assertFalse(actual);

        LocalDateTime leapDateTime = new LocalDateTime(2022, 2, 29);
        long leapMillis = leapDateTime.toMillis();

        actual = field.isLeap(leapMillis);
        assertTrue(actual);
    }

    @Test
    public void testAddWrapFieldMethod() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime dateTime = new LocalDateTime(2022, 12, 31);
        long millis = dateTime.toMillis();

        long newMillis = field.addWrapField(millis, 13);
        LocalDateTime newDateTime = new LocalDateTime(newMillis);
        int expected = 1;
        assertEquals(expected, newDateTime.getMonthOfYear());
    }
}