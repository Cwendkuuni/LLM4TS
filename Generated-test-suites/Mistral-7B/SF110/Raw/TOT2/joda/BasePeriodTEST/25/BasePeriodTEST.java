package org.junit.platform.commons.annotation.meta;

import org.junit.Test;
import org.joda.time.*;
import org.junit.Assert.*;

public class BasePeriodTest {
    @Test
    public void testBasePeriodDefaultConstructor() {
        BasePeriod period = new BasePeriod();
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertArrayEquals(new int[8], values);
        for (int i = 0; i < values.length; i++) {
            assertEquals(0, values[i]);
        }
    }
    
    @Test
    public void testBasePeriodInstantToInstant() {
        Instant start = new Instant(1492942356000L);
        Instant end = new Instant(1492942416000L);
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        Duration duration = period.toDurationFrom(start);
        assertEquals(new Duration(1, 0, 0, 60), duration);
        duration = period.toDurationTo(end);
        assertEquals(new Duration(-1, 0, 0, 60), duration);
    }
    
    @Test
    public void testBasePeriodArrayConstructor() {
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] valuesFromPeriod = period.getValues();
        assertArrayEquals(values, valuesFromPeriod);
    }
}