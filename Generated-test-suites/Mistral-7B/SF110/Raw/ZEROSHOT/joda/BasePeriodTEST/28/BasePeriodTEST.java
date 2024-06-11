import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasePeriodTest {

 private BasePeriod createPeriod(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int millis) {
 return new BasePeriod(year, month, dayOfMonth, hourOfDay, minute, second, millis, PeriodType.standard()) {
 @Override
 public int size() {
 return 8;
 }
 };
 }

 @Test
 public void testNewPeriod() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678);
 PeriodType periodType = period.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testNewPeriodWithChronology() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 PeriodType periodType = period.getPeriodType();
 assertEquals(DateTimeUtils.UTC, period.getChronology());
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testNewPeriodFromInstant() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC).toInstant(DateTimeUtils.UTC);
 BasePeriod expectedPeriod = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 assertEquals(expectedPeriod, period);
 }

 @Test
 public void testNewPeriodFromReadableInstant() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC).withStartAt(DateTimeUtils.parseLocalDate("2022-01-01T12:30:45.678Z").toInstant(DateTimeUtils.UTC));
 BasePeriod expectedPeriod = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 assertEquals(expectedPeriod, period);
 }

 @Test
 public void testNewPeriodFromReadableInstantAndDuration() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC).withStartAt(DateTimeUtils.parseLocalDate("2022-01-01T12:30:45.678Z").toInstant(DateTimeUtils.UTC)).plus(Duration.standardDays(3));
 BasePeriod expectedPeriod = createPeriod(2022, 1, 4, 12, 30, 45, 678, DateTimeUtils.UTC);
 assertEquals(expectedPeriod, period);
 }

 @Test
 public void testNewPeriodFromReadablePartial() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC).withStartAt(DateTimeUtils.parseLocalDate("2022-01-01").toLocalDateTimeAtOffset(12, 30, 45, 678, DateTimeUtils.UTC).toInstant(DateTimeUtils.UTC));
 BasePeriod expectedPeriod = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 assertEquals(expectedPeriod, period);
 }

 @Test
 public void testNewPeriodFromReadablePartialAndReadablePartial() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC).withStartAt(DateTimeUtils.parseLocalDate("2022-01-01").toLocalDateTimeAtOffset(12, 30, 45, 678, DateTimeUtils.UTC).toInstant(DateTimeUtils.UTC)).plus(createPeriod(1, 2, 3, 4, 5, 6, 7, PeriodType.days()));
 BasePeriod expectedPeriod = createPeriod(2022, 2, 4, 16, 2, 11, 7, PeriodType.days());
 assertEquals(expectedPeriod, period);
 }

 @Test
 public void testNewPeriodFromObject() {
 BasePeriod period = new BasePeriod(Duration.standardDays(1), PeriodType.days());
 BasePeriod expectedPeriod = new BasePeriod(1, 0, 0, 0, 0, 0, 0, 0, PeriodType.days());
 assertEquals(expectedPeriod, period);
 }

 @Test
 public void testGetPeriodType() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 PeriodType periodType = period.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testGetValue() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 assertEquals(2022, period.getValue(DurationFieldType.years().getCode()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueInvalidField() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 period.setValue(-1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueInvalidValue() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 period.setValue(DurationFieldType.years().getCode(), -1);
 }

 @Test
 public void testToDurationFrom() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 Duration duration = period.toDurationFrom(DateTimeUtils.parseLocalDate("2022-01-01T00:00:00Z").toInstant(DateTimeUtils.UTC));
 assertEquals(Duration.standardDays(1), duration);
 }

 @Test
 public void testToDurationTo() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 Duration duration = period.toDurationTo(DateTimeUtils.parseLocalDate("2022-01-02T00:00:00Z").toInstant(DateTimeUtils.UTC));
 assertEquals(Duration.standardDays(1).negate(), duration);
 }

 @Test
 public void testCheckAndUpdate() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 int[] values = new int[8];
 period.checkAndUpdate(DurationFieldType.years(), values, 2);
 assertEquals(2, values[0]);
 }

 @Test
 public void testSetPeriod() {
 BasePeriod period1 = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 BasePeriod period2 = createPeriod(2023, 2, 3, 4, 5, 6, 7, PeriodType.days());
 period1.setPeriod(period2);
 assertEquals(period2.getValue(DurationFieldType.years().getCode()), period1.getValue(DurationFieldType.years().getCode()));
 }

 @Test
 public void testAddField() {
 BasePeriod period = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 period.addField(DurationFieldType.years(), 1);
 assertEquals(2023, period.getValue(DurationFieldType.years().getCode()));
 }

 @Test
 public void testMergePeriod() {
 BasePeriod period1 = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 BasePeriod period2 = createPeriod(2, 2, 3, 4, 5, 6, 7, PeriodType.days());
 period1.mergePeriod(period2);
 assertEquals(2, period1.getValue(DurationFieldType.years().getCode()));
 }

 @Test
 public void testAddPeriod() {
 BasePeriod period1 = createPeriod(2022, 1, 1, 12, 30, 45, 678, DateTimeUtils.UTC);
 BasePeriod period2 = createPeriod(2, 2, 3, 4, 5, 6, 7, PeriodType.days());
 period1.addPeriod(period2);
 assertEquals(2, period1.getValue(DurationFieldType.years().getCode()));
 }
}