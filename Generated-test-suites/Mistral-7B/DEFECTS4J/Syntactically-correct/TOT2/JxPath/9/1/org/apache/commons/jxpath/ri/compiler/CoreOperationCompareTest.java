import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        // Test with basic data types
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("5");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        assertTrue(coreOperationCompare.equal(new EvalContext(), left, right));

        // Test with pointers
        Pointer leftPointer = new Pointer("1");
        Pointer rightPointer = new Pointer("1");
        Expression leftExpression = new PointerExpression(leftPointer);
        Expression rightExpression = new PointerExpression(rightPointer);
        CoreOperationCompare coreOperationComparePointer = new CoreOperationCompare(leftExpression, rightExpression);
        assertTrue(coreOperationComparePointer.equal(new EvalContext(), leftExpression, rightExpression));

        // Test with collections
        Expression list1 = new LiteralExpression("[1,2,3]");
        Expression list2 = new LiteralExpression("[1,2,3]");
        CoreOperationCompare coreOperationCompareCollection = new CoreOperationCompare(list1, list2);
        assertTrue(coreOperationCompareCollection.equal(new EvalContext(), list1, list2));

        // Test with different collections
        Expression list3 = new LiteralExpression("[1,2,3]");
        Expression list4 = new LiteralExpression("[2,3,4]");
        CoreOperationCompare coreOperationCompareCollectionDifferent = new CoreOperationCompare(list3, list4);
        assertFalse(coreOperationCompareCollectionDifferent.equal(new EvalContext(), list3, list4));

        // Test with null values
        Expression leftNull = new LiteralExpression(null);
        Expression rightNull = new LiteralExpression(null);
        CoreOperationCompare coreOperationCompareNull = new CoreOperationCompare(leftNull, rightNull);
        assertTrue(coreOperationCompareNull.equal(new EvalContext(), leftNull, rightNull));

        // Test with non-equal null values
        Expression leftNull2 = new LiteralExpression(null);
        Expression rightNull2 = new LiteralExpression("NULL");
        CoreOperationCompare coreOperationCompareNullNotEqual = new CoreOperationCompare(leftNull2, rightNull2);
        assertFalse(coreOperationCompareNullNotEqual.equal(new EvalContext(), leftNull2, rightNull2));
    }
}