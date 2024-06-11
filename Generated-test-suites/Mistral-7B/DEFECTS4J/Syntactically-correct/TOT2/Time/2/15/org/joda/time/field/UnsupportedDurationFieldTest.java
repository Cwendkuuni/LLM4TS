import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertNotNull(unsupportedField);
    }
}