package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.apache.commons.jxpath.ri.iter.StandardIterators;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;

/**
 * Test class for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression opGreater;
    private CoreOperationRelationalExpression opLess;
    private CoreOperationRelationalExpression opGreaterOrEqual;
    private CoreOperationRelationalExpression opLessOrEqual;

    @Before
    public void setUp() {
        ExpressionFactory factory = new ExpressionFactory();

        opGreater = new CoreOperationGreaterThan(new Expression[] {
                factory.createVariable("a") });
        opLess = new CoreOperationLessThan(new Expression[] {
                factory.createVariable("a") });
        opGreaterOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression[] { factory.createVariable("a") });
        opLessOrEqual = new CoreOperationLessThanOrEqual(
                new Expression[] { factory.createVariable("a") });
    }

    @Test
    public void testGreaterThan() {
        Number a = 5;
        Number b = 6;
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        Object result = opGreater.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context = new EvalContext(InfoSet.EMPTY_INFOSET, a);
        context.setVariable("a", a);

        result = opGreater.computeValue(context);
        result = opGreater.computeValue(context, b);
        assertFalse(result instanceof Boolean);

        Number c = 4;
        context.setVariable("a", a);

        result = opGreater.computeValue(context, c);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);

        context.setVariable("a", b);

        result = opGreater.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);

        result = opGreater.computeValue(context, c);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testLessThan() {
        Number a = 5;
        Number b = 6;
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        Object result = opLess.computeValue(context);
        assertFalse(result instanceof Boolean);

        result = opLess.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);

        context = new EvalContext(InfoSet.EMPTY_INFOSET, a);
        context.setVariable("a", a);

        result = opLess.computeValue(context);
        result = opLess.computeValue(context, b);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        Number c = 4;
        context.setVariable("a", a);

        result = opLess.computeValue(context, c);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context.setVariable("a", b);

        result = opLess.computeValue(context);
        assertFalse(result instanceof Boolean);

        result = opLess.computeValue(context, c);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Number a = 5;
        Number b = 6;
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        Object result = opGreaterOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);

        result = opGreaterOrEqual.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);

        context = new EvalContext(InfoSet.EMPTY_INFOSET, a);
        context.setVariable("a", a);

        result = opGreaterOrEqual.computeValue(context);
        result = opGreaterOrEqual.computeValue(context, b);
        assertFalse(result instanceof Boolean);

        result = opGreaterOrEqual.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context.setVariable("a", b);

        result = opGreaterOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        result = opGreaterOrEqual.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        result = opGreaterOrEqual.computeValue(context, c);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        Number a = 5;
        Number b = 6;
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        Object result = opLessOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);

        result = opLessOrEqual.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context = new EvalContext(InfoSet.EMPTY_INFOSET, a);
        context.setVariable("a", a);

        result = opLessOrEqual.computeValue(context);
        result = opLessOrEqual.computeValue(context, b);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);

        result = opLessOrEqual.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context.setVariable("a", b);

        result = opLessOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        result = opLessOrEqual.computeValue(context, a);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        result = opLessOrEqual.computeValue(context, c);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testComparisonWithCollection() {
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        List list = new ArrayList();
        list.add(Integer.valueOf(4));
        list.add(Integer.valueOf(6));

        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(
                new Expression[] { factory.createVariable("a") });
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression[] { factory.createVariable("a") });

        Object resultGreater = opGreater.computeValue(context, list.iterator());
        Object resultGreaterOrEqual = opGreaterOrEqual.computeValue(context, list.iterator());

        assertFalse(resultGreater instanceof Boolean);
        assertFalse(resultGreaterOrEqual instanceof Boolean);

        context.setVariable("a", list.iterator());

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertFalse(resultGreater instanceof Boolean);
        assertTrue(resultGreaterOrEqual instanceof Boolean);
        assertTrue((boolean) resultGreaterOrEqual);

        list.add(Integer.valueOf(7));

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertTrue(resultGreater instanceof Boolean);
        assertTrue((boolean) resultGreater);

        context.setVariable("a", list.iterator());
        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertTrue(resultGreater instanceof Boolean);
        assertTrue((boolean) resultGreater);

        list.add(Integer.valueOf(5));

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertFalse(resultGreater instanceof Boolean);
        assertTrue(resultGreaterOrEqual instanceof Boolean);
        assertTrue((boolean) resultGreaterOrEqual);
    }

    @Test
    public void testComparisonWithNull() {
        Number a = 5;
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        Object result = opGreater.computeValue(context, null);
        Object resultNull = opGreater.computeValue(context, a);

        assertFalse(result instanceof Boolean);
        assertTrue(resultNull instanceof Boolean);
        assertFalse((boolean) resultNull);

        context.setVariable("a", a);

        result = opGreater.computeValue(context, null);
        resultNull = opGreater.computeValue(context, null);

        assertFalse(result instanceof Boolean);
        assertFalse(resultNull instanceof Boolean);

        result = opGreaterOrEqual.computeValue(context, null);
        resultNull = opGreaterOrEqual.computeValue(context, a);

        assertFalse(result instanceof Boolean);
        assertTrue(resultNull instanceof Boolean);
        assertFalse((boolean) resultNull);

        context.setVariable("a", a);

        result = opGreaterOrEqual.computeValue(context, null);
        resultNull = opGreaterOrEqual.computeValue(context, null);

        assertFalse(result instanceof Boolean);
        assertTrue(resultNull instanceof Boolean);
        assertTrue((boolean) resultNull);
    }

    @Test
    public void testComparisonWithIterators() {
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        Iterator itA = Iterators.iter(1, 5);
        Iterator itB = Iterators.iter(1, 6);

        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(
                new Expression[] { factory.createVariable("a") });
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression[] { factory.createVariable("a") });

        Object resultGreater = opGreater.computeValue(context, itA);
        Object resultGreaterOrEqual = opGreaterOrEqual.computeValue(context, itA);

        assertFalse(resultGreater instanceof Boolean);
        assertFalse(resultGreaterOrEqual instanceof Boolean);

        context.setVariable("a", itA);

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertFalse(resultGreater instanceof Boolean);
        assertTrue(resultGreaterOrEqual instanceof Boolean);
        assertTrue((boolean) resultGreaterOrEqual);

        context.setVariable("a", itB);

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertFalse(resultGreater instanceof Boolean);
        assertTrue(resultGreaterOrEqual instanceof Boolean);
        assertFalse((boolean) resultGreaterOrEqual);

        context.setVariable("a", itA);
        itA = itA.next();

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertTrue(resultGreater instanceof Boolean);
        assertTrue((boolean) resultGreater);

        context.setVariable("a", itB);
        itB = itB.next();

        resultGreater = opGreater.computeValue(context);
        resultGreaterOrEqual = opGreaterOrEqual.computeValue(context);

        assertTrue(resultGreater instanceof Boolean);
        assertTrue((boolean) resultGreater);
    }

    @Test
    public void testComparisonWithDoubles() {
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        double a = 4.1;
        double b = 5.2;

        Object result = opGreater.computeValue(context);
        Object resultA = opGreater.computeValue(context, a);
        Object resultB = opGreater.computeValue(context, b);

        assertFalse(result instanceof Boolean);
        assertFalse(resultA instanceof Boolean);
        assertFalse(resultB instanceof Boolean);

        context.setVariable("a", a);

        result = opGreater.computeValue(context);
        resultA = opGreater.computeValue(context, b);
        resultB = opGreater.computeValue(context, a);

        assertFalse(result instanceof Boolean);
        assertTrue(resultA instanceof Boolean);
        assertFalse((boolean) resultA);

        context.setVariable("a", b);

        result = opGreater.computeValue(context);
        resultA = opGreater.computeValue(context, a);
        resultB = opGreater.computeValue(context, b);

        assertFalse(result instanceof Boolean);
        assertTrue(resultB instanceof Boolean);
        assertTrue((boolean) resultB);

        context.setVariable("a", a);

        result = opGreaterOrEqual.computeValue(context);
        resultA = opGreaterOrEqual.computeValue(context, b);
        resultB = opGreaterOrEqual.computeValue(context, a);

        assertFalse(result instanceof Boolean);
        assertTrue(resultA instanceof Boolean);
        assertFalse((boolean) resultA);

        context.setVariable("a", b);

        result = opGreaterOrEqual.computeValue(context);
        resultA = opGreaterOrEqual.computeValue(context, a);
        resultB = opGreaterOrEqual.computeValue(context, b);

        assertFalse(result instanceof Boolean);
        assertTrue(resultB instanceof Boolean);
        assertTrue((boolean) resultB);
    }

    @Test
    public void testComparisonWithDoublesAndNaN() {
        EvalContext context = new EvalContext(InfoSet.EMPTY_INFOSET, null);

        double a = 4.1;
        double n = Double.NaN;

        Object result = opGreater.computeValue(context);
        Object resultA = opGreater.computeValue(context, a);
        Object resultN = opGreater.computeValue(context, n);

        assertFalse(result instanceof Boolean);
        assertFalse(resultA instanceof Boolean);
        assertFalse(resultN instanceof Boolean);

        context.setVariable("a", a);

        result = opGreater.computeValue(context);
        resultA = opGreater.computeValue(context, n);
        resultN = opGreater.computeValue(context, a);

        assertFalse(result instanceof Boolean);
        assertFalse(resultA instanceof Boolean);
        assertTrue(resultN instanceof Boolean);
        assertFalse((boolean) resultN);

        context.setVariable("a", n);

        result = opGreater.computeValue(context);
        resultA = opGreater.computeValue(context, a);
        resultN = opGreater.computeValue(context, n);

        assertFalse(result instanceof Boolean);
        assertTrue(resultN instanceof Boolean);
        assertTrue((boolean) resultN);

        context.setVariable("a", a);

        result = opGreaterOrEqual.computeValue(context);
        resultA = opGreaterOrEqual.computeValue(context, n);
        resultN = opGreaterOrEqual.computeValue(context, a);

        assertFalse(result instanceof Boolean);
        assertFalse(resultA instanceof Boolean);
        assertTrue(resultN instanceof Boolean);
        assertFalse((boolean) resultN);

        context.setVariable("a", n);

        result = opGreaterOrEqual.computeValue(context);
        resultA = opGreaterOrEqual.computeValue(context, a);
        resultN = opGreaterOrEqual.computeValue(context, n);

        assertFalse(result instanceof Boolean);
        assertTrue(resultN instanceof Boolean);
        assertTrue((boolean) resultN);
    }
}