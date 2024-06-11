import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField instance = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField expectedInstance = new UnsupportedDurationField(type);
        assertEquals(expectedInstance.getType(), instance.getType());
    }
}