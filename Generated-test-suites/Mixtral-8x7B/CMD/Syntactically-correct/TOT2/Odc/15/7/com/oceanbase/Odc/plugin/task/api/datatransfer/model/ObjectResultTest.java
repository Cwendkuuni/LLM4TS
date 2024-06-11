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
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getSchema(), objectResult.getSchema());
 assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
 assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 String summary = objectResult.getSummary();
 assertEquals("schema.name[null]", summary);
 }

 @Test
 public void testExportPaths() throws JsonProcessingException, MalformedURLException {
 List<URL> urls = new ArrayList<>();
 urls.add(new URL("http://example.com"));
 objectResult.setExportPaths(urls);
 assertEquals(urls, objectResult.getExportPaths());
 }

 @Test(expected = NullPointerException.class)
 public void testNullExportPaths() {
 objectResult.setExportPaths(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyExportPaths() {
 List<URL> urls = objectResult.getExportPaths();
 urls.add(new URL("http://example.com"));
 }

 @Test
 public void testObjectResultWithArguments() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
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
 public void testGetType() {
 assertEquals("null", objectResult.getType());
 }

 @Test
 public void testSetStatus() {
 objectResult.setStatus(Status.FAILED);
 assertEquals(Status.FAILED, objectResult.getStatus());
 }

 @Test
 public void testSetCount() {
 objectResult.setCount(new AtomicLong(10));
 assertEquals(10, objectResult.getCount().get());
 }

 @Test
 public void testSetTotal() {
 objectResult.setTotal(new AtomicLong(20));
 assertEquals(20, objectResult.getTotal().get());
 }

}