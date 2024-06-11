import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression e1 = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(3));
        JXPathContext jxpathContext = JXPathContextFactory.newContext();
        jxpathContext.setThis("10");

        boolean result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("5");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertFalse(result);

        jxpathContext.setThis("6");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("4");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertFalse(result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression e1 = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(6));
        JXPathContext jxpathContext = JXPathContextFactory.newContext();

        boolean result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertFalse(result);

        jxpathContext.setThis("5");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("6");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("7");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testLessThan() {
        Expression e1 = new CoreOperationRelationalExpression(new CoreNumber(3), new CoreNumber(5));
        JXPathContext jxpathContext = JXPathContextFactory.newContext();

        boolean result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("4");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("5");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertFalse(result);

        jxpathContext.setThis("6");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertFalse(result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression e1 = new CoreOperationRelationalExpression(new CoreNumber(3), new CoreNumber(5));
        JXPathContext jxpathContext = JXPathContextFactory.newContext();

        boolean result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertFalse(result);

        jxpathContext.setThis("2");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("3");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);

        jxpathContext.setThis("4");
        result = e1.evaluate(jxpathContext) instanceof Boolean && ((Boolean) e1.evaluate(jxpathContext)).booleanValue();
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() {
        Expression e1 = new CoreOperationRelationalExpression(null, new CoreNumber(3));
        JXPathContext jxpathContext = JXPathContextFactory.newContext();
        e1.evaluate(jxpathContext);
    }
}