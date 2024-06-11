import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstruction() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodFromInstant() {
        BasePeriod period = new BasePeriod(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {31, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodFromReadablePartial() {
        BaseLocalDate date1 = new GregorianLocalDate(2022, 1, 1);
        BaseLocalDate date2 = new GregorianLocalDate(2022, 1, 2);
        BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {31, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodFromReadableInstantAndDuration() {
        DateTime instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        Duration duration = new Duration(2, 3, 4, 5, 6, 7);
        BasePeriod period = new BasePeriod(instant, duration, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {2, 3, 4, 5, 6, 7, 0, 0};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodFromReadableDurationAndReadableInstant() {
        DateTime instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        Duration duration = new Duration(2, 3, 4, 5, 6, 7);
        BasePeriod period = new BasePeriod(duration, instant, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {2, 3, 4, 5, 6, 7, 0, 0};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodFromObject() {
        Duration period = new Duration(2, 3, 4, 5, 6, 7);
        BasePeriod basePeriod = new BasePeriod(period, PeriodType.standard(), DateTimeZone.UTC.getCalendarInstance());
        PeriodType actualPeriodType = basePeriod.getPeriodType();
        int[] actualValues = new int[basePeriod.size()];
        System.arraycopy(basePeriod.getValues(), 0, actualValues, 0, basePeriod.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {2, 3, 4, 5, 6, 7, 0, 0};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodFromIntArrayAndPeriodType() {
        int[] values = {31, 1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        System.arraycopy(period.getValues(), 0, actualValues, 0, period.size());
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {31, 1, 2, 3, 4, 5, 6, 7};
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
        assertArrayEquals("Incorrect period values", expectedValues, actualValues);
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        PeriodType expectedPeriodType = PeriodType.standard();
        assertEquals("Incorrect period type", expectedPeriodType, actualPeriodType);
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int actualValue = period.getValue(DurationFieldType.days());
        int expectedValue = 1;
        assertEquals("Incorrect value for field 'days'", expectedValue, actualValue);
    }

    @Test
    public void testToDurationFromReadableInstant() {
        DateTime instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationFrom(instant);
        long expectedDurationMillis = 31L * 24L * 60L * 60L * 1000L + 2L * 60L * 60L * 1000L + 3L * 60L * 1000L + 4L * 1000L + 5L * 60L * 1000L + 6L * 60L * 1000L + 7L * 1000L + 8L * 1000L;
        long actualDurationMillis = duration.getMillis();
        assertEquals("Incorrect duration for 'toDurationFrom'", expectedDurationMillis, actualDurationMillis);
    }

    @Test
    public void testToDurationToReadableInstant() {
        DateTime instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(instant);
        long expectedDurationMillis = -(31L * 24L * 60L * 60L * 1000L + 2L * 60L * 60L * 1000L + 3L * 60L * 1000L + 4L * 1000L + 5L * 60L * 1000L + 6L * 60L * 1000L + 7L * 1000L + 8L * 1000L);
        long actualDurationMillis = duration.getMillis();
        assertEquals("Incorrect duration for 'toDurationTo'", expectedDurationMillis, actualDurationMillis);
    }
}