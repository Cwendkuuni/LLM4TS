package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.util.TestUtil;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @SuppressWarnings("unchecked")
    private Expression createExpression(Object leftValue, Object rightValue, boolean expectedResult) {
        Expression leftExpr = TestUtil.createExpression(leftValue);
        Expression rightExpr = TestUtil.createExpression(rightValue);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(leftExpr, rightExpr);
        return expression;
    }

    @org.junit.Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with doubles
        Object leftValue1 = 5.0;
        Object rightValue1 = 10.0;
        boolean expectedResult1 = false;
        Expression expression1 = createExpression(leftValue1, rightValue1, expectedResult1);

        EvalContext context1 = TestUtil.createEvalContext();
        Object result1 = expression1.computeValue(context1);
        assertEquals(expectedResult1, result1);

        Object leftValue2 = 5.0;
        Object rightValue2 = 5.0;
        boolean expectedResult2 = true;
        Expression expression2 = createExpression(leftValue2, rightValue2, expectedResult2);

        EvalContext context2 = TestUtil.createEvalContext();
        Object result2 = expression2.computeValue(context2);
        assertEquals(expectedResult2, result2);

        Object leftValue3 = 10.0;
        Object rightValue3 = 5.0;
        boolean expectedResult3 = false;
        Expression expression3 = createExpression(leftValue3, rightValue3, expectedResult3);

        EvalContext context3 = TestUtil.createEvalContext();
        Object result3 = expression3.computeValue(context3);
        assertEquals(expectedResult3, result3);

        Object leftValue4 = 0.0;
        Object rightValue4 = 0.0;
        boolean expectedResult4 = true;
        Expression expression4 = createExpression(leftValue4, rightValue4, expectedResult4);

        EvalContext context4 = TestUtil.createEvalContext();
        Object result4 = expression4.computeValue(context4);
        assertEquals(expectedResult4, result4);

        Object leftValue5 = 5.5;
        Object rightValue5 = 6.0;
        boolean expectedResult5 = false;
        Expression expression5 = createExpression(leftValue5, rightValue5, expectedResult5);

        EvalContext context5 = TestUtil.createEvalContext();
        Object result5 = expression5.computeValue(context5);
        assertEquals(expectedResult5, result5);

        Object leftValue6 = 5.5;
        Object rightValue6 = 5.5;
        boolean expectedResult6 = true;
        Expression expression6 = createExpression(leftValue6, rightValue6, expectedResult6);

        EvalContext context6 = TestUtil.createEvalContext();
        Object result6 = expression6.computeValue(context6);
        assertEquals(expectedResult6, result6);

        // Test with integers
        Object leftValue7 = 5;
        Object rightValue7 = 10;
        boolean expectedResult7 = false;
        Expression expression7 = createExpression(leftValue7, rightValue7, expectedResult7);

        EvalContext context7 = TestUtil.createEvalContext();
        Object result7 = expression7.computeValue(context7);
        assertEquals(expectedResult7, result7);

        Object leftValue8 = 5;
        Object rightValue8 = 5;
        boolean expectedResult8 = true;
        Expression expression8 = createExpression(leftValue8, rightValue8, expectedResult8);

        EvalContext context8 = TestUtil.createEvalContext();
        Object result8 = expression8.computeValue(context8);
        assertEquals(expectedResult8, result8);

        Object leftValue9 = 10;
        Object rightValue9 = 5;
        boolean expectedResult9 = false;
        Expression expression9 = createExpression(leftValue9, rightValue9, expectedResult9);

        EvalContext context9 = TestUtil.createEvalContext();
        Object result9 = expression9.computeValue(context9);
        assertEquals(expectedResult9, result9);

        Object leftValue10 = 0;
        Object rightValue10 = 0;
        boolean expectedResult10 = true;
        Expression expression10 = createExpression(leftValue10, rightValue10, expectedResult10);

        EvalContext context10 = TestUtil.createEvalContext();
        Object result10 = expression10.computeValue(context10);
        assertEquals(expectedResult10, result10);

        Object leftValue11 = 5;
        Object rightValue11 = 5;
        boolean expectedResult11 = true;
        Expression expression11 = createExpression(leftValue11, rightValue11, expectedResult11);

        EvalContext context11 = TestUtil.createEvalContext();
        Object result11 = expression11.computeValue(context11);
        assertEquals(expectedResult11, result11);

        Object leftValue12 = 5;
        Object rightValue12 = 4;
        boolean expectedResult12 = true;
        Expression expression12 = createExpression(leftValue12, rightValue12, expectedResult12);

        EvalContext context12 = TestUtil.createEvalContext();
        Object result12 = expression12.computeValue(context12);
        assertEquals(expectedResult12, result12);

        // Test with empty InfoSet
        Expression emptyExpression = TestUtil.createEmptyInfoSetExpression();
        CoreOperationLessThanOrEqual expressionEmpty = new CoreOperationLessThanOrEqual(emptyExpression, emptyExpression);

        EvalContext contextEmpty = TestUtil.createEvalContext();
        Object resultEmpty = expressionEmpty.computeValue(contextEmpty);
        assertNull(resultEmpty);
    }

    @org.junit.Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);
        String expectedSymbol = "<=";
        String symbol = expression.getSymbol();
        assertEquals(expectedSymbol, symbol);
    }
}