import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ObjectResultTest {

@Test
public void testObjectResult() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
assertNull(objectResult.getExportPaths());
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20), ObjectStatus.Status.AVAILABLE);
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
assertNull(objectResult.getExportPaths());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testSetExportPaths() {
List<URL> exportPaths = new ArrayList<>(Arrays.asList(new URL("http://example.com"), new URL("http://example2.com")));
ObjectResult objectResult = new ObjectResult();
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test
public void testGetExportPaths() {
List<URL> exportPaths = new ArrayList<>(Arrays.asList(new URL("http://example.com"), new URL("http://example2.com")));
ObjectResult objectResult = new ObjectResult();
objectResult.setExportPaths(exportPaths);
assertEquals(exportPaths, objectResult.getExportPaths());
}

@Test
public void testEquals() {
ObjectResult objectResult1 = new ObjectResult("schema", "name", "type");
ObjectResult objectResult2 = new ObjectResult("schema", "name", "type");
assertEquals(objectResult1, objectResult2);
assertEquals(objectResult1.hashCode(), objectResult2.hashCode());

objectResult2.setCount(new AtomicLong(1));
assertNotEquals(objectResult1, objectResult2);

objectResult2.setCount(new AtomicLong(0));
objectResult2.setStatus(ObjectStatus.Status.UNAVAILABLE);
assertNotEquals(objectResult1, objectResult2);

objectResult2.setStatus(ObjectStatus.Status.AVAILABLE);
objectResult2.setTotal(new AtomicLong(1));
assertNotEquals(objectResult1, objectResult2);

objectResult2.setTotal(new AtomicLong(0));
objectResult2.setExportPaths(new ArrayList<>(Arrays.asList(new URL("http://example.com"))));
assertNotEquals(objectResult1, objectResult2);
}

}