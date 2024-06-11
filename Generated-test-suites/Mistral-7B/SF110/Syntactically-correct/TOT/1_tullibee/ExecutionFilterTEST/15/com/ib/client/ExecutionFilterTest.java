
package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.common.truth.Truth.*;
import static com.google.common.truth.BooleanSubject.expect;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter expectedFilter = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        Assert.assertEquals(expectedFilter, filter);
    }

    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = filter1;
        expect(filter1).to.be.equalTo(filter2);
    }

    @Test
    public void testEqualsDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        expect(filter1).to.not.be.equalTo(filter2);
    }

    @Test
    public void testEqualsDifferentProperties() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "TestAccount", "2022-01-02", "AAPL", "STK", "SMART", "BUY");
        expect(filter1).to.not.be.equalTo(filter2);

        ExecutionFilter filter3 = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "SELL");
        expect(filter1).to.not.be.equalTo(filter3);

        ExecutionFilter filter4 = new ExecutionFilter(1, null, "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        expect(filter1).to.not.be.equalTo(filter4);

        ExecutionFilter filter5 = new ExecutionFilter(1, "TestAccount", null, "AAPL", "STK", "SMART", "BUY");
        expect(filter1).to.not.be.equalTo(filter5);
    }

    @Test
    public void testEqualsNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "TestAccount", "2022-01-01", "AAPL", "STK", "SMART", "BUY");
        expect(filter).to.not.be.equalTo(null);
    }
}

class Truth {
    // Empty for now
}
