import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(2.0);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.TRUE, result);

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(2.0);

        result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThan() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(2.0);

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.TRUE, result);

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(2.0);

        result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComparisonWithNullLeft() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(null);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result);
    }

    @Test
    public void testComparisonWithNullRight() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(1.0);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(null);

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result);
    }

    @Test
    public void testComparisonWithNullBoth() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(null);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(null);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComparisonWithIterators() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator leftIterator = Collections.singletonList(1).iterator();
        Iterator rightIterator = Collections.singletonList(2).iterator();

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(leftIterator);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(rightIterator);

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result);
    }

    @Test
    public void testComparisonWithMatchingIterators() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator leftIterator = Collections.singletonList(1).iterator();
        Iterator rightIterator = Collections.singletonList(1).iterator();

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(leftIterator);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(rightIterator);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparisonWithContainingIterators() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator leftIterator = Collections.singletonList(1).iterator();
        Iterator rightIterator = Collections.singletonList(1, 1).iterator();

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(leftIterator);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(rightIterator);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparisonWithOverlappingIterators() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator leftIterator = Collections.singletonList(1, 1).iterator();
        Iterator rightIterator = Collections.singletonList(1).iterator();

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(leftIterator);
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(rightIterator);

        Object result = expression.computeValue(new EvalContext());

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparisonWithDifferentDataTypes() {
        CoreOperation left = Mockito.mock(CoreOperation.class);
        CoreOperation right = Mockito.mock(CoreOperation.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(left.computeValue(Mockito.any(EvalContext.class))).thenReturn(InfoSetUtil.createInfoSet(1.0));
        Mockito.when(right.computeValue(Mockito.any(EvalContext.class))).thenReturn(InfoSetUtil.createInfoSet("2"));

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result);
    }
}