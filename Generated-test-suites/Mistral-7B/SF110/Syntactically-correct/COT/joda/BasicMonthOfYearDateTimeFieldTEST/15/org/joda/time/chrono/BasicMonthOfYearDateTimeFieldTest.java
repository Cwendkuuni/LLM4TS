import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BasicMonthOfYearDateTimeFieldTest {

 private Chronology chronology = new JulianChronology();
 private BasicMonthOfYearDateTimeField field;

 @Before
 public void setUp() {
 field = new BasicMonthOfYearDateTimeField(chronology, 2);
 }

 @Test
 public void testIsLenient() {
 assertEquals(false, field.isLenient());
 }

 @Test
 public void testGet_valid() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 assertEquals(2, field.get(date));
 }

 @Test
 public void testAdd_valid() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 long result = field.add(date, 1);
 assertEquals(new LocalDateTime(2022, 3, 28, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testAdd_crossYear() {
 long date = new LocalDateTime(2022, 12, 31, 0, 0, 0).getMillis();
 long result = field.add(date, 12);
 assertEquals(new LocalDateTime(2023, 1, 31, 0, 0, 0).getMillis(), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_tooBig() {
 long date = new LocalDateTime(2022, 12, 31, 0, 0, 0).getMillis();
 long result = field.add(date, 365 * 2 + 1);
 }

 @Test
 public void testAddWrapField() {
 long date = new LocalDateTime(2022, 13, 30, 0, 0, 0).getMillis();
 long result = field.addWrapField(date, 1);
 assertEquals(new LocalDateTime(2023, 1, 31, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 long date1 = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 long date2 = new LocalDateTime(2022, 3, 31, 0, 0, 0).getMillis();
 long result = field.getDifferenceAsLong(date1, date2);
 assertEquals(-1, result);
 }

 @Test
 public void testSet_valid() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 long result = field.set(date, 3);
 assertEquals(new LocalDateTime(2022, 3, 28, 0, 0, 0).getMillis(), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_invalid() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 long result = field.set(date, 13);
 }

 @Test
 public void testGetRangeDurationField() {
 DurationField result = field.getRangeDurationField();
 assertEquals(DateTimeFieldType.years(), result.getFieldType());
 }

 @Test
 public void testIsLeap_valid() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 assertTrue(field.isLeap(date));
 }

 @Test
 public void testIsLeap_invalid() {
 long date = new LocalDateTime(2022, 3, 28, 0, 0, 0).getMillis();
 assertFalse(field.isLeap(date));
 }

 @Test
 public void testGetLeapAmount_valid() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();
 assertEquals(0, field.getLeapAmount(date));
 }

 @Test
 public void testGetLeapAmount_leapYear() {
 long date = new LocalDateTime(2022, 2, 28, 0, 0, 0).getMillis();long date2 = new LocalDateTime(2022, 3, 1, 0, 0, 0).getMillis();
 assertEquals(1, field.getLeapAmount(date2));
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
 long date = new LocalDateTime(2022, 3, 1, 0, 0, 0).getMillis();
 long result = field.roundFloor(date);
 assertEquals(new LocalDateTime(2022, 3, 1, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testRemainder() {
 long date = new LocalDateTime(2022, 3, 1, 0, 0, 0).getMillis();
 long result = field.remainder(date);
 assertEquals(0, result);
 }

 @Test
 public void testAdd_nullInput() {
 assertThrows(NullPointerException.class, () -> field.add(null, 1));
 }

 @Test
 public void testAdd_nonLongInput() {
 MutableDateTime mutableDateTime = new MutableDateTime();
 assertThrows(ClassCastException.class, () -> field.add(mutableDateTime, 1));
 }

 @Test
 public void testAdd_negativeInput() {
 long date = new LocalDateTime(2022, 1, 1, 0, 0, 0).getMillis();
 long result = field.add(date, -1);
 assertEquals(new LocalDateTime(2021, 12, 31, 0, 0, 0).getMillis(), result);
 }

 @Test
 public void testSet_nullInput() {
 assertThrows(NullPointerException.class, () -> field.set(null, 1));
 }

 @Test
 public void testSet_nonLongInput() {
 MutableDateTime mutableDateTime = new MutableDateTime();
 assertThrows(ClassCastException.class, () -> field.set(mutableDateTime, 1));
 }
}