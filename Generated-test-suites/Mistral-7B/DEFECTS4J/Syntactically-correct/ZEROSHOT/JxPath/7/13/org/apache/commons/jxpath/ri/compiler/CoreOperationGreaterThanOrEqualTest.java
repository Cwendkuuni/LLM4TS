package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;

import java.math.BigDecimal;

public class CoreOperationGreaterThanOrEqualTest extends TestCase {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationGreaterThanOrEqual expression;

    @Override
    protected void setUp() throws Exception {
        arg1 = ExpressionFactory.createNumericConstant(1.0);
        arg2 = ExpressionFactory.createNumericConstant(2.0);
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    public void testComputeValueWithDoubleValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet());

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);
    }

    public void testComputeValueWithDoubleValuesNotGreaterThanOrEqual() {
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet());

        arg1 = ExpressionFactory.createNumericConstant(1.0);
        arg2 = ExpressionFactory.createNumericConstant(3.0);
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    public void testComputeValueWithBigDecimalValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet());

        Expression arg1BigDecimal = ExpressionFactory.createNumericConstant(new BigDecimal("1.5"));
        Expression arg2BigDecimal = ExpressionFactory.createNumericConstant(new BigDecimal("2.5"));
        expression = new CoreOperationGreaterThanOrEqual(arg1BigDecimal, arg2BigDecimal);

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);
    }

    public void testComputeValueWithBigDecimalValuesNotGreaterThanOrEqual() {
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet());

        Expression arg1BigDecimal = ExpressionFactory.createNumericConstant(new BigDecimal("1.5"));
        Expression arg2BigDecimal = ExpressionFactory.createNumericConstant(new BigDecimal("3.5"));
        expression = new CoreOperationGreaterThanOrEqual(arg1BigDecimal, arg2BigDecimal);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    public void testComputeValueWithNullValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet());

        arg1 = ExpressionFactory.createNullExpression();
        arg2 = ExpressionFactory.createNumericConstant(2.0);
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    public void testComputeValueWithNullValues1() {
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet());

        arg1 = ExpressionFactory.createNumericConstant(2.0);
        arg2 = ExpressionFactory.createNullExpression();
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    public void testGetSymbol() {
        String symbol = expression.getSymbol();
        assertEquals(">=", symbol);
    }
}