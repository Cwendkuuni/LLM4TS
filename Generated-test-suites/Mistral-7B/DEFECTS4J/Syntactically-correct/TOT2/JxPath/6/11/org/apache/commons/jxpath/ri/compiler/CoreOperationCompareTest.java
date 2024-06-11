import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        CoreOperation left = new CoreOperation(new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(5)));
        CoreOperationCompare compare = new CoreOperationCompare(left, new ConstantExpression(new Integer(5)));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertTrue(compare.evaluate(context));

        CoreOperation right = new CoreOperation(new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(6)));
        compare = new CoreOperationCompare(left, right);
        assertFalse(compare.evaluate(context));

        CoreOperation left2 = new CoreOperation(new ConstantExpression(new Integer(null)), new ConstantExpression(new Integer(5)));
        compare = new CoreOperationCompare(left2, new ConstantExpression(new Integer(5)));
        assertFalse(compare.evaluate(context));

        CoreOperation left3 = new CoreOperation(new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(5)));
        CoreOperation right2 = new CoreOperation(new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(5)));
        compare = new CoreOperationCompare(left3, right2);
        assertTrue(compare.evaluate(context));

        CoreOperation left4 = new CoreOperation(new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(5)));
        CoreOperation right3 = new CoreOperation(new ConstantExpression(new Integer(6)), new ConstantExpression(new Integer(5)));
        compare = new CoreOperationCompare(left4, right3);
        assertFalse(compare.evaluate(context));

        CoreOperation left5 = new CoreOperation(new ConstantExpression(new Integer(null)), new ConstantExpression(new Integer(null)));
        compare = new CoreOperationCompare(left5, new ConstantExpression(new Integer(null)));
        assertTrue(compare.evaluate(context));

        CoreOperation left6 = new CoreOperation(new VariableExpression("x"), new VariableExpression("x"));
        compare = new CoreOperationCompare(left6, left6);
        assertTrue(compare.evaluate(context));

        CoreOperation left7 = new CoreOperation(new VariableExpression("x"), new VariableExpression("y"));
        compare = new CoreOperationCompare(left7, left7);
        assertFalse(compare.evaluate(context));

        CoreOperation left8 = new CoreOperation(new ConstantExpression(new String("hello")), new ConstantExpression(new String("hello")));
        compare = new CoreOperationCompare(left8, left8);
        assertTrue(compare.evaluate(context));

        CoreOperation left9 = new CoreOperation(new ConstantExpression(new String("hello")), new ConstantExpression(new String("world")));
        compare = new CoreOperationCompare(left9, left9);
        assertFalse(compare.evaluate(context));
    }
}