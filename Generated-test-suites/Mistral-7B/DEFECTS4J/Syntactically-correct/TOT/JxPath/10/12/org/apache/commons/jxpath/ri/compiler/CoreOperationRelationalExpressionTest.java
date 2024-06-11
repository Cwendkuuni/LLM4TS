import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArgs;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CORE_OPERATION_GT;
import static org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CORE_OPERATION_GTE;
import static org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CORE_OPERATION_LT;
import static org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CORE_OPERATION_LTE;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression leftExp = ExpressionFactory.newNumber(1);
        Expression rightExp = ExpressionFactory.newNumber(2);
        CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(CORE_OPERATION_GT, leftExp, rightExp);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(new Object[]{new Integer(1), new Integer(2)});
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(1.0), new Double(2.1)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(2.0), new Double(1.0)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(1), null});
        result = expression.computeValue(context);
        Assert.assertNull(result);

        context = new EvalContext(new Object[]{null, new Integer(1)});
        result = expression.computeValue(context);
        Assert.assertNull(result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression leftExp = ExpressionFactory.newNumber(1);
        Expression rightExp = ExpressionFactory.newNumber(2);
        CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(CORE_OPERATION_GTE, leftExp, rightExp);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(new Object[]{new Integer(1), new Integer(2)});
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(1.0), new Double(2.1)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(2.0), new Double(2.0)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(1), null});
        result = expression.computeValue(context);
        Assert.assertNull(result);

        context = new EvalContext(new Object[]{null, new Integer(1)});
        result = expression.computeValue(context);
        Assert.assertNull(result);

        context = new EvalContext(new Object[]{new Integer(1), new Integer(1)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThan() {
        Expression leftExp = ExpressionFactory.newNumber(1);
        Expression rightExp = ExpressionFactory.newNumber(2);
        CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(CORE_OPERATION_LT, leftExp, rightExp);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(new Object[]{new Integer(1), new Integer(2)});
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(1.0), new Double(2.1)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Double(1.0), new Double(1.0)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new Object[]{new Integer(1), null});
        result = expression.computeValue(context);
        Assert.assertNull(result);

        context = new EvalContext(new Object[]{null, new Integer(1)});
        result = expression.computeValue(context);
        Assert.assertNull(result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression leftExp = ExpressionFactory.newNumber(1);
        Expression rightExp = ExpressionFactory.newNumber(2);
        CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(CORE_OPERATION_LTE, leftExp, rightExp);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(new Object[]{new Integer(1), new Integer(2)});
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(1.0), new Double(2.1)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Double(2.0), new Double(2.0)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{new Integer(1), null});
        result = expression.computeValue(context);
        Assert.assertNull(result);

        context = new EvalContext(new Object[]{null, new Integer(1)});
        result = expression.computeValue(context);
        Assert.assertNull(result);

        context = new EvalContext(new Object[]{new Integer(1), new Integer(1)});
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComparisonWithIterators() {
        Expression leftExp = ExpressionFactory.newList(Arrays.asList(1, 2, 3, 4));
        Expression rightExp = ExpressionFactory.newList(Arrays.asList(2, 3, 4, 5));
        CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(CORE_OPERATION_GTE, leftExp, rightExp);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(Collections.singletonMap("context", expression));
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        context = new EvalContext(Collections.singletonMap("context", expression));
        leftExp = ExpressionFactory.newList(Arrays.asList(1, 2, 3, 4));
        rightExp = ExpressionFactory.newList(Arrays.asList(1, 2, 4, 5));
        args = new CoreOperationRelationalExpressionArgs(CORE_OPERATION_GTE, leftExp, rightExp);
        expression = new CoreOperationRelationalExpression(args);

        context = new EvalContext(Collections.singletonMap("context", expression));
        result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }
}