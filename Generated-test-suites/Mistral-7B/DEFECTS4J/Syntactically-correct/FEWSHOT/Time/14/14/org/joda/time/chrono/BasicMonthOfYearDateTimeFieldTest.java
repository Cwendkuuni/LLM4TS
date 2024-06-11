package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology CHRONOLOGY = new BasicChronology("BasicChronology", DateTimeConstants.MIN_YEAR, DateTimeConstants.MAX_YEAR);
    private static final BasicMonthOfYearDateTimeField MONTH_FIELD = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    @Test
    public void testBasic() {
        assertEquals(DateTimeFieldType.monthOfYear(), MONTH_FIELD.getDateTimeFieldType());
        assertEquals(FieldUtils.WRAP_FIELD_BEHAVIOR_ALWAYS, MONTH_FIELD.getWrapFieldBehavior());
        assertEquals(CHRONOLOGY, MONTH_FIELD.getChronology());
        assertEquals(CHRONOLOGY.getAverageMillisPerMonth(), MONTH_FIELD.getMillisPerDay());
        assertEquals(0, MONTH_FIELD.getMinimumValue());
        assertEquals(12, MONTH_FIELD.getMaximumValue());
        assertNull(MONTH_FIELD.getRangeDurationField());
        assertFalse(MONTH_FIELD.isLenient());
    }

    @Test
    public void testGet() {
        // Get tests
        long instant = 0L;
        for (int month = 1; month <= 12; month++) {
            MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
            date.setField(MONTH_FIELD, month);
            assertEquals(month, MONTH_FIELD.get(instant));
            instant += CHRONOLOGY.getMillisPerDay(1);
        }
    }

    @Test
    public void testSet() {
        // Set tests
        long instant = 0L;
        long expected = 0L;
        for (int month = 1; month <= 12; month++) {
            MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
            int newMonth = (month + 1) % 12;
            date.setField(MONTH_FIELD, newMonth);
            expected += CHRONOLOGY.getMillisPerDay(1);
            assertEquals(expected, date.getMillis());
            instant = date.getMillis();
        }
    }

    @Test
    public void testAdd() {
        // Add tests
        long instant = 0L;
        int months = 0;
        for (int i = 0; i < 10; i++) {
            MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
            long result = MONTH_FIELD.add(instant, months);
            date.setMillis(result);
            assertEquals(instant + CHRONOLOGY.getMillisPerDay(1) * months, result);
            months += 2;
            instant = result;
        }

        // Test large addition
        MutableDateTime date = new MutableDateTime(0L, CHRONOLOGY);
        long result = MONTH_FIELD.add(instant, Integer.MAX_VALUE);
        assertEquals(-1, MONTH_FIELD.get(result));
    }

    @Test
    public void testAddWrapField() {
        long instant = 0L;
        for (int month = 1; month <= 12; month++) {
            MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
            int newMonth = (month + 1) % 12;
            long result = MONTH_FIELD.addWrapField(instant, newMonth);
            assertEquals(instant + CHRONOLOGY.getMillisPerDay(1), result);
            instant = result;
        }
    }

    @Test
    public void testGetDifference() {
        long instant1 = 0L;
        long instant2 = 0L;
        for (int months = -10; months <= 10; months += 2) {
            MutableDateTime date1 = new MutableDateTime(instant1, CHRONOLOGY);
            MutableDateTime date2 = new MutableDateTime(instant2, CHRONOLOGY);
            date1.setField(MONTH_FIELD, (MONTH_FIELD.get(instant1) + months) % 12);
            date2.setField(MONTH_FIELD, MONTH_FIELD.get(instant2));
            assertEquals(MONTH_FIELD.getDifferenceAsLong(instant1, instant2), MONTH_FIELD.getDifferenceAsLong(date1.getMillis(), date2.getMillis()));
            instant1 = date1.getMillis();
            instant2 += CHRONOLOGY.getMillisPerDay(1);
        }
    }

    @Test
    public void testAddReadablePartial() {
        // Add tests with ReadablePartial
        ReadablePartial partial = new ReadablePartial();
        partial.setField(MONTH_FIELD, 1);
        long instant = 0L;
        for (int i = 1; i <= 10; i++) {
            long result = MONTH_FIELD.add(partial, MONTH_FIELD.getFieldIndex(), new int[]{1}, i).getMillis();
            assertEquals(instant + CHRONOLOGY.getMillisPerDay(i), result);
            instant = result;
            partial.setMillis(result);
        }
    }

    @Test
    public void testIsLeap() {
        // IsLeap tests
        for (int year = DateTimeConstants.MIN_YEAR; year <= DateTimeConstants.MAX_YEAR; year++) {
            MutableDateTime date = new MutableDateTime(0L, CHRONOLOGY);
            date.setField(MONTH_FIELD, DateTimeConstants.JANUARY);
            date.setField(MONTH_FIELD, DateTimeConstants.FEBRUARY);
            date.setField(MONTH_FIELD, year);
            assertTrue(MONTH_FIELD.isLeap(date.getMillis()));
            date.setField(MONTH_FIELD, DateTimeConstants.MARCH);
            assertFalse(MONTH_FIELD.isLeap(date.getMillis()));
        }
    }

    @Test
    public void testGetLeapAmount() {
        // GetLeapAmount tests
        MutableDateTime date = new MutableDateTime(0L, CHRONOLOGY);
        for (int year = DateTimeConstants.MIN_YEAR; year <= DateTimeConstants.MAX_YEAR; year++) {
            date.setField(MONTH_FIELD, DateTimeConstants.FEBRUARY);
            date.setField(MONTH_FIELD, year);
            assertEquals(MONTH_FIELD.getLeapAmount(date.getMillis()), MONTH_FIELD.isLeap(date.getMillis()) ? 1 : 0);
            date.setField(MONTH_FIELD, DateTimeConstants.MARCH);
            assertEquals(0, MONTH_FIELD.getLeapAmount(date.getMillis()));
        }
    }

    @Test
    public void testGetLeapDurationField() {
        // GetLeapDurationField tests
        assertEquals(CHRONOLOGY.days(), MONTH_FIELD.getLeapDurationField());
    }

    @Test
    public void testMinimumValue() {
        assertEquals(0, MONTH_FIELD.getMinimumValue());
    }

    @Test
    public void testMaximumValue() {
        assertEquals(12, MONTH_FIELD.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        // RoundFloor tests
        long instant = 0L;
        for (int month = 1; month <= 12; month++) {
            MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
            date.setField(MONTH_FIELD, month);
            assertEquals(MONTH_FIELD.roundFloor(date.getMillis()), MONTH_FIELD.roundFloor(instant));
            instant += CHRONOLOGY.getMillisPerDay(1);
        }
    }

    @Test
    public void testRemainder() {
        // Remainder tests
        long instant = 0L;
        for (int month = 1; month <= 12; month++) {
            MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
            date.setField(MONTH_FIELD, month);
            assertEquals(MONTH_FIELD.remainder(date.getMillis()), MONTH_FIELD.remainder(instant));
            instant += CHRONOLOGY.getMillisPerDay(1);
        }
    }

    @Test
    public void testSerializable() {
        // Test serialization
        long instant = 0L;
        MutableDateTime date = new MutableDateTime(instant, CHRONOLOGY);
        date.setField(MONTH_FIELD, 12);
        assertSame(MONTH_FIELD, MONTH_FIELD.readResolve());
        assertEquals(12, MONTH_FIELD.get(date.getMillis()));
    }
}