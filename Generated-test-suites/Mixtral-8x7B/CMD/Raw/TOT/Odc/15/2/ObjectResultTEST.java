
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.ExpectedException;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult or = new ObjectResult("schema", "name", "type");
        assertEquals("schema", or.getSchema());
        assertEquals("name", or.getName());
        assertEquals("type", or.getType());
        assertEquals(0, or.getCount().get());
        assertEquals(0, or.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus os = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20));
        ObjectResult or = ObjectResult.of(os);
        assertEquals("schema", or.getSchema());
        assertEquals("name", or.getName());
        assertEquals("type", or.getType());
        assertEquals(10, or.getCount().get());
        assertEquals(20, or.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult or = new ObjectResult("schema", "name", "type");
        String summary = or.getSummary();
        assertEquals("schema.name[type]", summary);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultOfNullPointerException() {
        ObjectResult.of(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSummaryIllegalArgumentException() {
        ObjectResult or = new ObjectResult(null, "name", "type");
        or.getSummary();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSummaryIllegalArgumentException2() {
        ObjectResult or = new ObjectResult("schema", null, "type");
        or.getSummary();
    }

}
