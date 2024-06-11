import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Pointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        CoreOperation op = new CoreOperationCompare(new LiteralExpression(1), new LiteralExpression(1));
        EvalContext context = new EvalContext();
        assertTrue(op.evaluate(context).booleanValue());

        // Test with different types
        op = new CoreOperationCompare(new LiteralExpression("hello"), new LiteralExpression("hello"));
        assertTrue(op.evaluate(context).booleanValue());

        // Test with different objects
        op = new CoreOperationCompare(new LiteralExpression(1), new LiteralExpression(2));
        assertFalse(op.evaluate(context).booleanValue());

        // Test with null
        op = new CoreOperationCompare(new LiteralExpression(null), new LiteralExpression(null));
        assertTrue(op.evaluate(context).booleanValue());

        // Test with Collection
        Expression left = new ArrayExpression(new Expression[] { new LiteralExpression(1), new LiteralExpression(2) });
        Expression right = new ArrayExpression(new Expression[] { new LiteralExpression(2), new LiteralExpression(1) });
        op = new CoreOperationCompare(left, right);
        assertEquals(true, op.evaluate(context).booleanValue());

        // Test with Iterator
        Iterator leftIter = new ArrayIterator(new Object[] { 1, 2 });
        Iterator rightIter = new ArrayIterator(new Object[] { 2, 1 });
        op = new CoreOperationCompare(new IteratorExpression(leftIter), new IteratorExpression(rightIter));
        assertEquals(true, op.evaluate(context).booleanValue());

        // Test with Pointer
        Pointer leftPointer = new Pointer("$root/node1");
        Pointer rightPointer = new Pointer("$root/node1");
        op = new CoreOperationCompare(new PointerExpression(leftPointer), new PointerExpression(rightPointer));
        assertTrue(op.evaluate(context).booleanValue());
    }
}