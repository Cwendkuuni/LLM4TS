import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
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
public void testObjectResult() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertNotNull(objectResult);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertNull(objectResult.getExportPaths());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertNotNull(objectResult);
assertEquals("name", objectResult.getName());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws MalformedURLException {
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://localhost:8080/file1"));
exportPaths.add(new URL("http://localhost:8080/file2"));

ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(exportPaths);

assertEquals(2, objectResult.getExportPaths().size());
assertEquals("http://localhost:8080/file1", objectResult.getExportPaths().get(0).toString());
assertEquals("http://localhost:8080/file2", objectResult.getExportPaths().get(1).toString());

ObjectResult copyObjectResult = ObjectResult.of(objectResult);
assertNotSame(objectResult.getExportPaths(), copyObjectResult.getExportPaths());
}

@Test
public void testJsonIgnore() throws JsonProcessingException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(new ArrayList<>());

String jsonString = com.fasterxml.jackson.databind.ObjectMapper.builder().build().writeValueAsString(objectResult);
assertEquals("{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\",\"status\":null,\"count\":{\"long\":0},\"total\":{\"long\":0}}",
jsonString);
}

}