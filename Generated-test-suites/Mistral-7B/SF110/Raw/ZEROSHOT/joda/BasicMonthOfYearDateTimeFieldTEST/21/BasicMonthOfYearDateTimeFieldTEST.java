package org.joda.time.tests.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.util.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology gregorian = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(gregorian, field.getChronology());
        assertFalse(field.isLenient());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());

        long date1 = new DateTime(2022, 1, 31, 0, 0).getMillis();
        long date2 = new DateTime(2022, 2, 28, 0, 0).getMillis(); // non-leap year
        long date3 = new DateTime(2022, 2, 29, 0, 0).getMillis(); // leap year

        assertEquals(1, field.get(date1));
        assertEquals(2, field.get(date2));
        assertEquals(2, field.get(date3));

        long date4 = new DateTime(2022, 13, 1, 0, 0).getMillis(); // invalid month
        try {
            field.get(date4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long date5 = new DateTime(2023, 0, 1, 0, 0).getMillis(); // invalid month (negative)
        try {
            field.get(date5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long date6 = new DateTime(1900, 2, 29, 0, 0).getMillis(); // invalid year (leap year before 1972)
        try {
            field.get(date6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        DateTime date7 = new DateTime(2022, 1, 31, 0, 0);
        ReadablePartial readablePartial = new DateTimeFieldSet(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 3); // March
        int[] result = field.add(readablePartial, 1, new int[3], 0);
        assertEquals(4, result[0]); // year
        assertEquals(3, result[1]); // month
        assertEquals(1, result[2]); // day

        long date8 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date9 = field.add(date8, 1);
        assertEquals(2022, DateTimeFieldUtil.getYear(date9));
        assertEquals(2, DateTimeFieldUtil.getMonthOfYear(date9));
        assertEquals(1, DateTimeFieldUtil.getDayOfMonth(date9));

        long date10 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date11 = field.add(date10, -1);
        assertEquals(2022, DateTimeFieldUtil.getYear(date11));
        assertEquals(12, DateTimeFieldUtil.getMonthOfYear(date11));
        assertEquals(31, DateTimeFieldUtil.getDayOfMonth(date11));

        long date12 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date13 = field.addWrapField(date12, -100);
        assertEquals(2021, DateTimeFieldUtil.getYear(date13));
        assertEquals(12, DateTimeFieldUtil.getMonthOfYear(date13));
        assertEquals(31, DateTimeFieldUtil.getDayOfMonth(date13));

        long date14 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date15 = field.addWrapField(date14, 12345);
        assertEquals(2036, DateTimeFieldUtil.getYear(date15));
        assertEquals(1, DateTimeFieldUtil.getMonthOfYear(date15));
        assertEquals(1, DateTimeFieldUtil.getDayOfMonth(date15));

        long date16 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date17 = field.roundFloor(date16);
        assertEquals(2022, DateTimeFieldUtil.getYear(date16));
        assertEquals(1, DateTimeFieldUtil.getMonthOfYear(date16));
        assertEquals(1, DateTimeFieldUtil.getDayOfMonth(date16));
        assertEquals(2022, DateTimeFieldUtil.getYear(date17));
        assertEquals(1, DateTimeFieldUtil.getMonthOfYear(date17));
        assertEquals(1, DateTimeFieldUtil.getDayOfMonth(date17));

        long date18 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date19 = field.remainder(date18);
        assertEquals(0, date19);

        long date20 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date21 = field.set(date20, 13);
        assertEquals(2022, DateTimeFieldUtil.getYear(date21));
        assertEquals(1, DateTimeFieldUtil.getMonthOfYear(date21));
        assertEquals(28, DateTimeFieldUtil.getDayOfMonth(date21));

        long date22 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date23 = field.set(date22, 0);
        assertEquals(2022, DateTimeFieldUtil.getYear(date23));
        assertEquals(1, DateTimeFieldUtil.getMonthOfYear(date23));
        assertEquals(1, DateTimeFieldUtil.getDayOfMonth(date23));

        long date24 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date25 = field.addWrapField(date24, 123456789);
        assertEquals(2037, DateTimeFieldUtil.getYear(date25));
        assertEquals(1, DateTimeFieldUtil.getMonthOfYear(date25));
        assertEquals(1, DateTimeFieldUtil.getDayOfMonth(date25));

        long date26 = new DateTime(2022, 1, 1, 0, 0).getMillis();
        long date27 = field.addWrapField(date26, Long.MIN_VALUE);
        assertEquals(2021, DateTimeFieldUtil.getYear(date27));
        assertEquals(12, DateTimeFieldUtil.getMonthOfYear(date27));
        assertEquals(31, DateTimeFieldUtil.getDayOfMonth(date27));
    }

    @org.junit.Test
    public void testLeapYear() {
        BasicChronology gregorian = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date1 = new DateTime(2020, 2, 29, 0, 0).getMillis(); // leap year
        assertEquals(2, field.get(date1));

        long date2 = new DateTime(1900, 2, 29, 0, 0).getMillis(); // invalid year (leap year before 1972)
        try {
            field.get(date2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long date3 = new DateTime(2024, 2, 29, 0, 0).getMillis(); // future leap year
        assertEquals(2, field.get(date3));

        long date4 = new DateTime(2023, 2, 28, 0, 0).getMillis(); // non-leap year
        assertEquals(2, field.get(date4));
    }
}