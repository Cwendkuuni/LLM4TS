import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

ObjectMapper objectMapper = new ObjectMapper();

@Test
public void testConstructor() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertNull(objectResult.getExportPaths());
assertNull(objectResult.getCount());
assertNull(objectResult.getTotal());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "status");
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertEquals("name", objectResult.getName());
assertEquals(new Long(10), objectResult.getCount().get());
assertEquals(new Long(20), objectResult.getTotal().get());
assertEquals("status", objectResult.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws JsonMappingException, JsonProcessingException {
URL url1 = new URL("http://example.com/1");
URL url2 = new URL("http://example.com/2");
List<URL> exportPaths = new ArrayList<>(Arrays.asList(url1, url2));
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(exportPaths);
assertEquals(objectResult.getExportPaths(), exportPaths);
String json = objectMapper.writeValueAsString(objectResult);
ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
assertEquals(deserializedObjectResult.getExportPaths(), exportPaths);
}

@Test
public void testExportPathsNull() throws JsonMappingException, JsonProcessingException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertNull(objectResult.getExportPaths());
String json = objectMapper.writeValueAsString(objectResult);
ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
assertNull(deserializedObjectResult.getExportPaths());
}

@Test
public void testJsonSerialization() throws JsonMappingException, JsonProcessingException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setCount(new AtomicLong(10));
objectResult.setTotal(new AtomicLong(20));
objectResult.setStatus("status");
String json = objectMapper.writeValueAsString(objectResult);
assertNotNull(json);
ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
assertEquals(objectResult.getSchema(), deserializedObjectResult.getSchema());
assertEquals(objectResult.getName(), deserializedObjectResult.getName());
assertEquals(objectResult.getType(), deserializedObjectResult.getType());
assertEquals(objectResult.getCount().get(), deserializedObjectResult.getCount().get());
assertEquals(objectResult.getTotal().get(), deserializedObjectResult.getTotal().get());
assertEquals(objectResult.getStatus(), deserializedObjectResult.getStatus());
}

}