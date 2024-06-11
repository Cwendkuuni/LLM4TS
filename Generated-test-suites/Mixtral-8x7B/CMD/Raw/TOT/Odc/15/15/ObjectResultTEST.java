import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }
}