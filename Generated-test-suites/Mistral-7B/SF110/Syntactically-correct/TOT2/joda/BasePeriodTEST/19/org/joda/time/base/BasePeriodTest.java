import org.joda.time.BasePeriod;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasePeriodTest {
    @Test
    public void testBasePeriodDefaultInitialization() {
        BasePeriod basePeriod = new BasePeriod();
        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = new int[8];
        System.arraycopy(ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, 0), 0, expectedValues, 0, 8);

        assertEquals(expectedPeriodType, basePeriod.getPeriodType());
        assertArrayEquals(expectedValues, basePeriod.iValues);
    }
}