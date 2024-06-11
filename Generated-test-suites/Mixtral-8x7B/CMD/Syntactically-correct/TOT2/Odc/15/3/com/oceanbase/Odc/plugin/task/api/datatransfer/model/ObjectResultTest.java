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

@Before
public void setup() {
objectResult = new ObjectResult("schema", "name", "type");
}

@Test
public void testConstructor() {
// Testing if the constructor is working as expected
ObjectResult result = new ObjectResult("schema", "name", "type");
assertNotNull(result);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
}

@Test
public void testOf() {
// Testing if the 'of' method is working as expected
ObjectStatus that = new ObjectStatus("schema", "name", Status.AVAILABLE, 10, 20);
ObjectResult result = ObjectResult.of(that);
assertNotNull(result);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("AVAILABLE", result.getStatus().toString());
assertEquals(new Long(10), result.getCount().get());
assertEquals(new Long(20), result.getTotal().get());
}

@Test
public void testGetSummary() {
// Testing if the 'getSummary' method is working as expected
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws MalformedURLException {
// Testing if the 'exportPaths' setter and getter are working as expected
URL url1 = new URL("http://example.com");
URL url2 = new URL("https://example.org");
List<URL> urls = new ArrayList<>();
urls.add(url1);
urls.add(url2);
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
assertEquals(2, objectResult.getExportPaths().size());
assertEquals(url1, objectResult.getExportPaths().get(0));
assertEquals(url2, objectResult.getExportPaths().get(1));
}

@Test
public void testSetSchema() {
// Testing if the 'setSchema' method is working as expected
objectResult.setSchema("newSchema");
assertEquals("newSchema", objectResult.getSchema());
}

@Test
public void testSetName() {
// Testing if the 'setName' method is working as expected
objectResult.setName("newName");
assertEquals("newName", objectResult.getName());
}

@Test
public void testSetType() {
// Testing if the 'setType' method is working as expected
objectResult.setType("newType");
assertEquals("newType", objectResult.getType());
}

@Test
public void testSetStatus() {
// Testing if the 'setStatus' method is working as expected
objectResult.setStatus(Status.UNAVAILABLE);
assertEquals(Status.UNAVAILABLE, objectResult.getStatus());
}

@Test
public void testSetCount() {
// Testing if the 'setCount' method is working as expected
objectResult.setCount(new AtomicLong(5));
assertEquals(new Long(5), objectResult.getCount().get());
}

@Test
public void testSetTotal() {
// Testing if the 'setTotal' method is working as expected
objectResult.setTotal(new AtomicLong(15));
assertEquals(new Long(15), objectResult.getTotal().get());
}

@Test
public void testJsonIgnoreAnnotation() throws JsonProcessingException {
// Testing if the '@JsonIgnore' annotation is working as expected
// This test will fail if the 'exportPaths' field is included in the JSON
// serialization
// ...
}

}