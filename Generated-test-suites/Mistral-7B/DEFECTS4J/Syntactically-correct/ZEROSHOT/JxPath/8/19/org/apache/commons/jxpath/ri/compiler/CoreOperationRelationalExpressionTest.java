import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.InformationSet;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private CoreOperation arg1, arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = Mockito.mock(CoreOperation.class);
        arg2 = Mockito.mock(CoreOperation.class);
        context = Mockito.mock(EvalContext.class);
        expression = new CoreOperationRelationalExpression(new CoreOperation[]{arg1, arg2});
    }

    @Test
    public void testComputeValue() {
        Mockito.when(arg1.computeValue(context)).thenReturn(Integer.valueOf(5));
        Mockito.when(arg2.computeValue(context)).thenReturn(Integer.valueOf(6));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result.equals(Boolean.FALSE));

        Mockito.when(arg1.computeValue(context)).thenReturn(Integer.valueOf(5));
        Mockito.when(arg2.computeValue(context)).thenReturn(Integer.valueOf(5));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        expression = new GreaterThanExpression(new CoreOperation[]{arg1, arg2});
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));

        expression = new GreaterOrEqualExpression(new CoreOperation[]{arg1, arg2});
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Object left = Mockito.mock(InformationSet.class);
        Object right = Mockito.mock(InformationSet.class);
        Mockito.when(arg1.computeValue(context)).thenReturn(left);
        Mockito.when(arg2.computeValue(context)).thenReturn(right);

        Mockito.when(left instanceof InitialContext).thenReturn(true);
        Mockito.when(right instanceof InitialContext).thenReturn(true);
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Iterator).thenReturn(createIterator(1, 3));
        Mockito.when(right instanceof Iterator).thenReturn(createIterator(2, 3));
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Iterator).thenReturn(createIterator(1, 3));
        Mockito.when(right instanceof Collection).thenReturn(createCollection(2));
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Collection).thenReturn(createCollection(1));
        Mockito.when(right instanceof Iterator).thenReturn(createIterator(2, 3));
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Collection).thenReturn(createCollection(1));
        Mockito.when(right instanceof Collection).thenReturn(createCollection(2));
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Double).thenReturn(1.5);
        Mockito.when(right instanceof Double).thenReturn(2.5);
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Double).thenReturn(1.5);
        Mockito.when(right instanceof Double).thenReturn(1.5);
        assertTrue(expression.compute(left, right));

        Mockito.when(left instanceof Double).thenReturn(1.5);
        Mockito.when(right instanceof Double).thenReturn(2.0);
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Double).thenReturn(2.0);
        Mockito.when(right instanceof Double).thenReturn(1.5);
        assertFalse(expression.compute(left, right));

        Mockito.when(left instanceof Double).thenReturn(1.5);
        Mockito.when(right instanceof Double).thenReturn(1.5);
        assertTrue(expression.compute(left, right));
    }

    @Test
    public void testReduce() {
        Object value = createIterator(1, 3);
        Object result = expression.reduce(value);
        assertTrue(result instanceof Iterator);
        assertEquals(value, result);

        value = createCollection(1);
        result = expression.reduce(value);
        assertTrue(result instanceof Iterator);
        assertEquals(createIterator(1), result);

        value = Mockito.mock(SelfContext.class);
        result = expression.reduce(value);
        assertTrue(result instanceof InformationSet);
        assertEquals(Mockito.mock(InitialContext.class), result);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = createIterator(1, 3);
        Object value = 2;
        assertTrue(expression.containsMatch(it, value));

        it = createIterator(1, 3);
        assertFalse(expression.containsMatch(it, Integer.valueOf(4)));

        it = createIterator(1, 3);
        value = createIterator(2, 3);
        assertFalse(expression.containsMatch(it, value));

        it = createIterator(1, 3);
        value = createCollection(2);
        assertFalse(expression.containsMatch(it, value));

        it = createIterator(1);
        value = createCollection(2);
        assertFalse(expression.containsMatch(it, value));

        it = createIterator(1, 3);
        value = createCollection(1, 3);
        assertFalse(expression.containsMatch(it, value));

        it = createIterator(1, 3);
        assertTrue(expression.containsMatch(it, Integer.valueOf(1)));

        it = createIterator(1, 3);
        assertTrue(expression.containsMatch(it, Integer.valueOf(2)));

        it = createIterator(1, 3);
        assertTrue(expression.containsMatch(it, Integer.valueOf(3)));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = createIterator(1, 3);
        Iterator rit = createIterator(2, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createIterator(1, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createIterator(1, 3);
        assertFalse(expression.findMatch(rit, lit));

        lit = createIterator(1, 3);
        rit = createIterator(1, 3);
        assertFalse(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createIterator(1, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createIterator(2, 3);
        assertFalse(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createIterator(2, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createIterator(2, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createIterator(1, 3);
        rit = createCollection(1, 3);
        assertFalse(expression.findMatch(lit, rit));

        lit = createCollection(1, 3);
        rit = createIterator(1, 3);
        assertFalse(expression.findMatch(lit, rit));

        lit = createCollection(1, 3);
        rit = createCollection(1, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createCollection(1, 3);
        rit = createCollection(2, 3);
        assertFalse(expression.findMatch(lit, rit));

        lit = createCollection(1, 3);
        rit = createCollection(2, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createCollection(1, 3);
        rit = createCollection(2, 3);
        assertTrue(expression.findMatch(lit, rit));

        lit = createCollection(1, 3);
        rit = createCollection(2, 3);
        assertTrue(expression.findMatch(lit, rit));
    }

    private Iterator createIterator(int... values) {
        List list = new ArrayList();
        for (int value : values) {
            list.add(Integer.valueOf(value));
        }
        return list.iterator();
    }

    private Collection createCollection(int... values) {
        List list = new ArrayList();
        for (int value : values) {
            list.add(Integer.valueOf(value));
        }
        return list;
    }

    private void assertEquals(Object expected, Object actual) {
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }
}