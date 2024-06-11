package org.joda.time.chrono;
 import org.joda.time.DateTimeConstants;
 import org.joda.time.DurationFieldType;
 import org.joda.time.FieldUtils;
 import org.joda.time.LocalDate;
 import org.joda.time.LocalDateTime;
 import org.joda.time.LocalTime;
 import org.joda.time.ReadablePartial;
 import org.joda.time.chrono.GregorianChronology;
 import org.junit.Assert;
 import org.junit.Test;

 public class BasicMonthOfYearDateTimeFieldTest {

 @Test
 public void testIsLenient() {
 Assert.assertFalse(new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).isLenient());
 }

 @Test
 public void testGet() {
 final LocalDateTime dateTime = new LocalDateTime(2015, 1, 31, 12, 0);
 final int expected = DateTimeConstants.JANUARY;
 final int actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).get(dateTime.getMilliseconds());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAdd() {
 final LocalDateTime dateTime = new LocalDateTime(2015, 1, 31, 12, 0);
 final long expected = new LocalDateTime(2015, 2, 28, 12, 0).getMilliseconds();
 final long actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).add(dateTime.getMilliseconds(), 30).getMillis();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapField() {
 final long millis = new LocalDateTime(2015, 12, 31, 12, 0).getMilliseconds();
 final int expected = DateTimeConstants.DECEMBER + 1;
 final long actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).addWrapField(millis, 1).getMillis();
 Assert.assertEquals(actual, new LocalDateTime(2016, 1, 31, 12, 0).getMilliseconds());
 }

 @Test
 public void testGetDifferenceAsLong() {
 final LocalDateTime dateTime1 = new LocalDateTime(2015, 1, 31, 12, 0);
 final LocalDateTime dateTime2 = new LocalDateTime(2015, 2, 1, 12, 0);
 final long expected = 31;
 final long actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).getDifferenceAsLong(dateTime1.getMilliseconds(), dateTime2.getMilliseconds());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSet() {
 final LocalDateTime dateTime = new LocalDateTime(2015, 1, 31, 12, 0);
 final long expectedMillis = new LocalDateTime(2015, 2, 28, 12, 0).getMilliseconds();
 final long actualMillis = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).set(dateTime.getMilliseconds(), 28).getMillis();
 Assert.assertEquals(expectedMillis, actualMillis);
 }

 @Test
 public void testGetRangeDurationField() {
 final DurationField actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).getRangeDurationField();
 final DurationField expected = DurationFieldType.years();
 Assert.assertSame(expected, actual);
 }

 @Test
 public void testIsLeap() {
 final LocalDateTime dateTime = new LocalDateTime(2016, 2, 29, 12, 0);
 final boolean actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).isLeap(dateTime.getMilliseconds());
 final boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapAmount() {
 final LocalDateTime dateTime = new LocalDateTime(2016, 2, 29, 12, 0);
 final int actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).getLeapAmount(dateTime.getMilliseconds());
 final int expected = 1;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapDurationField() {
 final DurationField actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).getLeapDurationField();
 final DurationField expected = DurationFieldType.days();
 Assert.assertSame(expected, actual);
 }

 @Test
 public void testGetMinimumValue() {
 final int actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).getMinimumValue();
 final int expected = 1;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetMaximumValue() {
 final int actual = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).getMaximumValue();
 final int expected = 12;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testRoundFloor() {
 final LocalDateTime dateTime = new LocalDateTime(2015, 1, 31, 12, 0);
 final long expectedMillis = new LocalDateTime(2015, 1, 1, 12, 0).getMilliseconds();
 final long actualMillis = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).roundFloor(dateTime.getMilliseconds());
 Assert.assertEquals(expectedMillis, actualMillis);
 }

 @Test
 public void testRemainder() {
 final LocalDateTime dateTime = new LocalDateTime(2015, 1, 31, 12, 0);
 final long expectedMillis = new LocalDateTime(2015, 1, 31, 12, 30000).getMilliseconds() - new LocalDateTime(2015, 1, 31, 12, 0).getMilliseconds();
 final long actualMillis = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).remainder(dateTime.getMilliseconds());
 Assert.assertEquals(expectedMillis, actualMillis);
 }

 @Test
 public void testAddReadablePartial() {
 final LocalDate date = new LocalDate(2015, 1, 31);
 final ReadablePartial readablePartial = new LocalDateField(DateTimeFieldType.dayOfMonth(), GregorianChronology.INSTANCE).set(date, 28);
 final long expectedMillis = new LocalDateTime(2015, 2, 28, 12, 0).getMilliseconds();
 final long actualMillis = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, 2).add(readablePartial, 30, new long[0], 0).getMillis();
 Assert.assertEquals(expectedMillis, actualMillis);
 }
 }