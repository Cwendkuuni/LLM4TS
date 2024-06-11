package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEquals() {
        CoreOperationCompare op = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)),
            new LiteralExpression(new Integer(1))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));

        Assert.assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNotEquals() {
        CoreOperationCompare op = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)),
            new LiteralExpression(new Integer(2))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));

        Assert.assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualsWithNull() {
        CoreOperationCompare op = new CoreOperationCompare(
            new LiteralExpression((Object) null),
            new LiteralExpression((Object) null)
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));

        Assert.assertTrue(op.evaluate(context).booleanValue());
    }

    @Test(expected = AssertionError.class)
    public void testNotEqualsWithNull() {
        CoreOperationCompare op = new CoreOperationCompare(
            new LiteralExpression((Object) null),
            new LiteralExpression(new Integer(1))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));

        op.evaluate(context);
    }

    @Test
    public void testEqualsWithCollection() {
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("numbers"),
            new LiteralExpression(Collections.singletonList(1))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariables(new Variables());
        context.setVariables().declareVariable("numbers", new ArrayList<>());
        context.getVariables().getVariable("numbers").add(new Integer(1));
        context.getVariables().getVariable("numbers").add(new Integer(2));

        Assert.assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualsWithCollectionEqual() {
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("numbers"),
            new LiteralExpression(Collections.singletonList(1))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariables(new Variables());
        context.setVariables().declareVariable("numbers", new ArrayList<>());
        context.getVariables().getVariable("numbers").add(new Integer(1));

        Assert.assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testContains() {
        CoreOperationCompare op = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)),
            new VariableExpression("numbers")
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariables(new Variables());
        context.setVariables().declareVariable("numbers", Arrays.asList(1, 2, 3));

        Assert.assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testContainsNotPresent() {
        CoreOperationCompare op = new CoreOperationCompare(
            new LiteralExpression(new Integer(4)),
            new VariableExpression("numbers")
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariables(new Variables());
        context.setVariables().declareVariable("numbers", Arrays.asList(1, 2, 3));

        Assert.assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("a"),
            new VariableExpression("b")
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariables(new Variables());
        context.setVariables().declareVariable("a", Arrays.asList(1, 2, 3));
        context.setVariables().declareVariable("b", Arrays.asList(1, 3, 4));

        Assert.assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testFindMatchNotMatching() {
        CoreOperationCompare op = new CoreOperationCompare(
            new VariableExpression("a"),
            new VariableExpression("b")
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariables(new Variables());
        context.setVariables().declareVariable("a", Arrays.asList(1, 2, 3));
        context.setVariables().declareVariable("b", Arrays.asList(2, 3, 4));

        Assert.assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testFindMatchWithSelfContext() {
        CoreOperationCompare op = new CoreOperationCompare(
            new SelfContext(),
            new LiteralExpression(new Integer(1))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));
        context.setVariable("node", new Pointer(new Integer(1)));

        Assert.assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testFindMatchWithInitialContext() {
        CoreOperationCompare op = new CoreOperationCompare(
            new InitialContext(),
            new LiteralExpression(new Integer(1))
        );

        EvalContext context = new EvalContext(new JXPathContext(new InfoSet()));

        Assert.assertFalse(op.evaluate(context).booleanValue());
    }
}