package org.joda.time;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology = ISOChronology.getInstance();
        Partial partial = new Partial(chronology);
        assertEquals(0, partial.size());
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithOneField() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 },
                new int[] { 2022, 1 }, chronology);
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(1, partial.getValue(1));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithInvalidFields() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.millisecondOfSecond();
        try {
            new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 1 });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            new Partial(new DateTimeFieldType[] { null }, new int[] { 1 });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[1], null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialOrdering() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.millisecondOfSecond();
        Partial partial1 = new Partial(new DateTimeFieldType[] { dateTimeFieldType1 }, new int[] { 2022 },
                chronology);
        Partial partial2 = new Partial(new DateTimeFieldType[] { dateTimeFieldType2 }, new int[1],
                chronology);
        int compareTo = partial1.compareTo(partial2);
        assertTrue(compareTo < 0);
        Partial partial3 = new Partial(new DateTimeFieldType[] { dateTimeFieldType2 }, new int[1],
                chronology);
        compareTo = partial1.compareTo(partial3);
        assertTrue(compareTo < 0);
        Partial partial4 = new Partial(new DateTimeFieldType[] { dateTimeFieldType1 }, new int[1],
                chronology);
        compareTo = partial1.compareTo(partial4);
        assertTrue(compareTo > 0);
    }

    @Test
    public void testPartialClone() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        Partial clonedPartial = new Partial(partial);
        assertEquals(partial, clonedPartial);
        assertEquals(partial.size(), clonedPartial.size());
        assertEquals(partial.getChronology(), clonedPartial.getChronology());
        assertEquals(partial.getFieldType(0), clonedPartial.getFieldType(0));
        assertEquals(partial.getValue(0), clonedPartial.getValue(0));
    }

    @Test
    public void testPartialSerialization() throws IOException {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(dateTimeFieldType, 30, chronology);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(partial);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Partial clonedPartial = (Partial) objectInputStream.readObject();
        objectInputStream.close();
        assertEquals(partial, clonedPartial);
    }

    @Test
    public void testPartialToString() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 },
                new int[] { 2022, 1 }, chronology);
        assertEquals("[year=2022 monthOfYear=1]", partial.toString());
        assertEquals("[year=2022 monthOfYear=1]", partial.toStringList());
    }

    @Test
    public void testPartialToStringWithFormat() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        assertEquals("2022", partial.toString("yyyy"));
        assertEquals("2022", partial.toString(null, "yyyy"));
        assertEquals("2022", partial.toString("yyyy", Locale.ENGLISH));
        assertEquals("2022", partial.toString("yyyy", Locale.US));
    }

    @Test
    public void testPartialProperty() {
        Chronology chronology = ISOChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        Property property = new Property(partial, 0);
        assertEquals(partial, property.getPartial());
        assertEquals(dateTimeFieldType, property.getField());
        assertEquals(2022, property.get());
        assertEquals(partial, property.getReadablePartial());
        Partial partial2 = property.addToCopy(1);
        assertEquals(2023, partial2.getValue(0));
        assertEquals(partial, property.getPartial());
    }
}