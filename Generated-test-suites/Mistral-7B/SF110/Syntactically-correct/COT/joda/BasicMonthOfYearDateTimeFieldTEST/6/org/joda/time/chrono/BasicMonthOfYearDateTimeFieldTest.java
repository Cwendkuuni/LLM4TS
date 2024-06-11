import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableField;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private Chronology chronology;
 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setUp() {
 chronology = new GregorianChronology();
 field = new BasicMonthOfYearDateTimeField(chronology, 2); // February
 }

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet_ValidYearMonth() {
 long date = new GregorianCalendar(2022, 1, 1).getTimeInMillis();
 int month = field.get(date);
 assertEquals(1, month);
 }

 @Test
 public void testGet_ValidYearLeapMonth() {
 long date = new GregorianCalendar(2022, 1, 29).getTimeInMillis(); // Feb 29, 2022
 int month = field.get(date);
 assertEquals(2, month);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidYear() {
 long date = new GregorianCalendar(-1, 1, 1).getTimeInMillis();
 field.get(date);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_NegativeYearMonth() {
 long date = new GregorianCalendar(-1, 0, 1).getTimeInMillis();
 field.get(date);
 }

 @Test
 public void testAdd_ValidAmount() {
 long date = new GregorianCalendar(2022, 1, 1).getTimeInMillis();
 long result = field.add(date, 1);
 assertEquals(2022, Chronology.getInstance(result).getYear());
 assertEquals(2, Chronology.getInstance(result).getMonthOfYear());
 }

 @Test
 public void testAdd_NegativeAmount() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).minusMonths(1).getMillis();
 long result = field.add(date, -1);
 assertEquals(2022, Chronology.getInstance(result).getYear());
 assertEquals(12, Chronology.getInstance(result).getMonthOfYear());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_NegativeAmountBeyondRange() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).minusMonths(12).getMillis();
 field.add(date, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_PositiveAmountBeyondRange() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).plusMonths(12 * 13).getMillis();
 field.add(date, 1);
 }

 @Test
 public void testAddWrapField() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).plusMonths(13).getMillis();
 long result = field.addWrapField(date, -12);
 assertEquals(2022, Chronology.getInstance(result).getYear());
 assertEquals(1, Chronology.getInstance(result).getMonthOfYear());
 }

 @Test
 public void testAdd_Partial() {
 ReadablePartial readablePartial = new LocalDate().withFieldAdded(DateTimeFieldType.year(), 1).toLocalDateTime(LocalTime.MIDNIGHT).toMutableDateTime().toLocalDate().toLocalDateTime().toReadablePartial();
 int[] result = field.add(readablePartial, 2, new int[3], 0);
 assertEquals(2, result[0]);
 assertEquals(3, result[1]);
 }

 @Test
 public void testGetDifferenceAsLong() {
 long date1 = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).getMillis();
 long date2 = new MutableDateTime(new GregorianCalendar(2022, 1, 31).getTime()).getMillis();
 long result = field.getDifferenceAsLong(date1, date2);
 assertEquals(30, result);
 }

 @Test
 public void testSet_ValidYearMonth() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).getMillis();
 long result = field.set(date, 2);
 assertEquals(2022, Chronology.getInstance(result).getYear());
 assertEquals(2, Chronology.getInstance(result).getMonthOfYear());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_InvalidYearMonth() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 1).getTime()).getMillis();
 field.set(date, 13);
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField result = field.getRangeDurationField();
 assertEquals(DateTimeFieldType.years(), result.getFieldType());
 }

 @Test
 public void testIsLeap_ValidYearLeapMonth() {
 long date = new GregorianCalendar(2022, 1, 29).getTimeInMillis(); // Feb 29, 2022
 assertTrue(field.isLeap(date));
 }

 @Test
 public void testIsLeap_ValidYearNonLeapMonth() {
 long date = new GregorianCalendar(2022, 1, 28).getTimeInMillis(); // Feb 28, 2022
 assertFalse(field.isLeap(date));
 }

 @Test
 public void testGetLeapAmount_ValidYearLeapMonth() {
 long date = new GregorianCalendar(2022, 1, 29).getTimeInMillis(); // Feb 29, 2022
 assertEquals(1, field.getLeapAmount(date));
 }

 @Test
 public void testGetLeapAmount_ValidYearNonLeapMonth() {
 long date = new GregorianCalendar(2022, 1, 28).getTimeInMillis(); // Feb 28, 2022
 assertEquals(0, field.getLeapAmount(date));
 }

 @Test
 public void testGetLeapDurationField() {
 DurationField result = field.getLeapDurationField();
 assertEquals(DateTimeFieldType.days(), result.getFieldType());
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
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 15).getTime()).getMillis();
 long result = field.roundFloor(date);
 assertEquals(2022, Chronology.getInstance(result).getYear());
 assertEquals(1, Chronology.getInstance(result).getMonthOfYear());
 }

 @Test
 public void testRemainder() {
 long date = new MutableDateTime(new GregorianCalendar(2022, 1, 16).getTime()).getMillis();
 long result = field.remainder(date);
 assertEquals(0, result);
 }

 @Test
 public void testReadResolve() {
 Object result = field.readResolve();
 assertEquals(DateTimeFieldType.monthOfYear(), result);
 }
}