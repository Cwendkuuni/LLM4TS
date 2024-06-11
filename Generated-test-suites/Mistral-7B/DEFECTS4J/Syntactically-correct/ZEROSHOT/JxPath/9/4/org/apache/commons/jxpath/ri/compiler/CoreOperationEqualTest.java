package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationEqual coreOperationEqual;
 private EvalContext context;

 @Before
 public void setUp() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.spy(EvalContext.class);
 coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testComputeValue_whenBothExpressionsAreBooleanTrue_thenResultIsBooleanTrue() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);

 Object result = coreOperationEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValue_whenBothExpressionsAreBooleanFalse_thenResultIsBooleanFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.FALSE);
 Mockito.when(arg2.computeValue(context)).thenReturn(Boolean.FALSE);

 Object result = coreOperationEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenBothExpressionsAreNotBoolean_thenResultIsBooleanFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn("testString");
 Mockito.when(arg2.computeValue(context)).thenReturn("testString2");

 Object result = coreOperationEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsBooleanTrueAndSecondIsNotPresent_thenResultIsBooleanFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
 Mockito.when(arg2.computeValue(context)).thenReturn(null);

 Object result = coreOperationEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsBooleanFalseAndSecondIsNotPresent_thenResultIsBooleanFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(Boolean.FALSE);
 Mockito.when(arg2.computeValue(context)).thenReturn(null);

 Object result = coreOperationEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsNotPresent_thenResultIsBooleanFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(null);
 Mockito.when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);

 Object result = coreOperationEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_whenFirstExpressionIsNotPresent_thenResultIsBooleanFalse() {
 Mockito.when(arg1.computeValue(context)).thenReturn(null);
 Mockito.when(arg2.computeValue(context)).thenReturn(null);

 Object result = coreOperationEqual.computeValue(context);

 assertNull(result);
 }

 @Test
 public void testGetSymbol_thenReturnEqualSymbol() {
 String symbol = coreOperationEqual.getSymbol();

 assertEquals("=", symbol);
 }
}