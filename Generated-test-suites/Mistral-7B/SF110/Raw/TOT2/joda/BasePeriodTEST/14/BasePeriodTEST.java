import org.junit.Test;
import org.joda.time.BasePeriod;
import static org.junit.Assert.*;

public class BasePeriodTest {
    @Test
    public void testDefaultConstructor() {
        BasePeriod period = new BasePeriod();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[8], period.iValues);
    }
}