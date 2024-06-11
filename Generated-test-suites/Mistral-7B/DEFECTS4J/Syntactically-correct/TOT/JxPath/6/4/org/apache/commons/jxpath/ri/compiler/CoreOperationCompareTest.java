import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.evaluators.ConstantEvaluator;
import org.apache.commons.jxpath.ri.iterators.EmptyIterator;
import org.apache.commons.jxpath.ri.iterators.SingletonIterator;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.SimpleNodeInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    private EvaluationContext context;
    private CoreOperationCompare compare;
    private CoreOperationConstant leftConst;
    private CoreOperationConstant rightConst;

    @Before
    public void setup() {
        context = new EvaluationContext();
        compare = new CoreOperationCompare(new CoreOperationConstant("1"), new CoreOperationConstant("1"));
        leftConst = new CoreOperationConstant("1");
        rightConst = new CoreOperationConstant("1");
    }

    @Test
    public void testEqual() {
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualWithNulls() {
        compare = new CoreOperationCompare(new CoreOperationConstant(null), new CoreOperationConstant(null));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        compare = new CoreOperationCompare(leftConst, new CoreOperationConstant(1.0));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualWithIterators() {
        Iterator<NodeInfo> leftIter = Arrays.asList(
                new SimpleNodeInfo("1"), new SimpleNodeInfo("2"), new SimpleNodeInfo("3")).iterator();

        Iterator<NodeInfo> rightIter = Arrays.asList(
                new SimpleNodeInfo("1"), new SimpleNodeInfo("2"), new SimpleNodeInfo("3")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorsDifferentSize() {
        Iterator<NodeInfo> leftIter = Arrays.asList(
                new SimpleNodeInfo("1"), new SimpleNodeInfo("2")).iterator();

        Iterator<NodeInfo> rightIter = Arrays.asList(
                new SimpleNodeInfo("1"), new SimpleNodeInfo("2"), new SimpleNodeInfo("3")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorsNull() {
        Iterator<NodeInfo> leftIter = new EmptyIterator<>();
        Iterator<NodeInfo> rightIter = Arrays.asList(new SimpleNodeInfo("1")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorsSameElementDifferentOrder() {
        Iterator<NodeInfo> leftIter = Arrays.asList(new SimpleNodeInfo("2"), new SimpleNodeInfo("1")).iterator();
        Iterator<NodeInfo> rightIter = Arrays.asList(new SimpleNodeInfo("1"), new SimpleNodeInfo("2")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorsSameElementDifferentCase() {
        Iterator<NodeInfo> leftIter = Arrays.asList(
                new SimpleNodeInfo("one"), new SimpleNodeInfo("one")).iterator();
        Iterator<NodeInfo> rightIter = Arrays.asList(
                new SimpleNodeInfo("ONE"), new SimpleNodeInfo("one")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqual() {
        compare = new CoreOperationCompare(leftConst, new CoreOperationConstant("2"));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithNulls() {
        compare = new CoreOperationCompare(new CoreOperationConstant(null), new CoreOperationConstant("1"));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithDifferentTypes() {
        compare = new CoreOperationCompare(leftConst, new CoreOperationConstant(new Object()));
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithIterators() {
        Iterator<NodeInfo> leftIter = Arrays.asList(
                new SimpleNodeInfo("1"), new SimpleNodeInfo("2"), new SimpleNodeInfo("3")).iterator();

        Iterator<NodeInfo> rightIter = Arrays.asList(
                new SimpleNodeInfo("1"), new SimpleNodeInfo("1"), new SimpleNodeInfo("3")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithIteratorsSameElement() {
        Iterator<NodeInfo> leftIter = Arrays.asList(new SimpleNodeInfo("1")).iterator();
        Iterator<NodeInfo> rightIter = Arrays.asList(new SimpleNodeInfo("1")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testNotEqualWithIteratorsNull() {
        Iterator<NodeInfo> leftIter = new EmptyIterator<>();
        Iterator<NodeInfo> rightIter = Arrays.asList(new SimpleNodeInfo("1")).iterator();

        compare = new CoreOperationCompare(new CoreOperationConstant(leftIter), new CoreOperationConstant(rightIter));
        assertTrue(compare.evaluate(context));
    }
}