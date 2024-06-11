import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.UnsupportedDateTimeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISOLATED_GREGORIAN = new GregorianChronology();

 @Test
 public void testBasicConstruction() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(ISOLATED_GREGORIAN, field.getChronology());
 assertEquals(DateTimeConstants.JANUARY, field.getMaxValue());
 assertFalse(field.isLenient());
 }

 @Test
 public void testGetValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Valid values
 assertEquals(DateTimeConstants.JANUARY, field.get(LocalDate.now().toTimeMillis()));
 assertEquals(DateTimeConstants.JANUARY, field.get(BaseDateTime.now().getMillis()));

 // Invalid values
 assertThrows(UnsupportedDateTimeException.class, () -> field.get(Long.MIN_VALUE));
 assertThrows(UnsupportedDateTimeException.class, () -> field.get(Long.MAX_VALUE));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Valid values
 assertEquals(DateTimeConstants.FEBRUARY, field.add(LocalDate.now().toTimeMillis(), 1));
 assertEquals(DateTimeConstants.FEBRUARY, field.add(BaseDateTime.now().getMillis(), 1));

 // Max value
 assertEquals(DateTimeConstants.DECEMBER, field.add(LocalDate.now().toTimeMillis(), field.getMaxValue() - 1));
 assertEquals(DateTimeConstants.DECEMBER, field.add(BaseDateTime.now().getMillis(), field.getMaxValue() - 1));

 // Negative values
 assertEquals(DateTimeConstants.DECEMBER, field.add(LocalDate.now().toTimeMillis(), -1));
 assertEquals(DateTimeConstants.DECEMBER, field.add(BaseDateTime.now().getMillis(), -1));

 // Max value + 1
 assertThrows(IllegalArgumentException.class, () -> field.add(LocalDate.now().toTimeMillis(), field.getMaxValue()));
 assertThrows(IllegalArgumentException.class, () -> field.add(BaseDateTime.now().getMillis(), field.getMaxValue()));
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Valid values
 assertEquals(DateTimeConstants.DECEMBER, field.addWrapField(LocalDate.now().toTimeMillis(), 11));
 assertEquals(DateTimeConstants.DECEMBER, field.addWrapField(BaseDateTime.now().getMillis(), 11));

 // Min value
 assertEquals(DateTimeConstants.JANUARY, field.addWrapField(LocalDate.now().toTimeMillis(), -11));
 assertEquals(DateTimeConstants.JANUARY, field.addWrapField(BaseDateTime.now().getMillis(), -11));

 // Min value - 1
 assertThrows(IllegalArgumentException.class, () -> field.addWrapField(LocalDate.now().toTimeMillis(), -12));
 assertThrows(IllegalArgumentException.class, () -> field.addWrapField(BaseDateTime.now().getMillis(), -12));
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Valid values
 long date1 = LocalDate.now().toTimeMillis();
 long date2 = BaseDateTime.now().getMillis();
 long difference = field.getDifferenceAsLong(date1, date2);
 assertEquals(1, difference);

 // Negative values
 long date3 = date2;
 long date4 = date1;
 difference = field.getDifferenceAsLong(date3, date4);
 assertEquals(-1, difference);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Valid values
 long date1 = LocalDate.now().toTimeMillis();
 long date2 = field.set(date1, 12);
 assertEquals(DateTimeConstants.DECEMBER, field.get(date2));

 // Min value
 date1 = LocalDate.now().toTimeMillis();
 date2 = field.set(date1, 1);
 assertEquals(DateTimeConstants.JANUARY, field.get(date2));

 // Max value
 date1 = LocalDate.now().toTimeMillis();
 date2 = field.set(date1, DateTimeConstants.DECEMBER);
 assertEquals(DateTimeConstants.DECEMBER, field.get(date2));
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);
 DurationField rangeDurationField = field.getRangeDurationField();
 assertEquals(DurationFieldType.YEARS, rangeDurationField.getFieldType());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Non-leap year
 assertFalse(field.isLeap(LocalDate.of(2022, 1, 1).toTimeMillis()));

 // Leap year (February)
 assertFalse(field.isLeap(LocalDate.of(2024, 1, 1).toTimeMillis()));

 // Leap year (February, leap month)
 assertTrue(field.isLeap(LocalDate.of(2024, 2, 1).toTimeMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);

 // Non-leap year
 assertEquals(0, field.getLeapAmount(LocalDate.of(2022, 1, 1).toTimeMillis()));

 // Leap year (February)
 assertEquals(0, field.getLeapAmount(LocalDate.of(2024, 1, 1).toTimeMillis()));

 // Leap year (February, leap month)
 assertEquals(1, field.getLeapAmount(LocalDate.of(2024, 2, 1).toTimeMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOLATED_GREGORIAN, DateTimeConstants.JANUARY);
 DurationField leapDurationField = field.getLeapDurationField();
 assertEquals(DurationFieldType.DAYS, leapDurationField.getFieldType());
 }
}