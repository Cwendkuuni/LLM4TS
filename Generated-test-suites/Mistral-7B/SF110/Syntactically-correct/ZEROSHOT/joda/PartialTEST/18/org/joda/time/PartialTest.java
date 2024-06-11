package test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNull(partial.getChronology());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology = ISOChronology.getInstance();
        Partial partial = new Partial(chronology);
        assertEquals(0, partial.size());
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testSingleFieldPartial() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.YEAR;
        Partial partial = new Partial(dateTimeFieldType, 2019, chronology);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2019, partial.getValue(0));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testMultipleFieldsPartial() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODateField.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODateField.MONTH_OF_YEAR;
        int[] values = {2019, 12};
        Partial partial = new Partial(new DateTimeFieldType[] {dateTimeFieldType1, dateTimeFieldType2}, values, chronology);
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(2019, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(12, partial.getValue(1));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithValues() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        int[] values = {2019, 12};
        Partial partial = new Partial(types, values, chronology);
        assertEquals(2, partial.size());
        assertEquals(types[0], partial.getFieldType(0));
        assertEquals(2019, partial.getValue(0));
        assertEquals(types[1], partial.getFieldType(1));
        assertEquals(12, partial.getValue(1));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithReadablePartial() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        int[] values = {2019, 12};
        Partial partial1 = new Partial(types, values, chronology);
        Partial partial2 = new Partial(chronology, 2019, ISODateField.YEAR, 12, ISODateField.MONTH_OF_YEAR);
        Partial partial3 = new Partial(partial1);
        Partial partial4 = new Partial(partial2);
        Partial partial5 = new Partial(partial3, partial2.getValues());
        assertEquals(2, partial1.size());
        assertEquals(2, partial2.size());
        assertEquals(partial1, partial3);
        assertEquals(partial1, partial4);
        assertEquals(partial1, partial5);
    }

    @Test
    public void testPartialWithChronologyRetainFields() {
        Chronology chronology1 = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        int[] values = {2019, 12};
        Partial partial1 = new Partial(types, values, chronology1);
        Chronology chronology2 = GregorianChronology.getInstance();
        Partial partial2 = partial1.withChronologyRetainFields(chronology2);
        assertEquals(2, partial1.size());
        assertEquals(2, partial2.size());
        assertNotSame(chronology1, partial2.getChronology());
        assertEquals(partial1, partial2.getPartial());
    }

    @Test
    public void testPartialWithField() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.YEAR;
        Partial partial = new Partial(chronology);
        Partial partialWithField = partial.with(dateTimeFieldType, 2019);
        assertEquals(1, partial.size());
        assertEquals(1, partialWithField.size());
        assertEquals(dateTimeFieldType, partialWithField.getFieldType(0));
        assertEquals(2019, partialWithField.getValue(0));
        assertEquals(chronology, partialWithField.getChronology());
    }

    @Test
    public void testPartialWithFieldAdded() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.YEAR;
        Partial partial = new Partial(chronology);
        Partial partialWithFieldAdded = partial.withFieldAdded(dateTimeFieldType, 2);
        assertEquals(1, partial.size());
        assertEquals(2, partialWithFieldAdded.size());
        assertEquals(dateTimeFieldType, partialWithFieldAdded.getFieldType(1));
        assertEquals(0, partialWithFieldAdded.getValue(0));
        assertEquals(2, partialWithFieldAdded.getValue(1));
        assertEquals(chronology, partialWithFieldAdded.getChronology());
    }

    @Test
    public void testPartialWithFieldAddWrapped() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.YEAR;
        Partial partial = new Partial(chronology);
        Partial partialWithFieldAddWrapped = partial.withFieldAddWrapped(dateTimeFieldType, 2);
        assertEquals(1, partial.size());
        assertEquals(2, partialWithFieldAddWrapped.size());
        assertEquals(dateTimeFieldType, partialWithFieldAddWrapped.getFieldType(1));
        assertEquals(2, partialWithFieldAddWrapped.getValue(0));
        assertEquals(2, partialWithFieldAddWrapped.getValue(1));
        assertEquals(chronology, partialWithFieldAddWrapped.getChronology());
    }

    @Test
    public void testPartialWithPeriodAdded() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = ISODateField.MONTH_OF_YEAR;
        Partial partial = new Partial(chronology);
        ReadablePeriod readablePeriod = new GeneralDate("2019-12-31").toPeriod();
        Partial partialWithPeriodAdded = partial.withPeriodAdded(readablePeriod, 1);
        assertEquals(1, partial.size());
        assertEquals(2, partialWithPeriodAdded.size());
        assertEquals(dateTimeFieldType1, partialWithPeriodAdded.getFieldType(1));
        assertEquals(31, partialWithPeriodAdded.getValue(1));
        assertEquals(dateTimeFieldType2, partialWithPeriodAdded.getFieldType(2));
        assertEquals(12, partialWithPeriodAdded.getValue(2));
        assertEquals(chronology, partialWithPeriodAdded.getChronology());
    }

    @Test
    public void testPartialPlus() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(chronology);
        ReadablePeriod readablePeriod = new GeneralDate("2019-12-31").toPeriod();
        Partial partialPlus = partial.plus(readablePeriod);
        assertEquals(0, partial.size());
        assertEquals(2, partialPlus.size());
        assertEquals(dateTimeFieldType, partialPlus.getFieldType(1));
        assertEquals(31, partialPlus.getValue(1));
        assertEquals(chronology, partialPlus.getChronology());
    }

    @Test
    public void testPartialMinus() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(chronology);
        ReadablePeriod readablePeriod = new GeneralDate("2019-12-31").toPeriod();
        Partial partialMinus = partial.minus(readablePeriod);
        assertEquals(0, partial.size());
        assertEquals(2, partialMinus.size());
        assertEquals(dateTimeFieldType, partialMinus.getFieldType(1));
        assertEquals(0, partialMinus.getValue(1));
        assertEquals(chronology, partialMinus.getChronology());
    }

    @Test
    public void testProperty() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.YEAR;
        Partial partial = new Partial(chronology);
        Property property = partial.property(dateTimeFieldType);
        assertEquals(dateTimeFieldType, property.getField());
        assertEquals(partial, property.getPartial());
        assertEquals(2019, property.get());
        assertEquals(partial, property.getReadablePartial());
    }

    @Test
    public void testIsMatch() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODateField.YEAR;
        Partial partial = new Partial(new DateTimeFieldType[] {dateTimeFieldType}, new int[] {2019}, chronology);
        Instant instant = new DateTime(2019, 11, 30, 12, 0).toInstant();
        Instant instant2 = new DateTime(2019, 12, 31, 12, 0).toInstant();
        Instant instant3 = new DateTime(2020, 1, 1, 12, 0).toInstant();
        assertTrue(partial.isMatch(new DateTime(instant)));
        assertTrue(partial.isMatch(new DateTime(instant2)));
        assertFalse(partial.isMatch(new DateTime(instant3)));
    }

    @Test
    public void testGetFormatter() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        Partial partial = new Partial(types, new int[0], chronology);
        assertEquals(null, partial.getFormatter());
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testToString() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        int[] values = {2019, 12};
        Partial partial = new Partial(types, values, chronology);
        String toString = partial.toString();
        String toStringList = partial.toStringList();
        assertEquals("[year=2019 month_of_year=12]", toString);
        assertEquals("[year=2019 month_of_year=12]", toStringList);
    }

    @Test
    public void testToStringWithFormat() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        int[] values = {2019, 12};
        Partial partial = new Partial(types, values, chronology);
        String toString = partial.toString("yyyy-MM");
        assertEquals("2019-12", toString);
    }

    @Test
    public void testToStringWithLocale() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {ISODateField.YEAR, ISODateField.MONTH_OF_YEAR};
        int[] values = {2019, 12};
        Partial partial = new Partial(types, values, chronology);
        String toString = partial.toString("yyyy-MM", Locale.FRENCH);
        assertEquals("2019-12", toString);
    }
}