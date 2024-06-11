package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.apache.commons.jxpath.ri.compiler.CoreLiteral;
import org.apache.commons.jxpath.ri.types.IntegerType;
import org.apache.commons.jxpath.ri.types.Type;

public class CoreOperationNotEqualTest {

    @org.junit.Test
    public void testCoreOperationNotEqualBooleanFalse() {
        CoreExpression expr1 = new CoreLiteral(true);
        CoreExpression expr2 = new CoreLiteral(true);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualBooleanTrue() {
        CoreExpression expr1 = new CoreLiteral(true);
        CoreExpression expr2 = new CoreLiteral(false);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualIntegerZero() {
        CoreExpression expr1 = new CoreLiteral(0, new IntegerType());
        CoreExpression expr2 = new CoreLiteral(1, new IntegerType());
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualIntegerOne() {
        CoreExpression expr1 = new CoreLiteral(1, new IntegerType());
        CoreExpression expr2 = new CoreLiteral(1, new IntegerType());
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualIntegerNegativeOne() {
        CoreExpression expr1 = new CoreLiteral(-1, new IntegerType());
        CoreExpression expr2 = new CoreLiteral(-1, new IntegerType());
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualIntegerZeroAndOne() {
        CoreExpression expr1 = new CoreLiteral(0, new IntegerType());
        CoreExpression expr2 = new CoreLiteral(1, new IntegerType());
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualIntegerOneAndZero() {
        CoreExpression expr1 = new CoreLiteral(1, new IntegerType());
        CoreExpression expr2 = new CoreLiteral(0, new IntegerType());
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualStringDifferent() {
        CoreExpression expr1 = new CoreLiteral("abc", Type.STRING);
        CoreExpression expr2 = new CoreLiteral("def", Type.STRING);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqualStringEqual() {
        CoreExpression expr1 = new CoreLiteral("abc", Type.STRING);
        CoreExpression expr2 = new CoreLiteral("abc", Type.STRING);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(expr1, expr2);

        EvalContext context = new EvalContext(new Object[] {});
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}