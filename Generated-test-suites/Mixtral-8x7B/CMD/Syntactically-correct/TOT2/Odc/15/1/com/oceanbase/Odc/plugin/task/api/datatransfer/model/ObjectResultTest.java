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
objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(0), new AtomicLong(0), null);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
assertNotNull(objectResult);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
assertEquals(new AtomicLong(0), objectResult.getCount());
assertEquals(new AtomicLong(0), objectResult.getTotal());
}

@Test
public void testGetSummary() {
assertEquals("schema.name[null]", objectResult.getSummary());
}

@Test
public void testOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertNotNull(result);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getCount(), result.getCount());
assertEquals(objectStatus.getTotal(), result.getTotal());
}

@Test
public void testConstructor() throws MalformedURLException {
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://test.com"));
objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(urls);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(urls, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testConstructorNullSchema() {
objectResult = new ObjectResult(null, "name", "type");
}

@Test(expected = NullPointerException.class)
public void testConstructorNullName() {
objectResult = new ObjectResult("schema", null, "type");
}

@Test(expected = NullPointerException.class)
public void testConstructorNullType() {
objectResult = new ObjectResult("schema", "name", null);
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
public void testSetExportPaths() {
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://test.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test(expected = JsonProcessingException.class)
public void testSetExportPathsJsonProcessingException() throws JsonProcessingException {
objectResult.setExportPaths(null);
}

}