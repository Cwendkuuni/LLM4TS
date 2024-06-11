import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_CHRONOLOGY = DateTimeUtils.getISOChronology();

 @Test
 public void testGetMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2022, 1, 1, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int expected = 1;
 int actual = field.get(instant);
 assertEquals("Incorrect month value", expected, actual);
 }

 @Test
 public void testAddMethod_ValidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2022, 1, 1, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int addValue = 2;
 long expected = date.plusMonths(addValue).toInstant().getEpochMilli();
 long actual = field.add(instant, addValue);
 assertEquals("Incorrect result after adding a valid value", expected, actual);
 }

 @Test
 public void testAddMethod_NegativeInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2022, 2, 1, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int addValue = -1;
 long expected = date.minusMonths(addValue).toInstant().getEpochMilli();
 long actual = field.add(instant, addValue);
 assertEquals("Incorrect result after adding a negative value", expected, actual);
 }

 @Test
 public void testAddMethod_OverflowInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2022, 1, 1, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int addValue = Integer.MAX_VALUE + 1;
 assertThrows("Adding an overflow value should throw an IllegalArgumentException", IllegalArgumentException.class, () -> field.add(instant, addValue));
 }

 @Test
 public void testAddMethod_InvalidInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 long instant = Long.MIN_VALUE;
 int addValue = 1;
 long expected = instant;
 long actual = field.add(instant, addValue);
 assertEquals("Incorrect result after adding a valid value to a negative timestamp", expected, actual);
 }

 @Test
 public void testIsLeapMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, DateTimeConstants.FEBRUARY);
 LocalDateTime date = new LocalDateTime(2022, 2, 28, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 boolean expected = DateTimeUtils.isLeapYear(DateTimeUtils.getYear(date));
 boolean actual = field.isLeap(instant);
 assertTrue("Incorrect leap year determination", expected == actual);
 }

 @Test
 public void testGetLeapAmountMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, DateTimeConstants.FEBRUARY);
 LocalDateTime date = new LocalDateTime(2022, 2, 29, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int expected = 1;
 int actual = field.getLeapAmount(instant);
 assertEquals("Incorrect leap amount determination", expected, actual);
 }

 @Test
 public void testGetLeapDurationFieldMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(GregorianChronology.INSTANCE, DateTimeConstants.FEBRUARY);
 assertEquals("Incorrect leap duration field", Days.ONE, field.getLeapDurationField());
 }

 @Test
 public void testSetMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2022, 1, 1, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int setValue = 3;
 long expected = date.plusMonths(setValue).toInstant().getEpochMilli();
 long actual = field.set(instant, setValue);
 assertEquals("Incorrect result after setting a new month value", expected, actual);
 }

 @Test
 public void testSetMethod_OutOfRangeInput() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 LocalDateTime date = new LocalDateTime(2022, 1, 1, 12, 0);
 long instant = date.toInstant().getEpochMilli();
 int setValue = 13;
 assertThrows("Setting an out of range month value should throw an IllegalArgumentException", IllegalArgumentException.class, () -> field.set(instant, setValue));
 }

 @Test
 public void testGetMinimumValueMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 int expected = 1;
 int actual = field.getMinimumValue();
 assertEquals("Incorrect minimum month value", expected, actual);
 }

 @Test
 public void testGetMaximumValueMethod() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 int expected = ISO_CHRONOLOGY.getMaxMonth();
 int actual = field.getMaximumValue();
 assertEquals("Incorrect maximum month value", expected, actual);
 }
}