import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
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
ObjectResult or = new ObjectResult("schema", "name", "type");
assertNotNull(or.getSchema());
assertEquals("schema", or.getSchema());

assertNotNull(or.getName());
assertEquals("name", or.getName());

assertNotNull(or.getType());
assertEquals("type", or.getType());

assertNotNull(or.getCount());
assertNotNull(or.getTotal());
}

@Test
public void testOf() {
ObjectStatus os = new ObjectStatus("schema", "name", "type", new AtomicLong(0), new AtomicLong(0), null);
ObjectResult or = ObjectResult.of(os);

assertNotNull(or.getSchema());
assertEquals("schema", or.getSchema());

assertNotNull(or.getName());
assertEquals("name", or.getName());

assertNotNull(or.getType());
assertEquals("type", or.getType());

assertNotNull(or.getCount());
assertEquals(os.getCount().get(), or.getCount().get());

assertNotNull(or.getTotal());
assertEquals(os.getTotal().get(), or.getTotal().get());

assertNotSame(os, or);
}

@Test
public void testGetSummary() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", or.getSummary());
}

@Test
public void testExportPaths() {
ObjectResult or = new ObjectResult("schema", "name", "type");
List<URL> paths = new ArrayList<>();
paths.add(createURL("http://localhost:8080/1"));
paths.add(createURL("http://localhost:8080/2"));
or.setExportPaths(paths);

assertEquals(paths, or.getExportPaths());
}

@Test
public void testSetExportPathsNull() {
ObjectResult or = new ObjectResult("schema", "name", "type");
or.setExportPaths(null);
assertNull(or.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testSetExportPathsEmpty() {
ObjectResult or = new ObjectResult("schema", "name", "type");
or.setExportPaths(Arrays.asList((URL[]) null));
}

@Test
public void testSetExportPathsEmptyList() {
ObjectResult or = new ObjectResult("schema", "name", "type");
or.setExportPaths(new ArrayList<>());
assertNotNull(or.getExportPaths());
assertEquals(0, or.getExportPaths().size());
}

@Test
public void testSetExportPathsOneItem() {
ObjectResult or = new ObjectResult("schema", "name", "type");
List<URL> paths = new ArrayList<>();
paths.add(createURL("http://localhost:8080/1"));
or.setExportPaths(paths);
assertEquals(paths.size(), or.getExportPaths().size());
assertEquals(paths.get(0), or.getExportPaths().get(0));
}

@Test
public void testSetExportPathsMultipleItems() {
ObjectResult or = new ObjectResult("schema", "name", "type");
List<URL> paths = new ArrayList<>();
paths.add(createURL("http://localhost:8080/1"));
paths.add(createURL("http://localhost:8080/2"));
or.setExportPaths(paths);
assertEquals(paths.size(), or.getExportPaths().size());
assertEquals(paths.get(0), or.getExportPaths().get(0));
assertEquals(paths.get(1), or.getExportPaths().get(1));
}

private URL createURL(String path) {
try {
return new URL(path);
} catch (MalformedURLException e) {
throw new RuntimeException(e);
}
}
}