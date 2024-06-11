package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        // Test constructor
        Expression[] args1 = new Expression[2];
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(2);
        args1[0] = arg1;
        args1[1] = arg2;
        CoreOperationRelationalExpression coreOpRelationalExpression1 = new CoreOperationRelationalExpression(args1);
        assertEquals("Unexpected class for CoreOperationRelationalExpression1", CoreOperationRelationalExpression.class, coreOpRelationalExpression1.getClass());

        // Test getPrecedence()
        assertEquals("Unexpected precedence for CoreOperationRelationalExpression1", 3, coreOpRelationalExpression1.getPrecedence());

        // Test isSymmetric()
        assertFalse("CoreOperationRelationalExpression1 should not be symmetric", coreOpRelationalExpression1.isSymmetric());

        // Test constructor
        Expression[] args2 = new Expression[2];
        Expression arg3 = new CoreNumberExpression(2);
        Expression arg4 = new CoreNumberExpression(1);
        args2[0] = arg4;
        args2[1] = arg3;
        CoreOperationRelationalExpression coreOpRelationalExpression2 = new CoreOperationRelationalExpression(args2);
        assertEquals("Unexpected class for CoreOperationRelationalExpression2", CoreOperationRelationalExpression.class, coreOpRelationalExpression2.getClass());

        // Test getPrecedence()
        assertEquals("Unexpected precedence for CoreOperationRelationalExpression2", 3, coreOpRelationalExpression2.getPrecedence());

        // Test isSymmetric()
        assertFalse("CoreOperationRelationalExpression2 should not be symmetric", coreOpRelationalExpression2.isSymmetric());

        // Test equals()
        CoreOperationRelationalExpression coreOpRelationalExpression3 = new CoreOperationRelationalExpression(args1);
        assertTrue("CoreOperationRelationalExpression1 not equal to CoreOperationRelationalExpression3", coreOpRelationalExpression1.equals(coreOpRelationalExpression3));
        CoreOperationRelationalExpression coreOpRelationalExpression4 = new CoreOperationRelationalExpression(args2);
        assertFalse("CoreOperationRelationalExpression1 equal to CoreOperationRelationalExpression4", coreOpRelationalExpression1.equals(coreOpRelationalExpression4));

        // Test hashCode()
        int hashCode1 = coreOpRelationalExpression1.hashCode();
        CoreOperationRelationalExpression coreOpRelationalExpression5 = new CoreOperationRelationalExpression(args1);
        int hashCode2 = coreOpRelationalExpression5.hashCode();
        assertEquals("Unexpected hashCode for CoreOperationRelationalExpression1", hashCode1, hashCode2);

        // Test evaluate()
        CoreExpressionContext expressionContext = new CoreExpressionContext();
        Object result1 = coreOpRelationalExpression1.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression1.evaluate()", result1 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression1.evaluate()", Boolean.FALSE, result1);

        expressionContext.setVariable("a", arg1.evaluate(expressionContext));
        expressionContext.setVariable("b", arg2.evaluate(expressionContext));
        Object result2 = coreOpRelationalExpression1.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression1.evaluate()", result2 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression1.evaluate()", Boolean.FALSE, result2);

        CoreOperationRelationalExpression coreOpRelationalExpression6 = new CoreOperationRelationalExpression(args2);
        Object result3 = coreOpRelationalExpression6.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression6.evaluate()", result3 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression6.evaluate()", Boolean.TRUE, result3);

        // Test greater than operation (">")
        CoreOperationRelationalExpression coreOpRelationalExpression7 = new CoreOperationGreaterThanExpression(args1);
        Object result4 = coreOpRelationalExpression7.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression7.evaluate()", result4 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression7.evaluate()", Boolean.FALSE, result4);

        expressionContext.setVariable("a", arg1.evaluate(expressionContext));
        expressionContext.setVariable("b", arg2.evaluate(expressionContext));
        Object result5 = coreOpRelationalExpression7.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression7.evaluate()", result5 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression7.evaluate()", Boolean.FALSE, result5);

        CoreOperationRelationalExpression coreOpRelationalExpression8 = new CoreOperationGreaterThanOrEqualExpression(args2);
        Object result6 = coreOpRelationalExpression8.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression8.evaluate()", result6 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression8.evaluate()", Boolean.TRUE, result6);

        // Test less than operation ("<")
        CoreOperationRelationalExpression coreOpRelationalExpression9 = new CoreOperationLessThanExpression(args1);
        Object result7 = coreOpRelationalExpression9.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression9.evaluate()", result7 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression9.evaluate()", Boolean.TRUE, result7);

        expressionContext.setVariable("a", arg1.evaluate(expressionContext));
        expressionContext.setVariable("b", arg2.evaluate(expressionContext));
        Object result8 = coreOpRelationalExpression9.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression9.evaluate()", result8 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression9.evaluate()", Boolean.FALSE, result8);

        CoreOperationRelationalExpression coreOpRelationalExpression10 = new CoreOperationLessThanOrEqualExpression(args2);
        Object result9 = coreOpRelationalExpression10.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression10.evaluate()", result9 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression10.evaluate()", Boolean.TRUE, result9);

        // Test less than or equal operation ("<=")
        CoreOperationRelationalExpression coreOpRelationalExpression11 = new CoreOperationLessThanOrEqualExpression(args1);
        Object result10 = coreOpRelationalExpression11.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression11.evaluate()", result10 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression11.evaluate()", Boolean.TRUE, result10);

        expressionContext.setVariable("a", arg1.evaluate(expressionContext));
        expressionContext.setVariable("b", arg2.evaluate(expressionContext));
        Object result11 = coreOpRelationalExpression11.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression11.evaluate()", result11 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression11.evaluate()", Boolean.FALSE, result11);

        CoreOperationRelationalExpression coreOpRelationalExpression12 = new CoreOperationGreaterThanOrEqualExpression(args2);
        Object result12 = coreOpRelationalExpression12.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression12.evaluate()", result12 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression12.evaluate()", Boolean.TRUE, result12);

        // Test greater than or equal operation (">=")
        CoreOperationRelationalExpression coreOpRelationalExpression13 = new CoreOperationGreaterThanOrEqualExpression(args1);
        Object result13 = coreOpRelationalExpression13.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression13.evaluate()", result13 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression13.evaluate()", Boolean.FALSE, result13);

        expressionContext.setVariable("a", arg1.evaluate(expressionContext));
        expressionContext.setVariable("b", arg2.evaluate(expressionContext));
        Object result14 = coreOpRelationalExpression13.evaluate(expressionContext);
        assertTrue("Unexpected result for CoreOperationRelationalExpression13.evaluate()", result14 instanceof Boolean);
        assertEquals("Unexpected result for CoreOperationRelationalExpression13.evaluate()", Boolean.TRUE, result14);

    }

}