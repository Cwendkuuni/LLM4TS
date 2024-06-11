import org.joda.time.Partial;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testPartialConstructorNoArguments() {
        Partial partial = new Partial();
        assertNull(partial.getChronology());
        assertNull(partial.getFieldTypes());
        assertNull(partial.getValues());
    }
}