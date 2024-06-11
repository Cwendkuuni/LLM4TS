package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.BaseLocal;
import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.test.TestBase;

public class BasePeriodTest extends TestBase {

    @org.junit.Test
    public void testValidConstruction() {
        final BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }

    @org.junit.Test
    public void testInvalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(-1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, -2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, -3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, 3, -4, 5, 6, 7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, 3, 4, -5, 6, 7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, 3, 4, 5, -6, 7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, 3, 4, 5, 6, -7, 8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, 3, 4, 5, 6, 7, -8, PeriodType.standard()));
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, null));
    }

    @org.junit.Test
    public void testConstructionFromInstant() {
        final Chronology utc = DateTimeUtils.UTC;
        final long instant = LocalDateTime.parse("2015-01-01T00:00:00Z", DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).getMillis();
        final BasePeriod period = new BasePeriod(utc, instant, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @org.junit.Test
    public void testConstructionFromInstantInvalid() {
        final Chronology utc = DateTimeUtils.UTC;
        final long instant = -1L;
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(utc, instant, PeriodType.standard()));
    }

    @org.junit.Test
    public void testConstructionFromReadableInstant() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final LocalDateTime dt2 = new LocalDateTime(2015, 1, 2, 1, 1);
        final BasePeriod period = new BasePeriod(dt1, dt2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(1, period.getValue(DurationFieldType.hours()));
        assertEquals(1, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));

        final LocalDateTime dt3 = new LocalDateTime(2015, 1, 1, 0, 0);
        final LocalDateTime dt4 = null;
        assertThrows(NullPointerException.class, () -> new BasePeriod(dt3, dt4, PeriodType.standard()));
    }

    @org.junit.Test
    public void testConstructionFromReadablePartial() {
        final BaseLocal dl1 = new LocalDate();
        final BaseLocal dl2 = new LocalDate(2015, 1, 2);
        final BasePeriod period = new BasePeriod(dl1, dl2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(1, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));

        final BaseLocal dl3 = new LocalDate();
        final BaseLocal dl4 = null;
        assertThrows(NullPointerException.class, () -> new BasePeriod(dl3, dl4, PeriodType.standard()));

        final LocalTime tl1 = new LocalTime();
        final LocalTime tl2 = new LocalTime(1, 1);
        final BasePeriod period2 = new BasePeriod(tl1, tl2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertEquals(0, period2.getValue(DurationFieldType.years()));
        assertEquals(0, period2.getValue(DurationFieldType.months()));
        assertEquals(0, period2.getValue(DurationFieldType.weeks()));
        assertEquals(0, period2.getValue(DurationFieldType.days()));
        assertEquals(0, period2.getValue(DurationFieldType.hours()));
        assertEquals(1, period2.getValue(DurationFieldType.minutes()));
        assertEquals(1, period2.getValue(DurationFieldType.seconds()));
        assertEquals(0, period2.getValue(DurationFieldType.millis()));

        final LocalTime tl3 = new LocalTime();
        final LocalTime tl4 = null;
        assertThrows(NullPointerException.class, () -> new BasePeriod(tl3, tl4, PeriodType.standard()));
    }

    @org.junit.Test
    public void testConstructionFromReadableInstantAndDuration() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final Duration dt2 = new Duration(1, 1, 1, 1);
        final BasePeriod period = new BasePeriod(dt1, dt2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(1, period.getValue(DurationFieldType.hours()));
        assertEquals(1, period.getValue(DurationFieldType.minutes()));
        assertEquals(1, period.getValue(DurationFieldType.seconds()));
        assertEquals(1, period.getValue(DurationFieldType.millis()));

        final LocalDateTime dt3 = new LocalDateTime(2015, 1, 1, 0, 0);
        final Duration dt4 = null;
        assertThrows(NullPointerException.class, () -> new BasePeriod(dt3, dt4, PeriodType.standard()));
    }

    @org.junit.Test
    public void testConstructionFromReadableDurationAndReadableInstant() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final Duration dt2 = new Duration(1, 1, 1, 1);
        final BasePeriod period = new BasePeriod(dt2, dt1, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(1, period.getValue(DurationFieldType.hours()));
        assertEquals(1, period.getValue(DurationFieldType.minutes()));
        assertEquals(1, period.getValue(DurationFieldType.seconds()));
        assertEquals(1, period.getValue(DurationFieldType.millis()));

        final LocalDateTime dt3 = new LocalDateTime(2015, 1, 1, 0, 0);
        final Duration dt4 = new Duration(-1, -1, -1, -1);
        assertThrows(IllegalArgumentException.class, () -> new BasePeriod(dt4, dt3, PeriodType.standard()));
    }

    @org.junit.Test
    public void testToDurationFrom() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final BasePeriod period = new BasePeriod(dt1, dt1.plusDays(1), PeriodType.standard());
        final Duration duration = period.toDurationFrom(dt1);
        assertEquals(Duration.standardDays(1), duration);
    }

    @org.junit.Test
    public void testToDurationTo() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final BasePeriod period = new BasePeriod(dt1, dt1.plusDays(1), PeriodType.standard());
        final Duration duration = period.toDurationTo(dt1.plusDays(1));
        assertEquals(Duration.standardDays(1), duration);
    }

    @org.junit.Test
    public void testMergePeriod() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final LocalDateTime dt2 = new LocalDateTime(2015, 1, 2, 1, 1);
        final BasePeriod period1 = new BasePeriod(dt1, dt2, PeriodType.standard());
        final BasePeriod period2 = new BasePeriod(dt2, dt1, PeriodType.standard());
        period1.mergePeriod(period2);
        assertEquals(Duration.standardDays(-1), period1.toDurationFrom(dt1));
    }

    @org.junit.Test
    public void testAddPeriod() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final LocalDateTime dt2 = new LocalDateTime(2015, 1, 2, 1, 1);
        final BasePeriod period1 = new BasePeriod(dt1, dt2, PeriodType.standard());
        final BasePeriod period2 = new BasePeriod(PeriodType.standardDays(2), null);
        period1.addPeriod(period2);
        assertEquals(new LocalDateTime(2015, 1, 4, 1, 1), period1.toLocalDate().toLocalDateTime());
    }

    @org.junit.Test
    public void testSetPeriod() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final BasePeriod period = new BasePeriod(dt1, dt1.plusDays(1), PeriodType.standard());
        final int[] values = period.getValues();
        period.setPeriod(PeriodType.standardDays(2));
        assertEquals(PeriodType.standardDays(2), period.getPeriodType());
        assertEquals(2, values[0]);
        assertEquals(0, values[1]);
        assertEquals(0, values[2]);
        assertEquals(0, values[3]);
        assertEquals(0, values[4]);
        assertEquals(0, values[5]);
        assertEquals(0, values[6]);
        assertEquals(0, values[7]);
    }

    @org.junit.Test
    public void testSetPeriodFromString() {
        final LocalDateTime dt1 = new LocalDateTime(2015, 1, 1, 0, 0);
        final BasePeriod period = new BasePeriod(dt1, dt1.plusDays(1), PeriodType.standard());
        period.setPeriod("P2D");
        assertEquals(PeriodType.standardDays(2), period.getPeriodType());
        assertEquals(2, period.getValue(DurationFieldType.days()));
    }
}