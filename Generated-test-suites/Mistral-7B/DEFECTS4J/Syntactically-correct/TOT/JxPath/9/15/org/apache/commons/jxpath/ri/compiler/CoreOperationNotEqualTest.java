package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Term;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

/**
 * Test for CoreOperationNotEqual class.
 *
 * @author Dmitri Plotnikov
 */
public class CoreOperationNotEqualTest extends CompilerTest {

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression, Expression)} .
     */
    @SuppressWarnings("unchecked")
    public void testCoreOperationNotEqual() {
        Expression arg1 = (Expression) compileExpression("1");
        Expression arg2 = (Expression) compileExpression("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertSame(CoreOperationNotEqual.class, operation.getClass());
        assertSame(arg1, operation.getArg1());
        assertSame(arg2, operation.getArg2());
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with true result.
     */
    public void testComputeValueTrue() {
        Expression arg1 = (Expression) compileExpression("1");
        Expression arg2 = (Expression) compileExpression("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with false result.
     */
    public void testComputeValueFalse() {
        Expression arg1 = (Expression) compileExpression("2");
        Expression arg2 = (Expression) compileExpression("1");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with equals expressions.
     */
    public void testComputeValueEquals() {
        Expression arg1 = (Expression) compileExpression("1");
        Expression arg2 = (Expression) compileExpression("1");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()} .
     */
    public void testGetSymbol() {
        Expression arg1 = (Expression) compileExpression("1");
        Expression arg2 = (Expression) compileExpression("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with null argument.
     */
    public void testComputeValueNullArg() {
        Expression arg1 = null;
        Expression arg2 = (Expression) compileExpression("1");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with null context.
     */
    public void testComputeValueNullContext() {
        Expression arg1 = (Expression) compileExpression("1");
        Expression arg2 = (Expression) compileExpression("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        Object result = operation.computeValue(null);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with null arg1.
     */
    public void testComputeValueNullArg1() {
        Expression arg1 = null;
        Expression arg2 = (Expression) compileExpression("1");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with null arg2.
     */
    public void testComputeValueNullArg2() {
        Expression arg1 = (Expression) compileExpression("1");
        Expression arg2 = null;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with arg1 instance of Term.
     */
    public void testComputeValueArg1Term() {
        Expression arg1 = (Expression) compileExpression("1");
        Term arg2 = compileTerm("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with arg2 instance of Term.
     */
    public void testComputeValueArg2Term() {
        Expression arg1 = (Expression) compileExpression("1");
        Term arg2 = compileTerm("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }
}