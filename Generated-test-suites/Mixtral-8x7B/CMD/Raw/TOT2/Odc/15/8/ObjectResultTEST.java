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
public void testObjectResultConstructor() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
}

@Test
public void testGetSummary() {
String expected = "schema.name[type]";
assertEquals(expected, objectResult.getSummary());
}

@Test
public void testOf() {
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getType(), result.getType());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
}

@Test
public void testEdgeCaseExportPaths() {
List<URL> exportPaths = new ArrayList<>();
try {
exportPaths.add(new URL("http://example.com"));
} catch (MalformedURLException e) {
// Ignore
}
objectResult.setExportPaths(exportPaths);
assertEquals(1, objectResult.getExportPaths().size());
}

@Test(expected = NullPointerException.class)
public void testExceptionCaseExportPathsNull() {
objectResult.setExportPaths(null);
}

@Test(expected = UnsupportedOperationException.class)
public void testExceptionCaseExportPathsModification() {
List<URL> exportPaths = new ArrayList<>();
try {
exportPaths.add(new URL("http://example.com"));
} catch (MalformedURLException e) {
// Ignore
}
objectResult.setExportPaths(exportPaths);
objectResult.getExportPaths().remove(0);
}

@Test
public void testExceptionCaseSetStatus() throws JsonProcessingException {
objectResult.setStatus(null);
}

}