package org.javex.test;

import org.junit.Test;
import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.utils.JavexUtils;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testExpressionBasic() {
        Expression expression = new Expression("1 + 1");
        expression.prepare();
        assertEquals(2, expression.evaluate());
    }

    @Test
    public void testExpressionLogicalAnd() {
        Expression expression = new Expression("(1 = 1) and (2 > 1)");
        expression.prepare();
        expression.stack.push("and");
        expression.doEvaluationStack();
        assertTrue(expression.evaluate());
    }

    @Test
    public void testExpressionLogicalOr() {
        Expression expression = new Expression("(1 = 1) or (2 < 1)");
        expression.prepare();
        expression.stack.push("or");
        expression.doEvaluationStack();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testExpressionMathAdd() {
        Expression expression = new Expression("1 + 1");
        expression.prepare();
        expression.doEvaluationStack();
        assertEquals(2, expression.evaluate());
    }

    @Test
    public void testExpressionMathSubtract() {
        Expression expression = new Expression("1 - 1");
        expression.prepare();
        expression.doEvaluationStack();
        assertEquals(0, expression.evaluate());
    }

    @Test
    public void testExpressionDateAdd() {
        Expression expression = new Expression("DateAdd(day, '2022-01-01', 1)");
        expression.prepare();
        expression.doEvaluationStack();
        assertEquals("2022-01-02", expression.stack.get(0));
    }

    @Test
    public void testExpressionDateDiff() {
        Expression expression = new Expression("DateDiff(day, '2022-01-01', '2022-01-02')");
        expression.prepare();
        expression.doEvaluationStack();
        assertEquals(1, expression.evaluate());
    }

    @Test
    public void testExpressionStringLength() {
        Expression expression = new Expression("Length('hello')");
        expression.prepare();
        expression.doEvaluationStack();
        assertEquals(5, expression.evaluate());
    }

    @Test
    public void testExpressionStringSubstring() {
        Expression expression = new Expression("SubString('hello', 1, 3)");
        expression.prepare();
        expression.doEvaluationStack();
        assertEquals("hel", expression.stack.get(0));
    }

    @Test(expected = Exception.class)
    public void testExpressionInvalidSyntax() {
        Expression expression = new Expression("1 + 1 )");
        expression.prepare();
        expression.doEvaluationStack();
        fail("An exception should be thrown due to invalid syntax");
    }
}