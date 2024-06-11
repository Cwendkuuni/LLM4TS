import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

private ObjectResult or;

@Before
public void setup() {
or = new ObjectResult("schema", "name", "type");
}

@Test
public void testObjectResult() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
}

@Test
public void testOf() {
ObjectStatus os = new ObjectStatus("schema", new AtomicLong(0), new AtomicLong(0), null);
ObjectResult or2 = ObjectResult.of(os);
assertEquals("schema", or2.getSchema());
assertEquals(0, or2.getCount().get());
assertEquals(0, or2.getTotal().get());
}

@Test
public void testGetSummary() {
assertEquals("schema.name[type]", or.getSummary());
}

@Test
public void testExportPaths() {
List<URL> urls = new ArrayList<>();
try {
urls.add(new URL("http://test.com"));
} catch (MalformedURLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
or.setExportPaths(urls);
assertEquals(urls, or.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
or.setExportPaths(null);
}

@Test(expected = UnsupportedOperationException.class)
public void testGetExportPathsModification() {
List<URL> urls = new ArrayList<>();
try {
urls.add(new URL("http://test.com"));
} catch (MalformedURLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
List<URL> urls2 = or.getExportPaths();
urls2.add(new URL("http://test2.com"));
}

@Test
public void testJsonIgnore() throws JsonProcessingException {
ObjectResult or3 = new ObjectResult("schema", "name", "type");
or3.setExportPaths(new ArrayList<>());
String json = SerializationUtils.serialize(or3);
assertTrue(json.contains("exportPaths"));
json = SerializationUtils.serialize(or);
assertFalse(json.contains("exportPaths"));
}

}