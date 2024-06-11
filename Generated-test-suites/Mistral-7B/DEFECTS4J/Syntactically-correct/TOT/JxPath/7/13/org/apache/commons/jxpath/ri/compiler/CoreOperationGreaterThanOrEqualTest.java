import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqual() {
        Expression arg1 = new CoreNumberExpression(new ConstantExpression(5.0));
        Expression arg2 = new CoreNumberExpression(new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(new Path("/"), infoSet);
        Object result = expression.computeValue(context);
        assertTrue("Expected true for 5.0 >= 5.0", result instanceof Boolean);
        assertTrue("Expected true for 5.0 >= 5.0", ((Boolean) result).booleanValue());
    }

    @Test
    public void testGreaterThanButNotEqual() {
        Expression arg1 = new CoreNumberExpression(new ConstantExpression(5.0));
        Expression arg2 = new CoreNumberExpression(new ConstantExpression(4.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(new Path("/"), infoSet);
        Object result = expression.computeValue(context);
        assertTrue("Expected false for 5.0 >= 4.0", result instanceof Boolean);
        assertFalse("Expected false for 5.0 >= 4.0", ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanButNotEqual() {
        Expression arg1 = new CoreNumberExpression(new ConstantExpression(4.0));
        Expression arg2 = new CoreNumberExpression(new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(new Path("/"), infoSet);
        Object result = expression.computeValue(context);
        assertFalse("Expected false for 4.0 >= 5.0", result instanceof Boolean);
        assertNull("Expected null for 4.0 >= 5.0", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOneArgument() {
        Expression arg1 = new CoreNumberExpression(new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoArgumentsWithInvalidType() {
        Expression arg1 = new CoreStringExpression(new ConstantExpression("hello"));
        Expression arg2 = new CoreNumberExpression(new ConstantExpression(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoArgumentsBothInvalidType() {
        Expression arg1 = new CoreStringExpression(new ConstantExpression("hello"));
        Expression arg2 = new CoreStringExpression(new ConstantExpression("world"));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
}