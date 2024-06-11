import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

@Test
public void testConstructor() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertNotNull(result.getSchema());
assertEquals("schema", result.getSchema());

assertNotNull(result.getName());
assertEquals("name", result.getName());

assertNotNull(result.getType());
assertEquals("type", result.getType());

assertNotNull(result.getCount());
assertNotNull(result.getTotal());
assertNotNull(result.getStatus());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
assertEquals(ObjectStatus.Status.CREATED, result.getStatus());
}

@Test
public void testOf() {
ObjectStatus that = new ObjectResult("schema", "name", "type");
that.setStatus(ObjectStatus.Status.FINISHED);
that.getCount().set(10);
that.getTotal().set(20);

ObjectResult result = ObjectResult.of(that);
assertEquals(that.getSchema(), result.getSchema());
assertEquals(that.getName(), result.getName());
assertEquals(that.getType(), result.getType());
assertEquals(that.getCount().get(), result.getCount().get());
assertEquals(that.getTotal().get(), result.getTotal().get());
assertEquals(that.getStatus(), result.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", result.getSummary());
}

@Test
public void testExportPaths() throws MalformedURLException {
ObjectResult result = new ObjectResult("schema", "name", "type");

List<URL> exportPaths1 = new ArrayList<>();
exportPaths1.add(new URL("file:/foo"));
result.setExportPaths(exportPaths1);
assertEquals(exportPaths1, result.getExportPaths());

List<URL> exportPaths2 = new ArrayList<>(Arrays.asList(new URL("file:/foo"), new URL("file:/bar")));
result.setExportPaths(exportPaths2);
assertEquals(exportPaths2, result.getExportPaths());

result.setExportPaths(null);
assertNull(result.getExportPaths());
}
}