import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriodBasicFunctionality() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(1, period.size());
        assertEquals(fieldType, period.getFieldType(0));
        assertEquals(0, period.getValue(0));
        assertEquals(5, period.get(fieldType));
        assertEquals(5, period.get(fieldType, MutablePeriod.getInstance()));
        assertEquals(fieldType, period.isSupported(fieldType));
        assertEquals(PeriodType.standard(), period.getPeriodType());
        Period standardPeriod = period.toPeriod();
        assertEquals(standardPeriod, new Period(5, PeriodType.standard()));
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(standardPeriod, mutablePeriod);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriodInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidChronology() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(ISOChronology.getInstance().withBaseYear(1900).getMillis(new LocalDate(2014, 1, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(DurationFieldType.years());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidValues() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        period.setValue(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodNullArguments() {
        try {
            BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(new BaseSingleFieldPeriod(5));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidBetween() {
        ReadableInstant start = new DateTime(0);
        ReadableInstant end = new DateTime(Long.MAX_VALUE);
        try {
            BaseSingleFieldPeriod.between(start, end, DurationFieldType.weeks());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidZeroInstance() {
        ReadablePartial start = new LocalDate();
        ReadablePartial end = new LocalDate();
        try {
            BaseSingleFieldPeriod.between(start, end, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidPeriod() {
        try {
            BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(new Period());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = null;
        try {
            period.getPeriodType(periodType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}