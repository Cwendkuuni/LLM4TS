import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Assert;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultCreation() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("schema", "name", "type", new AtomicLong(10), new AtomicLong(20));
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
        Assert.assertEquals(10, objectResult.getCount().get());
        Assert.assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithNullSchema() {
        ObjectResult objectResult = new ObjectResult(null, "name", "type");
        Assert.assertEquals("name[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithNullName() {
        ObjectResult objectResult = new ObjectResult("schema", null, "type");
        Assert.assertEquals("schema.[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithEmptySchema() {
        ObjectResult objectResult = new ObjectResult("", "name", "type");
        Assert.assertEquals("name[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithEmptyName() {
        ObjectResult objectResult = new ObjectResult("schema", "", "type");
        Assert.assertEquals("schema.[][type]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithNullType() {
        ObjectResult objectResult = new ObjectResult("schema", "name", null);
        Assert.assertEquals("schema.name[null]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryWithEmptyType() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "");
        Assert.assertEquals("schema.name[]", objectResult.getSummary());
    }

}