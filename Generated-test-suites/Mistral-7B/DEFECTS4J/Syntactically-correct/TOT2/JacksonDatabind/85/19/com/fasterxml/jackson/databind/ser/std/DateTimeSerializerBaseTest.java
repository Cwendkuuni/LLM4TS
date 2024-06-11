import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(new Object() { // Assuming some object with a timestamp of 0
            long timestamp = 0;
        }));
    }
}