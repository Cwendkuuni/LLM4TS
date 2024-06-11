package com.ib.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {

	@Test
	public void testExecutionFilterConstructorWithNullClientId() {
		assertThrows(IllegalArgumentException.class, () -> new ExecutionFilter(null, "acctCode", "time", "symbol", "secType", "exchange", "side"));
	}

	@Test
	public void testExecutionFilterConstructorWithValidArguments() {
		int clientId = 1;
		String acctCode = "acctCode";
		String time = LocalDateTime.now().toString();
		String symbol = "symbol";
		String secType = "secType";
		String exchange = "exchange";
		String side = "side";

		ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

		assertEquals(clientId, filter.getClientId());
		assertEquals(acctCode, filter.getAcctCode());
		assertEquals(time, filter.getTime());
		assertEquals(symbol, filter.getSymbol());
		assertEquals(secType, filter.getSecType());
		assertEquals(exchange, filter.getExchange());
		assertEquals(side, filter.getSide());
	}

	@Test
	public void testExecutionFilterConstructorWithEmptyStrings() {
		int clientId = 1;
		String acctCode = "";
		String time = "";
		String symbol = "";
		String secType = "";
		String exchange = "";
		String side = "";

		ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

		assertEquals(clientId, filter.getClientId());
		assertEquals("", filter.getAcctCode());
		assertEquals("", filter.getTime());
		assertEquals("", filter.getSymbol());
		assertEquals("", filter.getSecType());
		assertEquals("", filter.getExchange());
		assertEquals("", filter.getSide());
	}

	@Test
	public void testExecutionFilterConstructorWithNullStrings() {
		int clientId = 1;
		String acctCode = null;
		String time = null;
		String symbol = null;
		String secType = null;
		String exchange = null;
		String side = null;

		assertThrows(NullPointerException.class, () -> new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side));
	}

	@Test
	public void testExecutionFilterEqualsNull() {
		ExecutionFilter filter = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		assertFalse(filter.equals(null));
	}

	@Test
	public void testExecutionFilterEqualsSameObject() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		ExecutionFilter filter2 = filter1;
		assertTrue(filter1.equals(filter2));
	}

	@Test
	public void testExecutionFilterEqualsDifferentObjects() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		ExecutionFilter filter2 = new ExecutionFilter(2, "acctCode2", "time2", "symbol2", "secType2", "exchange2", "side2");
		assertFalse(filter1.equals(filter2));
	}

	@Test
	public void testExecutionFilterEqualsDifferentObjectsWithSameValues() {
		ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		assertTrue(filter1.equals(filter2));
	}

	@Test
	public void testExecutionFilterGetters() {
		ExecutionFilter filter = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");

		assertEquals(1, filter.getClientId());
		assertEquals("acctCode1", filter.getAcctCode());
		assertEquals("time1", filter.getTime());
		assertEquals("symbol1", filter.getSymbol());
		assertEquals("secType1", filter.getSecType());
		assertEquals("exchange1", filter.getExchange());
		assertEquals("side1", filter.getSide());
	}

	@Test
	public void testExecutionFilterHashCode() {
		ExecutionFilter filter = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		int hashCode = filter.hashCode();
		ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
		assertEquals(hashCode, filter2.hashCode());
	}
}