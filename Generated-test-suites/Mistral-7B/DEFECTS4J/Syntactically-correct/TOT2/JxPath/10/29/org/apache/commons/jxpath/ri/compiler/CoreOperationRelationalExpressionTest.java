import org.apache.commons.jxpath.ri.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test for false result
        Expression left = new ConstantExpression(new Integer(5));
        Expression right = new ConstantExpression(new Integer(10));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperationUnaryMinusExpression(left), right);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test for true result
        right = new ConstantExpression(new Integer(5));
        expression = new CoreOperationGreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testEmptyCollections() {
        Iterator left = new HashSet<Object>().iterator();
        Iterator right = new HashSet<Object>().iterator();

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(left, right);
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test(expected = NullPointerException.class)
    public void testNullValues() {
        Object left = null;
        Object right = new Integer(5);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new ConstantExpression(left), new ConstantExpression(right));
        expression.computeValue(new EvalContext());
    }

    @Test
    public void testDifferentDataTypes() {
        Expression left = new ConstantExpression(new Integer(5));
        Expression right = new ConstantExpression("5");

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(left, right);
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testNaNValues() {
        Object left = Double.NaN;
        Object right = Double.NaN;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testSameObjects() {
        Object left = new Object();
        Object right = new Object();

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new ConstantExpression(left), new ConstantExpression(right));
        assertFalse(expression.evaluateCompare(-1));
    }
}