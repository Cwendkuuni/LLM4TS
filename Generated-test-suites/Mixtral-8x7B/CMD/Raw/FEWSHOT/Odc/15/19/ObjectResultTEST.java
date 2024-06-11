import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

@Test
public void testConstructor() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertNotNull(result.getType());
assertNotNull(result.getSchema());
assertNotNull(result.getName());
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(0, result.getCount().get());
assertEquals(0, result.getTotal().get());
}

@Test
public void testOf() {
ObjectStatus that = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult result = ObjectResult.of(that);
assertEquals("name", result.getName());
assertEquals(10, result.getCount().get());
assertEquals(20, result.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", result.getSummary());
}

@Test
public void testExportPaths() throws JsonProcessingException, MalformedURLException {
List<URL> exportPaths = Arrays.asList(new URL("file:/foo"), new URL("file:/bar"));
ObjectResult result = new ObjectResult("schema", "name", "type");
result.setExportPaths(exportPaths);
assertEquals(exportPaths, result.getExportPaths());
assertEquals(2, result.getExportPaths().size());
}

@Test
public void testExportPathsEmpty() throws JsonProcessingException, MalformedURLException {
ObjectResult result = new ObjectResult("schema", "name", "type");
result.setExportPaths(Collections.emptyList());
assertEquals(0, result.getExportPaths().size());
}

@Test
public void testExportPathsNull() throws JsonProcessingException, MalformedURLException {
ObjectResult result = new ObjectResult("schema", "name", "type");
result.setExportPaths(null);
assertNull(result.getExportPaths());
}
}