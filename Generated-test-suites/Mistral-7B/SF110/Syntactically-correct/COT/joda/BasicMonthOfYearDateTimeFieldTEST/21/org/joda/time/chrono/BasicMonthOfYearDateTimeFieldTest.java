package org.joda.time.test.chrono;

import org.joda.time.BaseMillisecond;
import org.joda.time.ChronologyException;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;
 private BasicChronology chronology;

 @Before
 public void setUp() {
 chronology = new GregorianChronology();
 field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
 }

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final int expected = 12;
 final int result = field.get(testDate);
 assertEquals(expected, result);
 }

 @Test
 public void testAdd() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final int monthsToAdd = 13;
 final long expected = new MutableDateTime(new LocalDate(2006, 1, 1), 0, 0, 0, 0, 0).getMillis();
 final long result = field.add(testDate, monthsToAdd);
 assertEquals(expected, result);
 }

 @Test
 public void testAddWithLong() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final long monthsToAdd = 13L;
 final long expected = new MutableDateTime(new LocalDate(2006, 1, 1), 0, 0, 0, 0, 0).getMillis();
 final long result = field.add(testDate, monthsToAdd);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNegativeNumber() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final int monthsToAdd = -1;
 final long result = field.add(testDate, monthsToAdd);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithLongNegativeNumber() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final long monthsToAdd = -1L;
 final long result = field.add(testDate, monthsToAdd);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test
 public void testAddWithPartial() {
 final MutableLocalDateTime dateTime = new MutableLocalDateTime();
 dateTime.set(2005, 11, 30, 23, 59, 59, 593);
 final int monthsToAdd = 1;
 final int[] expectedValues = {1, 1};
 final int[] resultValues = field.add(dateTime, monthsToAdd, new int[2], 2).toArray();
 assertArrayEquals(expectedValues, resultValues);
 }

 @Test
 public void testAddWrapField() {
 final long testDate = new Instant().getMillis();
 final int monthsToAdd = 13;
 final long expected = new MutableDateTime(new LocalDate(2006, 1, 1), 0, 0, 0, 0, 0).getMillis();
 final long result = field.addWrapField(testDate, monthsToAdd);
 assertEquals(expected, result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 final long testDate1 = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final long testDate2 = new MutableDateTime(new LocalDate(2005, 11, 30), 23, 59, 59, 59, 593).getMillis();
 final long expected = 1;
 final long result = field.getDifferenceAsLong(testDate1, testDate2);
 assertEquals(expected, result);
 }

 @Test
 public void testSet() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final int newMonth = 1;
 final long expected = new MutableDateTime(new LocalDate(2005, newMonth, 1), 0, 0, 0, 0, 0).getMillis();
 final long result = field.set(testDate, newMonth);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithOutOfBoundsValue() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final int newMonth = 13;
 final long result = field.set(testDate, newMonth);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test(expected = ChronologyException.class)
 public void testIsLeapWithNonLeapYear() {
 final long testDate = new MutableDateTime(new LocalDate(2000, 2, 29), 0, 0, 0, 0, 0).getMillis();
 final boolean result = field.isLeap(testDate);
 fail("Expected ChronologyException but got: " + result);
 }

 @Test(expected = ChronologyException.class)
 public void testIsLeapWithLeapYearAndWrongMonth() {
 final long testDate = new MutableDateTime(new LocalDate(2004, 3, 1), 0, 0, 0, 0, 0).getMillis();
 final boolean result = field.isLeap(testDate);
 fail("Expected ChronologyException but got: " + result);
 }

 @Test
 public void testGetLeapAmount() {
 final long testDate = new MutableDateTime(new LocalDate(2004, 2, 29), 0, 0, 0, 0, 0).getMillis();
 final int result = field.getLeapAmount(testDate);
 assertEquals(1, result);
 }

 @Test
 public void testGetLeapDurationField() {
 final long testDate = new MutableDateTime(new LocalDate(2004, 2, 29), 0, 0, 0, 0, 0).getMillis();
 final DurationField result = field.getLeapDurationField();
 assertEquals(DurationFieldType.days(), result.getType());
 }

 @Test
 public void testGetMinimumValue() {
 final int result = field.getMinimumValue();
 assertEquals(1, result);
 }

 @Test
 public void testGetMaximumValue() {
 final int result = field.getMaximumValue();
 assertEquals(12, result);
 }

 @Test
 public void testRoundFloor() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final long result = field.roundFloor(testDate);
 final long expected = new MutableDateTime(new LocalDate(2005, 12, 1), 0, 0, 0, 0, 0).getMillis();
 assertEquals(expected, result);
 }

 @Test
 public void testRemainder() {
 final long testDate = new MutableDateTime(new LocalDate(2005, 12, 31), 23, 59, 59, 59, 593).getMillis();
 final long result = field.remainder(testDate);
 final long expected = 0;
 assertEquals(expected, result);
 }
}