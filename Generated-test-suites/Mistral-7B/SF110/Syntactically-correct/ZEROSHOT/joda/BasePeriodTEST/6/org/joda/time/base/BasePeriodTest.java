import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodWithValidArguments() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, values);

        period = new BasePeriod(1234567890123L, PeriodType.standard(), ISOChronology.getInstanceUTC());
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 84674400000L}, values);

        ReadableInstant start = new DateTime(0L);
        ReadableInstant end = new DateTime(1234567890123L);
        period = new BasePeriod(start, end, PeriodType.standard());
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[]{1, 0, 0, 0, 0, 0, 0, 84674400000L}, values);

        ReadablePartial startPartial = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        ReadablePartial endPartial = new LocalDateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC);
        period = new BasePeriod(startPartial, endPartial, PeriodType.standard());
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 86400000L}, values);

        ReadableInstant startInstant = new DateTime(0L);
        ReadableDuration duration = new Duration(1234567890123L);
        period = new BasePeriod(duration, startInstant, PeriodType.standard());
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 84674400000L}, values);

        ReadableDuration duration2 = new Duration(1234567890123L);
        period = new BasePeriod(startInstant, duration2, PeriodType.standard());
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, -84674400000L}, values);

        period = new BasePeriod(0L, PeriodType.standard(), ISOChronology.getInstanceUTC());
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, values);

        period = new BasePeriod(0L, PeriodType.standard(), null);
        periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        values = period.getValues();
        assertArrayEquals(new int[8], values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithInvalidArguments() {
        new BasePeriod(null, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithNullReadableInstant() {
        new BasePeriod(new DateTime(0L), null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithNullReadablePartial() {
        new BasePeriod(new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithNonContiguousReadablePartial() {
        LocalDateTime startPartial = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        LocalDateTime endPartial = new LocalDateTime(2022, 1, 3, 0, 0, 0, 0, DateTimeZone.UTC);
        new BasePeriod(startPartial, endPartial, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithReadablePeriodOfDifferentTypes() {
        ReadablePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadablePeriod period2 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.days());
        period1.mergePeriod(period2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithInvalidFieldType() {
        ReadablePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.addField(DurationFieldType.years(), -1);
    }

    @Test
    public void testBasePeriodGettersAndSetters() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period.setField(DurationFieldType.years(), 2);
        assertEquals(2, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.years()));
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period.addField(DurationFieldType.months(), 3);
        assertEquals(2, period.getValue(DurationFieldType.years()));
        assertEquals(5, period.getValue(DurationFieldType.months()));
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period.mergePeriod(new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard()));
        assertEquals(3, period.getValue(DurationFieldType.years()));
        assertEquals(6, period.getValue(DurationFieldType.months()));
        assertEquals(4, period.getValue(DurationFieldType.weeks()));
        assertEquals(5, period.getValue(DurationFieldType.days()));
        assertEquals(7, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(8, period.getValue(DurationFieldType.seconds()));
        assertEquals(1, period.getValue(DurationFieldType.millis()));
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodToDuration() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration durationFrom = period.toDurationFrom(new DateTime(0L));
        Duration durationTo = period.toDurationTo(new DateTime(0L));
        assertEquals(period, durationFrom.toPeriod());
        assertEquals(period, durationTo.toPeriod());
        assertEquals(-new Duration(0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8), durationFrom);
        assertEquals(new Duration(0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8), durationTo);
    }
}