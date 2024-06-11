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
objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(0), new AtomicLong(0), null);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
// Test the constructor with valid arguments
assertNotNull(objectResult);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertEquals(null, objectResult.getExportPaths());
}

@Test
public void testGetSummary() {
assertEquals("schema.name[null]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://example.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test
public void testOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertNotNull(result);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
}

@Test(expected = NullPointerException.class)
public void testOfNull() {
ObjectResult.of(null);
}

@Test
public void testSetSchema() {
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test(expected = NullPointerException.class)
public void testSetSchemaNull() {
objectResult.setSchema(null);
}

@Test
public void testSetName() {
objectResult.setName("newName");
assertEquals("newName", objectResult.getName());
}

@Test(expected = NullPointerException.class)
public void testSetNameNull() {
objectResult.setName(null);
}

@Test
public void testSetType() {
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test(expected = NullPointerException.class)
public void testSetTypeNull() {
objectResult.setType(null);
}

@Test
public void testSetStatus() {
objectResult.setStatus(Status.NOT_AVAILABLE);
assertEquals(Status.NOT_AVAILABLE, objectResult.getStatus());
}

@Test(expected = NullPointerException.class)
public void testSetStatusNull() {
objectResult.setStatus(null);
}

@Test
public void testSetCount() {
objectResult.setCount(new AtomicLong(10));
assertEquals(10, objectResult.getCount().get());
}

@Test(expected = NullPointerException.class)
public void testSetCountNull() {
objectResult.setCount(null);
}

@Test
public void testSetTotal() {
objectResult.setTotal(new AtomicLong(20));
assertEquals(20, objectResult.getTotal().get());
}

@Test(expected = NullPointerException.class)
public void testSetTotalNull() {
objectResult.setTotal(null);
}

@Test
public void testSetExportPaths() throws MalformedURLException {
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://example.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
objectResult.setExportPaths(null);
}

}