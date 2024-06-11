import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetween() {
        BaseSingleFieldPeriod pd1 = new MyPeriodType(5); // Replace MyPeriodType with the actual period type
        BaseSingleFieldPeriod pd2 = new MyPeriodType(10);

        int result = BaseSingleFieldPeriod.between(null, null, DurationFieldType.DAYS); // Null check
        int expected = pd2.getValue() - pd1.getValue();

        int actual = BaseSingleFieldPeriod.between(pd1, pd2, DurationFieldType.DAYS);

        assertEquals(expected, actual);
    }
}