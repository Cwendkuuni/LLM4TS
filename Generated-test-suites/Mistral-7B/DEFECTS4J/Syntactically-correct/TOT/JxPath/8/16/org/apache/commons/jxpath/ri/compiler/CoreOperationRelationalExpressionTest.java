import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg2;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.InitialContext;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.apache.commons.jxpath.ri.compiler.TestHelper;
import org.apache.commons.jxpath.ri.evaluator.EvalContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        arg2 = ExpressionFactory.createNumber(0.5);
        context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThan() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        arg2 = ExpressionFactory.createNumber(2.0);
        context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsGreaterThan() {
        Expression arg1 = ExpressionFactory.createIterator(TestHelper.createList(1, 2, 3, 4));
        Expression arg2 = ExpressionFactory.createNumber(3);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIteratorsGreaterThanOrEqual() {
        Expression arg1 = ExpressionFactory.createIterator(TestHelper.createList(1, 2, 3, 4));
        Expression arg2 = ExpressionFactory.createNumber(3);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        arg2 = ExpressionFactory.createNumber(1);
        context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsLessThan() {
        Expression arg1 = ExpressionFactory.createIterator(TestHelper.createList(1, 2, 3, 4));
        Expression arg2 = ExpressionFactory.createNumber(3);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsLessThanOrEqual() {
        Expression arg1 = ExpressionFactory.createIterator(TestHelper.createList(1, 2, 3, 4));
        Expression arg2 = ExpressionFactory.createNumber(3);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);

        arg2 = ExpressionFactory.createNumber(1);
        context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDoublesGreaterThan() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDoublesGreaterThanOrEqual() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(1.5);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        arg2 = ExpressionFactory.createNumber(2.0);
        context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDoublesLessThan() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDoublesLessThanOrEqual() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(1.5);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        arg2 = ExpressionFactory.createNumber(1.4);
        context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNullWithIterators() {
        Expression arg1 = ExpressionFactory.createIterator(TestHelper.createList(null));
        Expression arg2 = ExpressionFactory.createNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNullWithDoubles() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNull();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createString("test");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionarg1(arg1), new CoreOperationRelationalExpressionarg2(arg2));

        EvalContext context = new EvalContext(new InitialContext("test"), new SelfContext(null), Collections.emptyList());
        Object result = expression.computeValue(context);
    }
}