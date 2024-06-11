import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testPartialConstruction() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.HOUR_OF_DAY;
        Partial partial = new Partial(dateTimeFieldType, 12);
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(12, partial.getValue(0));
    }
}