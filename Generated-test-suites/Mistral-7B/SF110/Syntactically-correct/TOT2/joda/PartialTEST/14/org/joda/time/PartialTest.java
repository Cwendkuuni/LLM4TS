import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest
{
    @Test
    public void testEmptyPartial()
    {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertTrue(emptyPartial.isMatch(new BaseDateTime()));
    }

    @Test
    public void testSingleFieldPartial()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        Partial singleFieldPartial = new Partial(DAY_OF_MONTH, 1);
        assertEquals(1, singleFieldPartial.size());
        assertEquals(DAY_OF_MONTH, singleFieldPartial.getFieldTypes()[0]);
        assertEquals(1, singleFieldPartial.getValue(0));
        assertTrue(singleFieldPartial.isMatch(new LocalDate().withDayOfMonth(1)));
    }

    @Test
    public void testMultipleFieldsPartial()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial multipleFieldsPartial = new Partial(DAY_OF_MONTH, 1, MONTH, 2);
        assertEquals(2, multipleFieldsPartial.size());
        assertEquals(DAY_OF_MONTH, multipleFieldsPartial.getFieldTypes()[0]);
        assertEquals(1, multipleFieldsPartial.getValue(0));
        assertEquals(MONTH, multipleFieldsPartial.getFieldTypes()[1]);
        assertEquals(2, multipleFieldsPartial.getValue(1));
        assertTrue(multipleFieldsPartial.isMatch(new LocalDate().withDayOfMonth(1).withMonthOfYear(2)));
    }

    @Test
    public void testInvalidFieldsOrder()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        try {
            new Partial(MONTH, 2, DAY_OF_MONTH, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidValues()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(DAY_OF_MONTH, -1);
        try {
            partial.getField(0).set(partial, 0, partial.getValues(), -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        partial = new Partial(DAY_OF_MONTH, 32);
        try {
            partial.getField(0).set(partial, 0, partial.getValues(), 32);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddingPeriod()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial();
        partial = partial.with(DAY_OF_MONTH, 1);
        partial = partial.with(MONTH, 2);
        Partial result = partial.withPeriodAdded(new LocalDate().withDayOfMonth(1).withMonthOfYear(1).toLocalDateTime(), 3);
        assertEquals(3, result.size());
        assertEquals(DAY_OF_MONTH, result.getFieldTypes()[0]);
        assertEquals(3, result.getValue(0));
        assertEquals(MONTH, result.getFieldTypes()[1]);
        assertEquals(4, result.getValue(1));
        assertTrue(result.isMatch(new LocalDate().withDayOfMonth(3).withMonthOfYear(4)));
    }

    @Test
    public void testAddingPeriodWithNegativeValue()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial();
        partial = partial.with(DAY_OF_MONTH, 1);
        partial = partial.with(MONTH, 2);
        Partial result = partial.withPeriodAdded(new LocalDate().withDayOfMonth(1).withMonthOfYear(1).toLocalDateTime(), -3);
        assertEquals(3, result.size());
        assertEquals(DAY_OF_MONTH, result.getFieldTypes()[0]);
        assertEquals(0, result.getValue(0));
        assertEquals(MONTH, result.getFieldTypes()[1]);
        assertEquals(10, result.getValue(1));
        assertTrue(result.isMatch(new LocalDate().withDayOfMonth(10).withMonthOfYear(11)));
    }

    @Test
    public void testNegativePartial()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        Partial negativePartial = partial.with(MONTH, -1).with(DAY_OF_MONTH, -1);
        Partial result = negativePartial.withChronologyRetainFields(DateTimeUtils.getUTC());
        assertEquals(2, result.size());
        assertEquals(MONTH, result.getFieldTypes()[0]);
        assertEquals(-1, result.getValue(0));
        assertEquals(DAY_OF_MONTH, result.getFieldTypes()[1]);
        assertEquals(-1, result.getValue(1));
        assertTrue(result.isMatch(new LocalDate().withDayOfMonth(-1).withMonthOfYear(-1)));
    }

    @Test
    public void testEquals()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial1 = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        Partial partial2 = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        Partial partial3 = new Partial(DAY_OF_MONTH, 1, MONTH, 3).withChronologyRetainFields(DateTimeUtils.getUTC());
        Partial partial4 = new Partial(DAY_OF_MONTH, 2, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        assertEquals(partial1, partial1);
        assertEquals(partial1, partial2);
        assertNotSame(partial1, partial3);
        assertNotSame(partial1, partial4);
    }

    @Test
    public void testToString()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        String expected = "[dayOfMonth=1, monthOfYear=2]";
        assertEquals(expected, partial.toString());
    }

    @Test
    public void testToStringList()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        String expected = "[dayOfMonth=1, monthOfYear=2]";
        assertEquals(expected, partial.toStringList());
    }

    @Test
    public void testToStringWithCustomFormat()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        String expected = "dayOfMonth=1, monthOfYear=2";
        assertEquals(expected, partial.toString("dayOfMonth=mm, monthOfYear=MM"));
    }

    @Test
    public void testToStringWithLocale()
    {
        final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
        final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(DAY_OF_MONTH, 1, MONTH, 2).withChronologyRetainFields(DateTimeUtils.getUTC());
        String expected = "dayOfMonth: 1, monthOfYear: 2";
        assertEquals(expected, partial.toString("dd: 'Day of the month': MM: 'Month of the year'", Locale.US));
    }
}