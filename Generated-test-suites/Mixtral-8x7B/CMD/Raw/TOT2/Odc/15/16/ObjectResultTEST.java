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
objectStatus = new ObjectStatus("testSchema", "testName", "testType", new AtomicLong(10), new AtomicLong(20), Status.AVAILABLE);
objectResult = ObjectResult.of(objectStatus);
}

@Test
public void testObjectResult() {
// Test the constructor with valid parameters
assertNotNull(objectResult);
assertEquals(objectResult.getType(), objectStatus.getType());
assertEquals(objectResult.getStatus(), objectStatus.getStatus());
assertEquals(objectResult.getName(), objectStatus.getName());
assertEquals(objectResult.getSchema(), objectStatus.getSchema());
assertEquals(objectResult.getCount().get(), objectStatus.getCount().get());
assertEquals(objectResult.getTotal().get(), objectStatus.getTotal().get());

// Test getSummary method
assertEquals(objectResult.getSummary(), "testSchema.testName[testType]");
}

@Test
public void testOf() {
// Test of method with valid ObjectStatus parameter
assertNotNull(ObjectResult.of(objectStatus));
}

@Test(expected = NullPointerException.class)
public void testOfWithNullParameter() {
// Test of method with null ObjectStatus parameter
ObjectResult.of(null);
}

@Test
public void testExportPaths() throws MalformedURLException {
// Test exportPaths setter and getter
List<URL> urls = new ArrayList<>();
urls.add(new URL("http://test.com"));
objectResult.setExportPaths(urls);
assertEquals(objectResult.getExportPaths(), urls);
}

@Test(expected = MalformedURLException.class)
public void testExportPathsWithInvalidURL() {
// Test exportPaths setter with invalid URL
List<URL> urls = new ArrayList<>();
urls.add(new URL("test.com")); // missing protocol
objectResult.setExportPaths(urls);
}
}