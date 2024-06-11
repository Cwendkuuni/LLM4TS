package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 private static final Chronology UTC = DateTimeUtils.UTC;

 @Test
 public void testEmptyPartial() {
 Partial empty = new Partial();
 assertEquals(0, empty.size());
 assertNull(empty.getChronology());
 }

 @Test
 public void testPartialWithValidArgs() {
 DateTimeFieldType year = DateTimeFieldType.year();
 DateTimeFieldType month = DateTimeFieldType.monthOfYear();
 DateTimeFieldType day = DateTimeFieldType.dayOfMonth();

 Partial valid = new Partial(year, 2022, UTC);
 Partial validWithUTC = new Partial(year, month, day, 12, UTC);
 Partial validWithUTCAndValues = new Partial(year, month, day, 12, 31, UTC);

 assertEquals(2, valid.size());
 assertEquals(UTC, valid.getChronology());
 assertEquals(year, valid.getFieldType(0));
 assertEquals(2022, valid.getValue(0));
 assertEquals(month, valid.getFieldType(1));
 assertEquals(12, valid.getValue(1));

 assertEquals(3, validWithUTC.size());
 assertEquals(UTC, validWithUTC.getChronology());
 assertEquals(year, validWithUTC.getFieldType(0));
 assertEquals(2022, validWithUTC.getValue(0));
 assertEquals(month, validWithUTC.getFieldType(1));
 assertEquals(12, validWithUTC.getValue(1));
 assertEquals(day, validWithUTC.getFieldType(2));
 assertEquals(31, validWithUTC.getValue(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFieldType() {
 Partial invalid = new Partial(null, 2022, UTC);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullChronology() {
 Partial invalid = new Partial(DateTimeFieldType.year(), null, 2022);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWrongLengthValues() {
 DateTimeFieldType year = DateTimeFieldType.year();
 DateTimeFieldType month = DateTimeFieldType.monthOfYear();
 DateTimeFieldType day = DateTimeFieldType.dayOfMonth();

 Partial invalid = new Partial(year, new int[] { 2022, 12, 31, 32 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValues() {
 DateTimeFieldType year = DateTimeFieldType.year();
 DateTimeFieldType month = DateTimeFieldType.monthOfYear();
 DateTimeFieldType day = DateTimeFieldType.dayOfMonth();

 Partial invalid = new Partial(year, month, day, 13, 32, UTC);
 }

 @Test
 public void testCopyConstructor() {
 Partial original = new Partial(DateTimeFieldType.year(), 2022, UTC);
 Partial copy = new Partial(original, original.getValues());
 assertEquals(original, copy);
 }

 @Test
 public void testWithChronologyRetainFields() {
 Partial original = new Partial(DateTimeFieldType.year(), 2022, UTC);
 Partial copy = original.withChronologyRetainFields(DateTimeUtils.GREGORIAN);
 assertNotSame(original, copy);
 assertEquals(UTC, original.getChronology());
 assertEquals(DateTimeFieldType.year(), original.getFieldType(0));
 assertEquals(2022, original.getValue(0));
 assertEquals(DateTimeFieldType.year(), copy.getFieldType(0));
 assertEquals(2022, copy.getValue(0));
 assertEquals(DateTimeUtils.GREGORIAN, copy.getChronology());
 }

 @Test
 public void testWith() {
 Partial original = new Partial(DateTimeFieldType.year(), 2022, UTC);
 Partial newPartial = original.with(DateTimeFieldType.monthOfYear(), 11);
 assertEquals(2, newPartial.size());
 assertEquals(UTC, newPartial.getChronology());
 assertEquals(DateTimeFieldType.year(), newPartial.getFieldType(0));
 assertEquals(2022, newPartial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldType(1));
 assertEquals(11, newPartial.getValue(1));
 }

 @Test
 public void testWithout() {
 Partial original = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 Partial newPartial = original.without(DateTimeFieldType.dayOfMonth());
 assertEquals(2, newPartial.size());
 assertEquals(UTC, newPartial.getChronology());
 assertNotSame(original.getFieldType(1), newPartial.getFieldType(1));
 assertEquals(DateTimeFieldType.year(), newPartial.getFieldType(0));
 assertEquals(2022, newPartial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldType(1));
 assertEquals(11, newPartial.getValue(1));
 }

 @Test
 public void testWithField() {
 Partial original = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 Partial newPartial = original.withField(DateTimeFieldType.monthOfYear(), 10);
 assertEquals(2, newPartial.size());
 assertEquals(UTC, newPartial.getChronology());
 assertEquals(DateTimeFieldType.year(), newPartial.getFieldType(0));
 assertEquals(2022, newPartial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldType(1));
 assertEquals(10, newPartial.getValue(1));
 }

 @Test
 public void testWithFieldAdded() {
 LocalDateTime date = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0, UTC);
 Partial original = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 Partial newPartial = original.withFieldAdded(DurationFieldType.dayOfYear(), 1);
 assertEquals(3, newPartial.size());
 assertEquals(UTC, newPartial.getChronology());
 assertEquals(DateTimeFieldType.year(), newPartial.getFieldType(0));
 assertEquals(2022, newPartial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldType(1));
 assertEquals(11, newPartial.getValue(1));
 assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(2));
 assertEquals(30, newPartial.getValue(2));
 assertEquals(DateTimeFieldType.dayOfYear(), newPartial.getFieldType(2));
 assertEquals(365, newPartial.getValue(3));
 }

 @Test
 public void testWithFieldAddWrapped() {
 LocalDateTime date = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0, UTC);
 Partial original = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 Partial newPartial = original.withFieldAddWrapped(DurationFieldType.dayOfYear(), 1);
 assertEquals(3, newPartial.size());
 assertEquals(UTC, newPartial.getChronology());
 assertEquals(DateTimeFieldType.year(), newPartial.getFieldType(0));
 assertEquals(2022, newPartial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldType(1));
 assertEquals(11, newPartial.getValue(1));
 assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(2));
 assertEquals(30, newPartial.getValue(2));
 assertEquals(DateTimeFieldType.dayOfYear(), newPartial.getFieldType(2));
 assertEquals(366, newPartial.getValue(3));
 }

 @Test
 public void testWithPeriodAdded() {
 LocalDateTime date1 = new LocalDateTime(2022, 11, 30, 12, 0, 0, 0, UTC);
 LocalDateTime date2 = new LocalDateTime(2023, 11, 1, 12, 0, 0, 0, UTC);
 Partial original = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 Partial newPartial = original.withPeriodAdded(new LocalDateTime(2023, 11, 1, 0, 0, 0, 0, UTC).toPeriod(), 1);
 assertEquals(3, newPartial.size());
 assertEquals(UTC, newPartial.getChronology());
 assertEquals(DateTimeFieldType.year(), newPartial.getFieldType(0));
 assertEquals(2023, newPartial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldType(1));
 assertEquals(11, newPartial.getValue(1));
 assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldType(2));
 assertEquals(1, newPartial.getValue(2));
 }

 @Test
 public void testToString() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 assertEquals("[year=2022, monthOfYear=11]", partial.toString());
 }

 @Test
 public void testToStringList() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 assertEquals("[year=2022, monthOfYear=11]", partial.toStringList());
 }

 @Test
 public void testToStringWithFormatter() {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 assertEquals("2022-11-30", partial.toString(formatter));
 }

 @Test
 public void testToStringWithLocaleAndFormatter() {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), 2022, 11, UTC);
 assertEquals("30/11/2022 12:00 PM", partial.toString(formatter, Locale.US));
 }
}