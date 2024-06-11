import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyException;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.TestChronology;
import org.joda.time.chrono.ISONewYearChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final int TEST_YEAR = 2022;
 private static final int TEST_MONTH = 2;
 private static final int TEST_MONTH_LENGTH = 28;
 private static final int TEST_MAX_MONTH = 12;
 private static final int TEST_LEAP_MONTH = 2;
 private static final int TEST_ADD_AMOUNT = 5;
 private static final int TEST_ADD_LONG = 120;
 private static final int[] TEST_ADD_ARRAY = {1, 2, 3, 4, 5};
 private static final int TEST_ADD_WRAPPED_VALUE = 15;
 private static final int TEST_DIFFERENCE_VALUE = 3;
 private static final long TEST_SET_VALUE = 1600000000000L;
 private static final int TEST_MAX_VALUE = TEST_MAX_MONTH;
 private static final int TEST_MIN_VALUE = 1;

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 int expected = TEST_MONTH;
 int result = field.get(testDate);
 assertEquals(expected, result);
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long expected = 1640995200000L; // future date in the same year
 long result = field.add(testDate, TEST_ADD_AMOUNT);
 assertEquals(expected, result);
 }

 @Test
 public void testAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long expected = 1641051600000L; // future date in the same year
 long result = field.add(testDate, TEST_ADD_LONG);
 assertEquals(expected, result);
 }

 @Test
 public void testAddArray() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISONewYearChronology.getInstance(), TEST_LEAP_MONTH);
 LocalDateTime testDate = new LocalDateTime(TEST_YEAR, TEST_MONTH, TEST_MONTH_LENGTH, 0, 0, 0);
 LocalDateTime[] expectedDates = new LocalDateTime[]{
 new LocalDateTime(TEST_YEAR, TEST_MONTH + 1, TEST_MONTH_LENGTH, 0, 0, 0),
 new LocalDateTime(TEST_YEAR + 1, TEST_MONTH, TEST_MONTH_LENGTH, 0, 0, 0),
 new LocalDateTime(TEST_YEAR + 1, TEST_MONTH + 1, TEST_MONTH_LENGTH, 0, 0, 0),
 new LocalDateTime(TEST_YEAR + 1, TEST_MONTH + 2, TEST_MONTH_LENGTH, 0, 0, 0),
 new LocalDateTime(TEST_YEAR + 1, TEST_MONTH + 3, TEST_MONTH_LENGTH, 0, 0, 0)
 };
 LocalDateTime[] resultDates = new LocalDateTime[TEST_ADD_ARRAY.length];
 field.add(new LocalDateTime(TEST_YEAR, TEST_MONTH, TEST_MONTH_LENGTH, 0, 0, 0), TEST_ADD_ARRAY, resultDates, 0);
 for (int i = 0; i < TEST_ADD_ARRAY.length; i++) {
 assertEquals(expectedDates[i].getYear(), resultDates[i].getYear());
 assertEquals(expectedDates[i].getMonthOfYear(), resultDates[i].getMonthOfYear());
 }
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long expected = 1672492800000L; // future date in the same year
 long result = field.addWrapField(testDate, TEST_ADD_WRAPPED_VALUE);
 assertEquals(expected, result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate1 = Instant.now().getMillis();
 long testDate2 = testDate1 + 3 * 24 * 60 * 60 * 1000; // future date in the same year
 long expected = 2;
 long result = field.getDifferenceAsLong(testDate1, testDate2);
 assertEquals(expected, result);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 int targetMonth = TEST_MONTH + TEST_ADD_AMOUNT;
 long expected = 1641232400000L; // future date in the same year
 long result = field.set(testDate, targetMonth);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidArgument() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long result = field.add(testDate, TEST_MAX_VALUE + 1);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLongInvalidArgument() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long result = field.add(testDate, Long.MAX_VALUE);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 DurationFieldType expected = DurationFieldType.years();
 DurationField result = field.getRangeDurationField();
 assertEquals(expected, result.getClass());
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 boolean expected = true;
 boolean result = field.isLeap(testDate);
 assertEquals(expected, result);
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 int expected = 1;
 int result = field.getLeapAmount(testDate);
 assertEquals(expected, result);
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 DurationFieldType expected = DurationFieldType.days();
 DurationField result = field.getLeapDurationField();
 assertEquals(expected, result.getClass());
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 int expected = TEST_MIN_VALUE;
 int result = field.getMinimumValue();
 assertEquals(expected, result);
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 int expected = TEST_MAX_VALUE;
 int result = field.getMaximumValue();
 assertEquals(expected, result);
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long expected = testDate - (testDate % (TestChronology.getInstanceUTC().getMillisPerDay() / TEST_MAX_VALUE));
 long result = field.roundFloor(testDate);
 assertEquals(expected, result);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 long testDate = Instant.now().getMillis();
 long expected = testDate % (TestChronology.getInstanceUTC().getMillisPerDay() / TEST_MAX_VALUE);
 long result = field.remainder(testDate);
 assertEquals(expected, result);
 }

 @Test
 public void testReadResolve() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TestChronology.getInstanceUTC(), TEST_LEAP_MONTH);
 Object resolved = field.readResolve();
 assertTrue(resolved instanceof MonthOfYearDateTimeField);
 }
}