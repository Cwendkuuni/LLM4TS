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
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

private ObjectResult objectResult;
 private ObjectStatus objectStatus;

 @Before
 public void setup() {
 objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(0),
 new AtomicLong(0), null);
 objectResult = ObjectResult.of(objectStatus);
 }

 @Test
 public void testObjectResult() {
 assertEquals(objectStatus.getSchema(), objectResult.getSchema());
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals(objectStatus.getCount(), objectResult.getCount());
 assertEquals(objectStatus.getTotal(), objectResult.getTotal());
 }

 @Test
 public void testObjectResultWithURL() throws MalformedURLException {
 List<URL> urls = new ArrayList<>();
 URL url = new URL("http://example.com");
 urls.add(url);
 objectResult.setExportPaths(urls);
 assertEquals(urls, objectResult.getExportPaths());
 }

 @Test
 public void testSummary() {
 String summary = objectResult.getSummary();
 assertTrue(summary.contains(objectResult.getSchema()));
 assertTrue(summary.contains(objectResult.getName()));
 assertTrue(summary.contains(objectResult.getType()));
 }

 @Test(expected = NullPointerException.class)
 public void testNullSchema() {
 new ObjectResult(null, "name", "type");
 }

 @Test(expected = NullPointerException.class)
 public void testNullName() {
 new ObjectResult("schema", null, "type");
 }

 @Test(expected = NullPointerException.class)
 public void testNullType() {
 new ObjectResult("schema", "name", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySchema() {
 new ObjectResult("", "name", "type");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 new ObjectResult("schema", "", "type");
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonProcessingException() throws JsonProcessingException {
 objectResult.setStatus(null);
 }
}