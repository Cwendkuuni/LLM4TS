package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGTExpression() {
        Expression left = new NumberValue(1);
        Expression right = new NumberValue(2);
        CoreOperationRelationalExpression gtExpression = new CoreGTExpression(new CoreOperation[] {left, right});

        JXPathContext context = new JXPathContext();
        assertTrue(gtExpression.evaluate(context).booleanValue());

        left = new NumberValue(2);
        right = new NumberValue(1);
        assertFalse(gtExpression.evaluate(context).booleanValue());
    }

    @Test
    public void testGTEExpression() {
        Expression left = new NumberValue(1);
        Expression right = new NumberValue(2);
        CoreOperationRelationalExpression gteExpression = new CoreGTEExpression(new CoreOperation[] {left, right});

        JXPathContext context = new JXPathContext();
        assertTrue(gteExpression.evaluate(context).booleanValue());

        left = new NumberValue(1);
        right = new NumberValue(1);
        assertTrue(gteExpression.evaluate(context).booleanValue());

        left = new NumberValue(2);
        right = new NumberValue(1);
        assertFalse(gteExpression.evaluate(context).booleanValue());
    }

    @Test
    public void testLTExpression() {
        Expression left = new NumberValue(1);
        Expression right = new NumberValue(2);
        CoreOperationRelationalExpression ltExpression = new CoreLTExpression(new CoreOperation[] {left, right});

        JXPathContext context = new JXPathContext();
        assertFalse(ltExpression.evaluate(context).booleanValue());

        left = new NumberValue(2);
        right = new NumberValue(1);
        assertTrue(ltExpression.evaluate(context).booleanValue());
    }

    @Test
    public void testLTEExpression() {
        Expression left = new NumberValue(1);
        Expression right = new NumberValue(2);
        CoreOperationRelationalExpression lteExpression = new CoreLTEExpression(new CoreOperation[] {left, right});

        JXPathContext context = new JXPathContext();
        assertFalse(lteExpression.evaluate(context).booleanValue());

        left = new NumberValue(1);
        right = new NumberValue(1);
        assertTrue(lteExpression.evaluate(context).booleanValue());

        left = new NumberValue(2);
        right = new NumberValue(1);
        assertFalse(lteExpression.evaluate(context).booleanValue());

        left = new StringValue("a");
        right = new StringValue("z");
        assertFalse(lteExpression.evaluate(context).booleanValue());

        left = new StringValue("z");
        right = new StringValue("a");
        assertTrue(lteExpression.evaluate(context).booleanValue());
    }

}