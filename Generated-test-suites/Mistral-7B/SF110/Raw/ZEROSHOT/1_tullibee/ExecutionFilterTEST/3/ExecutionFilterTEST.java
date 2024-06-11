package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter executionFilter1;
 ExecutionFilter executionFilter2;

 @Test
 public void testExecutionFilterWithNullConstructor() {
 ExecutionFilter constructorNullTest = new ExecutionFilter();
 assertTrue(constructorNullTest.getm_clientId() == 0);
 }

 @Test
 public void testExecutionFilterWithValidParameters() {
 int clientId = 1;
 String acctCode = "TestAcctCode";
 String time = "2022-01-01 12:30:00";
 String symbol = "IBM";
 String secType = "STK";
 String exchange = "SMART";
 String side = "BUY";

 executionFilter1 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 assertEquals(clientId, executionFilter1.getm_clientId());
 assertEquals(acctCode, executionFilter1.getm_acctCode());
 assertEquals(time, executionFilter1.getm_time());
 assertEquals(symbol, executionFilter1.getm_symbol());
 assertEquals(secType, executionFilter1.getm_secType());
 assertEquals(exchange, executionFilter1.getm_exchange());
 assertEquals(side, executionFilter1.getm_side());
 }

 @Test
 public void testExecutionFilterEqualsWithNullObject() {
 assertFalse(executionFilter1.equals(null));
 }

 @Test
 public void testExecutionFilterEqualsWithSameObject() {
 ExecutionFilter executionFilter3 = executionFilter1;
 assertTrue(executionFilter1.equals(executionFilter3));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentObject() {
 int clientId2 = 2;
 String acctCode2 = "TestAcctCode2";
 String time2 = "2022-01-02 12:30:00";
 String symbol2 = "IBM";
 String secType2 = "STK";
 String exchange2 = "SMART";
 String side2 = "SELL";

 executionFilter2 = new ExecutionFilter(clientId2, acctCode2, time2, symbol2, secType2, exchange2, side2);
 assertFalse(executionFilter1.equals(executionFilter2));
 }

 @Test
 public void testExecutionFilterEqualsWithEqualValues() {
 ExecutionFilter executionFilter4 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
 assertTrue(executionFilter1.equals(executionFilter4));
 }

 @Test
 public void testExecutionFilterEqualsWithDifferentValues() {
 int clientId3 = 3;
 String acctCode3 = "TestAcctCode3";
 String time3 = "2022-01-03 12:30:00";
 String symbol3 = "MSFT";
 String secType3 = "OPT";
 String exchange3 = "NASDAQ";
 String side3 = "BUY";

 ExecutionFilter executionFilter5 = new ExecutionFilter(clientId3, acctCode3, time3, symbol3, secType3, exchange3, side3);
 assertFalse(executionFilter1.equals(executionFilter5));
 }

 private int getm_clientId() {
 return executionFilter1.m_clientId;
 }

 private String getm_acctCode() {
 return executionFilter1.m_acctCode;
 }

 private String getm_time() {
 return executionFilter1.m_time;
 }

 private String getm_symbol() {
 return executionFilter1.m_symbol;
 }

 private String getm_secType() {
 return executionFilter1.m_secType;
 }

 private String getm_exchange() {
 return executionFilter1.m_exchange;
 }

 private String getm_side() {
 return executionFilter1.m_side;
 }
}