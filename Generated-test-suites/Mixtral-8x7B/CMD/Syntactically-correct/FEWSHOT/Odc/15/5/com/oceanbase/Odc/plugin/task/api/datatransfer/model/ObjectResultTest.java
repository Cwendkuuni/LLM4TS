import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ObjectResultTest {

 @Mock
 private ObjectMapper objectMapper;

 private ObjectResult objectResult;

 @Before
 public void setup() {
 objectResult = new ObjectResult("schema", "name", "type");
 objectResult.setCount(new AtomicLong(10));
 objectResult.setTotal(new AtomicLong(20));
 objectResult.setStatus("STATUS");
 }

 @Test
 public void testConstructor() {
 assertNotNull(objectResult);
 assertEquals("schema", objectResult.getSchema());
 assertEquals("name", objectResult.getName());
 assertEquals("type", objectResult.getType());
 assertEquals(0, objectResult.getExportPaths().size());
 }

 @Test
 public void testOfMethod() {
 ObjectStatus that = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
 ObjectResult result = ObjectResult.of(that);
 assertEquals(result.getSchema(), that.getSchema());
 assertEquals(result.getName(), that.getName());
 assertEquals(result.getType(), that.getType());
 assertEquals(result.getStatus(), that.getStatus());
 assertEquals(result.getCount().get(), that.getCount().get());
 assertEquals(result.getTotal().get(), that.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 assertEquals("schema.name[type]", objectResult.getSummary());
 }

 @Test
 public void testExportPaths() throws JsonMappingException, JsonProcessingException {
 URL url1 = new URL("http://localhost:8080/1");
 URL url2 = new URL("http://localhost:8080/2");
 List<URL> urls = new ArrayList<>();
 urls.add(url1);
 urls.add(url2);
 objectResult.setExportPaths(urls);

 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
 assertEquals(url1, deserializedObjectResult.getExportPaths().get(0));
 assertEquals(url2, deserializedObjectResult.getExportPaths().get(1));
 }

 @Test
 public void testJsonIgnore() {
 assertNull(objectResult.getExportPaths());
 }

}