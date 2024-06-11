package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InitialContext;
import org.apache.commons.jxpath.ri.SelfContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test file for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} method.
     */
    @org.junit.Test
    public void testComputeValue() {
        Integer left = 5;
        Integer right = 10;

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(left), new ConstantExpression(right));
        Object result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertFalse((( Boolean ) result).booleanValue());

        left = 10;
        right = 5;

        expression = new GreaterThanExpression(new ConstantExpression(left), new ConstantExpression(right));
        result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertTrue((( Boolean ) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} method with Iterator operands.
     */
    @org.junit.Test
    public void testComputeValueIteratorOperands() {
        List<Integer> leftList = new ArrayList<>();
        leftList.add(5);
        leftList.add(10);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(10);
        rightList.add(5);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new IteratorExpression(leftList.iterator()), new IteratorExpression(rightList.iterator()));
        Object result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertFalse((( Boolean ) result).booleanValue());

        leftList.clear();
        leftList.add(10);

        rightList.clear();
        rightList.add(5);

        expression = new GreaterThanExpression(new IteratorExpression(leftList.iterator()), new IteratorExpression(rightList.iterator()));
        result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertTrue((( Boolean ) result).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()} method.
     */
    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(10));

        int result = expression.getPrecedence();

        assertEquals(RELATIONAL_EXPR_PRECEDENCE, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()} method.
     */
    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(10));

        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} method.
     */
    @org.junit.Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new Expression[0]);

        try {
            expression.evaluateCompare(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        expression = new GreaterThanExpression(new Expression[0]);

        assertTrue(expression.evaluateCompare(0));

        expression = new GreaterThanExpression(new Expression[0]);

        assertFalse(expression.evaluateCompare(1));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with Iterator operands.
     */
    @org.junit.Test
    public void testComputeIteratorOperands() {
        Iterator<Integer> leftIterator = Collections.singletonList(5).iterator();
        Iterator<Integer> rightIterator = Collections.singletonList(10).iterator();

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new IteratorExpression(leftIterator), new IteratorExpression(rightIterator));

        assertFalse(expression.compute(new InitialContext(), new InitialContext()));

        leftIterator = Collections.singletonList(10).iterator();
        rightIterator = Collections.singletonList(5).iterator();

        assertTrue(expression.compute(new InitialContext(), new InitialContext()));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with Collection operands.
     */
    @org.junit.Test
    public void testComputeCollectionOperands() {
        List<Integer> leftList = new ArrayList<>();
        leftList.add(5);
        leftList.add(10);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(10);
        rightList.add(5);

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new IteratorExpression(leftList.iterator()), new IteratorExpression(rightList.iterator()));

        assertFalse(expression.compute(new InitialContext(), new InitialContext()));

        Collections.sort(leftList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        Collections.sort(rightList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        assertTrue(expression.compute(new InitialContext(), new InitialContext()));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with InitialContext operands.
     */
    @org.junit.Test
    public void testComputeInitialContextOperands() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(new InitialContext()), new ConstantExpression(new InitialContext()));

        assertFalse(expression.compute(new EvalContext(), new EvalContext()));

        expression = new GreaterThanExpression(new ConstantExpression(new SelfContext()), new ConstantExpression(new SelfContext()));

        assertTrue(expression.compute(new EvalContext(), new EvalContext()));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)} method.
     */
    @org.junit.Test
    public void testReduce() {
        Object left = new InitialContext();

        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(left), new ConstantExpression(5));

        Object reducedLeft = expression.reduce(left);

        assertTrue(reducedLeft instanceof InitialContext);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)} method.
     */
    @org.junit.Test
    public void testContainsMatch() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(10);

        Iterator<Integer> iterator = list.iterator();

        assertTrue(CoreOperationRelationalExpression.containsMatch(iterator, new ConstantExpression(10)));

        assertFalse(CoreOperationRelationalExpression.containsMatch(iterator, new ConstantExpression(6)));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)} method.
     */
    @org.junit.Test
    public void testFindMatch() {
        List<Integer> leftList = new ArrayList<>();
        leftList.add(5);
        leftList.add(10);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(10);
        rightList.add(5);

        Iterator<Integer> leftIterator = leftList.iterator();
        Iterator<Integer> rightIterator = rightList.iterator();

        assertTrue(CoreOperationRelationalExpression.findMatch(leftIterator, rightIterator));

        leftList.clear();
        leftList.add(10);

        rightList.clear();
        rightList.add(5);

        assertFalse(CoreOperationRelationalExpression.findMatch(leftIterator, rightIterator));
    }

}