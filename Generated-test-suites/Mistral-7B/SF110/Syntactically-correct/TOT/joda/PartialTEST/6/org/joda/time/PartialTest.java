package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PartialTest {

 private static final Chronology UTC = DateTimeUtils.UTC;

 @Test
 public void testEmptyPartial() {
 Partial emptyPartial = new Partial();
 assertEquals(0, emptyPartial.size());
 assertTrue(emptyPartial.isMatch(new BaseDateTime(0L, UTC)));
 }

 @Test
 public void testSingleFieldPartial() {
 DateTimeFieldType yearField = DateTimeFieldType.year();
 int yearValue = 2022;
 Partial singleFieldPartial = new Partial(yearField, yearValue);
 assertEquals(1, singleFieldPartial.size());
 assertTrue(singleFieldPartial.isMatch(new BaseDateTime(2022L, UTC)));
 }

 @Test
 public void testMultipleFieldsPartial() {
 DateTimeFieldType yearField = DateTimeFieldType.year();
 DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
 DateTimeFieldType dayOfMonthField = DateTimeFieldType.dayOfMonth();
 int[] values = {2022, 11, 30};
 Partial multipleFieldsPartial = new Partial(yearField, monthField, dayOfMonthField, values);
 assertEquals(3, multipleFieldsPartial.size());
 assertTrue(multipleFieldsPartial.isMatch(new LocalDateTime(2022, 11, 30, UTC)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFieldType() {
 DateTimeFieldType invalidFieldType = null;
 Partial partial = new Partial(invalidFieldType, 1);
 fail("Should throw IllegalArgumentException for null field type");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesLength() {
 DateTimeFieldType[] fieldTypes = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};
 int[] values = {1, 2, 3, 4};
 Partial partial = new Partial(fieldTypes, values);
 fail("Should throw IllegalArgumentException for values array length not equal to fieldTypes array length");
 }

 @Test
 public void testSameFieldTypeMultipleTimes() {
 DateTimeFieldType yearField = DateTimeFieldType.year();
 int[] values = {2022, 2023, 2024};
 Partial partial = new Partial(yearField, values);
 assertEquals(3, partial.size());
 assertTrue(partial.isMatch(new BaseDateTime(2022L, UTC)));
 assertTrue(partial.isMatch(new BaseDateTime(2023L, UTC)));
 assertTrue(partial.isMatch(new BaseDateTime(2024L, UTC)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFieldOrder() {
 DateTimeFieldType dayField = DateTimeFieldType.dayOfMonth();
 DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
 DateTimeFieldType yearField = DateTimeFieldType.year();
 int[] values = {2022, 2, 31};
 Partial partial = new Partial(yearField, monthField, dayField, values);
 fail("Should throw IllegalArgumentException for fields not in order: dayOfMonth > monthOfYear > year");
 }

 @Test
 public void testGetValues() {
 Partial partial = new Partial(DateTimeFieldType.year(), 2022);
 int[] values = partial.getValues();
 assertEquals(1, values.length);
 assertEquals(2022, values[0]);
 }

 @Test
 public void testGetValuesForMultipleFields() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 int[] values = partial.getValues();
 assertEquals(3, values.length);
 assertEquals(2022, values[0]);
 assertEquals(11, values[1]);
 assertEquals(30, values[2]);
 }

 @Test
 public void testGetFieldTypes() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
 assertEquals(3, fieldTypes.length);
 assertEquals(DateTimeFieldType.year(), fieldTypes[0]);
 assertEquals(DateTimeFieldType.monthOfYear(), fieldTypes[1]);
 assertEquals(DateTimeFieldType.dayOfMonth(), fieldTypes[2]);
 }

 @Test
 public void testToString() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 assertEquals("[year=2022, monthOfYear=11, dayOfMonth=30]", partial.toString());
 }

 @Test
 public void testSetValues() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 Partial newPartial = partial.with(DateTimeFieldType.year(), 2023);
 assertEquals(3, newPartial.size());
 assertEquals(2023, newPartial.getValue(0));
 }

 @Test
 public void testAddField() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 Partial newPartial = partial.withFieldAdded(DateTimeFieldType.dayOfWeek(), 1);
 assertEquals(4, newPartial.size());
 assertEquals(1, newPartial.getValue(3));
 }

 @Test
 public void testAddWrappedField() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.dayOfWeek(), 1);
 assertEquals(4, newPartial.size());
 assertEquals(2, newPartial.getValue(3));
 }

 @Test
 public void testSetField() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 Partial newPartial = partial.withField(DateTimeFieldType.year(), 2023);
 assertEquals(3, newPartial.size());
 assertEquals(2023, newPartial.getValue(0));
 }

 @Test
 public void testAddPeriod() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 ReadablePeriod period = new Period(1, 1, 1);
 Partial newPartial = partial.withPeriodAdded(period, 1);
 assertEquals(3, newPartial.size());
 assertEquals(2023, newPartial.getValue(0));
 assertEquals(12, newPartial.getValue(1));
 assertEquals(31, newPartial.getValue(2));
 }

 @Test
 public void testToStringFormat() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
 String formatted = formatter.print(partial);
 assertEquals("2022-11-30", formatted);
 }

 @Test
 public void testToStringFormatLocale() {
 Partial partial = new Partial(DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), new int[]{2022, 11, 30});
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.FRENCH);
 String formatted = formatter.print(partial);
 assertEquals("2022-11-30", formatted);
 }

}