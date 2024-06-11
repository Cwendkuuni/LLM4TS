package org.joda.time.chrono.test;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.test.BaseTestCase;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        // Test basic functionality
        BasicChronology gregorian = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYearField = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = new GregorianDate(2000, 1, 1).getMillis();
        assertEquals(1, monthOfYearField.get(date));

        date += gregorian.getMillisPerDay() * 28; // 28 days in February
        assertEquals(2, monthOfYearField.get(date));

        // Test adding months
        date += gregorian.getMillisPerDay() * 30; // 30 days in March
        assertEquals(3, monthOfYearField.get(date));
        assertEquals(2000 - 1970 + 34, monthOfYearField.add(date, 5).getYear()); // Add 5 months, year should be 34 years after 2000

        // Test negative months
        date -= gregorian.getMillisPerDay() * 30; // 30 days in March
        assertEquals(2, monthOfYearField.get(date));
        assertEquals(2000 - 1970 - 1, monthOfYearField.add(date, -1).getYear()); // Subtract 1 month, year should be one year before 2000

        // Test adding months across year boundaries
        date += gregorian.getMillisPerYear() * 1; // One year
        assertEquals(3, monthOfYearField.get(date));
        assertEquals(2001, monthOfYearField.add(date, 11).getYear()); // Add 11 months, year should be 2001
        
        // Test adding months to a date near a year boundary
        date += gregorian.getMillisPerYear() * 4; // Four years
        date += gregorian.getMillisPerMonth() * 11; // 11 months
        assertEquals(12, monthOfYearField.get(date)); // Last month of the year
        assertEquals(2005, monthOfYearField.add(date, 1).getYear()); // Add 1 month, year should be 2005
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTooManyMonths() {
        // Test adding too many months
        BasicChronology gregorian = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYearField = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = new GregorianDate(2000, 1, 1).getMillis();
        monthOfYearField.add(date, gregorian.getMillisPerMonth() * 13); // Add 13 months, which is more than a year with 12 months
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMonths() {
        // Test adding negative months
        BasicChronology gregorian = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYearField = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = new GregorianDate(2000, 1, 1).getMillis();
        monthOfYearField.add(date, -3); // Subtract 3 months
        monthOfYearField.add(date, -30); // Subtract 30 days, which is more than a month
    }

    @Test
    public void testAddWrapField() {
        // Test addWrapField
        BasicChronology gregorian = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYearField = new BasicMonthOfYearDateTimeField(gregorian, 2); // February

        long date = new GregorianDate(2000, 1, 1).getMillis();
        long wrappedDate = monthOfYearField.addWrapField(date, 13);
        assertEquals(2001, getYear(wrappedDate));
        assertEquals(2, getMonthOfYear(wrappedDate));
    }
}