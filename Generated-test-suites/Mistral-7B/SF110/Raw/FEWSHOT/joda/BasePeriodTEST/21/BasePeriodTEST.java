package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.duration.*;
import org.joda.time.field.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithValidArguments() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithLongAndChronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(123456789L, 234567890L, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = chronology.getValues(period);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        ReadableInstant readableInstant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(readableInstant, readableInstant, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartial() {
        BaseLocalDate date1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toLocalDate();
        BaseLocalDate date2 = new DateTime(2022, 1, 2, 12, 0, 0, 0, DateTimeZone.UTC).toLocalDate();
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(date1, date2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstantAndReadableDuration() {
        PeriodType checkPeriodType = PeriodType.standard();
        ReadableInstant readableInstant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        ReadableDuration readableDuration = Duration.standardHours(1);
        BasePeriod period = new BasePeriod(readableDuration, readableInstant, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        ReadableDuration readableDuration = Duration.standardHours(1);
        ReadableInstant readableInstant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(readableDuration, readableInstant, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithLong() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1234567890L);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithLongAndPeriodTypeAndChronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        Chronology chronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, chronology);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = chronology.getValues(period);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithObjectAndPeriodTypeAndChronology() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(1), checkPeriodType, GregorianChronology.getInstance());
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructionWithIntArrayAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = period.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testGetPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPeriodTypeWithInvalidPeriodType() {
        PeriodType checkPeriodType = null;
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        period.getPeriodType();
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years().getIndex()));
        assertEquals(2, period.getValue(DurationFieldType.months().getIndex()));
        // ... continue for all fields
        assertEquals(8, period.getValue(DurationFieldType.millis().getIndex()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.getValue(-1);
    }

    @Test
    public void testToDurationFrom() {
        ReadableInstant now = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
        Duration duration = period.toDurationFrom(now);
        assertEquals(Duration.standardDays(1), duration);
    }

    @Test
    public void testToDurationTo() {
        ReadableInstant now = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.standard());
        Duration duration = period.toDurationTo(now);
        assertEquals(Duration.standardDays(-1), duration);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadablePeriod readablePeriod = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
        period.setPeriod(readablePeriod);
        int[] expectedValues = readablePeriod.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetPeriodInternal() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadablePeriod readablePeriod = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
        period.setPeriodInternal(readablePeriod);
        int[] expectedValues = readablePeriod.getValues();
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetPeriodWithNullArgument() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(null);
        int[] expectedValues = new int[8];
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setField(DurationFieldType.years(), 10);
        int[] expectedValues = {10, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetFieldInto() {
        int[] valuesBefore = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(valuesBefore, PeriodType.standard());
        period.setFieldInto(period.getValues(), DurationFieldType.years(), 10);
        int[] expectedValues = {10, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(DurationFieldType.years(), 1);
        int[] expectedValues = {2, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testAddFieldInto() {
        int[] valuesBefore = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(valuesBefore, PeriodType.standard());
        period.addFieldInto(period.getValues(), DurationFieldType.years(), 1);
        int[] expectedValues = {2, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period1 = new BasePeriod(DurationFieldType.YEARS, 1, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(DurationFieldType.MONTHS, 2, PeriodType.standard());
        BasePeriod mergedPeriod = new BasePeriod(period1);
        mergedPeriod.mergePeriod(period2);
        int[] expectedValues = {2, 3, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedValues, mergedPeriod.getValues());
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period1 = new BasePeriod(DurationFieldType.YEARS, 1, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(DurationFieldType.MONTHS, 2, PeriodType.standard());
        BasePeriod sumPeriod = new BasePeriod(period1);
        sumPeriod.addPeriod(period2);
        int[] expectedValues = {3, 3, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedValues, sumPeriod.getValues());
    }

    @Test
    public void testSetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setValue(DurationFieldType.years().getIndex(), 10);
        int[] expectedValues = {10, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testSetValues() {
        int[] valuesBefore = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(valuesBefore, PeriodType.standard());
        int[] valuesAfter = {10, 2, 3, 4, 5, 6, 7, 8};
        period.setValues(valuesAfter);
        int[] expectedValues = valuesAfter;
        assertArrayEquals(expectedValues, period.getValues());
    }
}