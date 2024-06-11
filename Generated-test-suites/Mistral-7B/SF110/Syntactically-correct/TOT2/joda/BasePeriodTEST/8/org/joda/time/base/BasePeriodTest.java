import org.joda.time.BasePeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.joda.time.PeriodType;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructorWithTwoReadableInstants() {
        Instant instant1 = new Instant(1497356800000L); // Wed, 22 Mar 2017 00:00:00 GMT
        Instant instant2 = new Instant(1497360400000L); // Wed, 22 Mar 2017 01:00:00 GMT
        BasePeriod period = new BasePeriod(instant1, instant2, PeriodType.standard());

        // Check the period's values
        PeriodType periodType = period.getPeriodType();
        int[] values = period.getValues();
        assertNotNull(period);
        assertEquals(PeriodType.standard(), periodType);
        assertArrayEquals(new int[]{0, 0, 0, 3600000, 0, 0, 0, 0}, values);
    }
}