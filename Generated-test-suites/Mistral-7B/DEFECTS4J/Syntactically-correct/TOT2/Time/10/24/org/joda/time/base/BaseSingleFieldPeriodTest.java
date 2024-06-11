import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_ValidUsage() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5); // 5 units of the field
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertEquals(5, period.get(fieldType));
    }

    @Test
    public void testBaseSingleFieldPeriod_GetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(period.getFieldType(), DurationFieldType.DAYS); // Assuming the class extends BaseSingleFieldPeriod with DurationFieldType.DAYS
    }

    @Test
    public void testBaseSingleFieldPeriod_Size() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testBaseSingleFieldPeriod_IsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.HOURS;
        assertTrue(period.isSupported(fieldType));
        assertEquals(0, period.get(fieldType));
    }

    @Test
    public void testBaseSingleFieldPeriod_ToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expectedPeriod = new Period(5, 0, 0, 0);
        Period actualPeriod = period.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testBaseSingleFieldPeriod_ToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.set(DurationFieldType.DAYS, 5);
        MutablePeriod actualPeriod = period.toMutablePeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }
}