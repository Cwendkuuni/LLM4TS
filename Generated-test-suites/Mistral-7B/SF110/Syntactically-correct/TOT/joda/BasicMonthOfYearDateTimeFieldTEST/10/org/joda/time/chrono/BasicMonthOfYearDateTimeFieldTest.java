package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology ISO = new ISOChronology();

 @Test
 public void testGetMethod() {
     long date = new BaseDateTime(0, ISO).getMillis();
     int expected = ISO.getMonthOfYear(date);
     int actual = new BasicMonthOfYearDateTimeField(ISO, 2).get(date);
     assertEquals(expected, actual);
 }

 @Test
 public void testAddMethodWithValidInputs() {
     long date = new BaseDateTime(0, ISO).getMillis();
     int monthToAdd = 3;
     long expected = new BaseDateTime(2000, 4, 1, ISO).getMillis();
     long actual = new BasicMonthOfYearDateTimeField(ISO, 2).add(date, monthToAdd);
     assertEquals(expected, actual);
 }

 @Test
 public void testAddMethodWithNegativeInputs() {
     long date = new BaseDateTime(2000, 4, 1, ISO).getMillis();
     int monthToSubtract = 3;
     long expected = new BaseDateTime(1999, 11, 30, ISO).getMillis();
     long actual = new BasicMonthOfYearDateTimeField(ISO, 2).add(date, -monthToSubtract);
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMethodWithTooBigInput() {
     long date = new BaseDateTime(2000, 4, 1, ISO).getMillis();
     int monthToAdd = ISO.getMonthsPerYear() + 1;
     new BasicMonthOfYearDateTimeField(ISO, 2).add(date, monthToAdd);
 }

 @Test
 public void testAddMethodWithDifferentChronology() {
     long date = new BaseDateTime(2000, 4, 1, ISO).getMillis();
     BasicChronology testChronology = new BasicGregorianChronology();
     long expected = new BaseDateTime(1999, 11, 30, testChronology).getMillis();
     long actual = new BasicMonthOfYearDateTimeField(testChronology, 1).add(date, -13);
     assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapFieldMethod() {
     long date = new BaseDateTime(2000, 4, 31, ISO).getMillis();
     int monthToAdd = 3;
     long expected = new BaseDateTime(2001, 7, 31, ISO).getMillis();
     long actual = new BasicMonthOfYearDateTimeField(ISO, 2).addWrapField(date, monthToAdd);
     assertEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLongMethod() {
     long date1 = new BaseDateTime(2000, 4, 30, ISO).getMillis();
     long date2 = new BaseDateTime(2000, 5, 1, ISO).getMillis();
     long expected = 30;
     long actual = new BasicMonthOfYearDateTimeField(ISO, 2).getDifferenceAsLong(date1, date2);
     assertEquals(expected, actual);
 }

 @Test
 public void testSetMethod() {
     long date = new BaseDateTime(2000, 4, 30, ISO).getMillis();
     int monthToSet = 5;
     long expected = new BaseDateTime(2000, 5, 30, ISO).getMillis();
     long actual = new BasicMonthOfYearDateTimeField(ISO, 2).set(date, monthToSet);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsLeapMethod() {
     long date = new BaseDateTime(2000, 2, 29, ISO).getMillis();
     assertTrue(new BasicMonthOfYearDateTimeField(ISO, 2).isLeap(date));
 }

 @Test
 public void testGetLeapAmountMethod() {
     long date = new BaseDateTime(2000, 2, 28, ISO).getMillis();
     assertEquals(0, new BasicMonthOfYearDateTimeField(ISO, 2).getLeapAmount(date));
 }

 @Test
 public void testGetMinimumValueMethod() {
     int minValue = new BasicMonthOfYearDateTimeField(ISO, 2).getMinimumValue();
     assertEquals(1, minValue);
 }

 @Test
 public void testGetMaximumValueMethod() {
     int maxValue = new BasicMonthOfYearDateTimeField(ISO, 2).getMaximumValue();
     assertEquals(12, maxValue);
 }
}