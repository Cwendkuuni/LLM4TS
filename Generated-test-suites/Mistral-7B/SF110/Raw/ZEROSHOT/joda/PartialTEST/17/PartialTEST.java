package org.junit.joda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.BaseCalendar;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertEquals(BaseCalendar.UTC, emptyPartial.getChronology());
        assertEquals(0, emptyPartial.getValue(0));
    }

    @Test
    public void testPartialWithOneField() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();

        Partial partial = new Partial(yearType, 2022, chronology);
        assertEquals(1, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(2022, partial.getValue(0));

        assertEquals("2022", partial.toString());
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, 2022, monthType, 11, dayOfMonthType, 30, chronology);
        assertEquals(3, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(30, partial.getValue(2));

        assertEquals("2022-11-30", partial.toString());
    }

    @Test
    public void testPartialWithInvalidFields() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType invalidType = new DateTimeFieldType() {
            @Override
            public String getName() {
                return "invalid";
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public int getPrecision() {
                return -1;
            }

            @Override
            public int getValueInstant(long instant, Chronology chronology) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setValue(DateTimeBase dateTime, int field, int newValue, long[] values) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getValue(DateTimeBase dateTime, Chronology chronology) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isSupported(Chronology chronology) {
                return false;
            }

            @Override
            public DurationFieldType getRangeDurationType() {
                throw new UnsupportedOperationException();
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new Partial(yearType, monthType, invalidType, 1, dayOfMonthType, 30, chronology));
    }

    @Test
    public void testPartialWithDuplicateFields() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType dayOfYearType = DateTimeFieldType.dayOfYear();

        Partial partial = new Partial(yearType, monthType, dayOfMonthType, dayOfYearType, dayOfMonthType, 30, chronology);
        assertEquals(3, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(30, partial.getValue(2));
        assertEquals(30, partial.getValue(3));

        assertThrows(IllegalArgumentException.class, () -> new Partial(yearType, monthType, dayOfMonthType, dayOfMonthType, dayOfYearType, 30, chronology, dayOfMonthType));
    }

    @Test
    public void testPartialWithInvalidValues() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        assertThrows(IllegalArgumentException.class, () -> new Partial(yearType, monthType, dayOfMonthType, 33, dayOfMonthType, 30, chronology));
        assertThrows(IllegalArgumentException.class, () -> new Partial(yearType, monthType, dayOfMonthType, -1, dayOfMonthType, 30, chronology));
    }

    @Test
    public void testPartialWithDifferentChronologies() {
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Chronology gregorian = BaseCalendar.INSTANCE.getCalendar(Locale.ENGLISH).getTimeZone().getCalendar();
        Partial gregorianPartial = new Partial(yearType, monthType, dayOfMonthType, 2022, 11, 30, gregorian);

        Chronology isoPartial = BaseCalendar.UTC;
        Partial isoPartial = new Partial(yearType, monthType, dayOfMonthType, 2022, 11, 30, isoPartial);

        assertEquals(isoPartial, gregorianPartial.withChronologyRetainFields(isoPartial.getChronology()));
    }

    @Test
    public void testPartialWithAddedField() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType hourType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteType = DateTimeFieldType.minute();

        Partial partial = new Partial(hourType, minuteType, 12, 30, chronology);
        assertEquals(2, partial.size());

        Partial partialWithAddedField = partial.withFieldAdded(DateTimeFieldType.secondOfMinute(), 30);
        assertEquals(3, partialWithAddedField.size());
        assertEquals(30, partialWithAddedField.getValue(2));
    }

    @Test
    public void testPartialWithAddedFieldWrapped() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType hourType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteType = DateTimeFieldType.minute();
        DateTimeFieldType secondType = DateTimeFieldType.secondOfMinute();

        Partial partial = new Partial(hourType, minuteType, 12, 30, secondType, 30, chronology);
        assertEquals(3, partial.size());

        Partial partialWithAddedFieldWrapped = partial.withFieldAddWrapped(secondType, 45);
        assertEquals(3, partialWithAddedFieldWrapped.size());
        assertEquals(12, partialWithAddedFieldWrapped.getValue(0));
        assertEquals(30, partialWithAddedFieldWrapped.getValue(1));
        assertEquals(45, partialWithAddedFieldWrapped.getValue(2));
    }

    @Test
    public void testPartialWithAddedPeriod() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType dayOfYearType = DateTimeFieldType.dayOfYear();

        LocalDate date = LocalDate.of(2022, 11, 30);
        LocalDateTime time = LocalDateTime.of(LocalDate.of(2022, 11, 30), LocalTime.MIDNIGHT);

        Partial partial = new Partial(yearType, monthType, dayOfMonthType, 2022, 11, 30, chronology);

        ReadablePeriod period = LocalDate.parse("2022-12-31").toLocalDateTime().plus(LocalTime.of(12, 30)).minus(time);
        Partial partialWithAddedPeriod = partial.withPeriodAdded(period, 1);

        assertEquals(partialWithAddedPeriod.toString(), "2023-01-01 14:30:00.000");
        assertEquals("2023-01-01 14:30:00.000", partialWithAddedPeriod.toString());
    }

    @Test
    public void testPartialWithAddedPeriodNegative() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        LocalDate date = LocalDate.of(2022, 11, 30);
        LocalDateTime time = LocalDateTime.of(LocalDate.of(2022, 11, 30), LocalTime.MIDNIGHT);

        Partial partial = new Partial(yearType, monthType, dayOfMonthType, 2022, 11, 30, chronology);

        ReadablePeriod period = LocalDate.parse("2022-11-30").toLocalDateTime().minus(LocalTime.of(12, 30)).toLocalDate().plus(1);
        Partial partialWithAddedPeriodNegative = partial.withPeriodAdded(period, -1);

        assertEquals(partialWithAddedPeriodNegative.toString(), "2022-11-29");
        assertEquals("2022-11-29", partialWithAddedPeriodNegative.toString());
    }

    @Test
    public void testPartialWithFormatter() {
        Chronology chronology = BaseCalendar.UTC;
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthType = DateTimeFieldType.dayOfMonth();

        Partial partial = new Partial(yearType, monthType, dayOfMonthType, 2022, 11, 30, chronology);

        DateTimeFormatter formatter = ISODateTimeFormat.forFields(Arrays.asList(yearType, monthType, dayOfMonthType), true, false);

        assertEquals("2022-11-30", partial.toString(formatter.toString()));
    }
}