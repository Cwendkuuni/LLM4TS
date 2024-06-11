package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronology = new JulianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        // Test default values
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDateTimeFieldType());
        assertEquals(chronology.getAverageMillisPerMonth(), field.getAverageMillisPerDay());

        // Test get method
        MutableDateTime dt = new MutableDateTime();
        dt.set(2018, 1, 1);
        long n = dt.getMillis();
        int month = field.get(n);
        assertEquals(1, month);

        // Test add method with positive integer
        long newMillis = field.add(n, 2);
        dt.setMillis(newMillis);
        month = field.get(newMillis);
        assertEquals(3, month);

        // Test add method with negative integer
        newMillis = field.add(newMillis, -2);
        dt.setMillis(newMillis);
        month = field.get(newMillis);
        assertEquals(1, month);

        // Test add method with long
        newMillis = field.add(n, 100);
        dt.setMillis(newMillis);
        month = field.get(newMillis);
        assertEquals(11, month);

        // Test add method with addWrapField
        newMillis = field.addWrapField(newMillis, 3);
        dt.setMillis(newMillis);
        month = field.get(newMillis);
        assertEquals(4, month);

        // Test add method with ReadablePartial
        MutableDateTime readablePartial = new MutableDateTime();
        readablePartial.setField(DateTimeFieldType.year(), 2018);
        readablePartial.setField(DateTimeFieldType.monthOfYear(), 2);
        long[] addResult = field.add(readablePartial, 1, new long[3], 0);
        dt.set(readablePartial);
        dt.setMillis(field.getMillis(addResult));
        month = field.get(dt.getMillis());
        assertEquals(3, month);

        // Test add method with IllegalArgumentException
        MutableDateTime readablePartial2 = new MutableDateTime();
        readablePartial2.setField(DateTimeFieldType.year(), 2018);
        readablePartial2.setField(DateTimeFieldType.monthOfYear(), 13);
        long[] addResult2 = field.add(readablePartial2, 1, new long[3], 0);
        assertArrayEquals(new long[]{Long.MIN_VALUE, 0, 0}, addResult2);

        // Test isLeap method
        long leapMillis = dt.getMillis() + FieldUtils.millisPerDay(365) + FieldUtils.millisPerDay(1);
        assertTrue(field.isLeap(leapMillis));

        // Test getLeapDurationField
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DurationFieldType.dayOfMonth(), leapDurationField.getDateTimeFieldType());

        // Test getMinimumValue and getMaximumValue
        int minValue = field.getMinimumValue();
        int maxValue = field.getMaximumValue();
        assertEquals(1, minValue);
        assertEquals(12, maxValue);

        // Test roundFloor and remainder
        long roundFloorMillis = dt.getMillis();
        long roundFloorValue = field.roundFloor(roundFloorMillis);
        long remainderMillis = roundFloorMillis - roundFloorValue;
        long remainderValue = field.remainder(roundFloorMillis);
        assertEquals(roundFloorMillis, roundFloorValue + FieldUtils.millisPerDay(365 + (dt.getMonthOfYear() - 1)));
        assertEquals(0, remainderMillis);
        assertEquals(0, remainderValue);
    }
}