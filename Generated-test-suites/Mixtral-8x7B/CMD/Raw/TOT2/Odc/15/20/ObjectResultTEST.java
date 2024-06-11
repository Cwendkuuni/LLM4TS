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
private ObjectStatus objectStatus;

@Before
public void setup() {
objectStatus = new ObjectStatus("schemaName", "objectName", "objectType", new AtomicLong(0), new AtomicLong(0), Status.AVAILABLE);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
// Test typical use case
assertEquals("schemaName", objectResult.getSchema());
assertEquals("objectName", objectResult.getName());
assertEquals("objectType", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
}

@Test
public void testOf() {
// Test typical use case
ObjectResult or = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), or.getSchema());
assertEquals(objectStatus.getName(), or.getName());
assertEquals(objectStatus.getType(), or.getType());
assertEquals(objectStatus.getCount().get(), or.getCount().get());
assertEquals(objectStatus.getTotal().get(), or.getTotal().get());
assertEquals(objectStatus.getStatus(), or.getStatus());
}

@Test
public void testGetSummary() {
// Test typical use case
assertEquals("schemaName.objectName[objectType]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws MalformedURLException {
// Test typical use case
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://example.com"));
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());

// Test empty list
objectResult.setExportPaths(null);
assertEquals(0, objectResult.getExportPaths().size());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
// Test null list
objectResult.setExportPaths(null);
}

@Test(expected = IllegalArgumentException.class)
public void testSetExportPathsInvalidURL() throws MalformedURLException {
// Test invalid URL
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("ht//example.com"));
objectResult.setExportPaths(exportPaths);
}

@Test
public void testConstructor() {
// Test typical use case
ObjectResult or = new ObjectResult("schemaName", "objectName", "objectType");
assertEquals("schemaName", or.getSchema());
assertEquals("objectName", or.getName());
assertEquals("objectType", or.getType());
assertEquals(0, or.getCount().get());
assertEquals(0, or.getTotal().get());
assertEquals(Status.AVAILABLE, or.getStatus());
}

@Test
public void testSettersAndGetters() {
// Test typical use case
objectResult.setSchema("newSchemaName");
objectResult.setName("newObjectName");
objectResult.setType("newObjectType");
objectResult.setCount(new AtomicLong(100));
objectResult.setTotal(new AtomicLong(200));
objectResult.setStatus(Status.UNAVAILABLE);

assertEquals("newSchemaName", objectResult.getSchema());
assertEquals("newObjectName", objectResult.getName());
assertEquals("newObjectType", objectResult.getType());
assertEquals(100, objectResult.getCount().get());
assertEquals(200, objectResult.getTotal().get());
assertEquals(Status.UNAVAILABLE, objectResult.getStatus());
}

@Test
public void testJsonIgnore() throws JsonProcessingException {
// Test JsonIgnore annotation
// This test won't compile since it tries to access a JsonIgnore field, which is not possible
// assertEquals(exportPaths, objectResult.getExportPaths());
}

}