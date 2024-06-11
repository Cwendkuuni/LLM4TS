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
objectStatus = new ObjectStatus("schemaName", "objectName", "objectType", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResultConstructor() {
ObjectResult result = new ObjectResult("schema1", "object1", "type1");
assertEquals("schema1", result.getSchema());
assertEquals("object1", result.getName());
assertEquals("type1", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
assertEquals(Status.AVAILABLE, result.getStatus());
}

@Test
public void testGetSummary() {
String summary = objectResult.getSummary();
assertEquals("schemaName.objectName[objectType]", summary);
}

@Test
public void testOfMethod() {
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getType(), result.getType());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
assertEquals(objectStatus.getStatus(), result.getStatus());
}

@Test(expected = NullPointerException.class)
public void testOfMethodWithNullObjectStatus() {
ObjectResult.of(null);
}

@Test
public void testSetSchema() {
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test(expected = IllegalArgumentException.class)
public void testSetSchemaWithNull() {
objectResult.setSchema(null);
}

@Test
public void testSetName() {
objectResult.setName("newName");
assertEquals("newName", objectResult.getName());
}

@Test(expected = IllegalArgumentException.class)
public void testSetNameWithNull() {
objectResult.setName(null);
}

@Test
public void testSetType() {
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test(expected = IllegalArgumentException.class)
public void testSetTypeWithNull() {
objectResult.setType(null);
}

@Test
public void testSetCount() {
objectResult.setCount(new AtomicLong(50));
assertEquals(50, objectResult.getCount().get());
}

@Test(expected = IllegalArgumentException.class)
public void testSetCountWithNegativeNumber() {
objectResult.setCount(new AtomicLong(-10));
}

@Test
public void testSetTotal() {
objectResult.setTotal(new AtomicLong(150));
assertEquals(150, objectResult.getTotal().get());
}

@Test(expected = IllegalArgumentException.class)
public void testSetTotalWithNegativeNumber() {
objectResult.setTotal(new AtomicLong(-20));
}

@Test
public void testSetStatus() {
objectResult.setStatus(Status.FAILED);
assertEquals(Status.FAILED, objectResult.getStatus());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://example.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testExportPathsWithNull() throws MalformedURLException {
List<URL> urls = null;
objectResult.setExportPaths(urls);
}

@Test(expected = IllegalArgumentException.class)
public void testExportPathsWithInvalidURL() throws MalformedURLException {
List<URL> urls = new ArrayList<>();
urls.add(new URL("invalid://example.com"));
objectResult.setExportPaths(urls);
}

}