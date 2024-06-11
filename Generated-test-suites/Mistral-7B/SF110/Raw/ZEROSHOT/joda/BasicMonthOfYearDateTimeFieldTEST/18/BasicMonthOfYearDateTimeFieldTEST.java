import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology ISO = new ISOChronology();

 @Test
 public void testValidValues() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 for (int i = 1; i <= ISO.getMaximumValue(DateTimeFieldType.monthOfYear()); i++) {
 long value = field.get(i * DateTimeConstants.MILLIS_PER_DAY);
 assertEquals("Unexpected value for month " + i, i, field.get(value));
 }
 }

 @Test
 public void testMinValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long value = field.getMinimumValue() * DateTimeConstants.MILLIS_PER_DAY;
 assertEquals("Unexpected value for minimum value", 1, field.get(value));
 }

 @Test
 public void testMaxValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long value = new BaseDateTime(ISO.getMaxYear(), ISO.getMaxValue(DateTimeFieldType.monthOfYear()), 1, 0, 0, 0, 0).getMillis();
 assertEquals("Unexpected value for maximum value", ISO.getMaxValue(DateTimeFieldType.monthOfYear()), field.get(value));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.FEBRUARY);
 for (int year = 1; year <= ISO.getMaxYear(); year++) {
 if (ISO.isLeapYear(year)) {
 int leapMonth = ISO.getMonthOfYear(new BaseDateTime(year, 2, 29, 0, 0, 0, 0).getMillis());
 int leapAmount = field.getLeapAmount(new BaseDateTime(year, leapMonth, 29, 0, 0, 0, 0).getMillis());
 assertEquals("Unexpected leap amount for year " + year, 1, leapAmount);
 } else {
 int leapAmount = field.getLeapAmount(new BaseDateTime(year, 2, 28, 0, 0, 0, 0).getMillis());
 assertEquals("Unexpected leap amount for year " + year, 0, leapAmount);
 }
 }
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long current = DateTimeConstants.MINUTE_OF_DAY;
 for (int i = 1; i <= 12; i++) {
 long next = field.add(current, 1);
 assertEquals("Unexpected result of add " + i, current + DateTimeConstants.MILLIS_PER_DAY, next);
 current = next;
 }
 for (int i = 1; i <= 12; i++) {
 long next = field.add(current, -1);
 assertEquals("Unexpected result of add " + i, current - DateTimeConstants.MILLIS_PER_DAY, next);
 current = next;
 }
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long current = DateTimeConstants.MINUTE_OF_DAY;
 long next = field.addWrapField(current, 13);
 assertEquals("Unexpected result of addWrapField", current, next);
 next = field.addWrapField(current, -12);
 assertEquals("Unexpected result of addWrapField", current, next);
 }

 @Test
 public void testAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long current = DateTimeConstants.MINUTE_OF_DAY;
 long next = field.add(current, 365 * 2 + 29);
 long expected = new BaseDateTime(current.getYear() + 2, 2, 29, 0, 0, 0, 0).getMillis();
 assertEquals("Unexpected result of add", expected, next);
 }

 @Test
 public void testAddLongWrap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long current = DateTimeConstants.MINUTE_OF_DAY;
 long next = field.add(current, Long.MAX_VALUE + 1);
 long expected = new BaseDateTime(current.getYear() + 1, 1, 1, 0, 0, 0, 0).getMillis();
 assertEquals("Unexpected result of add wrap", expected, next);
 }

 @Test
 public void testAddReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long current = DateTimeConstants.MINUTE_OF_DAY;
 ReadablePartial partial = new DateTimeFieldsMutable();
 partial.add(DateTimeFieldType.monthOfYear(), 1);
 long next = field.add(current, partial);
 long expected = new BaseDateTime(current.getYear(), current.getMonthOfYear() + 1, 1, 0, 0, 0, 0).getMillis();
 assertEquals("Unexpected result of add readable partial", expected, next);
 }

 @Test
 public void testAddReadablePartialWrap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long current = DateTimeConstants.MINUTE_OF_DAY;
 ReadablePartial partial = new DateTimeFieldsMutable();
 partial.add(DateTimeFieldType.monthOfYear(), 12);
 long next = field.add(current, partial);
 long expected = new BaseDateTime(current.getYear() + 1, 1, 1, 0, 0, 0, 0).getMillis();
 assertEquals("Unexpected result of add readable partial wrap", expected, next);
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.FEBRUARY);
 for (int year = 1; year <= ISO.getMaxYear(); year++) {
 if (ISO.isLeapYear(year)) {
 assertTrue("Unexpected result of isLeap for year " + year + " and month " + DateTimeConstants.FEBRUARY, field.isLeap(new BaseDateTime(year, 2, 29, 0, 0, 0, 0).getMillis()));
 } else {
 assertFalse("Unexpected result of isLeap for year " + year + " and month " + DateTimeConstants.FEBRUARY, field.isLeap(new BaseDateTime(year, 2, 28, 0, 0, 0, 0).getMillis()));
 }
 }
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long earlier = DateTimeConstants.MINUTE_OF_DAY;
 long later = DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MINUTE_OF_DAY;
 assertEquals("Unexpected result of getDifferenceAsLong for negative difference", -1, field.getDifferenceAsLong(later, earlier));
 assertEquals("Unexpected result of getDifferenceAsLong for positive difference", 1, field.getDifferenceAsLong(earlier, later));
 }

 @Test
 public void testSetGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long value = DateTimeConstants.MINUTE_OF_DAY;
 int month = field.get(value);
 long set = field.set(value, month + 1);
 assertEquals("Unexpected result of set", value + DateTimeConstants.MILLIS_PER_DAY, set);
 assertNotSame("Unexpected result of get after set", month, field.get(set));
 assertEqual("Unexpected result of get after set", month + 1, field.get(set));
 }

 @Test
 public void testSetOutOfBounds() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long value = new BaseDateTime(2010, ISO.getMaxValue(DateTimeFieldType.monthOfYear()), 1, 0, 0, 0, 0).getMillis();
 try {
 field.set(value, 0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddWrapFieldOutOfBounds() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO, DateTimeConstants.JANUARY);
 long value = new BaseDateTime(2010, ISO.getMaxValue(DateTimeFieldType.monthOfYear()), 1, 0, 0, 0, 0).getMillis();
 try {
 field.addWrapField(value, ISO.getMaxValue(DateTimeFieldType.monthOfYear()) + 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}