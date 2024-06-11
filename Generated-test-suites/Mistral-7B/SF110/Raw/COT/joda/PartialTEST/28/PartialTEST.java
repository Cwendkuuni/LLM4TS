import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.Chronology;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.junit.Test;

public class PartialTest {

    

    @Test
    public void testEmptyPartialConstructor() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
    }

    @Test
    public void testPartialWithNullChronology() {
        try {
            new Partial(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithValidChronology() {
        Partial partial = new Partial(ISOChronology.getInstanceUTC());
        assertEquals(0, partial.size());
    }

    @Test
    public void testPartialWithDateTimeFieldTypeAndValue() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldTypes()[0]);
        assertEquals(2022, partial.getValue(0));
    }

    @Test
    public void testPartialWithDateTimeFieldTypeArrayAndValueArray() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        int[] values = {2022, 1};
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, values);
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldTypes()[0]);
        assertEquals(2022, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldTypes()[1]);
        assertEquals(1, partial.getValue(1));
    }

    @Test
    public void testPartialWithReadablePartial() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial1 = new Partial(dateTimeFieldType, 2022);
        ReadablePartial readablePartial = new Partial(partial1);
        assertEquals(1, readablePartial.size());
        assertEquals(dateTimeFieldType, readablePartial.getFieldType(0));
        assertEquals(2022, readablePartial.getValue(0));
    }

    @Test
    public void testSize() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 2022, 1 });
        assertEquals(2, partial.size());
    }

    @Test
    public void testGetChronology() {
        Partial partial = new Partial(ISOChronology.getInstanceUTC());
        assertEquals(ISOChronology.getInstanceUTC(), partial.getChronology());
    }

    @Test
    public void testGetField() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        DateTimeField field = partial.getField(0, null);
        assertEquals(dateTimeFieldType.getField(null), field);
    }

    @Test
    public void testGetFieldType() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
    }

    @Test
    public void testGetFieldTypes() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 2022, 1 });
        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
        assertEquals(2, fieldTypes.length);
        assertEquals(dateTimeFieldType1, fieldTypes[0]);
        assertEquals(dateTimeFieldType2, fieldTypes[1]);
    }

    @Test
    public void testGetValue() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        assertEquals(2022, partial.getValue(0));
    }

    @Test
    public void testGetValues() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 2022, 1 });
        int[] values = partial.getValues();
        assertEquals(2, values.length);
        assertEquals(2022, values[0]);
        assertEquals(1, values[1]);
    }

    @Test
    public void testWithChronologyRetainFields() {
        Partial partial = new Partial(ISOChronology.getInstanceUTC());
        partial = partial.withChronologyRetainFields(UTCChronology.getInstanceUTC());
        assertEquals(UTCChronology.getInstanceUTC(), partial.getChronology());
        assertEquals(1, partial.size());
    }

    @Test
    public void testWith() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        DateTimeFieldType newDateTimeFieldType = DateTimeFieldType.monthOfYear();
        int newValue = 1;
        Partial newPartial = partial.with(newDateTimeFieldType, newValue);
        assertEquals(2, newPartial.size());
        assertEquals(newDateTimeFieldType, newPartial.getFieldTypes()[1]);
        assertEquals(newValue, newPartial.getValue(1));
    }

    @Test
    public void testWithout() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType, dateTimeFieldType2 }, new int[] { 2022, 1 });
        Partial newPartial = partial.without(dateTimeFieldType);
        assertEquals(1, newPartial.size());
        assertNotSame(dateTimeFieldType, newPartial.getFieldTypes()[0]);
    }

    @Test
    public void testWithField() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        DateTimeFieldType newDateTimeFieldType = DateTimeFieldType.monthOfYear();
        int newValue = 1;
        Partial newPartial = partial.withField(newDateTimeFieldType, newValue);
        assertEquals(2, newPartial.size());
        assertEquals(newDateTimeFieldType, newPartial.getFieldTypes()[1]);
        assertEquals(newValue, newPartial.getValue(1));
    }

    @Test
    public void testWithFieldAdded() {
        DateTimeFieldType durationFieldType = DurationFieldType.days();
        Partial partial = new Partial(ISOChronology.getInstanceUTC());
        Partial newPartial = partial.withFieldAdded(durationFieldType, 2);
        DateTimeField newField = newPartial.getField(newPartial.size() - 1, null);
        assertEquals(durationFieldType.getField(null), newField);
        assertEquals(2, newPartial.getValue(newPartial.size() - 1));
    }

    @Test
    public void testWithFieldAddWrapped() {
        DateTimeFieldType durationFieldType = DurationFieldType.days();
        Partial partial = new Partial(ISOChronology.getInstanceUTC());
        Partial newPartial = partial.withFieldAddWrapped(durationFieldType, 365);
        DateTimeField newField = newPartial.getField(newPartial.size() - 1, null);
        assertEquals(durationFieldType.getField(null), newField);
        assertEquals(365, newPartial.getValue(newPartial.size() - 1));
    }

    @Test
    public void testWithPeriodAdded() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        Period period = new Period(1, 1, 1);
        Partial newPartial = partial.withPeriodAdded(period, 3);
        assertEquals(3, newPartial.size());
        assertEquals(dateTimeFieldType, newPartial.getFieldTypes()[0]);
        assertEquals(2025, newPartial.getValue(0));
    }

    @Test
    public void testPlus() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        Period period = new Period(1, 1, 1);
        Partial newPartial = partial.plus(period);
        assertEquals(1, newPartial.size());
        assertEquals(dateTimeFieldType, newPartial.getFieldTypes()[0]);
        assertEquals(2023, newPartial.getValue(0));
    }

    @Test
    public void testMinus() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2023);
        Period period = new Period(1, 1, 1);
        Partial newPartial = partial.minus(period);
        assertEquals(1, newPartial.size());
        assertEquals(dateTimeFieldType, newPartial.getFieldTypes()[0]);
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testProperty() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        Property property = new Property(partial, 0);
        assertEquals(property.getField(), partial.getField(0));
        assertEquals(property.getPartial(), partial);
        assertEquals(2022, property.get());
    }

    @Test
    public void testIsMatch() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).toInstant();
        assertTrue(partial.isMatch(new InstantMillis(instant.toEpochMilli())));
    }

    @Test
    public void testIsMatchWithReadablePartial() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        ReadableInstant readableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0).toInstant();
        ReadablePartial readablePartial = new DateTime(2022, 1, 1, 0, 0, 0, 0).toDateTime();
        assertTrue(partial.isMatch(readablePartial));
    }

    @Test
    public void testGetFormatter() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 2022, 1 });
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testToString() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        String toString = partial.toString();
        String expected = "[year=2022]";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringList() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 2022, 1 });
        String toStringList = partial.toStringList();
        String expected = "[year=2022, monthOfYear=1]";
        assertEquals(expected, toStringList);
    }

    @Test
    public void testToStringWithString() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        String toString = partial.toString("yyyy");
        String expected = "2022";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringWithStringAndLocale() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        String toString = partial.toString("yyyy", Locale.US);
        String expected = "2022";
        assertEquals(expected, toString);
    }

    
}