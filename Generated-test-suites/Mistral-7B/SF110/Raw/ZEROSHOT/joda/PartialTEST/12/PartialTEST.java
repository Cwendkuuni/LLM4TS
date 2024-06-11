package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import java.util.*;
import org.joda.time.field.*;

class PartialTest {

 @Test
 void testEmptyConstructor() {
 Partial partial = new Partial();
 assertEquals(0, partial.size());
 }

 @Test
 void testNullChronologyConstructor() {
 assertThrows(IllegalArgumentException.class, () -> new Partial(null));
 }

 @Test
 void testEmptyDateTimeFieldTypeAndIntConstructor() {
 Partial partial = new Partial();
 Partial emptyPartial = new Partial(partial);
 assertEquals(emptyPartial, partial);
 }

 @Test
 void testSingleDateTimeFieldTypeAndIntConstructor() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10);
 assertEquals(1, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 }

 @Test
 void testSingleDateTimeFieldTypeAndIntAndChronologyConstructor() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 assertEquals(1, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 }

 @Test
 void testMultipleDateTimeFieldTypeAndIntsConstructor() {
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute() };
 Partial partial = new Partial(fieldTypes, new int[] { 10, 30, 20 });
 assertEquals(3, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 assertEquals(DateTimeFieldType.minuteOfHour(), partial.getFieldTypes()[1]);
 assertEquals(30, partial.getValue(1));
 assertEquals(DateTimeFieldType.secondOfMinute(), partial.getFieldTypes()[2]);
 assertEquals(20, partial.getValue(2));
 }

 @Test
 void testMultipleDateTimeFieldTypeAndIntsAndChronologyConstructor() {
 DateTimeFieldType[] fieldTypes = { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute() };
 Partial partial = new Partial(fieldTypes, new int[] { 10, 30, 20 }, DateTimeUtils.UTC);
 assertEquals(3, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 assertEquals(DateTimeFieldType.minuteOfHour(), partial.getFieldTypes()[1]);
 assertEquals(30, partial.getValue(1));
 assertEquals(DateTimeFieldType.secondOfMinute(), partial.getFieldTypes()[2]);
 assertEquals(20, partial.getValue(2));
 }

 @Test
 void testReadablePartialConstructor() {
 Partial partial = new Partial();
 Partial readablePartial = new GregorianCalendar(2022, 1, 1).toPartial();
 Partial copy = new Partial(readablePartial);
 assertEquals(partial, copy);
 }

 @Test
 void testPartialWithChronologyRetainFields() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 Partial copy = partial.withChronologyRetainFields(DateTimeUtils.UTC);
 assertEquals(partial, copy);
 }

 @Test
 void testPartialWith() {
 Partial partial = new Partial();
 DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
 partial = partial.with(fieldType, 10);
 assertEquals(1, partial.size());
 assertEquals(fieldType, partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 }

 @Test
 void testPartialWithout() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 Partial copy = partial.without(DateTimeFieldType.minuteOfHour());
 assertEquals(0, copy.size());
 }

 @Test
 void testPartialWithField() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 DateTimeFieldType fieldType = DateTimeFieldType.minuteOfHour();
 partial = partial.withField(fieldType, 30);
 assertEquals(2, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 assertEquals(DateTimeFieldType.minuteOfHour(), partial.getFieldTypes()[1]);
 assertEquals(30, partial.getValue(1));
 }

 @Test
 void testPartialWithFieldAdded() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 DurationFieldType durationFieldType = DurationFieldType.milliseconds();
 partial = partial.withFieldAdded(durationFieldType, 500);
 assertEquals(2, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 assertEquals(durationFieldType, partial.getFieldTypes()[1]);
 assertEquals(500, partial.getValue(1));
 }

 @Test
 void testPartialWithFieldAddWrapped() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 DurationFieldType durationFieldType = DurationFieldType.milliseconds();
 partial = partial.withFieldAddWrapped(durationFieldType, 500);
 assertEquals(2, partial.size());
 assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldTypes()[0]);
 assertEquals(10, partial.getValue(0));
 assertEquals(durationFieldType, partial.getFieldTypes()[1]);
 assertEquals(-550, partial.getValue(1));
 }

 @Test
 void testPartialWithPeriodAdded() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 ReadablePeriod period = Duration.standardHours(1).plus(Duration.standardMinutes(30));
 Partial copy = partial.withPeriodAdded(period, 1);
 assertEquals(2, copy.size());
 assertEquals(DateTimeFieldType.hourOfDay(), copy.getFieldTypes()[0]);
 assertEquals(11, copy.getValue(0));
 assertEquals(DateTimeFieldType.minuteOfHour(), copy.getFieldTypes()[1]);
 assertEquals(30, copy.getValue(1));
 }

 @Test
 void testPartialPlus() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 ReadablePeriod period = Duration.standardHours(1);
 Partial copy = partial.plus(period);
 assertEquals(1, copy.size());
 assertEquals(DateTimeFieldType.hourOfDay(), copy.getFieldTypes()[0]);
 assertEquals(11, copy.getValue(0));
 }

 @Test
 void testPartialMinus() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 11, DateTimeUtils.UTC);
 ReadablePeriod period = Duration.standardHours(1);
 Partial copy = partial.minus(period);
 assertEquals(1, copy.size());
 assertEquals(DateTimeFieldType.hourOfDay(), copy.getFieldTypes()[0]);
 assertEquals(10, copy.getValue(0));
 }

 @Test
 void testProperty() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 Property property = new Property(partial, 0);
 assertEquals(DateTimeFieldType.hourOfDay(), property.getField().getType());
 assertEquals(10, property.getValue());
 }

 @Test
 void testIsMatch() {
 Instant instant = Instant.parse("2022-01-01T11:30:20.500Z");
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 11, DateTimeUtils.UTC);
 assertTrue(partial.isMatch(DateTimeUtils.parseInstant(instant)));
 }

 @Test
 void testIsMatchFalse() {
 Instant instant = Instant.parse("2022-01-01T12:30:20.500Z");
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 11, DateTimeUtils.UTC);
 assertFalse(partial.isMatch(DateTimeUtils.parseInstant(instant)));
 }

 @Test
 void testIsMatchReadablePartial() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 11, DateTimeUtils.UTC);
 Partial readablePartial = new GregorianCalendar(2022, 1, 1, 11, 30, 20, 500).toPartial();
 assertTrue(partial.isMatch(readablePartial));
 }

 @Test
 void testIsMatchFalseReadablePartial() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 11, DateTimeUtils.UTC);
 Partial readablePartial = new GregorianCalendar(2022, 1, 1, 12, 30, 20, 500).toPartial();
 assertFalse(partial.isMatch(readablePartial));
 }

 @Test
 void testToString() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 assertEquals("[hourOfDay=10]", partial.toString());
 }

 @Test
 void testToStringList() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 assertEquals("[hourOfDay=10]", partial.toStringList());
 }

 @Test
 void testToStringWithFormat() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 assertEquals("10:00:00", partial.toString("HH:mm:ss"));
 }

 @Test
 void testToStringWithLocale() {
 Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10, DateTimeUtils.UTC);
 assertEquals("10:00:00", partial.toString("HH:mm:ss", Locale.US));
 }

}