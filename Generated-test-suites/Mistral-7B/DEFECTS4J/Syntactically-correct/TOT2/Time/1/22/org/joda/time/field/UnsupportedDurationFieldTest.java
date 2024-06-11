import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    @org.junit.Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);

        assertEquals(field1, field2);
    }
}