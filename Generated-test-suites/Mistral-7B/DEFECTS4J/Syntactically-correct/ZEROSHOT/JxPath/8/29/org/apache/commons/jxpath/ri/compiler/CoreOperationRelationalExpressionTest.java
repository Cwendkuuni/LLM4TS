import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.Context;
import org.apache.commons.jxpath.ri.IterateContext;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.Sequence;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NumberLiteralExpression(5), new NumberLiteralExpression(6)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null), new IterateContext(Nodes.create(1), Nodes.create(2), null), null);
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new CompilerContext(new IterateContext(Nodes.create(1), Nodes.create(2), null), Nodes.EMPTY_SEQUENCE, null);
        result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NumberLiteralExpression(5), new NumberLiteralExpression(6)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null), new IterateContext(Nodes.create(1), Nodes.create(2), null), null);
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new CompilerContext(new IterateContext(Nodes.create(1), Nodes.create(2), null), Nodes.EMPTY_SEQUENCE, null);
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new CompilerContext(new IterateContext(Nodes.create(1), Nodes.create(1), null), Nodes.EMPTY_SEQUENCE, null);
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NumberLiteralExpression(5), new NumberLiteralExpression(6)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null), new IterateContext(Nodes.create(1), Nodes.create(2), null), null);
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);

        context = new CompilerContext(new IterateContext(Nodes.create(2), Nodes.create(1), null), Nodes.EMPTY_SEQUENCE, null);
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NumberLiteralExpression(5), new NumberLiteralExpression(6)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null), new IterateContext(Nodes.create(1), Nodes.create(2), null), null);
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);

        context = new CompilerContext(new IterateContext(Nodes.create(1), Nodes.create(1), null), Nodes.EMPTY_SEQUENCE, null);
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new CompilerContext(new IterateContext(Nodes.create(1), Nodes.create(2), null), Nodes.EMPTY_SEQUENCE, null);
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NodeSetExpression(Arrays.asList(1, 2, 3)), new NodeSetExpression(Arrays.asList(1, 3, 4))) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null),
                new IterateContext(Nodes.create(1, 2, 3), Nodes.create(1, 3, 4), null), null);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new CompilerContext(new IterateContext(Nodes.create(1, 2, 3), Nodes.create(1, 3, 4), null), Nodes.EMPTY_SEQUENCE, null);

        result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);

        context = new CompilerContext(new IterateContext(Nodes.create(1, 2, 3), Nodes.create(1, 2, 4), null), Nodes.EMPTY_SEQUENCE, null);

        result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testWithCollection() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NodeSetExpression(Arrays.asList(1, 2, 3)), new NodeSetExpression(Arrays.asList(2, 3, 4))) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null),
                new IterateContext(Nodes.createAll(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4)), null), null);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new CompilerContext(new IterateContext(Nodes.createAll(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4)), null), Nodes.EMPTY_SEQUENCE, null);

        result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);

        context = new IterateContext(Nodes.createAll(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 4)), null);

        result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testWithSingletonCollection() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new NodeSetExpression(Arrays.asList(1)), new NodeSetExpression(Arrays.asList(2))) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Context context = new CompilerContext(new IterateContext(Nodes.EMPTY_SEQUENCE, Nodes.EMPTY_SEQUENCE, null), new IterateContext(Nodes.create(1), Nodes.createAll(Collections.singletonList(2), Arrays.asList(3)), null), null);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new IterateContext(Nodes.createAll(Collections.singletonList(1), Arrays.asList(2)), null);

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

}