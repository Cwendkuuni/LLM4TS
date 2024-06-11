import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.FunctionExpression;
import java.util.Arrays;
import java.util.List;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("expression with external functions (and, contains, startswith)");
        List<FunctionExpression> expectedFunctions = Arrays.asList(
                new AndFunction(),
                new ContainsFunction(),
                new StartsWithFunction()
        );
        List<FunctionExpression> actualFunctions = expression.getExternalFunctions();
        Assert.assertEquals(expectedFunctions, actualFunctions);
    }
}