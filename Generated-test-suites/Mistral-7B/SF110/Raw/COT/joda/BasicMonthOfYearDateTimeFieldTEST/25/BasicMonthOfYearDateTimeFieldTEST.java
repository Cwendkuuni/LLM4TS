import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyException;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setup() {
 field = new BasicMonthOfYearDateTimeField(DateTimeUtils.UTC, DateTimeConstants.JANUARY);
 }

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 assertEquals(1, field.get(testDate));
 }

 @Test
 public void testAdd() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 long expected = new LocalDateTime(2012, 2, 1, 0, 0, 0).toMillis();
 assertEquals(expected, field.add(testDate, 1));

 testDate = new LocalDateTime(2012, 12, 31, 0, 0, 0).toMillis();
 expected = new LocalDateTime(2013, 1, 1, 0, 0, 0).toMillis();
 assertEquals(expected, field.add(testDate, 1));

 testDate = new LocalDateTime(2012, 2, 29, 0, 0, 0).toMillis();// Leap year
 expected = new LocalDateTime(2012, 3, 1, 0, 0, 0).toMillis();
 assertEquals(expected, field.add(testDate, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNegativeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.add(testDate, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithWrongTypeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.add(testDate, (byte)1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithTooLargeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.add(testDate, 3661); // more than a year
 }

 @Test
 public void testAddWrapField() {
 long testDate = new LocalDateTime(2012, 12, 31, 0, 0, 0).toMillis();
 long expected = new LocalDateTime(2013, 1, 1, 0, 0, 0).toMillis();
 assertEquals(expected, field.addWrapField(testDate, 1));

 testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 expected = new LocalDateTime(2011, 12, 31, 0, 0, 0).toMillis();
 assertEquals(expected, field.addWrapField(testDate, -1));
 }

 @Test
 public void testGetDifferenceAsLong() {
 long testDate1 = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 long testDate2 = new LocalDateTime(2012, 2, 1, 0, 0, 0).toMillis();
 long expected = 31;
 long difference = field.getDifferenceAsLong(testDate1, testDate2);
 assertEquals(expected, difference);

 testDate1 = new LocalDateTime(2012, 12, 31, 0, 0, 0).toMillis();
 testDate2 = new LocalDateTime(2013, 1, 1, 0, 0, 0).toMillis();
 difference = field.getDifferenceAsLong(testDate2, testDate1);
 assertEquals(-31, difference);
 }

 @Test
 public void testSet() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 long expected = new LocalDateTime(2012, 2, 1, 0, 0, 0).toMillis();
 assertEquals(expected, field.set(testDate, 2));

 testDate = new LocalDateTime(2012, 12, 31, 0, 0, 0).toMillis();
 expected = new LocalDateTime(2012, 11, 30, 0, 0, 0).toMillis();
 assertEquals(expected, field.set(testDate, 11));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithNegativeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.set(testDate, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithWrongTypeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.set(testDate, (byte)1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithTooLargeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.set(testDate, 3661); // more than a year
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField expected = new DurationFieldType() {
 @Override public int getPrecision() { return DurationFieldType.YEARS.getPrecision(); }
 @Override public int getValue(long duration) { return 1; }
 };
 assertEquals(expected, field.getRangeDurationField());
 }

 @Test(expected = ChronologyException.class)
 public void testIsLeapWithNonLeapYear() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.isLeap(testDate);
 }

 @Test(expected = ChronologyException.class)
 public void testIsLeapWithNonLeapMonth() {
 long testDate = new LocalDateTime(2012, 1, 2, 0, 0, 0).toMillis();
 field.isLeap(testDate);
 }

 @Test
 public void testIsLeapWithLeapYearAndLeapMonth() {
 long testDate = new LocalDateTime(2012, 2, 29, 0, 0, 0).toMillis();
 assertTrue(field.isLeap(testDate));
 }

 @Test
 public void testGetLeapAmountWithNonLeapYear() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 assertEquals(0, field.getLeapAmount(testDate));
 }

 @Test
 public void testGetLeapAmountWithLeapYearAndNonLeapMonth() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 assertEquals(0, field.getLeapAmount(testDate));
 }

 @Test
 public void testGetLeapAmountWithLeapYearAndLeapMonth() {
 long testDate = new LocalDateTime(2012, 2, 29, 0, 0, 0).toMillis();
 assertEquals(1, field.getLeapAmount(testDate));
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField expected = new DurationFieldType() {
 @Override public int getPrecision() { return DurationFieldType.DAYS.getPrecision(); }
 @Override public int getValue(long duration) { return 1; }
 };
 assertEquals(expected, field.getLeapDurationField());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullReadablePartial() {
 field.add((ReadablePartial[])null, 1, new int[0], 0);
 }

 @Test
 public void testAddWithEmptyReadablePartial() {
 ReadablePartial readablePartial = new ReadableDateTimeFieldThatAcceptsOnlyMonthOfYear(DateTimeFieldType.monthOfYear(), 12);
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 int[] result = field.add(readablePartial, 1, new int[0], 0);
 assertEquals(new int[]{13}, result);
 }

 @Test
 public void testAddWithContiguousReadablePartial() {
 ReadablePartial readablePartial = new ReadableDateTimeFieldThatAcceptsOnlyMonthOfYear(DateTimeFieldType.monthOfYear(), 12);
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 int[] result = field.add(readablePartial, 2, new int[0], 0);
 ImmutableDateTime expectedDate = new LocalDateTime(2012, 1, 3, 0, 0, 0).plusMonths(2).toDateTime();
 ImmutableDateTime actualDate = new BaseDateTime(testDate, field.getChronology()).toDateTime();
 assertEquals(expectedDate.getMillis(), actualDate.getMillis());
 assertEquals(new int[]{1, 3}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNonContiguousReadablePartial() {
 ReadablePartial readablePartial1 = new ReadableDateTimeFieldThatAcceptsOnlyMonthOfYear(DateTimeFieldType.monthOfYear(), 1);
 ReadablePartial readablePartial2 = new ReadableDateTimeFieldThatAcceptsOnlyMonthOfYear(DateTimeFieldType.monthOfYear(), 12);
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 field.add(new ReadablePartial[]{readablePartial1, readablePartial2}, 1, new int[0], 0);
 }

 @Test
 public void testRoundFloor() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 long expected = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 assertEquals(expected, field.roundFloor(testDate));
 }

 @Test
 public void testRemainder() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 long expected = 0;
 assertEquals(expected, field.remainder(testDate));
 }

 @Test
 public void testRemainderWithNegativeNumber() {
 long testDate = new LocalDateTime(2012, 1, 1, 0, 0, 0).toMillis();
 long expected = -1;
 assertEquals(expected, field.remainder(testDate - 1));
 }
}