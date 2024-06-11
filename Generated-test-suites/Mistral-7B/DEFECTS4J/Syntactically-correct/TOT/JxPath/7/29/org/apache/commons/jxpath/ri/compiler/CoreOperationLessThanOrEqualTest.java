import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Sequence;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual_valid_numbers() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertTrue("1.5 <= 3.0 should be true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_invalid_numbers_type_mismatch() {
        Expression arg1 = new LiteralExpression("1.5");
        Expression arg2 = new LiteralExpression(3);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        try {
            expression.computeValue(context);
            Assert.fail("Type mismatch exception expected");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testLessThanOrEqual_invalid_numbers_value() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("NaN <= 3.0 should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_invalid_numbers_value_2() {
        Expression arg1 = new LiteralExpression(3.0);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("3.0 <= NaN should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_invalid_numbers_negative() {
        Expression arg1 = new LiteralExpression(-1.5);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("(-1.5) <= 3.0 should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new Sequence(new LiteralExpression(3.0), new LiteralExpression(3.5));
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("1.5 <= [3.0, 3.5] should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence_first() {
        Expression arg1 = new Sequence(new LiteralExpression(1.5), new LiteralExpression(2.5));
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("[1.5, 2.5] <= 3.0 should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence_second() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new Sequence(new LiteralExpression(2.5), new LiteralExpression(3.0));
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("1.5 <= [2.5, 3.0] should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence_first_equal() {
        Expression arg1 = new Sequence(new LiteralExpression(1.5), new LiteralExpression(1.5));
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("=[1.5, 1.5] <= 3.0 should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence_second_equal() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new Sequence(new LiteralExpression(1.5), new LiteralExpression(1.5));
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertTrue("1.5 <= [1.5, 1.5] should be true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence_first_less() {
        Expression arg1 = new Sequence(new LiteralExpression(1.5), new LiteralExpression(2.5));
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertFalse("=[1.5, 2.5] <= 3.0 should be false", result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual_valid_numbers_with_sequence_second_less() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new Sequence(new LiteralExpression(2.5), new LiteralExpression(1.5));
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = expression.computeValue(context);
        assertTrue("1.5 <= [2.5, 1.5] should be true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }
}