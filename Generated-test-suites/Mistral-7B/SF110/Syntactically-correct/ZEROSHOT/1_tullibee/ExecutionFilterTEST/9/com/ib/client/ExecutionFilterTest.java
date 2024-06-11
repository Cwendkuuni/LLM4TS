package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

 ExecutionFilter executionFilter1;
 ExecutionFilter executionFilter2;

 @Test
 public void testExecutionFilterConstruction() {
 // Testing default constructor
 ExecutionFilter defaultConstructor = new ExecutionFilter();
 assertEquals(0, defaultConstructor.getClientId());
 assertNull(defaultConstructor.getAcctCode());
 assertNull(defaultConstructor.getTime());
 assertNull(defaultConstructor.getSymbol());
 assertNull(defaultConstructor.getSecType());
 assertNull(defaultConstructor.getExchange());
 assertNull(defaultConstructor.getSide());

 // Testing parameterized constructor
 executionFilter1 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 assertEquals(1, executionFilter1.getClientId());
 assertEquals("testAcctCode", executionFilter1.getAcctCode());
 assertEquals("testTime", executionFilter1.getTime());
 assertEquals("testSymbol", executionFilter1.getSymbol());
 assertEquals("testSecType", executionFilter1.getSecType());
 assertEquals("testExchange", executionFilter1.getExchange());
 assertEquals("testSide", executionFilter1.getSide());
 }

 @Test
 public void testExecutionFilterEquals() {
 // Testing equality with null
 assertFalse(executionFilter1.equals(null));

 // Testing equality with self
 assertTrue(executionFilter1.equals(executionFilter1));

 // Testing equality with different ExecutionFilter objects
 executionFilter2 = new ExecutionFilter(2, "testAcctCode2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
 assertFalse(executionFilter1.equals(executionFilter2));

 // Testing equality with same values
 ExecutionFilter equalExecutionFilter = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 assertTrue(executionFilter1.equals(equalExecutionFilter));
 }

 @Test
 public void testExecutionFilterHashCode() {
 // Testing hashcode for equal objects
 ExecutionFilter executionFilter3 = new ExecutionFilter(1, "testAcctCode", "testTime", "testSymbol", "testSecType", "testExchange", "testSide");
 int hashCodeExecutionFilter1 = executionFilter1.hashCode();
 int hashCodeExecutionFilter3 = executionFilter3.hashCode();
 assertEquals(hashCodeExecutionFilter1, hashCodeExecutionFilter3);

 // Testing hashcode for different objects
 ExecutionFilter executionFilter4 = new ExecutionFilter(2, "testAcctCode2", "testTime2", "testSymbol2", "testSecType2", "testExchange2", "testSide2");
 int hashCodeExecutionFilter1 = executionFilter1.hashCode();
 int hashCodeExecutionFilter4 = executionFilter4.hashCode();
 assertNotEquals(hashCodeExecutionFilter1, hashCodeExecutionFilter4);
 }
}