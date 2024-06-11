package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.SimpleValue;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreExpression arg1 = new CoreConstantExpression(new SimpleValue(5.0, InfoSet.NUMBER_TYPE));
        CoreExpression arg2 = new CoreConstantExpression(new SimpleValue(10.0, InfoSet.NUMBER_TYPE));

        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg2 = new CoreConstantExpression(new SimpleValue(5.0, InfoSet.NUMBER_TYPE));

        context = new EvalContext(new InfoSet());

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualInvalidArguments() {
        CoreExpression arg1 = new CoreConstantExpression(new SimpleValue(5.0, InfoSet.NUMBER_TYPE));
        CoreExpression arg2 = null;

        new CoreOperationLessThanOrEqual(arg1, arg2);
    }
}