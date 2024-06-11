import static org.junit.Assert.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.Test;

public class ObjectResultTest {

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
static class TestObjectStatus extends ObjectStatus {
public TestObjectStatus(String schema, String name, String type, Status status) {
super(name, new AtomicLong(0), new AtomicLong(0), null);
setType(type);
setSchema(schema);
setStatus(status);
}
}

@Test
public void testObjectResultEmptyConstructor() {
ObjectResult or = new ObjectResult();
assertNull(or.getSchema());
assertNull(or.getName());
assertNull(or.getType());
assertEquals(0, or.getCount().get());
assertEquals(0, or.getTotal().get());
assertEquals(Status.CREATED, or.getStatus());
}

@Test
public void testObjectResultFullConstructor() {
List<URL> exportPaths = new ArrayList<>();
ObjectResult or = new ObjectResult("schema", "name", "type", exportPaths);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
assertEquals(0, or.getCount().get());
assertEquals(0, or.getTotal().get());
assertEquals(Status.CREATED, or.getStatus());
assertEquals(exportPaths, or.getExportPaths());
}

@Test
public void testOf() {
ObjectStatus os = new TestObjectStatus("schema", "name", "type", Status.CREATED);
ObjectResult or = ObjectResult.of(os);
assertEquals("schema", or.getSchema());
assertEquals("name", or.getName());
assertEquals("type", or.getType());
assertEquals(0, or.getCount().get());
assertEquals(0, or.getTotal().get());
assertEquals(Status.CREATED, or.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult or = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", or.getSummary());
}

@Test
public void testJsonIgnore() {
ObjectResult or = new ObjectResult();
or.setExportPaths(null);
assertNull(or.getExportPaths());
}
}