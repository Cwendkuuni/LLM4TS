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
objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(10), new AtomicLong(20));
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
// Check the default constructor
ObjectResult result = new ObjectResult();
assertNotNull(result);
}

@Test
public void testObjectResultWithArgs() {
ObjectResult result = new ObjectResult("schema", "name", "table");
assertNotNull(result);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("table", result.getType());
}

@Test
public void testOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertNotNull(result);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getType(), result.getType());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
}

@Test
public void testGetSummary() {
String summary = objectResult.getSummary();
assertEquals("schema.name[table]", summary);
}

@Test
public void testExportPaths() throws MalformedURLException {
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://example.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
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
objectResult.setStatus(Status.UNAVAILABLE);
assertEquals(Status.UNAVAILABLE, objectResult.getStatus());
}

@Test
public void testSetCount() {
objectResult.setCount(new AtomicLong(50));
assertEquals(new Long(50), objectResult.getCount().get());
}

@Test
public void testSetTotal() {
objectResult.setTotal(new AtomicLong(100));
assertEquals(new Long(100), objectResult.getTotal().get());
}

@Test(expected = NullPointerException.class)
public void testSetSchemaNull() {
objectResult.setSchema(null);
}

@Test(expected = NullPointerException.class)
public void testSetNameNull() {
objectResult.setName(null);
}

@Test(expected = NullPointerException.class)
public void testSetTypeNull() {
objectResult.setType(null);
}

@Test(expected = JsonProcessingException.class)
public void testSetStatusJsonProcessingException() throws JsonProcessingException {
objectResult.setStatus(Status.valueOf("INVALID"));
}

}