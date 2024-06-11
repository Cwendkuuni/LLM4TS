import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Instant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour

        int result = BaseSingleFieldPeriod.between(instant, instant2, DurationFieldType.HOURS);
        int expected = 24; // hours between two instants, assuming one hour period

        assertEquals(expected, result);
    }
}