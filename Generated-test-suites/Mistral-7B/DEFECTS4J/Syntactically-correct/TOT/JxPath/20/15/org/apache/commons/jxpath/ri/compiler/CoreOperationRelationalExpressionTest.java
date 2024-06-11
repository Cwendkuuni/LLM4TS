package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterateiter;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.PathNotFoundException;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.iter.EmptyIterator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGt() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new ConstantExpression(5);

        CoreOperationRelationalExpression gt = new CoreOperationGT(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();
        context.setVariable("num1", new Integer(3));

        Object result = gt.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Integer(5));

        result = gt.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Integer(4));

        result = gt.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Double(5.0));
        right = new ConstantExpression(5.0);

        gt = new CoreOperationGT(new CoreExpression[] {left, right});

        result = gt.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Double(4.9));

        result = gt.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Double(5.1));

        result = gt.compute(context);
        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    public void testGte() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new ConstantExpression(5);

        CoreOperationRelationalExpression gte = new CoreOperationGTE(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();
        context.setVariable("num1", new Integer(3));

        Object result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Integer(5));

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Integer(4));

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Double(4.9));

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Double(4.8));

        result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Double(5.1));

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    public void testLt() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new ConstantExpression(5);

        CoreOperationRelationalExpression lt = new CoreOperationLT(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();
        context.setVariable("num1", new Integer(3));

        Object result = lt.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Integer(5));

        result = lt.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Integer(4));

        result = lt.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Double(5.0));
        right = new ConstantExpression(5.0);

        lt = new CoreOperationLT(new CoreExpression[] {left, right});

        result = lt.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Double(4.9));

        result = lt.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Double(5.1));

        result = lt.compute(context);
        assertThat(result, is(Boolean.FALSE));
    }

    @Test
    public void testLte() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new ConstantExpression(5);

        CoreOperationRelationalExpression lte = new CoreOperationLTE(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();
        context.setVariable("num1", new Integer(3));

        Object result = lte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Integer(5));

        result = lte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Integer(4));

        result = lte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Double(5.1));

        result = lte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", new Double(5.0));

        result = lte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", new Double(4.9));

        result = lte.compute(context);
        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    public void testComparisonWithIterators() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new VariableExpression("$num2");

        CoreOperationRelationalExpression gte = new CoreOperationGTE(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();

        Iterator<NodeInfo> leftIterator = TestUtils.createNodeList(context).iterator();
        Iterator<NodeInfo> rightIterator = TestUtils.createNodeList(context, new Object[] {1, 2, 3, 4, 5}).iterator();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        Object result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        context.setVariable("num1", leftIterator);

        Iterateiter leftIt = context.getVariable("num1").iterator();
        leftIt.next();

        context.setVariable("num2", rightIterator);

        Iterateiter rightIt = context.getVariable("num2").iterator();
        rightIt.next();

        leftIt.next();
        rightIt.next();

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        context.setVariable("num1", leftIterator);

        leftIt.next();
        rightIt.next();

        leftIt.next();
        rightIt.next();

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));

        leftIterator = TestUtils.createEmptyNodeList(context).iterator();
        rightIterator = TestUtils.createNodeList(context, new Object[] {1, 2, 3, 4, 5}).iterator();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        leftIterator = TestUtils.createNodeList(context).iterator();
        rightIterator = TestUtils.createEmptyNodeList(context).iterator();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        leftIterator = TestUtils.createEmptyNodeList(context).iterator();
        rightIterator = TestUtils.createEmptyNodeList(context).iterator();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    public void testComparisonWithEmptyIterators() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new VariableExpression("$num2");

        CoreOperationRelationalExpression gte = new CoreOperationGTE(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();

        Iterator<NodeInfo> leftIterator = TestUtils.createEmptyNodeList(context).iterator();
        Iterator<NodeInfo> rightIterator = TestUtils.createNodeList(context, new Object[] {1, 2, 3, 4, 5}).iterator();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        Object result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        leftIterator = TestUtils.createNodeList(context).iterator();
        rightIterator = EmptyIterator.getInstance();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        leftIterator = EmptyIterator.getInstance();
        rightIterator = TestUtils.createNodeList(context, new Object[] {1, 2, 3, 4, 5}).iterator();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        result = gte.compute(context);
        assertThat(result, is(Boolean.FALSE));

        leftIterator = TestUtils.createEmptyNodeList(context).iterator();
        rightIterator = EmptyIterator.getInstance();

        context.setVariable("num1", leftIterator);
        context.setVariable("num2", rightIterator);

        result = gte.compute(context);
        assertThat(result, is(Boolean.TRUE));
    }

    @Test(expected = PathNotFoundException.class)
    public void testComparisonWithMissingVariables() throws Exception {
        Expression left = new VariableExpression("$num1");
        Expression right = new VariableExpression("$num3");

        CoreOperationRelationalExpression gte = new CoreOperationGTE(new CoreExpression[] {left, right});

        EvalContext context = TestUtils.getEvalContext();
        context.setVariable("num1", new Integer(5));

        gte.compute(context);
    }
}