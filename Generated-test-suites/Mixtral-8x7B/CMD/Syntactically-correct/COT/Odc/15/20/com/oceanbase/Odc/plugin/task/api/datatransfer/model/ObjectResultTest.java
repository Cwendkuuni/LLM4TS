import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class ObjectResultTest {

    private ObjectResult or;

    @Before
    public void setup() {
        or = new ObjectResult("schema", "name", "type");
        or.setCount(new AtomicLong(10));
        or.setTotal(new AtomicLong(20));
        or.setStatus(Status.AVAILABLE);
    }

    @Test
    public void testConstructor() {
        ObjectResult or2 = new ObjectResult("schema2", "name2", "type2");
        assertEquals("schema2", or2.getSchema());
        assertEquals("name2", or2.getName());
        assertEquals("type2", or2.getType());
        assertNull(or2.getCount());
        assertNull(or2.getTotal());
        assertNull(or2.getStatus());
    }

    @Test
    public void testOf() {
        ObjectResult or2 = ObjectResult.of(or);
        assertEquals(or.getType(), or2.getType());
        assertEquals(or.getStatus(), or2.getStatus());
        assertEquals(or.getName(), or2.getName());
        assertEquals(or.getSchema(), or2.getSchema());
        assertEquals(or.getCount().get(), or2.getCount().get());
        assertEquals(or.getTotal().get(), or2.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", or.getSummary());
    }

    @Test
    public void testExportPaths() {
        List<URL> list = Arrays.asList(new URL("http://example.com"));
        or.setExportPaths(list);
        assertEquals(list, or.getExportPaths());
    }

    @Test
    public void testEdgeCases() {
        ObjectResult or2 = new ObjectResult(null, "name", "type");
        assertEquals("", or2.getSchema());

        or2 = new ObjectResult("schema", null, "type");
        assertEquals("", or2.getName());

        or2 = new ObjectResult("schema", "name", null);
        assertEquals("", or2.getType());

        or2 = new ObjectResult("", "name", "type");
        assertEquals("", or2.getSchema());

        or2 = new ObjectResult("schema", "", "type");
        assertEquals("", or2.getName());

        or2 = new ObjectResult("schema", "name", "");
        assertEquals("", or2.getType());

        or2 = new ObjectResult("schema", "name", "type");
        or2.setStatus(null);
        assertNull(or2.getStatus());

        or2 = new ObjectResult("schema", "name", "type");
        or2.setCount(new AtomicLong(-1));
        assertEquals(-1, or2.getCount().get());

        or2 = new ObjectResult("schema", "name", "type");
        or2.setTotal(new AtomicLong(-1));
        assertEquals(-1, or2.getTotal().get());
    }

    @Test(expected = NullPointerException.class)
    public void testExportPathsNull() {
        or.setExportPaths(null);
    }

    @Test(expected = NullPointerException.class)
    public void testExportPathsEmpty() {
        or.setExportPaths(Arrays.asList());
    }

    @Test(expected = NullPointerException.class)
    public void testExportPathsInvalid() {
        or.setExportPaths(Arrays.asList("string"));
    }

    @Test
    public void testSetExportPaths() {
        List<URL> list = Arrays.asList(new URL("http://example.com"), new URL("http://example2.com"));
        or.setExportPaths(list);
        assertEquals(list, or.getExportPaths());
    }

    @Test
    public void testGetExportPaths() {
        List<URL> list = Arrays.asList(new URL("http://example.com"));
        or.setExportPaths(list);
        assertEquals(list, or.getExportPaths());
    }

    @Test
    public void testSetTotal() {
        or.setTotal(new AtomicLong(100));
        assertEquals(100, or.getTotal().get());
    }

    @Test
    public void testSetCount() {
        or.setCount(new AtomicLong(50));
        assertEquals(50, or.getCount().get());
    }

    @Test
    public void testSetStatus() {
        or.setStatus(Status.UNAVAILABLE);
        assertEquals(Status.UNAVAILABLE, or.getStatus());
    }

    @Test
    public void testSetType() {
        or.setType("newType");
        assertEquals("newType", or.getType());
    }

    @Test
    public void testSetSchema() {
        or.setSchema("newSchema");
        assertEquals("newSchema", or.getSchema());
    }

    @Test
    public void testSetName() {
        or.setName("newName");
        assertEquals("newName", or.getName());
    }

    @Test
    public void testEquals() {
        ObjectResult or2 = new ObjectResult("schema", "name", "type");
        or2.setCount(or.getCount());
        or2.setTotal(or.getTotal());
        or2.setStatus(or.getStatus());
        assertEquals(or, or2);

        or2.setCount(new AtomicLong(100));
        assertNotEquals(or, or2);

        or2.setTotal(new AtomicLong(100));
        assertNotEquals(or, or2);

        or2.setStatus(Status.UNAVAILABLE);
        assertNotEquals(or, or2);
    }

    @Test
    public void testHashCode() {
        ObjectResult or2 = new ObjectResult("schema", "name", "type");
        or2.setCount(or.getCount());
        or2.setTotal(or.getTotal());
        or2.setStatus(or.getStatus());
        assertEquals(or.hashCode(), or2.hashCode());

        or2.setCount(new AtomicLong(100));
        assertNotEquals(or.hashCode(), or2.hashCode());

        or2.setTotal(new AtomicLong(100));
        assertNotEquals(or.hashCode(), or2.hashCode());

        or2.setStatus(Status.UNAVAILABLE);
        assertNotEquals(or.hashCode(), or2.hashCode());
    }

}

