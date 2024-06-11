import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private Chronology chronology = new IsabelaNewStyle(); // Use your desired chronology here

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet_ValidDate() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 1, 1).getTime(); //Valid date
 int month = field.get(date);
 assertEquals(1, month);
 }

 @Test
 public void testGet_OutOfRangeDate_BeforeMinYear() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(1800 - 1900, 1, 1).getTime(); //Out of range date before min year
 int month = field.get(date);
 assertEquals(1, month); //Minimum value is returned in this case
 }

 @Test
 public void testGet_OutOfRangeDate_LeapYear() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2024 - 1900, 3, 1).getTime(); //Leap year date
 int month = field.get(date);
 assertEquals(3, month);
 }

 @Test
 public void testGet_ValidDateWithLeapMonth() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2024 - 1900, 2, 29).getTime(); //Valid date with leap month
 int month = field.get(date);
 assertEquals(2, month);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_OutOfRangeValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 1, 1).getTime(); //Valid date
 long newDate = field.add(date, Int.MAX_VALUE);
 }

 @Test
 public void testAdd_ValidValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 1, 1).getTime(); //Valid date
 long newDate = field.add(date, 1);
 assertEquals(new Date(2022 - 1900, 2, 1).getTime(), newDate);
 }

 @Test
 public void testAdd_NegativeValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 2, 1).getTime(); //Valid date
 long newDate = field.add(date, -1);
 assertEquals(new Date(2022 - 1900, 1, 1).getTime(), newDate);
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 12, 31).getTime(); //Valid date
 long newDate = field.addWrapField(date, 13);
 assertEquals(new Date(2023 - 1900, 1, 1).getTime(), newDate);
 }

 @Test
 public void testAdd_ContiguousPartial() {
 LocalDate date = new LocalDate(new LocalDateTime(2022, 1, 1, 0, 0));
 LocalDate newDate = date.plusMonths(1);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 int[] result = field.add(date.toDate(), 1, new int[3], 0);
 assertEquals(newDate.getMonthOfYear(), result[0]);
 assertEquals(newDate.getDayOfMonth(), result[1]);
 assertEquals(newDate.getYear(), result[2]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_InvalidPartial() {
 LocalDate date = new LocalDate(new LocalDateTime(2022, 1, 1, 0, 0));
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 int[] result = field.add(date.toDate(), 1, new int[] {1, 3}, 0);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BaseDateTime date1 = new BaseDateTime(new Long(2022 - 1900, 1, 1).getTime(), chronology);
 BaseDateTime date2 = new BaseDateTime(new Long(2022 - 1900, 2, 1).getTime(), chronology);
 long difference = date2.getMillis() - date1.getMillis();
 int monthsDifference = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
 assertEquals(monthsDifference, 1);
 }

 @Test
 public void testSet_ValidValue() {
 MutableDateTime date = new MutableDateTime(new Long(2022 - 1900, 1, 1).getTime(), chronology);
 int newMonth = 2;
 long newDate = field.set(date.toLong(), newMonth);
 assertEquals(new Date(2022 - 1900, newMonth, 1).getTime(), newDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_OutOfRangeValue() {
 MutableDateTime date = new MutableDateTime(new Long(2022 - 1900, 1, 1).getTime(), chronology);
 int newMonth = 14; //Out of range month value
 long newDate = field.set(date.toLong(), newMonth);
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField durationField = field.getRangeDurationField();
 assertEquals(DateTimeFieldType.years(), durationField);
 }

 @Test
 public void testIsLeap_ValidDate() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2024 - 1900, 2, 29).getTime(); //Valid leap date
 assertTrue(field.isLeap(date));
 }

 @Test
 public void testIsLeap_NonLeapDate() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 2, 28).getTime(); //Non-leap date
 assertFalse(field.isLeap(date));
 }

 @Test
 public void testGetLeapAmount_LeapDate() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2024 - 1900, 2, 29).getTime(); //Leap date
 int leapAmount = field.getLeapAmount(date);
 assertEquals(1, leapAmount);
 }

 @Test
 public void testGetLeapAmount_NonLeapDate() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
 long date = new Long(2022 - 1900, 2, 28).getTime(); //Non-leap date
 int leapAmount = field.getLeapAmount(date);
 assertEquals(0, leapAmount);
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField durationField = field.getLeapDurationField();
 assertEquals(DateTimeFieldType.days(), durationField);
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 MutableDateTime date = new MutableDateTime(new Long(2022 - 1900, 2, 15).getTime(), chronology);
 long roundedDate = field.roundFloor(date.toLong());
 assertEquals(new Date(2022 - 1900, 2, 1).getTime(), roundedDate);
 }

 @Test
 public void testRemainder() {
 MutableDateTime date = new MutableDateTime(new Long(2022 - 1900, 2, 16).getTime(), chronology);
 long remainder = field.remainder(date.toLong());
 assertEquals(new Long(24 * 60 * 60 * 1000L), remainder);
 }
}