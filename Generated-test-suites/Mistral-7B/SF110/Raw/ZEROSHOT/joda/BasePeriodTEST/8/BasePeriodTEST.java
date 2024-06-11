import org.joda.time.BasePeriod;
import org.joda.time.BaseLocal;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodCreationWithArguments() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] actualValues = period.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodCreationWithInstant() {
        LocalDateTime instant = new LocalDateTime(2023, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(instant, instant.plusDays(1), BasePeriod.PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] expectedValues = DateTimeUtils.getInstantValues(instant, 1);
        int[] actualValues = period.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodCreationWithReadablePeriod() {
        LocalDate startDate = new LocalDate(2023, 1, 1);
        LocalDate endDate = new LocalDate(2023, 1, 3);
        BasePeriod period = new BasePeriod(new BaseLocal(startDate), new BaseLocal(endDate), BasePeriod.PeriodType.days());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] expectedValues = new int[]{0, 2};
        int[] actualValues = period.getValues();
        assertEquals(BasePeriod.PeriodType.days(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodCreationWithReadableInstantAndReadableDuration() {
        LocalDateTime startInstant = new LocalDateTime(2023, 1, 1, 12, 0, 0);
        Duration duration = Duration.standardDays(2);
        BasePeriod period = new BasePeriod(startInstant, duration, BasePeriod.PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] expectedValues = DateTimeUtils.getInstantValues(startInstant, 2);
        int[] actualValues = period.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testBasePeriodCreationWithReadableDurationAndReadableInstant() {
        LocalDateTime startInstant = new LocalDateTime(2023, 1, 1, 12, 0, 0);
        Duration duration = Duration.standardDays(2);
        BasePeriod period = new BasePeriod(duration, startInstant, BasePeriod.PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] expectedValues = DateTimeUtils.getInstantValues(startInstant, -2);
        int[] actualValues = period.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCreationWithNullReadablePartial() {
        new BasePeriod(null, null, BasePeriod.PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCreationWithNonContiguousReadablePartial() {
        LocalDate startDate = new LocalDate(2023, 1, 1);
        LocalDate endDate = new LocalDate(2023, 1, 3);
        LocalDate midDate = new LocalDate(2023, 1, 2);
        new BasePeriod(new BaseLocal(startDate), new BaseLocal(midDate), BasePeriod.PeriodType.days());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCreationWithDifferentFieldTypesReadablePartial() {
        LocalDate startDate = new LocalDate(2023, 1, 1);
        LocalDateTime startDateTime = new LocalDateTime(2023, 1, 1, 12, 0, 0);
        new BasePeriod(new BaseLocal(startDate), new BaseLocal(startDateTime), BasePeriod.PeriodType.standard());
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        assertEquals(BasePeriod.PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.months().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(7, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(8, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testToDurationFrom() {
        LocalDateTime instant = new LocalDateTime(2023, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(instant, instant.plusDays(1), BasePeriod.PeriodType.days());
        Duration expectedDuration = Duration.standardDays(1);
        Duration actualDuration = period.toDurationFrom(instant);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void testToDurationTo() {
        LocalDateTime instant = new LocalDateTime(2023, 1, 1, 12, 0, 0);
        BasePeriod period = new BasePeriod(instant, instant.plusDays(1), BasePeriod.PeriodType.days());
        Duration expectedDuration = Duration.standardDays(-1);
        Duration actualDuration = period.toDurationTo(instant);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        BasePeriod periodToSet = new BasePeriod(1, 3, 3, 3, 3, 3, 3, 3, BasePeriod.PeriodType.standard());
        period.setPeriod(periodToSet);
        PeriodType actualPeriodType = period.getPeriodType();
        int[] expectedValues = new int[]{1, 3, 3, 3, 3, 3, 3, 3};
        int[] actualValues = period.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, BasePeriod.PeriodType.standard());
        period1.mergePeriod(period2);
        PeriodType actualPeriodType = period1.getPeriodType();
        int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] actualValues = period1.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, BasePeriod.PeriodType.standard());
        period1.addPeriod(period2);
        PeriodType actualPeriodType = period1.getPeriodType();
        int[] expectedValues = new int[]{4, 6, 6, 8, 11, 12, 16, 18, 12, 10};
        int[] actualValues = period1.getValues();
        assertEquals(BasePeriod.PeriodType.standard(), actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, BasePeriod.PeriodType.standard());
        period.addField(DurationFieldType.years(), 1);
        int[] expectedValues = new int[]{2, 2, 3, 4, 5, 6, 7, 8, 1};
        int[] actualValues = period.getValues();
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddFieldInto() {
        int[] inputValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(inputValues, BasePeriod.PeriodType.standard());
        period.addFieldInto(DurationFieldType.years(), 1);
        int[] expectedValues = new int[]{2, 2, 3, 4, 5, 6, 7, 8, 1};
        int[] actualValues = period.getValues();
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testCheckPeriodType() {
        PeriodType inputPeriodType = BasePeriod.PeriodType.calendarDays();
        PeriodType expectedPeriodType = BasePeriod.PeriodType.standard();
        PeriodType actualPeriodType = period.checkPeriodType(inputPeriodType);
        assertEquals(expectedPeriodType, actualPeriodType);
    }
}