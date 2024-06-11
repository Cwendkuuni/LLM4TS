import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testConstructor() {
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 assertNotNull(objRes.getSchema());
 assertEquals("name", objRes.getName());
 assertEquals("type", objRes.getType());
 assertEquals(0, objRes.getCount().get());
 assertEquals(0, objRes.getTotal().get());
 }

 @Test
 public void testOf() {
 ObjectStatus objStat = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
 ObjectResult objRes = ObjectResult.of(objStat);
 assertEquals("name", objRes.getName());
 assertEquals(10, (long) objRes.getCount().get());
 assertEquals(20, (long) objRes.getTotal().get());
 assertEquals(null, objRes.getSchema());
 }

 @Test
 public void testGetSummary() throws JsonProcessingException {
 ObjectStatus objStat = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), null);
 ObjectResult objRes = ObjectResult.of(objStat);
 assertEquals("schema.name[type]", objRes.getSummary());
 }

 @Test
 public void testExportPaths() {
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 List<URL> exportPaths = new ArrayList<>();
 URL url1 = new URL("http://example.com");
 URL url2 = new URL("https://example.org");
 exportPaths.add(url1);
 exportPaths.add(url2);
 objRes.setExportPaths(exportPaths);
 assertEquals(2, objRes.getExportPaths().size());
 assertEquals(url1, objRes.getExportPaths().get(0));
 assertEquals(url2, objRes.getExportPaths().get(1));
 }

 @Test
 public void testJsonIgnore() {
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 assertNull(objRes.getExportPaths());
 }
}