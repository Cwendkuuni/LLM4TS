import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    // 
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertEquals(0, executionFilter.getm_clientId());
    }
    // 
}

// Method 2: Parameterized constructor test case:
// (Insert test case here)

// ...