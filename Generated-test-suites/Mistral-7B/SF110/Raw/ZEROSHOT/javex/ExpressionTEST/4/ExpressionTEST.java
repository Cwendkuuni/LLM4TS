package org.javex.Expression;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.javex.functions.*;
import org.javex.utils.JavexUtils;
import java.util.*;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 expression = new Expression("(2 + 3) * (4 and 5)");
 expression.prepare();
 }

 @Test
 public void testIsReservedFunction() {
 FunctionExpression function = Expression.getReservedFunctions("and");
 assertTrue(expression.isReservedFunction("and"));
 assertEquals(function, expression.getFunction("and"));
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression function = new AndFunction();
 assertTrue(expression.isLogicalFunction("and"));
 assertEquals(function, expression.getFunction("and"));
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression function = new AddFunction();
 assertFalse(expression.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression function = new DateAddFunction();
 assertFalse(expression.isDateFunction("and"));
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression function = new ToLowerFunction();
 assertFalse(expression.isStringFunction("and"));
 }

 @Test
 public void testIsFunction() {
 assertTrue(expression.isFunction("and"));
 }

 @Test
 public void testIsExternalFunction() {
 FunctionExpression function = new CustomFunction("myFunction");
 expression.setExternalFunctions(Arrays.asList(function));
 assertTrue(expression.isExternalFunction("myFunction"));
 }

 @Test
 public void testEvaluate() {
 try {
 assertEquals(14, expression.evaluate());
 } catch (Exception e) {
 fail("Evaluation failed: " + e.getMessage());
 }
 }

 @Test
 public void testGetStackCopy() {
 ExpressionStack stackCopy = expression.getStackCopy();
 assertEquals(expression.stack.size(), stackCopy.size());
 for (int i = 0; i < expression.stack.size(); i++) {
 assertEquals(expression.stack.get(i), stackCopy.get(i));
 }
 }
}