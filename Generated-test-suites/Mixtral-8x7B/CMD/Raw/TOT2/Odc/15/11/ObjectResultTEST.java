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
 assertEquals("schema", objectResult.getSchema());
 assertEquals("name", objectResult.getName());
 assertEquals(Status.AVAILABLE, objectResult.getStatus());
 assertEquals(new AtomicLong(0), objectResult.getCount());
 assertEquals(new AtomicLong(0), objectResult.getTotal());
 }

 @Test
 public void testGetSummary() {
 assertEquals("schema.name[null]", objectResult.getSummary());
 }

 @Test
 public void testExportPaths() throws MalformedURLException {
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("http://example.com"));
 objectResult.setExportPaths(exportPaths);
 assertEquals(exportPaths, objectResult.getExportPaths());
 }

 @Test
 public void testConstructor() {
 ObjectResult result = new ObjectResult("schema", "name", "type");
 assertEquals("schema", result.getSchema());
 assertEquals("name", result.getName());
 assertEquals("type", result.getType());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullSchema() {
 new ObjectResult(null, "name", "type");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullName() {
 new ObjectResult("schema", null, "type");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullType() {
 new ObjectResult("schema", "name", null);
 }

 @Test
 public void testOf() {
 ObjectResult result = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getType(), result.getType());
 assertEquals(objectStatus.getStatus(), result.getStatus());
 assertEquals(objectStatus.getName(), result.getName());
 assertEquals(objectStatus.getSchema(), result.getSchema());
 assertEquals(objectStatus.getCount(), result.getCount());
 assertEquals(objectStatus.getTotal(), result.getTotal());
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 // Serialize the object using a Jackson JSON mapper
 // Add assertions to ensure the serialized JSON contains the expected properties
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 // Deserialize the JSON string back into an ObjectResult instance
 // Add assertions to ensure the deserialized instance matches the original object's properties
 }
}