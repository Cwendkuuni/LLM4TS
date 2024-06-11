import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("schema", "name", Status.AVAILABLE);
        objectResult = ObjectResult.of(objectStatus);
    }

    // Test constructor with parameters
    @Test
    public void testObjectResult() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        assertEquals("schema.name[null]", objectResult.getSummary());
    }

    // Test of method with mocked object
    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus("schema2", "name2", Status.AVAILABLE);
        ObjectResult result = ObjectResult.of(that);
        assertNotEquals(objectResult, result);
        assertEquals(that.getSchema(), result.getSchema());
        assertEquals(that.getName(), result.getName());
        assertEquals(that.getType(), result.getType());
        assertEquals(that.getStatus(), result.getStatus());
    }

    // Test edge case for exportPaths, should be null
    @Test
    public void testExportPaths() {
        assertNull(objectResult.getExportPaths());
    }

    // Test exception handling for setExportPaths method
    @Test(expected = UnsupportedOperationException.class)
    public void testSetExportPaths() {
        List<URL> urls = Arrays.asList(mock(URL.class), mock(URL.class));
        objectResult.setExportPaths(urls);
    }

    // Test exception handling for setExportPaths method with null value
    @Test(expected = NullPointerException.class)
    public void testSetExportPathsWithNull() {
        objectResult.setExportPaths(null);
    }

}