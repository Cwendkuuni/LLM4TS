import org.apache.commons.jxpath.ri.AbstractEvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.Variable;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@code CoreOperationLessThan} class.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        // Given
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        InfoSet infoSet = new InfoSet();
        AbstractEvaluationContext context = new AbstractEvaluationContext(infoSet);

        // When
        double value = operation.computeValue(context).booleanValue();

        // Then
        double x = 5.0;
        double y = 10.0;
        infoSet.setValue(0, "x", ExpressionUtil.doubleValue(x));
        infoSet.setValue(0, "y", ExpressionUtil.doubleValue(y));
        assertTrue(value);

        x = 10.0;
        y = 5.0;
        infoSet.setValue(0, "x", ExpressionUtil.doubleValue(x));
        infoSet.setValue(0, "y", ExpressionUtil.doubleValue(y));
        assertFalse(value);

        x = 5.0;
        y = 5.0;
        infoSet.setValue(0, "x", ExpressionUtil.doubleValue(x));
        infoSet.setValue(0, "y", ExpressionUtil.doubleValue(y));
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        x = Double.NaN;
        y = 5.0;
        infoSet.setValue(0, "x", ExpressionUtil.doubleValue(x));
        infoSet.setValue(0, "y", ExpressionUtil.doubleValue(y));
        assertFalse(value);

        x = 5.0;
        y = Double.NaN;
        infoSet.setValue(0, "x", ExpressionUtil.doubleValue(x));
        infoSet.setValue(0, "y", ExpressionUtil.doubleValue(y));
        assertFalse(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanWithInvalidArgument() {
        // Given
        Expression arg1 = new Variable("x");
        Expression arg2 = null;
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        // When & Then
        AbstractEvaluationContext context = new AbstractEvaluationContext(new InfoSet());
        operation.computeValue(context);
    }
}