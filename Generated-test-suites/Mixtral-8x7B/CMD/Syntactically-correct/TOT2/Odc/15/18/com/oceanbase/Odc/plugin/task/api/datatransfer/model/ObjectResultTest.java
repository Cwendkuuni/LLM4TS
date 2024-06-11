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
public void testObjectResult() {
// Test the basic functionality of the constructor
assertNotNull(objectResult);
assertEquals(objectResult.getSchema(), objectStatus.getSchema());
assertEquals(objectResult.getName(), objectStatus.getName());
assertEquals(objectResult.getType(), objectStatus.getType());
assertEquals(objectResult.getStatus(), objectStatus.getStatus());
assertEquals(objectResult.getCount().get(), objectStatus.getCount().get());
assertEquals(objectResult.getTotal().get(), objectStatus.getTotal().get());
}

@Test
public void testGetSummary() {
// Test the basic functionality of getSummary method
assertEquals(objectResult.getSummary(), "schema.name[null]");
}

@Test
public void testEdgeCasesForGetSummary() {
// Test edge cases for getSummary method
objectResult.setSchema(null);
objectResult.setName(null);
objectResult.setType("type");
assertEquals(objectResult.getSummary(), "[null].null[type]");

objectResult.setSchema("schema");
objectResult.setName(null);
objectResult.setType(null);
assertEquals(objectResult.getSummary(), "schema.null[null]");

objectResult.setSchema(null);
objectResult.setName("name");
objectResult.setType(null);
assertEquals(objectResult.getSummary(), "[null].name[null]");
}

@Test(expected = NullPointerException.class)
public void testExceptionForGetSummary() throws JsonProcessingException {
// Test exception for getSummary method
objectResult.setSchema("schema");
objectResult.setName("name");
objectResult.setType("type");
objectResult.setCount(null);
objectResult.getSummary();
}

@Test
public void testExportPaths() throws MalformedURLException {
// Test the basic functionality of exportPaths
List<URL> urls = new ArrayList<URL>();
urls.add(new URL("http://test.com"));
objectResult.setExportPaths(urls);
assertEquals(objectResult.getExportPaths(), urls);
}

@Test(expected = NullPointerException.class)
public void testExceptionForExportPaths() {
// Test exception for exportPaths
objectResult.setExportPaths(null);
}

}