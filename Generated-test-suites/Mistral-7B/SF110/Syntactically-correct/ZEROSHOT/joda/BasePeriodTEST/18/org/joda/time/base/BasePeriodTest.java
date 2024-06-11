package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.joda.time.parse.*;

import java.io.*;
import java.util.*;

public class BasePeriodTest {

    @org.junit.Test
    public void testBasePeriodConstruction() {
        // Test empty construction
        BasePeriod period = new BasePeriod();
        assertEquals(PeriodType.time(), period.getPeriodType());
        assertArrayEquals(new int[8], period.iValues);

        // Test construction with ints
        period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);

        // Test construction with longs
        period = new BasePeriod(9, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{9, 0, 0, 0, 0, 0, 0, 0}, period.iValues);

        // Test construction with ReadableInstant
        DateTime baseDate = new DateTime(0L, DateTimeZone.UTC);
        period = new BasePeriod(baseDate, baseDate.plusDays(1), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 0}, period.iValues);

        // Test construction with ReadablePartial
        BaseLocalDateTime baseLocalDate = new BaseLocalDateTime(0L, DateTimeZone.UTC);
        period = new BasePeriod(baseLocalDate, baseLocalDate.plusDays(1), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 0}, period.iValues);

        // Test construction with ReadableInstant and ReadableDuration
        period = new BasePeriod(baseDate, new Duration(2, TimeUnit.DAYS), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{2, 0, 0, 0, 0, 0, 0, 0}, period.iValues);

        // Test construction with ReadableDuration and ReadableInstant
        period = new BasePeriod(new Duration(2, TimeUnit.DAYS), baseDate, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 2, 0, 0, 0, 0}, period.iValues);

        // Test construction with Object (Period)
        BasePeriod otherPeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period = new BasePeriod(otherPeriod, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(otherPeriod.iValues, period.iValues);

        // Test construction with ints and PeriodType
        period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @org.junit.Test
    public void testBasePeriodGetters() {
        // Test getPeriodType
        BasePeriod period = new BasePeriod();
        assertEquals(PeriodType.time(), period.getPeriodType());

        period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(9, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(new DateTime(0L, DateTimeZone.UTC), new DateTime(1L, DateTimeZone.UTC), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(new BaseLocalDateTime(0L, DateTimeZone.UTC), new BaseLocalDateTime(1L, DateTimeZone.UTC), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(new DateTime(0L, DateTimeZone.UTC), new Duration(2, TimeUnit.DAYS), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(new Duration(2, TimeUnit.DAYS), new DateTime(0L, DateTimeZone.UTC), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(new Object[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        // Test getValue
        period = new BasePeriod();
        assertEquals(0, period.getValue(0));
        assertEquals(0, period.getValue(1));
        assertEquals(0, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));

        period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(7, period.getValue(6));
        assertEquals(8, period.getValue(7));

        // Test getValue out of bounds
        period = new BasePeriod();
        try {
            period.getValue(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        try {
            period.getValue(8);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testBasePeriodMethods() {
        // Test toDurationFrom and toDurationTo
        BasePeriod period = new BasePeriod();
        DateTime baseDate = new DateTime(0L, DateTimeZone.UTC);
        DateTime resultDate;

        // Test with empty period
        assertEquals(new Duration(0L), period.toDurationFrom(baseDate));
        assertEquals(new Duration(0L), period.toDurationTo(baseDate));

        // Test with normal period
        period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        resultDate = new DateTime(baseDate.getMillis() + period.getMillis());
        assertEquals(new Duration(period.getMillis()), period.toDurationFrom(baseDate));
        assertEquals(new Duration(period.getMillis()), period.toDurationTo(resultDate));

        // Test with negative period
        period = new BasePeriod(-1, -2, -3, -4, -5, -6, -7, -8, PeriodType.standard());
        resultDate = new DateTime(baseDate.getMillis() - period.getMillis());
        assertEquals(new Duration(period.getMillis()), period.toDurationTo(baseDate));
        assertEquals(new Duration(period.getMillis()), period.toDurationFrom(resultDate));

        // Test mergePeriod
        BasePeriod otherPeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period = new BasePeriod();
        period.mergePeriod(otherPeriod);
        assertEquals(otherPeriod.iValues, period.iValues);

        // Test addField
        period = new BasePeriod();
        period.addField(DurationFieldType.days(), 1);
        assertEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 0}, period.iValues);

        // Test addPeriod
        period = new BasePeriod();
        period.addPeriod(otherPeriod);
        assertEquals(otherPeriod.iValues, period.iValues);

        // Test setField
        period = new BasePeriod();
        period.setField(DurationFieldType.days(), 1);
        assertEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 0}, period.iValues);
    }
}