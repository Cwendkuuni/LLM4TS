package org.joda.time.chrono.test;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.YearMonth;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;

import org.joda.time.test.TestBase;

public class BasicMonthOfYearDateTimeFieldTest extends TestBase {

    public void testBasicMonthOfYearDateTimeField() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        // Test get method
        assertEquals(1, field.get(BaseDateTime.UTC.getMillis()));
        assertEquals(12, field.get(BaseDateTime.UTC.getMillis() + 364 * 24 * 60 * 60 * 1000L));

        // Test add method with int
        assertEquals(32, field.add(BaseDateTime.UTC.getMillis(), 31).getMillis());

        // Test add method with long
        assertEquals(32, field.add(BaseDateTime.UTC.getMillis(), Days.days(31).getMillis()).getMillis());

        // Test set method
        assertEquals(13, field.set(BaseDateTime.UTC.getMillis(), 13).getMillis());

        // Test add method with ReadablePartial
        LocalDateTime localDateTime = new LocalDateTime(BaseDateTime.UTC);
        localDateTime = localDateTime.withMonthOfYear(13);
        assertEquals(13, field.add(new ReadablePartial() {
            @Override
            public int getValue(DateTimeFieldType fieldType) {
                return localDateTime.getMonthOfYear();
            }

            @Override
            public DateTimeFieldType[] getFieldTypes() {
                return new DateTimeFieldType[] { DateTimeFieldType.monthOfYear() };
            }

            @Override
            public int size() {
                return 1;
            }
        }, 1, new int[] { 0 }, 1).getMillis() / chronology.getMillisPerDay());

        // Test addWrapField method
        assertEquals(14, field.addWrapField(BaseDateTime.UTC.getMillis(), 1).getMillis());

        // Test isLeap method
        assertTrue(field.isLeap(BaseDateTime.UTC.getMillis() + Days.days(365).getMillis() + 1));

        // Test getMinimumValue and getMaximumValue
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());

        // Test roundFloor method
        assertEquals(364, field.roundFloor(BaseDateTime.UTC.getMillis() + 365 * 24 * 60 * 60 * 1000L).getMillis() / 1000 / 60 / 60 / 24);

        // Test remainder method
        assertEquals(-1, field.remainder(BaseDateTime.UTC.getMillis() + 365 * 24 * 60 * 60 * 1000L).getMillis() / 1000 / 60 / 60 / 24);

        // Test getLeapDurationField method
        assertEquals(1, field.getLeapDurationField().getValues(BaseDateTime.UTC.getMillis()).intValue());

        // Test getLeapAmount method
        assertEquals(1, field.getLeapAmount(BaseDateTime.UTC.getMillis() + Days.days(365).getMillis()));

        // Test getDifferenceAsLong method
        assertEquals(1, field.getDifferenceAsLong(BaseDateTime.UTC.getMillis() + Days.days(365).getMillis(), BaseDateTime.UTC.getMillis()));
    }

    public void testAddWithOutOfRangeValue() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        try {
            field.add(BaseDateTime.UTC.getMillis(), -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.add(BaseDateTime.UTC.getMillis(), 13 * chronology.getMillisPerDay() + 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testRoundFloorWithOutOfRangeValue() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        try {
            field.roundFloor(BaseDateTime.UTC.getMillis() - 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.roundFloor(BaseDateTime.UTC.getMillis() + 13 * chronology.getMillisPerDay() + 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testSetWithOutOfRangeValue() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        try {
            field.set(BaseDateTime.UTC.getMillis(), 0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.set(BaseDateTime.UTC.getMillis(), 13 * chronology.getMillisPerDay() + 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testAddWithNullValue() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        try {
            field.add(null, 1);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    public void testAddWithNullAddend() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        try {
            field.add(BaseDateTime.UTC.getMillis(), null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    public void testAddWithWrongDataType() {
        final Chronology chronology = new BasicChronology("UTC", null);
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        try {
            field.add(BaseDateTime.UTC.getMillis(), Months.months(1).toLocalDate(2013, 2, 29).toDateTimeAtStartOfDay().getMillis());
            fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }
}