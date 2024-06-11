import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationFactor;
import org.apache.commons.jxpath.ri.compiler.EvalContext;
import org.apache.commons.jxpath.ri.Expressions;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.jxpath.ri.Functions.booleanValue;
import static org.apache.commons.jxpath.ri.Functions.isBoolean;
import static org.apache.commons.jxpath.ri.Functions.isNumber;
import static org.apache.commons.jxpath.ri.Functions.isString;
import static org.apache.commons.jxpath.ri.Functions.stringValue;

/**
 * Test cases for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    /**
     * Test equal boolean values.
     */
    @Test
    public void testEqualBooleanValues() {
        Expression arg1 = Expressions.constant(Boolean.TRUE);
        Expression arg2 = Expressions.constant(Boolean.TRUE);
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test equal numbers.
     */
    @Test
    public void testEqualNumbers() {
        Expression arg1 = Expressions.constant(1);
        Expression arg2 = Expressions.constant(1);
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test not equal numbers.
     */
    @Test
    public void testNotEqualNumbers() {
        Expression arg1 = Expressions.constant(1);
        Expression arg2 = Expressions.constant(2);
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test equal strings.
     */
    @Test
    public void testEqualStrings() {
        Expression arg1 = Expressions.constant("foo");
        Expression arg2 = Expressions.constant("foo");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test not equal strings.
     */
    @Test
    public void testNotEqualStrings() {
        Expression arg1 = Expressions.constant("foo");
        Expression arg2 = Expressions.constant("bar");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test equal strings with different casing.
     */
    @Test
    public void testEqualStringsWithDifferentCasing() {
        Expression arg1 = Expressions.constant("foo");
        Expression arg2 = Expressions.constant("FOO");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test not equal null and non-null values.
     */
    @Test
    public void testNotEqualNullAndNonnullValues() {
        Expression arg1 = Expressions.constant(null);
        Expression arg2 = Expressions.constant("foo");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test equal factors.
     */
    @Test
    public void testEqualFactors() {
        Expression arg1 = new CoreOperationFactor(Expressions.constant(1), Functions.add, Expressions.constant(1));
        Expression arg2 = new CoreOperationFactor(Expressions.constant(1), Functions.add, Expressions.constant(1));
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test not equal factors.
     */
    @Test
    public void testNotEqualFactors() {
        Expression arg1 = new CoreOperationFactor(Expressions.constant(1), Functions.add, Expressions.constant(1));
        Expression arg2 = new CoreOperationFactor(Expressions.constant(1), Functions.add, Expressions.constant(2));
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test equal with different types.
     */
    @Test(expected = ClassCastException.class)
    public void testEqualWithDifferentTypes() {
        Expression arg1 = Expressions.constant(1);
        Expression arg2 = Expressions.constant("1");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        Assert.fail("Expected ClassCastException");
    }

    /**
     * Test context.
     */
    @Test
    public void testContext() {
        Expression arg1 = Expressions.variable("x");
        Expression arg2 = Expressions.variable("y");
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.put("x", 1);
        context.put("y", 1);
        Object result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        context.put("x", 1);
        context.put("y", 2);
        result = operationEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }
}