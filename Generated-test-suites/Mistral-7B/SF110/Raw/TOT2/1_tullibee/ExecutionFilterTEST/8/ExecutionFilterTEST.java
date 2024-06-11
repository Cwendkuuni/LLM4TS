import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterWithAllArguments() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAccount", "2022-10-01", "AAPL", "STK", "IDEALPRO", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("testAccount", filter.m_acctCode);
        assertEquals("2022-10-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("IDEALPRO", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
}