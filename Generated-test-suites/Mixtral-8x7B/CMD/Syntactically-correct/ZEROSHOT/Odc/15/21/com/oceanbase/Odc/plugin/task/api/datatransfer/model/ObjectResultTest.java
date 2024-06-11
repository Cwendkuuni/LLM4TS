import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ObjectResultTest {

@Test
public void testObjectResult() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20));
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setCount(new AtomicLong(10));
objectResult.setTotal(new AtomicLong(20));
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testJsonIgnore() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(Arrays.asList(new URL("http://example.com")));
assertEquals(null, objectResult.getExportPaths());
}

@Test
public void testEquals() {
ObjectResult objectResult1 = new ObjectResult("schema", "name", "type");
ObjectResult objectResult2 = new ObjectResult("schema", "name", "type");
assertEquals(objectResult1, objectResult2);
}

@Test
public void testNotEquals() {
ObjectResult objectResult1 = new ObjectResult("schema1", "name", "type");
ObjectResult objectResult2 = new ObjectResult("schema", "name", "type");
assertNotEquals(objectResult1, objectResult2);
}

@Test
public void testList() {
List<URL> urls = new ArrayList<>();
URL url1 = new URL("http://example1.com");
URL url2 = new URL("http://example2.com");
urls.add(url1);
urls.add(url2);
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}
}