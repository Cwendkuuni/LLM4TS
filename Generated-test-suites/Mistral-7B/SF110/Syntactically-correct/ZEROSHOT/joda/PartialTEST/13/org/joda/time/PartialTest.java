package org.joda.time.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.field.FieldUtils;
import org.joda.time.readable.ReadableInstant;
import org.joda.time.readable.ReadablePartial;
import org.joda.time.test.testutils.ChronologyUtils;

public class PartialTest {

    @org.junit.Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
    }

    @org.junit.Test
    public void testSingleFieldPartial() {
        Chronology utc = ChronologyUtils.UTC;
        DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
        Partial singleFieldPartial = new Partial(dayOfMonth, 22, utc);
        assertEquals(1, singleFieldPartial.size());
        assertEquals(dayOfMonth, singleFieldPartial.getFieldTypes()[0]);
        assertEquals(22, singleFieldPartial.getValue(0));

        assertEquals(utc, singleFieldPartial.getChronology());
        assertEquals(dayOfMonth.getMillisPerField(), singleFieldPartial.getField(0, utc).getMillisAdded());

        Partial emptyPartial = new Partial();
        assertEquals(emptyPartial, singleFieldPartial.without(dayOfMonth));

        Partial withValue22 = new Partial(singleFieldPartial);
        assertEquals(withValue22, singleFieldPartial);

        Partial withValue23 = new Partial(singleFieldPartial, 1);
        assertFalse(singleFieldPartial.equals(withValue23));
        assertEquals(withValue23.getValue(0), 23);

        Partial partialWithSameValue = new Partial(dayOfMonth, 22, utc);
        assertTrue(singleFieldPartial.isMatch(partialWithSameValue));
    }

    @org.junit.Test
    public void testManyFieldsPartial() {
        Chronology utc = ChronologyUtils.UTC;
        DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();

        Partial partial = new Partial(dayOfMonth, 22, utc);
        partial = partial.with(hourOfDay, 14).with(minuteOfHour, 30);

        assertEquals(3, partial.size());
        assertEquals(dayOfMonth, partial.getFieldTypes()[0]);
        assertEquals(hourOfDay, partial.getFieldTypes()[1]);
        assertEquals(minuteOfHour, partial.getFieldTypes()[2]);

        assertEquals(22, partial.getValue(0));
        assertEquals(14, partial.getValue(1));
        assertEquals(30, partial.getValue(2));

        assertEquals(utc, partial.getChronology());

        Partial emptyPartial = new Partial();
        assertEquals(emptyPartial, partial.without(dayOfMonth));
        assertEquals(emptyPartial, partial.without(hourOfDay));
        assertEquals(emptyPartial, partial.without(minuteOfHour));
        assertEquals(emptyPartial, partial.without(dayOfMonth, hourOfDay));
        assertEquals(emptyPartial, partial.without(dayOfMonth, hourOfDay, minuteOfHour));

        Partial withSameFieldTypesAndValues = new Partial(dayOfMonth, 22, hourOfDay, 14, minuteOfHour, 30, utc);
        assertTrue(partial.isMatch(withSameFieldTypesAndValues));

        Partial withDifferentValues = new Partial(dayOfMonth, 21, hourOfDay, 14, minuteOfHour, 30, utc);
        assertFalse(partial.isMatch(withDifferentValues));

        Partial withDifferentFieldTypes = new Partial(DateTimeFieldType.dayOfWeek(), 6, hourOfDay, 14, minuteOfHour, 30, utc);
        assertFalse(partial.isMatch(withDifferentFieldTypes));

        Partial withFieldOutOfOrder = new Partial(minuteOfHour, 30, hourOfDay, 14, dayOfMonth, 22, utc);
        assertFalse(partial.isMatch(withFieldOutOfOrder));

        Partial withInvalidFieldType = new Partial(DateTimeFieldType.millisecond(), 22, utc);
        assertFalse(partial.isMatch(withInvalidFieldType));

        Partial withInvalidValue = new Partial(dayOfMonth, 32, utc);
        assertFalse(partial.isMatch(withInvalidValue));

        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
        try {
            Date date = formatter.parseLocalDate("2015-03-22T14:30:00Z");
            assertEquals(new DateTime(2015, 3, 22, 14, 30, 0, 0, utc).toDate(), date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Date date = new Date(1426538400000L);
        Partial readablePartial = new Partial(utc, Arrays.asList(dayOfMonth, hourOfDay, minuteOfHour));
        assertEquals(partial, readablePartial);

        Partial readablePartialFromString = new Partial(partial.toString());
        assertEquals(partial, readablePartialFromString);
    }

    @org.junit.Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Chronology utc = ChronologyUtils.UTC;
        DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();

        Partial partial = new Partial(dayOfMonth, 22, utc).with(hourOfDay, 14).with(minuteOfHour, 30);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(partial);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Partial readPartial = (Partial) ois.readObject();
        ois.close();

        assertArrayEquals(partial.getValues(), readPartial.getValues());
    }
}