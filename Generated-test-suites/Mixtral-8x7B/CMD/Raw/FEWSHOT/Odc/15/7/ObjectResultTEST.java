import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectResultTest {

@Test
public void testConstructor() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals("schema", objRes.getSchema());
assertEquals("name", objRes.getName());
assertEquals("type", objRes.getType());
assertEquals(0, objRes.getCount().get());
assertEquals(0, objRes.getTotal().get());
assertEquals(ObjectStatus.Status.CREATED, objRes.getStatus());
}

@Test
public void testOfMethod() {
ObjectStatus os = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), ObjectStatus.Status.COMPLETED);
ObjectResult objRes = ObjectResult.of(os);
assertEquals("name", objRes.getName());
assertEquals(10, objRes.getCount().get());
assertEquals(20, objRes.getTotal().get());
assertEquals(ObjectStatus.Status.COMPLETED, objRes.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objRes.getSummary());
}

@Test
public void testSetExportPaths() {
List<URL> urls = new ArrayList<>();
URL url = getClass().getResource("/test.txt");
urls.add(url);
objRes.setExportPaths(urls);
assertEquals(urls, objRes.getExportPaths());
}

@Test(expected = JsonProcessingException.class)
public void testJsonIgnore() throws JsonProcessingException {
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(new ObjectResult("schema", "name", "type"));
}

@Test
public void testToJson() throws JsonProcessingException {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(objRes);
assertNull(json.indexOf("exportPaths"));
}
}