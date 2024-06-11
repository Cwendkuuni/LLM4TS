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
objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(0), new AtomicLong(0), null);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
assertNotNull(objectResult);
}

@Test
public void testObjectResultAttributes() {
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals(Status.AVAILABLE, objectResult.getStatus());
assertEquals(new AtomicLong(0), objectResult.getCount());
assertEquals(new AtomicLong(0), objectResult.getTotal());
}

@Test
public void testObjectResultOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertNotNull(result);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
}

@Test
public void testGetSummary() {
assertEquals("schema.name[null]", objectResult.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("http://example.com"));
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
objectResult.setExportPaths(null);
}

@Test(expected = IllegalArgumentException.class)
public void testSetExportPathsEmpty() {
objectResult.setExportPaths(new ArrayList<>());
}

@Test(expected = MalformedURLException.class)
public void testSetExportPathsInvalidURL() {
List<URL> exportPaths = new ArrayList<>();
exportPaths.add(new URL("invalid://example.com"));
objectResult.setExportPaths(exportPaths);
}

@Test
public void testConstructor() {
ObjectResult result = new ObjectResult("schema", "name", "table");
assertNotNull(result);
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("table", result.getType());
assertEquals(new AtomicLong(0), result.getCount());
assertEquals(new AtomicLong(0), result.getTotal());
}

}