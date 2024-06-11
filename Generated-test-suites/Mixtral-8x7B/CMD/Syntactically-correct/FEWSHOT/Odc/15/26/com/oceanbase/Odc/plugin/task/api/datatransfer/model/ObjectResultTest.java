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
public void testObjectResultDefaultConstructor() {
ObjectResult objectResult = new ObjectResult();
assertNotNull(objectResult.getCount());
assertNotNull(objectResult.getSchema());
assertNotNull(objectResult.getStatus());
assertNotNull(objectResult.getTotal());
assertEquals(objectResult.getName(), "");
assertEquals(objectResult.getType(), "");
}

@Test
public void testObjectResultConstructor() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals(objectResult.getName(), "name");
assertEquals(objectResult.getType(), "type");
assertEquals(objectResult.getSchema(), "schema");
assertEquals(objectResult.getCount().get(), 0);
assertEquals(objectResult.getTotal().get(), 0);
}

@Test
public void testOf() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objectResult1 = ObjectResult.of(objectStatus);
ObjectResult objectResult2 = ObjectResult.of(objectStatus);
assertNotSame(objectResult1, objectResult2);
assertEquals(objectResult1.getSchema(), objectResult2.getSchema());
assertEquals(objectResult1.getName(), objectResult2.getName());
assertEquals(objectResult1.getType(), objectResult2.getType());
assertEquals(objectResult1.getCount().get(), objectResult2.getCount().get());
assertEquals(objectResult1.getTotal().get(), objectResult2.getTotal().get());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals(objectResult.getSummary(), "schema.name[type]");
}

@Test
public void testExportPaths() throws MalformedURLException {
List<URL> exportPaths = new ArrayList<>(Arrays.asList(new URL("file:/foo"), new URL("file:/bar")));
ObjectResult objectResult = new ObjectResult();
objectResult.setExportPaths(exportPaths);
assertEquals(objectResult.getExportPaths(), exportPaths);
}

@Test
public void testJsonIgnore() throws JsonProcessingException {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertNull(objectResult.toJSONString());
}
}