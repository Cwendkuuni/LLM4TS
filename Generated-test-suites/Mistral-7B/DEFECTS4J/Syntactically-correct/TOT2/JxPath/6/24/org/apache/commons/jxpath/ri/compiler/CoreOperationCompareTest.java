import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionFactory;
import org.apache.commons.jxpath.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = ExpressionFactory.createParser().parse("1");
        Expression arg2 = ExpressionFactory.createParser().parse("1");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testEqual() {
        EvalContext context = null;
        Expression left = null;
        Expression right = null;

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        // Test equal method with null context, left, and right
        try {
            coreOperationCompare.equal(context, left, right);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test equal method with null left or right
        context = new EvalContext();
        left = null;

        try {
            coreOperationCompare.equal(context, left, right);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        right = null;

        try {
            coreOperationCompare.equal(context, left, right);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test equal method with different types
        left = ExpressionFactory.createParser().parse("1");
        right = ExpressionFactory.createParser().parse("'Hello'");
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test equal method with different iterators
        Collection leftColl = new HashSet();
        leftColl.add(1);
        leftColl.add(2);
        Iterator leftIter = leftColl.iterator();

        Collection rightColl = new HashSet();
        rightColl.add(3);
        rightColl.add(4);
        Iterator rightIter = rightColl.iterator();

        assertFalse(coreOperationCompare.equal(context, leftIter, rightIter));

        // Test equal method with different boolean values
        left = ExpressionFactory.createParser().parse("true");
        right = ExpressionFactory.createParser().parse("false");
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test equal method with different number values
        left = ExpressionFactory.createParser().parse("1.0");
        right = ExpressionFactory.createParser().parse("2.0");
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test equal method with different string values
        left = ExpressionFactory.createParser().parse("'Hello'");
        right = ExpressionFactory.createParser().parse("'World'");
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test equal method with typical use cases
        left = ExpressionFactory.createParser().parse("1");
        right = ExpressionFactory.createParser().parse("1");
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = ExpressionFactory.createParser().parse("2");
        right = ExpressionFactory.createParser().parse("2");
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = ExpressionFactory.createParser().parse("'Hello'");
        right = ExpressionFactory.createParser().parse("'Hello'");
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = ExpressionFactory.createParser().parse("true");
        right = ExpressionFactory.createParser().parse("true");
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = ExpressionFactory.createParser().parse("(1, 2, 3)");
        right = ExpressionFactory.createParser().parse("(1, 2, 3)");
        assertTrue(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        // Test contains method with typical use cases
        Collection coll = new HashSet();
        coll.add(1);
        coll.add(2);
        Iterator it = coll.iterator();

        Object value = 1;
        assertTrue(coreOperationCompare.contains(it, value));

        value = 2;
        assertTrue(coreOperationCompare.contains(it, value));

        value = 3;
        assertFalse(coreOperationCompare.contains(it, value));

        // Test contains method with edge cases and exception handling scenarios
        coll = new HashSet();
        it = coll.iterator();

        value = null;
        try {
            coreOperationCompare.contains(it, value);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        coll.add(null);
        assertTrue(coreOperationCompare.contains(it, null));

        coll.clear();
        it = null;
        try {
            coreOperationCompare.contains(it, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFindMatch() {
        // Test findMatch method with typical use cases
        Collection leftColl = new HashSet();
        leftColl.add(1);
        leftColl.add(2);
        Collection rightColl = new HashSet();
        rightColl.add(1);
        rightColl.add(2);
        Iterator leftIter = leftColl.iterator();
        Iterator rightIter = rightColl.iterator();

        assertTrue(coreOperationCompare.findMatch(leftIter, rightIter));

        leftColl.add(3);
        assertFalse(coreOperationCompare.findMatch(leftIter, rightIter));

        // Test findMatch method with edge cases and exception handling scenarios
        leftColl = new HashSet();
        leftColl.add(null);
        rightColl = new HashSet();
        rightColl.add(1);
        leftIter = leftColl.iterator();

        assertFalse(coreOperationCompare.findMatch(leftIter, rightIter));

        leftColl = new HashSet();
        leftColl.add(1);
        rightColl = new HashSet();
        rightColl.clear();

        assertFalse(coreOperationCompare.findMatch(leftIter, rightIter));
    }
}