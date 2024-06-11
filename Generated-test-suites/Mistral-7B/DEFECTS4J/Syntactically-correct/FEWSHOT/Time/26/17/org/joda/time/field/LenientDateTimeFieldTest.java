package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneUtils;
import org.joda.time.chrono.JulianDuration;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField field;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        base = mock(Chronology.class);
        field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_YEAR, 10);
        lenientField = LenientDateTimeField.getInstance(field, base);
    }

    @Test
    public void testGetInstance() {
        assertEquals(lenientField, LenientDateTimeField.getInstance(field, base));
        assertNotSame(field, LenientDateTimeField.getInstance(field, base));
        assertNull(LenientDateTimeField.getInstance(null, base));
    }

    @Test
    public void testNewInstance() {
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.getBase());
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet() {
        // test with valid value
        MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 12, 0, 0, 0, base);
        long instant = dateTime.getMillis();
        int value = 1;

        long newInstant = lenientField.set(instant, value);
        MutableDateTime newDateTime = new MutableDateTime(instant, base);
        newDateTime.setField(lenientField, value);

        assertEquals(instant + lenientField.getSize(), newInstant);
        assertEquals(newDateTime.toString(), dateTime.toString());

        // test with out of bounds value
        value = 100;

        newInstant = lenientField.set(instant, value);
        newDateTime = new MutableDateTime(instant, base);
        newDateTime.setField(lenientField, value);

        assertEquals(instant + lenientField.getSize() * 100, newInstant);
        assertEquals(newDateTime.toString(), dateTime.toString() + " +" + lenientField.getSize() * 100 + " days");

        // test with negative value
        value = -10;

        newInstant = lenientField.set(instant, value);
        newDateTime = new MutableDateTime(instant, base);
        newDateTime.setField(lenientField, value);

        assertEquals(instant - lenientField.getSize() * 10, newInstant);
        assertEquals(newDateTime.toString(), dateTime.toString() + " -" + lenientField.getSize() * 10 + " days");

        // test with DurationField
        DateTimeFieldType durationType = DurationFieldType.HOURS;
        DateTimeField durationField = new StrictDateTimeField(durationType, 1);
        LenientDateTimeField lenientDurationField = LenientDateTimeField.getInstance(durationField, base);

        long durationInstant = 0;
        long durationValue = 2;

        long newDurationInstant = lenientDurationField.set(durationInstant, durationValue);
        MutableLocalDateTime durationDateTime = new MutableLocalDateTime(0, base);
        durationDateTime.setField(lenientDurationField, durationValue);
        LocalDateTime newDurationDateTime = durationDateTime.toDateTimeAtStartOfNext(base);
        long newDurationInstant2 = newDurationDateTime.getMillis();

        assertEquals(durationInstant + lenientDurationField.getSize() * durationValue, newDurationInstant);
        assertEquals(newDurationDateTime.toString(), new LocalDateTime(newDurationDateTime.toDate()).toString() + " +" + lenientDurationField.getSize() * durationValue + " " + durationType.getPluralName() + "s");
        assertEquals(newDurationInstant2, newDurationInstant + lenientDurationField.getSize() * (lenientDurationField.getSize() > 0 ? 1 : -1));
    }
}