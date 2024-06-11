import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        // given
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new BooleanExpression(), new BooleanExpression());

        // when
        Boolean actual = expression.computeValue(new EvalContext());

        // then
        Boolean expected = Boolean.TRUE; // for this test, assume the result is true
        assertEquals(expected, actual);
    }
}