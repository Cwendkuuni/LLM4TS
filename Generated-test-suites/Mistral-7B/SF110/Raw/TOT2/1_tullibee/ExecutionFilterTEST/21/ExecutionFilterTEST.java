import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
// Test Start
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);
    }
// Test End
}