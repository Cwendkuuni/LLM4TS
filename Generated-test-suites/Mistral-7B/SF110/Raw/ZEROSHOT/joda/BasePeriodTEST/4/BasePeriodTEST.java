import org.joda.time.BasePeriod;
import org.joda.time.BasePeriodtest;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriod_int_int_int_int_int_int_int_int_PeriodType() {
 BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_long_long_PeriodType_Chronology() {
 BasePeriod period = new BasePeriod(1L, 2L, PeriodType.standard(), DateTimeUtils.UTC);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 1};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_ReadableInstant_ReadableInstant_PeriodType() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plusDays(1), PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.days();
 int[] expectedValues = new int[]{0, 0, 1, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_ReadablePartial_ReadablePartial_PeriodType() {
 BaseLocal baseLocal1 = new BaseLocal(DateTimeUtils.currentTime(), DateTimeUtils.UTC);
 BaseLocal baseLocal2 = baseLocal1.plusDays(1);
 BasePeriod period = new BasePeriod(baseLocal1, baseLocal2, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.days();
 int[] expectedValues = new int[]{0, 0, 1, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_ReadableInstant_ReadableDuration_PeriodType() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), DateTimeUtils.millis(1000), PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.milliseconds();
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 0, 1, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_ReadableDuration_ReadableInstant_PeriodType() {
 BasePeriod period = new BasePeriod(DateTimeUtils.millis(1000), DateTimeUtils.currentTime(), PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.milliseconds();
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 0, -1, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_long() {
 BasePeriod period = new BasePeriod(1L, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_long_PeriodType_Chronology() {
 BasePeriod period = new BasePeriod(1L, PeriodType.standard(), DateTimeUtils.UTC);
 PeriodType expectedPeriodType = PeriodType.standard();
 int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_Object_PeriodType_Chronology() {
 Period otherPeriod = new Duration(DateTimeUtils.millis(1000), DateTimeUtils.UTC);
 BasePeriod period = new BasePeriod(otherPeriod, PeriodType.standard(), DateTimeUtils.UTC);
 PeriodType expectedPeriodType = PeriodType.milliseconds();
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 0, 1, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testBasePeriod_intArray_PeriodType() {
 int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
 BasePeriod period = new BasePeriod(values, PeriodType.standard());
 PeriodType expectedPeriodType = PeriodType.standard();

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(values, period.getValues());
 }

 @Test
 public void testToDurationFrom() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 Duration duration = period.toDurationFrom(DateTimeUtils.currentTime().plusDays(1));
 Duration expectedDuration = DateTimeUtils.days(1);

 assertEquals(expectedDuration.getMillis(), duration.getMillis());
 }

 @Test
 public void testToDurationTo() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 Duration duration = period.toDurationTo(DateTimeUtils.currentTime().plusDays(1));
 Duration expectedDuration = DateTimeUtils.days(1).negate();

 assertEquals(expectedDuration.getMillis(), duration.getMillis());
 }

 @Test
 public void testSetPeriod() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 ReadablePeriod periodToSet = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.hours(1));
 period.setPeriod(periodToSet);
 PeriodType expectedPeriodType = PeriodType.hours();
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 1, 0, 0};

 assertEquals(expectedPeriodType, period.getPeriodType());
 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testSetField() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 period.setField(DurationFieldType.hours(), 2);
 int[] expectedValues = new int[]{0, 0, 0, 0, 2, 0, 0, 1};

 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testAddField() {
 BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 period.addField(DurationFieldType.hours(), 2);
 int[] expectedValues = new int[]{0, 0, 0, 0, 2, 0, 0, 1};

 assertArrayEquals(expectedValues, period.getValues());
 }

 @Test
 public void testMergePeriod() {
 BasePeriod period1 = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 BasePeriod period2 = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.hours(2));
 period1.mergePeriod(period2);
 PeriodType expectedPeriodType = PeriodType.hours(2, PeriodType.days(1));
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 2, 0, 1};

 assertEquals(expectedPeriodType, period1.getPeriodType());
 assertArrayEquals(expectedValues, period1.getValues());
 }

 @Test
 public void testAddPeriod() {
 BasePeriod period1 = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.days(1));
 BasePeriod period2 = new BasePeriod(DateTimeUtils.currentTime(), PeriodType.hours(2));
 period1.addPeriod(period2);
 PeriodType expectedPeriodType = PeriodType.hours(2, PeriodType.days(1));
 int[] expectedValues = new int[]{0, 0, 0, 0, 0, 2, 0, 1};

 assertEquals(expectedPeriodType, period1.getPeriodType());
 assertArrayEquals(expectedValues, period1.getValues());
 }
}