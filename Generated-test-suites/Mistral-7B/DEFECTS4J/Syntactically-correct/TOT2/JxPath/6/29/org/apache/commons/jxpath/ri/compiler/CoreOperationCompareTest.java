import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Pointer;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test with primitive types
        Expression left1 = new LiteralExpression(1);
        Expression right1 = new LiteralExpression(1);
        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(left1, right1);
        EvalContext context1 = new EvalContext();
        boolean actual1 = coreOperationCompare1.eval(context1);
        assertTrue(actual1);

        // Test with String
        Expression left2 = new LiteralExpression("Hello");
        Expression right2 = new LiteralExpression("Hello");
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(left2, right2);
        EvalContext context2 = new EvalContext();
        boolean actual2 = coreOperationCompare2.eval(context2);
        assertTrue(actual2);

        // Test with Boolean
        Expression left3 = new LiteralExpression(true);
        Expression right3 = new LiteralExpression(true);
        CoreOperationCompare coreOperationCompare3 = new CoreOperationCompare(left3, right3);
        EvalContext context3 = new EvalContext();
        boolean actual3 = coreOperationCompare3.eval(context3);
        assertTrue(actual3);

        // Test with different types
        Expression left4 = new LiteralExpression(1);
        Expression right4 = new LiteralExpression("One");
        CoreOperationCompare coreOperationCompare4 = new CoreOperationCompare(left4, right4);
        EvalContext context4 = new EvalContext();
        boolean actual4 = coreOperationCompare4.eval(context4);
        assertFalse(actual4);

        // Test with null values
        Expression left5 = new LiteralExpression(null);
        Expression right5 = new LiteralExpression(null);
        CoreOperationCompare coreOperationCompare5 = new CoreOperationCompare(left5, right5);
        EvalContext context5 = new EvalContext();
        boolean actual5 = coreOperationCompare5.eval(context5);
        assertTrue(actual5);

        // Test with Iterators
        Expression left6 = new IteratorExpression(new Iterator[]{new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        }, new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        }});
        Expression right6 = new IteratorExpression(new Iterator[]{new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        }});
        CoreOperationCompare coreOperationCompare6 = new CoreOperationCompare(left6, right6);
        EvalContext context6 = new EvalContext();
        boolean actual6 = coreOperationCompare6.eval(context6);
        assertTrue(actual6);
    }
}