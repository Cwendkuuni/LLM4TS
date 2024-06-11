package org.javex;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;
    private List externalFunctions;
    private static Map<String, FunctionExpression> reservedFunctions;

    @Before
    public void setUp() {
        expression = new Expression("(1 + 2) * 3 + 4");
        externalFunctions = new ArrayList<>();
        reservedFunctions = new HashMap<>();
        Expression.putFunction(new AndFunction(), reservedFunctions);
        Expression.putFunction(new BetweenFunction(), reservedFunctions);
        Expression.putFunction(new ContainsFunction(), reservedFunctions);
        Expression.putFunction(new EndsWithFunction(), reservedFunctions);
        Expression.putFunction(new EqualsFunction(), reservedFunctions);
        Expression.putFunction(new GreaterEqThanFunction(), reservedFunctions);
        Expression.putFunction(new GreaterThanFunction(), reservedFunctions);
        Expression.putFunction(new IsEmptyFunction(), reservedFunctions);
        Expression.putFunction(new IsFunction(), reservedFunctions);
        Expression.putFunction(new IsNullFunction(), reservedFunctions);
        Expression.putFunction(new LessEqThanFunction(), reservedFunctions);
        Expression.putFunction(new LessThanFunction(), reservedFunctions);
        Expression.putFunction(new NotEqualsFunction(), reservedFunctions);
        Expression.putFunction(new NotFunction(), reservedFunctions);
        Expression.putFunction(new NotCharFunction(), reservedFunctions);
        Expression.putFunction(new OrFunction(), reservedFunctions);
        Expression.putFunction(new StartsWithFunction(), reservedFunctions);
        Expression.putFunction(new MatchesFunction(), reservedFunctions);
        Expression.putFunction(new InListFunction(), reservedFunctions);
        Expression.putFunction(new AbsFunction(), reservedFunctions);
        Expression.putFunction(new DivideFunction(), reservedFunctions);
        Expression.putFunction(new MultiplyFunction(), reservedFunctions);
        Expression.putFunction(new PlusFunction(), reservedFunctions);
        Expression.putFunction(new SqrtFunction(), reservedFunctions);
        Expression.putFunction(new SubstractFunction(), reservedFunctions);
        Expression.putFunction(new PowerFunction(), reservedFunctions);
        Expression.putFunction(new PowerCharFunction(), reservedFunctions);
        Expression.putFunction(new DateAddFunction(), reservedFunctions);
        Expression.putFunction(new DateDiffFunction(), reservedFunctions);
        Expression.putFunction(new LengthFunction(), reservedFunctions);
        Expression.putFunction(new SubStringFunction(), reservedFunctions);
        Expression.putFunction(new ToLowerFunction(), reservedFunctions);
        Expression.putFunction(new ToUpperFunction(), reservedFunctions);
        expression.setExternalFunctions(externalFunctions);
    }

    @Test
    public void testGetExternalFunctions() {
        assertEquals(externalFunctions, expression.getExternalFunctions());
    }

    @Test
    public void testExpressionConstructor() {
        assertEquals("(1 + 2) * 3 + 4", expression.getExpression());
        assertEquals(false, expression.isPrepared());
        assertNull(expression.getStack());
        assertTrue(expression.isFunction("*"));
        assertTrue(expression.isFunction("+"));
        assertTrue(expression.isFunction("/"));
        assertTrue(expression.isFunction("("));
        assertTrue(expression.isFunction(")"));
        assertFalse(expression.isFunction("!"));
        assertFalse(expression.isFunction("x"));
    }

    @Test
    public void testDoEvaluationStack() {
        expression.doEvaluationStack();
        final Stack<String> stack = expression.getStack();
        assertFalse(stack.isEmpty());
        for (String s : Arrays.asList("1", "2", "+", "3", "*", "4")) {
            assertTrue(stack.contains(s));
        }
    }

    @Test
    public void testPutFunction() {
        FunctionExpression function1 = new AndFunction();
        FunctionExpression function2 = new BetweenFunction();
        Expression.putFunction(function1, reservedFunctions);
        Expression.putFunction(function2, reservedFunctions);
        FunctionExpression getFunction1 = Expression.getReservedFunctions("and");
        FunctionExpression getFunction2 = Expression.getReservedFunctions("between");
        assertSame(function1, getFunction1);
        assertSame(function2, getFunction2);
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("between"));
        assertTrue(expression.isReservedFunction("or"));
        assertFalse(expression.isReservedFunction("x"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));
        assertFalse(expression.isLogicalFunction("x"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("+"));
        assertTrue(expression.isMathFunction("-"));
        assertTrue(expression.isMathFunction("/"));
        assertTrue(expression.isMathFunction("*"));
        assertTrue(expression.isMathFunction("sqrt"));
        assertTrue(expression.isMathFunction("abs"));
        assertTrue(expression.isMathFunction("pow"));
        assertFalse(expression.isMathFunction("x"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateadd"));
        assertTrue(expression.isDateFunction("datediff"));
        assertFalse(expression.isDateFunction("x"));
    }

    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("length"));
        assertTrue(expression.isStringFunction("substr"));
        assertTrue(expression.isStringFunction("tolower"));
        assertTrue(expression.isStringFunction("toupper"));
        assertFalse(expression.isStringFunction("x"));
    }

    @Test
    public void testGetFunction() {
        FunctionExpression function1 = new AndFunction();
        FunctionExpression function2 = new BetweenFunction();
        Expression.putFunction(function1, reservedFunctions);
        Expression.putFunction(function2, reservedFunctions);
        FunctionExpression getFunction1 = expression.getFunction("and");
        FunctionExpression getFunction2 = expression.getFunction("between");
        assertSame(function1, getFunction1);
        assertSame(function2, getFunction2);
        FunctionExpression getFunction3 = expression.getFunction("x");
        assertNull(getFunction3);
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("+"));
        assertTrue(expression.isFunction("-"));
        assertTrue(expression.isFunction("*"));
        assertTrue(expression.isFunction("/"));
        assertTrue(expression.isFunction("and"));
        assertTrue(expression.isFunction("or"));
        assertTrue(expression.isFunction("x")); // assuming "x" is a valid function symbol
        assertFalse(expression.isFunction("!"));
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.isPrepared());
        assertEquals(expression.getStackCopy(), expression.getStack());
    }

    @Test(expected = Exception.class)
    public void testEvaluateException() throws Exception {
        expression.evaluate();
        fail("An exception should be thrown when evaluation is not prepared");
    }

    @Test
    public void testEvaluate() throws Exception {
        expression.prepare();
        assertEquals(13.0, expression.evaluate(), 0.0);
    }

    @Test
    public void testTokenize() {
        expression = new Expression("(1 + 2) * (3 + 4)");
        expression.tokenize();
        final Stack<String> stack = expression.getStack();
        assertFalse(stack.isEmpty());
        for (String s : Arrays.asList("1", "2", "+", "3", "+", "4", "*", " ")) {
            assertTrue(stack.contains(s));
        }
    }

    @Test
    public void testGetStackCopy() {
        Stack<String> stack = expression.getStack();
        Stack<String> copy = expression.getStackCopy();
        assertArrayEquals(stack.toArray(new String[0]), copy.toArray(new String[0]));
    }
}