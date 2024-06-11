import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

private ObjectResult objectResult;

@Before
public void setUp() {
objectResult = new ObjectResult("schema", "name", "type");
}

@Test
public void testObjectResult() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
}

@Test
public void testOf() {
ObjectStatus that = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), Status.READY);
ObjectResult result = ObjectResult.of(that);
assertEquals("schema", result.getSchema());
assertEquals(Status.READY, result.getStatus());
assertEquals(10, result.getCount().get());
assertEquals(20, result.getTotal().get());
}

@Test
public void testGetSummary() {
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testExportPaths() {
List<URL> exportPaths = new ArrayList<>();
try {
exportPaths.add(new URL("http://example.com"));
} catch (MalformedURLException e) {
// Ignore
}
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test
public void testGetCount() {
assertEquals(0, objectResult.getCount().get());
objectResult.getCount().incrementAndGet();
assertEquals(1, objectResult.getCount().get());
}

@Test
public void testGetTotal() {
assertEquals(0, objectResult.getTotal().get());
objectResult.getTotal().incrementAndGet();
assertEquals(1, objectResult.getTotal().get());
}

@Test
public void testSetSchema() {
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test
public void testSetName() {
objectResult.setName("newName");
assertEquals("newName", objectResult.getName());
}

@Test
public void testSetType() {
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test
public void testSetStatus() {
objectResult.setStatus(Status.READY);
assertEquals(Status.READY, objectResult.getStatus());
}

@Test
public void testSetCount() {
objectResult.setCount(new AtomicLong(10));
assertEquals(10, objectResult.getCount().get());
}

@Test
public void testSetTotal() {
objectResult.setTotal(new AtomicLong(20));
assertEquals(20, objectResult.getTotal().get());
}

@Test
public void testSerialization() throws JsonProcessingException {
String json = // Serialize to JSON using your preferred library
assertEquals("{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\","
+ "\"count\":0,\"total\":0,\"status\":\"NOT_STARTED\"}", json);
}

}