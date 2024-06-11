import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.NumberInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreOperationGreaterThanTest {

    @ParameterizedTest
    @ValueSource(doubles = {1.0d, 2.0d})
    public void testValidGreaterThan(double arg1, double arg2) {
        Expression arg1Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg1));
        Expression arg2Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg2));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1Expr, arg2Expr);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0d, 1.0d})
    public void testInvalidGreaterThan(double arg1, double arg2) {
        Expression arg1Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg1));
        Expression arg2Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg2));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1Expr, arg2Expr);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0d, -1.0d})
    public void testGreaterThanWithNegativeNumber(double arg1, double arg2) {
        Expression arg1Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg1));
        Expression arg2Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg2));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1Expr, arg2Expr);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(arg1 > arg2 ? Boolean.TRUE : Boolean.FALSE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.NaN})
    public void testGreaterThanWithNaN(double arg1, double arg2) {
        Expression arg1Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg1));
        Expression arg2Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg2));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1Expr, arg2Expr);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.MAX_VALUE, Double.MAX_VALUE})
    public void testGreaterThanWithMaxValues(double arg1, double arg2) {
        Expression arg1Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg1));
        Expression arg2Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg2));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1Expr, arg2Expr);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0d, Double.MIN_VALUE})
    public void testGreaterThanWithMinValue(double arg1, double arg2) {
        Expression arg1Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg1));
        Expression arg2Expr = new ConstantExpression(new NumberInfo(new InfoSet(), arg2));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1Expr, arg2Expr);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertEquals(arg1 > arg2 ? Boolean.TRUE : Boolean.FALSE, result);
    }
}