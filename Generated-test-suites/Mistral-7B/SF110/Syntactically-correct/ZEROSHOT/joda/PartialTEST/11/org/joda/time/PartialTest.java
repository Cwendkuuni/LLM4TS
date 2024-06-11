package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PartialTest {

    @Test
    public void testConstructor() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
    }

    @Test
    public void testConstructorWithChronology() {
        Partial partial = new Partial(DateTimeUtils.UTC);
        assertEquals(0, partial.size());
        assertEquals(DateTimeUtils.UTC, partial.getChronology());
    }

    @Test
    public void testConstructorWithDateTimeFieldTypeAndInt() {
        Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 12);
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldType(0));
        assertEquals(12, partial.getValue(0));
    }

    @Test
    public void testConstructorWithDateTimeFieldTypeArrayAndIntArray() {
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfMonth(), DateTimeFieldType.hourOfDay() }, new int[] { 28, 14 });
        assertEquals(2, partial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldType(0));
        assertEquals(28, partial.getValue(0));
        assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldType(1));
        assertEquals(14, partial.getValue(1));
    }

    @Test
    public void testConstructorWithReadablePartial() {
        LocalDateTime localDateTime = new LocalDateTime(2012, 12, 31, 23, 59, 59, 0, DateTimeUtils.UTC);
        Partial partial = new Partial(new LocalDateTimePartial(localDateTime));
        assertEquals(7, partial.size());
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
        assertEquals(2012, partial.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partial.getFieldType(1));
        assertEquals(12, partial.getValue(1));
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldType(2));
        assertEquals(31, partial.getValue(2));
        assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldType(3));
        assertEquals(23, partial.getValue(3));
        assertEquals(DateTimeFieldType.minuteOfHour(), partial.getFieldType(4));
        assertEquals(59, partial.getValue(4));
        assertEquals(DateTimeFieldType.secondOfMinute(), partial.getFieldType(5));
        assertEquals(59, partial.getValue(5));
        assertEquals(DateTimeFieldType.millisOfSecond(), partial.getFieldType(6));
        assertEquals(0, partial.getValue(6));
    }

    @Test
    public void testToString() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals("[dayOfMonth=28]", partial.toString());
    }

    @Test
    public void testToStringWithCustomFormat() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals("[dayOfMonth=28]", partial.toString());
        assertEquals("dd", partial.toString("dd"));
        assertEquals("d", partial.toString("d"));
        assertEquals("dayOfMonth", partial.toString("P"));
    }

    @Test
    public void testGetChronology() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(DateTimeUtils.UTC, partial.getChronology());
    }

    @Test
    public void testSize() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(1, partial.size());
    }

    @Test
    public void testGetFieldType() {
        Partial partial = new Partial();
        try {
            partial.getFieldType(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
        partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldType(0));
    }

    @Test
    public void testGetValues() {
        Partial partial = new Partial();
        try {
            partial.getValues();
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
        partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        int[] values = partial.getValues();
        assertEquals(1, values.length);
        assertEquals(28, values[0]);
    }

    @Test
    public void testGetField() {
        Partial partial = new Partial();
        try {
            partial.getField(0, DateTimeUtils.UTC);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
        partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getField(0, DateTimeUtils.UTC).getFieldType());
    }

    @Test
    public void testWithChronologyRetainFields() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28, DateTimeUtils.UTC);
        Partial newPartial = partial.withChronologyRetainFields(DateTimeUtils.ISO);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(28, newPartial.getValue(0));
        assertEquals(DateTimeUtils.ISO, newPartial.getChronology());
    }

    @Test
    public void testWith() {
        Partial partial = new Partial();
        Partial newPartial = partial.with(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(28, newPartial.getValue(0));
    }

    @Test
    public void testWithout() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        Partial newPartial = partial.without(DateTimeFieldType.dayOfMonth());
        assertEquals(0, newPartial.size());
    }

    @Test
    public void testWithField() {
        Partial partial = new Partial();
        Partial newPartial = partial.withField(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(28, newPartial.getValue(0));
    }

    @Test
    public void testWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        Partial newPartial = partial.withFieldAdded(DateTimeFieldType.minuteOfHour(), 30);
        assertEquals(2, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(28, newPartial.getValue(0));
        assertEquals(DateTimeFieldType.minuteOfHour(), newPartial.getFieldType(1));
        assertEquals(30, newPartial.getValue(1));
    }

    @Test
    public void testWithFieldAddWrapped() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.minuteOfHour(), 30);
        assertEquals(2, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(28, newPartial.getValue(0));
        assertEquals(DateTimeFieldType.minuteOfHour(), newPartial.getFieldType(1));
        assertEquals(29, newPartial.getValue(1));
    }

    @Test
    public void testWithPeriodAdded() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        ReadablePeriod readablePeriod = new GeneralInstantPeriod(new DateMidnight(2012, 12, 31, DateTimeUtils.UTC), new DateMidnight(2012, 12, 31, 23, 59, 59, DateTimeUtils.UTC));
        Partial newPartial = partial.withPeriodAdded(readablePeriod, 1);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(29, newPartial.getValue(0));
    }

    @Test
    public void testPlus() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        Partial newPartial = partial.plus(new GeneralInstantPeriod(new DateMidnight(2012, 12, 31, DateTimeUtils.UTC), new DateMidnight(2013, 1, 1, DateTimeUtils.UTC)));
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(29, newPartial.getValue(0));
    }

    @Test
    public void testMinus() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        Partial newPartial = partial.minus(new GeneralInstantPeriod(new DateMidnight(2013, 1, 1, DateTimeUtils.UTC), new DateMidnight(2012, 12, 31, DateTimeUtils.UTC)));
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(0));
        assertEquals(27, newPartial.getValue(0));
    }

    @Test
    public void testIsMatchWithInstant() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        LocalDateTime localDateTime = new LocalDateTime(2012, 12, 31, 23, 59, 59, 0, DateTimeUtils.UTC);
        assertTrue(partial.isMatch(new LocalDateTimePartial(localDateTime)));
        localDateTime = new LocalDateTime(2012, 12, 30, 23, 59, 59, 0, DateTimeUtils.UTC);
        assertFalse(partial.isMatch(new LocalDateTimePartial(localDateTime)));
    }

    @Test
    public void testIsMatchWithPartial() {
        Partial partial1 = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        Partial partial2 = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertTrue(partial1.isMatch(partial2));
        partial2 = new Partial(DateTimeFieldType.dayOfMonth(), 29);
        assertFalse(partial1.isMatch(partial2));
    }

    @Test
    public void testGetFormatter() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 28);
        assertEquals(ISODateTimeFormat.forFields(new DateTimeFieldType[] { DateTimeFieldType.dayOfMonth() }, true, false), partial.getFormatter());
    }
}