import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod instance = new MySingleFieldPeriod(5);
        DurationFieldType actual = instance.getFieldType();
        assertSame(expected = MyDurationFieldType.class, actual);
    }

    // Edge case: Test with null instance
    @Test(expected = NullPointerException.class)
    public void testGetFieldTypeNullInstance() {
        BaseSingleFieldPeriod instance = null;
        instance.getFieldType();
    }
}

// Replace MySingleFieldPeriod and MyDurationFieldType with actual class names