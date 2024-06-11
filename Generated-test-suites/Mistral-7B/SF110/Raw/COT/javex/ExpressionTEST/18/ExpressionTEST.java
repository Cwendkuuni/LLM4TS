import org.javex.Expression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        String expression = "1 + 2 * (3 + 4)";
        Expression exp = new Expression(expression);
        Assert.assertNotNull(exp);
        Assert.assertEquals(expression, exp.getExpression());
        Assert.assertFalse(exp.isPrepared());
    }

    @Test
    public void testGetExternalFunctions() {
        Expression exp = new Expression("");
        List<Expression> externalFunctions = Arrays.asList(new Expression(""), new Expression(""));
        exp.setExternalFunctions(externalFunctions);
        List<Expression> actual = exp.getExternalFunctions();
        Assert.assertEquals(externalFunctions, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoEvaluationStackWithEmptyStack() {
        Expression exp = new Expression("");
        exp.doEvaluationStack();
    }

    @Test
    public void testDoEvaluationStackHappyPath() {
        String expression = "1 + 2 * (3 + 4)";
        Expression exp = new Expression(expression);
        Stack<String> operands = new Stack<>();
        ExpressionStack values = new ExpressionStack();
        exp.doEvaluationStack();
        Assert.assertArrayEquals(new String[]{"1", "2", "*", "(", "3", "+", "4", ")"}, exp.stack.toArray(new String[0]));
        Assert.assertArrayEquals(new Object[]{1.0, 2.0, "*", "(", 3.0, "+", 4.0, ")"}, values.toArray());
    }

    @Test
    public void testPutFunction() {
        Map<FunctionExpression, FunctionExpression> map = new TreeMap<>();
        FunctionExpression function1 = new FunctionExpression("function1", FunctionExpression.LOGICAL);
        Expression.putFunction(function1, map);
        FunctionExpression actual = map.get(function1);
        Assert.assertEquals(function1, actual);
    }

    @Test
    public void testIsReservedFunction() {
        Expression exp = new Expression("");
        Assert.assertFalse(exp.isReservedFunction(""));
        Assert.assertTrue(exp.isReservedFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression exp = new Expression("");
        Assert.assertFalse(exp.isLogicalFunction(""));
        Assert.assertTrue(exp.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression exp = new Expression("");
        Assert.assertFalse(exp.isMathFunction(""));
        Assert.assertTrue(exp.isMathFunction("abs"));
    }

    @Test
    public void testIsDateFunction() {
        Expression exp = new Expression("");
        Assert.assertFalse(exp.isDateFunction(""));
        Assert.assertTrue(exp.isDateFunction("dateadd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression exp = new Expression("");
        Assert.assertFalse(exp.isStringFunction(""));
        Assert.assertTrue(exp.isStringFunction("toupper"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression exp = new Expression("");
        FunctionExpression function1 = new FunctionExpression("function1", FunctionExpression.LOGICAL);
        FunctionExpression function2 = new FunctionExpression("function2", FunctionExpression.MATH);
        exp.setExternalFunctions(Arrays.asList(function1, function2));
        FunctionExpression actualFunction1 = exp.getExternalFunction("function1");
        FunctionExpression actualFunction2 = exp.getExternalFunction("function2");
        Assert.assertEquals(function1, actualFunction1);
        Assert.assertEquals(function2, actualFunction2);
    }

    @Test
    public void testPrepare() throws Exception {
        Expression exp = new Expression("1 + 2 * (3 + 4)");
        exp.prepare();
        Assert.assertTrue(exp.isPrepared());
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithEmptyStack() throws Exception {
        Expression exp = new Expression("");
        exp.evaluate();
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithInvalidExpression() throws Exception {
        Expression exp = new Expression("1 + 2 * (3 + 4) +");
        exp.prepare();
        try {
            exp.evaluate();
        } catch (Exception e) {
            Assert.assertEquals("Error in expression evaluation: Unexpected symbol + at index 11", e.getMessage());
        }
    }

    @Test
    public void testEvaluateHappyPath() throws Exception {
        Expression exp = new Expression("1 + 2 * (3 + 4)");
        exp.prepare();
        Double result = (Double) exp.evaluate();
        Assert.assertEquals(7.0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenizeWithEmptyString() {
        Expression exp = new Expression("");
        exp.tokenize();
    }

    @Test
    public void testTokenizeHappyPath() {
        Expression exp = new Expression("1 + 2 * (3 + 4)");
        exp.tokenize();
        Stack<String> expectedStack = new Stack<>();
        expectedStack.push("1");
        expectedStack.push("+");
        expectedStack.push("2");
        expectedStack.push("*");
        expectedStack.push("(");
        expectedStack.push("3");
        expectedStack.push("+");
        expectedStack.push("4");
        expectedStack.push(")");
        Assert.assertEquals(expectedStack, exp.stack);
    }

    @Test
    public void testGetStackCopy() {
        Expression exp = new Expression("1 + 2 * (3 + 4)");
        exp.prepare();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.addAll(Arrays.asList("1", "+", "2", "*", "(", "3", "+", "4", ")", "7.0"));
        ExpressionStack actualStack = exp.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
    }
}