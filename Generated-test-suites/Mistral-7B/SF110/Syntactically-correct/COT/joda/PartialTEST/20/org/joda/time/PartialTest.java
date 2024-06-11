import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        Partial partial = new Partial(chronology);
        assertEquals(0, partial.size());
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithSingleField() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        Partial partial = new Partial(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType },
                new int[] { 2022, 11, 30, 12 }, chronology);
        assertEquals(4, partial.size());
        assertEquals(yearFieldType, partial.getFieldType(0));
        assertEquals(monthFieldType, partial.getFieldType(1));
        assertEquals(dayOfMonthFieldType, partial.getFieldType(2));
        assertEquals(hourOfDayFieldType, partial.getFieldType(3));
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(30, partial.getValue(2));
        assertEquals(12, partial.getValue(3));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithInvalidFields() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType invalidFieldType = new DateTimeFieldType() {
            @Override
            public String getName() {
                return "invalidFieldType";
            }

            @Override
            public DateTimeField getField(Chronology chronology) {
                throw new UnsupportedOperationException();
            }

            @Override
            public DurationField getDurationType() {
                throw new UnsupportedOperationException();
            }
        };
        try {
            Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType, monthFieldType, dayOfMonthFieldType, invalidFieldType },
                    new int[] { 2022, 11, 30, 1 }, chronology);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Types array must not contain null", e.getMessage());
        }

        try {
            Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType, monthFieldType, dayOfMonthFieldType, invalidFieldType },
                    new int[] { 2022, 11, 30, 1 }, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("WithUTC chronology must not be null", e.getMessage());
        }

        try {
            Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType, monthFieldType, dayOfMonthFieldType, dayOfMonthFieldType },
                    new int[] { 2022, 11, 30, 31 }, chronology);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Values array must be the same length as the types array", e.getMessage());
        }

        try {
            Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType, dayOfMonthFieldType },
                    new int[] { 2022, 11, 30, 1, 32 }, chronology);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Values array must not contain negative values", e.getMessage());
        }

        try {
            Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType },
                    new int[] { 2022, 13, 30, 12 }, chronology);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Types array must be in order largest-smallest: monthOfYear < hourOfDay", e.getMessage());
        }
    }

    @Test
    public void testPartialWithSameFieldTypeTwice() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType monthFieldType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType, monthFieldType2 },
                new int[] { 2022, 11, 30, 12, 11 }, chronology);
        assertEquals(5, partial.size());
        assertEquals(yearFieldType, partial.getFieldType(0));
        assertEquals(monthFieldType, partial.getFieldType(1));
        assertEquals(dayOfMonthFieldType, partial.getFieldType(2));
        assertEquals(hourOfDayFieldType, partial.getFieldType(3));
        assertEquals(monthFieldType2, partial.getFieldType(4));
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(30, partial.getValue(2));
        assertEquals(12, partial.getValue(3));
        assertEquals(11, partial.getValue(4));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithReadOnlyField() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteFieldType = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType, minuteFieldType },
                new int[] { 2022, 11, 30, 12, 30 }, chronology);
        assertEquals(5, partial.size());
        try {
            partial.with(minuteFieldType, 60);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithSameFieldTypeAndDifferentChronology() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        Chronology anotherChronology = BaseDateTime.CALENDAR.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        Partial partial1 = new Partial(new DateTimeFieldType[] { yearFieldType }, new int[] { 2022 }, chronology);
        Partial partial2 = new Partial(new DateTimeFieldType[] { yearFieldType }, new int[] { 2022 }, anotherChronology);
        assertEquals(1, partial1.size());
        assertEquals(1, partial2.size());
        assertEquals(yearFieldType, partial1.getFieldType(0));
        assertEquals(yearFieldType, partial2.getFieldType(0));
        assertEquals(2022, partial1.getValue(0));
        assertEquals(2022, partial2.getValue(0));
        assertNotSame(chronology, partial2.getChronology());
    }

    @Test
    public void testPartialWithValidFormatting() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType dayOfWeekFieldType = DateTimeFieldType.dayOfWeek();
        DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteFieldType = DateTimeFieldType.minuteOfHour();
        DateTimeFieldType secondFieldType = DateTimeFieldType.secondOfMinute();
        DateTimeFieldType millisFieldType = DateTimeFieldType.millisOfSecond();
        Partial partial = new Partial(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, dayOfWeekFieldType, hourOfDayFieldType, minuteFieldType, secondFieldType, millisFieldType },
                new int[] { 2022, 11, 30, 5, 12, 30, 15, 275 }, chronology);
        assertEquals(8, partial.size());
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(partial.getFieldTypes());
        String formattedString = formatter.print(partial);
        assertEquals("2022-11-30T05:12:30.152750", formattedString);
    }

    @Test
    public void testPartialWithInvalidFormatting() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType dayOfWeekFieldType = DateTimeFieldType.dayOfWeek();
        DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        DateTimeFieldType minuteFieldType = DateTimeFieldType.minuteOfHour();
        DateTimeFieldType secondFieldType = DateTimeFieldType.secondOfMinute();
        DateTimeFieldType millisFieldType = DateTimeFieldType.millisOfSecond();
        Partial partial = new Partial(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, dayOfWeekFieldType, hourOfDayFieldType, minuteFieldType, secondFieldType, millisFieldType },
                new int[] { 2022, 11, 30, 5, 12, 30, 15, 275 }, chronology);
        assertEquals(8, partial.size());
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType, minuteFieldType, secondFieldType });
        try {
            formatter.print(partial);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithFormatting() {
        Chronology chronology = BaseDateTime.UTC.getChronology();
        DateTimeFieldType yearFieldType = DateTimeFieldType.year();
        DateTimeFieldType monthFieldType = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dayOfMonthFieldType = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType hourOfDayFieldType = DateTimeFieldType.hourOfDay();
        Partial partial = new Partial(new DateTimeFieldType[] { yearFieldType, monthFieldType, dayOfMonthFieldType, hourOfDayFieldType },
                new int[] { 2022, 11, 30, 5 }, chronology);
        assertEquals(4, partial.size());
        DateTimeFormatter formatter = ISODateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        String formattedString = formatter.print(partial);
        assertEquals("2022-11-30 05:00", formattedString);
    }
}