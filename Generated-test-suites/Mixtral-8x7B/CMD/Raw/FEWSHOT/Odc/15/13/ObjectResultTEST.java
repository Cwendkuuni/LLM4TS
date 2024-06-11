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
public void testObjectResult() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals("schema", objRes.getSchema());
assertEquals("name", objRes.getName());
assertEquals("type", objRes.getType());
assertEquals(new AtomicLong(0), objRes.getCount());
assertEquals(new AtomicLong(0), objRes.getTotal());
}

@Test
public void testOf() {
ObjectStatus that = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "status");
ObjectResult objRes = ObjectResult.of(that);
assertEquals(that.getStatus(), objRes.getStatus());
assertEquals("name", objRes.getName());
assertEquals(new AtomicLong(10), objRes.getCount());
assertEquals(new AtomicLong(20), objRes.getTotal());
}

@Test
public void testGetSummary() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objRes.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException {
ObjectMapper mapper = new ObjectMapper();
URL url1 = new URL("http://example.com/1");
URL url2 = new URL("http://example.com/2");
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(url1);
exportPaths.add(url2);
ObjectResult objRes = new ObjectResult("schema", "name", "type");
objRes.setExportPaths(exportPaths);
String json = mapper.writeValueAsString(objRes);
assertEquals(true, json.contains(url1.toString()));
assertEquals(true, json.contains(url2.toString()));
}

@Test
public void testGetExportPaths() {
List<URL> exportPaths = new ArrayList<>();
URL url1 = new URL("http://example.com/1");
exportPaths.add(url1);
ObjectResult objRes = new ObjectResult("schema", "name", "type");
objRes.setExportPaths(exportPaths);
assertEquals(exportPaths, objRes.getExportPaths());
}

@Test
public void testJsonIgnore() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals(null, objRes.getExportPaths());
}

@Test
public void testSetExportPaths() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
List<URL> exportPaths = new ArrayList<>();
URL url1 = new URL("http://example.com/1");
exportPaths.add(url1);
objRes.setExportPaths(exportPaths);
assertEquals(exportPaths, objRes.getExportPaths());
}

@Test
public void testSetExportPathsNull() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
objRes.setExportPaths(null);
assertEquals(null, objRes.getExportPaths());
}

@Test
public void testSetExportPathsEmpty() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
List<URL> emptyList = new ArrayList<>();
objRes.setExportPaths(emptyList);
assertEquals(emptyList, objRes.getExportPaths());
}
}