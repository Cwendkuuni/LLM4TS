import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.JodaTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology JULIAN_CHRONOLOGY = JulianChronology.getInstance();

 @Test
 public void testGetMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     assertEquals(1, field.get(millis));
 }

 @Test
 public void testAddMethodWithValidArguments() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     long newMillis = field.add(millis, 2);
     LocalDateTime newDate = new LocalDateTime(JodaTimeUtils.millisTo(LocalDateTime.class, newMillis));
     assertEquals(3, field.get(newMillis));
     assertEquals(newDate.getYear(), 2022);
     assertEquals(newDate.getMonthOfYear(), 3);
 }

 @Test
 public void testAddMethodWithNegativeArgument() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     long newMillis = field.add(millis, -2);
     LocalDateTime newDate = new LocalDateTime(JodaTimeUtils.millisTo(LocalDateTime.class, newMillis));
     assertEquals(1, field.get(newMillis));
     assertEquals(newDate.getYear(), 2022);
     assertEquals(newDate.getMonthOfYear(), 11);
 }

 @Test
 public void testAddMethodWithInvalidArguments() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     assertThrows(IllegalArgumentException.class, () -> field.add(millis, Integer.MAX_VALUE));
     assertThrows(IllegalArgumentException.class, () -> field.add(millis, Long.MAX_VALUE));
 }

 @Test
 public void testAddWrapFieldMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     long newMillis = field.addWrapField(millis, 13);
     LocalDateTime newDate = new LocalDateTime(JodaTimeUtils.millisTo(LocalDateTime.class, newMillis));
     assertEquals(14, field.get(newMillis));
     assertEquals(newDate.getYear(), 2022);
     assertEquals(newDate.getMonthOfYear(), 1);
 }

 @Test
 public void testGetDifferenceAsLongMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date1 = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis1 = date1.getMillis();
     LocalDateTime date2 = new LocalDateTime(2022, 2, 28, 0, 0);
     long millis2 = date2.getMillis();
     long difference = field.getDifferenceAsLong(millis1, millis2);
     assertEquals(1, difference);
 }

 @Test
 public void testSetMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     long newMillis = field.set(millis, 3);
     LocalDateTime newDate = new LocalDateTime(JodaTimeUtils.millisTo(LocalDateTime.class, newMillis));
     assertEquals(3, field.get(newMillis));
     assertEquals(newDate.getYear(), 2022);
     assertEquals(newDate.getMonthOfYear(), 3);
 }

 @Test
 public void testGetRangeDurationFieldMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     assertEquals(field.getRangeDurationField().getMillisPerDay(), 30);
 }

 @Test
 public void testIsLeapMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.FEBRUARY);
     LocalDateTime date = new LocalDateTime(2022, 2, 28, 0, 0);
     long millis = date.getMillis();
     assertTrue(field.isLeap(millis));
     date = new LocalDateTime(2023, 2, 28, 0, 0);
     assertFalse(field.isLeap(millis));
 }

 @Test
 public void testGetLeapAmountMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.FEBRUARY);
     LocalDateTime date = new LocalDateTime(2022, 2, 28, 0, 0);
     long millis = date.getMillis();
     assertEquals(1, field.getLeapAmount(millis));
     date = new LocalDateTime(2023, 2, 28, 0, 0);
     assertEquals(0, field.getLeapAmount(millis));
 }

 @Test
 public void testGetLeapDurationFieldMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.FEBRUARY);
     assertEquals(field.getLeapDurationField().getMillisPerDay(), 1);
 }

 @Test
 public void testGetMinimumValueMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     assertEquals(field.getMinimumValue(), 1);
 }

 @Test
 public void testGetMaximumValueMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     assertEquals(field.getMaximumValue(), 12);
 }

 @Test
 public void testRoundFloorMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 0);
     long millis = date.getMillis();
     long roundedMillis = field.roundFloor(millis);
     LocalDateTime roundedDate = new LocalDateTime(JodaTimeUtils.millisTo(LocalDateTime.class, roundedMillis));
     assertEquals(1, field.get(roundedMillis));
     assertEquals(roundedDate.getYear(), 2022);
     assertEquals(roundedDate.getMonthOfYear(), 1);
 }

 @Test
 public void testRemainderMethod() {
     final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(JULIAN_CHRONOLOGY, DateTimeConstants.JANUARY);
     LocalDateTime date = new LocalDateTime(2022, 1, 31, 0, 15);
     long millis = date.getMillis();
     long remainderMillis = field.remainder(millis);
     LocalDateTime remainderDate = new LocalDateTime(JodaTimeUtils.millisTo(LocalDateTime.class, remainderMillis));
     assertEquals(0, field.get(remainderMillis));
     assertEquals(remainderDate.getYear(), 2022);
     assertEquals(remainderDate.getMonthOfYear(), 1);
 }
}