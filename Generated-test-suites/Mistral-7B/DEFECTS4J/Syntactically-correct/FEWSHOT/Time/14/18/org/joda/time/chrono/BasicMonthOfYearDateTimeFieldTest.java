package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.getUTC(), DateTimeConstants.JANUARY);

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 int expected = 2;
 long instant = DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2023, DateTimeConstants.MONTH_FEBRUARY, expected);
 int result = field.get(instant);
 assertEquals(expected, result);
 }

 @Test
 public void testAdd() {
 long instant = DateTimeConstants.MILLIS_EPOCH;
 int months = 2;
 long result = field.add(instant, months);
 assertEquals(DateTimeConstants.MILLIS_EPOCH + 2 * DateTimeConstants.MILLIS_PER_MONTH, result);
 }

 @Test
 public void testAddWrapField() {
 long instant = DateTimeConstants.MILLIS_EPOCH;
 int months = 13;
 long result = field.addWrapField(instant, months);
 assertEquals(result, field.set(instant, 1).getMillis());
 }

 @Test
 public void testGetDifferenceAsLong() {
 long minuendInstant = DateTimeConstants.MILLIS_EPOCH;
 long subtrahendInstant = DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2023, DateTimeConstants.MONTH_JANUARY, 1);
 long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 assertEquals(61, difference);
 }

 @Test
 public void testSet() {
 long instant = DateTimeConstants.MILLIS_EPOCH;
 int month = 13;
 long result = field.set(instant, month);
 assertEquals(result, field.set(instant, 1).getMillis());
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField result = field.getRangeDurationField();
 assertEquals(DateTimeFieldType.years(), result.getFieldType());
 }

 @Test
 public void testIsLeap() {
 long instant = DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2024, DateTimeConstants.MONTH_FEBRUARY, 1);
 assertTrue(field.isLeap(instant));
 }

 @Test
 public void testGetLeapAmount() {
 long instant = DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2023, DateTimeConstants.MONTH_FEBRUARY, 1);
 assertEquals(0, field.getLeapAmount(instant));
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField result = field.getLeapDurationField();
 assertEquals(DateTimeFieldType.days(), result.getFieldType());
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 long instant = DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2023, DateTimeConstants.MONTH_MAY, 15);
 long result = field.roundFloor(instant);
 assertEquals(DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2023, DateTimeConstants.MONTH_MAY, 15) - field.getRangeDurationField().getMillis(), result);
 }

 @Test
 public void testRemainder() {
 long instant = DateTimeUtils.toMilliseconds(DateTimeConstants.YEAR_2023, DateTimeConstants.MONTH_MAY, 15);
 long result = field.remainder(instant);
 assertEquals(field.getRangeDurationField().getMillis(), result);
 }
}