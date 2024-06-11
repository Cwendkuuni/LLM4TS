import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.BasicField;
import org.joda.time.field.DateTimeFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology TEST_CHRONOLOGY = new GregorianChronology();;

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 2, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 int expectedMonth = 2;
 int actualMonth = field.get(testMillis);
 assertEquals(expectedMonth, actualMonth);
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 2, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 int addAmount = 3;
 long expectedMillis = LocalDateTime.of(2022, 5, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long actualMillis = field.add(testMillis, addAmount);
 assertEquals(expectedMillis, actualMillis);
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 12, 31, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 int addAmount = 14;
 long expectedMillis = LocalDateTime.of(2023, 1, 14, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long actualMillis = field.addWrapField(testMillis, addAmount);
 assertEquals(expectedMillis, actualMillis);
 }

 @Test
 public void testAddReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 ReadablePartial readablePartial = new DateTimeFields(DateTimeFieldType.dayOfMonth(), 30, DateTimeFieldType.monthOfYear(), 2);
 int addAmount = 2;
 int[] expectedArray = new int[]{31, 4};
 int[] actualArray = field.add(readablePartial, addAmount, new int[2], 0);
 assertArrayEquals(expectedArray, actualArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 2, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 int addAmount = 366;
 field.add(testMillis, addAmount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNegativeAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 2, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 int addAmount = -1;
 field.add(testMillis, addAmount);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis1 = LocalDateTime.of(2022, 2, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long testMillis2 = LocalDateTime.of(2022, 3, 31, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long expectedDifference = 3;
 long actualDifference = field.getDifferenceAsLong(testMillis1, testMillis2);
 assertEquals(expectedDifference, actualDifference);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 DurationField expectedDurationField = new Years(1);
 DurationField actualDurationField = field.getRangeDurationField();
 assertEquals(expectedDurationField, actualDurationField);
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 2, 28, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 boolean expectedLeap = false;
 boolean actualLeap = field.isLeap(testMillis);
 assertEquals(expectedLeap, actualLeap);
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 2, 29, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 int expectedLeapAmount = 1;
 int actualLeapAmount = field.getLeapAmount(testMillis);
 assertEquals(expectedLeapAmount, actualLeapAmount);
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 DurationField expectedDurationField = new Days(1);
 DurationField actualDurationField = field.getLeapDurationField();
 assertEquals(expectedDurationField, actualDurationField);
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 int expectedMinimumValue = 1;
 int actualMinimumValue = field.getMinimumValue();
 assertEquals(expectedMinimumValue, actualMinimumValue);
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 int expectedMaximumValue = 12;
 int actualMaximumValue = field.getMaximumValue();
 assertEquals(expectedMaximumValue, actualMaximumValue);
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 3, 1, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long expectedMillis = LocalDateTime.of(2022, 3, 1, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long actualMillis = field.roundFloor(testMillis);
 assertEquals(expectedMillis, actualMillis);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);
 long testMillis = LocalDateTime.of(2022, 3, 2, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long expectedMillis = LocalDateTime.of(2022, 3, 1, 12, 0, 0).toInstant(DateTimeConstants.UTC).getMillis();
 long actualMillis = field.remainder(testMillis);
 assertEquals(expectedMillis, actualMillis);
 }
}