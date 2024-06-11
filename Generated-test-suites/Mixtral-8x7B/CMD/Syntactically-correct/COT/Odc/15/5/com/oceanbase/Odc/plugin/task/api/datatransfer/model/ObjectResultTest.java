import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectResultTest {

    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type");
        when(objectStatus.getStatus()).thenReturn(Status.AVAILABLE);
        when(objectStatus.getName()).thenReturn("name");
        when(objectStatus.getSchema()).thenReturn("schema");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));
    }

    @Test
    public void testObjectResult() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("type", objectResult.getType());
        assertEquals(Status.AVAILABLE, objectResult.getStatus());
        assertEquals("name", objectResult.getName());
        assertEquals("schema", objectResult.getSchema());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(mock(URL.class));
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testExceptionHandling() {
        ObjectResult objectResult = new ObjectResult("schema", "name", null);
        assertEquals("", objectResult.getType());
        objectResult = new ObjectResult(null, "name", "type");
        assertEquals("", objectResult.getSchema());
        objectResult = new ObjectResult("schema", null, "type");
        assertEquals("", objectResult.getName());
    }

    @Test
    public void testEdgeCases() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        objectResult.getCount().set(-1);
        objectResult.getTotal().set(-1);
        assertTrue(objectResult.getCount().get() < 0);
        assertTrue(objectResult.getTotal().get() < 0);
    }

}