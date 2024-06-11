package org.joda.time.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.calendar.GregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology gregorian = GregorianCalendar.getInstance().getCalendarType();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.FEBRUARY);

        // Test getters
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(gregorian, field.getCalendarField());
        assertEquals(-8258715387168736L, field.getSerialVersionUID(), 0L);
        assertEquals(gregorian, field.getChronology());
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
        assertEquals(31, field.getMaximumValue() / getDaysInMonth(gregorian, 12), 1);

        // Test isLenient() method
        assertFalse(field.isLenient());

        // Test get() method
        MutableDateTime dateTime = new MutableDateTime();
        int month = 1;
        for (int i = 0; i < 13; i++) {
            dateTime.setYear(2023).setMonthOfYear(month++).setDayOfMonth(1).setHourOfDay(0).setMinuteOfHour(0).setSecondOfMinute(0).setMillisOfSecond(0);
            assertEquals(month - 1, field.get(dateTime.getMillis()));
        }

        // Test set() method
        for (int i = 0; i < 13; i++) {
            int monthToSet = i + 1;
            long newMillis = field.set(dateTime.getMillis(), monthToSet);
            MutableDateTime newDateTime = new MutableDateTime(newMillis);
            assertEquals(monthToSet, newDateTime.getMonthOfYear());
            assertEquals(DateTimeConstants.JANUARY, newDateTime.getYear());
            assertEquals(DateTimeConstants.JANUARY_1, newDateTime.getDayOfMonth());
        }

        // Test add() method
        MutableDateTime instant = new MutableDateTime();
        int initialMonth = 5;
        int initialYear = 2023;
        int initialDay = 31;
        int initialHour = 0;
        int initialMinute = 0;
        int initialSecond = 0;
        int initialMillis = 0;
        instant.setYear(initialYear).setMonthOfYear(initialMonth).setDayOfMonth(initialDay).setHourOfDay(initialHour).setMinuteOfHour(initialMinute).setSecondOfMinute(initialSecond).setMillisOfSecond(initialMillis);

        long newMillis = field.add(instant.getMillis(), 1);
        MutableDateTime newDateTime1 = new MutableDateTime(newMillis);
        assertEquals(initialMonth + 1, newDateTime1.getMonthOfYear());

        newMillis = field.add(instant.getMillis(), -1);
        MutableDateTime newDateTime2 = new MutableDateTime(newMillis);
        assertEquals(initialMonth - 1, newDateTime2.getMonthOfYear());

        newMillis = field.add(instant.getMillis(), 12);
        MutableDateTime newDateTime12 = new MutableDateTime(newMillis);
        assertEquals(initialMonth + 12, newDateTime12.getMonthOfYear());

        // Test addWrapField() method
        long minuendMillis = instant.getMillis();
        long subtrahendMillis = instant.getMillis() - (365 * 24 * 60 * 60 * 1000);
        long result = field.addWrapField(minuendMillis, 12);
        MutableDateTime minuendDateTime = new MutableDateTime(minuendMillis);
        MutableDateTime subtrahendDateTime = new MutableDateTime(subtrahendMillis);
        MutableDateTime resultDateTime = new MutableDateTime(result);

        assertEquals(DateTimeConstants.JANUARY, resultDateTime.getMonthOfYear());
        assertEquals(resultDateTime.getYear(), minuendDateTime.getYear() - 1);

        // Test getDifferenceAsLong() method
        long minuendMillis2 = instant.getMillis();
        long subtrahendMillis2 = instant.getMillis() + (365 * 24 * 60 * 60 * 1000);
        long difference = field.getDifferenceAsLong(minuendMillis2, subtrahendMillis2);
        assertEquals(difference, 1);

        // Test set() method with out-of-range values
        long outOfMinValueMillis = field.set(instant.getMillis(), DateTimeConstants.JANUARY - 1);
        MutableDateTime outOfMinValueDateTime = new MutableDateTime(outOfMinValueMillis);
        assertEquals(DateTimeConstants.JANUARY, outOfMinValueDateTime.getMonthOfYear());
        assertEquals(DateTimeConstants.JANUARY - 1, outOfMinValueDateTime.getYear());

        long outOfMaxValueMillis = field.set(instant.getMillis(), DateTimeConstants.DECEMBER + 1);
        MutableDateTime outOfMaxValueDateTime = new MutableDateTime(outOfMaxValueMillis);
        assertEquals(DateTimeConstants.DECEMBER, outOfMaxValueDateTime.getMonthOfYear());
        assertEquals(DateTimeConstants.DECEMBER, outOfMaxValueDateTime.getYear());
        assertEquals(DateTimeConstants.JANUARY_31, outOfMaxValueDateTime.getDayOfMonth());

        // Test add() method with out-of-range values
        long addOutOfMinValueMillis = field.add(instant.getMillis(), DateTimeConstants.JANUARY - 1);
        MutableDateTime addOutOfMinValueDateTime = new MutableDateTime(addOutOfMinValueMillis);
        assertEquals(DateTimeConstants.DECEMBER, addOutOfMinValueDateTime.getMonthOfYear());
        assertEquals(DateTimeConstants.JANUARY - 1, addOutOfMinValueDateTime.getYear());
        assertEquals(DateTimeConstants.DECEMBER_30, addOutOfMinValueDateTime.getDayOfMonth());

        long addOutOfMaxValueMillis = field.add(instant.getMillis(), DateTimeConstants.DECEMBER + 1);
        MutableDateTime addOutOfMaxValueDateTime = new MutableDateTime(addOutOfMaxValueMillis);
        assertEquals(DateTimeConstants.JANUARY, addOutOfMaxValueDateTime.getMonthOfYear());
        assertEquals(DateTimeConstants.DECEMBER + 1, addOutOfMaxValueDateTime.getYear());
        assertEquals(DateTimeConstants.JANUARY_1, addOutOfMaxValueDateTime.getDayOfMonth());

        // Test addWrapField() method with out-of-range values
        long addWrapOutOfMinValueMillis = field.addWrapField(instant.getMillis(), DateTimeConstants.JANUARY - 1);
        MutableDateTime addWrapOutOfMinValueDateTime = new MutableDateTime(addWrapOutOfMinValueMillis);
        assertEquals(DateTimeConstants.DECEMBER, addWrapOutOfMinValueDateTime.getMonthOfYear());
        assertEquals(DateTimeConstants.JANUARY - 1, addWrapOutOfMinValueDateTime.getYear());
        assertEquals(DateTimeConstants.DECEMBER_30, addWrapOutOfMinValueDateTime.getDayOfMonth());

        long addWrapOutOfMaxValueMillis = field.addWrapField(instant.getMillis(), DateTimeConstants.DECEMBER + 1);
        MutableDateTime addWrapOutOfMaxValueDateTime = new MutableDateTime(addWrapOutOfMaxValueMillis);
        assertEquals(DateTimeConstants.JANUARY, addWrapOutOfMaxValueDateTime.getMonthOfYear());
        assertEquals(DateTimeConstants.DECEMBER + 1, addWrapOutOfMaxValueDateTime.getYear());
        assertEquals(DateTimeConstants.JANUARY_31, addWrapOutOfMaxValueDateTime.getDayOfMonth());

        // Test add() method with leap years
        long leapYearMillis = instant.getMillis() - (365 * 24 * 60 * 60 * 1000);
        long newMillisLeap = field.add(leapYearMillis, 2);
        MutableDateTime newDateTimeLeap = new MutableDateTime(newMillisLeap);
        assertEquals(DateTimeConstants.FEBRUARY, newDateTimeLeap.getMonthOfYear());
        assertEquals(instant.getYear(), newDateTimeLeap.getYear());
        assertEquals(DateTimeConstants.FEBRUARY_29, newDateTimeLeap.getDayOfMonth());

        long newMillisLeap2 = field.add(leapYearMillis, 3);
        MutableDateTime newDateTimeLeap2 = new MutableDateTime(newMillisLeap2);
        assertEquals(DateTimeConstants.MARCH, newDateTimeLeap2.getMonthOfYear());
        assertEquals(instant.getYear(), newDateTimeLeap2.getYear());
        assertEquals(DateTimeConstants.MARCH_1, newDateTimeLeap2.getDayOfMonth());

        // Test isLeap() method
        int testYear = 2023;
        boolean isLeap = field.isLeap(instant.getMillis());
        assertTrue(isLeap);

        long testYearLeapMillis = instant.getMillis() - (365 * 24 * 60 * 60 * 1000);
        isLeap = field.isLeap(testYearLeapMillis);
        assertTrue(isLeap);

        int testYear2 = 2000;
        isLeap = field.isLeap(instant.getMillis());
        assertFalse(isLeap);

        long testYear2LeapMillis = instant.getMillis() - (366 * 24 * 60 * 60 * 1000);
        isLeap = field.isLeap(testYear2LeapMillis);
        assertTrue(isLeap);
    }

    private int getDaysInMonth(Chronology chronology, int month) {
        return chronology.getDaysInYearMonth(DateTimeConstants.getCalendar(chronology), month);
    }
}