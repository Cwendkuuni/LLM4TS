package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.LocalField;
import org.joda.time.field.LocalFields;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        final ISOChronology instanceUTC = ISOChronology.getInstanceUTC();
        final ReadableInstant readableInstant = instanceUTC.getUTCInstants().get(0);
        final ReadableInstant readableInstant2 = instanceUTC.getUTCInstants().get(1);
        final LocalField localField = LocalFields.MILLISECONDS;
        final DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        final int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
        assertEquals(0, result);

        final long diff = readableInstant2.getMillis() - readableInstant.getMillis();
        final int result2 = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
        assertEquals(diff, result2);

        final int millisDiff = (int) Math.abs(diff);
        final int result3 = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
        assertEquals(millisDiff, result3);

        final ReadablePeriod readablePeriod = Period.MILLISECONDS.withMillis(-diff);
        final int result4 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1000);
        assertEquals(1, result4);

        final ReadablePeriod readablePeriod2 = Period.MILLISECONDS.withMillis(-diff / 2);
        final int result5 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod2, 500);
        assertEquals(1, result5);
    }

    @Test
    public void testBetween_BadArguments() {
        final ReadableInstant readableInstant = null;
        final ReadableInstant readableInstant2 = new ReadableInstant();
        final DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        try {
            BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        final ReadablePartial readablePartial = null;
        final ReadablePeriod readablePeriod = Period.MILLISECONDS.withMillis(1);
        try {
            BaseSingleFieldPeriod.between(readablePartial, readablePeriod, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        final ReadablePartial readablePartial2 = new ReadablePartial();
        final ReadablePeriod readablePeriod2 = Period.MILLISECONDS.withMillis(1);
        try {
            BaseSingleFieldPeriod.between(readablePartial, readablePeriod2, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        final ReadablePeriod readablePeriod3 = Period.MILLISECONDS.withMillis(1);
        final ReadablePeriod readablePeriod4 = Period.MILLISECONDS.withMillis(-1);
        try {
            BaseSingleFieldPeriod.between(readablePeriod3, readablePeriod4, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        final ReadablePeriod readablePeriod5 = Period.MILLISECONDS.withMillis(1);
        final ReadablePartial readablePartial3 = new ReadablePartial();
        try {
            BaseSingleFieldPeriod.between(readablePeriod5, readablePartial3, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        final ReadablePeriod readablePeriod6 = Period.MILLISECONDS.withMillis(1);
        final ReadablePartial readablePartial4 = new ReadablePartial().withField(LocalFields.MILLISECONDS, 2);
        try {
            BaseSingleFieldPeriod.between(readablePeriod6, readablePartial4, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        final PeriodType periodType = PeriodType.MILLISECONDS;
        final DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        final int millis = 1234;
        final ReadablePeriod readablePeriod = Period.MILLISECONDS.withMillis(millis);
        final int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1);
        assertEquals(millis, result);

        final int result2 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 2);
        assertEquals(millis * 2, result2);

        final int result3 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 3);
        assertEquals(millis * 3, result3);

        final int result4 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 0);
        assertEquals(0, result4);

        final int result5 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, -2);
        assertEquals(millis * 2, result5);

        final int result6 = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, -3);
        assertEquals(millis * 3, result6);

        final int result7 = BaseSingleFieldPeriod.standardPeriodIn(null, 1);
        assertNull(result7);

        final int result8 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), 1);
        assertEquals(1, result8);

        final int result9 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), 2);
        assertEquals(1 * 2, result9);

        final int result10 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), 3);
        assertEquals(1 * 3, result10);

        final int result11 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), 0);
        assertEquals(0, result11);

        final int result12 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), -2);
        assertEquals(1 * 2, result12);

        final int result13 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), -3);
        assertEquals(1 * 3, result13);

        final int result14 = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), -10);
        assertEquals(1 * 10, result14);
    }

    @Test
    public void testSize() {
        final BaseSingleFieldPeriod period = new BaseFixedPeriod(1);
        assertEquals(1, period.size());

        final BaseSingleFieldPeriod period2 = new BaseFixedPeriod(2);
        assertEquals(1, period2.size());

        final BaseSingleFieldPeriod period3 = new BaseFixedPeriod(3);
        assertEquals(1, period3.size());
    }

    @Test
    public void testGetFieldType() {
        final LocalField localField = LocalFields.MILLISECONDS;
        final DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        final BaseSingleFieldPeriod period = new BaseFixedPeriod(1);
        assertEquals(durationFieldType, period.getFieldType());
        assertEquals(localField, period.getFieldType().getField(ISOChronology.getInstanceUTC()));
    }

    @Test
    public void testGetPeriodType() {
        final BaseSingleFieldPeriod period = new BaseFixedPeriod(1);
        final PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.MILLISECONDS, periodType);
    }

    @Test
    public void testHashCode() {
        final int iPeriod1 = 1;
        final int iPeriod2 = 2;
        final BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(iPeriod1);
        final BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(iPeriod2);
        final int hashCode1 = period1.hashCode();
        final int hashCode2 = period2.hashCode();
        final int expectedHashCode = 27 * (27 * 17 + iPeriod1) + durationFieldType.hashCode();
        assertEquals(expectedHashCode, hashCode1);
        assertEquals(expectedHashCode, hashCode2);

        final BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(iPeriod1);
        final int hashCode3 = period3.hashCode();
        assertEquals(expectedHashCode, hashCode3);

        final BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(iPeriod2);
        final int hashCode4 = period4.hashCode();
        assertEquals(expectedHashCode, hashCode4);

        final BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(iPeriod1 + iPeriod2);
        final int hashCode5 = period5.hashCode();
        assertEquals(expectedHashCode * 27 + iPeriod2, hashCode5);
    }

    @Test
    public void testEquals() {
        final int iPeriod1 = 1;
        final int iPeriod2 = 1;
        final BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(iPeriod1);
        final BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(iPeriod2);
        assertTrue(period1.equals(period2));

        final BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(iPeriod2);
        assertFalse(period1.equals(period3));

        final BaseSingleFieldPeriod period4 = new BaseFixedPeriod(2);
        assertFalse(period1.equals(period4));

        final BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(iPeriod1);
        assertTrue(period1.equals(period5));

        final BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(-iPeriod1);
        assertFalse(period1.equals(period6));

        final BaseSingleFieldPeriod period7 = new BaseSingleFieldPeriod(iPeriod1 + 1);
        assertFalse(period1.equals(period7));

        final Object obj = new Object();
        assertFalse(period1.equals(obj));

        final BaseSingleFieldPeriod period8 = null;
        assertFalse(period1.equals(period8));
    }

    @Test
    public void testCompareTo() {
        final int iPeriod1 = 1;
        final int iPeriod2 = 2;
        final BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(iPeriod1);
        final BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(iPeriod2);
        assertEquals(period2.compareTo(period1), -1);

        final BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(-iPeriod1);
        assertEquals(period3.compareTo(period1), -1);

        final BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(-iPeriod2);
        assertEquals(period1.compareTo(period4), 1);

        final BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(iPeriod1 + 1);
        assertEquals(period5.compareTo(period1), 1);

        final BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(iPeriod1 + iPeriod2);
        assertEquals(period6.compareTo(period1), 1);

        final BaseSingleFieldPeriod period7 = new BaseSingleFieldPeriod(iPeriod1);
        assertEquals(period1.compareTo(period7), 0);

        final BaseSingleFieldPeriod period8 = new BaseSingleFieldPeriod(iPeriod1);
        assertEquals(period1.compareTo(period8), 0);

        final BaseSingleFieldPeriod period9 = new BaseSingleFieldPeriod(0);
        assertEquals(period1.compareTo(period9), 1);

        final BaseSingleFieldPeriod period10 = new BaseSingleFieldPeriod(Integer.MIN_VALUE);
        assertEquals(period1.compareTo(period10), 1);
    }

    @Test
    public void testToPeriod() {
        final int iPeriod = 1;
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(iPeriod);
        final Period expectedResult = Period.MILLISECONDS.withMillis(iPeriod * 1000);
        final Period result = period.toPeriod();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToMutablePeriod() {
        final int iPeriod = 1;
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(iPeriod);
        final MutablePeriod expectedResult = new MutablePeriod().withFields(period);
        final MutablePeriod result = period.toMutablePeriod();
        assertEquals(expectedResult, result);
    }
}