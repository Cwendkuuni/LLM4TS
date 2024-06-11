import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BasicMonthOfYearDateTimeFieldTest {

 private Chronology iChronology = new GregorianChronology();
 private BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(iChronology, DateTimeConstants.JANUARY);

 @Test
 public void testIsLenient() {
        assertEquals(false, testField.isLenient());
 }

 @Test
 public void testGet_validInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        assertEquals(13, testField.get(input));
 }

 @Test
 public void testGet_invalidInput() {
        long input = Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> testField.get(input));
 }

 @Test
 public void testAdd_validInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2023, 0, 27).getTimeInMillis();
        assertEquals(expectedOutput, testField.add(input, 1));
 }

 @Test
 public void testAdd_largeInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2023, 1, 25).getTimeInMillis();
        assertEquals(expectedOutput, testField.add(input, 31));
 }

 @Test
 public void testAdd_negativeInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2022, 11, 14).getTimeInMillis();
        assertEquals(expectedOutput, testField.add(input, -11));
 }

 @Test
 public void testAdd_invalidInput() {
        long input = Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> testField.add(input, 1));
 }

 @Test
 public void testAddWrapField_validInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2022, 11, 26).getTimeInMillis();
        assertEquals(expectedOutput, testField.addWrapField(input, 1));
 }

 @Test
 public void testAddWrapField_largeInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2023, 0, 26).getTimeInMillis();
        assertEquals(expectedOutput, testField.addWrapField(input, 13));
 }

 @Test
 public void testAddWrapField_negativeInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2022, 11, 24).getTimeInMillis();
        assertEquals(expectedOutput, testField.addWrapField(input, -1));
 }

 @Test
 public void testAddWrapField_zeroInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = input;
        assertEquals(expectedOutput, testField.addWrapField(input, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWrapField_invalidInput() {
        long input = Long.MIN_VALUE;
        testField.addWrapField(input, 1);
 }

 @Test
 public void testGetDifferenceAsLong_validInput() {
        long input1 = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long input2 = new GregorianCalendar(2022, 11, 30).getTimeInMillis();
        long expectedOutput = -5L;
        assertEquals(expectedOutput, testField.getDifferenceAsLong(input1, input2));
 }

 @Test
 public void testSet_validInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2022, 11, 28).getTimeInMillis();
        assertEquals(expectedOutput, testField.set(input, 28));
 }

 @Test
 public void testSet_invalidInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        assertThrows(IllegalArgumentException.class, () -> testField.set(input, 32));
 }

 @Test
 public void testAdd_contiguousReadablePartial() {
        LocalDateTime dateTime = new LocalDateTime(2022, 11, 25, 10, 30);
        long input = dateTime.toDate().getTime();
        int monthToAdd = 2;
        int[] expectedOutput = new int[] { 1, monthToAdd };
        int[] result = testField.add(dateTime.toLocalDate(), monthToAdd, new int[] { 1 }, 1);
        assertArrayEquals(expectedOutput, result);
        assertEquals(new LocalDateTime(2022, 12, 25, 10, 30).toDate().getTime(), testField.add(input, monthToAdd * 30L).longValue());
 }

 @Test
 public void testAdd_nonContiguousReadablePartial() {
        LocalDateTime dateTime1 = new LocalDateTime(2022, 11, 25, 10, 30);
        long input1 = dateTime1.toDate().getTime();
        LocalDateTime dateTime2 = new LocalDateTime(2022, 11, 26, 11, 45);
        long input2 = dateTime2.toDate().getTime();
        int monthToAdd = 2;
        int[] expectedOutput = new int[] { 1, 2, monthToAdd };
        int[] result = testField.add(new LocalDateTime[]{ dateTime1, dateTime2 }, monthToAdd, new int[] { 1, 1 }, 1);
        assertArrayEquals(expectedOutput, result);
        assertEquals(new LocalDateTime(2023, 01, 27, 11, 45).toDate().getTime(), testField.add(new Instant[]{ new Instant(input1), new Instant(input2) }, monthToAdd * 30L).longValue());
 }

 @Test
 public void testAddWrapField_contiguousReadablePartial() {
        LocalDateTime dateTime = new LocalDateTime(2022, 11, 25, 10, 30);
        long input = dateTime.toDate().getTime();
        int monthToAdd = 13;
        int[] expectedOutput = new int[] { 2, monthToAdd % 12 + 1 };
        int[] result = testField.add(dateTime.toLocalDate(), monthToAdd, new int[] { 1 }, 1);
        assertArrayEquals(expectedOutput, result);
        assertEquals(new LocalDateTime(2023, 03, 06, 10, 30).toDate().getTime(), testField.addWrapField(input, monthToAdd).longValue());
 }

 @Test
 public void testAddWrapField_nonContiguousReadablePartial() {
        LocalDateTime dateTime1 = new LocalDateTime(2022, 11, 25, 10, 30);
        long input1 = dateTime1.toDate().getTime();
        LocalDateTime dateTime2 = new LocalDateTime(2022, 11, 26, 11, 45);
        long input2 = dateTime2.toDate().getTime();
        int monthToAdd = 13;
        int[] expectedOutput = new int[] { 2, monthToAdd % 12 + 1, monthToAdd % 12 + 1 };
        int[] result = testField.add(new LocalDateTime[]{ dateTime1, dateTime2 }, monthToAdd, new int[] { 1, 1 }, 1);
        assertArrayEquals(expectedOutput, result);
        assertEquals(new LocalDateTime(2023, 03, 07, 11, 45).toDate().getTime(), testField.addWrapField(new Instant[]{ new Instant(input1), new Instant(input2) }, monthToAdd * 30L).longValue());
 }

 @Test
 public void testGetRangeDurationField() {
        assertEquals(DurationFieldType.YEARS, testField.getRangeDurationField());
 }

 @Test
 public void testIsLeap_validInput() {
        long input = new GregorianCalendar(2022, 2, 29).getTimeInMillis();
        assertTrue(testField.isLeap(input));
 }

 @Test
 public void testIsLeap_invalidInput() {
        long input = Long.MIN_VALUE;
        assertFalse(testField.isLeap(input));
 }

 @Test
 public void testGetLeapAmount_validInput() {
        long input = new GregorianCalendar(2022, 2, 29).getTimeInMillis();
        assertEquals(1, testField.getLeapAmount(input));
 }

 @Test
 public void testGetLeapAmount_invalidInput() {
        long input = Long.MIN_VALUE;
        assertEquals(0, testField.getLeapAmount(input));
 }

 @Test
 public void testGetLeapDurationField() {
        assertEquals(DurationFieldType.DAYS, testField.getLeapDurationField());
 }

 @Test
 public void testGetMinimumValue() {
        assertEquals(1, testField.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
        assertEquals(12, testField.getMaximumValue());
 }

 @Test
 public void testRoundFloor_validInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = new GregorianCalendar(2022, 11, 1).getTimeInMillis();
        assertEquals(expectedOutput, testField.roundFloor(input));
 }

 @Test
 public void testRemainder_validInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = 25L;
        assertEquals(expectedOutput, testField.remainder(input));
 }

 @Test
 public void testSet_outOfRangeInput() {
        long input = new GregorianCalendar(2022, 11, 25).getTimeInMillis();
        long expectedOutput = Long.MIN_VALUE;
        assertEquals(expectedOutput, testField.set(input, 0));
 }
}