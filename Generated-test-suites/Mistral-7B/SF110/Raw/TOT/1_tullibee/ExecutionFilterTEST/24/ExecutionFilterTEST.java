import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");

        // Test equals with same object
        ExecutionFilter filter3 = filter1;
        assertTrue(filter1.equals(filter3));

        // Test equals with different object having same values
        ExecutionFilter filter4 = new ExecutionFilter(1, "JohnDoe", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        assertTrue(filter1.equals(filter4));

        // Test equals with null
        assertFalse(filter1.equals(null));

        // Test equals with different object having different values
        ExecutionFilter filter5 = new ExecutionFilter(2, "JaneDoe", "2022-01-02", "MSFT", "OPT", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter5));

        // Test equals with different data types
        Object obj = new Object();
        assertFalse(filter1.equals(obj));
    }

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(-1, "TestAccount", "2022-01-01", "MSFT", "STK", "NASDAQ", "SELL");

        // Test with negative clientId
        Assert.assertEquals(-1, filter.m_clientId);

        // Test with invalid account code
        Assert.assertEquals("TestAccount", filter.m_acctCode);

        // Test with invalid time format
        Assert.assertEquals("2022-01-01", filter.m_time);

        // Test with invalid symbol
        Assert.assertEquals("MSFT", filter.m_symbol);

        // Test with invalid secType
        Assert.assertEquals("STK", filter.m_secType);

        // Test with invalid exchange
        Assert.assertEquals("NASDAQ", filter.m_exchange);

        // Test with invalid side
        Assert.assertEquals("SELL", filter.m_side);

        // Test with valid constructor
        ExecutionFilter validFilter = new ExecutionFilter(1, "ValidAccount", "2022-01-02", "GOOG", "STK", "NYSE", "BUY");
        Assert.assertNotEquals(filter, validFilter);
    }
}