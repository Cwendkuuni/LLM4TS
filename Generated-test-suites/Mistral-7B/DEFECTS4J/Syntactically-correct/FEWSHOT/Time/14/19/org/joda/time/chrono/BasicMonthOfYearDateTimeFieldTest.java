package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.LocalDate;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.ISO_8601_DATETIME, DateTimeConstants.JANUARY);

    @Test
    public void testGetMinValue() {
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @Test
    public void testGetMaxValue() {
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @Test
    public void testIsLeap() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2000, 2, 29).toDate());
        assertTrue(field.isLeap(instant));
    }

    @Test
    public void testIsLeapFalse() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2001, 2, 28).toDate());
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGet() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 3, 23).toDate());
        int month = field.get(instant);
        assertEquals(3, month);
    }

    @Test
    public void testAdd() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 3, 23).toDate());
        long result = field.add(instant, 1);
        long expected = DateTimeUtils.toMillis(LocalDate.of(2022, 4, 23).toDate());
        assertEquals(expected, result);
    }

    @Test
    public void testAddNegative() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 4, 23).toDate());
        long result = field.add(instant, -1);
        long expected = DateTimeUtils.toMillis(LocalDate.of(2022, 3, 23).toDate());
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 13, 23).toDate());
        long result = field.addWrapField(instant, 1);
        long expected = DateTimeUtils.toMillis(LocalDate.of(2022, 1, 23).toDate());
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapFieldNegative() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 1, 23).toDate());
        long result = field.addWrapField(instant, -1);
        long expected = DateTimeUtils.toMillis(LocalDate.of(2022, 13, 23).toDate());
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 3, 23).toDate());
        long result = field.set(instant, DateTimeConstants.MAY);
        long expected = DateTimeUtils.toMillis(LocalDate.of(2022, 5, 23).toDate());
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidValue() {
        long instant = DateTimeUtils.toMillis(LocalDate.of(2022, 3, 23).toDate());
        field.set(instant, 13);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = DateTimeUtils.toMillis(LocalDate.of(2022, 3, 23).toDate());
        long instant2 = DateTimeUtils.toMillis(LocalDate.of(2022, 5, 23).toDate());
        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(2, difference);
    }

    @Test
    public void testAddReadablePartial() {
        LocalDate initial = LocalDate.of(2022, 3, 23);
        ReadablePartial partial = new ReadablePartial() {
            @Override
            public DateTimeFieldType getFieldType(int i) {
                return field;
            }

            @Override
            public int getValue(int i) {
                return initial.getMonthOfYear();
            }

            @Override
            public int size() {
                return 1;
            }
        };
        LocalDate result = new LocalDate(2022, 5, 23);
        long[] values = new long[1];
        values[0] = 2;
        LocalDate[] expected = new LocalDate[]{result};
        field.add(partial, 0, values, 1);
        assertEquals(expected[0], partial.getValues(values)[0]);
    }

    @Test
    public void testAddWrapFieldReadablePartial() {
        LocalDate initial = LocalDate.of(2022, 13, 23);
        ReadablePartial partial = new ReadablePartial() {
            @Override
            public DateTimeFieldType getFieldType(int i) {
                return field;
            }

            @Override
            public int getValue(int i) {
                return initial.getMonthOfYear();
            }

            @Override
            public int size() {
                return 1;
            }
        };
        LocalDate result = LocalDate.of(2022, 1, 23);
        long[] values = new long[1];
        values[0] = 1;
        LocalDate[] expected = new LocalDate[]{result};
        field.add(partial, 0, values, 1);
        assertEquals(expected[0], partial.getValues(values)[0]);
    }
}