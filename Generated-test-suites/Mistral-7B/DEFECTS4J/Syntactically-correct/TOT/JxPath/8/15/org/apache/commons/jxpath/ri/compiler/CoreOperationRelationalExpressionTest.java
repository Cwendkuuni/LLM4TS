import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg2;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Integer leftValue = 5;
        Integer rightValue = 6;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThan_False() {
        Integer leftValue = 5;
        Integer rightValue = 5;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Integer leftValue = 5;
        Integer rightValue = 6;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThanOrEqual_True() {
        Integer leftValue = 5;
        Integer rightValue = 5;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThan() {
        Integer leftValue = 5;
        Integer rightValue = 6;

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThan_True() {
        Integer leftValue = 5;
        Integer rightValue = 4;

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        Integer leftValue = 5;
        Integer rightValue = 6;

        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqual_True() {
        Integer leftValue = 5;
        Integer rightValue = 5;

        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCompare_Doubles_Equals() {
        double leftValue = 3.14;
        double rightValue = 3.14;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCompare_Doubles_NotEquals() {
        double leftValue = 3.14;
        double rightValue = 3.15;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(
                new CoreOperationRelationalExpressionarg1(new ExpressionConstant(leftValue)),
                new CoreOperationRelationalExpressionarg2(new ExpressionConstant(rightValue))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompare_Iterators() {
        Integer[] leftValues = {1, 2, 3};
        Integer[] rightValues = {1, 2, 3, 4};

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionList(Arrays.asList(leftValues))),
                new CoreOperationRelationalExpressionarg2(new ExpressionList(Arrays.asList(rightValues)))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCompare_Iterators_True() {
        Integer[] leftValues = {1, 2, 3};
        Integer[] rightValues = {1, 2};

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionList(Arrays.asList(leftValues))),
                new CoreOperationRelationalExpressionarg2(new ExpressionList(Arrays.asList(rightValues)))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCompare_Collections() {
        List<Integer> leftValues = Arrays.asList(1, 2, 3);
        List<Integer> rightValues = Collections.singletonList(1);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionList(leftValues)),
                new CoreOperationRelationalExpressionarg2(new ExpressionList(rightValues))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCompare_Collections_False() {
        List<Integer> leftValues = Arrays.asList(1, 2, 3);
        List<Integer> rightValues = Arrays.asList(1, 2, 3);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreOperationRelationalExpressionarg1(new ExpressionList(leftValues)),
                new CoreOperationRelationalExpressionarg2(new ExpressionList(rightValues))
        );

        EvalContext context = Mockito.spy(new EvalContext());

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}