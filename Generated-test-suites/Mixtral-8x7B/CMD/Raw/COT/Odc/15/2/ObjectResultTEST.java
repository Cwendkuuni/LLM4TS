import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

@RunWith(MockitoJUnitRunner.class)
public class ObjectResultTest {

 private ObjectResult objectResult;

 @Before
 public void setup() {
 objectResult = new ObjectResult("schema", "name", "type");
 objectResult.setCount(new AtomicLong(10));
 objectResult.setTotal(new AtomicLong(20));
 objectResult.setStatus(Status.AVAILABLE);
 }

 // Test for constructor
 @Test
 public void testObjectResultConstructor() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertNotNull(result);
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 assertEquals(0, result.getCount().get());
 assertEquals(0, result.getTotal().get());
 assertEquals(Status.AVAILABLE, result.getStatus());
 }

 // Test for of method
 @Test
 public void testOfMethod() {
 ObjectStatus that = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
 ObjectResult result = ObjectResult.of(that);
 assertNotNull(result);
 assertEquals("schema", result.getSchema());
 assertEquals(10, result.getCount().get());
 assertEquals(20, result.getTotal().get());
 assertEquals(Status.AVAILABLE, result.getStatus());
 }

 // Test for getSummary method
 @Test
 public void testGetSummary() {
 assertEquals("schema.name[type]", objectResult.getSummary());
 }

 // Test for setExportPaths method
 @Test
 public void testSetExportPaths() {
 List<URL> urls = Arrays.asList(createURL("http://example.com"), createURL("https://example.org"));
 objectResult.setExportPaths(urls);
 assertEquals(urls, objectResult.getExportPaths());
 }

 // Test for getExportPaths method
 @Test
 public void testGetExportPaths() {
 List<URL> urls = Arrays.asList(createURL("http://example.com"), createURL("https://example.org"));
 objectResult.setExportPaths(urls);
 assertEquals(urls, objectResult.getExportPaths());
 }

 // Edge case: null export paths
 @Test
 public void testSetExportPathsNull() {
 objectResult.setExportPaths(null);
 assertEquals(Collections.emptyList(), objectResult.getExportPaths());
 }

 // Exception: setExportPaths with non-URL elements
 @Test(expected = IllegalArgumentException.class)
 public void testSetExportPathsInvalid() {
 List<String> invalidList = Arrays.asList("not a URL", "another invalid URL");
 objectResult.setExportPaths(invalidList);
 }

 // Helper method to create URL
 private URL createURL(String urlString) {
 try {
 return new URL(urlString);
 } catch (MalformedURLException e) {
 throw new RuntimeException(e);
 }
 }

 // Helper method to create JSON processing exception
 private JsonProcessingException createJsonProcessingException() {
 return new JsonProcessingException("test", new RuntimeException("test"));
 }

}