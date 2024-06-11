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

 private ObjectResult objectResult;

 @Before
 public void setUp() {
 objectResult = new ObjectResult("schema", "name", "type");
 objectResult.setCount(new AtomicLong(10));
 objectResult.setTotal(new AtomicLong(20));
 }

 // Test for constructor with 3 arguments
 @Test
 public void testObjectResult() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertNotNull(result);
 }

 // Test for getSummary method
 @Test
 public void testGetSummary() {
 String summary = objectResult.getSummary();
 assertEquals("schema.name[type]", summary);
 }

 // Test for of method
 @Test
 public void testOf() {
 ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
 ObjectResult result = ObjectResult.of(objectStatus);
 assertNotNull(result);
 assertEquals("name", result.getName());
 }

 // Test for setSchema method with null value
 @Test(expected = NullPointerException.class)
 public void testSetSchemaNull() {
 objectResult.setSchema(null);
 }

 // Test for setName method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testSetNameEmpty() {
 objectResult.setName("");
 }

 // Test for setType method with null value
 @Test(expected = NullPointerException.class)
 public void testSetTypeNull() {
 objectResult.setType(null);
 }

 // Test for setType method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testSetTypeEmpty() {
 objectResult.setType("");
 }

 // Test for setCount method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetCountNegative() {
 objectResult.setCount(new AtomicLong(-1));
 }

 // Test for setTotal method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetTotalNegative() {
 objectResult.setTotal(new AtomicLong(-1));
 }

 // Test for exportPaths with add, get and size methods
 @Test
 public void testExportPaths() throws MalformedURLException {
 List<URL> urls = new ArrayList<URL>();
 URL url1 = new URL("http://example.com");
 URL url2 = new URL("https://example.com");
 urls.add(url1);
 urls.add(url2);
 objectResult.setExportPaths(urls);
 assertEquals(2, objectResult.getExportPaths().size());
 assertEquals(url1, objectResult.getExportPaths().get(0));
 assertEquals(url2, objectResult.getExportPaths().get(1));
 }

 // Test for toJsonString method
 @Test
 public void testToJsonString() throws JsonProcessingException {
 // Add the toJsonString method in ObjectStatus class
 // ...
 String jsonString = objectResult.toJsonString();
 // Assert the jsonString
 // ...
 }

}