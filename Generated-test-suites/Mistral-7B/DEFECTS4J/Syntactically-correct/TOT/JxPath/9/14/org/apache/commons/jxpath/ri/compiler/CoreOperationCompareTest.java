import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class CoreOperationCompareTest extends CoreOperationTest {

    @Test
    public void testEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            new ConstantExpression(new Integer(5))
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            new ConstantExpression(new Integer(6))
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testEqualCollection() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            new ConstantExpression(new IntegerCollectionExpression(Arrays.asList(new Integer[]{5, 5, 5})))
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testNotEqualCollection() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            new ConstantExpression(new IntegerCollectionExpression(Arrays.asList(new Integer[]{5, 6, 5})))
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testEqualIterator() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            new IteratorExpression(new ArrayList<Integer>(Arrays.asList(new Integer[]{5, 5, 5})).iterator())
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testNotEqualIterator() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            new IteratorExpression(new ArrayList<Integer>(Arrays.asList(new Integer[]{5, 5, 6})).iterator())
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test(expected = IllegalStateException.class)
    public void testLeftNull() {
        CoreOperationCompare op = new CoreOperationCompare(
            null,
            new ConstantExpression(new Integer(5))
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        fail("Should have thrown an IllegalStateException");
    }

    @Test(expected = IllegalStateException.class)
    public void testRightNull() {
        CoreOperationCompare op = new CoreOperationCompare(
            new ConstantExpression(new Integer(5)),
            null
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        fail("Should have thrown an IllegalStateException");
    }

    @Test
    public void testSelfContext() {
        CoreOperationCompare op = new CoreOperationCompare(
            new SelfContextExpression(),
            new SelfContextExpression()
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testNotEqualSelfContext() {
        CoreOperationCompare op = new CoreOperationCompare(
            new SelfContextExpression(),
            new SelfContextExpression()
        );
        EvalContext context = new EvalContext();
        context.setCurrentNode(new Pointer("notSelf"));
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testInitialContext() {
        CoreOperationCompare op = new CoreOperationCompare(
            new InitialContextExpression(),
            new InitialContextExpression()
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testNotEqualInitialContext() {
        CoreOperationCompare op = new CoreOperationCompare(
            new InitialContextExpression(),
            new InitialContextExpression()
        );
        EvalContext context = new EvalContext();
        context.setCurrentNode(new Pointer("notInitial"));
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testBooleanTrue() {
        CoreOperationCompare op = new CoreOperationCompare(
            new BooleanConstantExpression(true),
            new BooleanConstantExpression(true)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testBooleanFalse() {
        CoreOperationCompare op = new CoreOperationCompare(
            new BooleanConstantExpression(false),
            new BooleanConstantExpression(false)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testBooleanFalseAndTrue() {
        CoreOperationCompare op = new CoreOperationCompare(
            new BooleanConstantExpression(false),
            new BooleanConstantExpression(true)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testStringEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new StringConstantExpression("foo"),
            new StringConstantExpression("foo")
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testStringNotEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new StringConstantExpression("foo"),
            new StringConstantExpression("bar")
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testNumberEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new NumberConstantExpression(1.0),
            new NumberConstantExpression(1.0)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test
    public void testNumberNotEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new NumberConstantExpression(1.0),
            new NumberConstantExpression(2.0)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testNaNEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new NumberConstantExpression(Double.NaN),
            new NumberConstantExpression(Double.NaN)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testNaNNotEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new NumberConstantExpression(Double.NaN),
            new NumberConstantExpression(1.0)
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == false);
    }

    @Test
    public void testNullEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            null,
            null
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        assertTrue(result == true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAndNotNull() {
        CoreOperationCompare op = new CoreOperationCompare(
            null,
            new StringConstantExpression("foo")
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        fail("Should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotNullAndNull() {
        CoreOperationCompare op = new CoreOperationCompare(
            new StringConstantExpression("foo"),
            null
        );

        EvalContext context = new EvalContext();
        Object result = op.evaluate(context);
        fail("Should have thrown an IllegalArgumentException");
    }

}