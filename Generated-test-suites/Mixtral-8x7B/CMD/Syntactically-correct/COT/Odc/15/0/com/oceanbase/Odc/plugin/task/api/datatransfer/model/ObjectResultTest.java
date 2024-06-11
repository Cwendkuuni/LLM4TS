import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private List<URL> exportPaths;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
        exportPaths = new ArrayList<>();
        try {
            exportPaths.add(new URL("http://example.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
        assertEquals(Status.AVAILABLE, result.getStatus());
    }

    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus("schema", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult result = ObjectResult.of(that);
        assertEquals("schema", result.getSchema());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testSetters() {
        objectResult.setSchema("schema2");
        objectResult.setName("name2");
        objectResult.setType("type2");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));
        objectResult.setStatus(Status.UNAVAILABLE);

        assertEquals("schema2", objectResult.getSchema());
        assertEquals("name2", objectResult.getName());
        assertEquals("type2", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertEquals(Status.UNAVAILABLE, objectResult.getStatus());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSchemaNull() {
        objectResult.setSchema(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameNull() {
        objectResult.setName(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTypeNull() {
        objectResult.setType(null);
    }

    @Test
    public void testSetStatus() {
        objectResult.setStatus(Status.DELETING);
        assertEquals(Status.DELETING, objectResult.getStatus());
    }

    @Test
    public void testHashCodeAndEquals() {
        ObjectResult result1 = new ObjectResult("schema", "name", "type");
        ObjectResult result2 = new ObjectResult("schema", "name", "type");

        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());

        result2.setSchema("schema2");
        assertNotEquals(result1, result2);
        assertEquals(1, Math.abs(result1.hashCode() - result2.hashCode()));

        result2.setSchema("schema");
        result2.setType("type2");
        assertNotEquals(result1, result2);
        assertEquals(1, Math.abs(result1.hashCode() - result2.hashCode()));

        result2.setType("type");
        result2.setName("name2");
        assertNotEquals(result1, result2);
        assertEquals(1, Math.abs(result1.hashCode() - result2.hashCode()));
    }
}
//