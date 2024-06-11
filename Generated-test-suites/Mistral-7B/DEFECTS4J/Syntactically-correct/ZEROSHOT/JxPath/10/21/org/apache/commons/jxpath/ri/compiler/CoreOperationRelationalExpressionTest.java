import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new LiteralExpression(new SelfContext(1));
        arg2 = new LiteralExpression(new SelfContext(2));
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
    }

    @Test
    public void testGT() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = arg1.computeValue(context);
        Object value2 = arg2.computeValue(context);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLT() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = arg1.computeValue(context);
        Object value2 = arg2.computeValue(context);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGE() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = arg1.computeValue(context);
        Object value2 = arg2.computeValue(context);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLE() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = arg1.computeValue(context);
        Object value2 = arg2.computeValue(context);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsWithMatchingElements() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIteratorsWithNoMatchingElements() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(2);
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsWithOneMatchingElement() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIteratorsWithOneNonMatchingElement() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(2);
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsWithMultipleMatchingElements() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it3 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        expression = new CoreOperationRelationalExpression(expression) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return containsMatch(it1, it2);
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIteratorsWithNoMatchingElementsBetweenThem() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(1);
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new SelfContext(3);
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return containsMatch(it1, it2);
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDoubleValuesGreaterThan() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = new Double(1.0);
        Object value2 = new Double(2.0);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDoubleValuesLessThan() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = new Double(1.0);
        Object value2 = new Double(2.0);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDoubleValuesGreaterThanOrEqual() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = new Double(1.0);
        Object value2 = new Double(2.0);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDoubleValuesLessThanOrEqual() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = new Double(1.0);
        Object value2 = new Double(2.0);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDoubleValuesEqual() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = new Double(1.0);
        Object value2 = new Double(1.0);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDoubleValuesNotEqual() {
        EvalContext context = new EvalContext(new InitialContext());
        Object value1 = new Double(1.0);
        Object value2 = new Double(2.0);
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyIterators() {
        EvalContext context = new EvalContext(new InitialContext());
        Iterator<Object> it1 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        Iterator<Object> it2 = new Iterate<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return containsMatch(it1, it2);
            }
        };
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        EvalContext context = new EvalContext(new InitialContext());
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new IllegalArgumentException();
            }
        };
        expression.computeValue(context);
    }
}