import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

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

    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
        assertEquals(1, filter.m_clientId);
        assertEquals("testAcctCode", filter.m_acctCode);
        assertEquals("testTime", filter.m_time);
        assertEquals("testSymbol", filter.m_symbol);
        assertEquals("testSecType", filter.m_secType);
        assertEquals("testExchange", filter.m_exchange);
        assertEquals("testSide", filter.m_side);
    }

    @Test(expected = NullPointerException.class)
    public void testNullClientId() {
        ExecutionFilter filter = new ExecutionFilter(null, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
    }

    @Test(expected = NullPointerException.class)
    public void testNullAcctCode() {
        ExecutionFilter filter = new ExecutionFilter(1, null, "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTime() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", null, "testSymbol", "testSecType", "testExchange", "testSide");
    }

    @Test(expected = NullPointerException.class)
    public void testNullSymbol() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", null, "testSecType", "testExchange", "testSide");
    }

    @Test(expected = NullPointerException.class)
    public void testNullSecType() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", null, "testExchange", "testSide");
    }

    @Test(expected = NullPointerException.class)
    public void testNullExchange() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", null, "testSide");
    }

    @Test(expected = NullPointerException.class)
    public void testNullSide() {
        ExecutionFilter filter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeClientId() {
        ExecutionFilter filter = new ExecutionFilter(-1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
    }

    @Test
    public void testEmptyStrings() {
        ExecutionFilter filter = new ExecutionFilter(1, "", "", "", "", "", "");
        assertEquals(1, filter.m_clientId);
        assertEquals("", filter.m_acctCode);
        assertEquals("", filter.m_time);
        assertEquals("", filter.m_symbol);
        assertEquals("", filter.m_secType);
        assertEquals("", filter.m_exchange);
        assertEquals("", filter.m_side);
    }
}