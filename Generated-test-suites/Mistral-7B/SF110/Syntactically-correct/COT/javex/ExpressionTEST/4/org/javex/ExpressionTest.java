package org.javex.test;

import org.javex.Expression;
import org.javex.functions.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

// Test cases for each method go here
// ...

@Test (expected = IllegalArgumentException.class)
public void testDoEvaluationStackWithEmptyExpression() {
// Test the doEvaluationStack method with an empty expression
Expression e = new Expression("");
try {
e.doEvaluationStack();
fail("Expected an IllegalArgumentException");
} catch (IllegalArgumentException e1) {
// Expected exception
}
}

@Test (expected = IllegalArgumentException.class)
public void testDoEvaluationStackWithInvalidExpression() {
// Test the doEvaluationStack method with an invalid expression (contains invalid symbols)
Expression e = new Expression("test + 1");
try {
e.doEvaluationStack();
fail("Expected an IllegalArgumentException");
} catch (IllegalArgumentException e1) {
// Expected exception
}
}

@Test (expected = IllegalStateException.class)
public void testEvaluateWithoutPrepare() throws Exception {
// Test the evaluate method without preparing the expression
Expression e = new Expression("1 + 2");
try {
Object result = e.evaluate();
fail("Expected an IllegalStateException");
} catch (IllegalStateException e1) {
// Expected exception
}
}

@Test
public void testEvaluateWithPreparedExpression() throws Exception {
// Test the evaluate method with a prepared expression
Expression e = new Expression("1 + 2");
e.prepare();
assertEquals(3, e.evaluate());
}

@Test (expected = NullPointerException.class)
public void testGetFunctionWithNullSymbol() {
// Test the getFunction method with a null String
Expression e = new Expression("test");
assertNull(e.getFunction(null));
}

@Test (expected = IllegalArgumentException.class)
public void testGetFunctionWithInvalidSymbol() {
// Test the getFunction method with an invalid symbol
Expression e = new Expression("test");
assertNull(e.getFunction("test"));
}

@Test (expected = IllegalArgumentException.class)
public void testGetExternalFunctionWithNullSymbol() {
// Test the getExternalFunction method with a null String
Expression e = new Expression("test");
assertNull(e.getExternalFunction(null));
}

@Test (expected = IllegalArgumentException.class)
public void testGetExternalFunctionWithInvalidSymbol() {
// Test the getExternalFunction method with an invalid symbol
Expression e = new Expression("test");
assertNull(e.getExternalFunction("test"));
}

@Test
public void testIsFunctionWithVariadicFunctionSymbol() {
// Test the isFunction method with a variadic function symbol (e.g., dateadd, concat)
FunctionExpression variadicFunction = new DateFunctionExpression("dateadd");
Expression e = new Expression("dateadd(day, '2022-01-01', '2022-01-02', '3')");
assertTrue(e.isFunction(variadicFunction.getSymbol()));
}
}