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
// Test the default constructor
ObjectResult result = new ObjectResult();
assertNotNull(result);
}

@Test
public void testObjectResultWithArgs() {
// Test the constructor with arguments
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
}

@Test
public void testOf() {
// Test the of method
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getType(), result.getType());
assertEquals(objectStatus.getCount(), result.getCount());
assertEquals(objectStatus.getTotal(), result.getTotal());
}

@Test
public void testGetSummary() {
// Test the getSummary method
assertEquals("schema.name[null]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
// Test the exportPaths setter and getter
List<URL> urls = new ArrayList<URL>();
urls.add(new URL("http://example.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test
public void testSetSchema() {
// Test the setSchema method
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test
public void testSetName() {
// Test the setName method
objectResult.setName("newName");
assertEquals("newName", objectResult.getName());
}

@Test
public void testSetType() {
// Test the setType method
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test
public void testSetCount() {
// Test the setCount method
AtomicLong count = new AtomicLong(100);
objectResult.setCount(count);
assertEquals(count, objectResult.getCount());
}

@Test
public void testSetTotal() {
// Test the setTotal method
AtomicLong total = new AtomicLong(200);
objectResult.setTotal(total);
assertEquals(total, objectResult.getTotal());
}

@Test
public void testSetStatus() {
// Test the setStatus method
objectResult.setStatus(Status.NOT_AVAILABLE);
assertEquals(Status.NOT_AVAILABLE, objectResult.getStatus());
}

@Test(expected = NullPointerException.class)
public void testSetSchemaNull() {
// Test the setSchema method with null
objectResult.setSchema(null);
}

@Test(expected = NullPointerException.class)
public void testSetNameNull() {
// Test the setName method with null
objectResult.setName(null);
}

@Test(expected = NullPointerException.class)
public void testSetTypeNull() {
// Test the setType method with null
objectResult.setType(null);
}

@Test(expected = IllegalArgumentException.class)
public void testSetStatusInvalid() {
// Test the setStatus method with invalid status
objectResult.setStatus(Status.values()[3]);
}

}