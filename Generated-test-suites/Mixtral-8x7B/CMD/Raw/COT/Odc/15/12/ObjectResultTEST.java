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

public class ObjectResultTest {

    private ObjectResult or;

    @Before
    public void setUp() {
        or = new ObjectResult("schema", "name", "type");
        or.setCount(new AtomicLong(5));
        or.setTotal(new AtomicLong(10));
        or.setStatus(Status.COMPLETED);
    }

    // Test for constructor ObjectResult(String schema, String name, String type)
    @Test
    public void testObjectResultConstructor() {
        assertEquals("schema", or.getSchema());
        assertEquals("name", or.getName());
        assertEquals("type", or.getType());
        assertEquals(0, or.getCount().get());
        assertEquals(0, or.getTotal().get());
        assertEquals(Status.CREATED, or.getStatus());
    }

    // Test for of(ObjectStatus that)
    @Test
    public void testOfObjectStatus() {
        ObjectStatus os = new ObjectStatus("schema", "name", new AtomicLong(5), new AtomicLong(10), Status.COMPLETED);
        ObjectResult or2 = ObjectResult.of(os);
        assertEquals("schema", or2.getSchema());
        assertEquals("name", or2.getName());
        assertEquals("type", or2.getType());
        assertEquals(5, or2.getCount().get());
        assertEquals(10, or2.getTotal().get());
        assertEquals(Status.COMPLETED, or2.getStatus());
    }

    // Test for @JsonIgnore getSummary()
    @Test
    public void testGetSummary() {
        assertEquals("schema.name[type]", or.getSummary());
    }

    // Edge case: Test for constructor ObjectResult(String schema, String name, String type) with null parameters
    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorEdgeCase() {
        new ObjectResult(null, "name", "type");
    }

    // Edge case: Test for of(ObjectStatus that) with null parameter
    @Test(expected = NullPointerException.class)
    public void testOfObjectStatusEdgeCase() {
        ObjectResult.of(null);
    }

    // Exception handling: Test for getSummary() with @JsonIgnore
    @Test
    public void testGetSummaryException() {
        or.setSchema(null);
        or.setName(null);
        or.setType(null);
        assertEquals("null[null]", or.getSummary());
    }
}

//