package org.joda.time.test;

import org.joda.time.BaseTestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartialTest extends BaseTestCase {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertTrue(emptyPartial.isMatch(new LocalDateTime()));
    }

    @Test
    public void testSimplePartial() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial simplePartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 5, chronology);
        assertEquals(1, simplePartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, simplePartial.getFieldTypes()[0]);
        assertEquals(5, simplePartial.getValue(0));

        LocalDateTime date = new LocalDateTime(2022, 11, 5, 12, 0);
        assertTrue(simplePartial.isMatch(date));
        assertFalse(simplePartial.isMatch(new LocalDateTime(2022, 11, 4, 12, 0)));
    }

    @Test
    public void testComplexPartial() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial complexPartial = new Partial(new DateTimeFieldType[] {
                DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR, DateTimeFieldType.YEAR
        }, new int[] { 5, 11, 2022 });
        assertEquals(3, complexPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, complexPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, complexPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.YEAR, complexPartial.getFieldTypes()[2]);
        assertEquals(5, complexPartial.getValue(0));
        assertEquals(11, complexPartial.getValue(1));
        assertEquals(2022, complexPartial.getValue(2));

        LocalDateTime date = new LocalDateTime(2022, 11, 5, 12, 0);
        assertTrue(complexPartial.isMatch(date));
        assertFalse(complexPartial.isMatch(new LocalDateTime(2022, 10, 5, 12, 0)));
    }

    @Test
    public void testInvalidComplexPartial() {
        try {
            Partial complexPartial = new Partial(new DateTimeFieldType[] {
                    DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR, DateTimeFieldType.YEAR
            }, new int[] { 5, 12, 2022 });
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Types array must be in order largest-smallest: MONTH_OF_YEAR < YEAR", e.getMessage());
        }
    }

    @Test
    public void testWithChronologyRetainFields() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial(new DateTimeFieldType[] {
                DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR, DateTimeFieldType.YEAR
        }, new int[] { 5, 11, 2022 }, chronology);
        Partial newPartial = partial.withChronologyRetainFields(DateTimeUtils.getGregorianCalendar());
        assertEquals(3, newPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.YEAR, newPartial.getFieldTypes()[2]);
        assertEquals(5, newPartial.getValue(0));
        assertEquals(11, newPartial.getValue(1));
        assertEquals(2022, newPartial.getValue(2));
    }

    @Test
    public void testAddField() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        Partial newPartial = partial.with(DateTimeFieldType.HOUR_OF_DAY, 12);
        assertEquals(4, newPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.YEAR, newPartial.getFieldTypes()[2]);
        assertEquals(DateTimeFieldType.HOUR_OF_DAY, newPartial.getFieldTypes()[3]);
        assertEquals(5, newPartial.getValue(0));
        assertEquals(11, newPartial.getValue(1));
        assertEquals(2022, newPartial.getValue(2));
        assertEquals(12, newPartial.getValue(3));
    }

    @Test
    public void testAddFieldWithValueGreaterThanMax() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        try {
            Partial newPartial = partial.with(DateTimeFieldType.DAY_OF_MONTH, DateTimeConstants.MAX_VALUE_DAY_OF_MONTH + 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Value is out of range for field: DAY_OF_MONTH", e.getMessage());
        }
    }

    @Test
    public void testAddFieldWithValueLessThanMin() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        try {
            Partial newPartial = partial.with(DateTimeFieldType.DAY_OF_MONTH, DateTimeConstants.MIN_VALUE_DAY_OF_MONTH - 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Value is out of range for field: DAY_OF_MONTH", e.getMessage());
        }
    }

    @Test
    public void testAddWrapField() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 31);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 12);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.DAY_OF_MONTH, 1);
        assertEquals(4, newPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.YEAR, newPartial.getFieldTypes()[2]);
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[3]);
        assertEquals(31, newPartial.getValue(0));
        assertEquals(1, newPartial.getValue(3));
    }

    @Test
    public void testAddWrapFieldWithValueGreaterThanMax() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, DateTimeConstants.MAX_VALUE_DAY_OF_MONTH);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 12);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        try {
            Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.DAY_OF_MONTH, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Value is out of range for field: DAY_OF_MONTH", e.getMessage());
        }
    }

    @Test
    public void testAddWrapFieldWithValueLessThanMin() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 0);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 12);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        try {
            Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.DAY_OF_MONTH, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Value is out of range for field: DAY_OF_MONTH", e.getMessage());
        }
    }

    @Test
    public void testAddPeriod() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        Period period = new Period(1, 2, 3);
        Partial newPartial = partial.withPeriodAdded(period, 1);
        assertEquals(4, newPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.YEAR, newPartial.getFieldTypes()[2]);
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[3]);
        assertEquals(6, newPartial.getValue(0));
        assertEquals(12, newPartial.getValue(1));
        assertEquals(2023, newPartial.getValue(2));
        assertEquals(1, newPartial.getValue(3));
    }

    @Test
    public void testAddPeriodWithNegativeMultiplier() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        Period period = new Period(1, 2, 3);
        Partial newPartial = partial.withPeriodAdded(period, -1);
        assertEquals(4, newPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.YEAR, newPartial.getFieldTypes()[2]);
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldTypes()[3]);
        assertEquals(4, newPartial.getValue(0));
        assertEquals(10, newPartial.getValue(1));
        assertEquals(2021, newPartial.getValue(2));
        assertEquals(28, newPartial.getValue(3));
    }

    @Test
    public void testToString() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        assertEquals("[YEAR=2022, MONTH_OF_YEAR=11, DAY_OF_MONTH=5]", partial.toString());
    }

    @Test
    public void testToStringList() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        assertEquals("[YEAR=2022, MONTH_OF_YEAR=11, DAY_OF_MONTH=5]", partial.toStringList());
    }

    @Test
    public void testToStringFormat() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        assertEquals("2022-11-05", partial.toString("yyyy-MM-dd"));
        assertEquals("November 5, 2022", partial.toString("MMMM d, yyyy"));
    }

    @Test
    public void testToStringFormatLocale() {
        Chronology chronology = DateTimeUtils.getCaledarInstances().getISOCalendar();
        Partial partial = new Partial();
        partial = partial.with(DateTimeFieldType.DAY_OF_MONTH, 5);
        partial = partial.with(DateTimeFieldType.MONTH_OF_YEAR, 11);
        partial = partial.with(DateTimeFieldType.YEAR, 2022);

        assertEquals("2022-11-05", partial.toString("yyyy-MM-dd", Locale.ENGLISH));
        assertEquals("November 5, 2022", partial.toString("MMMM d, yyyy", Locale.ENGLISH));
    }
}