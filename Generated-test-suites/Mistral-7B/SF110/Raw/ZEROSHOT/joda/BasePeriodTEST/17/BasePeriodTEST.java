import org.joda.time.*;
import org.joda.time.base.BasePeriod;
import org.joda.time.duration.Duration;
import org.joda.time.duration.DurationFieldType;
import org.joda.time.field.DecimalFields;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.IntervalFields;
import org.joda.time.field.LocalFieldType;
import org.joda.time.field.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstruction() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());

        period = new BasePeriod(1000L, PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        expectedValues = new int[8];
        System.arraycopy(period.getValues(), 0, expectedValues, 0, 8);
        assertArrayEquals(expectedValues, period.getValues());

        period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-05"), PeriodType.dayTime());
        assertNotNull(period);
        assertEquals(PeriodType.dayTime(), period.getPeriodType());
        assertEquals(3, period.getValues(DurationFieldType.days()).getValue());

        period = new BasePeriod(DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000"), DateTimeUtils.parseLocalDateTime("2022-01-02T15:35:10.500"), PeriodType.standard());
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Duration.standardDays(1).plus(Duration.standardHours(3)).plus(Duration.standardMinutes(15)).plus(Duration.standardSeconds(25)).plus(Duration.standardMillis(500)).toStandardDuration().getMillis(), period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-02T15:35:10.500")).getMillis());

        period = new BasePeriod(Duration.standardDays(1), DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000"), PeriodType.standard());
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Duration.standardDays(1).toStandardDuration().getMillis(), period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000")).getMillis());

        period = new BasePeriod(Duration.standardDays(1), DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000"), PeriodType.standard(), ISOChronology.getInstanceUTC());
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Duration.standardDays(1).getMillis(), period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000")).getMillis());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        int[] expectedPeriodValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedPeriodValues, period.getValues());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.setField(DurationFieldType.years(), 10);
        expectedPeriodValues = new int[] {10, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedPeriodValues, period.getValues());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.setField(DurationFieldType.years(), 10);
        period.setField(DurationFieldType.months(), 2);
        expectedPeriodValues = new int[] {10, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedPeriodValues, period.getValues());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.addField(DurationFieldType.days(), 1);
        expectedPeriodValues = new int[] {1, 2, 3, 4, 5, 6, 7, 9};
        assertArrayEquals(expectedPeriodValues, period.getValues());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.mergePeriod(new BasePeriod(new int[] {2, 3}, PeriodType.standard()));
        expectedPeriodValues = new int[] {1, 2, 3, 4, 5, 6, 7, 10};
        assertArrayEquals(expectedPeriodValues, period.getValues());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        period.addPeriod(new BasePeriod(new int[] {1, 1, 1}, PeriodType.standard()));
        expectedPeriodValues = new int[] {2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expectedPeriodValues, period.getValues());
    }

    @Test
    public void testBasePeriodGetters() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
        assertEquals(Duration.standardDays(1).getMillis(), period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000")).getMillis());
        assertEquals(Duration.standardDays(1).plus(Duration.standardHours(3)).plus(Duration.standardMinutes(15)).plus(Duration.standardSeconds(25)).plus(Duration.standardMillis(500)).toStandardDuration().getMillis(), period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-02T15:35:10.500")).getMillis());

        period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-05"), PeriodType.dayTime());
        assertEquals(PeriodType.dayTime(), period.getPeriodType());
        assertEquals(3, period.getValues(DurationFieldType.days()).getValue());

        period = new BasePeriod(DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000"), DateTimeUtils.parseLocalDateTime("2022-01-02T15:35:10.500"), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Duration.standardDays(1).getMillis(), period.toDurationFrom(DateTimeUtils.parseLocalDateTime("2022-01-02T15:35:10.500")).getMillis());
        assertEquals(Duration.standardDays(1).plus(Duration.standardHours(3)).plus(Duration.standardMinutes(15)).plus(Duration.standardSeconds(25)).plus(Duration.standardMillis(500)).toStandardDuration().getMillis(), period.toDurationTo(DateTimeUtils.parseLocalDateTime("2022-01-01T12:30:45.000")).getMillis());

        period = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
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
}