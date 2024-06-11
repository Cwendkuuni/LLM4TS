import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.NumberConstant;

import java.util.Collections;
import java.util.Iterator;
import java.util.HashSet;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGT() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] { new Constant(new SelfContext()), new NumberConstant(3) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object left = new SelfContext();
        Object right = new NumberConstant(2);

        assertTrue(expression.computeValue(context));

        left = new NumberConstant(3);
        assertFalse(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(4);
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testGTE() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] { new Constant(new SelfContext()), new NumberConstant(3) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object left = new SelfContext();
        Object right = new NumberConstant(2);

        assertTrue(expression.computeValue(context));

        left = new NumberConstant(2);
        assertTrue(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(3);
        assertTrue(expression.computeValue(context));

        left = new NumberConstant(3);
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testLT() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] { new Constant(new SelfContext()), new NumberConstant(3) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object left = new SelfContext();
        Object right = new NumberConstant(2);

        assertFalse(expression.computeValue(context));

        left = new NumberConstant(3);
        assertFalse(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(2);
        assertFalse(expression.computeValue(context));

        left = new NumberConstant(2);
        assertFalse(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(3);
        assertFalse(expression.computeValue(context));

        left = new NumberConstant(3);
        assertFalse(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(3);
        assertFalse(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(4);
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testLTE() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] { new Constant(new SelfContext()), new NumberConstant(3) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object left = new SelfContext();
        Object right = new NumberConstant(2);

        assertTrue(expression.computeValue(context));

        left = new NumberConstant(3);
        assertTrue(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(3);
        assertTrue(expression.computeValue(context));

        left = new NumberConstant(3);
        assertTrue(expression.computeValue(context));

        left = new SelfContext();
        right = new NumberConstant(4);
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComparisonWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] { new Constant(new IteratorTestData()[0].iterator()), new Constant(new IteratorTestData()[1].iterator()) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());

        Iterator<Object> leftIterator = IteratorTestData[0].iterator();
        Iterator<Object> rightIterator = IteratorTestData[1].iterator();

        assertTrue(expression.computeValue(context));
    }

    static class IteratorTestData {
        static Object[] data = {1, 2, 3, 4, 5};

        static Iterator<Object> iterator() {
            return Collections.singletonList(data).iterator();
        }
    }
}