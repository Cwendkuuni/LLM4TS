import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

public class BasePeriodTest {
    @Test
    public void testBasePeriodConstructorWithDefaults() {
        BasePeriod period = new BasePeriod();
        PeriodType expectedType = PeriodType.standard();
        assertEquals(expectedType, period.getPeriodType());
        int[] expectedValues = new int[BasePeriod.SIZE];
        assertArrayEquals(expectedValues, period.iValues);
    }
}