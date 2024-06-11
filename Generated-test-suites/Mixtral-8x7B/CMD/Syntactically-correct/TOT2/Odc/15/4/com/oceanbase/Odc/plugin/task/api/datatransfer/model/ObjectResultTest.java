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

@Before
public void setup() {
objectResult = new ObjectResult("schema", "name", "type");
}

@Test
public void testConstructor() {
// Testing the constructor with valid parameters
ObjectResult result = new ObjectResult("schema", "name", "type");
assertNotNull(result);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
}

@Test
public void testOfMethod() {
// Testing the of method with valid ObjectStatus
ObjectStatus that = new ObjectStatus("schema", "name", "type");
that.setCount(new AtomicLong(10));
that.setTotal(new AtomicLong(20));

ObjectResult result = ObjectResult.of(that);
assertNotNull(result);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(10, result.getCount().get());
assertEquals(20, result.getTotal().get());
}

@Test
public void testGetSummary() {
String expectedSummary = "schema.name[type]";
assertEquals(expectedSummary, objectResult.getSummary());
}

@Test
public void testExportPaths() {
List<URL> exportPaths = new ArrayList<>();
try {
exportPaths.add(new URL("http://example.com"));
} catch (MalformedURLException e) {
// This should not happen as we are creating the URL ourselves
fail("Unexpected MalformedURLException");
}

objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test
public void testSetExportPathsNull() {
objectResult.setExportPaths(null);
assertNull(objectResult.getExportPaths());
}

@Test
public void testSetExportPathsEmpty() {
objectResult.setExportPaths(new ArrayList<>());
assertTrue(objectResult.getExportPaths().isEmpty());
}

@Test
public void testGetSetSchema() {
String schema = "newSchema";
objectResult.setSchema(schema);
assertEquals(schema, objectResult.getSchema());
}

@Test
public void testGetSetName() {
String name = "newName";
objectResult.setName(name);
assertEquals(name, objectResult.getName());
}

@Test
public void testGetSetType() {
String type = "newType";
objectResult.setType(type);
assertEquals(type, objectResult.getType());
}

@Test
public void testGetSetCount() {
long count = 10;
objectResult.setCount(new AtomicLong(count));
assertEquals(count, objectResult.getCount().get());
}

@Test
public void testGetSetTotal() {
long total = 20;
objectResult.setTotal(new AtomicLong(total));
assertEquals(total, objectResult.getTotal().get());
}

@Test
public void testJsonIgnore() throws JsonProcessingException {
// Testing that exportPaths is not included in the JSON serialization
String json = ObjectMapperHelper.writeValueAsString(objectResult);
assertFalse(json.contains("exportPaths"));
}

}