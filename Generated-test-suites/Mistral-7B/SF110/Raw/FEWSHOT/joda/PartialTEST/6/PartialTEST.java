package org.joda.time;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.joda.time.*;
import org.joda.time.base.DateTimeFieldType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.field.*;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNull(partial.getChronology());
    }

    @Test
    public void testPartialWithSingleField() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
    }

    @Test
    public void testPartialWithUTCChronology() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Chronology chronology = GregorianChronology.INSTANCE;
        Partial partial = new Partial(chronology).with(dateTimeFieldType, 2022);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithMultipleFields() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, monthOfYear(1), dayOfMonth(1), monthType, 2, dayOfMonth(31), dayType(1));
        assertEquals(7, partial.size());
        assertArrayEquals(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType, monthType, dayOfMonthType, dayType(1), dayType(1)}, partial.getFieldTypes());
        assertEquals(new int[]{2022, 1, 1, 2, 31, 1, 1}, partial.getValues());
    }

    @Test
    public void testPartialWithInvalidFields() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType invalidFieldType = new DurationFieldType("InvalidFieldType") {
            @Override
            public int getInstant(long instant, Chronology instantChronology) {
                throw new IllegalStateException("Invalid field type");
            }

            @Override
            public long getValue(ReadableInstant readableInstant) {
                throw new IllegalStateException("Invalid field type");
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[]{yearType, monthType, invalidFieldType, dayOfMonthType}));
        assertThrows(IllegalArgumentException.class, () -> new Partial(new int[]{Integer.MIN_VALUE, 1, 1, Integer.MAX_VALUE}));
        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType}, new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    public void testPartialWithInvalidOrder() {
        DateTimeFieldType hourType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteType = DateTimeFieldType.minute();
        DateTimeFieldType secondType = DateTimeFieldType.second();

        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[]{minuteType, hourType, secondType}));
    }

    @Test
    public void testPartialWithDuplicateFields() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[]{yearType, monthType, dayOfMonthType, yearType}));
    }

    @Test
    public void testPartialWithDuplicateRangeFields() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DurationFieldType dayType = DurationFieldType.days();
        DurationFieldType dayType2 = DurationFieldType.days();

        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[]{yearType, monthType, dayType, dayType2}));
    }

    @Test
    public void testPartialWithInvalidRangeFields() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DurationFieldType invalidDurationFieldType = new DurationFieldType("InvalidDurationFieldType") {
            @Override
            public int getInstant(long instant, Chronology instantChronology) {
                throw new IllegalStateException("Invalid duration field type");
            }

            @Override
            public Duration toDuration(ReadableInstant start, ReadableInstant end, Chronology instantChronology) {
                throw new IllegalStateException("Invalid duration field type");
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[]{yearType, monthType}, new DurationFieldType[]{invalidDurationFieldType}));
    }

    @Test
    public void testPartialWithInvalidChronology() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        assertThrows(IllegalArgumentException.class, () -> new Partial(null, dateTimeFieldType, 2022));
    }

    @Test
    public void testPartialWithChangedChronology() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial();
        Chronology chronology1 = GregorianChronology.INSTANCE;
        Partial changedPartial = partial.withChronologyRetainFields(ISOChronology.INSTANCE);

        assertNotEquals(chronology1, changedPartial.getChronology());
        assertEquals(partial.getValues(), changedPartial.getValues());

        Partial partial2 = new Partial(partial, changedPartial.getValues());
        assertEquals(partial.getChronology(), partial2.getChronology());
    }

    @Test
    public void testPartialWithAddField() {
        DateTimeFieldType minuteType = DateTimeFieldType.minute();
        Partial partial = new Partial();
        Partial addedPartial = partial.withFieldAdded(Minutes.MINUTES, 30);
        assertEquals(1, addedPartial.size());
        assertEquals(minuteType, addedPartial.getFieldType(0));
        assertEquals(30, addedPartial.getValue(0));
    }

    @Test
    public void testPartialWithAddWrapField() {
        DateTimeFieldType hourType = DateTimeFieldType.hourOfDay();
        Partial partial = new Partial();
        Partial addedPartial = partial.withFieldAddWrapped(Hours.HOURS, 1);
        assertEquals(1, addedPartial.size());
        assertEquals(hourType, addedPartial.getFieldType(0));
        assertEquals(1, addedPartial.getValue(0));
    }

    @Test
    public void testPartialWithAddPeriod() {
        Partial partial = new Partial();
        ReadablePeriod readablePeriod = new Duration(1, Minutes.MINUTES);
        Partial addedPartial = partial.withPeriodAdded(readablePeriod, 1);
        assertEquals(1, addedPartial.size());
        assertEquals(Minutes.MINUTES, addedPartial.getFieldType(0));
        assertEquals(60, addedPartial.getValue(0));
    }

    @Test
    public void testPartialWithSubtractPeriod() {
        Partial partial = new Partial();
        ReadablePeriod readablePeriod = new Duration(1, Minutes.MINUTES);
        Partial subtractedPartial = partial.minus(readablePeriod);
        assertEquals(1, subtractedPartial.size());
        assertEquals(Minutes.MINUTES, subtractedPartial.getFieldType(0));
        assertEquals(-60, subtractedPartial.getValue(0));
    }

    @Test
    public void testProperty() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        Partial partial = new Partial(yearType, 2022);
        Property property = new Property(partial, 0);
        DateTimeField field = property.getField();

        assertEquals(2022, field.get(new GregorianCalendar()));
        assertEquals(2022, field.get(new BaseCalendar()));
        assertEquals(2022, field.get(new JulianCalendar()));
        assertEquals(2022L, field.getMillis(new GregorianCalendar()));
        assertEquals(2022L, field.getMillis(new BaseCalendar()));
        assertEquals(2022L, field.getMillis(new JulianCalendar()));
        assertEquals(2022, field.getLocalValue(new GregorianCalendar()));
        assertEquals(2022, field.getLocalValue(new BaseCalendar()));
        assertEquals(2022, field.getLocalValue(new JulianCalendar()));
        assertEquals(2022, property.get());

        Partial updatedPartial = property.set(2023);
        assertEquals(2023, field.get(new GregorianCalendar()));
        assertEquals(2023, field.get(new BaseCalendar()));
        assertEquals(2023, field.get(new JulianCalendar()));
        assertEquals(2023L, field.getMillis(new GregorianCalendar()));
        assertEquals(2023L, field.getMillis(new BaseCalendar()));
        assertEquals(2023L, field.getMillis(new JulianCalendar()));
        assertEquals(2023, property.get());
        assertEquals(updatedPartial, property.getPartial());
    }

    @Test
    public void testFormatter() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, 2022, monthType, 1, dayOfMonthType, 31);

        DateTimeFormatter formatter = partial.getFormatter();
        assertEquals("yyyy'-'MM'-'dd", formatter.getPattern());
    }

    @Test
    public void testToString() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, 2022, monthType, 1, dayOfMonthType, 31);

        assertEquals("[year=2022, monthOfYear=1, dayOfMonth=31]", partial.toString());
    }

    @Test
    public void testToStringList() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, 2022, monthType, 1, dayOfMonthType, 31);

        assertEquals("[year=2022, monthOfYear=1, dayOfMonth=31]", partial.toStringList());
    }

    @Test
    public void testToStringWithLocale() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, 2022, monthType, 1, dayOfMonthType, 31);

        assertEquals("[year=2022, monthOfYear=1, dayOfMonth=31]", partial.toString("", null));
        assertEquals("[year=2022, monthOfYear=January, dayOfMonth=31]", partial.toString("yyyy'-'MMM'-'dd", Locale.ENGLISH));
    }

    private static DateTimeFieldType dayOfMonth(int dayOfMonth) {
        return DateTimeFieldType.dayOfMonth();
    }

    private static DateTimeFieldType dayType(int dayOfWeek) {
        return DurationFieldType.dayOfWeek();
    }

    private static int monthOfYear(int monthOfYear) {
        return DateTimeFieldType.monthOfYear();
    }
}