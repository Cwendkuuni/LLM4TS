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
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertNotNull(objectResult);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertNotNull(objectResult);
assertEquals("name", objectResult.getName());
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
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://localhost:8080/export/path1"));
exportPaths.add(new URL("http://localhost:8080/export/path2"));
objectResult.setExportPaths(exportPaths);
assertEquals(2, objectResult.getExportPaths().size());
assertEquals("http://localhost:8080/export/path1", objectResult.getExportPaths().get(0).toString());
assertEquals("http://localhost:8080/export/path2", objectResult.getExportPaths().get(1).toString());
}

@Test
public void testJsonIgnore() throws JsonProcessingException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://localhost:8080/export/path1"));
exportPaths.add(new URL("http://localhost:8080/export/path2"));
objectResult.setExportPaths(exportPaths);
assertEquals(null, objectResult.getExportPaths());
}

@Test
public void testSetExportPaths() throws MalformedURLException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://localhost:8080/export/path1"));
exportPaths.add(new URL("http://localhost:8080/export/path2"));
objectResult.setExportPaths(exportPaths);
assertEquals(2, objectResult.getExportPaths().size());
assertEquals("http://localhost:8080/export/path1", objectResult.getExportPaths().get(0).toString());
assertEquals("http://localhost:8080/export/path2", objectResult.getExportPaths().get(1).toString());
}

@Test
public void testGetExportPaths() throws MalformedURLException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://localhost:8080/export/path1"));
exportPaths.add(new URL("http://localhost:8080/export/path2"));
objectResult.setExportPaths(exportPaths);
assertEquals(2, objectResult.getExportPaths().size());
assertEquals("http://localhost:8080/export/path1", objectResult.getExportPaths().get(0).toString());
assertEquals("http://localhost:8080/export/path2", objectResult.getExportPaths().get(1).toString());
}

@Test
public void testSetExportPathsNull() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(null);
assertNull(objectResult.getExportPaths());
}
}