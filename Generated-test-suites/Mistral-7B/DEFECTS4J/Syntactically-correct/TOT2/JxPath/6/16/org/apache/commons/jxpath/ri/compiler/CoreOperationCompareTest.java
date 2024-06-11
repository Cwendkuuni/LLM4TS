// Test cases for CoreOperationCompare class

package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assert.*;
import org.apache.commons.jxpath.*;
import org.apache.commons.jxpath.axes.*;
import java.util.*;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new VariableExpression("test");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testEqual() {
        EvaluationContext context = new EvaluationContext();
        Expression left = new ConstantExpression(1);
        Expression right = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);

        left = new ConstantExpression("hello");
        right = new ConstantExpression("hello");
        result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);

        left = new ConstantExpression(true);
        right = new ConstantExpression(true);
        result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);
    }

    @Test
    public void testEqualEdgeCases() {
        EvaluationContext context = new EvaluationContext();

        // Testing with null
        Expression left = new ConstantExpression(null);
        Expression right = new ConstantExpression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);

        // Testing with different data types
        left = new ConstantExpression(1);
        right = new ConstantExpression("1");
        result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);

        left = new ConstantExpression(new Integer(1));
        right = new ConstantExpression(new Integer(2));
        result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);

        // Testing with collections
        Collection<Integer> leftCollection = new ArrayList<>();
        leftCollection.add(1);
        leftCollection.add(2);
        Iterator<Integer> leftIterator = leftCollection.iterator();

        Collection<Integer> rightCollection = new ArrayList<>();
        rightCollection.add(1);
        rightCollection.add(2);
        Iterator<Integer> rightIterator = rightCollection.iterator();

        coreOperationCompare = new CoreOperationCompare(new IteratorExpression(leftIterator), new IteratorExpression(rightIterator));
        result = coreOperationCompare.equal(context, leftIterator, rightIterator);
        assertTrue(result);

        leftCollection.remove(1);
        rightCollection.add(3);
        result = coreOperationCompare.equal(context, leftIterator, rightIterator);
        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testEqualExceptionHandling() {
        EvaluationContext context = new EvaluationContext();

        Expression left = new ThrowingExpression(new RuntimeException("Test Exception"));
        Expression right = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        coreOperationCompare.equal(context, left, right);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualIllegalArgumentException() {
        EvaluationContext context = new EvaluationContext();

        Expression left = new ConstantExpression(null);
        Expression right = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        coreOperationCompare.equal(context, left, right);
    }
}