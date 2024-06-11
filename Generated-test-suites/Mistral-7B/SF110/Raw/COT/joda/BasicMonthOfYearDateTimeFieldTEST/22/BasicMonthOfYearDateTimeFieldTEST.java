import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology CHRONOLOGY = new BaseDateTime().getChronology();

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet_int() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 int expected = CHRONOLOGY.getMonthOfYear(testTime);
 int actual = field.get(testTime);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd_long_int() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 long expected = new MutableDateTime(2022, 3, 3, CHRONOLOGY).getMillis();
 long actual = field.add(testTime, 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd_long_long() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 long expected = new MutableDateTime(2023, 3, 3, CHRONOLOGY).getMillis();
 long actual = field.add(testTime, 12);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapField_long_int() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 long testTime = new MutableDateTime(2022, 13, 3, CHRONOLOGY).getMillis(); // out of range
 long expected = new MutableDateTime(2023, 1, 3, CHRONOLOGY).getMillis();
 long actual = field.addWrapField(testTime, 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd_ReadablePartial_int_int_int() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 ReadablePartial readablePartial = new MutableDateTime(2022, 2, 3, CHRONOLOGY).toLocalDate().toDateTimeAtStartOfDay().toMutableDateTime().getLocalDate().toLocalDate().toReadablePartial();
 int[] expected = new int[]{3, 0};
 int[] actual = field.add(readablePartial, 1, new int[2], 0);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testSet_long_int() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 int setValue = 3;
 long expected = new MutableDateTime(2022, 3, 3, CHRONOLOGY).getMillis();
 long actual = field.set(testTime, setValue);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_long_int_IllegalArgumentException() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 int addAmount = 13 * CHRONOLOGY.getMillisPerYear(); // too large magnitude
 long actual = field.add(testTime, addAmount);
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 int maxValue = field.getMaximumValue();
 assertEquals(CHRONOLOGY.getMaxMonth(), maxValue);
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 int minValue = field.getMinimumValue();
 assertEquals(1, minValue);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 0);
 DurationFieldType expected = DurationFieldType.years();
 DurationField fieldDuration = field.getRangeDurationField();
 assertEquals(expected, fieldDuration.getClass());
 }

 @Test
 public void testIsLeap_long() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2); // February
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 boolean expected = CHRONOLOGY.isLeapYear(CHRONOLOGY.getYear(testTime));
 boolean actual = field.isLeap(testTime);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapAmount_long() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2); // February
 long testTime = new MutableDateTime(2022, 2, 3, CHRONOLOGY).getMillis();
 int expected = CHRONOLOGY.isLeapYear(CHRONOLOGY.getYear(testTime)) ? 1 : 0;
 int actual = field.getLeapAmount(testTime);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2); // February
 DurationFieldType expected = DurationFieldType.days();
 DurationField fieldDuration = field.getLeapDurationField();
 assertEquals(expected, fieldDuration.getClass());
 }

}