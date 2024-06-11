import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicMonthOfYearDateTimeField TEST_FIELD = new BasicMonthOfYearDateTimeField(JulianChronology.INSTANCE, DateTimeConstants.JANUARY);

 @Test
 public void testIsLenient() {
 assertFalse(TEST_FIELD.isLenient());
 }

 @Test
 public void testGet() {
 // Test valid get
 LocalDateTime testDateTime = new LocalDateTime(JulianChronology.INSTANCE, new LocalDate(2022, 11, 30), new LocalTime(12, 0));
 assertEquals(11, TEST_FIELD.get(testDateTime.getMillis()));

 // Test invalid get
 Long invalidMillis = Long.MIN_VALUE;
 try {
 TEST_FIELD.get(invalidMillis);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testAdd() {
 // Test valid add with single value
 long testMillis = LocalDateTime.parse("2022-11-30T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 LocalDateTime testDateTime = new LocalDateTime(JulianChronology.INSTANCE, new LocalDate(2022, 11, 30), new LocalTime(12, 0));
 long newMillis = TEST_FIELD.add(testMillis, 1);
 assertEquals(LocalDateTime.parse("2022-12-01T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis(), newMillis);

 // Test valid add with long value
 LocalDateTime testDateTime2 = new LocalDateTime(JulianChronology.INSTANCE, new LocalDate(2022, 5, 30), new LocalTime(12, 0));
 long testLongValue = 2;
 long newMillis2 = TEST_FIELD.add(testDateTime2.getMillis(), testLongValue);
 assertEquals(LocalDateTime.parse("2023-06-01T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis(), newMillis2);

 // Test invalid add with negative value
 long testMillis3 = LocalDateTime.parse("2022-12-31T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 try {
 TEST_FIELD.add(testMillis3, -1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testAddWrapField() {
 // Test valid addWrapField with single value
 long testMillis4 = LocalDateTime.parse("2022-11-30T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 long newMillis3 = TEST_FIELD.addWrapField(testMillis4, 13);
 assertEquals(LocalDateTime.parse("2023-01-01T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis(), newMillis3);

 // Test valid addWrapField with long value
 long testMillis5 = LocalDateTime.parse("2022-11-30T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 long newMillis4 = TEST_FIELD.addWrapField(testMillis5, 13L);
 assertEquals(LocalDateTime.parse("2023-01-01T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis(), newMillis4);
 }

 @Test
 public void testAddWrapFieldWithMaxValue() {
 // Test addWrapField with max value
 long testMillis6 = LocalDateTime.parse("1970-01-01T00:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 long newMillis5 = TEST_FIELD.addWrapField(testMillis6, TEST_FIELD.getMaximumValue());
 assertEquals(LocalDateTime.parse("1970-02-01T00:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis(), newMillis5);
 }

 @Test
 public void testGetDifferenceAsLong() {
 // Test valid getDifferenceAsLong
 long testMillis7 = LocalDateTime.parse("2022-11-30T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 long testMillis8 = LocalDateTime.parse("2022-12-01T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis();
 long difference = TEST_FIELD.getDifferenceAsLong(testMillis8, testMillis7);
 assertEquals(1, difference);
 }

 @Test
 public void testSet() {
 // Test valid set
 MutableDateTime mutableDateTime = new MutableDateTime(JulianChronology.INSTANCE, 2022, 11, 30, 12, 0, 0, 0);
 TEST_FIELD.set(mutableDateTime.getMillis(), 12);
 assertEquals(12, TEST_FIELD.get(mutableDateTime.getMillis()));
 }

 @Test
 public void testGetRangeDurationField() {
 assertEquals(DurationFieldType.years(), TEST_FIELD.getRangeDurationField());
 }

 @Test
 public void testIsLeap() {
 // Test valid isLeap
 Instant leapDate = new Instant(JulianChronology.INSTANCE.getMillis(2022, 2, 29));
 assertTrue(TEST_FIELD.isLeap(leapDate.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 // Test valid getLeapAmount
 Instant leapDate = new Instant(JulianChronology.INSTANCE.getMillis(2022, 2, 29));
 assertEquals(1, TEST_FIELD.getLeapAmount(leapDate.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 assertEquals(DurationFieldType.days(), TEST_FIELD.getLeapDurationField());
 }

 @Test
 public void testGetMinimumValue() {
 assertEquals(1, TEST_FIELD.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 assertEquals(TEST_FIELD.iMax, TEST_FIELD.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 // Test valid roundFloor
 LocalDateTime testDateTime3 = new LocalDateTime(JulianChronology.INSTANCE, new LocalDate(2022, 11, 30), new LocalTime(12, 0, 30, 0));
 long roundFloor = TEST_FIELD.roundFloor(testDateTime3.getMillis());
 assertEquals(LocalDateTime.parse("2022-11-30T12:00:00.000Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).getMillis(), roundFloor);
 }

 @Test
 public void testRemainder() {
 // Test valid remainder
 LocalDateTime testDateTime4 = new LocalDateTime(JulianChronology.INSTANCE, new LocalDate(2022, 11, 30), new LocalTime(12, 0, 30, 0));
 long remainder = TEST_FIELD.remainder(testDateTime4.getMillis());
 assertEquals(0, remainder);
 }

 @Test
 public void testReadResolve() {
 assertSame(JulianChronology.INSTANCE.monthOfYear(), TEST_FIELD.readResolve());
 }
}