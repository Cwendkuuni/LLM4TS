package org.joda.time.base; // Make sure to import all necessary packages and classes

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.convert.Defaults;
import org.junit.*;

public class BasePeriodTest {

    

    @Test
    public void testConstructorWithGivenValues() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values);
    }

    @Test
    public void testConstructorWithGivenChronologyAndLongValues() {
        BasePeriod period = new BasePeriod(1234567890123L, 2345678901234L, PeriodType.standard(), GregorianChronology.getInstanceUTC());
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values);
    }

    @Test
    public void testConstructorWithGivenInstantAndReadableDuration() {
        BasePeriod period = new BasePeriod(new Instant(1234567890123L), new Duration(1, 2, 3, 4, 5, 6, 7, 8), PeriodType.standard());
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values);
    }

    @Test
    public void testConstructorWithGivenInstantAndReadablePeriod() {
        BasePeriod period1 = new BasePeriod(new Instant(1234567890123L), new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), PeriodType.standard());
        PeriodType type1 = period1.getPeriodType();
        int[] values1 = period1.getValues();
        assertEquals(PeriodType.standard(), type1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values1);

        BasePeriod period2 = new BasePeriod(new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), new Instant(1234567890123L), PeriodType.standard());
        PeriodType type2 = period2.getPeriodType();
        int[] values2 = period2.getValues();
        assertEquals(PeriodType.standard(), type2);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values2);
    }

    @Test
    public void testConstructorWithGivenReadablePartial1() {
        BasePeriod period = new BasePeriod(new LocalDateTime(2022, 1, 1, 0, 0), new LocalDateTime(2022, 1, 1, 1, 1), PeriodType.standard());
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 1, 0, 0, 1, 0, 0, 0}, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullReadablePartial1() {
        new BasePeriod(null, new LocalDateTime(2022, 1, 1, 1, 1), PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonContiguousReadablePartial1() {
        LocalDateTime readablePartial1 = new LocalDateTime(2022, 1, 1, 0, 0);
        LocalDateTime readablePartial2 = new LocalDateTime(2022, 1, 2, 0, 0);
        new BasePeriod(readablePartial1, readablePartial2, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDifferentFieldSetsReadablePartial1() {
        LocalDateTime readablePartial1 = new LocalDateTime(2022, 1, 1, 0, 0);
        LocalDateTime readablePartial2 = new LocalDateTime(2022, 1, 1, 0, 1);
        new BasePeriod(readablePartial1, readablePartial2, PeriodType.standard());
    }

    @Test
    public void testConstructorWithGivenReadablePartial2() {
        BasePeriod period = new BasePeriod(new BaseLocalDateTime(2022, 1, 1, 0, 0, Defaults.UTC), new BaseLocalDateTime(2022, 1, 1, 1, 1, Defaults.UTC), PeriodType.standard());
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 1, 0, 0, 1, 0, 0, 0}, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullReadablePartial2() {
        new BasePeriod(new BaseLocalDateTime(2022, 1, 1, 0, 0, Defaults.UTC), null, PeriodType.standard());
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationFrom(new Instant(1234567890123L));
        PeriodType type = duration.getPeriodType();
        int[] values = duration.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToDurationFromWithNullReadableInstant() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.toDurationFrom(null);
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(new Instant(1234567890123L));
        PeriodType type = duration.getPeriodType();
        int[] values = duration.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{-1, -2, -3, -4, -5, -6, -7, -8}, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToDurationToWithNullReadableInstant() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.toDurationTo(null);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod periodInternal = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
        period.setPeriod(periodInternal);
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1, 1, 1}, values);
    }

    @Test(expected = NullPointerException.class)
    public void testSetPeriodWithNullPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(null);
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = period.getValues();
        period.checkAndUpdate(DurationFieldType.years(), values, 1);
        assertEquals(2, values[0]);
        period.checkAndUpdate(DurationFieldType.years(), values, -1);
        assertEquals(1, values[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdateWithNullDurationFieldType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = period.getValues();
        period.checkAndUpdate(null, values, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdateWithNegativeValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = period.getValues();
        period.checkAndUpdate(DurationFieldType.years(), values, -1);
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setField(DurationFieldType.years(), 10);
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertEquals(10, values[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldWithNullDurationFieldType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setField(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldWithNegativeValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setField(DurationFieldType.years(), -1);
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(DurationFieldType.years(), 1);
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertEquals(2, values[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFieldWithNullDurationFieldType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFieldWithNegativeValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(DurationFieldType.years(), -1);
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
        period1.mergePeriod(period2);
        PeriodType type = period1.getPeriodType();
        int[] values = period1.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{2, 3, 4, 5, 7, 7, 8, 9}, values);
    }

    @Test(expected = NullPointerException.class)
    public void testMergePeriodWithNullPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.mergePeriod(null);
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
        period1.addPeriod(period2);
        PeriodType type = period1.getPeriodType();
        int[] values = period1.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(new int[]{2, 3, 4, 5, 7, 8, 9, 10}, values);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPeriodWithNullPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addPeriod(null);
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] newValues = {11, 12, 13, 14, 15, 16, 17, 18};
        period.setValues(newValues);
        PeriodType type = period.getPeriodType();
        int[] values = period.getValues();
        assertEquals(PeriodType.standard(), type);
        assertArrayEquals(newValues, values);
    }

    
}