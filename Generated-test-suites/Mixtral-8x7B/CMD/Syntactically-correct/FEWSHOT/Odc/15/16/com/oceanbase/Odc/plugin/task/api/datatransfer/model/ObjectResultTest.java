import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {
 private ObjectResult createObjectResult() {
 ObjectResult objectResult = new ObjectResult("schema", "name", "type");
 objectResult.setCount(new AtomicLong(10));
 objectResult.setTotal(new AtomicLong(20));
 return objectResult;
 }

 @Test
 public void testObjectResult() {
 ObjectResult objectResult = createObjectResult();
 assertEquals("schema", objectResult.getSchema());
 assertEquals("name", objectResult.getName());
 assertEquals("type", objectResult.getType());
 assertEquals(new Long(10), objectResult.getCount().get());
 assertEquals(new Long(20), objectResult.getTotal().get());
 }

 @Test
 public void testOf() {
 ObjectStatus that = createObjectResult();
 ObjectResult objectResult = ObjectResult.of(that);
 assertEquals(that.getSchema(), objectResult.getSchema());
 assertEquals(that.getName(), objectResult.getName());
 assertEquals(that.getType(), objectResult.getType());
 assertEquals(that.getCount().get(), objectResult.getCount().get());
 assertEquals(that.getTotal().get(), objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = createObjectResult();
 assertEquals("schema.name[type]", objectResult.getSummary());
 }

 @Test
 public void testExportPaths() {
 ObjectResult objectResult = createObjectResult();
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(getClass().getResource("/file1.txt"));
 exportPaths.add(getClass().getResource("/file2.txt"));
 objectResult.setExportPaths(exportPaths);
 assertEquals(2, objectResult.getExportPaths().size());
 assertEquals(getClass().getResource("/file1.txt"), objectResult.getExportPaths().get(0));
 assertEquals(getClass().getResource("/file2.txt"), objectResult.getExportPaths().get(1));
 }

 @Test
 public void testObjectResultSerialization() throws JsonMappingException, JsonProcessingException {
 ObjectResult objectResult = createObjectResult();
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
 assertEquals(objectResult.getSchema(), deserializedObjectResult.getSchema());
 assertEquals(objectResult.getName(), deserializedObjectResult.getName());
 assertEquals(objectResult.getType(), deserializedObjectResult.getType());
 assertEquals(objectResult.getCount().get(), deserializedObjectResult.getCount().get());
 assertEquals(objectResult.getTotal().get(), deserializedObjectResult.getTotal().get());
 }

 @Test
 public void testObjectResultSerializationWithNullExportPaths() throws JsonMappingException, JsonProcessingException {
 ObjectResult objectResult = createObjectResult();
 objectResult.setExportPaths(null);
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
 assertNull(deserializedObjectResult.getExportPaths());
 }
}