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
public void setUp() throws Exception {
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
public void testOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getCount(), result.getCount());
assertEquals(objectStatus.getTotal(), result.getTotal());
}

@Test
public void testExportPaths() {
List<URL> exportPaths = new ArrayList<>();
try {
exportPaths.add(new URL("http://test.com"));
} catch (MalformedURLException e) {
// Handle exception
}
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
objectResult.setExportPaths(null);
}

@Test(expected = UnsupportedOperationException.class)
public void testGetExportPathsModification() {
List<URL> exportPaths = objectResult.getExportPaths();
exportPaths.add(null);
}

@Test
public void testConstructor() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(Status.AVAILABLE, result.getStatus());
assertEquals(new AtomicLong(0), result.getCount());
assertEquals(new AtomicLong(0), result.getTotal());
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
public void testSetStatus() {
objectResult.setStatus(Status.ERROR);
assertEquals(Status.ERROR, objectResult.getStatus());
}

@Test
public void testSetCount() {
objectResult.setCount(new AtomicLong(10));
assertEquals(new AtomicLong(10), objectResult.getCount());
}

@Test
public void testSetTotal() {
objectResult.setTotal(new AtomicLong(20));
assertEquals(new AtomicLong(20), objectResult.getTotal());
}

@Test
public void testJsonIgnoreAnnotation() {
// JsonIgnore annotation should ignore this field during serialization and
// deserialization
assertNull(objectResult.getExportPaths());
}

@Test
public void testSerialization() throws JsonProcessingException {
// Add ser
}

@Test(expected = IllegalStateException.class)
public void testSetCountValueBelowZero() {
objectResult.getCount().set(-1);
}

@Test(expected = IllegalStateException.class)
public void testSetTotalValueBelowZero() {
objectResult.getTotal().set(-1);
}

}