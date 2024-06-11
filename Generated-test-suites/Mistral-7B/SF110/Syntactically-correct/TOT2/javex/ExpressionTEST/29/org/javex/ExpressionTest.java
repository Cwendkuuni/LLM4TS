import org.junit.Test;
import org.javex.Expression;
import org.javex.FunctionExpression;
import org.junit.Assert.*;
import java.util.List;
import static org.javex.JavexUtils.*;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        assertNotNull(expression);
    }

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("1 + sin(x)");
        List<FunctionExpression> functions = expression.getExternalFunctions();
        assertTrue(functions.size() > 0);
        assertEquals(functions.get(0).getSimbol(), "sin");
    }

    @Test
    public void testGetExternalFunctionsEmptyExpression() {
        Expression expression = new Expression("");
        List<FunctionExpression> functions = expression.getExternalFunctions();
        assertTrue(functions.isEmpty());
    }

    @Test
    public void testIsFunctionValidSymbol() {
        Expression expression = new Expression("sin(x)");
        assertTrue(expression.isFunction("sin"));
    }

    @Test
    public void testIsFunctionInvalidSymbol() {
        Expression expression = new Expression("sin(x)");
        assertFalse(expression.isFunction("cos"));
    }

    @Test
    public void testIsFunctionEdgeCase() {
        Expression expression = new Expression("sin x");
        assertTrue(expression.isFunction("sin"));
    }

    @Test(expected = Exception.class)
    public void testDoEvaluationStackInvalidExpression() {
        Expression expression = new Expression("");
        expression.prepare();
        expression.doEvaluationStack();
    }

    @Test(expected = Exception.class)
    public void testDoEvaluationStackUnsupportedFunction() {
        Expression expression = new Expression("sqrt(-1)");
        expression.prepare();
        expression.doEvaluationStack();
    }

    @Test
    public void testDoEvaluationStackSimpleExpression() {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        Object result = expression.doEvaluationStack().pop();
        assertEquals(3, result);
    }

    @Test
    public void testDoEvaluationStackComplexExpression() {
        Expression expression = new Expression("sin(x) + cos(x)");
        expression.prepare();
        Object result = expression.doEvaluationStack().pop();
        assertEquals(Math.sin(Math.PI / 4) + Math.cos(Math.PI / 4), result, 0.0001);
    }

    @Test
    public void testDoEvaluationStackParentheses() {
        Expression expression = new Expression("(1 + 2) * 3");
        expression.prepare();
        Object result = expression.doEvaluationStack().pop();
        assertEquals(11, result);
    }

    @Test
    public void testDoEvaluationStackLogicalOperators() {
        Expression expression = new Expression("(1 > 2) and (3 > 2)");
        expression.prepare();
        Object result = expression.doEvaluationStack().pop();
        assertEquals(false, result);
    }

    @Test
    public void testDoEvaluationStackExternalFunctions() {
        Expression expression = new Expression("sin(pi/2) + cos(pi/2)");
        expression.prepare();
        Object result = expression.doEvaluationStack().pop();
        assertEquals(Math.sqrt(2), result, 0.0001);
    }
}