import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        DurationFieldType actualType = unsupportedDurationField.getType();
        DurationFieldType expectedType = DurationFieldType.HOURS;
        assertEquals(expectedType, actualType);
    }
}