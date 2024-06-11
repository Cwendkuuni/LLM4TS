import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testConstructor() {
 ObjectResult objResult = new ObjectResult("schema", "name", "type");
 assertNotNull(objResult);
 assertEquals("schema", objResult.getSchema());
 assertEquals("name", objResult.getName());
 assertEquals("type", objResult.getType());
 assertEquals(0L, objResult.getCount().get());
 assertEquals(0L, objResult.getTotal().get());
 }

 @Test
 public void testOf() {
 ObjectStatus objStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
 ObjectResult objResult = ObjectResult.of(objStatus);
 assertNotNull(objResult);
 assertEquals(objStatus.getSchema(), objResult.getSchema());
 assertEquals(objStatus.getName(), objResult.getName());
 assertEquals(objStatus.getType(), objResult.getType());
 assertEquals(objStatus.getCount().get(), objResult.getCount().get());
 assertEquals(objStatus.getTotal().get(), objResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objResult = new ObjectResult("schema", "name", "type");
 assertEquals("schema.name[type]", objResult.getSummary());
 }

 @Test
 public void testExportPaths() {
 ObjectResult objResult = new ObjectResult("schema", "name", "type");
 List<URL> urls = new ArrayList<>();
 URL url1 = null;
 URL url2 = null;
 try {
 url1 = new URL("http://example.com");
 url2 = new URL("https://example.org");
 } catch (MalformedURLException e) {
 e.printStackTrace();
 }
 urls.add(url1);
 urls.add(url2);
 objResult.setExportPaths(urls);
 assertEquals(2, objResult.getExportPaths().size());
 assertEquals(url1, objResult.getExportPaths().get(0));
 assertEquals(url2, objResult.getExportPaths().get(1));
 }

 @Test
 public void testJsonIgnore() {
 ObjectResult objResult = new ObjectResult("schema", "name", "type");
 assertNull(objResult.getExportPaths());
 }

}