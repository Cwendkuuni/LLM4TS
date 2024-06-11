import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;

public class BasePeriodTest {
    @Test
    public void testBasePeriodConstructor() {
        BasePeriod period = new BasePeriod();
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
        int[] values = period.getValues();
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, values);
    }
}