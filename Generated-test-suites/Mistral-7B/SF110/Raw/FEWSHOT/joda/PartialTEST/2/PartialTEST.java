package org.joda.time;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.*;
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
    public void testSimplePartial() {
        Chronology chronology = GregorianChronology.getInstance();
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{2022, 1, 1});
        assertEquals(3, partial.size());
        assertEquals(chronology, partial.getChronology());
        DateTimeFieldType fieldType = DateTimeFieldType.year();
        assertEquals(2022, partial.getValue(partial.indexOf(fieldType)));
        assertEquals(2022, partial.getFieldType(0).get(DateTime.now(chronology)));
        assertEquals(2022, partial.getFieldType(0).get(new DateTime(2022, 1, 1, 0, 0, 0, 0, chronology)));

        assertEquals("year=2022, monthOfYear=1, dayOfMonth=1", partial.toString());
    }

    @Test
    public void testComplexPartial() {
        Chronology chronology = GregorianChronology.getInstance();
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute()}, new int[]{2022, 1, 1, 12, 30, 45});
        assertEquals(6, partial.size());
        assertEquals(chronology, partial.getChronology());
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        assertEquals(12, partial.getValue(partial.indexOf(fieldType)));
        assertEquals(12, partial.getFieldType(3).get(DateTime.now(chronology)));
        assertEquals(12, partial.getFieldType(3).get(new DateTime(2022, 1, 1, 12, 30, 45, 0, chronology)));

        assertEquals("year=2022, monthOfYear=1, dayOfMonth=1, hourOfDay=12, minuteOfHour=30, secondOfMinute=45", partial.toString());

        Partial partial2 = new Partial(partial, new int[]{2023, 2, 29, 13, 45, 56});
        assertEquals(6, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(2023, partial2.getValue(partial2.indexOf(DateTimeFieldType.year())));
        assertEquals(2023, partial2.getFieldType(0).get(DateTime.now(chronology)));
        assertEquals(2, partial2.getValue(partial2.indexOf(DateTimeFieldType.monthOfYear())));
        assertEquals(29, partial2.getValue(partial2.indexOf(DateTimeFieldType.dayOfMonth())));
        assertEquals(13, partial2.getValue(partial2.indexOf(DateTimeFieldType.hourOfDay())));
        assertEquals(45, partial2.getValue(partial2.indexOf(DateTimeFieldType.minuteOfHour())));
        assertEquals(56, partial2.getValue(partial2.indexOf(DateTimeFieldType.secondOfMinute())));
    }

    @Test
    public void testCopyConstructor() {
        Chronology chronology = GregorianChronology.getInstance();
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{2022, 1, 1});
        Partial partial2 = new Partial(partial, new int[]{2023, 2, 29});
        assertEquals(3, partial.size());
        assertEquals(3, partial2.size());
        assertNotSame(partial, partial2);
        assertEquals(2022, partial.getValue(0));
        assertEquals(2023, partial2.getValue(0));
        assertEquals(1, partial.getValue(1));
        assertEquals(2, partial2.getValue(1));
        assertEquals(1, partial.getValue(2));
        assertEquals(29, partial2.getValue(2));
    }

    @Test
    public void testAddField() {
        Chronology chronology = GregorianChronology.getInstance();
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{2022, 1, 1});
        Partial partial2 = partial.with(DateTimeFieldType.dayOfMonth(), 31);
        assertEquals(3, partial.size());
        assertEquals(3, partial2.size());
        assertEquals(2022, partial.getValue(0));
        assertEquals(1, partial.getValue(1));
        assertEquals(1, partial.getValue(2));
        assertEquals(31, partial2.getValue(2));

        Partial partial3 = partial.withFieldAdded(DateTimeFieldType.dayOfWeek(), 1);
        assertEquals(4, partial3.size());
        assertEquals(2022, partial3.getValue(0));
        assertEquals(1, partial3.getValue(1));
        assertEquals(1, partial3.getValue(2));
        assertEquals(1, partial3.getValue(3));
    }

    @Test
    public void testAddPeriod() {
        Chronology chronology = GregorianChronology.getInstance();
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{2022, 1, 1});
        Period period = new GeneralPeriod(new Duration(2, 30, 0, 0), new Duration(1, 0, 0, 0));
        Partial partial2 = partial.withPeriodAdded(period, 1);
        assertEquals(3, partial.size());
        assertEquals(3, partial2.size());
        assertEquals(2022, partial.getValue(0));
        assertEquals(1, partial.getValue(1));
        assertEquals(1, partial.getValue(2));
        assertEquals(2023, partial2.getValue(0));
        assertEquals(2, partial2.getValue(1));
        assertEquals(29, partial2.getValue(2));
    }

    @Test
    public void testFormatter() {
        Chronology chronology = GregorianChronology.getInstance();
        Partial partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[]{2022, 1, 1});
        DateTimeFormatter formatter = partial.getFormatter();
        assertEquals("yyyy-MM-dd", formatter.getPattern());
        assertEquals("2022-01-01", formatter.print(partial));
    }
}