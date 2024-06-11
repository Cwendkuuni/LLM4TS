import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodDefaultConstructor() {
        BasePeriod period = new BasePeriod();
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[BasePeriod.SIZE];
        assertSame(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.iValues);
    }
}