import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals(10, (long) result.getCount().get());
        assertEquals(20, (long) result.getTotal().get());
    }

    @Test
    public void testObjectResultGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", result.getSummary());
    }
}