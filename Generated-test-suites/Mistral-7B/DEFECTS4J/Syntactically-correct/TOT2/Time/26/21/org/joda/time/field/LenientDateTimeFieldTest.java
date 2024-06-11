import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.StrictDateTimeField;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField strictDateTimeField = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH, ISOChronology.INSTANCE);
        DateTimeField lenientDateTimeField = LenientDateTimeField.getInstance(strictDateTimeField, ISOChronology.INSTANCE);
        assertTrue(lenientDateTimeField instanceof LenientDateTimeField);
    }
}