import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertEquals(0, filter.m_clientId);
        Assert.assertNull(filter.m_acctCode);
        Assert.assertNull(filter.m_time);
        Assert.assertNull(filter.m_symbol);
        Assert.assertNull(filter.m_secType);
        Assert.assertNull(filter.m_exchange);
        Assert.assertNull(filter.m_side);
    }
}