import org.junit.Test;
import org.joda.time.BasePeriod;
import static org.junit.Assert.assertEquals;

public class BasePeriodTest {

    @Test
    public void testBasePeriodWithEmptyConstructor() {
        BasePeriod period = new BasePeriod();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(new int[8], period.iValues);
    }
}