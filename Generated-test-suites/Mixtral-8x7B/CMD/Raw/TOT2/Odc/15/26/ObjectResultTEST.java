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

public class ObjectResultTest {

 private ObjectResult or;

 @Before
 public void setup() {
 or = new ObjectResult("schema", "name", "type");
 }

 @Test
 public void testConstructor() {
 ObjectResult or = new ObjectResult("schema", "name", "type");
 assertNotNull(or);
 assertEquals("schema", or.getSchema());
 assertEquals("name", or.getName());
 assertEquals("type", or.getType());
 assertEquals(0, or.getCount().get());
 assertEquals(0, or.getTotal().get());
 assertEquals(ObjectStatus.Status.AVAILABLE, or.getStatus());
 }

 @Test
 public void testOf() {
 ObjectStatus os = new ObjectStatus();
 os.setSchema("schema");
 os.setName("name");
 os.setType("type");
 os.setCount(new AtomicLong(10));
 os.setTotal(new AtomicLong(20));
 ObjectResult or = ObjectResult.of(os);
 assertEquals("schema", or.getSchema());
 assertEquals("name", or.getName());
 assertEquals("type", or.getType());
 assertEquals(10, or.getCount().get());
 assertEquals(20, or.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 String summary = or.getSummary();
 assertEquals("schema.name[type]", summary);
 }

 @Test
 public void testExportPaths() throws JsonProcessingException, MalformedURLException {
 List<URL> paths = new ArrayList<>();
 paths.add(new URL("http://example.com"));
 or.setExportPaths(paths);
 assertEquals(paths, or.getExportPaths());
 }

 @Test(expected = NullPointerException.class)
 public void testExportPathsNull() {
 or.setExportPaths(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExportPathsEmpty() {
 or.setExportPaths(new ArrayList<URL>());
 }

 @Test
 public void testAtomicLong() {
 AtomicLong count = new AtomicLong(0);
 count.incrementAndGet();
 assertEquals(1, count.get());
 count.decrementAndGet();
 assertEquals(0, count.get());
 }

 @Test
 public void testSetters() {
 or.setSchema("schema1");
 or.setName("name1");
 or.setType("type1");
 or.setCount(new AtomicLong(10));
 or.setTotal(new AtomicLong(20));
 or.setStatus(ObjectStatus.Status.UNAVAILABLE);
 assertEquals("schema1", or.getSchema());
 assertEquals("name1", or.getName());
 assertEquals("type1", or.getType());
 assertEquals(10, or.getCount().get());
 assertEquals(20, or.getTotal().get());
 assertEquals(ObjectStatus.Status.UNAVAILABLE, or.getStatus());
 }

}