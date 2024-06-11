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
 private ObjectStatus os;
 private List<URL> urls;

 @Before
 public void setup() {
 os = new ObjectStatus("testSchema", "testName", "testType", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
 or = ObjectResult.of(os);

 try {
 urls = new ArrayList<URL>();
 urls.add(new URL("http://test1"));
 urls.add(new URL("http://test2"));
 } catch (MalformedURLException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testObjectResult() {
 ObjectResult or1 = new ObjectResult("schema", "name", "type");
 assertEquals("schema", or1.getSchema());
 assertEquals("name", or1.getName());
 assertEquals("type", or1.getType());
 }

 @Test
 public void testGetSummary() {
 assertEquals("testSchema.testName[testType]", or.getSummary());
 }

 @Test
 public void testOf() {
 ObjectResult or2 = ObjectResult.of(os);
 assertEquals(os.getSchema(), or2.getSchema());
 assertEquals(os.getName(), or2.getName());
 assertEquals(os.getType(), or2.getType());
 }

 @Test
 public void testExportPaths() {
 or.setExportPaths(urls);
 assertEquals(urls, or.getExportPaths());
 }

 @Test(expected = NullPointerException.class)
 public void testSetExportPathsNull() {
 or.setExportPaths(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetExportPathsEmpty() {
 or.setExportPaths(new ArrayList<URL>());
 }

 @Test
 public void testSetExportPaths() {
 or.setExportPaths(urls);
 assertEquals(urls, or.getExportPaths());
 }

 @Test
 public void testSetSchema() {
 or.setSchema("newSchema");
 assertEquals("newSchema", or.getSchema());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSchemaEmpty() {
 or.setSchema("");
 }

 @Test
 public void testSetName() {
 or.setName("newName");
 assertEquals("newName", or.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNameEmpty() {
 or.setName("");
 }

 @Test
 public void testSetType() {
 or.setType("newType");
 assertEquals("newType", or.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTypeEmpty() {
 or.setType("");
 }

 @Test
 public void testSetCount() {
 or.setCount(new AtomicLong(50));
 assertEquals(new Long(50), or.getCount().get());
 }

 @Test
 public void testSetTotal() {
 or.setTotal(new AtomicLong(100));
 assertEquals(new Long(100), or.getTotal().get());
 }

 @Test
 public void testStatus() {
 assertEquals(Status.AVAILABLE, or.getStatus());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testExportPathsImmutable() {
 or.getExportPaths().add(new URL("http://test3"));
 }

}