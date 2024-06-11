import org.joda.time.Partial;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testDefaultConstructor() {
        Partial partial = new Partial();
        assertNull(partial.getChronology());
        assertArrayEquals(new DateTimeFieldType[0], partial.getFieldTypes());
        assertArrayEquals(new int[0], partial.getValues());
    }
}