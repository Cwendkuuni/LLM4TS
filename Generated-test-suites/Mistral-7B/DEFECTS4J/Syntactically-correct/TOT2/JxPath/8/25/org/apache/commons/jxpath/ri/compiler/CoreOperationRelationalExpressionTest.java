package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.core.CoreOperation;
import org.apache.commons.jxpath.ri.core.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.core.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.iter.InitialContext;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testComputeValue() {
        // Test for CoreOperationGreaterThan
        CoreOperation leftExp = new CoreOperationLiteral(Boolean.TRUE);
        CoreOperation rightExp = new CoreOperationLiteral(Boolean.TRUE);
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { leftExp, rightExp });

        EvalContext context = new EvalContext();
        Object result = relationalExpression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        leftExp = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp = new CoreOperationLiteral(Integer.valueOf(5));
        relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { leftExp, rightExp });

        result = relationalExpression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        leftExp = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp = new CoreOperationLiteral(Integer.valueOf(4));
        relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { leftExp, rightExp });

        result = relationalExpression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test for CoreOperationGreaterThanOrEqual
        CoreOperation leftExp2 = new CoreOperationLiteral(Integer.valueOf(5));
        CoreOperation rightExp2 = new CoreOperationLiteral(Integer.valueOf(5));
        CoreOperationRelationalExpression relationalExpression2 = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { leftExp2, rightExp2 });

        result = relationalExpression2.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        leftExp2 = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp2 = new CoreOperationLiteral(Integer.valueOf(6));
        relationalExpression2 = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { leftExp2, rightExp2 });

        result = relationalExpression2.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        leftExp2 = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp2 = new CoreOperationLiteral(Integer.valueOf(5));
        relationalExpression2 = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { leftExp2, rightExp2 });

        result = relationalExpression2.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test for CoreOperationLessThan
        CoreOperation leftExp3 = new CoreOperationLiteral(Integer.valueOf(5));
        CoreOperation rightExp3 = new CoreOperationLiteral(Integer.valueOf(4));
        CoreOperationRelationalExpression relationalExpression3 = new CoreOperationLessThan(new CoreOperation[] { leftExp3, rightExp3 });

        result = relationalExpression3.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        leftExp3 = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp3 = new CoreOperationLiteral(Integer.valueOf(5));
        relationalExpression3 = new CoreOperationLessThan(new CoreOperation[] { leftExp3, rightExp3 });

        result = relationalExpression3.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        leftExp3 = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp3 = new CoreOperationLiteral(Integer.valueOf(5));
        relationalExpression3 = new CoreOperationLessThan(new CoreOperation[] { leftExp3, rightExp3 });

        result = relationalExpression3.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test for CoreOperationLessThanOrEqual
        CoreOperation leftExp4 = new CoreOperationLiteral(Integer.valueOf(5));
        CoreOperation rightExp4 = new CoreOperationLiteral(Integer.valueOf(5));
        CoreOperationRelationalExpression relationalExpression4 = new CoreOperationLessThanOrEqual(new CoreOperation[] { leftExp4, rightExp4 });

        result = relationalExpression4.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        leftExp4 = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp4 = new CoreOperationLiteral(Integer.valueOf(4));
        relationalExpression4 = new CoreOperationLessThanOrEqual(new CoreOperation[] { leftExp4, rightExp4 });

        result = relationalExpression4.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        leftExp4 = new CoreOperationLiteral(Integer.valueOf(5));
        rightExp4 = new CoreOperationLiteral(Integer.valueOf(5));
        relationalExpression4 = new CoreOperationLessThanOrEqual(new CoreOperation[] { leftExp4, rightExp4 });

        result = relationalExpression4.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[0]);
        assertEquals(3, relationalExpression.getPrecedence());

        CoreOperationRelationalExpression relationalExpression2 = new CoreOperationLessThan(new CoreOperation[0]);
        assertEquals(3, relationalExpression2.getPrecedence());

        CoreOperationRelationalExpression relationalExpression3 = new CoreOperationGreaterThanOrEqual(new CoreOperation[0]);
        assertEquals(3, relationalExpression3.getPrecedence());

        CoreOperationRelationalExpression relationalExpression4 = new CoreOperationLessThanOrEqual(new CoreOperation[0]);
        assertEquals(3, relationalExpression4.getPrecedence());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[0]);
        assertTrue(relationalExpression.evaluateCompare(1) == -1);
        assertTrue(relationalExpression.evaluateCompare(0) == 0);
        assertTrue(relationalExpression.evaluateCompare(-1) == 1);
    }
}