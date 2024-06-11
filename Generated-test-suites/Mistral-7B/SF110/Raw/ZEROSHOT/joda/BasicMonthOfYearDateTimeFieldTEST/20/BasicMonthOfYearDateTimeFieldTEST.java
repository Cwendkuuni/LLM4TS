import org.joda.time.*;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.DateTimeFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 int expected = 3; // March
 int actual = field.get(testDate);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 long expected = new DateTime(2016, 4, 15).getMillis();
 long actual = field.add(testDate, 1);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddTooLarge() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 12, 31).getMillis();
 long expected = new DateTime(2017, 1, 1).getMillis();
 long actual = field.add(testDate, 13);
 fail("Adding too large value should throw IllegalArgumentException");
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 29).getMillis(); // February has 29 days in this test case
 long expected = new DateTime(2016, 3, 31).getMillis(); // March should have 31 days
 long actual = field.addWrapField(testDate, 1);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWrapFieldTooLarge() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 29).getMillis(); // February has 29 days in this test case
 long expected = new DateTime(2017, 3, 1).getMillis(); // March of next year
 long actual = field.addWrapField(testDate, 13);
 fail("Adding too large value in wrapField should throw IllegalArgumentException");
 }

 @Test
 public void testAddReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 ReadablePartial readablePartial = new DateTime(2016, 2, 15).toLocalDate().toDateTimeAtStartOfDay().toLocalDateTime().toLocalDate().toLocalDate();
 long testDate = new DateTime(2016, 2, 15).getMillis();
 int[] expected = new int[]{3, 15};
 int[] actual = field.add(readablePartial, 1, new int[]{1, 15}, 0);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate1 = new DateTime(2016, 2, 15).getMillis();
 long testDate2 = new DateTime(2016, 4, 15).getMillis();
 long expected = 1;
 long actual = field.getDifferenceAsLong(testDate1, testDate2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 int expectedMonth = 4;
 long actual = field.set(testDate, 4);
 assertEquals(expectedMonth, field.get(actual));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTooLarge() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 int expectedMonth = 15; // invalid value
 long actual = field.set(testDate, expectedMonth);
 fail("Setting too large value should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegative() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 int expectedMonth = -1; // invalid value
 long actual = field.set(testDate, expectedMonth);
 fail("Setting negative value should throw IllegalArgumentException");
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DurationField expected = newFixedDuration(0, 12, 0);
 DurationField actual = field.getRangeDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(GregorianChronology.getInstance(), 2); // February
 long testDate = new DateTime(2016, 2, 29).getMillis(); // February has 29 days in this test case
 assertTrue(field.isLeap(testDate));
 }

 @Test
 public void testIsNotLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 12); // December
 long testDate = new DateTime(2016, 12, 31).getMillis();
 assertFalse(field.isLeap(testDate));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(GregorianChronology.getInstance(), 2); // February
 long testDate = new DateTime(2016, 2, 29).getMillis(); // February has 29 days in this test case
 int expected = 1;
 int actual = field.getLeapAmount(testDate);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(GregorianChronology.getInstance(), 2); // February
 DurationField expected = newFixedDuration(0, 1, 0);
 DurationField actual = field.getLeapDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 int expected = 1;
 int actual = field.getMinimumValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 int expected = 12;
 int actual = field.getMaximumValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 16).getMillis();
 long expected = new DateTime(2016, 2, 15).getMillis();
 long actual = field.roundFloor(testDate);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 long expected = 0;
 long actual = field.remainder(testDate);
 assertEquals(expected, actual);
 }

 @Test
 public void testRoundFloorNegative() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 14).getMillis();
 long expected = new DateTime(2016, 2, 15).getMillis();
 long actual = field.roundFloor(testDate);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemainderNegative() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 long testDate = new DateTime(2016, 2, 15).getMillis();
 long expected = new DateTime(2016, 2, 14).getMillis();
 long actual = field.remainder(testDate);
 assertEquals(expected, actual);
 }
}