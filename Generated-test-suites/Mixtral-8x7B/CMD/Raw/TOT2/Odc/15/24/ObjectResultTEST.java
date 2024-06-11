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

public class ObjectResultTest {

private ObjectResult objectResult;
private ObjectStatus objectStatus;

@Before
public void setup() {
objectStatus = new ObjectStatus("schema", "name", "type", new AtomicLong(0), new AtomicLong(0), null);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
// Test the constructor
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
}

@Test
public void testOfMethod() throws JsonProcessingException {
// Test the of method
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
}

@Test
public void testGetSummary() {
// Test the getSummary method
String summary = objectResult.getSummary();
assertEquals("schema.name[type]", summary);
}

@Test
public void testExportPaths() throws MalformedURLException {
// Test the exportPaths
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://example.com"));
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test
public void testSetType() {
// Test the setType method
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test
public void testSetSchema() {
// Test the setSchema method
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test(expected = NullPointerException.class)
public void testSetSchemaNull() {
// Test setting schema to null
objectResult.setSchema(null);
}

@Test(expected = NullPointerException.class)
public void testSetTypeNull() {
// Test setting type to null
objectResult.setType(null);
}

@Test
public void testSetCount() {
// Test the setCount method
objectResult.setCount(new AtomicLong(10));
assertEquals(10, objectResult.getCount().get());
}

@Test
public void testSetTotal() {
// Test the setTotal method
objectResult.setTotal(new AtomicLong(20));
assertEquals(20, objectResult.getTotal().get());
}

@Test
public void testSetStatus() {
// Test the setStatus method
objectResult.setStatus("newStatus");
assertEquals("newStatus", objectResult.getStatus());
}

@Test(expected = NullPointerException.class)
public void testSetStatusNull() {
// Test setting status to null
objectResult.setStatus(null);
}

}