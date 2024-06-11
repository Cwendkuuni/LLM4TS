import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private Chronology gregorian;
    private DateTimeField strictDayOfMonth;
    private DateTimeField lenientDayOfMonth;

    @Before
    public void setUp() {
        gregorian = org.joda.time.DateTimeUtils.getUTC();
        strictDayOfMonth = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), gregorian);
        lenientDayOfMonth = LenientDateTimeField.getInstance(strictDayOfMonth, gregorian);
    }

    @Test
    public void testLenientSetValidValue() {
        MutableDateTime dateTime = new MutableDateTime(1512165600000L, gregorian);
        int validValue = 5;
        long expectedValue = 1512242000000L;

        long result = lenientDayOfMonth.set(dateTime.getLong(), validValue);

        assertEquals(expectedValue, result);
        assertEquals(validValue, lenientDayOfMonth.get(dateTime));
    }

    @Test
    public void testLenientSetOutOfBoundsValue_Addition() {
        MutableDateTime dateTime = new MutableDateTime(1512165600000L, gregorian);
        int outOfBoundsValue = 32;
        long expectedValue = 1512316800000L;

        long result = lenientDayOfMonth.set(dateTime.getLong(), outOfBoundsValue);

        assertEquals(expectedValue, result);
        assertEquals(outOfBoundsValue, lenientDayOfMonth.get(dateTime));
    }

    @Test
    public void testLenientSetOutOfBoundsValue_Negative() {
        MutableDateTime dateTime = new MutableDateTime(1512165600000L, gregorian);
        int outOfBoundsNegativeValue = -3;

        long result = lenientDayOfMonth.set(dateTime.getLong(), outOfBoundsNegativeValue);

        long expectedValue = 1512162200000L; // 3 days before
        assertEquals(expectedValue, result);
        assertEquals(3, lenientDayOfMonth.get(dateTime)); // but it still returns the value, not -3
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLenientSetInvalidValue() {
        MutableDateTime dateTime = new MutableDateTime(1512165600000L, gregorian);
        int invalidValue = 366;

        lenientDayOfMonth.set(dateTime.getLong(), invalidValue);
    }

    @Test
    public void testLenientGet() {
        LocalDateTime dateTime = new LocalDateTime(2018, 1, 1, 0, 0);
        int value = 31;

        DateTimeField field = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), gregorian), gregorian);

        int result = field.get(dateTime.toDateTime(gregorian).getLong());

        assertEquals(value, result);
    }

    @Test
    public void testLenientGet_OutOfBoundsValue() {
        LocalDateTime dateTime = new LocalDateTime(2018, 1, 1, 0, 0);
        int value = 366;

        DateTimeField field = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), gregorian), gregorian);

        int result = field.get(dateTime.toDateTime(gregorian).getLong());

        // lenient field should return the value, not throw an exception
        assertEquals(value % 30, result);
    }

    @Test
    public void testLenientFormat() {
        BaseDateTime dateTime = new BaseDateTime(1512165600000L, gregorian);
        DateTimeFormat formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

        String expectedValue = "2018-03-31";
        String result = formatter.print(lenientDayOfMonth.get(new MutableDateTime(dateTime.getLong()).toLocalDateTime(gregorian)));

        assertEquals(expectedValue, result);
    }
}