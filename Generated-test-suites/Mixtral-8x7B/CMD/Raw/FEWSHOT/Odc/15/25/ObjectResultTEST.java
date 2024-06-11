import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

@Test
public void testOf_shouldReturnCorrectObjectResult() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", "type", Status.AVAILABLE, new AtomicLong(10),
new AtomicLong(20));

ObjectResult result = ObjectResult.of(objectStatus);

assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
assertEquals(Status.AVAILABLE, result.getStatus());
assertEquals(new AtomicLong(10), result.getCount());
assertEquals(new AtomicLong(20), result.getTotal());
}

@Test
public void testGetSummary_shouldReturnCorrectSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");

String summary = objectResult.getSummary();

assertEquals("schema.name[type]", summary);
}

@Test
public void testGetSummary_shouldReturnCorrectSummaryWithEmptySchema() {
ObjectResult objectResult = new ObjectResult(null, "name", "type");

String summary = objectResult.getSummary();

assertEquals("name[type]", summary);
}

@Test
public void testGetExportPaths_shouldReturnNull() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");

assertNull(objectResult.getExportPaths());
}

@Test
public void testGetExportPaths_shouldReturnEmptyList() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setExportPaths(Arrays.asList());

List<URL> exportPaths = objectResult.getExportPaths();

assertEquals(0, exportPaths.size());
}

@Test
public void testGetExportPaths_shouldReturnCorrectList() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
URL url1 = mock(URL.class);
URL url2 = mock(URL.class);
List<URL> urls = Arrays.asList(url1, url2);
objectResult.setExportPaths(urls);

List<URL> exportPaths = objectResult.getExportPaths();

assertEquals(urls, exportPaths);
assertEquals(2, exportPaths.size());
assertEquals(url1, exportPaths.get(0));
assertEquals(url2, exportPaths.get(1));
}

@Test
public void testGetExportPaths_shouldReturnCorrectListWithNull() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
URL url1 = mock(URL.class);
List<URL> urls = Arrays.asList(url1, null);
objectResult.setExportPaths(urls);

List<URL> exportPaths = objectResult.getExportPaths();

assertEquals(urls, exportPaths);
assertEquals(2, exportPaths.size());
assertEquals(url1, exportPaths.get(0));
assertEquals(null, exportPaths.get(1));
}

@Test
public void testGetExportPaths_shouldReturnCorrectListWithEmptyString() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
URL url1 = mock(URL.class);
List<URL> urls = Arrays.asList(url1, new URL(""));
objectResult.setExportPaths(urls);

List<URL> exportPaths = objectResult.getExportPaths();

assertEquals(urls, exportPaths);
assertEquals(2, exportPaths.size());
assertEquals(url1, exportPaths.get(0));
assertEquals(new URL(""), exportPaths.get(1));
}

@JsonIgnore
public void testGetExportPaths_shouldThrowException() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");

// @JsonIgnore should throw exception here
when(objectResult.getExportPaths()).thenCallRealMethod();

// assert exception here
}
}