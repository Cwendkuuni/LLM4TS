import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField expected = new UnsupportedDurationField(type);
        UnsupportedDurationField actual = UnsupportedDurationField.getInstance(type);
        assertEquals(expected, actual);
    }
}