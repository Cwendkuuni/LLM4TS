import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateField;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_CHRONOLOGY = DateTimeUtils.ISO_INSTANT_CHRONOLOGY;

 @Test
 public void testValidAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableLocalDateTime initial = new MutableLocalDateTime();
 initial.setChronology(ISO_CHRONOLOGY);
 initial.set(2010, 1, 1, 0, 0, 0);

 MutableLocalDateTime result = new MutableLocalDateTime(initial);
 result.setField(DateTimeFieldType.monthOfYear(), field.get(initial.getMillis()));
 field.add(result.getMillis(), 1);

 assertEquals("Expected the first of February, 2010", new LocalDateTime(2010, 2, 1, 0, 0, 0), result.toLocalDateTime());
 }

 @Test
 public void testValidAddWrapping() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableLocalDateTime initial = new MutableLocalDateTime();
 initial.setChronology(ISO_CHRONOLOGY);
 initial.set(2010, 12, 31, 0, 0, 0);

 MutableLocalDateTime result = new MutableLocalDateTime(initial);
 result.setField(DateTimeFieldType.monthOfYear(), field.get(initial.getMillis()));
 field.addWrapField(result.getMillis(), 13);

 assertEquals("Expected the first of January, 2011", new LocalDateTime(2011, 1, 1, 0, 0, 0), result.toLocalDateTime());
 }

 @Test
 public void testValidSubtract() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableLocalDateTime initial = new MutableLocalDateTime();
 initial.setChronology(ISO_CHRONOLOGY);
 initial.set(2010, 2, 1, 0, 0, 0);

 MutableLocalDateTime result = new MutableLocalDateTime(initial);
 result.setField(DateTimeFieldType.monthOfYear(), field.get(initial.getMillis()));
 field.add(result.getMillis(), -1);

 assertEquals("Expected the last day of January, 2010", new LocalDateTime(2010, 1, DateTimeConstants.LAST_DAY_OF_MONTH, 0, 0, 0), result.toLocalDateTime());
 }

 @Test
 public void testInvalidAddTooLarge() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableLocalDateTime initial = new MutableLocalDateTime();
 initial.setChronology(ISO_CHRONOLOGY);
 initial.set(2010, 12, 31, 0, 0, 0);

 try {
 field.add(initial.getMillis(), Integer.MAX_VALUE);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidAddNegative() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableLocalDateTime initial = new MutableLocalDateTime();
 initial.setChronology(ISO_CHRONOLOGY);
 initial.set(2010, 1, 1, 0, 0, 0);

 try {
 field.add(initial.getMillis(), Integer.MIN_VALUE);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 MutableLocalDateTime initial = new MutableLocalDateTime();
 initial.setChronology(ISO_CHRONOLOGY);
 initial.set(2012, 2, 29, 0, 0, 0); // Leap year

 int leapAmount = field.getLeapAmount(initial.getMillis());
 assertEquals("Expected 1", 1, leapAmount);

 initial.set(2011, 2, 28, 0, 0, 0); // Non-leap year
 leapAmount = field.getLeapAmount(initial.getMillis());
 assertEquals("Expected 0", 0, leapAmount);
 }
}