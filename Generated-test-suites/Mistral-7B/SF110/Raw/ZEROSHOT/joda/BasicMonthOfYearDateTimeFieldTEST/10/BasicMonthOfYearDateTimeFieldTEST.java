import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyException;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;

 @Test
 public void testBasic() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertFalse(field.isLenient());
 assertEquals(1, field.getMinimumValue());
 assertEquals(12, field.getMaximumValue());

 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(1, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();

 assertEquals(1, field.get(originalMillis));

 date.setField(field, 2);
 assertEquals(2, field.get(date.getMillis()));
 assertEquals(2012, date.getYear());
 assertEquals(28, date.getDayOfMonth());

 date.setField(field, -1);
 assertEquals(1, field.get(date.getMillis()));

 date.setField(field, 13);
 assertEquals(1, field.get(date.getMillis()));
 assertEquals(2012, date.getYear());
 assertEquals(1, date.getDayOfMonth());

 date.setField(field, 13 + 12);
 assertEquals(1, field.get(date.getMillis()));
 assertEquals(2013, date.getYear());
 assertEquals(1, date.getDayOfMonth());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOverflow() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(1);
 date.setMonthOfYear(1, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();

 long add = Long.MAX_VALUE + 1;
 assertEquals(originalMillis, field.add(originalMillis, add));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNegative() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(1, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();

 long add = -1;
 try {
 field.add(originalMillis, add);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testAddWrap() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(1);
 date.setMonthOfYear(12, 31);
 date.setDayOfMonth(31);
 Long originalMillis = date.getMillis();

 long add = 1;
 assertEquals(originalMillis, field.addWrapField(originalMillis, add));
 assertEquals(originalMillis, field.add(originalMillis, add));

 long add2 = Long.MAX_VALUE + 1;
 assertEquals(originalMillis, field.addWrapField(originalMillis, add2));
 assertEquals(originalMillis, field.add(originalMillis, add2));

 long add3 = -1;
 assertEquals(originalMillis, field.addWrapField(originalMillis, add3));
 assertEquals(originalMillis, field.add(originalMillis, add3));
 }

 @Test
 public void testGetDifference() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date1 = new MutableDateTime();
 date1.setYear(2012);
 date1.setMonthOfYear(1, 31);
 date1.setDayOfMonth(1);
 Long originalMillis1 = date1.getMillis();
 MutableDateTime date2 = new MutableDateTime();
 date2.setYear(2012);
 date2.setMonthOfYear(2, 28);
 date2.setDayOfMonth(28);
 Long originalMillis2 = date2.getMillis();

 long difference = field.getDifferenceAsLong(originalMillis1, originalMillis2);
 assertEquals(1, difference);
 }

 @Test
 public void testSet() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(1, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();

 int newMonth = 2;
 assertEquals(originalMillis, field.set(originalMillis, newMonth));
 assertEquals(2012, date.getYear());
 assertEquals(28, date.getDayOfMonth());

 newMonth = 13;
 assertEquals(originalMillis, field.set(originalMillis, newMonth));
 assertEquals(2012, date.getYear());
 assertEquals(1, date.getDayOfMonth());

 newMonth = 13 + 12;
 assertEquals(originalMillis, field.set(originalMillis, newMonth));
 assertEquals(2013, date.getYear());
 assertEquals(1, date.getDayOfMonth());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOutOfBounds() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(1, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();

 int newMonth = 0;
 try {
 field.set(originalMillis, newMonth);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 newMonth = 13 + 1;
 try {
 field.set(originalMillis, newMonth);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = ChronologyException.class)
 public void testLeapYear() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.FEBRUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(2, 29);
 date.setDayOfMonth(29);
 Long originalMillis = date.getMillis();

 assertEquals(1, field.getLeapAmount(originalMillis));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLeapYearInvalidMonth() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();

 try {
 field.getLeapAmount(originalMillis);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testGetRangeDurationField() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 DurationField durationField = field.getRangeDurationField();
 assertEquals(DurationFieldType.YEARS, durationField.getFieldType());
 }

 @Test
 public void testIsLeap() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.FEBRUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(2, 29);
 date.setDayOfMonth(29);
 Long originalMillis = date.getMillis();
 assertTrue(field.isLeap(originalMillis));
 }

 @Test
 public void testIsLeapInvalidYear() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(1, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();
 assertFalse(field.isLeap(originalMillis));
 }

 @Test
 public void testIsLeapInvalidMonth() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 MutableDateTime date = new MutableDateTime();
 date.setYear(2012);
 date.setMonthOfYear(3, 31);
 date.setDayOfMonth(1);
 Long originalMillis = date.getMillis();
 assertFalse(field.isLeap(originalMillis));
 }

 @Test
 public void testAddReadablePartial() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2012, 1, 1, 12, 0);
 int[] values = { 1, 2, 3 };
 int n2 = 5;
 int[] result = new int[values.length];

 int[] expected = new int[] { 3, 4, 5 };
 assertEquals(expected, field.add(date.toMutableDateTime(), values, result, n2));
 }

 @Test
 public void testAddReadablePartialInvalidFieldType() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2012, 1, 1, 12, 0);
 int[] values = { -1, DateTimeFieldType.minuteOfHour() };
 int n2 = 5;
 int[] result = new int[values.length];

 try {
 field.add(date.toMutableDateTime(), values, result, n2);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddReadablePartialInvalidSize() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2012, 1, 1, 12, 0);
 int[] values = { 1 };
 int n2 = 0;
 int[] result = new int[values.length];

 try {
 field.add(date.toMutableDateTime(), values, result, n2);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddReadablePartialInvalidAmount() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2012, 1, 1, 12, 0);
 int[] values = { DateTimeFieldType.MONTH_OF_YEAR() };
 int n2 = 0;
 int[] result = new int[values.length];

 try {
 field.add(date.toMutableDateTime(), values, result, n2);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddReadablePartialInvalidAmountNegative() {
 field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2012, 1, 1, 12, 0);
 int[] values = { DateTimeFieldType.MONTH_OF_YEAR() };
 int n2 = -1;
 int[] result = new int[values.length];

 try {
 field.add(date.toMutableDateTime(), values, result, n2);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
}