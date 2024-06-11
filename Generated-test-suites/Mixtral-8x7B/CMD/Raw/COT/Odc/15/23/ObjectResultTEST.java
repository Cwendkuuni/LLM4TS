import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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
 objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(0), new AtomicLong(0));
 objectResult = new ObjectResult(objectStatus);
 }

 @Test
 public void testConstructor() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 }

 @Test
 public void testOf() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getSchema(), result.getSchema());
 assertEquals(objectStatus.getName(), result.getName());
 assertEquals(objectStatus.getStatus(), result.getStatus());
 assertEquals(objectStatus.getType(), result.getType());
 }

 @Test
 public void testGetSummary() {
 String summary = objectResult.getSummary();
 assertEquals("schema.name[null]", summary);
 }

 @Test
 public void testExportPaths() throws MalformedURLException {
 List<URL> urls = new ArrayList<>();
 URL url = new URL("http://test.com");
 urls.add(url);
 objectResult.setExportPaths(urls);
 assertEquals(urls, objectResult.getExportPaths());
 }

 @Test
 public void testSetSchema() {
 objectResult.setSchema("newSchema");
 assertEquals("newSchema", objectResult.getSchema());
 }

 @Test
 public void testSetName() {
 objectResult.setName("newName");
 assertEquals("newName", objectResult.getName());
 }

 @Test
 public void testSetType() {
 objectResult.setType("newType");
 assertEquals("newType", objectResult.getType());
 }

 @Test
 public void testSetStatus() {
 objectResult.setStatus(Status.ERROR);
 assertEquals(Status.ERROR, objectResult.getStatus());
 }

 @Test
 public void testSetCount() {
 AtomicLong count = new AtomicLong(10);
 objectResult.setCount(count);
 assertEquals(count, objectResult.getCount());
 }

 @Test
 public void testSetTotal() {
 AtomicLong total = new AtomicLong(100);
 objectResult.setTotal(total);
 assertEquals(total, objectResult.getTotal());
 }

 @Test
 public void testJsonSerialization() throws JsonProcessingException {
 // You can use any Jackson JSON serializer here
 // ...
 }

 @Test(expected = NullPointerException.class)
 public void testSetExportPathsNull() {
 objectResult.setExportPaths(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetExportPathsEmpty() {
 objectResult.setExportPaths(new ArrayList<URL>());
 }

 @Test(expected = MalformedURLException.class)
 public void testSetExportPathsInvalidURL() throws MalformedURLException {
 List<URL> urls = new ArrayList<>();
 urls.add(new URL("invalid://url"));
 objectResult.setExportPaths(urls);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testExportPathsImmutable() {
 List<URL> urls = objectResult.getExportPaths();
 urls.add(new URL("http://test.com"));
 }
}