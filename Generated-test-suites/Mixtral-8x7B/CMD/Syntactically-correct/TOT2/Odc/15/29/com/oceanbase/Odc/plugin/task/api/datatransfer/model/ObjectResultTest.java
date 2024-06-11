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
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.StatusType;

public class ObjectResultTest {

private ObjectResult objectResult;

@Before
public void setUp() {
objectResult = new ObjectResult("schema", "name", "type");
}

@Test
public void testObjectResult() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
}

@Test
public void testOf() {
ObjectStatus that = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), StatusType.EXPORTING);
ObjectResult result = ObjectResult.of(that);
assertEquals("schema", result.getSchema());
assertEquals(10, result.getCount().get());
assertEquals(20, result.getTotal().get());
assertEquals(StatusType.EXPORTING, result.getStatus());
}

@Test
public void testGetSummary() {
String summary = objectResult.getSummary();
assertEquals("schema.name[type]", summary);
}

@Test
public void testExportPaths() {
List<URL> exportPaths = new ArrayList<>();
try {
exportPaths.add(new URL("http://test.com"));
} catch (MalformedURLException e) {
// never reach here
}
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsNull() {
objectResult.setExportPaths(null);
}

@Test
public void testGettersAndSetters() throws JsonProcessingException {
ObjectStatus os = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), StatusType.EXPORTING);
objectResult = ObjectResult.of(os);
objectResult.setSchema("newSchema");
objectResult.setName("newName");
objectResult.setType("newType");
objectResult.setCount(new AtomicLong(30));
objectResult.setTotal(new AtomicLong(40));
assertEquals("newSchema", objectResult.getSchema());
assertEquals("newName", objectResult.getName());
assertEquals("newType", objectResult.getType());
assertEquals(30, objectResult.getCount().get());
assertEquals(40, objectResult.getTotal().get());
}

@Test
public void testToString() {
String toString = objectResult.toString();
assertNotNull(toString);
}

}