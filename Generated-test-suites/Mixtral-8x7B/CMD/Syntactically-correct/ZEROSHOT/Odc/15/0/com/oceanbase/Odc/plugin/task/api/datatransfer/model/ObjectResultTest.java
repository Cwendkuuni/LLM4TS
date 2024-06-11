import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ObjectResultTest {

@Before
public void setup() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), null);
objectResult = ObjectResult.of(objectStatus);
}

private ObjectResult objectResult;

@Test
public void testObjectResult() {
ObjectResult result = new ObjectResult();
assertEquals(result.getSchema(), null);
assertEquals(result.getName(), null);
assertEquals(result.getType(), null);
assertEquals(result.getCount(), new AtomicLong(0));
assertEquals(result.getTotal(), new AtomicLong(0));
}

@Test
public void testObjectResultWithParameters() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals(result.getSchema(), "schema");
assertEquals(result.getName(), "name");
assertEquals(result.getType(), "type");
assertEquals(result.getCount(), new AtomicLong(0));
assertEquals(result.getTotal(), new AtomicLong(0));
}

@Test
public void testOfMethod() {
assertEquals(objectResult.getSchema(), "schema");
assertEquals(objectResult.getName(), "name");
assertEquals(objectResult.getType(), null);
assertEquals(objectResult.getCount(), new AtomicLong(10));
assertEquals(objectResult.getTotal(), new AtomicLong(20));
}

@Test
public void testGetSummary() {
assertEquals(objectResult.getSummary(), "schema.name[null]");
}

@Test
public void testSetters() {
objectResult.setSchema("newSchema");
objectResult.setName("newName");
objectResult.setType("newType");
objectResult.setCount(new AtomicLong(30));
objectResult.setTotal(new AtomicLong(40));
assertEquals(objectResult.getSchema(), "newSchema");
assertEquals(objectResult.getName(), "newName");
assertEquals(objectResult.getType(), "newType");
assertEquals(objectResult.getCount(), new AtomicLong(30));
assertEquals(objectResult.getTotal(), new AtomicLong(40));
}

@Test
public void testExportPaths() {
List<URL> urls = new ArrayList<>(Arrays.asList(new URL[] { new URL("http://example.com") }));
objectResult.setExportPaths(urls);
assertEquals(objectResult.getExportPaths(), urls);
}

@Test
public void testEqualsAndHashCode() {
ObjectResult result = new ObjectResult("schema", "name", "type");
result.setCount(new AtomicLong(10));
result.setTotal(new AtomicLong(20));
ObjectResult result2 = new ObjectResult("schema", "name", "type");
result2.setCount(new AtomicLong(10));
result2.setTotal(new AtomicLong(20));
ObjectResult result3 = new ObjectResult("schema", "name", "type");
result3.setCount(new AtomicLong(20));
result3.setTotal(new AtomicLong(20));
assertEquals(objectResult, result);
assertEquals(objectResult.hashCode(), result.hashCode());
assertNotEquals(objectResult, result2);
assertNotEquals(objectResult.hashCode(), result2.hashCode());
assertEquals(objectResult, result3);
assertEquals(objectResult.hashCode(), result3.hashCode());
}

}