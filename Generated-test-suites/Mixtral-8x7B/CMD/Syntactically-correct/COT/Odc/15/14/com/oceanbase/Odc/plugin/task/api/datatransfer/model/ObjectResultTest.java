import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema", result.getSchema());
        Assert.assertEquals("name", result.getName());
        Assert.assertEquals("type", result.getType());
    }

    @Test
    public void testOf() {
        ObjectResult that = new ObjectResult("schema", "name", "type");
        ObjectResult result = ObjectResult.of(that);
        Assert.assertEquals("schema", result.getSchema());
        Assert.assertEquals("name", result.getName());
        Assert.assertEquals("type", result.getType());
    }

    @Test
    public void testGetSummary() {
        String expected = "schema.name[type]";
        Assert.assertEquals(expected, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        List<URL> paths = new ArrayList<>();
        try {
            paths.add(new URL("https://test.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objectResult.setExportPaths(paths);
        Assert.assertEquals(paths, objectResult.getExportPaths());
    }

    @Test
    public void testSetStatus() {
        objectResult.setStatus(Status.COMPLETED);
        Assert.assertEquals(Status.COMPLETED, objectResult.getStatus());
    }

    @Test
    public void testSetCount() {
        objectResult.setCount(new AtomicLong(100));
        Assert.assertEquals(new Long(100), objectResult.getCount().get());
    }

    @Test
    public void testSetTotal() {
        objectResult.setTotal(new AtomicLong(200));
        Assert.assertEquals(new Long(200), objectResult.getTotal().get());
    }

    @Test
    public void testEdgeCases() {
        // Test empty schema, name, or type
        ObjectResult emptySchema = new ObjectResult("", "name", "type");
        ObjectResult emptyName = new ObjectResult("schema", "", "type");
        ObjectResult emptyType = new ObjectResult("schema", "name", "");

        Assert.assertEquals("", emptySchema.getSchema());
        Assert.assertEquals("name", emptySchema.getName());
        Assert.assertEquals("type", emptySchema.getType());

        Assert.assertEquals("schema", emptyName.getSchema());
        Assert.assertEquals("", emptyName.getName());
        Assert.assertEquals("type", emptyName.getType());

        Assert.assertEquals("schema", emptyType.getSchema());
        Assert.assertEquals("name", emptyType.getName());
        Assert.assertEquals("", emptyType.getType());

        // Test null schema, name, or type
        ObjectResult nullSchema = new ObjectResult(null, "name", "type");
        ObjectResult nullName = new ObjectResult("schema", null, "type");
        ObjectResult nullType = new ObjectResult("schema", "name", null);

        Assert.assertEquals(null, nullSchema.getSchema());
        Assert.assertEquals("name", nullSchema.getName());
        Assert.assertEquals("type", nullSchema.getType());

        Assert.assertEquals("schema", nullName.getSchema());
        Assert.assertEquals(null, nullName.getName());
        Assert.assertEquals("type", nullName.getType());

        Assert.assertEquals("schema", nullType.getSchema());
        Assert.assertEquals("name", nullType.getName());
        Assert.assertEquals(null, nullType.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testNullUrl() {
        List<URL> paths = new ArrayList<>();
        paths.add(null);
        objectResult.setExportPaths(paths);
    }

}