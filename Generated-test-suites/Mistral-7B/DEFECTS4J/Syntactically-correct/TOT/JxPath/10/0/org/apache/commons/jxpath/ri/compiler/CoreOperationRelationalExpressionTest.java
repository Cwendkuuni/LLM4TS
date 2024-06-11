package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterators;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.util.ObjectDeque;
import org.apache.commons.jxpath.ri.util.SimpleIterable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private Object leftValue;
    private Object rightValue;
    private Object expectedResult;

    public CoreOperationRelationalExpressionTest(Object leftValue, Object rightValue, Object expectedResult) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: test({0}, {1}) = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 1, Boolean.TRUE},
                {1, 2, Boolean.FALSE},
                {2, 1, Boolean.FALSE},
                {2, 2, Boolean.TRUE},
                {1.0, 1.0, Boolean.TRUE},
                {1.0, 2.0, Boolean.FALSE},
                {2.0, 1.0, Boolean.FALSE},
                {2.0, 2.0, Boolean.TRUE},
                {Integer.MIN_VALUE, Integer.MIN_VALUE, Boolean.TRUE},
                {Integer.MIN_VALUE, -1, Boolean.FALSE},
                {-1, Integer.MIN_VALUE, Boolean.FALSE},
                {-1, -1, Boolean.TRUE},
                {Double.NaN, Double.NaN, Boolean.TRUE},
                {Double.NaN, 1.0, Boolean.FALSE},
                {1.0, Double.NaN, Boolean.FALSE},
                {Collections.singletonList(1), Collections.singletonList(1), Boolean.TRUE},
                {Collections.singletonList(1), Arrays.asList(2, 3), Boolean.FALSE},
                {Arrays.asList(2, 3), Collections.singletonList(1), Boolean.FALSE},
                {Arrays.asList(1, 2), Arrays.asList(1, 2), Boolean.TRUE},
                {Arrays.asList(1, 2), Arrays.asList(2, 1), Boolean.FALSE}
        });
    }

    @Before
    public void setup() {
        expression = new GreaterThanExpression(new VariableExpression("$a"), new VariableExpression("$b"));
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setVariable("$a", leftValue);
        context.setVariable("$b", rightValue);
        Object result = expression.computeValue(context);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testGetPrecedence() {
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testEvaluateCompare() {
        boolean result = expression.evaluateCompare(0);
        assertFalse(result);

        result = expression.evaluateCompare(-1);
        assertTrue(result);

        result = expression.evaluateCompare(1);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        Object left = reduce(leftValue);
        Object right = reduce(rightValue);

        boolean result = expression.compute(left, right);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReduce() {
        Object left = reduce(leftValue);
        assertNotNull(left);

        if (left instanceof Collection) {
            Iterator it = ((Collection) left).iterator();
            assertTrue(it.hasNext());
            Object element = it.next();
            assertNotNull(element);
        }
    }

    @Test
    public void testContainsMatch() {
        Iterator it = (Iterator) leftValue;
        assertTrue(it.hasNext());
        Object element = it.next();
        assertTrue(expression.containsMatch(it, rightValue));

        it = (Iterator) rightValue;
        assertFalse(expression.containsMatch(it, leftValue));

        it = Iterators.emptyIterator();
        assertFalse(expression.containsMatch(it, leftValue));
        assertFalse(expression.containsMatch(it, rightValue));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = (Iterator) leftValue;
        Iterator rit = (Iterator) rightValue;

        List left = new ArrayList();
        while (lit.hasNext()) {
            left.add(lit.next());
        }

        assertTrue(expression.findMatch(lit, rit));

        Iterator emptyIter = Iterators.emptyIterator();
        assertFalse(expression.findMatch(lit, emptyIter));
        assertFalse(expression.findMatch(emptyIter, lit));

        Comparator comparator = Comparator.comparingInt(o -> ((Number) o).intValue());
        Collections.sort(left, comparator);
        Collections.sort(Arrays.asList(rightValue), comparator);
        assertFalse(expression.findMatch(lit, rightValue));
    }

    private Object reduce(Object o) {
        if (o instanceof SelfContext) {
            o = context.getSingleNodePointer();
        }
        if (o instanceof Collection) {
            o = ((Collection) o).iterator();
        }
        return o;
    }

}