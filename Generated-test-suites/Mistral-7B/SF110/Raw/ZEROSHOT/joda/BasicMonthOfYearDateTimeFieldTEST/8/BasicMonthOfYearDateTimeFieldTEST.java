import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private Chronology chronology;
 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setup() {
 chronology = new ISOChronology();
 field = new BasicMonthOfYearDateTimeField(chronology, 2);
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals("Minimum value should be 1", 1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals("Maximum value should match chronology's maximum month", chronology.getMaxMonth(), field.getMaximumValue());
 }

 @Test
 public void testIsLeap() {
 long leapYear = new BaseDateTime(2000, 2, 29, chronology).getMillis();
 long notLeapYear = new BaseDateTime(1999, 2, 28, chronology).getMillis();
 assertTrue("Leap year should be true", field.isLeap(leapYear));
 assertFalse("Non-leap year should be false", field.isLeap(notLeapYear));
 }

 @Test
 public void testGetLeapAmount() {
 assertEquals("Leap amount should be 1 for leap years", 1, field.getLeapAmount(new BaseDateTime(2000, 2, 29, chronology).getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 assertEquals("Leap duration field should be days", DurationFieldType.days(), field.getLeapDurationField());
 }

 @Test
 public void testIsLenient() {
 assertFalse("Field is not lenient", field.isLenient());
 }

 @Test
 public void testGet() {
 long testDate = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 assertEquals("Get method should return month of test date", 3, field.get(testDate));
 }

 @Test
 public void testAdd() {
 long testDate = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 long newDate = field.add(testDate, 2);
 MutableDateTime mutableDate = new MutableDateTime(newDate, chronology);
 assertEquals("Add method should return new date with added month", 5, field.get(mutableDate.toLong()));
 }

 @Test
 public void testAddLong() {
 long testDate = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 long newDate = field.add(testDate, 6);
 MutableDateTime mutableDate = new MutableDateTime(newDate, chronology);
 assertEquals("Add long method should return new date with added month", 6, field.get(mutableDate.toLong()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLongTooLarge() {
 long testDate = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 field.add(testDate, (long) (chronology.getMaxYear() * 12 * 2 + 1));
 }

 @Test
 public void testAddReadablePartial() {
 ReadablePartial testPartial = new MutableDateTime(new BaseDateTime(2000, 2, 31, chronology).getMillis(), null).toReadOnlyDateTime().toLocalDateTime().toDateTimeFields();
 int[] result = field.add(testPartial, 2, new int[3], 0);
 assertEquals("Add method with readable partial as arguments should return new date with added month", 5, result[0]);
 }

 @Test
 public void testAddWrapField() {
 long testDate = new BaseDateTime(2000, 13, 31, chronology).getMillis(); // 13 is an invalid month
 long newDate = field.addWrapField(testDate, 2);
 MutableDateTime mutableDate = new MutableDateTime(newDate, chronology);
 assertEquals("AddWrapField method should return new date with added month, wrapping around", 1 + 12, field.get(mutableDate.toLong()));
 }

 @Test
 public void testGetDifferenceAsLong() {
 long testDate1 = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 long testDate2 = new BaseDateTime(2000, 4, 30, chronology).getMillis();
 long difference = field.getDifferenceAsLong(testDate1, testDate2);
 assertEquals("GetDifferenceAsLong method should return correct month difference", -1, difference);
 }

 @Test
 public void testSet() {
 long testDate = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 long newDate = field.set(testDate, 12);
 MutableDateTime mutableDate = new MutableDateTime(newDate, chronology);
 assertEquals("Set method should return new date with set month", 12, field.get(mutableDate.toLong()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetInvalidValue() {
 long testDate = new BaseDateTime(2000, 3, 31, chronology).getMillis();
 field.set(testDate, -2);
 }
}