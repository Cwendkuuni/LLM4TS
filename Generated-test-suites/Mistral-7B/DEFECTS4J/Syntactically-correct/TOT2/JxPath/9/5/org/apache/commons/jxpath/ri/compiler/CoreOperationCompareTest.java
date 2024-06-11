import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        CoreOperation leftExpression = new ConstantExpression(1);
        CoreOperation rightExpression = new ConstantExpression(1);
        CoreOperationCompare coreOperationCompare = new EqualExpression(leftExpression, rightExpression);
        EvalContext context = new EvalContext();

        boolean result = coreOperationCompare.evaluate(context);
        assertEquals(true, result);
    }
}