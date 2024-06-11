package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Test;
import org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThanDoubleNumbers() {
        // Test valid less than scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("5.0"));
        Expression arg2 = new LiteralExpression(new BigDecimal("7.0"));
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        InfoSet infoSet = new InfoSet();
        infoSet.setRoot(new NodeInfo(new JXPathContext(null, null), new Path("/"), null, null, null, null, null));

        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new BigDecimal("4.5"));
        arg2 = new LiteralExpression(new BigDecimal("7.0"));
        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(new BigDecimal("3.5"));
        arg2 = new LiteralExpression(new BigDecimal("7.0"));
        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new BigDecimal("2.5"));
        arg2 = new LiteralExpression(new BigDecimal("7.0"));
        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new BigDecimal("1.5"));
        arg2 = new LiteralExpression(new BigDecimal("7.0"));
        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanNullAndDoubleNumber() {
        // Test less than null and double number scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("5.0"));
        Expression arg2 = new NullExpression();

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanDoubleNumberAndNull() {
        // Test double number and less than null scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("5.0"));
        Expression arg2 = new NullExpression();

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg2, arg1);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNulls() {
        // Test less than two nulls scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new NullExpression();
        Expression arg2 = new NullExpression();

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithSameValue() {
        // Test less than two numbers with same value scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("5.0"));
        Expression arg2 = new LiteralExpression(new BigDecimal("5.0"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithEqualValueButDifferentTypes() {
        // Test less than two numbers with equal value but different types scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("5.0"));
        Expression arg2 = new LiteralExpression(new Integer(5));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithEqualValueButDifferentSigns() {
        // Test less than two numbers with equal value but different signs scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("-5.0"));
        Expression arg2 = new LiteralExpression(new BigDecimal("5.0"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithOneNegativeAndOnePositive() {
        // Test less than two numbers with one negative and one positive scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("-5.0"));
        Expression arg2 = new LiteralExpression(new BigDecimal("3.0"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithOneNegativeAndOneZero() {
        // Test less than two numbers with one negative and one zero scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("-5.0"));
        Expression arg2 = new LiteralExpression(new Integer(0));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithOneZeroAndOnePositive() {
        // Test less than two numbers with one zero and one positive scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new Integer(0));
        Expression arg2 = new LiteralExpression(new BigDecimal("5.0"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithOneZeroAndOneNegative() {
        // Test less than two numbers with one zero and one negative scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new Integer(0));
        Expression arg2 = new LiteralExpression(new BigDecimal("-5.0"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithOneNaN() {
        // Test less than two numbers with one NaN scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("NaN"));
        Expression arg2 = new LiteralExpression(new BigDecimal("5.0"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoNumbersWithBothNaN() {
        // Test less than two numbers with both NaN scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(new BigDecimal("NaN"));
        Expression arg2 = new LiteralExpression(new BigDecimal("NaN"));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoStrings() {
        // Test less than two strings scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression("apple");
        Expression arg2 = new LiteralExpression("banana");

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoStringsWithOneEmpty() {
        // Test less than two strings with one empty scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression("");
        Expression arg2 = new LiteralExpression("apple");

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanTwoStringsWithOneNull() {
        // Test less than two strings with one null scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new NullExpression();
        Expression arg2 = new LiteralExpression("apple");

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression("apple");
        arg2 = new NullExpression();

        lessThan = new CoreOperationLessThan(arg1, arg2);
        Object result2 = lessThan.computeValue(context);

        assertTrue(result2 instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result2);
    }

    @Test
    public void testLessThanTwoBooleans() {
        // Test less than two booleans scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(Boolean.TRUE);
        Expression arg2 = new LiteralExpression(Boolean.TRUE);

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanTwoBooleansWithOneFalse() {
        // Test less than two booleans with one false scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(Boolean.TRUE);
        Expression arg2 = new LiteralExpression(Boolean.FALSE);

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanTwoBooleansWithBothFalse() {
        // Test less than two booleans with both false scenario
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(Boolean.FALSE);
        Expression arg2 = new LiteralExpression(Boolean.FALSE);

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        Object result = lessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }
}