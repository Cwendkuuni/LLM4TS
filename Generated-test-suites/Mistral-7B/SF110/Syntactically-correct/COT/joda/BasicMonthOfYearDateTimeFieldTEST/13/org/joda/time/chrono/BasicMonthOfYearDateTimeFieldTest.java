import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology CHRONOLOGY = DateTimeConstants.ISO_8601;

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 int expected = DateTimeConstants.JANUARY;
 int actual = field.get(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 int addAmount = 1;
 long expected = new LocalDateTime(2023, 2, 1, 0, 0, 0, 0).toMillis();
 long actual = field.add(input, addAmount);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd_NegativeInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 2, 1, 0, 0, 0, 0).toMillis();
 int addAmount = -1;
 long expected = new LocalDateTime(2023, 1, 31, 0, 0, 0, 0).toMillis();
 long actual = field.add(input, addAmount);
 assertEquals(expected, actual);
 }

 @Test
 public void testAdd_MaxValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 12, 31, 0, 0, 0, 0).toMillis();
 int addAmount = 1;
 long expected = new LocalDateTime(2024, 1, 1, 0, 0, 0, 0).toMillis();
 long actual = field.add(input, addAmount);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_InvalidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 int addAmount = 13;
 field.add(input, addAmount);
 }

 @Test
 public void testAddWrapField_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 12, 31, 0, 0, 0, 0).toMillis();
 int addAmount = 1;
 long expected = new LocalDateTime(2024, 1, 1, 0, 0, 0, 0).toMillis();
 long actual = field.addWrapField(input, addAmount);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddWrapField_MaxValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 12, 31, 0, 0, 0, 0).toMillis();
 int addAmount = 13;
 long expected = new LocalDateTime(2024, 1, 1, 0, 0, 0, 0).toMillis();
 long actual = field.addWrapField(input, addAmount);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWrapField_InvalidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 int addAmount = -1;
 field.addWrapField(input, addAmount);
 }

 @Test
 public void testGetDifferenceAsLong_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long date1 = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 long date2 = new LocalDateTime(2023, 2, 1, 0, 0, 0, 0).toMillis();
 int expected = 1;
 long actual = field.getDifferenceAsLong(date1, date2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 int setValue = 2;
 long expected = new LocalDateTime(2023, 2, 1, 0, 0, 0, 0).toMillis();
 long actual = field.set(input, setValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testSet_MaxValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 12, 31, 0, 0, 0, 0).toMillis();
 int setValue = 13;
 long expected = new LocalDateTime(2024, 1, 1, 0, 0, 0, 0).toMillis();
 long actual = field.set(input, setValue);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_InvalidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 int setValue = 0;
 field.set(input, setValue);
 }

 @Test
 public void testIsLeap_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
 long input = new LocalDateTime(2023, 2, 28, 0, 0, 0, 0).toMillis();
 boolean expected = false;
 boolean actual = field.isLeap(input);
 assertFalse(actual);
 }

 @Test
 public void testIsLeap_LeapYear() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
 long input = new LocalDateTime(2024, 2, 29, 0, 0, 0, 0).toMillis();
 boolean expected = true;
 boolean actual = field.isLeap(input);
 assertTrue(actual);
 }

 @Test
 public void testGetLeapAmount_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
 long input = new LocalDateTime(2023, 2, 28, 0, 0, 0, 0).toMillis();
 int expected = 0;
 int actual = field.getLeapAmount(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLeapAmount_LeapYear() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
 long input = new LocalDateTime(2024, 2, 29, 0, 0, 0, 0).toMillis();
 int expected = 1;
 int actual = field.getLeapAmount(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 DurationField expected = CHRONOLOGY.years();
 DurationField actual = field.getRangeDurationField();
 assertEquals(expected, actual);
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 12, 34, 56, 789).toMillis();
 long expected = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0).toMillis();
 long actual = field.roundFloor(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
 long input = new LocalDateTime(2023, 1, 1, 12, 34, 56, 789).toMillis();
 long expected = new LocalDateTime(2023, 1, 1, 12, 34, 56, 789).getMillisOfDay() % 30;
 long actual = field.remainder(input);
 assertEquals(expected, actual);
 }

}