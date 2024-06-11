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
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

@Test
public void testObjectResult() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertNotNull(or);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
assertEquals(0L, or.getCount().get());
assertEquals(0L, or.getTotal().get());
assertEquals(Status.CREATED, or.getStatus());
}

@Test
public void testObjectResultOf() {
ObjectStatus os = new ObjectStatus("schema", "name", "type", Status.CREATED, new AtomicLong(10L), new AtomicLong(20L));
ObjectResult or = ObjectResult.of(os);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
assertEquals(10L, or.getCount().get());
assertEquals(20L, or.getTotal().get());
assertEquals(Status.CREATED, or.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", or.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
ObjectResult or = new ObjectResult("schema", "name", "type");
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://example.com/test1"));
urls.add(new URL("http://example.com/test2"));
or.setExportPaths(urls);
assertNotNull(or.getExportPaths());
assertEquals(2, or.getExportPaths().size());
assertEquals("http://example.com/test1", or.getExportPaths().get(0).toString());
assertEquals("http://example.com/test2", or.getExportPaths().get(1).toString());
}

@Test
public void testExportPathsNull() {
ObjectResult or = new ObjectResult("schema", "name", "type");
or.setExportPaths(null);
assertNull(or.getExportPaths());
}

@Test
public void testExportPathsEmpty() {
ObjectResult or = new ObjectResult("schema", "name", "type");
or.setExportPaths(new ArrayList<>());
assertNotNull(or.getExportPaths());
assertEquals(0, or.getExportPaths().size());
}

}