import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private Chronology isoChronology = new ISOChronology();

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 assertFalse(testField.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 assertEquals(1, testField.get(testDate));
 }

 @Test
 public void testAddWithinLimits() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long result = testField.add(testDate, 1);
 assertEquals(new LocalDateTime(2022, 2, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testAddWithinLeapYear() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.FEBRUARY);
 long testDate = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 long result = testField.add(testDate, 1);
 assertEquals(new LocalDateTime(2022, 3, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testAddAcrossLeapYear() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.FEBRUARY);
 long testDate = new LocalDateTime(2021, 2, 28, 0, 0, 0).getMillis();
 long result = testField.add(testDate, 12);
 assertEquals(new LocalDateTime(2022, 3, 1, 0, 0, 0).getMillis(), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOutOfLimits() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long result = testField.add(testDate, 13);
 fail("Exception should have been thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNegativeValue() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long result = testField.add(testDate, -1);
 fail("Exception should have been thrown");
 }

 @Test
 public void testAddWithWrappedValue() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 12, 31, 0, 0, 0).getMillis();
 long result = testField.addWrapField(testDate, 12);
 assertEquals(new LocalDateTime(2023, 1, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testAddWithReadablePartial() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 ReadablePartial readablePartial = new LocalDate(2022, 1, 1).toLocalDateTime().toDateTime(isoChronology).toLocalDateTime().toDateTimeFields();
 int[] expected = { 1, 1, 1 };
 int[] result = testField.add(readablePartial, 1, new int[3], 0);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate1 = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long testDate2 = new LocalDateTime(2022, 1, 15, 0, 0, 0).getMillis();
 long result = testField.getDifferenceAsLong(testDate1, testDate2);
 assertEquals(14, result);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long result = testField.set(testDate, 2);
 assertEquals(new LocalDateTime(2022, 2, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testSetWithMaxValue() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 12, 31, 0, 0, 0).getMillis();
 long result = testField.set(testDate, 13);
 assertEquals(new LocalDateTime(2023, 1, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testSetWithMinValue() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long result = testField.set(testDate, 0);
 assertEquals(new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 DurationField result = testField.getRangeDurationField();
 assertEquals(DurationFieldType.YEARS, result.getFieldType());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.FEBRUARY);
 long testDate = new LocalDateTime(2022, 2, 1, 0, 0, 0).getMillis();
 assertTrue(testField.isLeap(testDate));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.FEBRUARY);
 long testDate = new LocalDateTime(2022, 2, 1, 0, 0, 0).getMillis();
 assertEquals(1, testField.getLeapAmount(testDate));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.FEBRUARY);
 DurationField result = testField.getLeapDurationField();
 assertEquals(DurationFieldType.DAYS, result.getFieldType());
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 assertEquals(1, testField.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 assertEquals(12, testField.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 15, 0, 0, 0).getMillis();
 assertEquals(new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis(), testField.roundFloor(testDate));
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 long testDate = new LocalDateTime(2022, 1, 16, 0, 0, 0).getMillis();
 assertEquals(1, testField.remainder(testDate));
 }

 @Test
 public void testReadResolve() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);
 assertEquals(isoChronology.monthOfYear(), testField.readResolve());
 }
}