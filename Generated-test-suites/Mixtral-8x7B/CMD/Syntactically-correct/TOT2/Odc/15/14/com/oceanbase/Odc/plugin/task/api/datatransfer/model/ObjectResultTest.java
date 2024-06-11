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
public void setUp() throws Exception {
objectStatus = new ObjectStatus("testSchema", "testName", "testType", new AtomicLong(0), new AtomicLong(0), Status.AVAILABLE);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
// Test default constructor
ObjectResult or = new ObjectResult();
assertNotNull(or);
}

@Test
public void testObjectResultStringStringString() {
// Test parameterized constructor
ObjectResult or = new ObjectResult("schema", "name", "type");
assertNotNull(or);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
}

@Test
public void testOfObjectStatus() throws JsonProcessingException {
// Test of method
ObjectResult or = ObjectResult.of(objectStatus);
assertNotNull(or);
assertEquals(objectStatus.getSchema(), or.getSchema());
assertEquals(objectStatus.getName(), or.getName());
assertEquals(objectStatus.getType(), or.getType());
assertEquals(objectStatus.getCount(), or.getCount());
assertEquals(objectStatus.getTotal(), or.getTotal());
assertEquals(objectStatus.getStatus(), or.getStatus());
}

@Test
public void testGetSummary() {
String expected = "testSchema.testName[testType]";
String actual = objectResult.getSummary();
assertEquals(expected, actual);
}

@Test
public void testExportPaths() {
// Test exportPaths setter and getter
List<URL> urls = new ArrayList<>();
try {
urls.add(new URL("http://test.com"));
} catch (MalformedURLException e) {
// Handle exception
}
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test
public void testSetSchema() {
// Test setSchema method
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test(expected = NullPointerException.class)
public void testSetSchemaNull() {
// Test setSchema method with null
objectResult.setSchema(null);
}

@Test(expected = IllegalArgumentException.class)
public void testSetSchemaEmpty() {
// Test setSchema method with empty string
objectResult.setSchema("");
}

@Test
public void testSetName() {
// Test setName method
objectResult.setName("newName");
assertEquals("newName", objectResult.getName());
}

@Test(expected = NullPointerException.class)
public void testSetNameNull() {
// Test setName method with null
objectResult.setName(null);
}

@Test(expected = IllegalArgumentException.class)
public void testSetNameEmpty() {
// Test setName method with empty string
objectResult.setName("");
}

@Test
public void testSetType() {
// Test setType method
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test(expected = NullPointerException.class)
public void testSetTypeNull() {
// Test setType method with null
objectResult.setType(null);
}

@Test(expected = IllegalArgumentException.class)
public void testSetTypeEmpty() {
// Test setType method with empty string
objectResult.setType("");
}

@Test
public void testSetCount() {
// Test setCount method
objectResult.setCount(new AtomicLong(100));
assertEquals(new Long(100), objectResult.getCount().get());
}

@Test
public void testSetTotal() {
// Test setTotal method
objectResult.setTotal(new AtomicLong(200));
assertEquals(new Long(200), objectResult.getTotal().get());
}

@Test
public void testSetStatus() {
// Test setStatus method
objectResult.setStatus(Status.NOT_AVAILABLE);
assertEquals(Status.NOT_AVAILABLE, objectResult.getStatus());
}

}