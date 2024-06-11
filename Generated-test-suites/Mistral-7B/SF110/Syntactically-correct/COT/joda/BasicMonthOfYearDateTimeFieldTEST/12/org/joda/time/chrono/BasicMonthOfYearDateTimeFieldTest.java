import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.BaseCalendar;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setUp() {
 field = new BasicMonthOfYearDateTimeField(BaseCalendar.UTC, DateTimeConstants.JANUARY);
 }

 @Test
 public void testIsLenient() {
 Assert.assertFalse(field.isLenient());
 }

 @Test
 public void testGetWhenValid() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 int month = field.get(date);
 Assert.assertEquals(1, month);
 }

 @Test
 public void testAddWhenValid() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.add(date, 1);
 long expectedDate = DateTimeConstants.MILLIS_PER_MONTH + DateTimeConstants.MINUTE_OF_DAY;
 Assert.assertEquals(expectedDate, newDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWhenMagnitudeTooLarge() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.add(date, Integer.MAX_VALUE + 1);
 }

 @Test
 public void testAddWhenNegative() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.add(date, -1);
 long expectedDate = DateTimeConstants.MILLIS_PER_MONTH - DateTimeConstants.MINUTE_OF_DAY;
 Assert.assertEquals(expectedDate, newDate);
 }

 @Test
 public void testAddWrapField() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.addWrapField(date, 13);
 long expectedDate = DateTimeConstants.MILLIS_PER_MONTH + DateTimeConstants.MINUTE_OF_DAY + 12 * DateTimeConstants.MILLIS_PER_MONTH;
 Assert.assertEquals(expectedDate, newDate);
 }

 @Test
 public void testGetDifferenceAsLong() {
 long startDate = DateTimeConstants.MINUTE_OF_DAY;
 long endDate = DateTimeConstants.HOURS_PER_DAY * 3 + DateTimeConstants.MINUTE_OF_HOUR;
 long difference = field.getDifferenceAsLong(startDate, endDate);
 Assert.assertEquals(DateTimeConstants.MILLIS_PER_MONTH * 3 + DateTimeConstants.HOURS_PER_DAY * 3 + DateTimeConstants.MINUTES_PER_HOUR * 3, difference);
 }

 @Test
 public void testSet() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.set(date, DateTimeConstants.MARCH);
 long expectedDate = DateTimeConstants.MILLIS_PER_MONTH * 2 + DateTimeConstants.MINUTE_OF_DAY;
 Assert.assertEquals(expectedDate, newDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWhenInvalidValue() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.set(date, DateTimeConstants.FEBRUARY + 13);
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField durationField = field.getRangeDurationField();
 Assert.assertEquals(DateTimeFieldType.years(), durationField.getFieldType());
 }

 @Test
 public void testIsLeap() {
 long date = DateTimeConstants.MILLIS_PER_YEAR / 2 + DateTimeConstants.MINUTE_OF_DAY;
 boolean isLeap = field.isLeap(date);
 Assert.assertFalse(isLeap);
 }

 @Test
 public void testGetLeapAmount() {
 long date = DateTimeConstants.MILLIS_PER_YEAR / 2 + DateTimeConstants.MINUTE_OF_DAY;
 int leapAmount = field.getLeapAmount(date);
 Assert.assertEquals(0, leapAmount);
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField durationField = field.getLeapDurationField();
 Assert.assertEquals(DateTimeFieldType.days(), durationField.getFieldType());
 }

 @Test
 public void testGetMinimumValue() {
 int minimumValue = field.getMinimumValue();
 Assert.assertEquals(1, minimumValue);
 }

 @Test
 public void testGetMaximumValue() {
 int maximumValue = field.getMaximumValue();
 Assert.assertEquals(12, maximumValue);
 }

 @Test
 public void testRoundFloor() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long expectedDate = DateTimeConstants.MILLIS_PER_MONTH;
 long floorDate = field.roundFloor(date);
 Assert.assertEquals(expectedDate, floorDate);
 }

 @Test
 public void testRemainder() {
 long date = DateTimeConstants.MINUTE_OF_DAY;
 long remainder = field.remainder(date);
 Assert.assertEquals(0, remainder);
 }

 @Test
 public void testSetWhenDateWithSameMonthButDifferentYear() {
 long startDate = DateTimeConstants.MILLIS_PER_YEAR / 2 + DateTimeConstants.MINUTE_OF_DAY;
 long newDate = field.set(startDate, DateTimeConstants.FEBRUARY);
 long expectedDate = DateTimeConstants.MILLIS_PER_YEAR / 2 + DateTimeConstants.MILLIS_PER_MONTH;
 Assert.assertEquals(expectedDate, newDate);
 }

 @Test
 public void testSetWhenDateWithDifferentMonthButSameYear() {
 long startDate = DateTimeConstants.MILLIS_PER_MONTH;
 long newDate = field.set(startDate, DateTimeConstants.MARCH);
 long expectedDate = DateTimeConstants.MILLIS_PER_MONTH + DateTimeConstants.MILLIS_PER_MONTH;
 Assert.assertEquals(expectedDate, newDate);
 }

 @Test
 public void testSetWhenDateWithDifferentMonthAndDifferentYear() {
 long startDate = DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_PER_MONTH;
 long newDate = field.set(startDate, DateTimeConstants.APRIL);
 long expectedDate = DateTimeConstants.MILLIS_PER_YEAR + 2 * DateTimeConstants.MILLIS_PER_MONTH;
 Assert.assertEquals(expectedDate, newDate);
 }
}