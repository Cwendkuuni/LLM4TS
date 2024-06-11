package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testEmpty() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
        assertEquals(0, emptyPartial.getValue(0));
        assertTrue(emptyPartial.isMatch(BaseDateTime.UTC));
        assertTrue(emptyPartial.isMatch(new LocalDateTime()));
        assertTrue(emptyPartial.isMatch(new LocalDate()));
        assertTrue(emptyPartial.isMatch(new LocalTime()));
        assertTrue(emptyPartial.isMatch(new BaseDateTime()));
        assertTrue(emptyPartial.toString().equals("[]"));
        
        Partial emptyPartial2 = new Partial(emptyPartial);
        assertEquals(0, emptyPartial2.size());
        assertArrayEquals(emptyPartial.getValues(), emptyPartial2.getValues());
    }

    @Test
    public void testSingle() {
        Chronology isoChronology = DateTimeUtils.forISO();
        Partial singleYear = new Partial(DateTimeFieldType.year(), 2020, isoChronology);
        assertEquals(1, singleYear.size());
        assertEquals(isoChronology, singleYear.getChronology());
        assertEquals(2020, singleYear.getValue(0));
        assertFalse(singleYear.isMatch(new LocalDateTime(2021, 1, 1, 0, 0)));
        assertTrue(singleYear.isMatch(new LocalDateTime(2020, 1, 1, 0, 0)));
        assertTrue(singleYear.isMatch(new LocalDate(2020, 1, 1)));
        assertTrue(singleYear.isMatch(new LocalTime(0, 0)));
        assertTrue(singleYear.isMatch(new BaseDateTime(2020, 1, 1, 0, 0, 0, DateTimeConstants.UTC)));
        assertTrue(singleYear.toString().equals("[year=2020]"));
        
        Partial singleYear2 = new Partial(singleYear);
        assertEquals(1, singleYear2.size());
        assertArrayEquals(singleYear.getValues(), singleYear2.getValues());
    }

    @Test
    public void testMultiple() {
        Chronology isoChronology = DateTimeUtils.forISO();
        Partial singleYear = new Partial(DateTimeFieldType.year(), 2020, isoChronology);
        Partial singleMonth = new Partial(DateTimeFieldType.monthOfYear(), 1, isoChronology);
        Partial multiple = new Partial(singleYear, singleMonth);
        assertEquals(2, multiple.size());
        assertEquals(isoChronology, multiple.getChronology());
        assertEquals(2020, multiple.getValue(0));
        assertEquals(1, multiple.getValue(1));
        assertFalse(multiple.isMatch(new LocalDateTime(2021, 2, 1, 0, 0)));
        assertTrue(multiple.isMatch(new LocalDateTime(2020, 1, 1, 0, 0)));
        assertTrue(multiple.isMatch(new LocalDate(2020, 1, 1)));
        assertTrue(multiple.isMatch(new LocalTime(0, 0)));
        assertTrue(multiple.isMatch(new BaseDateTime(2020, 1, 1, 0, 0, 0, DateTimeConstants.UTC)));
        assertTrue(multiple.toString().equals("[year=2020, monthOfYear=1]"));
        
        Partial multiple2 = new Partial(multiple);
        assertEquals(2, multiple2.size());
        assertArrayEquals(multiple.getValues(), multiple2.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidType() {
        Chronology isoChronology = DateTimeUtils.forISO();
        Partial invalid = new Partial(new DurationFieldType[] { DateTimeFieldType.year() }, new int[] { 2020 }, isoChronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValuesLength() {
        Chronology isoChronology = DateTimeUtils.forISO();
        DateTimeFieldType yearType = DateTimeFieldType.year();
        Partial invalid = new Partial(new DateTimeFieldType[] { yearType }, new int[] { 2020, 2021 }, isoChronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValuesOrder() {
        Chronology isoChronology = DateTimeUtils.forISO();
        DateTimeFieldType yearType = DateTimeFieldType.year();
        DateTimeFieldType monthType = DateTimeFieldType.monthOfYear();
        Partial invalid = new Partial(new DateTimeFieldType[] { yearType, monthType }, new int[] { 1, 2020 }, isoChronology);
    }
}