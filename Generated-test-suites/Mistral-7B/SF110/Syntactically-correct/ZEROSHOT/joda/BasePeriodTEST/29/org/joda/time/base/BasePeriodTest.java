import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithInts() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithLongs() {
        BasePeriod period = new BasePeriod(1L, 2L, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, 1);
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstants() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-02-01"), PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[]{31, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartials() {
        BasePartial basePartial1 = new BaseLocalDate(2022, 1, 1);
        BasePartial basePartial2 = new BaseLocalDate(2022, 2, 1);
        BasePeriod period = new BasePeriod(basePartial1, basePartial2, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[]{31, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstantAndDuration() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), new Duration(1, DurationFieldType.days()), PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[]{31, 0, 1, 0, 0, 0, 0, 0};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        BasePeriod period = new BasePeriod(new Duration(1, DurationFieldType.days()), DateTimeUtils.parseLocalDate("2022-01-01"), PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[]{0, 0, 1, 0, 0, 0, 0, 0};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithObject() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), PeriodType.standard(), Period.days(1));
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[]{31, 0, 1, 0, 0, 0, 0, 0};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodGetters() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-02-01"), PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[]{31, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(31, period.getValue(DurationFieldType.days()));
        assertEquals(-31, period.getValue(DurationFieldType.days().negate()));
        assertEquals(1, period.getValue(DurationFieldType.months()));
        assertEquals(-1, period.getValue(DurationFieldType.months().negate()));
        assertEquals(1, period.getValue(DurationFieldType.weeks().multiply(7)));
        assertEquals(-1, period.getValue(DurationFieldType.weeks().multiply(-7)));
        assertEquals(1, period.getValue(DurationFieldType.years().multiply(365)));
        assertEquals(-1, period.getValue(DurationFieldType.years().multiply(-365)));
        assertEquals(1, period.getValue(DurationFieldType.days().multiply(365).multiply(12).multiply(1)));
        assertEquals(-1, period.getValue(DurationFieldType.days().multiply(-365).multiply(12).multiply(1)));
        assertEquals(1, period.getValue(DurationFieldType.years().multiply(365).multiply(12).multiply(1)));
        assertEquals(-1, period.getValue(DurationFieldType.years().multiply(-365).multiply(12).multiply(1)));
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-02-01"), PeriodType.standard());
        Duration actualDuration = period.toDurationFrom(DateTimeUtils.parseLocalDate("2022-02-01"));
        Duration expectedDuration = new Duration(1, DurationFieldType.days());
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-02-01"), PeriodType.standard());
        Duration actualDuration = period.toDurationTo(DateTimeUtils.parseLocalDate("2022-02-01"));
        Duration expectedDuration = new Duration(1, DurationFieldType.days());
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void testBasePeriodSetters() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setValue(DurationFieldType.days(), 10);
        int[] actualValues = new int[8];
        System.arraycopy(period.getValues(), 0, actualValues, 0, 8);
        int[] expectedValues = {0, 0, 0, 10, 0, 0, 0, 0};
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodSettersInvalidFieldType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setValue(DurationFieldType.YEARS(), 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodSettersInvalidValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setValue(DurationFieldType.days(), 1000);
    }
}