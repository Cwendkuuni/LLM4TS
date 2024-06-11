import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.core.Context;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoreOperationLessThanOrEqualTest {

    @Test
    void testLessThanOrEqualWithBothNumbersGreaterThan() {
        Expression arg1 = new Const(3.5);
        Expression arg2 = new Const(5.5);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    void testLessThanOrEqualWithBothNumbersEqual() {
        Expression arg1 = new Const(3.5);
        Expression arg2 = new Const(3.5);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    void testLessThanOrEqualWithFirstNumberLessThanSecond() {
        Expression arg1 = new Const(2.5);
        Expression arg2 = new Const(3.5);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    void testLessThanOrEqualWithMixedTypes() {
        Expression arg1 = new Const(3.5);
        Expression arg2 = new Const(4);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    void testLessThanOrEqualWithNaN() {
        Expression arg1 = new Const(Double.NaN);
        Expression arg2 = new Const(4.5);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    void testLessThanOrEqualWithNull() {
        Expression arg1 = new Const(null);
        Expression arg2 = new Const(4.5);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    void testLessThanOrEqualWithNegativeNumbers() {
        Expression arg1 = new Const(-3.5);
        Expression arg2 = new Const(-2.5);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-Double.MAX_VALUE, Double.MAX_VALUE})
    void testLessThanOrEqualWithExtremes(double num) {
        Expression arg1 = new Const(num);
        Expression arg2 = new Const(Double.MIN_VALUE);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Context(new InfoSet()), new NodeInfo(null));
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}