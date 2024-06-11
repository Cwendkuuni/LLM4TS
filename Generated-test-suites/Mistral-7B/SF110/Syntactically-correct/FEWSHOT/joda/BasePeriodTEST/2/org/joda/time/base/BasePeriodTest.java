package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.duration.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testConstructorWithValuesAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @Test
    public void testConstructorWithMillisAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, DateTimeUtils.UTC);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{3, 2, 1, 1, 5, 2, 4, 6}, period.iValues);
    }

    @Test
    public void testConstructorWithInstantAndInstantAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-02"), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{-31, 0, 0, 0, 0, 0, 1, 0}, period.iValues);
    }

    @Test
    public void testConstructorWithReadablePartialAndReadablePartialAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeFieldType.dayOfYear().getLocalDate(DateTimeUtils.currentTime()), DateTimeFieldType.dayOfMonth().getLocalDate(DateTimeUtils.currentTime()), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{DateTimeUtils.get(DateTimeFieldType.dayOfYear(), DateTimeUtils.currentTime()), -DateTimeUtils.get(DateTimeFieldType.dayOfMonth(), DateTimeUtils.currentTime())}, period.iValues);
    }

    @Test
    public void testConstructorWithInstantAndDurationAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), Duration.standardHours(2), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{DateTimeUtils.get(DateTimeFieldType.dayOfYear(), DateTimeUtils.parseLocalDate("2022-01-01")), 2}, period.iValues);
    }

    @Test
    public void testConstructorWithDurationAndInstantAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardHours(2), DateTimeUtils.parseLocalDate("2022-01-01"), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{2, DateTimeUtils.get(DateTimeFieldType.dayOfYear(), DateTimeUtils.parseLocalDate("2022-01-01"))}, period.iValues);
    }

    @Test
    public void testConstructorWithMillis() {
        BasePeriod period = new BasePeriod(1234567890L);
        assertNull(period.getPeriodType());
        assertArrayEquals(new int[]{3, 2, 1, 1, 5, 2, 4, 6}, period.iValues);
    }

    @Test
    public void testConstructorWithMillisAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{3, 2, 1, 1, 5, 2, 4, 6}, period.iValues);
    }

    @Test
    public void testConstructorWithObjectAndPeriodTypeAndChronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(3), checkPeriodType, DateTimeUtils.UTC);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{3, 0, 0, 0, 0, 0, 0, 0}, period.iValues);
    }

    @Test
    public void testConstructorWithIntArrayAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(5, period.getValue(DurationFieldType.seconds()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueInvalidFieldType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.getValue(DurationFieldType.years());
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationFrom(DateTimeUtils.parseLocalDate("2022-01-01"));
        assertEquals(Duration.standardDays(1), duration);
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(DateTimeUtils.parseLocalDate("2022-01-01"));
        assertEquals(Duration.standardDays(-1), duration);
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.checkAndUpdate(DurationFieldType.years(), new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 0);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);

        period.checkAndUpdate(DurationFieldType.years(), new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 1);
        assertArrayEquals(new int[]{2, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        ReadablePeriod periodInternal = DateTimeUtils.parsePeriod("PT1S");
        period.setPeriod(periodInternal);
        assertEquals(DurationFieldType.seconds(), period.getPeriodType().getFields()[0]);
        assertEquals(Duration.standardSeconds(1).getValues(), period.iValues);
    }

    @Test
    public void testSetPeriodInternal() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        ReadablePeriod periodInternal = DateTimeUtils.parsePeriod("PT1S");
        period.setPeriodInternal(periodInternal);
        assertEquals(DurationFieldType.seconds(), period.getPeriodType().getFields()[0]);
        assertEquals(Duration.standardSeconds(1).getValues(), period.iValues);
    }

    @Test
    public void testSetPeriodAndSetPeriodInternal() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        ReadablePeriod periodInternal = DateTimeUtils.parsePeriod("PT1S");
        period.setPeriod(periodInternal);
        period.setPeriodInternal(new int[]{2, 2, 3, 4, 5, 6, 7, 8});
        assertEquals(new int[]{2, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @Test
    public void testMergePeriodInto() {
        BasePeriod period1 = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{1, 1, 2, 3, 4, 5, 6, 7}, PeriodType.standard());
        int[] result = period1.mergePeriodInto(period1.iValues, period2);
        assertEquals(new int[]{2, 3, 3, 7, 5, 6, 6, 7}, result);
    }

    @Test
    public void testAddPeriodInto() {
        BasePeriod period1 = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{1, 1, 2, 3, 4, 5, 6, 7}, PeriodType.standard());
        int[] result = period1.addPeriodInto(period1.iValues, period2);
        assertEquals(new int[]{2, 3, 4, 7, 5, 6, 6, 8}, result);
    }
}