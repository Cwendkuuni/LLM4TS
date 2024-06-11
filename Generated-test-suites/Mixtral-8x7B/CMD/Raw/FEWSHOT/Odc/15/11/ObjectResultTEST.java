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
public void testObjectResult() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals("schema", objRes.getSchema());
assertEquals("name", objRes.getName());
assertEquals("type", objRes.getType());
assertEquals(0, objRes.getCount().get());
assertEquals(0, objRes.getTotal().get());
assertEquals(Status.NOT_STARTED, objRes.getStatus());
}

@Test
public void testOf() {
ObjectStatus objSts = mock(ObjectStatus.class);
when(objSts.getType()).thenReturn("type");
when(objSts.getStatus()).thenReturn(Status.COMPLETED);
when(objSts.getName()).thenReturn("name");
when(objSts.getSchema()).thenReturn("schema");
when(objSts.getCount()).thenReturn(new AtomicLong(10));
when(objSts.getTotal()).thenReturn(new AtomicLong(20));

ObjectResult objRes = ObjectResult.of(objSts);
assertEquals("schema", objRes.getSchema());
assertEquals("name", objRes.getName());
assertEquals("type", objRes.getType());
assertEquals(10, objRes.getCount().get());
assertEquals(20, objRes.getTotal().get());
assertEquals(Status.COMPLETED, objRes.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objRes.getSummary());
}

@Test
public void testExportPaths() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
URL url1 = mock(URL.class);
URL url2 = mock(URL.class);
List<URL> exportPaths = Arrays.asList(url1, url2);
objRes.setExportPaths(exportPaths);
assertEquals(exportPaths, objRes.getExportPaths());
}

@Test
public void testJsonIgnore() {
ObjectResult objRes = new ObjectResult("schema", "name", "type");
assertNull(objRes.getExportPaths());
}
}