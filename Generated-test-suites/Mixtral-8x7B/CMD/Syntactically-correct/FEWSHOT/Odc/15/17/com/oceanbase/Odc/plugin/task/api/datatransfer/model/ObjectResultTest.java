import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

@Test
public void testConstructor() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertNotNull(objectResult.getSchema());
assertEquals("schema", objectResult.getSchema());
assertNotNull(objectResult.getName());
assertEquals("name", objectResult.getName());
assertNotNull(objectResult.getType());
assertEquals("type", objectResult.getType());
assertNull(objectResult.getExportPaths());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertNotNull(objectResult);
assertEquals("name", objectResult.getName());
assertEquals(10, (long) objectResult.getCount().get());
assertEquals(20, (long) objectResult.getTotal().get());
}

@Test
public void testGetSummary() throws JsonProcessingException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws MalformedURLException {
List<URL> exportPaths = new ArrayList<>();
URL url1 = new URL("http://example.com/1");
URL url2 = new URL("http://example.com/2");
exportPaths.add(url1);
exportPaths.add(url2);
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(exportPaths);
assertNotNull(objectResult.getExportPaths());
assertEquals(2, objectResult.getExportPaths().size());
assertEquals(url1, objectResult.getExportPaths().get(0));
assertEquals(url2, objectResult.getExportPaths().get(1));
}
}