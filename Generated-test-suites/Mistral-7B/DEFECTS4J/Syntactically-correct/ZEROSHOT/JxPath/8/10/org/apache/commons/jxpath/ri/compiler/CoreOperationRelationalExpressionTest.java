import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(3)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Object left = new Integer(5);
        Object right = new Integer(3);

        Assert.assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(3)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Object left = new Integer(5);
        Object right = new Integer(3);

        Assert.assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        left = new Integer(3);
        Assert.assertTrue(expression.computeValue(context).equals(Boolean.FALSE));
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(3), new CoreNumberExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Object left = new Integer(3);
        Object right = new Integer(5);

        Assert.assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        left = new Integer(5);
        Assert.assertFalse(expression.computeValue(context).equals(Boolean.FALSE));
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(3), new CoreNumberExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Object left = new Integer(3);
        Object right = new Integer(5);

        Assert.assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        left = new Integer(5);
        Assert.assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testIteratorsWithSameValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Collection list1 = new ArrayList();
        list1.add(new Integer(5));
        Object left = list1.iterator();
        Collection list2 = new ArrayList();
        list2.add(new Integer(5));
        Object right = list2.iterator();

        Assert.assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testIteratorsWithDifferentValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(3)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Collection list1 = new ArrayList();
        list1.add(new Integer(5));
        Object left = list1.iterator();
        Collection list2 = new ArrayList();
        list2.add(new Integer(3));
        Object right = list2.iterator();

        Assert.assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testIteratorsWithSameIterator() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Iterator iterator = new ArrayList() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        }.iterator();
        Object left = iterator;
        Object right = iterator;

        Assert.assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOperation() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new CoreNumberExpression(3)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new RuntimeException();
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Object left = new Integer(5);
        Object right = new Integer(3);

        expression.computeValue(context);
    }

    @Test
    public void testComparisonWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(5), new SelfContext()) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext(new InitialContext());
        Object left = new Integer(5);
        Assert.assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        context.setCurrentNode(left);
        Assert.assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }
}