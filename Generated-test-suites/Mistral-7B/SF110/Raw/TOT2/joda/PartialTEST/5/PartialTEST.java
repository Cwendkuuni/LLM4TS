import org.joda.time.DateTimeFieldType;
import org.joda.time.util.AbstractBaseTestCase;

public class PartialTest extends AbstractBaseTestCase {

    @Test
    public void testPartialConstructor() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.dayOfWeek();
        int value = 2;
        Partial partial = new Partial(dateTimeFieldType, value);
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(value, partial.getValue(0));
    }
}