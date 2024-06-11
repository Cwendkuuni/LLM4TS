import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.apache.commons.jxpath.ri.compiler.VariableContext;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CoreOperationRelationalExpression} class.
 */
public class CoreOperationRelationalExpressionTest {

    private static CoreOperation createCompareOperation(String op, Expression left, Expression right) {
        String[] args = new String[]{"x", "y"};
        return new CoreOperationRelationalExpression(new CoreOperationFactory().createExpression(args, op, null, null, new Expression[]{left, right}));
    }

    @Test
    public void testGreaterThan() {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertTrue(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testLessThan() {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperation operation = createCompareOperation("&lt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testGreaterOrEqualThan() {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(1.0);
        CoreOperation operation = createCompareOperation("&gt;=", left, right);

        assertTrue(operation.computeValue(new VariableContext()));

        left = ExpressionFactory.createNumber(1.0);
        right = ExpressionFactory.createNumber(2.0);
        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testLessOrEqualThan() {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(1.0);
        CoreOperation operation = createCompareOperation("&lt;=", left, right);

        assertTrue(operation.computeValue(new VariableContext()));

        left = ExpressionFactory.createNumber(1.0);
        right = ExpressionFactory.createNumber(2.0);
        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareNaNValues() {
        Expression left = ExpressionFactory.createNumber(Double.NaN);
        Expression right = ExpressionFactory.createNumber(Double.NaN);
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareDifferentTypes() {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createString("one");
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareIterators() {
        Expression left = ExpressionFactory.createIterator(List.of(1.0, 2.0, 3.0));
        Expression right = ExpressionFactory.createIterator(List.of(2.0, 3.0, 4.0));
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));

        left = ExpressionFactory.createIterator(List.of(1.0, 2.0, 3.0, 4.0));
        right = ExpressionFactory.createIterator(List.of(2.0, 3.0, 4.0));
        assertTrue(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareIteratorsWithMatchingElements() {
        Expression left = ExpressionFactory.createIterator(List.of(1.0, 2.0, 3.0));
        Expression right = ExpressionFactory.createIterator(List.of(1.0, 2.0, 3.0));
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareIteratorsWithMismatchingElements() {
        Expression left = ExpressionFactory.createIterator(List.of(1.0, 2.0, 3.0));
        Expression right = ExpressionFactory.createIterator(List.of(1.0, 2.0, 4.0));
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));

        left = ExpressionFactory.createIterator(List.of(1.0, 2.0, 3.0));
        right = ExpressionFactory.createIterator(Collections.singletonList(4.0));
        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareInitialContexts() {
        Expression left = ExpressionFactory.createInitialContext();
        Expression right = ExpressionFactory.createInitialContext();
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));
    }

    @Test
    public void testCompareSelfContexts() {
        Expression left = ExpressionFactory.createSelfContext();
        Expression right = ExpressionFactory.createSelfContext();
        CoreOperation operation = createCompareOperation("&gt;", left, right);

        assertFalse(operation.computeValue(new VariableContext()));
    }
}