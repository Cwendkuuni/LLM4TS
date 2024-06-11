import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() throws EvaluationException {
        JXPathContext context = new JXPathContext(new Object[]{"1", "1"});
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(1), new ConstantExpression(1));
        Assert.assertTrue(op.compute(context, null).toString(), op.compute(context, null).booleanValue());
    }

    @Test
    public void testNotEqual() throws EvaluationException {
        JXPathContext context = new JXPathContext(new Object[]{"1", "2"});
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(1), new ConstantExpression(2));
        Assert.assertFalse(op.compute(context, null).toString(), op.compute(context, null).booleanValue());
    }

    @Test
    public void testEqualCollections() throws EvaluationException {
        JXPathContext context = new JXPathContext(new Object[]{
            new ArrayList<>(Collections.singletonList(1)),
            new ArrayList<>(Collections.singletonList(1))
        });
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new ArrayList<>(Collections.singletonList(1))),
                new ConstantExpression(new ArrayList<>(Collections.singletonList(1))));
        Assert.assertTrue(op.compute(context, null).toString(), op.compute(context, null).booleanValue());
    }

    @Test
    public void testNotEqualCollections() throws EvaluationException {
        JXPathContext context = new JXPathContext(new Object[]{
            new ArrayList<>(Collections.singletonList(1)),
            new ArrayList<>(Collections.singletonList(2))
        });
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(new ArrayList<>(Collections.singletonList(1))),
                new ConstantExpression(new ArrayList<>(Collections.singletonList(2))));
        Assert.assertFalse(op.compute(context, null).toString(), op.compute(context, null).booleanValue());
    }

    @Test(expected = EvaluationException.class)
    public void testDifferentNumberOfOperands() throws EvaluationException {
        new CoreOperationCompare(
                new ConstantExpression(1),
                new ConstantExpression(1),
                new ConstantExpression(2));
    }

    @Test(expected = EvaluationException.class)
    public void testNullLeftOperand() throws EvaluationException {
        new CoreOperationCompare(
                null,
                new ConstantExpression(1));
    }

    @Test(expected = EvaluationException.class)
    public void testNullRightOperand() throws EvaluationException {
        new CoreOperationCompare(
                new ConstantExpression(1),
                null);
    }

    @Test(expected = EvaluationException.class)
    public void testMismatchedTypes() throws EvaluationException {
        new CoreOperationCompare(
                new ConstantExpression(1),
                new ConstantExpression("str"));
    }
}