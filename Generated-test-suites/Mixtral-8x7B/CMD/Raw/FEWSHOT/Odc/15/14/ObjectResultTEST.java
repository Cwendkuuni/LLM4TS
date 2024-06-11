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
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 assertNotNull(objRes.getSchema());
 assertNotNull(objRes.getName());
 assertNotNull(objRes.getType());
 assertEquals("schema", objRes.getSchema());
 assertEquals("name", objRes.getName());
 assertEquals("type", objRes.getType());
 assertEquals(0, objRes.getCount().get());
 assertEquals(0, objRes.getTotal().get());
 }

 @Test
 public void testOf() {
 ObjectResult that = new ObjectResult("schema", "name", "type");
 ObjectResult result = ObjectResult.of(that);
 assertEquals(that.getSchema(), result.getSchema());
 assertEquals(that.getName(), result.getName());
 assertEquals(that.getType(), result.getType());
 assertEquals(that.getCount().get(), result.getCount().get());
 assertEquals(that.getTotal().get(), result.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 assertEquals("schema.name[type]", objRes.getSummary());
 }

 @Test
 public void testExportPaths() {
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 List<URL> exportPaths = new ArrayList<>();
 try {
 exportPaths.add(new URL("http://example.com"));
 } catch (MalformedURLException e) {
 e.printStackTrace();
 }
 objRes.setExportPaths(exportPaths);
 assertEquals(exportPaths, objRes.getExportPaths());
 }

 @Test
 public void testJsonIgnore() {
 ObjectResult objRes = new ObjectResult("schema", "name", "type");
 assertNull(objRes.getExportPaths());
 }
}