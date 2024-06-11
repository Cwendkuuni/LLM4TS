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
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

@Before
public void setUp() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), "type");
objectStatus.setStatus("status");
}

@Test
public void testObjectResult() {
ObjectResult objectResult = new ObjectResult();
assertNotNull(objectResult);
}

@Test
public void testObjectResultWithParameters() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertNotNull(objectResult);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
}

@Test
public void testOfMethod() {
ObjectResult objectResult = ObjectResult.of(new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), "type"));
assertNotNull(objectResult);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals((long) 10, (long) objectResult.getCount().get());
assertEquals((long) 20, (long) objectResult.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

@Test
public void testExportPaths() {
ObjectResult objectResult = new ObjectResult();
List<URL> urls = new ArrayList<>(Arrays.asList(new URL[10]));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test
public void testJsonIgnore() {
ObjectResult objectResult = new ObjectResult();
assertEquals(null, objectResult.getExportPaths());
}

@Test
public void testSetSchema() {
ObjectResult objectResult = new ObjectResult();
objectResult.setSchema("schema");
assertEquals("schema", objectResult.getSchema());
}

@Test
public void testSetName() {
ObjectResult objectResult = new ObjectResult();
objectResult.setName("name");
assertEquals("name", objectResult.getName());
}

@Test
public void testSetType() {
ObjectResult objectResult = new ObjectResult();
objectResult.setType("type");
assertEquals("type", objectResult.getType());
}

@Test
public void testSetCount() {
ObjectResult objectResult = new ObjectResult();
AtomicLong count = new AtomicLong(10);
objectResult.setCount(count);
assertEquals(count, objectResult.getCount());
}

@Test
public void testSetTotal() {
ObjectResult objectResult = new ObjectResult();
AtomicLong total = new AtomicLong(20);
objectResult.setTotal(total);
assertEquals(total, objectResult.getTotal());
}

@Test
public void testSetStatus() {
ObjectResult objectResult = new ObjectResult();
objectResult.setStatus("status");
assertEquals("status", objectResult.getStatus());
}
}