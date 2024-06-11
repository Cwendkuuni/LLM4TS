import org.joda.time.BasePeriod;
import org.junit.Test;

public class BasePeriodTest {
    @Test
    public void testBasePeriod() {
        BasePeriod period = new BasePeriod();
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[8], period.iValues);
    }
}