import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.test.BaseTestCase;

public class BasePeriodTest extends BaseTestCase {

    @org.junit.Test
    public void testBasePeriodConstructionWithValues() {
        // Test construction with given values
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals("Period type should be set correctly", PeriodType.standard(), period.getPeriodType());
        assertArrayEquals("Values should be set correctly", new int[]{1, 2, 3, 4, 5, 6, 7, 8}, period.iValues);
    }

    @org.junit.Test
    public void testBasePeriodConstructionWithDuration() {
        // Test construction with given duration
        ReadableInstant start = new DateTime(1357041600000L).toInstant();
        ReadableInstant end = new DateTime(1357042200000L).toInstant();
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        assertEquals("Period type should be set correctly", PeriodType.standard(), period.getPeriodType());

        Duration duration = period.toDurationFrom(start);
        assertEquals("Duration should be equal to input duration", 60 * 60 * 1000L, duration.getMillis());
    }

    @org.junit.Test
    public void testBasePeriodConstructionWithReadablePeriod() {
        // Test construction with given ReadablePeriod
        BasePeriod period1 = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        BasePeriod period2 = new BasePeriod(period1, PeriodType.standard());

        assertEquals("Period types should be equal", PeriodType.standard(), period1.getPeriodType());
        assertEquals("Period types should be equal", PeriodType.standard(), period2.getPeriodType());
        assertArrayEquals("Values should be equal", period1.iValues, period2.iValues);
    }

    @org.junit.Test
    public void testBasePeriodAddition() {
        // Test addition of two periods
        BasePeriod period1 = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new GeneralBaseDateTime(1357043200000L), new GeneralBaseDateTime(1357044200000L), PeriodType.standard());

        BasePeriod sum = new BasePeriod(period1, period2, PeriodType.standard());

        assertEquals("Period types should be equal", PeriodType.standard(), sum.getPeriodType());

        for (int i = 0; i < sum.size(); i++) {
            int expected = period1.getValue(i) + period2.getValue(i);
            int actual = sum.getValue(i);
            assertEquals("Values should be equal", expected, actual);
        }
    }

    @org.junit.Test
    public void testBasePeriodMerging() {
        // Test merging two periods
        BasePeriod period1 = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new GeneralBaseDateTime(1357042200000L), new GeneralBaseDateTime(1357043200000L), PeriodType.standard());

        BasePeriod merged = new BasePeriod(period1, period2, PeriodType.standard());

        assertEquals("Period types should be equal", PeriodType.standard(), merged.getPeriodType());

        for (int i = 0; i < merged.size(); i++) {
            int expected = period1.getValue(i) + period2.getValue(i);
            int actual = merged.getValue(i);
            assertEquals("Values should be equal", expected, actual);
        }
    }

    @org.junit.Test
    public void testBasePeriodSettingFields() {
        // Test setting fields
        BasePeriod period = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        period.setField(DurationFieldType.years(), 2);
        period.setField(DurationFieldType.months(), 3);
        period.setField(DurationFieldType.days(), 4);

        for (int i = 0; i < period.size(); i++) {
            DurationFieldType fieldType = DurationFieldType.values()[i];
            if (fieldType == DurationFieldType.years() || fieldType == DurationFieldType.months() || fieldType == DurationFieldType.days()) {
                int expected = fieldType == DurationFieldType.years() ? 2 : (fieldType == DurationFieldType.months() ? 3 : 4);
                int actual = period.getValue(i);
                assertEquals("Values should be equal", expected, actual);
            } else {
                int expected = 0;
                int actual = period.getValue(i);
                assertEquals("Values should be zero for other fields", expected, actual);
            }
        }
    }

    @org.junit.Test
    public void testBasePeriodNegation() {
        // Test negation of a period
        BasePeriod period = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        BasePeriod negated = new BasePeriod(period, new PeriodType[]{PeriodType.millennia(), PeriodType.centuries(), PeriodType.decades()}, PeriodType.standard()).negated();

        for (int i = 0; i < negated.size(); i++) {
            int expected = -period.getValue(i);
            int actual = negated.getValue(i);
            assertEquals("Values should be negated", expected, actual);
        }
    }

    @org.junit.Test
    public void testBasePeriodEquals() {
        // Test equals method
        BasePeriod period1 = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());
        BasePeriod period3 = new BasePeriod(new GeneralBaseDateTime(1357042200000L), new GeneralBaseDateTime(1357043200000L), PeriodType.standard());

        assertEquals("Periods with the same values and types should be equal", period1, period2);
        assertNotSame("Periods with different values or types should not be equal", period1, period3);

        assertNotEquals("A period and a different object should not be equal", new Object(), period1);
    }

    @org.junit.Test
    public void testBasePeriodToString() {
        // Test toString method
        BasePeriod period = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());

        String expected = "P2DT2H";
        String actual = period.toString();
        assertEquals("ToString method should return correct string representation", expected, actual);
    }

    @org.junit.Test
    public void testBasePeriodGetDuration() {
        // Test getting durations from a period
        BasePeriod period = new BasePeriod(new GeneralBaseDateTime(1357041600000L), new GeneralBaseDateTime(1357042200000L), PeriodType.standard());

        Duration durationFrom = period.toDurationFrom(new DateTime(1357041600000L).toInstant());
        Duration durationTo = period.toDurationTo(new DateTime(1357042200000L).toInstant());

        assertEquals("Duration from start should be equal to period", period, durationFrom.toPeriod(PeriodType.standard()));
        assertEquals("Duration to end should be equal to period negated", period.negated(), durationTo.toPeriod(PeriodType.standard()).negated());

        assertEquals("Duration from start should have correct millis value", 3600000L + 60 * 60 * 1000L + 60 * 60 * 24 * 1000L, durationFrom.getMillis());
        assertEquals("Duration to end should have correct millis value", 3600000L + 60 * 60 * 1000L + 60 * 60 * 24 * 1000L - 5 * 60 * 60 * 1000L, durationTo.getMillis());
    }
}