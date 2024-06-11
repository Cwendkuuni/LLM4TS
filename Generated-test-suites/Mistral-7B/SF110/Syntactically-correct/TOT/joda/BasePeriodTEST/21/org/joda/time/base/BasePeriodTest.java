package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodConstructionWithValidArguments() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.dayTime());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[period.size()];
 System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {3, 2, 1, 4, 5, 6, 7, 8};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodConstructionWithInvalidArguments() {
 try {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, null);
 fail("Should not be able to create a BasePeriod instance with null PeriodType");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BasePeriod period = new BasePeriod(new Object() {}, PeriodType.dayTime(), null);
 fail("Should not be able to create a BasePeriod instance with null iValues");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBasePeriodCreationFromInstantToInstant() {
 BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.001Z"), DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:01.002Z"), PeriodType.dayTime());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[period.size()];
 System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {-1, 0, 0, 0, 0, 0, 0, 1, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodCreationFromReadableInstantAndReadableDuration() {
 BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.001Z"), new Duration(1, TimeUnit.DAYS), PeriodType.dayTime());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[period.size()];
 System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {-1, 0, 0, 0, 0, 0, 0, 1, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodCreationFromReadableDurationAndReadableInstant() {
 BasePeriod period = new BasePeriod(new Duration(1, TimeUnit.DAYS), DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.001Z"), PeriodType.dayTime());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[period.size()];
 System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {-1, 0, 0, 0, 0, 0, 0, 1, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodCreationFromReadablePartials() {
 BaseLocal localDate1 = new LocalDate(2022, 1, 1);
 BaseLocal localDateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeUtils.UTC);
 BaseLocal localDate2 = new LocalDate(2022, 1, 1, 1, 0, 0, 0, DateTimeUtils.UTC);
 BaseLocal localDateTime2 = new LocalDateTime(2022, 1, 1, 1, 0, 0, 0, DateTimeUtils.UTC);
 BasePeriod period = new BasePeriod(localDateTime1, localDateTime2, PeriodType.dayTime());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = new int[period.size()];
 System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {-1, 0, 0, 0, 0, 0, 0, 1, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodAddition() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
 BasePeriod period2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.dayTime());
 BasePeriod periodSum = period1.plus(period2);
 PeriodType actualPeriodType = periodSum.getPeriodType();
 int[] actualValues = new int[periodSum.size()];
 System.arraycopy(periodSum.getValues(), 0, actualValues, 0, periodSum.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {3, 5, 7, 6, 8, 9, 10, 11, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodMerge() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
 BasePeriod period2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.dayTime());
 BasePeriod periodMerged = period1.merge(period2);
 PeriodType actualPeriodType = periodMerged.getPeriodType();
 int[] actualValues = new int[periodMerged.size()];
 System.arraycopy(periodMerged.getValues(), 0, actualValues, 0, periodMerged.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {3, 5, 7, 7, 8, 11, 10, 12, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodSubtraction() {
 BasePeriod period1 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.dayTime());
 BasePeriod period2 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
 BasePeriod periodDifference = period1.minus(period2);
 PeriodType actualPeriodType = periodDifference.getPeriodType();
 int[] actualValues = new int[periodDifference.size()];
 System.arraycopy(periodDifference.getValues(), 0, actualValues, 0, periodDifference.size());

 PeriodType expectedPeriodType = PeriodType.dayTime();
 int[] expectedValues = {-1, -1, -1, -1, 0, 0, 0, 0, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect period values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodConversionToDuration() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
 Duration duration = period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.001Z"));
 PeriodType actualPeriodType = period.getPeriodType();

 PeriodType expectedPeriodType = PeriodType.dayTime();
 long expectedDurationMillis = 86400011L; // 1 day, 1 hour, 11 minutes, 1 second

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertEquals("Incorrect duration value", expectedDurationMillis, duration.getMillis());
 }

 @Test
 public void testBasePeriodConversionToDurationTo() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
 Duration duration = period.toDurationTo(DateTimeUtils.parseLocalDateTime("2022-01-01T00:00:00.001Z"));
 PeriodType actualPeriodType = period.getPeriodType();

 PeriodType expectedPeriodType = PeriodType.dayTime();
 long expectedDurationMillis = 86400009L; // 1 day, 1 hour, 1 hour, 59 minutes, 59 seconds, 59 milliseconds

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertEquals("Incorrect duration value", expectedDurationMillis, duration.getMillis());
 }
}