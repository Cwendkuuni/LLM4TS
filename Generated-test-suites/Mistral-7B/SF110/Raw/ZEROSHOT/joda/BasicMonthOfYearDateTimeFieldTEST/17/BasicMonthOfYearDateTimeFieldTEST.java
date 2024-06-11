package org.joda.time.tests.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.BasicField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.IntegerField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology isoChronology = new ISOChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), testField.getFieldType());
        assertEquals(isoChronology.getAverageMillisPerMonth(), testField.getPrecision());
        assertFalse(testField.isLenient());
        assertEquals(1, testField.getMinimumValue());
        assertEquals(isoChronology.getMaxMonth(), testField.getMaximumValue());

        // get test
        long testDateTime = new LocalDateTime(1999, 2, 29, 12, 30, 0, 0).toInstant().getMillis();
        assertEquals(2, testField.get(testDateTime));

        // add test
        long newDateTime = testDateTime + testField.getMillisPerDay() * 2;
        assertEquals(3, testField.get(newDateTime));

        long negativeAdd = testDateTime - testField.getMillisPerDay() * 2;
        assertEquals(1, testField.get(negativeAdd));

        long addWrappedValue = testDateTime + 13 * testField.getMillisPerDay();
        assertEquals(13, testField.get(addWrappedValue));

        // add test with other fields
        LocalDate testDate = new LocalDate(1999, 2, 29);
        LocalDateTime testLocalDateTime = testDate.toLocalDateTime();
        testLocalDateTime = testLocalDateTime.plus(new LocalTime(12, 30, 0, 0));
        Instant testInstant = testLocalDateTime.toInstant();
        long testMillis = testInstant.getMillis();

        long testMillisAdd = testMillis + 2 * testField.getMillisPerDay();
        DateTimeFieldType testFieldType = new IntegerField(DateTimeFieldType.dayOfMonth(), isoChronology);
        long[] testArray = new long[2];
        testArray[0] = testMillis;
        testArray[1] = 2;

        long[] resultArray = testField.add(testFieldType.getField(isoChronology), 2, testArray, 0);
        assertEquals(testMillisAdd, resultArray[0]);
        assertEquals(3, resultArray[1]);

        // getDifferenceAsLong test
        long testMillisSubtract = testMillis - testField.getMillisPerDay();
        assertEquals(-1, testField.getDifferenceAsLong(testMillisSubtract, testMillis));

        long testMillisSubtract2 = testMillis - 2 * testField.getMillisPerDay();
        assertEquals(-2, testField.getDifferenceAsLong(testMillis, testMillisSubtract2));

        // set test
        long testMillisSet = testMillis + testField.getMillisPerDay();
        long resultMillisSet = testField.set(testMillisSet, 3);
        assertEquals(testMillisSet, resultMillisSet);
        assertEquals(3, testField.get(testMillisSet));

        // isLeap test
        long testMillisLeap = testMillis + 4 * testField.getMillisPerDay();
        assertTrue(testField.isLeap(testMillisLeap));

        // getLeapAmount test
        long testMillisLeap2 = testMillisLeap + testField.getMillisPerDay();
        assertEquals(1, testField.getLeapAmount(testMillisLeap2));

        // getLeapDurationField test
        DurationField resultDurationField = testField.getLeapDurationField();
        assertEquals(DurationFieldType.days(), resultDurationField.getFieldType());
    }
}