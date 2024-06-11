import org.junit.Test;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenPositive() {
        DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1000);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2000);

        int result = BaseSingleFieldPeriod.between(new LocalDateTime(), new LocalDateTime(), durationFieldType);
        int expected = 3000;

        // Test
        int actual = period1.getValue() + period2.getValue();
        int difference = BaseSingleFieldPeriod.between(new LocalDateTime(), new LocalDateTime().plus(actual, durationFieldType), durationFieldType);

        // Assert
        assertEquals(expected, difference);
    }
}