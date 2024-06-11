package org.joda.time.tests.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.FieldUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.JulianPersianChronology;
import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronology = new JulianPersianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
         long testValue = new BaseDateTime(0, 0, 1, 1, 1, 2000, chronology).getMillis();
         int expected = chronology.getMonthOfYear(testValue);
         int actual = field.get(testValue);
         assertEquals("get method returns correct value for given test date", expected, actual);

         long testAdd值 = field.add(testValue, 1);
         long expectedAdd = new BaseDateTime(0, 0, 1, 1, 2, 2000, chronology).getMillis();
         assertEquals("add method returns correct value when adding 1", expectedAdd, testAdd);

         long testAddWrap = field.addWrapField(testValue, -1);
         long expectedAddWrap = new BaseDateTime(0, 0, 1, 11, 12, 2000, chronology).getMillis();
         assertEquals("addWrapField method returns correct value when wrapping negative value", expectedAddWrap, testAddWrap);

         // test setting value
         long testSet = field.set(testValue, 2);
         int expectedSet = chronology.getMonthOfYear(testSet);
         assertEquals("set method returns correct value when setting value to 2", 2, expectedSet);

         // test get minimum value
         int minValue = field.getMinimumValue();
         assertEquals("getMinimumValue method returns correct minimum value", 1, minValue);

         // test get maximum value
         int maxValue = field.getMaximumValue();
         assertEquals("getMaximumValue method returns correct maximum value", chronology.getMaxMonth(), maxValue);

         // test is leap
         long testIsLeap = new BaseDateTime(0, 0, 1, 1, 1, 2001, chronology).getMillis();
         boolean expectedIsLeap = chronology.isLeapYear(2001) && chronology.getMonthOfYear(testIsLeap) == DateTimeConstants.FEBRUARY;
         boolean actualIsLeap = field.isLeap(testIsLeap);
         assertTrue("isLeap method returns correct value for given leap year test date", expectedIsLeap == actualIsLeap);

         // test getLeapAmount
         int leapAmount = field.getLeapAmount(testIsLeap);
         assertEquals("getLeapAmount method returns correct value for given leap year test date", 1, leapAmount);

         // test getLeapDurationField
         DurationField expectedLeapDuration = chronology.days();
         DurationField actualLeapDuration = field.getLeapDurationField();
         assertEquals("getLeapDurationField method returns correct leap duration field", expectedLeapDuration, actualLeapDuration);

         // test getRangeDurationField
         DurationField expectedRangeDuration = chronology.years();
         DurationField actualRangeDuration = field.getRangeDurationField();
         assertEquals("getRangeDurationField method returns correct range duration field", expectedRangeDuration, actualRangeDuration);

         // test getDifferenceAsLong
         long testGetDifference = testValue;
         long testSetDifference = testValue - 1;
         long expectedDifference = 1;
         long actualDifference = field.getDifferenceAsLong(testSetDifference, testValue);
         assertEquals("getDifferenceAsLong method returns correct difference value", expectedDifference, actualDifference);

         // test set and get values using ReadablePartial
         LocalDate testDate = new LocalDate(2000, 1, 1);
         ReadablePartial testReadablePartial = testDate.toDateTimeAtStartOfDay().toLocalDateTime().toLocalDateTime().toDateTimeFieldValues();
         int[] expectedArray = new int[testReadablePartial.size()];
         int[] actualArray = field.add(testReadablePartial, 1, new int[testReadablePartial.size()], 0);
         assertEquals("add method returns correct values when adding 1 to multiple fields", expectedArray, actualArray);
         for (int i = 0; i < testReadablePartial.size(); i++) {
             assertEquals("get method returns correct value for given test date", testReadablePartial.getValue(i) + 1, actualArray[i]);
         }
    }
}