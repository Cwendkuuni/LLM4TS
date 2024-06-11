import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithValues() {
        PeriodType periodType = PeriodType.standard();
        int[] iValues = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(periodType, iValues);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        assertArrayEquals(iValues, actualValues);
    }

    @Test
    public void testBasePeriodConstructionWithInvalidValues() {
        PeriodType periodType = PeriodType.standard();
        int[] iValues = {1, 2, 3, 4, 5, 6, 7, 9};
        try {
            new BasePeriod(periodType, iValues);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBasePeriodConstructionWithInstant() {
        PeriodType periodType = PeriodType.standard();
        long instantMillis = 1234567890000L;
        BasePeriod period = new BasePeriod(instantMillis, periodType);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the chronology and instant provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodConstructionWithInstantAndChronology() {
        PeriodType periodType = PeriodType.standard();
        long instantMillis = 1234567890000L;
        Chronology chronology = DateTimeUtils.getISOChronology();
        BasePeriod period = new BasePeriod(instantMillis, periodType, chronology);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the chronology and instant provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstant() {
        PeriodType periodType = PeriodType.standard();
        ReadableInstant readableInstant = DateTimeUtils.parseLocalDate("2022-01-01").toInstant();
        BasePeriod period = new BasePeriod(readableInstant, periodType);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the instant provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodConstructionWithReadableDuration() {
        PeriodType periodType = PeriodType.standard();
        ReadableDuration readableDuration = Duration.standardDays(1);
        BasePeriod period = new BasePeriod(readableDuration, periodType);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the duration provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        PeriodType periodType = PeriodType.standard();
        ReadableDuration readableDuration = Duration.standardDays(1);
        ReadableInstant readableInstant = DateTimeUtils.parseLocalDate("2022-01-01").toInstant();
        BasePeriod period = new BasePeriod(readableDuration, readableInstant, periodType);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the duration and instant provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartial() {
        PeriodType periodType = PeriodType.standard();
        ReadablePartial readablePartial = new BaseLocalDate().withYear(2022).withMonthOfYear(1).withDayOfMonth(1);
        BasePeriod period = new BasePeriod(readablePartial, periodType);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the partial provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodConstructionWithObject() {
        PeriodType periodType = PeriodType.standard();
        Object input = Duration.standardDays(1);
        BasePeriod period = new BasePeriod(input, periodType, null);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        // The actual values depend on the input provided
        // To test the correctness of the values, test the methods that set and get them
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(periodType, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
        ReadableInstant readableInstant = DateTimeUtils.parseLocalDate("2022-01-01").toInstant();
        Duration expectedDuration = Duration.standardDays(1).plus(Duration.standardHours(2)).plus(Duration.standardMinutes(3));
        Duration actualDuration = period.toDurationFrom(readableInstant);

        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(periodType, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
        ReadableInstant readableInstant = DateTimeUtils.parseLocalDate("2022-01-01").toInstant();
        Duration expectedDuration = Duration.standardDays(1).minus(Duration.standardHours(2)).minus(Duration.standardMinutes(3));
        Duration actualDuration = period.toDurationTo(readableInstant);

        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void testBasePeriodSetPeriod() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(periodType, new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        ReadablePeriod readablePeriod = Duration.standardDays(1).plus(Duration.standardHours(2)).plus(Duration.standardMinutes(3));
        period.setPeriod(readablePeriod);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = new int[period.size()];
        period.getValues(actualValues);

        assertEquals(readablePeriod.getPeriodType(), actualPeriodType);
        assertArrayEquals(readablePeriod.getValues(), actualValues);
    }

    @Test
    public void testBasePeriodSetField() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(periodType, new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DurationFieldType fieldType = DurationFieldType.years();
        int value = 1;
        period.setField(fieldType, value);
        int actualValue = period.getValue(0);

        assertEquals(value, actualValue);
    }

    @Test
    public void testBasePeriodSetFieldInto() {
        PeriodType periodType = PeriodType.standard();
        int[] iValues = new int[8];
        periodType.getValues(iValues);
        DurationFieldType fieldType = DurationFieldType.years();
        int value = 1;
        periodType.setFieldInto(iValues, fieldType, value);
        int actualValue = iValues[0];

        assertEquals(value, actualValue);
    }

    @Test
    public void testBasePeriodAddField() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(periodType, new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DurationFieldType fieldType = DurationFieldType.years();
        int value = 1;
        period.addField(fieldType, value);
        int actualValue = period.getValue(0);

        assertEquals(value, actualValue);
    }

    @Test
    public void testBasePeriodAddFieldInto() {
        PeriodType periodType = PeriodType.standard();
        int[] iValues = new int[8];
        periodType.getValues(iValues);
        DurationFieldType fieldType = DurationFieldType.years();
        int value = 1;
        periodType.addFieldInto(iValues, fieldType, value);
        int actualValue = iValues[0];

        assertEquals(value, actualValue);
    }

    @Test
    public void testBasePeriodMergePeriod() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period1 = new BasePeriod(periodType, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
        BasePeriod period2 = new BasePeriod(periodType, new int[] {3, 4, 5, 6, 7, 8, 9, 10});
        BasePeriod mergedPeriod = new BasePeriod();
        period1.mergePeriod(period2);
        mergedPeriod.setPeriod(period1);
        PeriodType actualPeriodType = mergedPeriod.getPeriodType();
        int[] actualValues = new int[mergedPeriod.size()];
        mergedPeriod.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        assertArrayEquals(new int[] {4, 6, 8, 10, 7, 9, 9, 11}, actualValues);
    }

    @Test
    public void testBasePeriodAddPeriod() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period1 = new BasePeriod(periodType, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
        BasePeriod period2 = new BasePeriod(periodType, new int[] {3, 4, 5, 6, 7, 8, 9, 10});
        BasePeriod sumPeriod = new BasePeriod();
        period1.addPeriod(period2);
        sumPeriod.setPeriod(period1);
        PeriodType actualPeriodType = sumPeriod.getPeriodType();
        int[] actualValues = new int[sumPeriod.size()];
        sumPeriod.getValues(actualValues);

        assertEquals(periodType, actualPeriodType);
        assertArrayEquals(new int[] {4, 6, 8, 10, 7, 14, 16, 18}, actualValues);
    }
}