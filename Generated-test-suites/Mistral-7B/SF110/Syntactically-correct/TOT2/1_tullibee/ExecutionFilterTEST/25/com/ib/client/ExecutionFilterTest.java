import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    @Test
    public void testConstructorNoArgs() {
        ExecutionFilter filter = new ExecutionFilter();
        ExecutionFilter filter1 = new ExecutionFilter();
        assertNotSame(filter, filter1); // Will be false since objects are not equal
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);
    }
}