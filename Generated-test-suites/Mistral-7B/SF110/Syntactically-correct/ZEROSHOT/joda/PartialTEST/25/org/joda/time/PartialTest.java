package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import java.util.*;
import org.joda.time.field.*;

@SuppressWarnings("deprecation")
public class PartialTest {

 @Test
 public void testDefaultConstructor() {
 Partial partial = new Partial();
 assertEquals(0, partial.size());
 assertNull(partial.getChronology());
 }

 @Test
 public void testNullChronology() {
 assertThrows(IllegalArgumentException.class, () -> new Partial(null));
 }

 @Test
 public void testEmptyConstructor() {
 Partial partial = new Partial();
 Partial emptyPartial = new Partial(partial);
 assertEquals(0, emptyPartial.size());
 assertArrayEquals(new int[0], emptyPartial.getValues());
 assertArrayEquals(new DateTimeFieldType[0], emptyPartial.getFieldTypes());
 assertEquals(partial.getChronology(), emptyPartial.getChronology());
 }

 @Test
 public void testSingleFieldConstructor() {
 DateTimeFieldType fieldType = ISODateField.DAY_OF_WEEK;
 Partial partial = new Partial(fieldType, 1);
 assertEquals(1, partial.size());
 assertArrayEquals(new int[]{1}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{fieldType}, partial.getFieldTypes());
 assertEquals(DateTimeUtils.UTC, partial.getChronology());
 }

 @Test
 public void testSingleFieldConstructorWithChronology() {
 DateTimeFieldType fieldType = ISODateField.DAY_OF_WEEK;
 Partial partial = new Partial(fieldType, 1, DateTimeUtils.UTC);
 assertEquals(1, partial.size());
 assertArrayEquals(new int[]{1}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{fieldType}, partial.getFieldTypes());
 assertEquals(DateTimeUtils.UTC, partial.getChronology());
 }

 @Test
 public void testMultipleFieldsConstructor() {
 DateTimeFieldType dateType = ISODateField.YEAR;
 DateTimeFieldType timeType = ISODateField.HOUR_OF_DAY;
 Partial partial = new Partial(new DateTimeFieldType[]{dateType, timeType}, new int[]{2023, 12}, DateTimeUtils.UTC);
 assertEquals(2, partial.size());
 assertArrayEquals(new int[]{2023, 12}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{dateType, timeType}, partial.getFieldTypes());
 assertEquals(DateTimeUtils.UTC, partial.getChronology());
 }

 @Test
 public void testMultipleFieldsConstructorWithChronology() {
 DateTimeFieldType dateType = ISODateField.YEAR;
 DateTimeFieldType timeType = ISODateField.HOUR_OF_DAY;
 Partial partial = new Partial(DateTimeUtils.UTC, new DateTimeFieldType[]{dateType, timeType}, new int[]{2023, 12});
 assertEquals(2, partial.size());
 assertArrayEquals(new int[]{2023, 12}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{dateType, timeType}, partial.getFieldTypes());
 assertEquals(DateTimeUtils.UTC, partial.getChronology());
 }

 @Test
 public void testCopyConstructor() {
 DateTimeFieldType fieldType = ISODateField.DAY_OF_WEEK;
 Partial originalPartial = new Partial(fieldType, 1);
 Partial copyPartial = new Partial(originalPartial);
 assertEquals(originalPartial.size(), copyPartial.size());
 assertArrayEquals(originalPartial.getValues(), copyPartial.getValues());
 assertArrayEquals(originalPartial.getFieldTypes(), copyPartial.getFieldTypes());
 assertArrayEquals(originalPartial.getChronology(), copyPartial.getChronology());
 }

 @Test
 public void testPartialWithChronologyRetainFields() {
 DateTimeFieldType fieldType = ISODateField.DAY_OF_WEEK;
 Partial originalPartial = new Partial(fieldType, 1, DateTimeUtils.UTC);
 Partial newPartial = originalPartial.withChronologyRetainFields(DateTimeUtils.ISO_8601);
 assertNotEquals(originalPartial.getChronology(), newPartial.getChronology());
 assertEquals(originalPartial.size(), newPartial.size());
 assertArrayEquals(originalPartial.getValues(), newPartial.getValues());
 assertArrayEquals(originalPartial.getFieldTypes(), newPartial.getFieldTypes());
 }

 @Test
 public void testWithField() {
 DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
 Partial partial = new Partial();
 partial = partial.withField(fieldType, 31);
 assertEquals(1, partial.size());
 assertArrayEquals(new int[]{31}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{fieldType}, partial.getFieldTypes());
 }

 @Test
 public void testWithFieldAdded() {
 DurationFieldType durationFieldType = DurationFieldType.HOURS;
 Partial partial = new Partial();
 partial = partial.withFieldAdded(durationFieldType, 1);
 assertEquals(1, partial.size());
 assertArrayEquals(new int[]{1}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{durationFieldType}, partial.getFieldTypes());
 }

 @Test
 public void testWithFieldAddWrapped() {
 DurationFieldType durationFieldType = DurationFieldType.HOURS;
 Partial partial = new Partial();
 partial = partial.withFieldAddWrapped(durationFieldType, 24);
 assertEquals(1, partial.size());
 assertArrayEquals(new int[]{24}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{durationFieldType}, partial.getFieldTypes());
 }

 @Test
 public void testWithPeriodAdded() {
 ReadablePeriod period = new GeneralInstant("2023-12-31T23:59:59.999Z").withOffsetAtEnd(DurationFieldType.HOURS, 2);
 Partial partial = new Partial();
 partial = partial.withPeriodAdded(period, 1);
 assertEquals(2, partial.size());
 assertArrayEquals(new int[]{31, 2}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{ISODateField.DAY_OF_MONTH, DurationFieldType.HOURS}, partial.getFieldTypes());
 }

 @Test
 public void testPlus() {
 ReadablePeriod period = new GeneralInstant("2023-12-31T23:59:59.999Z").withOffsetAtEnd(DurationFieldType.HOURS, 1);
 Partial partial = new Partial();
 partial = partial.plus(period);
 assertEquals(2, partial.size());
 assertArrayEquals(new int[]{31, 1}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{ISODateField.DAY_OF_MONTH, DurationFieldType.HOURS}, partial.getFieldTypes());
 }

 @Test
 public void testMinus() {
 ReadablePeriod period = new GeneralInstant("2023-12-31T23:59:59.999Z").withOffsetAtEnd(DurationFieldType.HOURS, 1);
 Partial partial = new Partial();
 partial = partial.minus(period);
 assertEquals(2, partial.size());
 assertArrayEquals(new int[]{31, -1}, partial.getValues());
 assertArrayEquals(new DateTimeFieldType[]{ISODateField.DAY_OF_MONTH, DurationFieldType.HOURS}, partial.getFieldTypes());
 }

 @Test
 public void testProperty() {
 DateTimeFieldType fieldType = ISODateField.DAY_OF_WEEK;
 Partial partial = new Partial(fieldType, 1);
 Property property = new Property(partial, 0);
 assertEquals(fieldType, property.getField());
 assertEquals(partial, property.getPartial());
 assertEquals(1, property.get());
 }

 @Test
 public void testIsMatch() {
 Instant instant = new GeneralInstant("2023-12-31T23:59:59.999Z").toInstant();
 Partial partial = new Partial(ISODateField.DAY_OF_MONTH, 31, DateTimeUtils.UTC);
 assertTrue(partial.isMatch(instant));
 }

 @Test
 public void testIsMatchWithReadablePartial() {
 Partial partial = new Partial(ISODateField.DAY_OF_MONTH, 31, DateTimeUtils.UTC);
 ReadablePartial readablePartial = new Partial(ISODateField.DAY_OF_MONTH, 31, DateTimeUtils.UTC);
 assertTrue(partial.isMatch(readablePartial));
 }

 @Test
 public void testGetFormatter() {
 Partial partial = new Partial(ISODateField.YEAR, 2023, DateTimeUtils.UTC);
 assertEquals(ISODateFormat.standard().withZoneUTC(), partial.getFormatter());
 }

 @Test
 public void testToString() {
 Partial partial = new Partial(ISODateField.YEAR, 2023, DateTimeUtils.UTC);
 assertEquals("[year=2023]", partial.toString());
 }

 @Test
 public void testToStringList() {
 Partial partial = new Partial(ISODateField.YEAR, 2023, DateTimeUtils.UTC);
 assertEquals("[year=2023]", partial.toStringList());
 }

 @Test
 public void testToStringWithLocale() {
 Partial partial = new Partial(ISODateField.YEAR, 2023, DateTimeUtils.UTC);
 assertEquals("[year=2023]", partial.toString("yyyy"));
 }

 @Test
 public void testToStringWithLocaleAndFormat() {
 Partial partial = new Partial(ISODateField.YEAR, 2023, DateTimeUtils.UTC);
 assertEquals("[year=2023]", partial.toString("yyyy", Locale.US));
 }
}