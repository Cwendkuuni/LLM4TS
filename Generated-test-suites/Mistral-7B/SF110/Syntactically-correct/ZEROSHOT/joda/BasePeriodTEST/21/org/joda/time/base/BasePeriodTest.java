import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasePeriodTest {

 private BasePeriod period;
 private PeriodType standardPeriodType;
 private PeriodType customPeriodType;
 private Chronology isoChronology;
 private Chronology gregorianChronology;
 private Instant instant;
 private ReadableInstant readableInstant;
 private ReadablePartial readablePartial;
 private ReadableDuration readableDuration;

 @Before
 public void setUp() {
 standardPeriodType = PeriodType.standard();
 customPeriodType = new DefaultPeriodType(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, new DurationFieldType[]{DurationFieldType.years(), DurationFieldType.months(), DurationFieldType.weeks(), DurationFieldType.days(), DurationFieldType.hours(), DurationFieldType.minutes(), DurationFieldType.seconds(), DurationFieldType.millis()});
 isoChronology = ISOChronology.getInstanceUTC();
 gregorianChronology = GregorianChronology.getInstance();
 instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, isoChronology).toInstant();
 readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, isoChronology).toReadableInstant();
 readablePartial = new DateTime(2022, 1, 1, 1, 1, 1, 1, gregorianChronology).toReadablePartial();
 readableDuration = new Duration(7, TimeUnit.DAYS);
 }

 @Test
 public void testBasePeriodConstructionWithInts() {
 period = new BasePeriod(4, 2, 3, 1, 1, 1, 1, 1, standardPeriodType);
 assertEquals(standardPeriodType, period.getPeriodType());
 assertArrayEquals(new int[]{4, 2, 3, 1, 1, 1, 1, 1}, period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithLong() {
 period = new BasePeriod(123456789);
 assertEquals(standardPeriodType, period.getPeriodType());
 assertArrayEquals(ISOChronology.getInstanceUTC().getValues(BasePeriod.DUMMY_PERIOD, 123456789), period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadableInstant() {
 period = new BasePeriod(readableInstant, readableInstant, standardPeriodType);
 assertEquals(standardPeriodType, period.getPeriodType());
 assertArrayEquals(isoChronology.getValues(period, readableInstant.getMillis(), readableInstant.getMillis()), period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadablePartial() {
 period = new BasePeriod(readablePartial, readablePartial, standardPeriodType);
 assertEquals(standardPeriodType, period.getPeriodType());
 assertArrayEquals(gregorianChronology.getValues(period, readablePartial.getLocalMillis(), readablePartial.getLocalMillis()), period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadableInstantAndDuration() {
 period = new BasePeriod(readableInstant, readableDuration, standardPeriodType);
 assertEquals(standardPeriodType, period.getPeriodType());
 long instantMillis = DateTimeUtils.getInstantMillis(readableInstant);
 long safeAdd = FieldUtils.safeAdd(instantMillis, DateTimeUtils.getDurationMillis(readableDuration));
 assertArrayEquals(isoChronology.getValues(period, instantMillis, safeAdd), period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
 period = new BasePeriod(readableDuration, readableInstant, standardPeriodType);
 assertEquals(standardPeriodType, period.getPeriodType());
 long instantMillis = DateTimeUtils.getInstantMillis(readableInstant);
 long safeSubtract = FieldUtils.safeSubtract(instantMillis, DateTimeUtils.getDurationMillis(readableDuration));
 assertArrayEquals(isoChronology.getValues(period, safeSubtract, instantMillis), period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithObject() {
 Duration testDuration = new Duration(3, TimeUnit.DAYS);
 period = new BasePeriod(testDuration, standardPeriodType, gregorianChronology);
 assertEquals(standardPeriodType, period.getPeriodType());
 assertArrayEquals(gregorianChronology.getValues(period, 0, 0), period.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithIntsAndPeriodType() {
 period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, customPeriodType);
 assertEquals(customPeriodType, period.getPeriodType());
 assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.getValues());
 }

 @Test
 public void testGetPeriodType() {
 period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 assertEquals(standardPeriodType, period.getPeriodType());
 period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, customPeriodType);
 assertEquals(customPeriodType, period.getPeriodType());
 }

 @Test
 public void testGetValue() {
 period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 assertEquals(4, period.getValue(DurationFieldType.days().getCode()));
 assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
 }

 @Test
 public void testToDurationFrom() {
 Duration duration = period.toDurationFrom(readableInstant);
 assertEquals(new Duration(DurationFieldType.days().getMillisPerUnit() * 31 + DurationFieldType.hours().getMillisPerUnit() * 24 * 30 + DurationFieldType.minutes().getMillisPerUnit() * 60 * 24 * 30 + DurationFieldType.seconds().getMillisPerUnit() * 60 * 24 * 30 + DurationFieldType.millis().getMillisPerUnit() * 60 * 24 * 30, gregorianChronology), duration);
 }

 @Test
 public void testToDurationTo() {
 Duration duration = period.toDurationTo(readableInstant);
 assertEquals(new Duration(-(DurationFieldType.days().getMillisPerUnit() * 31 + DurationFieldType.hours().getMillisPerUnit() * 24 * 30 + DurationFieldType.minutes().getMillisPerUnit() * 60 * 24 * 30 + DurationFieldType.seconds().getMillisPerUnit() * 60 * 24 * 30 + DurationFieldType.millis().getMillisPerUnit() * 60 * 24 * 30), gregorianChronology), duration);
 }
}