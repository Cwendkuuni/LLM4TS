import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test Class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

    private EvalContext context;
    private CompilerContext compilerContext;

    @Before
    public void setup() {
        compilerContext = new CompilerContext();
        context = new EvalContext(compilerContext);
    }

    /**
     * Test for &gt;
     */
    @Test
    public void testGreaterThan() {
        NodeInfo left = new NodeInfo(1);
        NodeInfo right = new NodeInfo(2);
        CoreOperationRelationalExpression expression =
                new CoreOperationGreaterThanExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for &gt; with NaN
     */
    @Test
    public void testGreaterThanNaN() {
        NodeInfo left = new NodeInfo(Double.NaN);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationGreaterThanExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for &gt; with same number
     */
    @Test
    public void testGreaterThanSameNumber() {
        NodeInfo left = new NodeInfo(1.0);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationGreaterThanExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for &gt;=
     */
    @Test
    public void testGreaterThanOrEqual() {
        NodeInfo left = new NodeInfo(1);
        NodeInfo right = new NodeInfo(1);
        CoreOperationRelationalExpression expression =
                new CoreOperationGreaterThanOrEqualExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for &gt;= with NaN
     */
    @Test
    public void testGreaterThanOrEqualNaN() {
        NodeInfo left = new NodeInfo(Double.NaN);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationGreaterThanOrEqualExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for &gt;= with same number
     */
    @Test
    public void testGreaterThanOrEqualSameNumber() {
        NodeInfo left = new NodeInfo(1.0);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationGreaterThanOrEqualExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for &lt;
     */
    @Test
    public void testLessThan() {
        NodeInfo left = new NodeInfo(1);
        NodeInfo right = new NodeInfo(2);
        CoreOperationRelationalExpression expression =
                new CoreOperationLessThanExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test for &lt; with NaN
     */
    @Test
    public void testLessThanNaN() {
        NodeInfo left = new NodeInfo(Double.NaN);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationLessThanExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for &lt; with same number
     */
    @Test
    public void testLessThanSameNumber() {
        NodeInfo left = new NodeInfo(1.0);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationLessThanExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for &lt;=
     */
    @Test
    public void testLessThanOrEqual() {
        NodeInfo left = new NodeInfo(1);
        NodeInfo right = new NodeInfo(1);
        CoreOperationRelationalExpression expression =
                new CoreOperationLessThanOrEqualExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for &lt;= with NaN
     */
    @Test
    public void testLessThanOrEqualNaN() {
        NodeInfo left = new NodeInfo(Double.NaN);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationLessThanOrEqualExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for &lt;= with same number
     */
    @Test
    public void testLessThanOrEqualSameNumber() {
        NodeInfo left = new NodeInfo(1.0);
        NodeInfo right = new NodeInfo(1.0);
        CoreOperationRelationalExpression expression =
                new CoreOperationLessThanOrEqualExpression(new CoreExpressionNode(left),
                        new CoreExpressionNode(right));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for Iterator comparison - equals
     */
    @Test
    public void testIteratorEquals() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2, 3);
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        CoreOperationRelationalExpression expression =
                new CoreOperationEqualIteratorExpression(new CoreExpressionIteratorNode(it1),
                        new CoreExpressionIteratorNode(it2));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for Iterator comparison - not equals
     */
    @Test
    public void testIteratorNotEquals() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 3, 3);
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        CoreOperationRelationalExpression expression =
                new CoreOperationNotEqualIteratorExpression(new CoreExpressionIteratorNode(it1),
                        new CoreExpressionIteratorNode(it2));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test for Iterator comparison - contains
     */
    @Test
    public void testIteratorContains() {
        List list = Arrays.asList(1, 2, 3);
        Iterator it = list.iterator();

        CoreOperationRelationalExpression expression =
                new CoreOperationContainsIteratorExpression(new CoreExpressionIteratorNode(it),
                        new CoreExpressionNode(2));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test for Iterator comparison - does not contain
     */
    @Test
    public void testIteratorDoesNotContain() {
        List list = Arrays.asList(1, 2, 3);
        Iterator it = list.iterator();

        CoreOperationRelationalExpression expression =
                new CoreOperationDoesNotContainIteratorExpression(new CoreExpressionIteratorNode(it),
                        new CoreExpressionNode(4));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test for Iterator comparison - intersection empty
     */
    @Test
    public void testIteratorIntersectionEmpty() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Collections.emptyList();
        Iterator it1 = list1.iterator();
        Iterator it2 = Collections.emptyIterator();

        CoreOperationRelationalExpression expression =
                new CoreOperationIntersectionIteratorExpression(new CoreExpressionIteratorNode(it1),
                        new CoreExpressionIteratorNode(it2));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test for Iterator comparison - intersection not empty
     */
    @Test
    public void testIteratorIntersectionNotEmpty() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(2, 3, 4);
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        CoreOperationRelationalExpression expression =
                new CoreOperationIntersectionIteratorExpression(new CoreExpressionIteratorNode(it1),
                        new CoreExpressionIteratorNode(it2));

        Object result = expression.compile(context).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}