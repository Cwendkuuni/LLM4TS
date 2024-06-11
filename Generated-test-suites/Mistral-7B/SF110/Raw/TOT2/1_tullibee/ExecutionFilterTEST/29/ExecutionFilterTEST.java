import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertEquals(0, executionFilter.getm_clientId());
    }
}