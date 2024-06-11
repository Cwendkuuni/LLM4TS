import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(1), new ConstantExpression(1)
        );

        EvalContext context = new EvalContext(new InitialContext());
        boolean result = coreOperationCompare.execute(context);

        assertTrue(result);
    }

    // Add more test cases for different types and values
}