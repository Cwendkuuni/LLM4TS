import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodConstructionWithValidArguments() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = period.getValues();
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodConstructionWithValidArgumentsAndChronology() {
 BasePeriod period = new BasePeriod(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = period.getValues();
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[] {31, 1, 0, 0, 0, 0, 0, 0};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodConstructionWithNullArguments() {
 try {
 new BasePeriod(null, null, PeriodType.standard());
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testBasePeriodConstructionWithInvalidArguments() {
 try {
 new BasePeriod("invalid", "invalid", PeriodType.standard());
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testBasePeriodConstructionWithInvalidValues() {
 try {
 new BasePeriod(1, Integer.MAX_VALUE, PeriodType.standard());
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testBasePeriodImmutableMethods() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

 PeriodType actualPeriodType = period.getPeriodType();
 int[] actualValues = period.getValues();

 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};

 assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
 assertArrayEquals("Incorrect values", expectedValues, actualValues);

 assertThrows(UnsupportedOperationException.class, () -> period.setPeriod(null));
 assertThrows(UnsupportedOperationException.class, () -> period.setValues(new int[period.size()]));
 }

 @Test
 public void testBasePeriodMutableMethods() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 int[] values = period.getValues();

 // Set values to new values
 period.setPeriod(new BasePeriod(1, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard()));
 int[] newValues = period.getValues();

 assertArrayEquals("Incorrect new values after setting the period", newValues, new int[] {1, 3, 4, 5, 6, 7, 8, 9});

 // Set values to custom values
 period.setValues(new int[] {10, 20, 30, 40, 50, 60, 70, 80});
 int[] customValues = period.getValues();

 assertArrayEquals("Incorrect custom values after setting the values", customValues, new int[] {10, 20, 30, 40, 50, 60, 70, 80});
 }

 @Test
 public void testBasePeriodToDurationMethods() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

 Duration durationFrom = period.toDurationFrom(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC));
 Duration durationTo = period.toDurationTo(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC));

 Long expectedDurFromMillis = 86400001L; // 1 day + 1 second
 Long expectedDurToMillis = 86400000L; // 1 day

 long actualDurFromMillis = durationFrom.getMillis();
 long actualDurToMillis = durationTo.getMillis();

 assertEquals("Incorrect duration from milliseconds", expectedDurFromMillis, actualDurFromMillis);
 assertEquals("Incorrect duration to milliseconds", expectedDurToMillis, actualDurToMillis);
 }

 @Test
 public void testBasePeriodSetFieldMethods() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

 period.setField(DurationFieldType.years(), 3);
 period.setField(DurationFieldType.months(), 2);
 period.setField(DurationFieldType.days(), -1);
 period.setField(DurationFieldType.hours(), 23);
 period.setField(DurationFieldType.minutes(), 59);
 period.setField(DurationFieldType.seconds(), 59);
 period.setField(DurationFieldType.millis(), 999);

 int[] expectedValues = {3, 2, 0, 23, 59, 59, 999, 0};
 int[] actualValues = period.getValues();

 assertArrayEquals("Incorrect values after setting fields", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodAddFieldMethods() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

 period.addField(DurationFieldType.years(), 3);
 period.addField(DurationFieldType.months(), 2);
 period.addField(DurationFieldType.days(), 1);
 period.addField(DurationFieldType.hours(), 22);
 period.addField(DurationFieldType.minutes(), 58);
 period.addField(DurationFieldType.seconds(), 58);
 period.addField(DurationFieldType.millis(), 1000);

 int[] expectedValues = {4, 4, 1, 23, 59, 59, 1000, 9};
 int[] actualValues = period.getValues();

 assertArrayEquals("Incorrect values after adding fields", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodMergePeriodMethods() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(3, 2, 1, 2, 5, 6, 7, 8, PeriodType.standard());

 period1.mergePeriod(period2);
 int[] expectedValues = {4, 4, 1, 23, 11, 12, 7, 8};
 int[] actualValues = period1.getValues();

 assertArrayEquals("Incorrect values after merging periods", expectedValues, actualValues);
 }

 @Test
 public void testBasePeriodAddPeriodMethods() {
 BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(3, 2, 1, 2, 5, 6, 7, 8, PeriodType.standard());

 period1.addPeriod(period2);
 int[] expectedValues = {4, 4, 1, 23, 11, 12, 7, 16};
 int[] actualValues = period1.getValues();

 assertArrayEquals("Incorrect values after adding periods", expectedValues, actualValues);
 }
}