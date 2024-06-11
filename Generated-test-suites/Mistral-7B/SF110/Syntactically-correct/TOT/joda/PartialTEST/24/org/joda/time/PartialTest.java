package org.joda.time.test;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.calendar.*;
import org.joda.time.field.*;
import org.joda.time.format.*;

public class PartialTest {

 private static final Chronology UTC = DateTimeUtils.UTC;

 @Test
 public void testEmptyPartial() {
     Partial emptyPartial = new Partial();
     assertEquals(0, emptyPartial.size());
     assertTrue(emptyPartial.isMatch(DateTime.now(UTC)));
 }

 @Test
 public void testSingleFieldPartial() {
     DateTimeFieldType yearType = ISODateField.year();
     Partial singleFieldPartial = new Partial(yearType, 2021);
     assertEquals(1, singleFieldPartial.size());
     assertEquals(2021, singleFieldPartial.getValue(0));
     assertEquals(Year.getInstance(2021), singleFieldPartial.getField(0));
     assertEquals(UTC, singleFieldPartial.getChronology());
     assertTrue(singleFieldPartial.isMatch(new DateTime(2021, 1, 1, UTC)));
     assertFalse(singleFieldPartial.isMatch(new DateTime(2020, 1, 1, UTC)));
 }

 @Test
 public void testMultiFieldPartial() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     Partial multiFieldPartial = new Partial(yearType, 2021, monthType, 1, dayOfMonthType, 15);
     assertEquals(3, multiFieldPartial.size());
     assertEquals(2021, multiFieldPartial.getValue(0));
     assertEquals(Month.JANUARY, multiFieldPartial.getValue(1));
     assertEquals(15, multiFieldPartial.getValue(2));
     assertEquals(Year.getInstance(2021), multiFieldPartial.getField(0));
     assertEquals(Months.JANUARY, multiFieldPartial.getField(1));
     assertEquals(DayOfMonth.FIFTEENTH_OF_JANUARY, multiFieldPartial.getField(2));
     assertEquals(UTC, multiFieldPartial.getChronology());
     assertTrue(multiFieldPartial.isMatch(new DateTime(2021, 1, 15, UTC)));
     assertFalse(multiFieldPartial.isMatch(new DateTime(2021, 2, 15, UTC)));
 }

 @Test
 public void testWithChronologyRetainFields() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     Partial partial = new Partial(yearType, 2021, monthType, 1, dayOfMonthType, 15, UTC);
     Partial withChronologyRetainFields = partial.withChronologyRetainFields(GregorianChronology.getInstance());
     assertEquals(partial, withChronologyRetainFields);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypesArray() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType invalidType = null;
     try {
         new Partial(yearType, monthType, dayOfMonthType, invalidType, 1);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesArray() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     int[] invalidValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
     try {
         new Partial(yearType, monthType, dayOfMonthType, new int[15], invalidValues);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypesArrayLength() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType invalidType2 = ISODateField.dayOfWeek();
     try {
         new Partial(yearType, monthType, dayOfMonthType, new DateTimeFieldType[]{yearType, monthType, dayOfMonthType, invalidType2}, new int[3]);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesArrayLength() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType[] types = new DateTimeFieldType[]{yearType, monthType, dayOfMonthType};
     int[] invalidValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
     try {
         new Partial(types, invalidValues);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesArrayNotSameLengthAsTypes() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType[] types = new DateTimeFieldType[]{yearType, monthType, dayOfMonthType};
     int[] invalidValues = {1, 2, 3, 4, 5, 6, 7, 8};
     try {
         new Partial(types, invalidValues);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypesArrayDuplicate() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType duplicateType = ISODateField.year();
     try {
         new Partial(yearType, monthType, dayOfMonthType, new DateTimeFieldType[]{yearType, monthType, dayOfMonthType, duplicateType}, new int[3]);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypesArrayOutOfOrder() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType dayOfWeekType = ISODateField.dayOfWeek();
     try {
         new Partial(yearType, dayOfWeekType, monthType, dayOfMonthType, new int[3]);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRangeDurationTypeNull() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType dayOfWeekType = ISODateField.dayOfWeek();
     try {
         new Partial(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType, dayOfWeekType}, new int[3], null);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRangeDurationType() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     DateTimeFieldType dayOfWeekType = ISODateField.dayOfWeek();
     DateTimeFieldType invalidDayOfWeekType = DayOfWeek.SATURDAY;
     try {
         new Partial(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType, dayOfWeekType, invalidDayOfWeekType}, new int[3]);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesArrayDuplicate() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     try {
         new Partial(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType}, new int[]{1, 1, 1});
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValuesArrayNotInOrder() {
     DateTimeFieldType yearType = ISODateField.year();
     DateTimeFieldType monthType = ISODateField.monthOfYear();
     DateTimeFieldType dayOfMonthType = ISODateField.dayOfMonth();
     int[] invalidValues = {1, 2, 3, 3, 2, 1};
     try {
         new Partial(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType}, invalidValues);
         fail();
     } catch (IllegalArgumentException ex) {
         // Expected
     }
 }

}