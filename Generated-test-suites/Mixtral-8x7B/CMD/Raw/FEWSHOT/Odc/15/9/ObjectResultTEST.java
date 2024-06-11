import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testConstructor() {
 ObjectResult objRes = new ObjectResult("schema1", "table1", "TABLE");
 assertNotNull(objRes);
 assertEquals("schema1", objRes.getSchema());
 assertEquals("table1", objRes.getName());
 assertEquals("TABLE", objRes.getType());
 assertEquals(0, objRes.getCount().get());
 assertEquals(0, objRes.getTotal().get());
 }

 @Test
 public void testOf() {
 ObjectStatus objSts = new ObjectStatus("table1", new AtomicLong(10), new AtomicLong(100), "COMPLETED");
 ObjectResult objRes = ObjectResult.of(objSts);
 assertEquals("table1", objRes.getName());
 assertEquals("COMPLETED", objRes.getStatus());
 assertEquals(10, objRes.getCount().get());
 assertEquals(100, objRes.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objRes = new ObjectResult("schema1", "table1", "TABLE");
 assertEquals("schema1.table1[TABLE]", objRes.getSummary());
 }

 @Test
 public void testExportPaths() throws MalformedURLException {
 URL url1 = new URL("http://example.com/path1");
 URL url2 = new URL("https://example.com/path2");
 List<URL> urls = new ArrayList<>(Arrays.asList(url1, url2));
 ObjectResult objRes = new ObjectResult("schema1", "table1", "TABLE");
 objRes.setExportPaths(urls);
 assertEquals(urls, objRes.getExportPaths());
 }

 @Test
 public void testExportPathsNull() {
 ObjectResult objRes = new ObjectResult("schema1", "table1", "TABLE");
 objRes.setExportPaths(null);
 assertNull(objRes.getExportPaths());
 }
}