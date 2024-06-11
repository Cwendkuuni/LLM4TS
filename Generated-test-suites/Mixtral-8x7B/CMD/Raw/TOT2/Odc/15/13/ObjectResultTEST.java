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
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

private ObjectResult objectResult;
private ObjectStatus objectStatus;

@Before
public void setup() {
objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE, new AtomicLong(0), new AtomicLong(0), null);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResultWithValidArguments() {
ObjectResult result = new ObjectResult("schema", "name", "type");
assertEquals("schema", result.getSchema());
assertEquals("name", result.getName());
assertEquals("type", result.getType());
}

@Test
public void testOfMethodWithValidObjectStatus() {
ObjectResult result = ObjectResult.of(objectStatus);
assertEquals(objectStatus.getSchema(), result.getSchema());
assertEquals(objectStatus.getName(), result.getName());
assertEquals(objectStatus.getType(), result.getType());
assertEquals(objectStatus.getStatus(), result.getStatus());
assertEquals(objectStatus.getCount().get(), result.getCount().get());
assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
}

@Test
public void testGetSummaryWithValidObjectResult() {
String summary = objectResult.getSummary();
assertEquals("schema.name[type]", summary);
}

@Test
public void testExportPathsWithSetterAndGetter() throws JsonProcessingException, MalformedURLException {
List<URL> urls = new ArrayList<URL>();
urls.add(new URL("http://test.com"));
objectResult.setExportPaths(urls);
assertEquals(urls, objectResult.getExportPaths());
}

@Test(expected = NullPointerException.class)
public void testOfMethodWithNullObjectStatus() {
ObjectResult.of(null);
}

@Test(expected = IllegalArgumentException.class)
public void testObjectResultWithNullSchema() {
new ObjectResult(null, "name", "type");
}

@Test(expected = IllegalArgumentException.class)
public void testObjectResultWithNullName() {
new ObjectResult("schema", null, "type");
}

@Test(expected = IllegalArgumentException.class)
public void testObjectResultWithNullType() {
new ObjectResult("schema", "name", null);
}

@Test(expected = MalformedURLException.class)
public void testExportPathsWithMalformedURL() throws MalformedURLException {
List<URL> urls = new ArrayList<URL>();
urls.add(new URL("ht//test.com"));
objectResult.setExportPaths(urls);
}

}