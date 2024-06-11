import org.joda.time.*;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        String input = "P3Y6M4D"; // Three years, six months, four days
        MutablePeriod expected = new MutablePeriod(3, 6, 4);
        MutablePeriod actual = MutablePeriod.parse(input);
        assertEquals(expected.toString(), actual.toString());
    }
}