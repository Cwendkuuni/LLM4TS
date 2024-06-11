package org.joda.time;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertEquals(DateTimeUtils.UTC, emptyPartial.getChronology());
        assertNull(emptyPartial.getFieldTypes());
        assertArrayEquals(new int[0], emptyPartial.getValues());
    }

    @Test
    public void testSingleFieldPartial() {
        DateTimeFieldType fieldType = DateTimeFieldType.year();
        Partial singleFieldPartial = new Partial(fieldType, 2021);
        assertEquals(1, singleFieldPartial.size());
        assertEquals(DateTimeUtils.UTC, singleFieldPartial.getChronology());
        DateTimeFieldType[] fieldTypes = singleFieldPartial.getFieldTypes();
        assertArrayEquals(new DateTimeFieldType[]{fieldType}, fieldTypes);
        int[] values = singleFieldPartial.getValues();
        assertEquals(2021, values[0]);
    }

    @Test
    public void testMultipleFieldsPartial() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        Partial multipleFieldsPartial = new Partial(yearType, 2021, monthType, 11, dayOfMonthType, 30);
        assertEquals(3, multipleFieldsPartial.size());
        assertEquals(DateTimeUtils.UTC, multipleFieldsPartial.getChronology());
        DateTimeFieldType[] fieldTypes = multipleFieldsPartial.getFieldTypes();
        assertArrayEquals(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType}, fieldTypes);
        int[] values = multipleFieldsPartial.getValues();
        assertEquals(2021, values[0]);
        assertEquals(11, values[1]);
        assertEquals(30, values[2]);
    }

    @Test
    public void testSettingFieldsInPartial() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial();
        partial = partial.with(yearType, 2021);
        partial = partial.with(monthType, 11);
        partial = partial.with(dayOfMonthType, 30);
        assertEquals(3, partial.size());
        int[] values = partial.getValues();
        assertEquals(2021, values[0]);
        assertEquals(11, values[1]);
        assertEquals(30, values[2]);
    }

    @Test
    public void testAddingFieldsToPartial() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial();
        partial = partial.with(yearType, 2021);
        partial = partial.with(monthType, 11);
        partial = partial.with(dayOfMonthType, 30);
        Partial newPartial = partial.with(yearType, 2022);
        assertEquals(4, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(2022, values[0]);
        assertEquals(11, values[1]);
        assertEquals(30, values[2]);
        assertEquals(2021, values[3]);
    }

    @Test
    public void testRemovingFieldsFromPartial() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(yearType, 2021, monthType, 11, dayOfMonthType, 30);
        Partial newPartial = partial.without(dayOfMonthType);
        assertEquals(2, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(2021, values[0]);
        assertEquals(11, values[1]);
    }

    @Test
    public void testSettingFieldValueInPartial() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial();
        partial = partial.with(yearType, 2021);
        partial = partial.with(monthType, 11);
        partial = partial.with(dayOfMonthType, 30);
        Partial newPartial = partial.withField(dayOfMonthType, 31);
        assertEquals(3, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(2021, values[0]);
        assertEquals(11, values[1]);
        assertEquals(31, values[2]);
    }

    @Test
    public void testAddingFieldValueToPartial() {
        DateTimeFieldType dayOfYearType = DateTimeFieldType.dayOfYear();
        DateTimeFieldType partialDayType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial();
        Partial newPartial = partial.withFieldAdded(dayOfYearType, 10);
        assertEquals(2, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(0, values[0]);
        assertEquals(10, values[1]);
    }

    @Test
    public void testAddingWrappedFieldValueToPartial() {
        DateTimeFieldType dayOfWeekType = DateTimeFieldType.dayOfWeek();
        DateTimeFieldType dayOfYearType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial();
        Partial newPartial = partial.withFieldAddWrapped(dayOfWeekType, -1);
        assertEquals(1, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(DateTimeConstants.SATURDAY, values[0]);
    }

    @Test
    public void testAddingPeriodToPartial() {
        DateTimeFieldType hourOfDayType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteType = DateTimeFieldType.minute();
        DateTimeFieldType secondType = DateTimeFieldType.second();
        ReadablePeriod period = DurationFieldType.standardHours(1).plus(DurationFieldType.standardMinutes(30));
        Partial partial = new Partial();
        Partial newPartial = partial.withPeriodAdded(period, 1);
        assertEquals(3, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(1, values[hourOfDayType.getIndex()]);
        assertEquals(30, values[minuteType.getIndex()]);
        assertEquals(30, values[secondType.getIndex()]);
    }

    @Test
    public void testAddingPeriodToPartialWithWrappedFields() {
        DateTimeFieldType hourOfDayType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteType = DateTimeFieldType.minute();
        DateTimeFieldType secondType = DateTimeFieldType.second();
        DateTimeFieldType dayOfWeekType = DateTimeFieldType.dayOfWeek();
        ReadablePeriod period = DurationFieldType.standardHours(1).plus(DurationFieldType.standardMinutes(30));
        Partial partial = new Partial();
        partial = partial.withFieldAddWrapped(dayOfWeekType, -1);
        Partial newPartial = partial.withPeriodAdded(period, 1);
        assertEquals(4, newPartial.size());
        int[] values = newPartial.getValues();
        assertEquals(DateTimeConstants.SATURDAY, values[dayOfWeekType.getIndex()]);
        assertEquals(1, values[hourOfDayType.getIndex()]);
        assertEquals(30, values[minuteType.getIndex()]);
        assertEquals(30, values[secondType.getIndex()]);
    }
}