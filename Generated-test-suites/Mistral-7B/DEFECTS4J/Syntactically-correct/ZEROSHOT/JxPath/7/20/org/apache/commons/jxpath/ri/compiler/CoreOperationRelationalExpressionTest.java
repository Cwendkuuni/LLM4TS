package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionInstance() {
        Expression exp1 = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return true;
            }
        };

        assertNotNull(exp1);
        assertTrue(exp1 instanceof CoreOperationRelationalExpression);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanExpression(new Expression[0]);
        int precedence = exp.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanExpression(new Expression[0]);
        boolean symmetric = exp.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testCoreOperationRelationalExpressionWithArguments() {
        CoreOperation exp1 = new CoreOperationConstantExpression(1);
        CoreOperation exp2 = new CoreOperationConstantExpression(2);
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        assertNotNull(exp);
        assertTrue(exp instanceof CoreOperationRelationalExpression);
        assertEquals(2, exp.getArity());
        assertNotNull(exp.getArguments());
        assertEquals(exp1, exp.getArguments()[0]);
        assertEquals(exp2, exp.getArguments()[1]);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluation() {
        CoreOperation exp1 = new CoreOperationConstantExpression(1);
        CoreOperation exp2 = new CoreOperationConstantExpression(2);
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        JXPathContext context = new JXPathContext();
        context.setValues(new Object[]{"value1", "value2"});

        Object result = exp.evaluate(context);
        assertNull(result);

        exp1 = new CoreOperationConstantExpression(2);
        exp2 = new CoreOperationConstantExpression(1);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertFalse(result instanceof Boolean);

        exp1 = new CoreOperationConstantExpression(2);
        exp2 = new CoreOperationConstantExpression(2);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertFalse(result instanceof Boolean);

        exp1 = new CoreOperationConstantExpression(3);
        exp2 = new CoreOperationConstantExpression(2);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);

        exp1 = new CoreOperationConstantExpression(2);
        exp2 = new CoreOperationConstantExpression(3);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertFalse(result instanceof Boolean);

        exp1 = new CoreOperationConstantExpression(3);
        exp2 = new CoreOperationConstantExpression(3);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertFalse(result instanceof Boolean);

        exp1 = new CoreOperationConstantExpression(null);
        exp2 = new CoreOperationConstantExpression(2);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertNull(result);

        exp1 = new CoreOperationConstantExpression(2);
        exp2 = new CoreOperationConstantExpression(null);
        exp = new CoreOperationGreaterThanExpression(new Expression[]{exp1, exp2});

        result = exp.evaluate(context);
        assertNull(result);
    }
}