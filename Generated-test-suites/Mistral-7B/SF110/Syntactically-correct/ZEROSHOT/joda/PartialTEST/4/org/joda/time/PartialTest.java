import org.joda.time.*;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.Chronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testPartial() {
        // Test empty Partial
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
        assertArrayEquals(new int[0], emptyPartial.getValues());

        // Test Partial with one field
        DateTimeFieldType dateType = ISODateField.DAY_OF_MONTH;
        int value = 1;
        Partial partial = new Partial(dateType, value);
        assertEquals(1, partial.size());
        assertEquals(DateTimeUtils.UTC, partial.getChronology());
        assertArrayEquals(new int[]{value}, partial.getValues());
        assertEquals(dateType, partial.getFieldTypes()[0]);
        assertEquals(value, partial.getValue(0));

        // Test Partial with multiple fields
        DateTimeFieldType dateType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType timeType1 = ISODateTimeField.HOUR_OF_DAY;
        DateTimeFieldType dateType2 = ISODateField.MONTH_OF_YEAR;
        DateTimeFieldType timeType2 = ISODateTimeField.MINUTE_OF_HOUR;
        int[] values = {1, 2, 3, 4};
        Partial partial1 = new Partial(new DateTimeFieldType[]{dateType1, timeType1, dateType2, timeType2}, values);
        assertEquals(4, partial1.size());
        assertEquals(DateTimeUtils.UTC, partial1.getChronology());
        assertArrayEquals(values, partial1.getValues());
        assertEquals(dateType1, partial1.getFieldTypes()[0]);
        assertEquals(dateType2, partial1.getFieldTypes()[2]);
        assertEquals(values[0], partial1.getValue(0));
        assertEquals(values[2], partial1.getValue(2));
        
        // Test Partial with wrong values
        try {
            new Partial(null, 1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, values);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(new DateTimeFieldType[0], values);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, values, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, values, new Chronology[]{});
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, values, new Chronology[]{DateTimeUtils.UTC, DateTimeUtils.UTC});
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, new DateTimeFieldType[0], values);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            new Partial(dateType1, new DateTimeFieldType[0], values, DateTimeUtils.UTC);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        // Test Partial with Chronology
        Chronology chrono = GregorianChronology.getInstance();
        Partial partial2 = new Partial(chrono);
        assertEquals(0, partial2.size());
        assertEquals(chrono, partial2.getChronology());
        assertArrayEquals(new int[0], partial2.getValues());

        // Test Partial with invalid Chronology
        try {
            new Partial(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        // Test Partial with different Chronology
        Chronology chrono2 = ISOChronology.getInstance();
        Partial partial3 = new Partial(partial2);
        assertEquals(4, partial1.size());
        assertNotSame(partial2.getChronology(), partial3.getChronology());
        assertArrayEquals(partial1.getValues(), partial3.getValues());

        // Test Partial with same Chronology but different values
        Partial partial4 = new Partial(partial1);
        assertArrayEquals(partial1.getValues(), partial4.getValues());
        partial4.setValue(0, 2);
        assertNotSame(partial1.getValues(), partial4.getValues());

        // Test Partial with same Chronology and same values but different order
        Partial partial5 = new Partial(new DateTimeFieldType[]{timeType2, dateType1, dateType2, timeType1}, values);
        assertArrayEquals(partial1.getValues(), partial5.getValues());

        // Test Partial with invalid index
        try {
            partial1.getValue(-1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {}

        try {
            partial1.getValue(4);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {}

        // Test Partial with getField
        DateTimeField field = partial1.getField(0);
        assertEquals(dateType1, field);
        assertEquals(1, field.get(partial1, 0));

        field = partial1.getField(3);
        assertEquals(timeType2, field);
        assertEquals(4, field.get(partial1, 3));

        // Test Partial with getFieldTypes
        DateTimeFieldType[] fieldTypes = partial1.getFieldTypes();
        assertArrayEquals(new DateTimeFieldType[]{dateType1, timeType1, dateType2, timeType2}, fieldTypes);

        // Test Partial with getValues
        int[] values1 = partial1.getValues();
        assertArrayEquals(values, values1);

        // Test Partial with setValue
        partial1.setValue(0, 3);
        assertNotSame(values, partial1.getValues());
        assertEquals(3, partial1.getValue(0));

        // Test Partial with setValues
        int[] values2 = new int[]{1, 1, 3, 4};
        partial1.setValues(values2);
        assertNotSame(values, partial1.getValues());
        assertArrayEquals(values2, partial1.getValues());

        // Test Partial with toString
        assertEquals("[" + dateType1.getName() + "=1, " + timeType1.getName() + "=2, " + dateType2.getName() + "=3, " + timeType2.getName() + "=4]", partial1.toString());
        assertEquals("[dayOfMonth=1, hourOfDay=2, monthOfYear=3, minuteOfHour=4]", partial1.toStringList());

        // Test Partial with getFormatter
        DateTimeFormatter formatter = partial1.getFormatter();
        assertEquals("[dayOfMonth=1, hourOfDay=2, monthOfYear=3, minuteOfHour=4]", formatter.print(partial1));

        // Test Partial with equals
        Partial partial6 = new Partial(partial1);
        assertTrue(partial1.equals(partial6));
        assertFalse(partial1.equals(null));
        assertFalse(partial1.equals(""));

        // Test Partial with hashCode
        int hashCode1 = partial1.hashCode();
        Partial partial7 = new Partial(partial1);
        int hashCode2 = partial7.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test Partial with clone
        Partial partial8 = partial1.clone();
        assertEquals(partial1, partial8);

        // Test Partial with withChronology
        Partial partial9 = partial1.withChronology(ISOChronology.getInstance());
        assertEquals(partial1.getChronology(), DateTimeUtils.UTC);
        assertEquals(ISOChronology.getInstance(), partial9.getChronology());

        // Test Partial with with
        Partial partial10 = partial1.with(dateType1, 5);
        assertNotSame(partial1.getValues(), partial10.getValues());
        assertEquals(5, partial10.getValue(0));

        // Test Partial with without
        Partial partial11 = partial1.without(timeType2);
        assertNotSame(partial1.getValues(), partial11.getValues());
        assertArrayEquals(new int[]{1, 2, 3}, partial11.getValues());

        // Test Partial with withField
        Partial partial12 = partial1.withField(dateType2, 1);
        assertNotSame(partial1.getValues(), partial12.getValues());
        assertEquals(1, partial12.getValue(2));

        // Test Partial with withFieldAdded
        Partial partial13 = partial1.withFieldAdded(ISODateTimeField.SECOND_OF_MINUTE, 30);
        assertNotSame(partial1.getValues(), partial13.getValues());
        assertEquals(30, partial13.getValue(3));

        // Test Partial with withFieldAddWrapped
        Partial partial14 = partial1.withFieldAddWrapped(ISODateTimeField.SECOND_OF_MINUTE, 60);
        assertNotSame(partial1.getValues(), partial14.getValues());
        assertEquals(1, partial14.getValue(3));

        // Test Partial with withPeriodAdded
        ReadablePeriod period = new GeneralDatePeriod(new DateTime(2022, 2, 1), new DateTime(2022, 2, 2));
        Partial partial15 = partial1.withPeriodAdded(period, 1);
        assertNotSame(partial1.getValues(), partial15.getValues());
        assertEquals(new DateTime(2022, 2, 3, 2, 2, 0, 0).getMillis(), partial15.getInstant().getMillis());

        // Test Partial with plus
        Partial partial16 = partial1.plus(period);
        assertEquals(partial15, partial16);

        // Test Partial with minus
        Partial partial17 = partial15.minus(period);
        assertEquals(partial1, partial17);

        // Test Property
        Property property = partial1.property(dateType1);
        assertEquals(dateType1, property.getField());
        assertEquals(1, property.get());
        Partial partial18 = property.setCopy(2);
        assertNotSame(partial1, partial18);
        assertEquals(2, partial18.getValue(0));
    }
}