import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20));
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20));
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema.name[null]", objectResult.getSummary());
    }
}