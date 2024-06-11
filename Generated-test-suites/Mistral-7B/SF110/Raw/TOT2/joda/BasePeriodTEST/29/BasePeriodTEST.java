package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;

public class BasePeriodTest {
    @Test
    public void testBasePeriod() {
        BasePeriod period = new BasePeriod();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        int[] values = new int[8];
        period.setValues(values);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodLong() {
        BasePeriod period = new BasePeriod(123456789L);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        int[] values = new int[8];
        period.setValues(values);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodLongCustomPeriodTypeChronology() {
        PeriodType customPeriodType = PeriodType.ISODateTime();
        Chronology customChronology = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(123456789L, customPeriodType, customChronology);
        assertEquals(customPeriodType, period.getPeriodType());
        int[] values = new int[8];
        period.setValues(values);
        assertEquals(customPeriodType, period.getPeriodType());
    }
}