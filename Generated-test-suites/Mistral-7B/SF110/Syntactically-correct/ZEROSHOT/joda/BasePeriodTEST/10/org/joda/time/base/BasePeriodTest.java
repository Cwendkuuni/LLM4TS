package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.junit.*;

public class BasePeriodTest {

    @Test
    public void testConstructorWithInts() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        assertEquals(1, values[0]);
        assertEquals(2, values[1]);
        assertEquals(3, values[2]);
        assertEquals(4, values[3]);
        assertEquals(5, values[4]);
        assertEquals(6, values[5]);
        assertEquals(7, values[6]);
        assertEquals(8, values[7]);
    }

    @Test
    public void testConstructorWithLong() {
        BasePeriod period = new BasePeriod(1234567890L);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithInstantAndPeriodType() {
        BasePeriod period = new BasePeriod(DateTime.now(), PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithInstantAndChronology() {
        BasePeriod period = new BasePeriod(DateTime.now(), DateTimeUtils.UTC);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithReadableInstantAndReadableInstant() {
        BasePeriod period = new BasePeriod(DateTime.now(), DateTime.now().plusDays(1));
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithReadablePartialAndReadablePartial() {
        LocalDateTime dateTime = LocalDateTime.now();
        BasePeriod period = new BasePeriod(new BaseLocalDateTime(dateTime), new BaseLocalDateTime(dateTime.plusHours(1)), PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithReadableInstantAndReadableDuration() {
        LocalDateTime dateTime = LocalDateTime.now();
        BasePeriod period = new BasePeriod(dateTime, Duration.standardHours(1));
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithReadableDurationAndReadableInstant() {
        LocalDateTime dateTime = LocalDateTime.now();
        BasePeriod period = new BasePeriod(Duration.standardHours(1), dateTime);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithObject() {
        Duration duration = Duration.standardHours(1);
        BasePeriod period = new BasePeriod(duration, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testConstructorWithIntsAndPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertEquals(8, values.length);
        assertEquals(1, values[0]);
        assertEquals(2, values[1]);
        assertEquals(3, values[2]);
        assertEquals(4, values[3]);
        assertEquals(5, values[4]);
        assertEquals(6, values[5]);
        assertEquals(7, values[6]);
        assertEquals(8, values[7]);
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.months().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(7, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(8, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testToDurationFrom() {
        LocalDateTime dateTime = LocalDateTime.now();
        BasePeriod period = new BasePeriod(dateTime, dateTime.plusDays(1));
        Duration duration = period.toDurationFrom(dateTime);
        assertEquals(Duration.standardDays(1), duration);
    }

    @Test
    public void testToDurationTo() {
        LocalDateTime dateTime = LocalDateTime.now();
        BasePeriod period = new BasePeriod(dateTime, dateTime.plusDays(1));
        Duration duration = period.toDurationTo(dateTime);
        assertEquals(Duration.standardDays(1), duration.negate());
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = period.getValues();
        int[] newValues = new int[8];
        period.checkAndUpdate(DurationFieldType.days(), newValues, 5);
        assertEquals(8, newValues.length);
        assertEquals(1, newValues[0]);
        assertEquals(2, newValues[1]);
        assertEquals(3, newValues[2]);
        assertEquals(4, newValues[3]);
        assertEquals(5, newValues[4]);
        assertEquals(6, newValues[5]);
        assertEquals(7, newValues[6]);
        assertEquals(8, newValues[7]);
        assertEquals(5, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testSetPeriod() {
        LocalDateTime dateTime = LocalDateTime.now();
        BasePeriod period1 = new BasePeriod(dateTime, dateTime.plusDays(1));
        BasePeriod period2 = new BasePeriod();
        period2.setPeriod(period1);
        assertEquals(PeriodType.standard(), period2.getPeriodType());
        int[] values = period2.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
        assertEquals(1, period2.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, period2.getValue(DurationFieldType.months().getCode()));
        assertEquals(0, period2.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(1, period2.getValue(DurationFieldType.days().getCode()));
        assertEquals(0, period2.getValue(DurationFieldType.hours().getCode()));
        assertEquals(0, period2.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, period2.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(0, period2.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testSetPeriodInternal() {
        LocalDateTime dateTime = LocalDateTime.now();
        int[] values = new int[8];
        BasePeriod period = new BasePeriod();
        period.setPeriodInternal(new BasePeriod(dateTime, dateTime.plusDays(1)));
        assertEquals(PeriodType.standard(), period.getPeriodType());
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
        assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.months().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setField(DurationFieldType.days(), 5);
        assertEquals(5, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testSetFieldInto() {
        int[] values = new int[8];
        BasePeriod period = new BasePeriod();
        period.setFieldInto(values, DurationFieldType.days(), 5);
        assertEquals(8, values.length);
        assertEquals(0, values[0]);
        assertEquals(0, values[1]);
        assertEquals(0, values[2]);
        assertEquals(0, values[3]);
        assertEquals(0, values[4]);
        assertEquals(0, values[5]);
        assertEquals(0, values[6]);
        assertEquals(5, values[7]);
        assertEquals(5, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(DurationFieldType.days(), 5);
        assertEquals(6, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testAddFieldInto() {
        int[] values = new int[8];
        BasePeriod period = new BasePeriod();
        period.addFieldInto(values, DurationFieldType.days(), 5);
        assertEquals(8, values.length);
        assertEquals(0, values[0]);
        assertEquals(0, values[1]);
        assertEquals(0, values[2]);
        assertEquals(0, values[3]);
        assertEquals(0, values[4]);
        assertEquals(0, values[5]);
        assertEquals(0, values[6]);
        assertEquals(5, values[7]);
        assertEquals(5, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
        period1.mergePeriod(period2);
        assertEquals(8, period1.getValues().length);
        assertEquals(4, period1.getValue(DurationFieldType.days().getCode()));
        assertEquals(8, period1.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
        period1.addPeriod(period2);
        assertEquals(8, period1.getValues().length);
        assertEquals(4, period1.getValue(DurationFieldType.days().getCode()));
        assertEquals(8, period1.getValue(DurationFieldType.seconds().getCode()));
    }

    @Test
    public void testConstructorWithIntsAndChronology() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod periodWithUTC = new BasePeriod(period, DateTimeUtils.UTC);
        PeriodType periodType = periodWithUTC.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = periodWithUTC.getValues();
        assertEquals(8, values.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(0, values[i]);
        }
    }

    @Test
    public void testToString() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        String string = period.toString();
        String format = ISODateTimeFormat.standard().print(DateTime.now().plus(period));
        assertEquals(format, string);
    }
}