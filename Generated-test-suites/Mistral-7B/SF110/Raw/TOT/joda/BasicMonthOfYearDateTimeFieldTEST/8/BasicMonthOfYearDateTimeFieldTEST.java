import org.junit.jupiter.api.Test;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import static org.junit.jupiter.api.Assertions.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology ISO_CALENDAR = new GregorianChronology();

 @Test
 public void testBasicMonthOfYearDateTimeFieldConstruction() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
 assertEquals(ISO_CALENDAR, field.getChronology());
 assertEquals(2, field.getLeapMonth());
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 int month = field.get(date);
 assertEquals(3, month);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 long result = field.add(date, 1);
 LocalDateTime expected = new LocalDateTime(2022, 4, 30, 0, 0, 0, 0);
 assertEquals(expected.toInstant().toEpochMilli(), result);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddNegative() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 4, 30, 0, 0, 0, 0).toInstant().toEpochMilli();
 long result = field.add(date, -1);
 LocalDateTime expected = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0);
 assertEquals(expected.toInstant().toEpochMilli(), result);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddTooLarge() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 assertThrows(IllegalArgumentException.class, () -> field.add(date, Integer.MAX_VALUE));
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddWrapped() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 12, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 long result = field.addWrapField(date, Integer.MAX_VALUE);
 LocalDateTime expected = new LocalDateTime(2023, 1, 1, 0, 0, 0, 0);
 assertEquals(expected.toInstant().toEpochMilli(), result);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldAddContiguous() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 int[] values = {1, 2, 3};
 long[] expected = {3229465600000L, 3230324480000L, 3231183360000L};
 int[] result = field.add(new GeneralDate(date), values, new int[values.length], 0);
 assertArrayEquals(values, result);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field1 = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 BasicMonthOfYearDateTimeField field2 = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date1 = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 long date2 = new LocalDateTime(2022, 4, 30, 0, 0, 0, 0).toInstant().toEpochMilli();
 long difference = field1.getDifferenceAsLong(date1, date2);
 assertEquals(31 * ISO_CALENDAR.getMillisPerDay(), difference);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 int month = 5;
 long result = field.set(date, month);
 LocalDateTime expected = new LocalDateTime(2022, 5, 30, 0, 0, 0, 0);
 assertEquals(expected.toInstant().toEpochMilli(), result);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 DurationField durationField = field.getRangeDurationField();
 assertEquals(DurationFieldType.years(), durationField.getFieldType());
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 2, 29, 0, 0, 0, 0).toInstant().toEpochMilli();
 assertTrue(field.isLeap(date));
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 2, 29, 0, 0, 0, 0).toInstant().toEpochMilli();
 assertEquals(1, field.getLeapAmount(date));
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 DurationField durationField = field.getLeapDurationField();
 assertEquals(DurationFieldType.days(), durationField.getFieldType());
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 long roundedFloor = field.roundFloor(date);
 LocalDateTime expected = new LocalDateTime(2022, 3, 1, 0, 0, 0, 0);
 assertEquals(expected.toInstant().toEpochMilli(), roundedFloor);
 }

 @Test
 public void testBasicMonthOfYearDateTimeFieldRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CALENDAR, 2);
 long date = new LocalDateTime(2022, 3, 31, 0, 0, 0, 0).toInstant().toEpochMilli();
 long remainder = field.remainder(date);
 assertEquals(0, remainder);
 }
}