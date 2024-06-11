package org.joda.time.chrono.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalDateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology ISO_CHRONOLOGY = new BasicChronology("ISO", DateTimeConstants.ISO);

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(ISO_CHRONOLOGY.getAverageMillisPerMonth(), field.getPrecision());
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
        assertFalse(field.isLenient());

        // Test get method
        long instant = BaseDateTime.UTC.getMillis();
        int actual = field.get(instant);
        assertEquals(DateTimeConstants.JANUARY, actual);

        // Test set method
        long newInstant = field.set(instant, DateTimeConstants.FEBRUARY).getMillis();
        assertEquals(DateTimeConstants.FEBRUARY, iChronology.getMonthOfYear(newInstant));

        // Test add method
        long addResult = field.add(instant, 1).getMillis();
        assertEquals(DateTimeConstants.FEBRUARY, iChronology.getMonthOfYear(addResult));
        long addWrapResult = field.addWrapField(instant, 12).getMillis();
        assertEquals(DateTimeConstants.JANUARY, iChronology.getMonthOfYear(addWrapResult));

        // Test add method with long
        long addLongResult = field.add(instant, 12).getMillis();
        assertEquals(DateTimeConstants.JANUARY, iChronology.getMonthOfYear(addLongResult));

        // Test getDifferenceAsLong method
        long diffResult = field.getDifferenceAsLong(instant, BaseDateTime.UTC.getMillis());
        assertEquals(0, diffResult);
        long diffResult2 = field.getDifferenceAsLong(BaseDateTime.UTC.getMillis(), instant);
        assertEquals(0, diffResult2);
        long diffResult3 = field.getDifferenceAsLong(instant, BaseDateTime.UTC.getMillis() + (DateTimeConstants.DAYS_PER_YEAR * 2));
        assertEquals(2, diffResult3);

        // Test isLeap method
        long leapInstant = ISO_CHRONOLOGY.getMillis(DateTimeConstants.YEAR_2000, DateTimeConstants.FEBRUARY, 29);
        assertTrue(field.isLeap(leapInstant));
        long notLeapInstant = ISO_CHRONOLOGY.getMillis(DateTimeConstants.YEAR_2000, DateTimeConstants.FEBRUARY, 28);
        assertFalse(field.isLeap(notLeapInstant));

        // Test getLeapAmount method
        assertEquals(1, field.getLeapAmount(leapInstant));
        assertEquals(0, field.getLeapAmount(notLeapInstant));

        // Test getLeapDurationField method
        DurationField durationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.dayOfMonth(), durationField.getFieldType());
        assertEquals(1, durationField.getPrecision());
        assertEquals(1, durationField.getMinimumValue());
        assertEquals(31, durationField.getMaximumValue());

        // Test getRangeDurationField method
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeDurationField.getFieldType());
        assertEquals(0, rangeDurationField.getMinimumValue());
        assertEquals(Long.MAX_VALUE, rangeDurationField.getMaximumValue());

        // Test set with out of bounds value
        try {
            field.set(instant, 13);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test get with out of bounds value
        try {
            field.get(Long.MIN_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithZeroValue() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, 0).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithMaxValue() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MAX_VALUE).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithMinValue() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MIN_VALUE).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithValueGreaterThanMax() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MAX_VALUE + 1).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithValueLessThanMin() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MIN_VALUE - 1).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addField() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        MutableLocalDateTime fieldMutable = new MutableLocalDateTime();
        MutableLocalDateTimeField monthField = new MutableLocalDateTimeField(field);
        MutableLocalDateTime testDateTime = new MutableLocalDateTime();

        testDateTime.set(2023, 1, 31, 12, 0, 0, 0, ISO_CHRONOLOGY);
        fieldMutable.set(testDateTime);

        field.add(testDateTime.getMillis(), 2);
        assertEquals(DateTimeConstants.MARCH, fieldMutable.getMonthOfYear());

        LocalDate date = fieldMutable.toLocalDate();
        assertEquals(DateTimeConstants.MARCH, date.getMonthOfYear());
        assertEquals(DateTimeConstants.MARCH, date.getMonthOfYear());
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithValueGreaterThanMaxForWrap() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MAX_VALUE + 1).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithValueLessThanMinForWrap() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MIN_VALUE - 1).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithValueGreaterThanMaxPlusOne() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MAX_VALUE + 2).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_addWrapFieldWithValueLessThanMinMinusOne() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.addWrapField(instant, Integer.MIN_VALUE - 2).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_set() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.set(instant, DateTimeConstants.FEBRUARY).getMillis();
        assertEquals(DateTimeConstants.FEBRUARY, iChronology.getMonthOfYear(result));

        result = field.set(instant, DateTimeConstants.MAY).getMillis();
        assertEquals(DateTimeConstants.MAY, iChronology.getMonthOfYear(result));

        result = field.set(instant, DateTimeConstants.DECEMBER + 1).getMillis();
        assertEquals(DateTimeConstants.JANUARY, iChronology.getMonthOfYear(result));
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_setWithMinValue() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.set(instant, DateTimeConstants.JANUARY - 1).getMillis();
        assertEquals(DateTimeConstants.DECEMBER, iChronology.getMonthOfYear(result));
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_setWithMaxValue() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.set(instant, DateTimeConstants.DECEMBER + 1).getMillis();
        assertEquals(DateTimeConstants.JANUARY, iChronology.getMonthOfYear(result));
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_setWithValueGreaterThanMax() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.set(instant, Integer.MAX_VALUE + 1).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_setWithValueLessThanMin() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.set(instant, Integer.MIN_VALUE - 1).getMillis();
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_getDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant1 = BaseDateTime.UTC.getMillis();
        long instant2 = BaseDateTime.UTC.getMillis() + (DateTimeConstants.DAYS_PER_YEAR * 2);

        long result = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(2, result);

        result = field.getDifferenceAsLong(instant2, instant1);
        assertEquals(-2, result);

        long instant3 = BaseDateTime.UTC.getMillis() + (DateTimeConstants.DAYS_PER_YEAR * 5);

        result = field.getDifferenceAsLong(instant1, instant3);
        assertEquals(3, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_roundFloor() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.roundFloor(instant);
        assertEquals(instant, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_remainder() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        long instant = BaseDateTime.UTC.getMillis();

        long result = field.remainder(instant);
        assertEquals(0, result);

        long instant2 = BaseDateTime.UTC.getMillis() + (DateTimeConstants.DAYS_PER_YEAR / 2);

        result = field.remainder(instant2);
        assertEquals(DateTimeConstants.DAYS_PER_YEAR / 2, result);
    }

    @org.junit.Test
    public void testBasicMonthOfYearDateTimeField_serialization() {
        BasicMonthOfYearDateTimeField field = ISO_CHRONOLOGY.monthOfYear();
        mutableFieldSerialTest(field);
    }
}