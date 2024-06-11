import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String input = "P3Y2M6DT12H30M"; //Valid ISO 8601 period format string
        MutablePeriod mutablePeriod = MutablePeriod.parse(input);
        assertEquals("Test parsing a valid ISO 8601 period string", "P3Y2M6DT12H30M", mutablePeriod.toString());

        input = "P-3Y2M6DT12H30M"; //Negative ISO 8601 period format string
        mutablePeriod = MutablePeriod.parse(input);
        assertEquals("Test parsing a negative ISO 8601 period string", "-P3Y2M6DT12H30M", mutablePeriod.toString());

        input = "P1W-1D"; //ISO 8601 period with negative week and day components
        mutablePeriod = MutablePeriod.parse(input);
        assertEquals("Test parsing a ISO 8601 period with negative week and day components", "-P1W-1D", mutablePeriod.toString());

        PeriodFormatter formatter = ISOPeriodFormat.standard();
        // Test parsing with custom formatter
        mutablePeriod = MutablePeriod.parse("PT1S", formatter);
        assertEquals("Test parsing a valid custom period format string with formatter", "PT1S", mutablePeriod.toString());
    }
}