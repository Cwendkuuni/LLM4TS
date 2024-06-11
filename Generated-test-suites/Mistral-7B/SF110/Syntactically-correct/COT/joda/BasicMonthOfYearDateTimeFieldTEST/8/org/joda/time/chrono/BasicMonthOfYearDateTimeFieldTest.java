package org.joda.time.tests.chrono; // add the package name for the test file

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDate;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Period;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicChronology chronology = new BasicGregorianChronology(); // assume BasicGregorianChronology is used
    private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();
        int month = field.get(date);
        assertEquals(1, month);

        date = new BaseDateTime(0, 2000, 12, 31, 23, 59, 59, chronology).getMillis();
        month = field.get(date);
        assertEquals(12, month);
    }

    @Test
    public void testAdd() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();

        long result = field.add(date, 1);
        assertEquals(2000 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been incremented correctly
        assertEquals(2, field.get(result));

        result = field.add(date, DateTimeConstants.MAX_VALUE_MONTHS);
        assertEquals(2100 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been incremented correctly
        assertEquals(12, field.get(result));

        result = field.add(date, -1);
        assertEquals(1999 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been decremented correctly
        assertEquals(12, field.get(result));

        result = field.add(date, -DateTimeConstants.MAX_VALUE_MONTHS);
        assertEquals(1900 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been decremented correctly
        assertEquals(1, field.get(result));
    }

    @Test
    public void testAddLong() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();

        long result = field.add(date, 12 * 12);
        assertEquals(2000 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been incremented correctly
        assertEquals(13, field.get(result));

        result = field.add(date, -12 * 12);
        assertEquals(1999 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been decremented correctly
        assertEquals(1, field.get(result));
    }

    @Test
    public void testSet() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();

        long result = field.set(date, 3);
        assertEquals(2000 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been incremented correctly
        assertEquals(3, field.get(result));

        result = field.set(date, 13);
        assertEquals(2001 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been incremented correctly
        assertEquals(1, field.get(result)); // the month is out of bounds, so it should wrap around to 1
    }

    @Test
    public void testAddWrapField() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();

        long result = field.addWrapField(date, 13);
        assertEquals(2001 - 1900, Chronology.getInstance(result).getCentury()); // check the century to ensure the year has been incremented correctly
        assertEquals(1, field.get(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLargeAmount() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();
        field.add(date, DateTimeConstants.MAX_VALUE_MONTHS * 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeLargeAmount() {
        long date = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, chronology).getMillis();
        field.add(date, -DateTimeConstants.MAX_VALUE_MONTHS * 2);
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }
}