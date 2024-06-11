package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testConstructor() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(chronology, field.getChronology());
        assertEquals(2, field.getLeapMonth());
    }

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2015-02-28T12:34:56.789Z").toInstant().toEpochMilli();
        int value = field.get(date);
        assertEquals(2, value);
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2015-02-28T12:34:56.789Z").toInstant().toEpochMilli();

        long newDate = field.add(date, 1);
        assertEquals(LocalDateTime.parse("2015-03-28T12:34:56.789Z").toInstant().toEpochMilli(), newDate);

        newDate = field.add(date, 12);
        assertEquals(LocalDateTime.parse("2015-03-28T12:34:56.789Z").plusMonths(1).toInstant().toEpochMilli(), newDate);

        newDate = field.add(date, -1);
        assertEquals(LocalDateTime.parse("2015-03-27T12:34:56.789Z").toInstant().toEpochMilli(), newDate);

        newDate = field.add(date, -12);
        assertEquals(LocalDateTime.parse("2015-02-27T12:34:56.789Z").toInstant().toEpochMilli(), newDate);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2015-02-28T12:34:56.789Z").toInstant().toEpochMilli();

        long newDate = field.addWrapField(date, 13);
        assertEquals(LocalDateTime.parse("2015-03-28T12:34:56.789Z").toInstant().toEpochMilli(), newDate);

        newDate = field.addWrapField(date, -13);
        assertEquals(LocalDateTime.parse("2015-01-28T12:34:56.789Z").toInstant().toEpochMilli(), newDate);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date1 = LocalDateTime.parse("2015-02-28T12:34:56.789Z").toInstant().toEpochMilli();
        long date2 = LocalDateTime.parse("2015-03-31T12:34:56.789Z").toInstant().toEpochMilli();

        long diff = field.getDifferenceAsLong(date1, date2);
        assertEquals(-1, diff);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2015-02-28T12:34:56.789Z").toInstant().toEpochMilli();

        long newDate = field.set(date, 3);
        assertEquals(LocalDateTime.parse("2015-03-28T12:34:56.789Z").toInstant().toEpochMilli(), newDate);

        newDate = field.set(date, 13);
        assertEquals(LocalDateTime.parse("2015-04-28T12:34:56.789Z").toInstant().toEpochMilli(), newDate);

        newDate = field.set(date, 0);
        assertEquals(LocalDateTime.parse("2015-01-28T12:34:56.789Z").toInstant().toEpochMilli(), newDate);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), rangeDurationField.getType());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2016-02-29T12:34:56.789Z").toInstant().toEpochMilli();
        assertTrue(field.isLeap(date));

        date = LocalDateTime.parse("2015-03-01T12:34:56.789Z").toInstant().toEpochMilli();
        assertFalse(field.isLeap(date));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2016-02-29T12:34:56.789Z").toInstant().toEpochMilli();
        assertEquals(1, field.getLeapAmount(date));

        date = LocalDateTime.parse("2015-03-01T12:34:56.789Z").toInstant().toEpochMilli();
        assertEquals(0, field.getLeapAmount(date));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), leapDurationField.getType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2015-03-01T12:34:56.789Z").toInstant().toEpochMilli();
        assertEquals(LocalDateTime.parse("2015-03-01T00:00:00.000Z").toInstant().toEpochMilli(), field.roundFloor(date));
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long date = LocalDateTime.parse("2015-03-01T12:34:56.789Z").toInstant().toEpochMilli();
        assertEquals(0, field.remainder(date));

        date += 500;
        assertEquals(500, field.remainder(date));
    }
}