import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 @Test
 public void testObjectResultEmptyConstructor() {
 ObjectResult objectResult = new ObjectResult();
 assertNotNull(objectResult.getName());
 assertNotNull(objectResult.getType());
 assertNotNull(objectResult.getSchema());
 assertNotNull(objectResult.getCount());
 assertNotNull(objectResult.getTotal());
 assertNull(objectResult.getExportPaths());
 assertEquals(objectResult.getStatus(), ObjectStatus.Status.CREATED);
 }

 @Test
 public void testObjectResultConstructor() throws MalformedURLException {
 List<URL> exportPaths = new ArrayList<URL>();
 exportPaths.add(new URL("http://example.com"));
 ObjectResult objectResult = new ObjectResult("schema", "name", "type", exportPaths);
 assertEquals(objectResult.getSchema(), "schema");
 assertEquals(objectResult.getName(), "name");
 assertEquals(objectResult.getType(), "type");
 assertEquals(objectResult.getCount().get(), 0);
 assertEquals(objectResult.getTotal().get(), 0);
 assertEquals(objectResult.getExportPaths(), exportPaths);
 assertEquals(objectResult.getStatus(), ObjectStatus.Status.CREATED);
 }

 @Test
 public void testOf() {
 ObjectStatus objectStatus = new ObjectStatus("schema", "name", "type", 0, 0, ObjectStatus.Status.CREATED);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals(objectResult.getSchema(), "schema");
 assertEquals(objectResult.getName(), "name");
 assertEquals(objectResult.getType(), "type");
 assertEquals(objectResult.getCount().get(), 0);
 assertEquals(objectResult.getTotal().get(), 0);
 assertEquals(objectResult.getStatus(), ObjectStatus.Status.CREATED);
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema", "name", "type");
 assertEquals(objectResult.getSummary(), "schema.name[type]");
 }
}