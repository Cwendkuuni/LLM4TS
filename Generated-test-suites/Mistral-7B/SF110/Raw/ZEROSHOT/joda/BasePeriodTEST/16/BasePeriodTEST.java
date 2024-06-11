package org.junit.jupiter.api;
import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasePeriodTest {

 private BasePeriod basePeriod;
 private PeriodType standardPeriodType;
 private PeriodType customPeriodType;
 private Chronology isoChronology;
 private ReadableInstant readableInstant;
 private ReadableDuration readableDuration;
 private ReadablePartial readablePartial;
 private ReadablePeriod readablePeriod;
 private int[] expectedValues;

 @BeforeEach
 public void setup() {
 standardPeriodType = PeriodType.standard();
 customPeriodType = new PeriodTypeBase(new String[]{"testField"}, new DurationFieldType[] {DurationFieldType.days()});
 isoChronology = ISOChronology.getInstanceUTC();

 basePeriod = new BasePeriod(4, 2, 1, 3, 5, 6, 7, 8, standardPeriodType);
 }

 @Test
 public void testBasePeriodConstructionWithValues() {
 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 Assertions.assertArrayEquals(new int[]{4, 2, 1, 3, 5, 6, 7, 8}, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithInstant() {
 final long instantMillis = 1609459200000L;
 readableInstant = DateTimeUtils.millis(instantMillis);
 basePeriod = new BasePeriod(readableInstant, readableInstant, standardPeriodType);

 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 expectedValues = isoChronology.get(new BasePeriod(instantMillis), 0);
 Assertions.assertArrayEquals(expectedValues, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadableInstantAndReadableDuration() {
 final long instantMillis = 1609459200000L;
 readableInstant = DateTimeUtils.millis(instantMillis);
 readableDuration = Duration.standardDays(1);
 basePeriod = new BasePeriod(readableInstant, readableDuration, standardPeriodType);

 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 expectedValues = isoChronology.get(new BasePeriod(instantMillis + DateTimeUtils.getDurationMillis(readableDuration)), 0);
 Assertions.assertArrayEquals(expectedValues, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadablePartial() {
 final long localMillis = 1609459200000L;
 readablePartial = new BaseLocalDateTime(isoChronology, localMillis);
 basePeriod = new BasePeriod(readablePartial, readablePartial, standardPeriodType);

 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 expectedValues = isoChronology.get(new BasePeriod(localMillis), 0);
 Assertions.assertArrayEquals(expectedValues, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadableInstantAndReadablePeriod() {
 final long instantMillis = 1609459200000L;
 readableInstant = DateTimeUtils.millis(instantMillis);
 readablePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 basePeriod = new BasePeriod(readableInstant, readablePeriod, standardPeriodType);

 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithDurationAndReadableInstant() {
 readableDuration = Duration.standardDays(1);
 readableInstant = DateTimeUtils.millis(1609459200000L);
 basePeriod = new BasePeriod(readableDuration, readableInstant, standardPeriodType);

 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 expectedValues = isoChronology.get(new BasePeriod(readableDuration.negate(), readableInstant), 0);
 Assertions.assertArrayEquals(expectedValues, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodConstructionWithReadablePeriodAndReadableInstant() {
 readablePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 readableInstant = DateTimeUtils.millis(1609459200000L);
 basePeriod = new BasePeriod(readablePeriod, readableInstant, standardPeriodType);

 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 Assertions.assertArrayEquals(readablePeriod.getValues(), basePeriod.getValues());
 }

 @Test
 public void testBasePeriodGetPeriodType() {
 Assertions.assertEquals(standardPeriodType, basePeriod.getPeriodType());
 }

 @Test
 public void testBasePeriodGetValue() {
 for (int i = 0; i < basePeriod.size(); i++) {
 Assertions.assertEquals(basePeriod.getValue(i), expectedValues[i]);
 }
 }

 @Test
 public void testBasePeriodToDurationFrom() {
 final long instantMillis = 1609459300000L;
 final ReadableInstant readableInstant = DateTimeUtils.millis(instantMillis);
 final Duration expectedDuration = Duration.standardDays(1);

 Assertions.assertEquals(expectedDuration, basePeriod.toDurationFrom(readableInstant));
 }

 @Test
 public void testBasePeriodToDurationTo() {
 final long instantMillis = 1609459200000L;
 final ReadableInstant readableInstant = DateTimeUtils.millis(instantMillis);
 final Duration expectedDuration = Duration.standardDays(1);

 Assertions.assertEquals(expectedDuration, basePeriod.toDurationTo(readableInstant));
 }

 @Test
 public void testBasePeriodCheckAndUpdate() {
 final int[] initialValues = basePeriod.getValues();
 final DurationFieldType fieldType = DurationFieldType.days();
 final int newValue = 5;

 basePeriod.checkAndUpdate(fieldType, newValue);

 Assertions.assertArrayNotEquals(initialValues, basePeriod.getValues());
 Assertions.assertEquals(newValue, basePeriod.getValue(basePeriod.indexOf(fieldType)));
 }

 @Test
 public void testBasePeriodSetPeriod() {
 final ReadablePeriod readablePeriodToSet = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 basePeriod.setPeriod(readablePeriodToSet);

 Assertions.assertEquals(readablePeriodToSet.getPeriodType(), basePeriod.getPeriodType());
 Assertions.assertArrayEquals(readablePeriodToSet.getValues(), basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetField() {
 final int newValue = 5;
 final DurationFieldType fieldType = DurationFieldType.days();
 basePeriod.setField(fieldType, newValue);

 Assertions.assertEquals(newValue, basePeriod.getValue(basePeriod.indexOf(fieldType)));
 }

 @Test
 public void testBasePeriodAddField() {
 final int newValue = 5;
 final DurationFieldType fieldType = DurationFieldType.days();
 basePeriod.addField(fieldType, newValue);

 Assertions.assertEquals(basePeriod.getValue(basePeriod.indexOf(fieldType)) + newValue, basePeriod.getValue(basePeriod.indexOf(fieldType)));
 }

 @Test
 public void testBasePeriodMergePeriod() {
 final ReadablePeriod readablePeriodToMerge = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 basePeriod.mergePeriod(readablePeriodToMerge);

 Assertions.assertEquals(readablePeriodToMerge.getPeriodType(), basePeriod.getPeriodType());
 Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 4, 2}, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodAddPeriod() {
 final ReadablePeriod readablePeriodToAdd = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 basePeriod.addPeriod(readablePeriodToAdd);

 Assertions.assertEquals(readablePeriodToAdd.getPeriodType(), basePeriod.getPeriodType());
 Assertions.assertArrayEquals(new int[]{2, 4, 4, 7, 6, 8, 11, 11, 4, 2}, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetFieldInto() {
 final int[] initialValues = basePeriod.getValues();
 final DurationFieldType fieldType = DurationFieldType.days();
 final int newValue = 5;

 basePeriod.setFieldInto(basePeriod.getValues(), fieldType, newValue);

 Assertions.assertArrayNotEquals(initialValues, basePeriod.getValues());
 Assertions.assertEquals(newValue, basePeriod.getValue(basePeriod.indexOf(fieldType)));
 }

 @Test
 public void testBasePeriodAddFieldInto() {
 final int[] initialValues = basePeriod.getValues();
 final DurationFieldType fieldType = DurationFieldType.days();
 final int newValue = 5;

 basePeriod.addFieldInto(basePeriod.getValues(), fieldType, newValue);

 Assertions.assertArrayNotEquals(initialValues, basePeriod.getValues());
 Assertions.assertEquals(basePeriod.getValue(basePeriod.indexOf(fieldType)) + newValue, basePeriod.getValue(basePeriod.indexOf(fieldType)));
 }

 @Test
 public void testBasePeriodMergePeriodInto() {
 final int[] readablePeriodValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
 final ReadablePeriod readablePeriodToMerge = new BasePeriod(readablePeriodValues, standardPeriodType);
 final int[] initialValues = basePeriod.getValues();

 basePeriod.mergePeriodInto(basePeriod.getValues(), readablePeriodToMerge);

 Assertions.assertArrayNotEquals(initialValues, basePeriod.getValues());
 Assertions.assertArrayEquals(readablePeriodValues, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodAddPeriodInto() {
 final int[] readablePeriodValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
 final ReadablePeriod readablePeriodToAdd = new BasePeriod(readablePeriodValues, standardPeriodType);
 final int[] initialValues = basePeriod.getValues();

 basePeriod.addPeriodInto(basePeriod.getValues(), readablePeriodToAdd);

 Assertions.assertArrayNotEquals(initialValues, basePeriod.getValues());
 Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 1, 2}, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetValues() {
 final int[] newValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
 basePeriod.setValues(newValues);

 Assertions.assertArrayEquals(newValues, basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetPeriodInternal() {
 final ReadablePeriod readablePeriodToSet = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, standardPeriodType);
 basePeriod.setPeriodInternal(readablePeriodToSet);

 Assertions.assertEquals(readablePeriodToSet.getPeriodType(), basePeriod.getPeriodType());
 Assertions.assertArrayEquals(readablePeriodToSet.getValues(), basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetPeriodInternalWithCustomPeriodType() {
 final ReadablePeriod readablePeriodToSet = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, customPeriodType);
 basePeriod.setPeriodInternal(readablePeriodToSet);

 Assertions.assertEquals(customPeriodType, basePeriod.getPeriodType());
 Assertions.assertArrayEquals(readablePeriodToSet.getValues(), basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetPeriodInternalWithReadablePeriodAndCustomPeriodType() {
 final ReadablePeriod readablePeriodToSet = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, customPeriodType);
 basePeriod.setPeriodInternal(readablePeriodToSet, customPeriodType);

 Assertions.assertEquals(customPeriodType, basePeriod.getPeriodType());
 Assertions.assertArrayEquals(readablePeriodToSet.getValues(), basePeriod.getValues());
 }

 @Test
 public void testBasePeriodSetPeriodInternalWithObjectAndCustomPeriodType() {
 final Object period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, customPeriodType);
 basePeriod.setPeriodInternal(period, customPeriodType);

 Assertions.assertEquals(customPeriodType, basePeriod.getPeriodType());
 Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, basePeriod.getValues());
 }
}