import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.field.MonthOfYearField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology UTC_CHRONOLOGY = DateTimeUtils.UTC;

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(false, field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();
 assertEquals(1, field.get(testTime));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();

 long result = field.add(testTime, 1);
 assertEquals(new LocalDate(2022, 2, 1).toInstant().getMillis(), result);

 result = field.add(testTime, -1);
 assertEquals(new LocalDate(2022, 12, 31).toInstant().getMillis(), result);
 }

 @Test
 public void testAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();

 long result = field.add(testTime, 2);
 assertEquals(new LocalDate(2022, 3, 1).toInstant().getMillis(), result);

 result = field.add(testTime, -3);
 assertEquals(new LocalDate(2022, 10, 31).toInstant().getMillis(), result);
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 31, 0, 0, 0, 0).toInstant().getMillis();

 long result = field.addWrapField(testTime, 13);
 assertEquals(new LocalDate(2023, 2, 1).toInstant().getMillis(), result);
 }

 @Test
 public void testAddReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);

 MutableLocalDateTime testDate = new MutableLocalDateTime(2022, 1, 1, 0, 0, 0, 0);
 int[] expected = {12, 1};
 int n2 = 3;
 int[] result = field.add(testDate, 0, new int[]{1, 1}, n2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithIllegalArgumentException() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();

 field.add(testTime, Integer.MAX_VALUE + 1);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();
 long testTime2 = new LocalDateTime(2022, 3, 1, 0, 0, 0, 0).toInstant().getMillis();

 long expected = 1;
 long result = field.getDifferenceAsLong(testTime1, testTime2);
 assertEquals(expected, result);
 }

 @Test
 public void testSetGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();

 long result = field.set(testTime, DateTimeConstants.FEBRUARY);
 assertEquals(new LocalDate(2022, 2, 1).toInstant().getMillis(), result);

 assertEquals(2, field.get(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithIllegalArgumentException() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0).toInstant().getMillis();

 field.set(testTime, 13);
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 DurationFieldType expected = DateTimeConstants.YEARS;
 assertEquals(expected, field.getRangeDurationField());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.GERMAN, DateTimeConstants.FEBRUARY);
 assertTrue(field.isLeap(new LocalDateTime(2022, 2, 29, 0, 0, 0, 0).toInstant().getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.GERMAN, DateTimeConstants.FEBRUARY);
 assertEquals(1, field.getLeapAmount(new LocalDateTime(2022, 2, 29, 0, 0, 0, 0).toInstant().getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.GERMAN, DateTimeConstants.FEBRUARY);
 DurationFieldType expected = DateTimeConstants.DAYS;
 assertEquals(expected, field.getLeapDurationField());
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 31, 0, 0, 0, 0).toInstant().getMillis();

 long expected = new LocalDate(2022, 1, 1).toInstant().getMillis();
 long result = field.roundFloor(testTime);
 assertEquals(expected, result);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC_CHRONOLOGY, DateTimeConstants.JANUARY);
 long testTime = new LocalDateTime(2022, 1, 31, 0, 0, 0, 0).toInstant().getMillis();

 long expected = new LocalDate(2022, 1, 31).toInstant().getMillis() - new LocalDate(2022, 1, 1).toInstant().getMillis();
 long result = field.remainder(testTime);
 assertEquals(expected, result);
 }
}