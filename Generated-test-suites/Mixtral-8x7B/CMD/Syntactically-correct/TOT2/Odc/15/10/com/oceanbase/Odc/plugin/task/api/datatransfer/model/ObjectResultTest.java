import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
assertNotNull(result);
}

@Test
public void testExportPaths() {
List<URL> urls = new ArrayList<>();
try {
urls.add(new URL("http://example.com"));
} catch (MalformedURLException e) {
// Ignore
}
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
objectResult.setExportPaths(null);
}

@Test
public void testSetExportPathsEmpty() {
objectResult.setExportPaths(new ArrayList<>());
assertEquals(0, objectResult.getExportPaths().size());
}

@JsonIgnore
@Test
public void testGetSummaryWithType() {
objectResult.setType("table");
assertEquals("schema.name[table]", objectResult.getSummary());
}

@Test(expected = NullPointerException.class)
public void testObjectResultWithNullSchema() {
new ObjectResult(null, "name", "type");
}

@Test(expected = NullPointerException.class)
public void testObjectResultWithNullName() {
new ObjectResult("schema", null, "type");
}

@Test(expected = NullPointerException.class)
public void testObjectResultWithNullType() {
new ObjectResult("schema", "name", null);
}

@Test
public void testObjectResultWithEmptySchema() {
ObjectResult result = new ObjectResult("", "name", "type");
assertEquals("", result.getSchema());
}

@Test
public void testObjectResultWithEmptyName() {
ObjectResult result = new ObjectResult("schema", "", "type");
assertEquals("", result.getName());
}

@Test
public void testObjectResultWithEmptyType() {
ObjectResult result = new ObjectResult("schema", "name", "");
assertEquals("", result.getType());
}

}