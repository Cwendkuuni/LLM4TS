import org.junit.Test;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField expected = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField actual = UnsupportedDurationField.getInstance(type);
        assertSame(expected, actual);
    }
}