package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.Calends;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicConstruction() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(12, field.getMaximumValue());
        assertEquals(0, field.getMinimumValue());
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        LocalDate date = new LocalDate(2018, 1, 1);
        long n = date.getMillis();

        int actual = field.get(n);
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        LocalDate date = new LocalDate(2018, 1, 1);
        long n = date.getMillis();

        long result = field.add(n, 12);
        LocalDate expectedDate = new LocalDate(2019, 1, 1);
        assertEquals(expectedDate.getMillis(), result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        long initial = 1551167600000L; // 2019-01-01T00:00:00Z
        long result = field.addWrapField(initial, 13);
        assertEquals(1552377600000L, result); // 2019-03-01T00:00:00Z
    }

    @Test
    public void testAddWithReadablePartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        LocalDateTime date = new LocalDateTime(2018, 1, 1, 0, 0);
        LocalDateTime dateWithAddition = date.plusMonths(3);

        int[] result = field.add(date.toLocalDate().toLong(), 3, new int[3], 0);
        int expectedYear = dateWithAddition.getYear();
        int expectedMonth = dateWithAddition.getMonthOfYear();
        int expectedDayOfMonth = dateWithAddition.getDayOfMonth();

        assertEquals(expectedYear, result[0]);
        assertEquals(expectedMonth, result[1]);
        assertEquals(expectedDayOfMonth, result[2]);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        LocalDate date1 = new LocalDate(2018, 1, 1);
        LocalDate date2 = new LocalDate(2018, 1, 31);

        long difference = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        assertEquals(29, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), Calends.JANUARY);
        long initial = 1551167600000L; // 2019-01-01T00:00:00Z
        int targetMonth = 3;

        long result = field.set(initial, targetMonth);
        LocalDate expectedDate = new LocalDate(2019, 3, 1);

        assertEquals(expectedDate.getMillis(), result);
    }
}