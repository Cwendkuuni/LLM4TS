import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.util.TestHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(5));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(3));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(5));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(5));
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(3));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(5));
        CoreOperationRelationalExpression expression = new LessThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(5));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(5));
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testMixedTypes() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(3.5d));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(4L));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalStateException.class)
    public void testMixedTypesWithGreaterThanOrEqual() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(3.5d));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(4L));
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);
    }

    @Test
    public void testNullObjects() {
        CoreOperation arg1 = new CoreOperation(new ConstantExpression(new Object()));
        CoreOperation arg2 = new CoreOperation(new ConstantExpression(new Object()));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testIteratorComparison() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(2, 3, 4);
        CoreOperation arg1 = new CoreOperation(new IteratorExpression(list1.iterator()));
        CoreOperation arg2 = new CoreOperation(new IteratorExpression(list2.iterator()));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testIteratorComparisonWithMatchingElements() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(3, 2, 1);
        CoreOperation arg1 = new CoreOperation(new IteratorExpression(list1.iterator()));
        CoreOperation arg2 = new CoreOperator(new IteratorExpression(list2.iterator()));
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testIteratorComparisonWithMismatchedLength() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2);
        CoreOperation arg1 = new CoreOperation(new IteratorExpression(list1.iterator()));
        CoreOperation arg2 = new CoreOperation(new IteratorExpression(list2.iterator()));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextComparison() {
        CoreOperation arg1 = new CoreOperation(new SelfContext("node1"));
        CoreOperation arg2 = new CoreOperation(new SelfContext("node2"));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextComparisonWithSymmetricOperation() {
        CoreOperation arg1 = new CoreOperation(new SelfContext("node1"));
        CoreOperation arg2 = new CoreOperation(new SelfContext("node2"));
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(arg1, arg2);

        EvalContext context = TestHelper.newContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}