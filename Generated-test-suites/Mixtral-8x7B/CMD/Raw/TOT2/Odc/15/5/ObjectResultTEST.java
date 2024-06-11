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

private ObjectResult or;

@Before
public void setUp() {
or = new ObjectResult("schema", "name", "type");
}

@Test
public void testConstructor() {
// Test typical use case
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
}

@Test
public void testOf() throws JsonProcessingException {
// Test typical use case
ObjectStatus os = new ObjectStatus("schema", "name", "type");
ObjectResult or = ObjectResult.of(os);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
}

@Test
public void testGetSummary() {
// Test typical use case
String summary = or.getSummary();
assertEquals("schema.name[type]", summary);
}

@Test
public void testExportPaths() {
// Test typical use case
List<URL> urls = new ArrayList<>();
URL url = null;
try {
url = new URL("http://localhost");
} catch (MalformedURLException e) {
// Handle exception
}
urls.add(url);
or.setExportPaths(urls);
List<URL> result = or.getExportPaths();
assertEquals(url, result.get(0));
}

@Test
public void testSetSchema() {
// Test typical use case
or.setSchema("newSchema");
assertEquals("newSchema", or.getSchema());
}

@Test
public void testSetName() {
// Test typical use case
or.setName("newName");
assertEquals("newName", or.getName());
}

@Test
public void testSetType() {
// Test typical use case
or.setType("newType");
assertEquals("newType", or.getType());
}

@Test
public void testSetStatus() {
// Test typical use case
or.setStatus("newStatus");
assertEquals("newStatus", or.getStatus());
}

@Test
public void testSetCount() {
// Test typical use case
AtomicLong count = new AtomicLong(100);
or.setCount(count);
assertEquals(count.get(), or.getCount().get());
}

@Test
public void testSetTotal() {
// Test typical use case
AtomicLong total = new AtomicLong(200);
or.setTotal(total);
assertEquals(total.get(), or.getTotal().get());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
// Test exception handling
or.setExportPaths(null);
}

@Test(expected = UnsupportedOperationException.class)
public void testExportPathsImmutable() {
// Test immutability
List<URL> urls = or.getExportPaths();
urls.add(null);
}

}