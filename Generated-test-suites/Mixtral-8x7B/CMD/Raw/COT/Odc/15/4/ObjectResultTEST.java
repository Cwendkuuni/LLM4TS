import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private List<URL> exportPaths;

    @Before
    public void setUp() {
        exportPaths = new ArrayList<>();

        try {
            exportPaths.add(new URL("https://example.com/obj1"));
            exportPaths.add(new URL("https://example.com/obj2"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setCount(new AtomicLong(0));
        objectResult.setTotal(new AtomicLong(100));
        objectResult.setStatus(Status.COMPLETED);
    }

    @Test
    public void testObjectResult() {
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(100, objectResult.getTotal().get());
        assertEquals(Status.COMPLETED, objectResult.getStatus());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus();
        that.setSchema("schema");
        that.setName("name");
        that.setType("type");
        that.setCount(new AtomicLong(100));
        that.setTotal(new AtomicLong(200));
        that.setStatus(Status.FAILED);

        ObjectResult result = ObjectResult.of(that);

        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(100, result.getCount().get());
        assertEquals(200, result.getTotal().get());
        assertEquals(Status.FAILED, result.getStatus());
    }

    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testJsonIgnore() {
        objectResult.setExportPaths(exportPaths);
        assertNull(objectResult.getExportPaths());
    }
}
//