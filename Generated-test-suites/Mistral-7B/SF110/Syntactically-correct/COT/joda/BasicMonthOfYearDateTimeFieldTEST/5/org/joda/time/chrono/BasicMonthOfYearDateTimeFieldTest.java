import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicGregorianChronology;
import org.joda.time.field.DateTimeFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology iChronology = new BasicGregorianChronology();

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 assertEquals(false, field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();
 assertEquals(DateTimeConstants.JANUARY, field.get(testDate));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 long result = field.add(testDate, 1);
 assertEquals(DateTimeConstants.FEBRUARY, field.get(result));

 result = field.add(testDate, -1);
 assertEquals(DateTimeConstants.DECEMBER, field.get(result));

 result = field.add(testDate, 13);
 assertEquals(DateTimeConstants.MARCH, field.get(result));

 result = field.add(testDate, -12);
 assertEquals(DateTimeConstants.FEBRUARY, field.get(result));
 }

 @Test
 public void testAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 long result = field.add(testDate, 5000);
 assertEquals(DateTimeConstants.MAY, field.get(result));

 result = field.add(testDate, -5000);
 assertEquals(DateTimeConstants.OCTOBER, field.get(result));
 }

 @Test
 public void testAddWithWrap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.DECEMBER);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 long result = field.addWrapField(testDate, 12);
 assertEquals(DateTimeConstants.JANUARY, field.get(result));

 result = field.addWrapField(testDate, -12);
 assertEquals(DateTimeConstants.DECEMBER, field.get(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOutOfRange() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 field.add(testDate, Int.MAX_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNegativeOutOfRange() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 field.add(testDate, Int.MIN_VALUE);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate1 = new BaseDateTime(1234567890L, iChronology).getMillis();
 long testDate2 = new BaseDateTime(1357946380L, iChronology).getMillis();

 long result = field.getDifferenceAsLong(testDate1, testDate2);
 assertEquals(1, result);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();
 int month = 5;
 long setDate = field.set(testDate, month);

 assertEquals(DateTimeConstants.MAY, field.get(setDate));
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 assertEquals(DateTimeFieldType.years(), field.getRangeDurationField());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 assertTrue(field.isLeap(testDate));
 testDate += 31 * 28; // leap year, February
 assertFalse(field.isLeap(testDate));
 testDate += 31 * 28 + 1; // leap year, February with a leap day
 assertTrue(field.isLeap(testDate));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.FEBRUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 assertEquals(0, field.getLeapAmount(testDate));
 testDate += 31 * 28; // leap year, February
 assertEquals(1, field.getLeapAmount(testDate));
 testDate += 1; // February with a leap day
 assertEquals(0, field.getLeapAmount(testDate));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.FEBRUARY);
 assertEquals(DateTimeFieldType.days(), field.getLeapDurationField());
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();

 assertEquals(testDate, field.roundFloor(testDate));
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);
 long testDate = new BaseDateTime(1234567890L, iChronology).getMillis();
 long remainder = testDate % (12 * 30);

 assertEquals(remainder, field.remainder(testDate));
 }
}