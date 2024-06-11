import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String testString = "P1Y2M3D4H5M6S765MS";
        MutablePeriod result = MutablePeriod.parse(testString);
        MutablePeriod expected = new MutablePeriod(3663, PeriodType.standard());
        assertEquals(expected, result);
    }
}