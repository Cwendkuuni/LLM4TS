import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {
@Test
public void testObjectResultConstruction() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
}

@Test
public void testObjectResultOf() {
ObjectStatus that = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objectResult = ObjectResult.of(that);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
}

@Test
public void testObjectResultGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test(expected = NullPointerException.class)
public void testObjectResultNullSchemaConstruction() {
ObjectResult objectResult = new ObjectResult(null, "name", "type");
}

@Test(expected = NullPointerException.class)
public void testObjectResultNullNameConstruction() {
ObjectResult objectResult = new ObjectResult("schema", null, "type");
}

@Test(expected = NullPointerException.class)
public void testObjectResultNullTypeConstruction() {
ObjectResult objectResult = new ObjectResult("schema", "name", null);
}
}