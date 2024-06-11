import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

ObjectMapper mapper = new ObjectMapper();

@Test
public void testObjectResult() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertNotNull(or);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
assertEquals(0L, or.getCount().get());
assertEquals(0L, or.getTotal().get());
}

@Test
public void testObjectResultOf() throws JsonMappingException, JsonProcessingException {
ObjectStatus os = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20));
ObjectResult or = ObjectResult.of(os);
assertNotNull(or);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
assertEquals(10L, or.getCount().get());
assertEquals(20L, or.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", or.getSummary());
}

@Test
public void testExportPaths() {
ObjectResult or = new ObjectResult("schema", "name", "type");
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://localhost"));
or.setExportPaths(urls);
assertEquals(urls, or.getExportPaths());
}

@Test
public void testJsonIgnore() throws JsonMappingException, JsonProcessingException {
ObjectResult or = new ObjectResult("schema", "name", "type");
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://localhost"));
or.setExportPaths(urls);
assertEquals("{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\",\"count\":0,\"total\":0,\"status\":null}",
mapper.writeValueAsString(or));
}

@Test
public void testJsonIgnoreOf() throws JsonMappingException, JsonProcessingException {
ObjectStatus os = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20));
ObjectResult or = ObjectResult.of(os);
assertEquals("{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\",\"count\":10,\"total\":20,\"status\":null}",
mapper.writeValueAsString(or));
}

@Test
public void testJsonIgnoreGetSummary() throws JsonMappingException, JsonProcessingException {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\",\"count\":0,\"total\":0,\"status\":null}",
mapper.writeValueAsString(or));
}
}