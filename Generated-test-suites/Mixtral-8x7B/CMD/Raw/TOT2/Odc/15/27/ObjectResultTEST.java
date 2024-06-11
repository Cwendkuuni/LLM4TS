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
objectStatus = new ObjectStatus("schema", "name", Status.EXPORTED, new AtomicLong(10), new AtomicLong(20));
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResultConstructor() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
}

@Test
public void testGetSummary() {
String expected = "schema.name[type]";
assertEquals(expected, objectResult.getSummary());
}

@Test
public void testOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getType(), result.getType());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
}

@Test
public void testExportPaths() {
List<URL> urls = new ArrayList<>();
try {
urls.add(new URL("http://example.com"));
} catch (MalformedURLException e) {
// handle exception
}
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
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
public void testSetCount() {
objectResult.setCount(new AtomicLong(50));
assertEquals(50, objectResult.getCount().get());
}

@Test
public void testSetTotal() {
objectResult.setTotal(new AtomicLong(100));
assertEquals(100, objectResult.getTotal().get());
}

@Test
public void testSerialize() throws JsonProcessingException {
// add serialize test here
}

}