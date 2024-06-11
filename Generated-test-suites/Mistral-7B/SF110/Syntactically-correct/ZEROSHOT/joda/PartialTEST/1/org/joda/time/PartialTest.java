package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertTrue(emptyPartial.isMatch(new LocalDateTime()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChronology() {
        new Partial(null);
    }

    @Test
    public void testPartialWithSingleField() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial singleFieldPartial = new Partial(DateTimeFieldType.DAY_OF_MONTH, 12, chronology);
        assertEquals(1, singleFieldPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, singleFieldPartial.getFieldType(0));
        assertEquals(12, singleFieldPartial.getValue(0));
        assertEquals(12, singleFieldPartial.getValues()[0]);
        assertEquals("dayOfMonth=12", singleFieldPartial.toString());
        assertTrue(singleFieldPartial.isMatch(new LocalDateTime().withDayOfMonth(12)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFieldType() {
        new Partial(null, 12);
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial multipleFieldsPartial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        assertEquals(2, multipleFieldsPartial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, multipleFieldsPartial.getFieldType(0));
        assertEquals(12, multipleFieldsPartial.getValue(0));
        assertEquals(12, multipleFieldsPartial.getValues()[0]);
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, multipleFieldsPartial.getFieldType(1));
        assertEquals(11, multipleFieldsPartial.getValue(1));
        assertEquals(11, multipleFieldsPartial.getValues()[1]);
        assertEquals("dayOfMonth=12,monthOfYear=11", multipleFieldsPartial.toString());
        assertTrue(multipleFieldsPartial.isMatch(new LocalDateTime().withDayOfMonth(12).withMonthOfYear(11)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuesArray() {
        new Partial(DateTimeFieldType.DAY_OF_MONTH, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedArrayLengths() {
        new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldTypeOrder() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        new Partial(new DateTimeFieldType[]{DateTimeFieldType.MONTH_OF_YEAR, DateTimeFieldType.DAY_OF_MONTH}, new int[]{1, 2}, chronology);
    }

    @Test
    public void testCloning() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial originalPartial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        Partial clonedPartial = new Partial(originalPartial);
        assertEquals(originalPartial, clonedPartial);
        assertEquals(originalPartial.hashCode(), clonedPartial.hashCode());
        clonedPartial.setValue(0, 13);
        assertNotEquals(originalPartial, clonedPartial);
        assertNotEquals(originalPartial.hashCode(), clonedPartial.hashCode());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology newChronology = DateTimeUtils.forLocale(Locale.US);
        Partial emptyPartial = new Partial();
        Partial newPartial = new Partial(newDateTimeFieldTypes(), intValues(), newChronology);
        assertEquals(newChronology, newPartial.getChronology());
    }

    @Test
    public void testPartialWithReadablePartial() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial readablePartial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        Partial partialFromReadablePartial = new Partial(readablePartial);
        assertEquals(readablePartial, partialFromReadablePartial);
    }

    @Test
    public void testPartialWithValues() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partialWithValues = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11});
        assertEquals(2, partialWithValues.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, partialWithValues.getFieldType(0));
        assertEquals(12, partialWithValues.getValue(0));
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, partialWithValues.getFieldType(1));
        assertEquals(11, partialWithValues.getValue(1));
    }

    @Test
    public void testPartialWithFieldTypesAndValues() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11});
        assertEquals(2, partial.size());
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, partial.getFieldType(0));
        assertEquals(12, partial.getValue(0));
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR, partial.getFieldType(1));
        assertEquals(11, partial.getValue(1));
    }

    @Test
    public void testGetFormatter() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
        String formattedString = formatter.print(partial);
        assertEquals("12-11", formattedString);
    }

    @Test
    public void testToString() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        assertEquals("dayOfMonth=12,monthOfYear=11", partial.toString());
    }

    @Test
    public void testToStringList() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        assertEquals("[dayOfMonth=12,monthOfYear=11]", partial.toStringList());
    }

    @Test
    public void testToStringWithFormat() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        String format = "dd-MM";
        assertEquals("12-11", partial.toString(format));
    }

    @Test
    public void testToStringWithLocale() {
        Chronology chronology = DateTimeUtils.forLocale(Locale.US);
        Partial partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR}, new int[]{12, 11}, chronology);
        Locale locale = Locale.CANADA;
        assertEquals("12-11", partial.toString(format, locale));
    }

    private DateTimeFieldType[] newDateTimeFieldTypes() {
        return new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.MONTH_OF_YEAR};
    }

    private int[] intValues() {
        return new int[]{12, 11};
    }
}