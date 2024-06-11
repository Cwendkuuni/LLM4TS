import org.joda.time.BasePeriod;
import org.joda.time.BaseLocal;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.field.LocalDateField;
import org.joda.time.field.LocalDateTimeField;
import org.joda.time.field.LocalTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodConstructionWithValues() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 int[] iValues = basePeriod.getValues();
 assertEquals(8, iValues.length);
 assertEquals(1, iValues[0]);
 assertEquals(2, iValues[1]);
 // ... check other values as well
 }

 @Test
 public void testBasePeriodConstructionWithInstant() {
 BasePeriod basePeriod = new BasePeriod(DateTimeUtils.currentTimeMillis(), PeriodType.standard(), DateTimeUtils.getCurrent());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodConstructionWithReadableInstant() {
 BasePeriod basePeriod = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(Duration.standardDays(1)), PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodConstructionWithReadablePartial() {
 BaseLocal start = new LocalDateTime(2012, 1, 1, 0, 0, 0).toLocalDateTime();
 BaseLocal end = new LocalDateTime(2012, 1, 2, 0, 0, 0).toLocalDateTime();
 BasePeriod basePeriod = new BasePeriod(start, end, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodConstructionWithReadableInstantAndReadableDuration() {
 BasePeriod basePeriod = new BasePeriod(DateTimeUtils.currentTime(), Duration.standardDays(1), PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
 BasePeriod basePeriod = new BasePeriod(Duration.standardDays(1), DateTimeUtils.currentTime(), PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodConstructionWithObject() {
 BasePeriod basePeriod = new BasePeriod(DurationField.standardMonths(3), PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodGetPeriodType() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

 @Test
 public void testBasePeriodGetValue() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 int value = basePeriod.getValue(DurationFieldType.years());
 assertEquals(1, value);
 }

 @Test
 public void testBasePeriodToDurationFrom() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration duration = basePeriod.toDurationFrom(DateTimeUtils.currentTime());
 assertEquals(1, duration.getYears());
 assertEquals(2, duration.getMonths());
 // ... check other fields as well
 }

 @Test
 public void testBasePeriodToDurationTo() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration duration = basePeriod.toDurationTo(DateTimeUtils.currentTime());
 assertEquals(-1, duration.getYears());
 assertEquals(-2, duration.getMonths());
 // ... check other fields as well
 }

 @Test
 public void testBasePeriodSetField() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 basePeriod.setField(DurationFieldType.years(), 10);
 int value = basePeriod.getValue(DurationFieldType.years());
 assertEquals(10, value);
 }

 @Test
 public void testBasePeriodAddField() {
 BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 basePeriod.addField(DurationFieldType.years(), 5);
 int value = basePeriod.getValue(DurationFieldType.years());
 assertEquals(6, value);
 }

 @Test
 public void testBasePeriodMergePeriod() {
 BasePeriod first = new BasePeriod(DurationField.standardDays(1), PeriodType.standard());
 BasePeriod second = new BasePeriod(DurationField.standardDays(2), PeriodType.standard());
 BasePeriod merged = new BasePeriod();
 merged.mergePeriod(first);
 merged.mergePeriod(second);
 int[] mergedValues = merged.getValues();
 assertEquals(8, mergedValues.length);
 assertEquals(1, mergedValues[0]); // years
 assertEquals(0, mergedValues[1]); // months
 // ... check other values as well
 }

 @Test
 public void testBasePeriodAddPeriod() {
 BasePeriod first = new BasePeriod(DurationField.standardDays(1), PeriodType.standard());
 BasePeriod second = new BasePeriod(DurationField.standardDays(2), PeriodType.standard());
 BasePeriod sum = new BasePeriod();
 sum.addPeriod(first);
 sum.addPeriod(second);
 int[] sumValues = sum.getValues();
 assertEquals(8, sumValues.length);
 assertEquals(3, sumValues[0]); // years
 // ... check other values as well
 }

 @Test
 public void testBasePeriodSetPeriod() {
 BasePeriod basePeriod = new BasePeriod();
 ReadablePeriod periodToSet = new BasePeriod(DurationField.standardDays(1), PeriodType.standard());
 basePeriod.setPeriod(periodToSet);
 PeriodType periodType = basePeriod.getPeriodType();
 assertEquals(PeriodType.standard(), periodType);
 }

}