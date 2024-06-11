package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Locale;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterConstruction() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());

        DateTimeFormatter formatterWithLocale = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), Locale.US);
        assertEquals(Locale.US, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithOffsetParsed = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), Locale.US, true);
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithChronology = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), Locale.US, false, new GregorianChronology());
        assertEquals(GregorianChronology.getInstance(), formatterWithChronology.getChrono());

        DateTimeFormatter formatterWithZone = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), Locale.US, false, null, DateTimeZone.forOffsetHours(1));
        assertEquals(DateTimeZone.forOffsetHours(1), formatterWithZone.getZone());

        DateTimeFormatter formatterWithPivotYear = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), Locale.US, false, null, null, 2005);
        assertEquals(2005, formatterWithPivotYear.getPivotYear());

        DateTimeFormatter formatterWithDefaultYear = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), Locale.US, false, null, null, null, 2010);
        assertEquals(2010, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterMethods() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());

        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getPrinter());
        assertNull(formatter.getParser());

        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, formatterWithLocale.getLocale());
        assertSame(formatter, formatterWithLocale);

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
        assertSame(formatter, formatterWithOffsetParsed);

        assertFalse(formatter.isOffsetParsed());
        assertFalse(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithChronology = formatter.withChronology(new GregorianChronology());
        assertEquals(GregorianChronology.getInstance(), formatterWithChronology.getChrono());
        assertSame(formatter, formatterWithChronology);

        assertNull(formatter.getChrono());
        assertNull(formatterWithChronology.getChrono());

        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
        assertSame(formatter, formatterWithZoneUTC);

        DateTimeFormatter formatterWithZone = formatter.withZone(DateTimeZone.forOffsetHours(1));
        assertEquals(DateTimeZone.forOffsetHours(1), formatterWithZone.getZone());
        assertSame(formatter, formatterWithZone);

        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2005);
        assertEquals(2005, formatterWithPivotYear.getPivotYear());
        assertSame(formatter, formatterWithPivotYear);

        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2010);
        assertEquals(2010, formatterWithDefaultYear.getDefaultYear());
        assertSame(formatter, formatterWithDefaultYear);
    }
}