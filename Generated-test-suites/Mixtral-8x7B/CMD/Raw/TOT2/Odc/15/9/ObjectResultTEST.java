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

 private ObjectResult or;

 @Before
 public void setUp() throws Exception {
 or = new ObjectResult("schema", "name", "type");
 }

 @Test
 public void testObjectResult() {
 ObjectResult or = new ObjectResult();
 assertNotNull(or);
 }

 @Test
 public void testObjectResultStringStringString() {
 ObjectResult or = new ObjectResult("schema", "name", "type");
 assertEquals("schema", or.getSchema());
 assertEquals("name", or.getName());
 assertEquals("type", or.getType());
 }

 @Test
 public void testOf() {
 ObjectStatus os = new ObjectStatus();
 os.setStatus("status");
 ObjectResult or = ObjectResult.of(os);
 assertEquals("status", or.getStatus());
 }

 @Test
 public void testGetSummary() {
 assertEquals("schema.name[type]", or.getSummary());
 }

 @Test
 public void testExportPaths() {
 List<URL> list = new ArrayList<>();
 try {
 list.add(new URL("http://test.com"));
 } catch (MalformedURLException e) {
 e.printStackTrace();
 }
 or.setExportPaths(list);
 assertEquals(list, or.getExportPaths());
 }

 @Test(expected = NullPointerException.class)
 public void testSetExportPathsNull() {
 or.setExportPaths(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetExportPathsEmpty() {
 or.setExportPaths(new ArrayList<URL>());
 }

 @Test
 public void testGetSchema() {
 assertEquals("schema", or.getSchema());
 }

 @Test
 public void testSetSchema() {
 or.setSchema("newSchema");
 assertEquals("newSchema", or.getSchema());
 }

 @Test
 public void testGetName() {
 assertEquals("name", or.getName());
 }

 @Test
 public void testSetName() {
 or.setName("newName");
 assertEquals("newName", or.getName());
 }

 @Test
 public void testGetType() {
 assertEquals("type", or.getType());
 }

 @Test
 public void testSetType() {
 or.setType("newType");
 assertEquals("newType", or.getType());
 }

 @Test
 public void testGetCount() {
 assertEquals(new Long(0), or.getCount().get());
 }

 @Test
 public void testSetCount() {
 or.getCount().set(10);
 assertEquals(new Long(10), or.getCount().get());
 }

 @Test
 public void testGetTotal() {
 assertEquals(new Long(0), or.getTotal().get());
 }

 @Test
 public void testSetTotal() {
 or.getTotal().set(10);
 assertEquals(new Long(10), or.getTotal().get());
 }

 @Test
 public void testSetStatus() {
 or.setStatus("newStatus");
 assertEquals("newStatus", or.getStatus());
 }

 @Test
 public void testToString() throws JsonProcessingException {
 String json = "{\"schema\":\"schema\",\"name\":\"name\",\"type\":\"type\",\"status\":null,\"count\":0,\"total\":0,\"exportPaths\":[]}";
 assertEquals(json, or.toString());
 }

}