import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithInts() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType actualPeriodType = period.getPeriodType();
        int[] actualValues = period.iValues;

        PeriodType expectedPeriodType = PeriodType.standard();
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};

        assertSame(expectedPeriodType, actualPeriodType);
        assertArrayEquals(expectedValues, actualValues);
    }
}