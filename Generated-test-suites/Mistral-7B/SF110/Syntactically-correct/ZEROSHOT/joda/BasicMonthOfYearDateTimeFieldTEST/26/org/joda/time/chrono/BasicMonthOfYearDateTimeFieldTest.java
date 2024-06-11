import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeFactory;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology JULIAN = new JulianChronology();
 private static final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN, 2);

 @Test
 public void testInstance() {
 assertInstanceOf(BasicMonthOfYearDateTimeField.class, field);
 }

 @Test
 public void testIsLenient() {
 assertEquals(false, field.isLenient());
 }

 @Test
 public void testGet() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final int expected = 1;
 final int actual = field.get(testValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final int amount = 12;
 final long expected = new BaseDateTime(0, 2016, 3, 1, 0, 0, 0, JULIAN).getMillis();
 final long actual = field.add(testValue, amount);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddLong() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final long amount = 365L;
 final long expected = new BaseDateTime(0, 2016, 2, 1, 0, 0, 0, JULIAN).getMillis();
 final long actual = field.add(testValue, amount);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWrappedValue() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final int amount = 13;
 final long expected = new BaseDateTime(0, 2016, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final long actual = field.addWrapField(testValue, amount);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDifferenceAsLong() {
 final long testValue1 = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final long testValue2 = new BaseDateTime(0, 2016, 2, 1, 0, 0, 0, JULIAN).getMillis();
 final int expected = 365;
 final long actual = field.getDifferenceAsLong(testValue1, testValue2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final int newValue = 3;
 final long expected = new BaseDateTime(0, 2015, 3, 1, 0, 0, 0, JULIAN).getMillis();
 final long actual = field.set(testValue, newValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRangeDurationField() {
 final DurationFieldType expected = DurationFieldType.years();
 final DurationFieldType actual = field.getRangeDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLeap() {
 final long testValue = new BaseDateTime(0, 2016, 2, 29, 0, 0, 0, JULIAN).getMillis();
 final boolean expected = true;
 final boolean actual = field.isLeap(testValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapAmount() {
 final long testValue = new BaseDateTime(0, 2016, 2, 29, 0, 0, 0, JULIAN).getMillis();
 final int expected = 1;
 final int actual = field.getLeapAmount(testValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapDurationField() {
 final DurationFieldType expected = DurationFieldType.days();
 final DurationFieldType actual = field.getLeapDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMinimumValue() {
 final int expected = 1;
 final int actual = field.getMinimumValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaximumValue() {
 final int expected = 12;
 final int actual = field.getMaximumValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testRoundFloor() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 15, 30, 0, JULIAN).getMillis();
 final long expected = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final long actual = field.roundFloor(testValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemainder() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 15, 30, 0, JULIAN).getMillis();
 final long expected = 21600000L;
 final long actual = field.remainder(testValue);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddTooLarge() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final long amount = Long.MAX_VALUE + 1;
 final long expected = TestUtil.INVALID_MILLISECOND;
 field.add(testValue, amount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWrappedValueTooLarge() {
 final long testValue = new BaseDateTime(0, 2015, 1, 1, 0, 0, 0, JULIAN).getMillis();
 final int amount = Integer.MAX_VALUE + 1;
 final long expected = TestUtil.INVALID_MILLISECOND;
 field.addWrapField(testValue, amount);
 }

 @Test
 public void testAddReadablePartial() {
 final MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(new LocalDate(2015, 1, 1), new LocalTime(0, 0, 0, 0));
 mutableLocalDateTime.setField(DateTimeFieldType.monthOfYear(), 2);
 final int[] days = new int[1];
 final LocalDateTime result = field.add(mutableLocalDateTime, 1, null, days);
 final LocalDateTime expected = new LocalDateTime(2015, 2, 1, 0, 0, 0);
 assertEquals(expected, result);
 assertEquals(1, days[0]);
 }
}