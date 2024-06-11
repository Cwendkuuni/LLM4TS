import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThanExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(
                new CorePrimitiveExpression("5.0"), new CorePrimitiveExpression("4.0"));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(
                new CorePrimitiveExpression("5.0"), new CorePrimitiveExpression("4.0"));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqualExpression(
                new CorePrimitiveExpression("4.0"), new CorePrimitiveExpression("5.0"));

        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(
                new CorePrimitiveExpression("4.0"), new CorePrimitiveExpression("5.0"));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationLessThanExpression(
                new CorePrimitiveExpression("5.0"), new CorePrimitiveExpression("4.0"));

        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(
                new CorePrimitiveExpression("4.0"), new CorePrimitiveExpression("5.0"));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationLessThanOrEqualExpression(
                new CorePrimitiveExpression("5.0"), new CorePrimitiveExpression("4.0"));

        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompareDoubleValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CorePrimitiveExpression("5.0"), new CorePrimitiveExpression("5.0"));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationRelationalExpression(
                new CorePrimitiveExpression("5.0"), new CorePrimitiveExpression("6.0"));

        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompareCollections() {
        Iterator leftIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        Iterator rightIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreIteratorExpression(leftIter), new CoreIteratorExpression(rightIter));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        leftIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        context = new EvalContext(new InitialContext());
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompareIteratorsWithMatchingElements() {
        Iterator leftIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        Iterator rightIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreIteratorExpression(leftIter), new CoreIteratorExpression(rightIter));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCompareIteratorsWithNoMatchingElements() {
        Iterator leftIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        Iterator rightIter = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreIteratorExpression(leftIter), new CoreIteratorExpression(rightIter));

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCompareSelfContexts() {
        Expression left = new CorePrimitiveExpression("node1");
        Expression right = new CorePrimitiveExpression("node1");

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CorePrimitiveExpression("node1"), right);

        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}