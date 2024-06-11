package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.DefaultDateTimeConverter;
import org.joda.time.field.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @BeforeClass
 public static void setUp() {
 DateTimeUtils.initCachedChronologyMap();
 }

 @Test
 public void testEmptyPeriod() {
 BasePeriod period = new BasePeriod(0L, PeriodType.standard());
 assertEquals(0, period.getPeriodType().getValues().length);
 assertEquals(0, period.size());
 }

 @Test
 public void testPeriodWithValues() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 assertEquals(8, period.getPeriodType().getValues().length);
 assertEquals(8, period.size());
 assertEquals(1, period.getValue(DurationFieldType.years()));
 assertEquals(2, period.getValue(DurationFieldType.months()));
 // other assertions for other fields
 }

 @Test
 public void testPeriodFromInstant() {
 DateTime dateTime = new DateTime(0L);
 BasePeriod period = new BasePeriod(dateTime, dateTime.plus(Duration.standardDays(1)), PeriodType.standard());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testPeriodFromReadablePartial() {
 LocalDate date1 = new LocalDate();
 LocalDate date2 = date1.plusDays(1);
 BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testPeriodFromInstantAndDuration() {
 DateTime dateTime = new DateTime(0L);
 BasePeriod period = new BasePeriod(dateTime, Duration.standardDays(1), PeriodType.standard());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPeriodFromNullReadablePartial() {
 // This test case should throw an IllegalArgumentException as ReadablePartial objects must not be null
 BasePeriod period = new BasePeriod(null, null, PeriodType.standard());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPeriodFromNonContiguousReadablePartial() {
 LocalDate date1 = new LocalDate();
 LocalDateTime time1 = new LocalDateTime();
 LocalDate date2 = date1.plusDays(1);
 LocalDateTime time2 = time1.plusHours(1);
 BasePeriod period = new BasePeriod(date1, time1, PeriodType.standard());
 try {
 new BasePeriod(date2, time2, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPeriodFromDifferentClassReadablePartial() {
 LocalDate date1 = new LocalDate();
 LocalDateTime time1 = new LocalDateTime();
 BasePeriod period = new BasePeriod(date1, time1, PeriodType.standard());
 try {
 new BasePeriod(new LocalDateTime(), time1, PeriodType.standard());
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPeriodFromDurations() {
 DateTime dateTime1 = new DateTime(0L);
 DateTime dateTime2 = dateTime1.plusDays(1);
 BasePeriod period1 = new BasePeriod(dateTime1, Duration.standardDays(1), PeriodType.standard());
 BasePeriod period2 = new BasePeriod(dateTime2, Duration.standardDays(-1), PeriodType.standard());
 BasePeriod periodSum = period1.plus(period2);
 assertEquals(0, periodSum.size());
 }

 @Test
 public void testPeriodFromReadablePeriod() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
 BasePeriod periodSum = period1.plus(period2);
 assertEquals(17, periodSum.size());
 }

 @Test
 public void testSerializability() throws IOException {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(period);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 BasePeriod deserializedPeriod = (BasePeriod) ois.readObject();
 assertEquals(period, deserializedPeriod);
 }

 @AfterClass
 public static void tearDown() {
 DateTimeUtils.shutdown();
 }
}