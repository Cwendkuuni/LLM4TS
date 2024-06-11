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
 public void testObjectResultConstructor() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 }

 @Test
 public void testGetSummary() {
 String summary = objectResult.getSummary();
 assertEquals("schema.name[null]", summary);
 }

 @Test
 public void testOf() throws JsonProcessingException {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getType(), result.getType());
 assertEquals(objectStatus.getStatus(), result.getStatus());
 assertEquals(objectStatus.getName(), result.getName());
 assertEquals(objectStatus.getSchema(), result.getSchema());
 assertEquals(objectStatus.getCount().get(), result.getCount().get());
 assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
 }

 @Test
 public void testExportPaths() {
 List<URL> urls = new ArrayList<>();
 URL url;
 try {
 url = new URL("http://example.com");
 urls.add(url);
 } catch (MalformedURLException e) {
 fail("Unexpected MalformedURLException");
 }
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
 public void testSetCount() {
 objectResult.setCount(new AtomicLong(100));
 assertEquals(100, objectResult.getCount().get());
 }

 @Test
 public void testSetTotal() {
 objectResult.setTotal(new AtomicLong(200));
 assertEquals(200, objectResult.getTotal().get());
 }

 @Test
 public void testSetStatus() {
 objectResult.setStatus(Status.ERROR);
 assertEquals(Status.ERROR, objectResult.getStatus());
 }

 @Test
 public void testGetStatus() {
 assertEquals(Status.AVAILABLE, objectResult.getStatus());
 }

 @Test
 public void testGetSchema() {
 assertEquals("schema", objectResult.getSchema());
 }

 @Test
 public void testGetName() {
 assertEquals("name", objectResult.getName());
 }

 @Test
 public void testGetType() {
 assertEquals("null", objectResult.getType());
 }

 @Test
 public void testGetCount() {
 assertEquals(0, objectResult.getCount().get());
 }

 @Test
 public void testGetTotal() {
 assertEquals(0, objectResult.getTotal().get());
 }

}