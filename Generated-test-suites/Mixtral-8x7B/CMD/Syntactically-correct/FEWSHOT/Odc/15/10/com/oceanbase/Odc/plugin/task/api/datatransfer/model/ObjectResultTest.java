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
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertNotNull(result);
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 assertEquals(Status.CREATED, result.getStatus());
 assertEquals(0L, result.getCount().get());
 assertEquals(0L, result.getTotal().get());
 assertNull(result.getExportPaths());
 }

 @Test
 public void testOf() {
 ObjectStatus that = new ObjectStatus("schema", "name", Status.CREATED, new AtomicLong(10), new AtomicLong(20));
 ObjectResult result = ObjectResult.of(that);
 assertNotNull(result);
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("", result.getType());
 assertEquals(Status.CREATED, result.getStatus());
 assertEquals(10, result.getCount().get());
 assertEquals(20, result.getTotal().get());
 assertNull(result.getExportPaths());
 }

 @Test
 public void testGetSummary() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema.name[type]", result.getSummary());
 }

 @Test
 public void testExportPaths() throws MalformedURLException {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("http://localhost:8080/test1"));
 exportPaths.add(new URL("http://localhost:8080/test2"));
 result.setExportPaths(exportPaths);
 assertEquals(2, result.getExportPaths().size());
 assertEquals("http://localhost:8080/test1", result.getExportPaths().get(0).toString());
 assertEquals("http://localhost:8080/test2", result.getExportPaths().get(1).toString());
 }

 @Test
 public void testJsonIgnore() throws JsonProcessingException {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("http://localhost:8080/test1"));
 exportPaths.add(new URL("http://localhost:8080/test2"));
 result.setExportPaths(exportPaths);
 String jsonString = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(result);
 assertEquals("{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\",\"status\":\"CREATED\",\"count\":0,\"total\":0}",
 jsonString);
 }
}