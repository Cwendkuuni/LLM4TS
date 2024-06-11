package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;
import org.joda.time.test.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
        assertEquals(false, field.isLenient());
        assertEquals(365600000L, field.getAverageMillisPerDay());
        assertEquals(31, field.getDaysInYearMonth(1, 1));
        assertEquals(29, field.getDaysInYearMonth(1, 2)); // leap year
        assertEquals(28, field.getDaysInYearMonth(1, 3));

        Date date1 = new Date(112, 1, 1);
        long millis1 = date1.getMillis();
        int month1 = field.get(millis1);
        long millis2 = field.get(millis1 + 1);
        int month2 = field.get(millis2);

        assertEquals(1, month1);
        assertEquals(2, month2);

        long millis3 = field.add(millis1, 12);
        int month3 = field.get(millis3);
        assertEquals(13, month3); // wrap around

        long millis4 = field.add(millis1, -12);
        int month4 = field.get(millis4);
        assertEquals(11, month4); // wrap around

        long millis5 = field.addWrapField(millis1, 13);
        int month5 = field.get(millis5);
        assertEquals(14, month5); // wrap around

        long millis6 = field.getDifferenceAsLong(millis1, millis2);
        int monthsBetween = field.getRangeDurationField().getStandardMillis() / 30;
        assertEquals(monthsBetween, Math.abs(millis6));
    }

    @Test
    public void testBasicMonthOfYearDateTimeFieldAdd() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        Date date1 = new Date(112, 1, 1);
        long millis1 = date1.getMillis();
        int month1 = field.get(millis1);

        long millis2 = field.add(millis1, 1);
        int month2 = field.get(millis2);

        long millis3 = field.add(millis1, 12);
        int month3 = field.get(millis3);

        long millis4 = field.add(millis1, 13 * 30);
        int month4 = field.get(millis4);

        long millis5 = field.add(millis1, -12);
        int month5 = field.get(millis5);

        long millis6 = field.addWrapField(millis1, 130);
        int month6 = field.get(millis6);

        long millis7 = field.add(millis1, 366);
        int month7 = field.get(millis7);

        // Test adding to invalid dates
        long millisInvalid1 = Long.MIN_VALUE;
        int monthInvalid1 = field.get(millisInvalid1);
        assertEquals(Integer.MIN_VALUE, monthInvalid1);

        long millisInvalid2 = Long.MAX_VALUE;
        int monthInvalid2 = field.get(millisInvalid2);
        assertEquals(Integer.MAX_VALUE, monthInvalid2);
    }

    @Test
    public void testBasicMonthOfYearDateTimeFieldSet() {
        BasicChronology gregorian = GregorianChronology.INSTANCE;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long millis = 112 * 31536000000L + 1 * 86400000;
        int month = field.get(millis);

        long millisSet = field.set(millis, 3);
        int monthSet = field.get(millisSet);

        assertEquals(3, monthSet);

        long millisSetWrapped = field.set(millis, 13);
        int monthSetWrapped = field.get(millisSetWrapped);

        assertEquals(14, monthSetWrapped);

        long millisSetMax = field.set(millis, field.getMaximumValue());
        int monthSetMax = field.get(millisSetMax);

        assertEquals(12, monthSetMax);

        long millisSetMin = field.set(millis, field.getMinimumValue());
        int monthSetMin = field.get(millisSetMin);

        assertEquals(1, monthSetMin);
    }

    @Test
    public void testBasicMonthOfYearDateTimeFieldLeap() {
        BasicChronology gregorianLeap = new GregorianChronology(365, 1461, 1);
        BasicMonthOfYearDateTimeField fieldLeap = new BasicMonthOfYearDateTimeField(gregorianLeap, 2); // February

        long millis = 10000L;
        int month = fieldLeap.get(millis);

        long millisLeap = fieldLeap.set(millis, 29);
        int monthLeap = fieldLeap.get(millisLeap);

        assertEquals(3, monthLeap);

        long millisLeapYear = fieldLeap.set(millis, 59);
        int monthLeapYear = fieldLeap.get(millisLeapYear);

        assertEquals(3, monthLeapYear); // Leap year

        long millisLeapYear2 = fieldLeap.set(millisLeapYear, 30);
        int monthLeapYear2 = fieldLeap.get(millisLeapYear2);

        assertEquals(1, monthLeapYear2); // Out of range
    }
}