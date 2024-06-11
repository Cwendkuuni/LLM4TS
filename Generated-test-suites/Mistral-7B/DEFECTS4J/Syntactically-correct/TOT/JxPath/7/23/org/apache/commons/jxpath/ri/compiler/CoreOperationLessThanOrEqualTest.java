import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.ExpressionFactory;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualDouble() {
        // Given
        Expression arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        Expression arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("7.5"));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        // When
        EvalContext context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        Object result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5").setScale(1));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.TRUE, result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        arg2 = ExpressionFactory.createNumberExpression(null);

        // When / Then
        Assert.assertThrows(ClassCastException.class, () -> operation.computeValue(context));

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("NaN"));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertNull(result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("-Inf"));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("-Inf"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("Inf"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.FALSE, result);

        // Given
        arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("Inf"));
        arg2 = ExpressionFactory.createNumberExpression(new BigDecimal("Inf"));

        // When
        context = new EvalContext(new Context(new NodeInfo(1, null, null), new InfoSet(), null));
        result = operation.computeValue(context);

        // Then
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqualString() {
        // Given
        Expression arg1 = ExpressionFactory.createStringExpression("5.5");
        Expression arg2 = ExpressionFactory.createStringExpression("7.5");

        // When / Then
        Assert.assertThrows(ClassCastException.class, () -> new CoreOperationLessThanOrEqual(arg1, arg2));
    }

    @Test
    public void testLessThanOrEqualList() {
        // Given
        List<Expression> argList = new ArrayList<>();
        argList.add(ExpressionFactory.createNumberExpression(new BigDecimal("5.5")));
        argList.add(ExpressionFactory.createNumberExpression(new BigDecimal("7.5")));

        // When / Then
        Assert.assertThrows(IllegalArgumentException.class, () -> new CoreOperationLessThanOrEqual(ExpressionUtil.createListExpression(argList), ExpressionFactory.createNumberExpression(new BigDecimal("7.5"))));
    }

    @Test
    public void testLessThanOrEqualMixedTypes() {
        // Given
        Expression arg1 = ExpressionFactory.createNumberExpression(new BigDecimal("5.5"));
        Expression arg2 = ExpressionFactory.createStringExpression("5.5");

        // When / Then
        Assert.assertThrows(ClassCastException.class, () -> new CoreOperationLessThanOrEqual(arg1, arg2));
    }
}