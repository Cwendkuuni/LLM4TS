package org.javex.expression;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.EqualsFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Test;

public class ExpressionTest
{
    @Test
    public void testPrepareAndEvaluateSimpleExpressions()
    {
        String expression = "3 + 4";
        Expression e = new Expression(expression);
        e.prepare();
        Object result = e.evaluate();
        assertEquals(7, result);

        expression = "2 * 3";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(6, result);

        expression = "5 - 2";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(3, result);

        expression = "10 / 2";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(5, result);

        expression = "sqrt(16)";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(4, result);

        expression = "abs(-5)";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(5, result);
    }

    @Test
    public void testPrepareAndEvaluateLogicalExpressions()
    {
        String expression = "true and false";
        Expression e = new Expression(expression);
        e.prepare();
        Object result = e.evaluate();
        assertFalse((Boolean) result);

        expression = "true or false";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);

        expression = "not true";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertFalse((Boolean) result);

        expression = "not false";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);

        expression = "not (a = b)";
        e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new AndFunction(), new EqualsFunction()));
        e.prepare();
        result = e.evaluate();
        assertFalse((Boolean) result);

        expression = "not (a <> b)";
        e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new AndFunction(), new NotEqualsFunction()));
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);
    }

    @Test
    public void testPrepareAndEvaluateStringExpressions()
    {
        String expression = "length(\"abc\")";
        Expression e = new Expression(expression);
        e.prepare();
        Object result = e.evaluate();
        assertEquals(3, result);

        expression = "lower(\"ABC\")";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals("abc", result.toString());

        expression = "upper(\"abc\")";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals("ABC", result.toString());

        expression = "substring(\"abc\", 1, 2)";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals("bc", result.toString());

        expression = "contains(\"abc\", \"bc\")";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);

        expression = "startsWith(\"abc\", \"ab\")";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);

        expression = "endsWith(\"abc\", \"bc\")";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertFalse((Boolean) result);
    }

    @Test
    public void testPrepareAndEvaluateDateExpressions()
    {
        String expression = "dateadd(now(), 1, \"day\")";
        Expression e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new DateAddFunction()));
        e.prepare();
        Object result = e.evaluate();
        assertNotNull(result);

        expression = "datediff(now(), \"2022-01-01\", \"day\")";
        e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new DateDiffFunction()));
        e.prepare();
        result = e.evaluate();
        assertEquals(101, result);
    }

    @Test
    public void testPrepareAndEvaluateComplexExpressions()
    {
        String expression = "3 + (2 * (5 - 1))";
        Expression e = new Expression(expression);
        e.prepare();
        Object result = e.evaluate();
        assertEquals(11, result);

        expression = "sqrt(2 * (5 - 1))";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(3, result);

        expression = "abs(-5) * 2 + 3";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertEquals(11, result);

        expression = "not (length(\"abc\") = 3)";
        e = new Expression(expression);
        e.prepare();
        result = e.evaluate();
        assertFalse((Boolean) result);

        expression = "contains(\"abc\", \"bc\") and length(\"abc\") > 2";
        e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new AndFunction(), new ContainsFunction(), new LengthFunction()));
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);

        expression = "lower(\"ABC\") startsWith \"AB\" and upper(\"abc\") endsWith \"bc\"";
        e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new AndFunction(), new EndsWithFunction(), new StartsWithFunction(), new ToLowerFunction(), new ToUpperFunction()));
        e.prepare();
        result = e.evaluate();
        assertTrue((Boolean) result);

        expression = "dateadd(now(), 1, \"day\") > dateadd(now(), 1, \"month\")";
        e = new Expression(expression);
        e.setExternalFunctions(Arrays.asList(new DateAddFunction()));
        e.prepare();
        result = e.evaluate();
        assertFalse((Boolean) result);
    }

    @Test
    public void testPrepareAndEvaluateWithExternalFunctions()
    {
        Map<String, FunctionExpression> functions = new HashMap<>();
        Expression e = new Expression("");
        e.setExternalFunctions(Arrays.asList(new AndFunction(), new OrFunction(), new AbsFunction(), new DivideFunction(), new MultiplyFunction(), new PlusFunction(), new SqrtFunction(), new SubstractFunction(), new DateAddFunction(), new DateDiffFunction(), new ContainsFunction(), new EndsWithFunction(), new EqualsFunction(), new GreaterEqThanFunction(), new GreaterThanFunction(), new IsEmptyFunction(), new IsFunction(), new IsNullFunction(), new LessEqThanFunction(), new LessThanFunction(), new NotEqualsFunction(), new NotFunction(), new NotCharFunction(), new OrFunction(), new StartsWithFunction(), new MatchesFunction(), new InListFunction()));
        e.setReservedFunctions(functions);
        e.prepare();
        Object result = e.evaluate();
        assertNotNull(result);

        e = new Expression("myFunction(1, 2)");
        e.setExternalFunctions(Arrays.asList(new MyFunctionExpression()));
        e.setReservedFunctions(functions);
        e.prepare();
        result = e.evaluate();
        assertNotNull(result);
    }
}

class MyFunctionExpression extends FunctionExpression
{
    public MyFunctionExpression()
    {
        super("myFunction", FunctionExpression.CUSTOM);
    }

    @Override
    public Object evaluate(ExpressionStack stack, List externalFunctions, Map reservedFunctions)
    {
        // Implement your custom function logic here
        return 0;
    }
}

class ExpressionStack extends Stack<String>
{
}