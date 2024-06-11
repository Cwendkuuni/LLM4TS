import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

@Test
public void testObjectResult() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
assertNull(objectResult.getExportPaths());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertEquals("schema", objectResult.getSchema());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testJsonIgnore() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals(null, objectResult.getExportPaths());
}

@Test
public void testExportPaths() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
List<URL> urls = Arrays.asList(mock(URL.class), mock(URL.class));
when(urls.get(0).toExternalForm()).thenReturn("url1");
when(urls.get(1).toExternalForm()).thenReturn("url2");
objectResult.setExportPaths(urls);
assertEquals("url1, url2", objectResult.getExportPathsAsString());
}

@Test
public void testExportPathsAsString() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(null);
assertEquals("", objectResult.getExportPathsAsString());
}
}