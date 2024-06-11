import org.junit.Test;
import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.PeriodType;
import org.joda.time.base.BasePeriod;
import static org.junit.Assert.assertArrayEquals;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructorWithTwoReadableInstants() {
        final LocalDateTime start = new LocalDateTime(2015, 1, 1, 12, 0, 0, 0);
        final LocalDateTime end = new LocalDateTime(2015, 1, 2, 12, 0, 0, 0);
        final BasePeriod period = new BasePeriod(start.toInstant(), end.toInstant(), PeriodType.standard());
        final int[] expectedValues = new int[]{0, 1, 0, 24, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedValues, period.iValues);
    }
}