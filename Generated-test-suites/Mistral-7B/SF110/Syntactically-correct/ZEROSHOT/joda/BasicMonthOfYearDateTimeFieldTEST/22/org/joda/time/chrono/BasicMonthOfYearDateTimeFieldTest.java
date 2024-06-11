import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology CHRONOLOGY = new GregorianChronology();

 @Test
 public void testBasicMonthOfYearDateTimeFieldConstruction() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
 assertEquals(CHRONOLOGY, field.getChronology());
 assertEquals(2, field.getLeapMonth());
 }

 @Test
 public void testIsLenientReturnsFalse() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGetReturnsCorrectMonth() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 int month = field.get(date);
 assertEquals(12, month);
 }

 @Test
 public void testAddReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long result = field.add(date, 2);
 DateTime dateTime = new DateTime(result);
 assertEquals(new DateTime(2016, 01, 30).getMillis(), result);
 assertEquals(new DateTime(2016, 01, 30), dateTime);
 }

 @Test
 public void testAddWithLongArgReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long result = field.add(date, 3L);
 DateTime dateTime = new DateTime(result);
 assertEquals(new DateTime(2016, 02, 29).getMillis(), result); // Leap year, February has 29 days
 assertEquals(new DateTime(2016, 02, 29), dateTime);
 }

 @Test
 public void testAddWrapFieldReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long result = field.addWrapField(date, 13);
 DateTime dateTime = new DateTime(result);
 assertEquals(new DateTime(2016, 03, 31).getMillis(), result); // March has 31 days
 assertEquals(new DateTime(2016, 03, 31), dateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithTooLargeArgThrowsIllegalArgumentException() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 try {
 field.add(date, Integer.MAX_VALUE + 1);
 } catch (IllegalArgumentException e) {
 assertEquals("Magnitude of add amount is too large: 2147483648", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testGetDifferenceAsLongReturnsCorrectValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date1 = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long date2 = new GregorianCalendar(2015, 8, 30).getTimeInMillis();
 long difference = field.getDifferenceAsLong(date1, date2);
 assertEquals(5, difference);
 }

 @Test
 public void testSetReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long result = field.set(date, 5);
 DateTime dateTime = new DateTime(result);
 assertEquals(new DateTime(2015, 08, 30).getMillis(), result);
 assertEquals(new DateTime(2015, 08, 30), dateTime);
 }

 @Test
 public void testAddWithReadablePartialReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 ReadablePartial readablePartial = new DateTime(date).toLocalDate().toDateTimeAtStartOfDay().getLocalDate().toBaseDateTime().toReadablePartial();
 int[] result = field.add(readablePartial, 1, new int[3], 0);
 assertEquals(1, result[0]);
 assertEquals(8, result[1]);
 assertEquals(30, result[2]);
 long newDate = new DateTime(CHRONOLOGY, new BaseLocalDate(2015, 8, 30)).getMillis();
 assertEquals(newDate, field.getMillis(result));
 }

 @Test
 public void testAddWithWrappedValueReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 int month = field.get(date) + 13;
 long result = field.addWrapField(date, month);
 DateTime dateTime = new DateTime(result);
 assertEquals(new DateTime(2016, 03, 31).getMillis(), result); // March has 31 days
 assertEquals(new DateTime(2016, 03, 31), dateTime);
 }

 @Test
 public void testRoundFloorReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long result = field.roundFloor(date);
 DateTime dateTime = new DateTime(result);
 assertEquals(new DateTime(2015, 11, 30).getMillis(), result);
 assertEquals(new DateTime(2015, 11, 30), dateTime);
 }

 @Test
 public void testRemainderReturnsCorrectMillis() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 11, 30).getTimeInMillis();
 long result = field.remainder(date);
 assertEquals(0, result);
 }

 @Test
 public void testSetAndGetLeapAmountReturnsCorrectValues() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long date = new GregorianCalendar(2015, 2, 29).getTimeInMillis(); // Leap year, February has 29 days
 int leapAmount = field.getLeapAmount(date);
 assertEquals(1, leapAmount);
 int leapAmount2 = field.getLeapAmount(date - 1);
 assertEquals(0, leapAmount2);
 }
}