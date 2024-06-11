package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Evaluator;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationFactory;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.JXPathCompiler;
import org.apache.commons.jxpath.ri.compiler.NodeSet;
import org.apache.commons.jxpath.ri.compiler.NodeSetFactory;
import org.apache.commons.jxpath.ri.compiler.StaticContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private JXPathCompiler compiler;
    private CoreOperationCompareFactory compareFactory;
    private ExpressionFactory expressionFactory;
    private NodeSetFactory nodeSetFactory;

    @Before
    public void init() {
        this.compiler = new JXPathCompiler();
        this.compareFactory = (CoreOperationCompareFactory) this.compiler
            .getOperationFactory().getOperationFactory(
                CoreOperationCompare.class);
        this.expressionFactory = this.compiler.getExpressionFactory();
        this.nodeSetFactory = this.compiler.getNodeSetFactory();
    }

    @Test
    public void testCompareBoolean() {
        Expression left = this.expressionFactory.createSimplePathExpression("/true");
        Expression right = this.expressionFactory.createSimplePathExpression("/false");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] { evaluator.evaluate(left, context) });
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertTrue(result);
    }

    @Test
    public void testCompareBooleanTrue() {
        Expression left = this.expressionFactory.createSimplePathExpression("/true");
        Expression right = this.expressionFactory.createSimplePathExpression("/true");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] { evaluator.evaluate(left, context) });
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertTrue(result);
    }

    @Test
    public void testCompareBooleanFalse() {
        Expression left = this.expressionFactory.createSimplePathExpression("/true");
        Expression right = this.expressionFactory.createSimplePathExpression("/false");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext1 = JXPathContext.newContext(context);
        jxpathContext1.setValues(new Object[] { evaluator.evaluate(left, context) });
        JXPathContext jxpathContext2 = JXPathContext.newContext(context);
        jxpathContext2.setValues(new Object[] { evaluator.evaluate(right, context) });
        boolean result1 = compare.evaluateToBoolean(jxpathContext1);
        boolean result2 = compare.evaluateToBoolean(jxpathContext2);
        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    public void testCompareNumber() {
        Expression left = this.expressionFactory.createSimplePathExpression("/1");
        Expression right = this.expressionFactory.createSimplePathExpression("/2");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] { evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertFalse(result);
    }

    @Test
    public void testCompareNumberEqual() {
        Expression left = this.expressionFactory.createSimplePathExpression("/1");
        Expression right = this.expressionFactory.createSimplePathExpression("/1");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] { evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertTrue(result);
    }

    @Test
    public void testCompareNumberNotEqual() {
        Expression left = this.expressionFactory.createSimplePathExpression("/1");
        Expression right = this.expressionFactory.createSimplePathExpression("/2");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext1 = JXPathContext.newContext(context);
        jxpathContext1.setValues(new Object[] { evaluator.evaluate(left, context) });
        JXPathContext jxpathContext2 = JXPathContext.newContext(context);
        jxpathContext2.setValues(new Object[] { evaluator.evaluate(right, context) });
        boolean result1 = compare.evaluateToBoolean(jxpathContext1);
        boolean result2 = compare.evaluateToBoolean(jxpathContext2);
        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    public void testCompareString() {
        Expression left = this.expressionFactory.createSimplePathExpression("/'abc'");
        Expression right = this.expressionFactory.createSimplePathExpression("/'def'");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] { evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertFalse(result);
    }

    @Test
    public void testCompareStringEqual() {
        Expression left = this.expressionFactory.createSimplePathExpression("/'abc'");
        Expression right = this.expressionFactory.createSimplePathExpression("/'abc'");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] { evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertTrue(result);
    }

    @Test
    public void testCompareStringNotEqual() {
        Expression left = this.expressionFactory.createSimplePathExpression("/'abc'");
        Expression right = this.expressionFactory.createSimplePathExpression("/'def'");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext1 = JXPathContext.newContext(context);
        jxpathContext1.setValues(new Object[] { evaluator.evaluate(left, context) });
        JXPathContext jxpathContext2 = JXPathContext.newContext(context);
        jxpathContext2.setValues(new Object[] { evaluator.evaluate(right, context) });
        boolean result1 = compare.evaluateToBoolean(jxpathContext1);
        boolean result2 = compare.evaluateToBoolean(jxpathContext2);
        assertFalse(result1);
        assertFalse(result2);
    }

    @Test
    public void testCompareCollection() {
        Expression left = this.expressionFactory.createSimplePathExpression("/1/..");
        Expression right = this.expressionFactory.createSimplePathExpression("/2/..");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] {
            evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        NodeSet leftNodeSet = (NodeSet) evaluator.evaluate(left, context);
        NodeSet rightNodeSet = (NodeSet) evaluator.evaluate(right, context);
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertFalse(result);
    }

    @Test
    public void testCompareCollectionEqual() {
        Expression left = this.expressionFactory
            .createSimplePathExpression("/1/../ancestor::node()[1]");
        Expression right = this.expressionFactory
            .createSimplePathExpression("/1/../ancestor::node()[1]");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        jxpathContext.setValues(new Object[] {
            evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        NodeSet leftNodeSet = (NodeSet) evaluator.evaluate(left, context);
        NodeSet rightNodeSet = (NodeSet) evaluator.evaluate(right, context);
        boolean result = compare.evaluateToBoolean(jxpathContext);
        assertTrue(result);
    }

    @Test
    public void testCompareCollectionNotEqual() {
        Expression left = this.expressionFactory
            .createSimplePathExpression("/1/../ancestor::node()[1]");
        Expression right = this.expressionFactory
            .createSimplePathExpression("/2/../ancestor::node()[1]");
        CoreOperationCompare compare = this.compareFactory.createOperationCompare(
            left, right);
        StaticContext context = new StaticContext();
        Evaluator evaluator = this.compiler.getEvaluator();
        JXPathContext jxpathContext1 = JXPathContext.newContext(context);
        jxpathContext1.setValues(new Object[] {
            evaluator.evaluate(left, context),
            evaluator.evaluate(right, context) });
        NodeSet leftNodeSet = (NodeSet) evaluator.evaluate(left, context);
        NodeSet rightNodeSet = (NodeSet) evaluator.evaluate(right, context);
        boolean result1 = compare.evaluateToBoolean(jxpathContext1);
        boolean result2 = compare.evaluateToBoolean(jxpathContext1);
        assertFalse(result1);
        assertFalse(result2);
    }

}