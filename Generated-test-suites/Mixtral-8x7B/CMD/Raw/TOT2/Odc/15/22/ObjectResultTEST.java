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

 private ObjectResult or;

 @Before
 public void setUp() {
 or = new ObjectResult("schema", "name", "type");
 }

 @Test
 public void testObjectResult() {
 ObjectResult or = new ObjectResult("schema", "name", "type");
 assertEquals("schema", or.getSchema());
 assertEquals("name", or.getName());
 assertEquals("type", or.getType());
 assertEquals(0, or.getCount().get());
 assertEquals(0, or.getTotal().get());
 assertEquals(Status.AVAILABLE, or.getStatus());
 }

 @Test
 public void testOf() {
 ObjectStatus os = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
 ObjectResult or = ObjectResult.of(os);
 assertEquals("schema", or.getSchema());
 assertEquals(10, or.getCount().get());
 assertEquals(20, or.getTotal().get());
 assertEquals(Status.AVAILABLE, or.getStatus());
 }

 @Test
 public void testGetSummary() {
 assertEquals("schema.name[type]", or.getSummary());
 }

 @Test
 public void testExportPaths() {
 List<URL> urls = new ArrayList<>();
 try {
 urls.add(new URL("http://example.com"));
 } catch (MalformedURLException e) {
 fail("Unexpected MalformedURLException");
 }
 or.setExportPaths(urls);
 assertEquals(urls, or.getExportPaths());
 }

 @Test
 public void testGetCount() {
 assertEquals(0, or.getCount().get());
 or.getCount().incrementAndGet();
 assertEquals(1, or.getCount().get());
 }

 @Test
 public void testGetTotal() {
 assertEquals(0, or.getTotal().get());
 or.getTotal().incrementAndGet();
 assertEquals(1, or.getTotal().get());
 }

 @Test
 public void testSetCount() {
 or.setCount(new AtomicLong(10));
 assertEquals(10, or.getCount().get());
 }

 @Test
 public void testSetTotal() {
 or.setTotal(new AtomicLong(10));
 assertEquals(10, or.getTotal().get());
 }

 @Test
 public void testSetStatus() {
 or.setStatus(Status.AVAILABLE);
 assertEquals(Status.AVAILABLE, or.getStatus());
 }

 @Test(expected = JsonProcessingException.class)
 public void testJsonIgnore() throws JsonProcessingException {
 // This test will fail if Jackson tries to serialize exportPaths
 new ObjectMapper().writeValueAsString(or);
 }

}