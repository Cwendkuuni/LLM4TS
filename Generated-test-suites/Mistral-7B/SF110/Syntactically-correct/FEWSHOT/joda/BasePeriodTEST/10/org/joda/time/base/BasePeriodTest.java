package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testConstructor_int_int_int_int_int_int_int_int() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = {4, 2, 3, 1, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_long_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = ISOChronology.getInstanceUTC();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = chronology.getValues(BasePeriod.DUMMY_PERIOD, 1234567890L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_ReadableInstant_ReadableInstant_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(instant, instant.plus(Duration.standardDays(1)), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(instant).get(period, instant.getMillis(), instant.getMillis() + Duration.standardDays(1).getMillis());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_ReadablePartial_ReadablePartial_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BaseLocalDate baseLocalDate1 = new LocalDate(2022, 1, 1);
        BaseLocalDate baseLocalDate2 = new LocalDate(2022, 1, 2);
        BasePeriod period = new BasePeriod(baseLocalDate1, baseLocalDate2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(baseLocalDate1.getChronology()).get(period, baseLocalDate1.getLocalMillis(), baseLocalDate2.getLocalMillis());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_ReadableInstant_ReadableDuration_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Duration duration = Duration.standardDays(1);
        BasePeriod period = new BasePeriod(instant, duration, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = new int[8];
        Chronology chronology = DateTimeUtils.getInstantChronology(instant);
        chronology.get(period, instant.getMillis(), instant.getMillis() + duration.getMillis());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_ReadableDuration_ReadableInstant_PeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        Duration duration = Duration.standardDays(1);
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(duration, instant, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = new int[8];
        Chronology chronology = DateTimeUtils.getInstantChronology(instant);
        chronology.get(period, instant.getMillis() - duration.getMillis(), instant.getMillis());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_long() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1234567890L);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, 1234567890L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_long_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = ISOChronology.getInstanceUTC();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = chronology.get(BasePeriod.DUMMY_PERIOD, 1234567890L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_Object_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(1), checkPeriodType, ISOChronology.getInstanceUTC());
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = new int[8];
        DateTimeUtils.getIntervalChronology(new DateTime(), new DateTime().plusDays(1)).get(period, 0L, 0L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testConstructor_int_PeriodType_Chronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(4, checkPeriodType, ISOChronology.getInstanceUTC());
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(8, period.size());
        int[] expectedValues = new int[8];
        DateTimeUtils.getIntervalChronology(new DateTime(), new DateTime().plusYears(4)).get(period, 0L, 0L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testGetPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());

        checkPeriodType = PeriodType.fixed(DurationFieldType.hours(), 24);
        period = new BasePeriod(Duration.standardHours(24), checkPeriodType, ISOChronology.getInstanceUTC());
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(4, period.getValue(DurationFieldType.years().getIndex()));
        assertEquals(2, period.getValue(DurationFieldType.months().getIndex()));
        assertEquals(3, period.getValue(DurationFieldType.weeks().getIndex()));
        assertEquals(1, period.getValue(DurationFieldType.days().getIndex()));
        assertEquals(5, period.getValue(DurationFieldType.hours().getIndex()));
        assertEquals(6, period.getValue(DurationFieldType.minutes().getIndex()));
        assertEquals(7, period.getValue(DurationFieldType.seconds().getIndex()));
        assertEquals(8, period.getValue(DurationFieldType.millis().getIndex()));
    }

    @Test
    public void testToDurationFrom() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(instant, instant.plus(Duration.standardDays(1)), PeriodType.standard());
        Duration duration = period.toDurationFrom(instant);
        assertEquals(Duration.standardDays(1), duration);
    }

    @Test
    public void testToDurationTo() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(instant, instant.plus(Duration.standardDays(1)), PeriodType.standard());
        Duration duration = period.toDurationTo(instant);
        assertEquals(Duration.standardDays(1).negate(), duration);
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = new int[8];
        period.checkAndUpdate(DurationFieldType.years(), values, 4);
        assertEquals(4, values[DurationFieldType.years().getIndex()]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdate_invalidField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = new int[8];
        period.checkAndUpdate(DurationFieldType.HOURS, values, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdate_invalidValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = new int[8];
        period.checkAndUpdate(DurationFieldType.years(), values, -1);
    }

    @Test
    public void testSetPeriod() {
        int[] values = new int[8];
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        ReadablePeriod readablePeriod = new BasePeriod(DurationFieldType.weeks(), 3, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.setPeriod(readablePeriod);
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(readablePeriod.getChronology()).get(BasePeriod.DUMMY_PERIOD, 0L, 0L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetPeriodInternal() {
        int[] values = new int[8];
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        ReadablePeriod readablePeriod = new BasePeriod(DurationFieldType.weeks(), 3, PeriodType.standard(), ISOChronology.getInstanceUTC());
        period.setPeriodInternal(readablePeriod);
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(readablePeriod.getChronology()).get(BasePeriod.DUMMY_PERIOD, 0L, 0L);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetPeriod_null() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriod(null);
        assertArrayEquals(new int[8], period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPeriodInternal_null() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        period.setPeriodInternal(null);
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = {4, 2, 3, 1, 5, 6, 7, 8};
        period.setValues(values);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(8, period.size());
        assertArrayEquals(values, period.getValues());
    }

    @Test
    public void testMergePeriodInto() {
        int[] baseValues = new int[8];
        BasePeriod basePeriod = new BasePeriod(baseValues, PeriodType.standard());
        int[] mergeValues = new int[8];
        ReadablePeriod mergePeriod = new BasePeriod(DurationFieldType.days(), 3, PeriodType.standard(), ISOChronology.getInstanceUTC());
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(mergePeriod.getChronology()).get(BasePeriod.DUMMY_PERIOD, 0L, 0L);
        basePeriod.mergePeriodInto(mergePeriod);
        assertArrayEquals(expectedValues, basePeriod.getValues());
    }

    @Test(expected = NullPointerException.class)
    public void testMergePeriodInto_null() {
        int[] baseValues = new int[8];
        BasePeriod basePeriod = new BasePeriod(baseValues, PeriodType.standard());
        basePeriod.mergePeriodInto(null);
    }

    @Test
    public void testAddPeriodInto() {
        int[] baseValues = new int[8];
        BasePeriod basePeriod = new BasePeriod(baseValues, PeriodType.standard());
        int[] addValues = new int[8];
        ReadablePeriod addPeriod = new BasePeriod(DurationFieldType.weeks(), 3, PeriodType.standard(), ISOChronology.getInstanceUTC());
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(addPeriod.getChronology()).get(BasePeriod.DUMMY_PERIOD, 0L, 0L);
        basePeriod.addPeriodInto(addPeriod);
        assertArrayEquals(expectedValues, basePeriod.getValues());
    }

    @Test(expected = NullPointerException.class)
    public void testAddPeriodInto_null() {
        int[] baseValues = new int[8];
        BasePeriod basePeriod = new BasePeriod(baseValues, PeriodType.standard());
        basePeriod.addPeriodInto(null);
    }
}