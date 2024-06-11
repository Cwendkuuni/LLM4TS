import static org.junit.Assert.*;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNumber;
import org.apache.commons.jxpath.ri.compiler.CoreOperationString;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    private CoreOperationCompare compare;
    private CoreOperationNumber num1;
    private CoreOperationNumber num2;
    private CoreOperationString str1;
    private CoreOperationString str2;

    @Before
    public void setUp() {
        compare = new CoreOperationCompare(num1 = new CoreOperationNumber(1.0), num2 = new CoreOperationNumber(1.0));
    }

    @Test
    public void testEqualWithSameValues() {
        // Test with same values of the same type
        assertTrue(compare.evaluate(new EvalContext(), null));
    }

    // Add test cases for other data types and combinations of data types
    // Add test cases for null values
    // Add test cases for empty collections and iterators
    // Add test cases for edge cases and exception handling scenarios
}