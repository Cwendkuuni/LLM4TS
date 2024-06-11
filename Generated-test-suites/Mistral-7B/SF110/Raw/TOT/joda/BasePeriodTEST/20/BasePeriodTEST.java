import org.joda.time.BasePeriod;
import org.joda.time.BaseLocal;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodCreationFromValues() {
 PeriodType periodType = PeriodType.standard();
 int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
 BasePeriod period = new BasePeriod(values, periodType);
 assertEquals(periodType, period.getPeriodType());

 for (int i = 0; i < values.length; i++) {
 assertEquals(values[i], period.getValue(i));
 }
 }

 @Test
 public void testBasePeriodCreationFromInstant() {
 PeriodType periodType = PeriodType.standard();
 long instantMillis = System.currentTimeMillis();
 BasePeriod period = new BasePeriod(instantMillis, periodType);
 assertEquals(periodType, period.getPeriodType());

 long testValue = instantMillis + 1000L;
 BasePeriod anotherPeriod = new BasePeriod(testValue, periodType);
 assertEquals(anotherPeriod.getValues()[0], 1);
 }

 @Test
 public void testBasePeriodCreationFromReadableInstant() {
 PeriodType periodType = PeriodType.standard();
 LocalDateTime readableInstant = new LocalDateTime();
 BasePeriod period = new BasePeriod(readableInstant, readableInstant.plusDays(1), periodType);
 assertEquals(periodType, period.getPeriodType());

 long testValue = readableInstant.getMillis() + 1000L;
 LocalDateTime anotherReadableInstant = new LocalDateTime(testValue);
 BasePeriod anotherPeriod = new BasePeriod(readableInstant, anotherReadableInstant, periodType);
 assertEquals(anotherPeriod.getValues()[0], 1);
 }

 @Test
 public void testBasePeriodCreationFromReadablePartial() {
 PeriodType periodType = PeriodType.standard();
 LocalDate readablePartial = new LocalDate();
 BaseLocal baseLocal = new BaseLocal(readablePartial, null);
 BasePeriod period = new BasePeriod(baseLocal, baseLocal.plusDays(1), periodType);
 assertEquals(periodType, period.getPeriodType());

 for (int i = 0; i < baseLocal.size(); i++) {
 assertEquals(baseLocal.getValue(i), period.getValue(i));
 }
 }

 @Test
 public void testBasePeriodCreationFromReadableDurationAndReadableInstant() {
 PeriodType periodType = PeriodType.standard();
 LocalDateTime readableInstant = new LocalDateTime();
 Duration readableDuration = Duration.standardDays(1);
 BasePeriod period = new BasePeriod(readableDuration, readableInstant, periodType);
 assertEquals(periodType, period.getPeriodType());

 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodCreationFromReadableDurationAndReadableInstantWithNegativeDuration() {
 PeriodType periodType = PeriodType.standard();
 LocalDateTime readableInstant = new LocalDateTime();
 Duration readableDuration = Duration.standardDays(-1);
 BasePeriod period = new BasePeriod(readableDuration, readableInstant, periodType);
 assertEquals(periodType, period.getPeriodType());

 assertEquals(-1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodCreationFromObject() {
 PeriodType periodType = PeriodType.standard();
 Duration testDuration = Duration.standardDays(1);
 BasePeriod period = new BasePeriod(testDuration, periodType, null);
 assertEquals(periodType, period.getPeriodType());

 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodToDurationFromAndTo() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, periodType);

 Duration durationFrom = period.toDurationFrom(new LocalDateTime());
 Duration durationTo = period.toDurationTo(new LocalDateTime());

 assertEquals(durationFrom, durationTo);
 }

 @Test
 public void testBasePeriodSetValue() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, periodType);
 period.setValue(DurationFieldType.days(), 10);

 assertEquals(10, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodSetValues() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, periodType);
 period.setValues(new int[] {10, 20, 30, 40, 50, 60, 70, 80});

 for (int i = 0; i < period.size(); i++) {
 assertEquals(period.getValue(i), i + 10);
 }
 }

 @Test
 public void testBasePeriodSetPeriod() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period1 = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, periodType);
 BasePeriod period2 = new BasePeriod(new int[] {11, 12, 13, 14, 15, 16, 17, 18}, periodType);
 period1.setPeriod(period2);

 for (int i = 0; i < period1.size(); i++) {
 assertEquals(period2.getValue(i), period1.getValue(i));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodInvalidSetPeriod() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period1 = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, periodType);
 BasePeriod period2 = null;
 period1.setPeriod(period2);
 }

 @Test
 public void testBasePeriodCheckPeriodType() {
 PeriodType periodType1 = PeriodType.standard();
 PeriodType periodType2 = PeriodType.micro();
 BasePeriod period = new BasePeriod(new int[8], periodType1);
 assertEquals(periodType1, period.getPeriodType());
 period.setPeriodType(periodType2);
 assertEquals(periodType2, period.getPeriodType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodInvalidSetPeriodType() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period = new BasePeriod(new int[8], periodType);
 period.setPeriodType(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodInvalidMergePeriod() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period1 = new BasePeriod(new int[8], periodType);
 BasePeriod period2 = null;
 period1.mergePeriod(period2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasePeriodInvalidAddPeriod() {
 PeriodType periodType = PeriodType.standard();
 BasePeriod period1 = new BasePeriod(new int[8], periodType);
 BasePeriod period2 = null;
 period1.addPeriod(period2);
 }
}