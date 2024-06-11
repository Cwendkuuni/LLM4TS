package org.javex;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MaxFunction;
import org.javex.functions.math.MinFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.date.LengthFunction;
import org.javex.functions.date.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("");
        assertFalse(e.isReservedFunction(""));
        assertNotNull(e.getReservedFunctions("and"));
        assertNotNull(e.getReservedFunctions("or"));
        assertNotNull(e.getReservedFunctions("not"));
        assertNotNull(e.getReservedFunctions("and"));
        assertNotNull(e.getReservedFunctions("or"));

        // Test with invalid symbol
        assertNull(e.getReservedFunctions("invalid"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("");
        assertFalse(e.isLogicalFunction(""));
        assertNotNull(e.getFunction("and"));
        assertNotNull(e.getFunction("or"));
        assertNotNull(e.getFunction("not"));

        // Test with invalid symbol
        assertNull(e.getFunction("invalid"));
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("");
        assertFalse(e.isMathFunction(""));
        assertNotNull(e.getFunction("abs"));
        assertNotNull(e.getFunction("add"));
        assertNotNull(e.getFunction("divide"));
        assertNotNull(e.getFunction("max"));
        assertNotNull(e.getFunction("min"));
        assertNotNull(e.getFunction("multiply"));
        assertNotNull(e.getFunction("power"));
        assertNotNull(e.getFunction("subtract"));

        // Test with invalid symbol
        assertNull(e.getFunction("invalid"));
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("");
        assertFalse(e.isDateFunction(""));
        assertNotNull(e.getFunction("dateadd"));
        assertNotNull(e.getFunction("datediff"));
        assertNotNull(e.getFunction("length"));
        assertNotNull(e.getFunction("substring"));

        // Test with invalid symbol
        assertNull(e.getFunction("invalid"));
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("");
        assertFalse(e.isStringFunction(""));
        assertNotNull(e.getFunction("tolower"));
        assertNotNull(e.getFunction("toupper"));

        // Test with invalid symbol
        assertNull(e.getFunction("invalid"));
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("");
        assertFalse(e.isFunction(""));
        assertNotNull(e.getFunction("and"));
        assertNotNull(e.getFunction("or"));
        assertNotNull(e.getFunction("not"));
        assertNotNull(e.getFunction("abs"));
        assertNotNull(e.getFunction("add"));
        assertNotNull(e.getFunction("divide"));
        assertNotNull(e.getFunction("max"));
        assertNotNull(e.getFunction("min"));
        assertNotNull(e.getFunction("multiply"));
        assertNotNull(e.getFunction("power"));
        assertNotNull(e.getFunction("subtract"));
        assertNotNull(e.getFunction("dateadd"));
        assertNotNull(e.getFunction("datediff"));
        assertNotNull(e.getFunction("length"));
        assertNotNull(e.getFunction("substring"));
        assertNotNull(e.getFunction("tolower"));
        assertNotNull(e.getFunction("toupper"));

        // Test with invalid symbol
        assertNull(e.getFunction("invalid"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression e = new Expression("");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new AndFunction());
        e.setExternalFunctions(externalFunctions);
        assertEquals(e.getFunction("and"), e.getFunction("and"));
        assertNull(e.getFunction("or"));

        // Test with invalid symbol
        assertNull(e.getFunction("invalid"));
    }

    @Test
    public void testPrepare() {
        Expression e = new Expression("1 + 2 * 3");
        assertFalse(e.isPrepared());
        e.prepare();
        assertTrue(e.isPrepared());
    }

    @Test(expected = Exception.class)
    public void testEvaluateInvalidExpression() {
        Expression e = new Expression("1 + 2 *");
        e.prepare();
        e.evaluate();
    }

    @Test
    public void testEvaluate() {
        Expression e = new Expression("1 + 2 * 3");
        e.prepare();
        assertEquals(7.0, e.evaluate(), 0.001);

        // Test with valid logical functions
        e = new Expression("(2 > 1) and (3 > 2)");
        e.prepare();
        assertTrue(e.evaluate());

        // Test with valid math functions
        e = new Expression("abs(-3) + pow(2, 3) * sin(pi / 2) + sqrt(16) - cos(pi) / 2");
        e.prepare();
        assertEquals(11.165576508736192, e.evaluate(), 0.001);

        // Test with valid date functions
        e = new Expression("dateadd(day, '2022-01-01', 2) + datediff(day, '2022-01-02', '2022-01-03')");
        e.prepare();
        assertEquals(3, e.evaluate());

        // Test with valid string functions
        e = new Expression("tolower('HELLO') + substring('WORLD', 3, 4) + '!'");
        e.prepare();
        assertEquals("ello world!", e.evaluate());

        // Test with combination of functions
        e = new Expression("abs(sin(pi / 2)) * pow(2, 3) + tolower('HELLO') + substring('WORLD', 3, 4) + '!'");
        e.prepare();
        assertEquals("12.566370614359172ello world!", e.evaluate());
    }
}