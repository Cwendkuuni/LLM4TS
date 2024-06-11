import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilter() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "EQUITY", "SMART_ routed", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "EQUITY", "SMART_ routed", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(1, "account2", "2022-01-01 10:10:10", "AAPL", "EQUITY", "SMART_ routed", "BUY");
        ExecutionFilter executionFilter4 = new ExecutionFilter(1, "account1", "2022-01-01 10:10:11", "AAPL", "EQUITY", "SMART_ routed", "BUY");

        // Test equals method with same object
        assertTrue(executionFilter1.equals(executionFilter1));

        // Test equals method with null object
        assertFalse(executionFilter1.equals(null));

        // Test equals method with different class
        assertFalse(executionFilter1.equals(new Object()));

        // Test equals method with different ExecutionFilter objects having same values
        assertTrue(executionFilter1.equals(executionFilter2));

        // Test equals method with different ExecutionFilter objects having different values
        assertFalse(executionFilter1.equals(executionFilter3));

        // Test equals method with different ExecutionFilter objects having same values but different case
        ExecutionFilter executionFilter5 = new ExecutionFilter(1, "ACCOUNT1", "2022-01-01 10:10:10", "AAPL", "EQUITY", "SMART_ routed", "BUY");
        assertTrue(executionFilter1.equals(executionFilter5));

        // Test equals method with different ExecutionFilter objects having same values but different order
        ExecutionFilter executionFilter6 = new ExecutionFilter(1, "2022-01-01 10:10:10", "account1", "AAPL", "EQUITY", "SMART_ routed", "BUY");
        assertTrue(executionFilter1.equals(executionFilter6));

        // Test equals method with different ExecutionFilter objects having different values
        assertFalse(executionFilter1.equals(executionFilter4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterNullValues() {
        new ExecutionFilter(null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecutionFilterEmptyString() {
        new ExecutionFilter(1, "", "", "", "", "", "");
    }

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertEquals(0, executionFilter.m_clientId);
    }

    @Test
    public void testExecutionFilterParameterizedConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "account1", "2022-01-01 10:10:10", "AAPL", "EQUITY", "SMART_ routed", "BUY");
        Assert.assertEquals(1, executionFilter.m_clientId);
        Assert.assertEquals("account1", executionFilter.m_acctCode);
        Assert.assertEquals("2022-01-01 10:10:10", executionFilter.m_time);
        Assert.assertEquals("AAPL", executionFilter.m_symbol);
        Assert.assertEquals("EQUITY", executionFilter.m_secType);
        Assert.assertEquals("SMART_ routed", executionFilter.m_exchange);
        Assert.assertEquals("BUY", executionFilter.m_side);
    }
}