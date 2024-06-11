package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.ZoneUtils;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology gregorian;
    private DateTimeField field;
    private DateTimeField lenientField;

    @Before
    public void setUp() {
        gregorian = org.joda.time.DateTimeUtils.getUTC();
        field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH, gregorian);
    }

    @Test
    public void testGetInstance() {
        lenientField = LenientDateTimeField.getInstance(field, gregorian);
        assertNotSame(field, lenientField);
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSetInstant() {
        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, gregorian);
        long value = DateTimeConstants.DAYS_PER_YEAR + 1;

        lenientField = LenientDateTimeField.getInstance(field, gregorian);

        long result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        MutableDateTime expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) + value, gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1, lenientField.get(result));
        assertEquals(DateTimeConstants.DAYS_PER_YEAR + 1, lenientField.get(result));
    }

    @Test
    public void testSetOutOfBoundsValue() {
        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, gregorian);
        long value = DateTimeConstants.DAYS_PER_YEAR + 1;

        lenientField = LenientDateTimeField.getInstance(field, gregorian);

        long result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        MutableDateTime expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) + value, gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1, lenientField.get(result));
        assertEquals(DateTimeConstants.DAYS_PER_YEAR + 1, lenientField.get(result));

        value += 100;
        result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) + value, gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1 + (value / DateTimeConstants.DAYS_PER_YEAR), lenientField.get(result));
    }

    @Test
    public void testSetNegativeValue() {
        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, gregorian);
        long value = -1;

        lenientField = LenientDateTimeField.getInstance(field, gregorian);

        long result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        MutableDateTime expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) - value, gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1 - (value / DateTimeConstants.DAYS_PER_YEAR), lenientField.get(result));
    }

    @Test
    public void testSetValueWithDurationFieldType() {
        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, gregorian);
        DateTimeFieldType fieldType = DurationFieldType.HOURS_PER_DAY;
        long value = 2;

        lenientField = LenientDateTimeField.getInstance(field, gregorian);

        long result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        MutableDateTime expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) + value * getField(fieldType).get(mutableDateTime.getLocalMillis()), gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1, lenientField.get(result));
        assertEquals(value, lenientField.get(result, fieldType));
    }

    @Test
    public void testSetValueWithOutOfBoundsDurationFieldType() {
        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, gregorian);
        DateTimeFieldType fieldType = DurationFieldType.HOURS_PER_DAY;
        long value = 2 + DateTimeConstants.HOURS_PER_DAY;

        lenientField = LenientDateTimeField.getInstance(field, gregorian);

        long result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        MutableDateTime expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) + value * getField(fieldType).get(mutableDateTime.getLocalMillis()), gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1, lenientField.get(result));
        assertEquals(value, lenientField.get(result, fieldType));

        value += 100;
        result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) + value * getField(fieldType).get(mutableDateTime.getLocalMillis()), gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);
    }

    @Test
    public void testSetNegativeDurationFieldType() {
        Instant instant = Instant.now();
        MutableDateTime mutableDateTime = new MutableDateTime(instant, gregorian);
        DateTimeFieldType fieldType = DurationFieldType.HOURS_PER_DAY;
        long value = -1;

        lenientField = LenientDateTimeField.getInstance(field, gregorian);

        long result = lenientField.set(mutableDateTime.getLocalMilliseconds(), value);
        MutableDateTime expectedDateTime = new MutableDateTime(mutableDateTime.getLocalMillis() + getType().getField(gregorian).get(mutableDateTime.getLocalMillis()) - value * getField(fieldType).get(mutableDateTime.getLocalMillis()), gregorian);
        assertEquals(expectedDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(1, lenientField.get(result));
        assertEquals(-value, lenientField.get(result, fieldType));
    }

    @Test
    public void testSetValueWithCustomChronology() {
        Instant instant = Instant.now();
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(instant, mock(Chronology.class));
        DateTimeFieldType fieldType = DateTimeFieldType.DAY_OF_MONTH;
        long value = 2;

        lenientField = LenientDateTimeField.getInstance(field, mock(Chronology.class));

        long result = lenientField.set(mutableLocalDateTime.getLocalMillis(), value);
        MutableLocalDateTime expectedLocalDateTime = mutableLocalDateTime.plus(getField(fieldType).get(mutableLocalDateTime.getLocalMillis()), getType().getField(mock(Chronology.class)).get(mutableLocalDateTime.getLocalMillis()));
        assertEquals(expectedLocalDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(value, lenientField.get(result));
    }

    @Test
    public void testSetValueWithOutOfBoundsCustomChronology() {
        Instant instant = Instant.now();
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(instant, mock(Chronology.class));
        DateTimeFieldType fieldType = DateTimeFieldType.DAY_OF_MONTH;
        long value = 2 + DateTimeConstants.DAYS_PER_MONTH;

        lenientField = LenientDateTimeField.getInstance(field, mock(Chronology.class));

        long result = lenientField.set(mutableLocalDateTime.getLocalMillis(), value);
        MutableLocalDateTime expectedLocalDateTime = mutableLocalDateTime.plus(getField(fieldType).get(mutableLocalDateTime.getLocalMillis()), getType().getField(mock(Chronology.class)).get(mutableLocalDateTime.getLocalMillis()));
        assertEquals(expectedLocalDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(value, lenientField.get(result));

        value += 100;
        result = lenientField.set(mutableLocalDateTime.getLocalMillis(), value);
        expectedLocalDateTime = mutableLocalDateTime.plus(getField(fieldType).get(mutableLocalDateTime.getLocalMillis()), getType().getField(mock(Chronology.class)).get(mutableLocalDateTime.getLocalMillis()));
        assertEquals(expectedLocalDateTime.getLocalMillis(), result);
    }

    @Test
    public void testSetNegativeCustomChronology() {
        Instant instant = Instant.now();
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(instant, mock(Chronology.class));
        DateTimeFieldType fieldType = DateTimeFieldType.DAY_OF_MONTH;
        long value = -1;

        lenientField = LenientDateTimeField.getInstance(field, mock(Chronology.class));

        long result = lenientField.set(mutableLocalDateTime.getLocalMillis(), value);
        MutableLocalDateTime expectedLocalDateTime = mutableLocalDateTime.minus(getField(fieldType).get(mutableLocalDateTime.getLocalMillis()), getType().getField(mock(Chronology.class)).get(mutableLocalDateTime.getLocalMillis()));
        assertEquals(expectedLocalDateTime.getLocalMillis(), result);

        assertEquals(DateTimeFieldType.DAY_OF_MONTH, lenientField.getType());
        assertEquals(-value, lenientField.get(result));
    }

    private DateTimeField getField(DateTimeFieldType fieldType) {
        if (field instanceof StrictDateTimeField) {
            field = ((StrictDateTimeField)field).getWrappedField();
        }
        return (DateTimeField)ClassUtils.getFieldValue(field, fieldType.getClass().getName() + "Field");
    }

    private DateTimeType getType() {
        return (DateTimeType)ClassUtils.getFieldValue(field, "TYPE");
    }
}