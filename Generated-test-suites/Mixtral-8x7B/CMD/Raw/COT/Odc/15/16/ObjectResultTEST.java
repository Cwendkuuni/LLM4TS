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
 public void setUp() throws Exception {
 objectResult = new ObjectResult("schema", "name", "type");
 }

 @Test
 public void testObjectResult() {
 // Check if the object is correctly initialized
 // with the given schema, name and type
 assertNotNull(objectResult);
 assertEquals("schema", objectResult.getSchema());
 assertEquals("name", objectResult.getName());
 assertEquals("type", objectResult.getType());
 }

 @Test
 public void testOf() throws JsonProcessingException {
 // Create an ObjectStatus for testing
 ObjectStatus objectStatus = new ObjectStatus("schema", "name", "type");
 objectStatus.setCount(new AtomicLong(10));
 objectStatus.setTotal(new AtomicLong(20));

 // Use the 'of' method to create an ObjectResult
 ObjectResult result = ObjectResult.of(objectStatus);

 // Check if the ObjectResult is correctly created
 // with the same schema, name, type, count and total
 assertEquals(objectStatus.getSchema(), result.getSchema());
 assertEquals(objectStatus.getName(), result.getName());
 assertEquals(objectStatus.getType(), result.getType());
 assertEquals(objectStatus.getCount(), result.getCount());
 assertEquals(objectStatus.getTotal(), result.getTotal());
 }

 @Test
 public void testGetSummary() {
 // Check if the summary is correctly generated
 assertEquals("schema.name[type]", objectResult.getSummary());
 }

 @Test
 public void testExportPaths() throws MalformedURLException {
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("file:/foo/bar"));
 objectResult.setExportPaths(exportPaths);

 // Check if the exportPaths are correctly set
 assertEquals(exportPaths, objectResult.getExportPaths());
 }

 @Test
 public void testSetSchema() {
 // Check if setting a new schema works correctly
 objectResult.setSchema("newSchema");
 assertEquals("newSchema", objectResult.getSchema());
 }

 @Test
 public void testSetName() {
 // Check if setting a new name works correctly
 objectResult.setName("newName");
 assertEquals("newName", objectResult.getName());
 }

 @Test
 public void testSetType() {
 // Check if setting a new type works correctly
 objectResult.setType("newType");
 assertEquals("newType", objectResult.getType());
 }

 @Test
 public void testSetCount() {
 // Check if setting a new count works correctly
 objectResult.setCount(new AtomicLong(100));
 assertEquals(new Long(100), objectResult.getCount().get());
 }

 @Test
 public void testSetTotal() {
 // Check if setting a new total works correctly
 objectResult.setTotal(new AtomicLong(200));
 assertEquals(new Long(200), objectResult.getTotal().get());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullSchema() {
 // Check if a NullPointerException is thrown
 // when setting a null schema
 objectResult.setSchema(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullName() {
 // Check if a NullPointerException is thrown
 // when setting a null name
 objectResult.setName(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullType() {
 // Check if a NullPointerException is thrown
 // when setting a null type
 objectResult.setType(null);
 }

}