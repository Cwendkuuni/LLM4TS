import org.junit.Before;
import org.junit.Test;
import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ExpressionTest {

    Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("(2 + 3) * (4 > 3) and (5 = 5) or (6 < 5)");
        expression.prepare();
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("or"));
        assertFalse(expression.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));
        assertFalse(expression.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("or"));
        assertTrue(expression.isMathFunction("+"));
    }

    @Test
    public void testIsDateFunction() {
        assertFalse(expression.isDateFunction("and"));
        assertFalse(expression.isDateFunction("or"));
        assertFalse(expression.isDateFunction("+"));

        Expression dateExpression = new Expression("DateAdd(DateDiff(now(), '2022-01-01'), 1) > Date('2022-01-02')");
        dateExpression.prepare();
        assertTrue(dateExpression.isDateFunction("DateAdd"));
        assertTrue(dateExpression.isDateFunction("DateDiff"));
    }

    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction("and"));
        assertFalse(expression.isStringFunction("or"));
        assertFalse(expression.isStringFunction("+"));

        Expression stringExpression = new Expression("ToUpper(SubString(Length(concat('hello', ' world')), 7)) = 'World'");
        stringExpression.prepare();
        assertTrue(stringExpression.isStringFunction("ToUpper"));
        assertTrue(stringExpression.isStringFunction("SubString"));
        assertTrue(stringExpression.isStringFunction("Length"));
        assertTrue(stringExpression.isStringFunction("concat"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression customFunctionExpression = new Expression("myFunction(1, 2)");
        Expression customFunction = new MyFunction();
        customFunctionExpression.setExternalFunctions(Arrays.asList(customFunction));
        customFunctionExpression.prepare();
        assertEquals(customFunction, customFunctionExpression.getFunction("myFunction"));
    }

    @Test(expected = RuntimeException.class)
    public void testEvaluateInvalidExpression() {
        Expression invalidExpression = new Expression("(2 + 3) * (4 > z) and (5 = 5) or (6 < 5z)");
        invalidExpression.prepare();
        invalidExpression.evaluate();
    }

    @Test
    public void testEvaluate() {
        Object result = expression.evaluate();
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }

    @Test
    public void testEvaluateWithCustomFunction() {
        Expression customFunctionExpression = new Expression("myFunction(1, 2)");
        Expression customFunction = new MyFunction();
        customFunctionExpression.setExternalFunctions(Arrays.asList(customFunction));
        customFunction.setFunctionValue(new Integer(4));
        Object result = customFunctionExpression.evaluate();
        assertEquals(4, result);
    }
}

class ExpressionStack extends Stack<String> {
}

class MyFunction implements FunctionExpression {
    private static final long serialVersionUID = 1L;

    @Override
    public FunctionType getType() {
        return FunctionType.MATH;
    }

    @Override
    public FunctionExpression function(List<Object> arguments) {
        return this;
    }

    @Override
    public Object functionValue() {
        return null;
    }

    @Override
    public String getSimbol() {
        return "myFunction";
    }
}

enum FunctionType {
    LOGICAL, MATH, DATE, STRING
}

interface FunctionExpression {
    FunctionType getType();

    FunctionExpression function(List<Object> arguments);

    Object functionValue();

    String getSimbol();
}