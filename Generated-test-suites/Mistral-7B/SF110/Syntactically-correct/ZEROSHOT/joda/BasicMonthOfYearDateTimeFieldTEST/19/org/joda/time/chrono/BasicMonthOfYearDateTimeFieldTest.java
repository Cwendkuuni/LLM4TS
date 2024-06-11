import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.Months;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_8601 = Months.getISO8601();

 @Test
 public void testBasicCreation() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(ISO_8601, field.getChronology());
 assertEquals(2, field.getMaximumValue());
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 DateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 assertEquals(2, field.get(dateTime.getMillis()));
 }

 @Test
 public void testAdd() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 DateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 long result = field.add(dateTime.getMillis(), 3);
 LocalDateTime expected = new LocalDateTime(2015, 4, 1, 12, 0, 0, 0, ISO_8601);
 assertEquals(expected.getMillis(), result);
 }

 @Test
 public void testAddLong() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 DateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 long result = field.add(dateTime.getMillis(), 1234567);
 LocalDateTime expected = new LocalDateTime(2016, 5, 22, 12, 0, 0, 0, ISO_8601);
 assertEquals(expected.getMillis(), result);
 }

 @Test
 public void testAddWrapField() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 DateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 long result = field.addWrapField(dateTime.getMillis(), 13);
 LocalDateTime expected = new LocalDateTime(2015, 3, 1, 12, 0, 0, 0, ISO_8601);
 assertEquals(expected.getMillis(), result);
 }

 @Test
 public void testAddReadablePartial() {
 LocalDate baseDate = new LocalDate(2015, 1, 1, ISO_8601);
 DateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 LocalDate testDate = baseDate.withMonthOfYearAdded(3);
 ReadablePartial readablePartial = new BaseDateTime(testDate.toDate().getTime(), ISO_8601).toLocalDateTime().toDateMidnight().toLocalDate().toReadOnlyFields();
 int[] result = field.add(readablePartial, 2, new int[3], 3);
 assertEquals(result[0], 4);
 assertEquals(result[1], 1);
 assertEquals(result[2], 1);
 }

 @Test
 public void testAddWrapFieldReadablePartial() {
 LocalDate baseDate = new LocalDate(2015, 1, 1, ISO_8601);
 DateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, 2);
 LocalDate testDate = baseDate.withMonthOfYearAdded(13);
 ReadablePartial readablePartial = new BaseDateTime(testDate.toDate().getTime(), ISO_8601).toLocalDateTime().toDateMidnight().toLocalDate().toReadOnlyFields();
 int[] result = field.addWrapField(readablePartial, 14, new int[3], 3);
 assertEquals(result[0], 3);
 assertEquals(result[1], 1);
 assertEquals(result[2], 12);
 }

 @Test
 public void testIsLeap() {
 MutableDateTime dateTime = new MutableDateTime(2016, 2, 1, 12, 0, 0, 0, ISO_8601);
 assertTrue(new BasicMonthOfYearDateTimeField(ISO_8601, 2).isLeap(dateTime.getMillis()));
 dateTime.setYear(2015);
 assertFalse(new BasicMonthOfYearDateTimeField(ISO_8601, 2).isLeap(dateTime.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 MutableDateTime dateTime = new MutableDateTime(2016, 2, 1, 12, 0, 0, 0, ISO_8601);
 assertEquals(1, new BasicMonthOfYearDateTimeField(ISO_8601, 2).getLeapAmount(dateTime.getMillis()));
 dateTime.setYear(2015);
 assertEquals(0, new BasicMonthOfYearDateTimeField(ISO_8601, 2).getLeapAmount(dateTime.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 assertEquals(DurationFieldType.days(), new BasicMonthOfYearDateTimeField(ISO_8601, 2).getLeapDurationField());
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals(1, new BasicMonthOfYearDateTimeField(ISO_8601, 2).getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals(12, new BasicMonthOfYearDateTimeField(ISO_8601, 2).getMaximumValue());
 }

 @Test
 public void testGetDifferenceAsLong() {
 MutableDateTime dateTime1 = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 MutableDateTime dateTime2 = new MutableDateTime(2015, 3, 1, 12, 0, 0, 0, ISO_8601);
 assertEquals(1 * 12 + 1, new BasicMonthOfYearDateTimeField(ISO_8601, 2).getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis()));
 }

 @Test
 public void testSet() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 LocalDate targetDate = new LocalDate(2015, 3, 1, ISO_8601);
 assertEquals(targetDate.getMillis(), new BasicMonthOfYearDateTimeField(ISO_8601, 2).set(dateTime.getMillis(), 3).getMillis());
 }

 @Test
 public void testRoundFloor() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601);
 assertEquals(new LocalDateTime(2015, 1, 1, 12, 0, 0, 0, ISO_8601).getMillis(), new BasicMonthOfYearDateTimeField(ISO_8601, 2).roundFloor(dateTime.getMillis()));
 }

 @Test
 public void testRemainder() {
 MutableDateTime dateTime = new MutableDateTime(2015, 1, 1, 12, 0, 1, 0, ISO_8601);
 long remainder = new BasicMonthOfYearDateTimeField(ISO_8601, 2).remainder(dateTime.getMillis());
 assertEquals(1, remainder);
 }
}