import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.RIConstants;
import org.apache.commons.jxpath. ripper.CoreOperationCompare;
import org.apache.commons.jxpath.ripper.CoreOperationTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest extends CoreOperationTest {

    @Test
    public void testEqual() {
        Expression left = createExpression("1");
        Expression right = createExpression("1");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createContext();
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("true");
        right = createExpression("true");
        compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("'abc'");
        right = createExpression("'abc'");
        compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3)::integer(.)");
        right = createExpression("(1, 2, 3)::integer(.)");
        compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3)::integer(.)");
        right = createExpression("(1, 2, 3)::integer(.)");
        ComparisonOperators op = new ComparisonOperators();
        compare = op.createCompareOperation("=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("1");
        right = createExpression("1.0");
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("1");
        right = createExpression("'1'");
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("'abc'");
        right = createExpression("'def'");
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3)::integer(.)");
        right = createExpression("(1, 2, 4)::integer(.)");
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("'abc'");
        right = createExpression("null");
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("null");
        right = createExpression("'abc'");
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("null");
        right = createExpression("null");
        compare = new CoreOperationCompare(left, right);
        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testNotEqual() {
        Expression left = createExpression("1");
        Expression right = createExpression("1");
        ComparisonOperators op = new ComparisonOperators();
        CoreOperationCompare compare = op.createCompareOperation("!=", left, right);
        EvalContext context = createContext();
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("true");
        right = createExpression("false");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("'abc'");
        right = createExpression("'def'");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3)::integer(.)");
        right = createExpression("(1, 2, 4)::integer(.)");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("1");
        right = createExpression("1.0");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("1");
        right = createExpression("'1'");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("'abc'");
        right = createExpression("null");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("null");
        right = createExpression("'abc'");
        compare = op.createCompareOperation("!=", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("null");
        right = createExpression("null");
        compare = op.createCompareOperation("!=", left, right);
        assertFalse(compare.evaluateToBoolean(context));
    }

    @Test
    public void testContainment() {
        Expression left = createExpression("(1, 2, 3, 4, 5)");
        Expression right = createExpression("(1, 2)");
        ComparisonOperators op = new ComparisonOperators();
        CoreOperationCompare compare = op.createCompareOperation("contains", left, right);
        EvalContext context = createContext();
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(6, 7)");
        compare = op.createCompareOperation("contains", left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(3, 4)");
        compare = op.createCompareOperation("contains", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5)");
        compare = op.createCompareOperation("contains", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5, 6, 7)");
        compare = op.createCompareOperation("contains", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(2, 3, 4, 5)");
        compare = op.createCompareOperation("contains", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("()");
        compare = op.createCompareOperation("contains", left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("()");
        right = createExpression("(1, 2, 3, 4, 5)");
        compare = op.createCompareOperation("contains", left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5)");
        compare = op.createCompareOperation("contains", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5, 1, 1)");
        compare = op.createCompareOperation("contains", left, right);
        assertTrue(compare.evaluateToBoolean(context));
    }

    @Test
    public void testMatches() {
        Expression left = createExpression("(1, 2, 3, 4, 5)");
        Expression right = createExpression("(1, 2, 3)");
        ComparisonOperators op = new ComparisonOperators();
        CoreOperationCompare compare = op.createCompareOperation("matches", left, right);
        EvalContext context = createContext();
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(3, 4)");
        compare = op.createCompareOperation("matches", left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(3, 4, 3)");
        compare = op.createCompareOperation("matches", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5, 1, 1)");
        compare = op.createCompareOperation("matches", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5, 6, 7)");
        compare = op.createCompareOperation("matches", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("()");
        compare = op.createCompareOperation("matches", left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("()");
        right = createExpression("(1, 2, 3, 4, 5)");
        compare = op.createCompareOperation("matches", left, right);
        assertFalse(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5)");
        compare = op.createCompareOperation("matches", left, right);
        assertTrue(compare.evaluateToBoolean(context));

        left = createExpression("(1, 2, 3, 4, 5)");
        right = createExpression("(1, 2, 3, 4, 5, 1, 1)");
        compare = op.createCompareOperation("matches", left, right);
        assertTrue(compare.evaluateToBoolean(context));
    }

    private Expression createExpression(String expression) {
        return createExpressionFromText(RIConstants.JXPATH_EXPRESSION, expression);
    }

    private static Iterator createIterator(Object[] array) {
        return Arrays.asList(array).iterator();
    }

    private static Iterator createIterator(List list) {
        return list.iterator();
    }

    private static Iterator createIterator(Collection collection) {
        return collection.iterator();
    }
}