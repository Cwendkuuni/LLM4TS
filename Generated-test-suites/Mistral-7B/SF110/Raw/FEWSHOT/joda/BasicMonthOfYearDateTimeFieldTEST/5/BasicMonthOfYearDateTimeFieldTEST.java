package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertGreaterThan;
import static org.junit.Assert.assertLessThan;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), testField.getDataType());
        assertEquals(gregorianChronology, testField.getChronology());
        assertEquals(false, testField.isLenient());
        assertEquals(1, testField.getMinimumValue());
        assertEquals(12, testField.getMaximumValue());
        assertNull(testField.getRangeDurationField());
        assertSame(gregorianChronology.months(), testField.getLeapDurationField());
        assertFalse(testField.isLeap(1592, 2)); // February 29, 1592 does not exist in the Gregorian calendar
        assertFalse(testField.isLeap(1600, 2)); // February 29, 1600 does exist in the Gregorian calendar
        assertSame(29, testField.getLeapAmount(1600, 2));
    }

    @Test
    public void testGet() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTime testDate = new DateTime(1592, 2, 29, 0, 0, 0);
        long testDateMillis = testDate.getMillis();
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        DateTime testDate2 = new DateTime(1600, 2, 28, 0, 0, 0);
        long testDate2Millis = testDate2.getMillis();
        long testValue2 = testField.get(testDate2Millis);
        assertEquals(2, testValue2);

        DateTime testDate3 = new DateTime(1600, 2, 29, 0, 0, 0); // Leap year
        long testDate3Millis = testDate3.getMillis();
        long testValue3 = testField.get(testDate3Millis);
        assertEquals(3, testValue3);
    }

    @Test
    public void testAdd() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTime testDate = new DateTime(1592, 2, 29, 0, 0, 0);
        long testDateMillis = testDate.getMillis();
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        long newValue = testField.add(testDateMillis, 3);
        DateTime newTestDate = new DateTime(newValue);
        assertEquals(5, testField.get(newTestDate.getMillis()));

        long newValue2 = testField.add(testDateMillis, -1);
        DateTime newTestDate2 = new DateTime(newValue2);
        assertEquals(1, testField.get(newTestDate2.getMillis()));

        long newValue3 = testField.add(testDateMillis, 13);
        DateTime newTestDate3 = new DateTime(newValue3);
        assertEquals(15, testField.get(newTestDate3.getMillis()));
    }

    @Test
    public void testAddWrapField() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        long testDateMillis = 1592 * 31536000000L + 5988800000L;
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        long newValue = testField.addWrapField(testDateMillis, 13);
        DateTime newTestDate = new DateTime(newValue);
        assertEquals(15, testField.get(newTestDate.getMillis()));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTime testDate = new DateTime(1592, 2, 29, 0, 0, 0);
        long testDateMillis = testDate.getMillis();
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        DateTime testDate2 = new DateTime(1600, 2, 28, 0, 0, 0);
        long testDate2Millis = testDate2.getMillis();
        long difference = testField.getDifferenceAsLong(testDateMillis, testDate2Millis);
        assertEquals(3, difference);

        DateTime testDate3 = new DateTime(1600, 2, 29, 0, 0, 0); // Leap year
        long testDate3Millis = testDate3.getMillis();
        difference = testField.getDifferenceAsLong(testDateMillis, testDate3Millis);
        assertEquals(11, difference);
    }

    @Test
    public void testSet() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTime testDate = new DateTime(1592, 2, 29, 0, 0, 0);
        long testDateMillis = testDate.getMillis();
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        long newValue = testField.set(testDateMillis, 5);
        DateTime newTestDate = new DateTime(newValue);
        assertEquals(5, testField.get(newTestDate.getMillis()));

        long newValue2 = testField.set(testDateMillis, 15);
        DateTime newTestDate2 = new DateTime(newValue2);
        assertEquals(15, testField.get(newTestDate2.getMillis()));

        long newValue3 = testField.set(testDateMillis, 20);
        DateTime newTestDate3 = new DateTime(newValue3);
        assertEquals(2, testField.get(newTestDate3.getMillis())); // The value is wrapped around.
    }

    @Test
    public void testIsLeap() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTime testDate = new DateTime(1592, 2, 29, 0, 0, 0);
        assertFalse(testField.isLeap(testDate.getMillis()));

        DateTime testDate2 = new DateTime(1600, 2, 29, 0, 0, 0); // Leap year
        assertTrue(testField.isLeap(testDate2.getMillis()));

        DateTime testDate3 = new DateTime(1700, 2, 28, 0, 0, 0); // Not a leap year
        assertFalse(testField.isLeap(testDate3.getMillis()));

        DateTime testDate4 = new DateTime(2000, 2, 29, 0, 0, 0); // Leap year
        assertTrue(testField.isLeap(testDate4.getMillis()));

        DateTime testDate5 = new DateTime(1900, 2, 28, 0, 0, 0); // Not a leap year
        assertFalse(testField.isLeap(testDate5.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTime testDate = new DateTime(1592, 2, 29, 0, 0, 0);
        assertFalse(testField.isLeap(testDate.getMillis()));
        assertFalse(testField.isLeap(testDate.getMillis(), 2));
        assertEquals(0, testField.getLeapAmount(testDate.getMillis()));

        DateTime testDate2 = new DateTime(1600, 2, 29, 0, 0, 0); // Leap year
        assertTrue(testField.isLeap(testDate2.getMillis()));
        assertEquals(1, testField.getLeapAmount(testDate2.getMillis()));

        DateTime testDate3 = new DateTime(1700, 2, 28, 0, 0, 0); // Not a leap year
        assertFalse(testField.isLeap(testDate3.getMillis()));
        assertFalse(testField.isLeap(testDate3.getMillis(), 2));
        assertEquals(0, testField.getLeapAmount(testDate3.getMillis()));

        DateTime testDate4 = new DateTime(2000, 2, 29, 0, 0, 0); // Leap year
        assertTrue(testField.isLeap(testDate4.getMillis()));
        assertEquals(1, testField.getLeapAmount(testDate4.getMillis()));

        DateTime testDate5 = new DateTime(1900, 2, 28, 0, 0, 0); // Not a leap year
        assertFalse(testField.isLeap(testDate5.getMillis()));
        assertFalse(testField.isLeap(testDate5.getMillis(), 2));
        assertEquals(0, testField.getLeapAmount(testDate5.getMillis()));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        assertEquals(DurationFieldType.days(), testField.getLeapDurationField().getDataType());
    }

    @Test
    public void testRoundFloor() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        long testDateMillis = 1592 * 31536000000L + 5988800000L;
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        long roundedFloor = testField.roundFloor(testDateMillis);
        DateTime roundedFloorDateTime = new DateTime(roundedFloor);
        assertEquals(28522150400000L, roundedFloor); // 1592-02-29

        long testDateMillis2 = 1600 * 31536000000L + 225266800000L;
        long testValue2 = testField.get(testDateMillis2);
        assertEquals(3, testValue2);

        long roundedFloor2 = testField.roundFloor(testDateMillis2);
        DateTime roundedFloorDateTime2 = new DateTime(roundedFloor2);
        assertEquals(2252668000000L, roundedFloor2); // 1600-02-28
    }

    @Test
    public void testRemainder() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        long testDateMillis = 1592 * 31536000000L + 5988800000L;
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        long remainder = testField.remainder(testDateMillis);
        assertEquals(0, remainder);

        long testDateMillis2 = 1600 * 31536000000L + 225266800000L;
        long testValue2 = testField.get(testDateMillis2);
        assertEquals(3, testValue2);

        long remainder2 = testField.remainder(testDateMillis2);
        assertEquals(0, remainder2);

        long testDateMillis3 = 1600 * 31536000000L + 22526684600000L; // 1600-02-29
        long testValue3 = testField.get(testDateMillis3);
        assertEquals(3, testValue3);

        long remainder3 = testField.remainder(testDateMillis3);
        assertEquals(86400000L, remainder3); // 1 day
    }

    @Test
    public void testParse() {
        BasicGregorianChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);

        DateTimeParsingUtils dateTimeParsingUtils = new DateTimeParsingUtils();
        DateTimeFormatter dateTimeFormatter = dateTimeParsingUtils.getFormatter("yyyy-MM-dd HH:mm:ss.SSS z");

        DateTime testDate = new DateTime("1592-02-29 00:00:00.000 +0000");
        long testDateMillis = testDate.getMillis();
        long testValue = testField.get(testDateMillis);
        assertEquals(2, testValue);

        DateTime testDate2 = new DateTime("1600-02-28 00:00:00.000 +0000");
        long testDate2Millis = testDate2.getMillis();
        long testValue2 = testField.get(testDate2Millis);
        assertEquals(2, testValue2);

        DateTime testDate3 = new DateTime("1600-02-29 00:00:00.000 +0000"); // Leap year
        long testDate3Millis = testDate3.getMillis();
        long testValue3 = testField.get(testDate3Millis);
        assertEquals(3, testValue3);

        try {
            DateTime testDate4 = new DateTime("1592-02-31 00:00:00.000 +0000");
            long testDate4Millis = testDate4.getMillis();
            testField.get(testDate4Millis);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}