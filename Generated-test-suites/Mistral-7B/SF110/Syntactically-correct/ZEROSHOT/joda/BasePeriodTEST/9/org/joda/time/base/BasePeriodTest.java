import org.joda.time.*;
import org.joda.time.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithValues() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithChronologyAndInstantMillis() {
        Chronology chrono = GregorianChronology.getInstance();
        BasePeriod period = new BasePeriod(1234567890000L, 2345678901234L, PeriodType.standard(), chrono);
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{1, 0, 0, 3, 7, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstant() {
        DateTime date1 = new DateTime(2012, 11, 28, 15, 30, 0, 0, DateTimeZone.UTC);
        DateTime date2 = new DateTime(2012, 11, 28, 16, 30, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartial() {
        BaseLocalDate date1 = new BaseLocalDate(2012, 11, 28);
        BaseLocalDate date2 = new BaseLocalDate(2012, 11, 29);
        BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        DateTime date = new DateTime(2012, 11, 28, 15, 30, 0, 0, DateTimeZone.UTC);
        Duration duration = new Duration(1, ChronoUnits.HOURS);
        BasePeriod period = new BasePeriod(duration, date, PeriodType.standard());
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstantReverse() {
        DateTime date = new DateTime(2012, 11, 28, 15, 30, 0, 0, DateTimeZone.UTC);
        Duration duration = new Duration(1, ChronoUnits.HOURS);
        BasePeriod period = new BasePeriod(date, duration, PeriodType.standard());
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithLong() {
        BasePeriod period = new BasePeriod(12345678987654321L);
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 5, 7, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithLongAndPeriodType() {
        BasePeriod period = new BasePeriod(12345678987654321L, PeriodType.isocalendar());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();
        assertEquals(PeriodType.isocalendar(), periodType);
        assertArrayEquals(new int[]{1, 2, 3, 5, 7, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithObject() {
        Period period = new Period(new BigDecimal("1.5"));
        BasePeriod basePeriod = new BasePeriod(period, PeriodType.standard());
        PeriodType periodType = basePeriod.getPeriodType();
        int[] iValues = basePeriod.getValues();
        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithObjectAndPeriodType() {
        Period period = new Period(new BigDecimal("1.5"));
        BasePeriod basePeriod = new BasePeriod(period, PeriodType.standard());
        PeriodType periodType = basePeriod.getPeriodType();
        int[] iValues = basePeriod.getValues();
        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, iValues);
        basePeriod = new BasePeriod(period, PeriodType.isocalendar());
        periodType = basePeriod.getPeriodType();
        iValues = basePeriod.getValues();
        assertEquals(PeriodType.isocalendar(), periodType);
        assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithIntArrayAndPeriodType() {
        int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();
        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(values, iValues);
    }
}