import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

private ObjectResult createObjectResult(String schema, String name, String type) {
return new ObjectResult(schema, name, type);
}

private ObjectStatus createObjectStatus(String schema, String name, String type, Long count, Long total) {
ObjectStatus objectStatus = new ObjectStatus();
objectStatus.setSchema(schema);
objectStatus.setName(name);
objectStatus.setType(type);
objectStatus.setCount(new AtomicLong(count));
objectStatus.setTotal(new AtomicLong(total));
objectStatus.setStatus(Status.AVAILABLE);
return objectStatus;
}

@Test
public void testObjectResult() {
ObjectResult objResult = createObjectResult("schema", "name", "table");
assertNotNull(objResult);
assertEquals("schema", objResult.getSchema());
assertEquals("name", objResult.getName());
assertEquals("table", objResult.getType());
assertEquals(0, objResult.getCount().get());
assertEquals(0, objResult.getTotal().get());
assertEquals(Status.AVAILABLE, objResult.getStatus());
}

@Test
public void testOf() {
ObjectStatus objectStatus = createObjectStatus("schema", "name", "table", 5L, 10L);
ObjectResult objResult = ObjectResult.of(objectStatus);
assertEquals("schema", objResult.getSchema());
assertEquals("name", objResult.getName());
assertEquals("table", objResult.getType());
assertEquals(5, objResult.getCount().get());
assertEquals(10, objResult.getTotal().get());
assertEquals(Status.AVAILABLE, objResult.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult objResult = createObjectResult("schema", "name", "table");
assertEquals("schema.name[table]", objResult.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
ObjectResult objResult = createObjectResult("schema", "name", "table");
List<URL> exportPaths = new ArrayList<>(
Arrays.asList(new URL("http://example.com/path1"), new URL("http://example.com/path2")));
objResult.setExportPaths(exportPaths);
assertEquals(2, objResult.getExportPaths().size());
assertEquals("http://example.com/path1", objResult.getExportPaths().get(0).toString());
assertEquals("http://example.com/path2", objResult.getExportPaths().get(1).toString());
}

@Test
public void testExportPathsNull() throws JsonProcessingException, MalformedURLException {
ObjectResult objResult = createObjectResult("schema", "name", "table");
objResult.setExportPaths(null);
assertNull(objResult.getExportPaths());
}
}