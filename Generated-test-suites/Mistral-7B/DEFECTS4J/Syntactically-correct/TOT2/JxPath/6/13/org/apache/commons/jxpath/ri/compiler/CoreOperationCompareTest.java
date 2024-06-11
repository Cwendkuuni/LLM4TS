import org.apache.commons.jxpath.ri.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(1),
            new CoreConstantExpression(1)
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualWithNullArgument() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            null,
            new CoreConstantExpression(1)
        );
    }

    @Test
    public void testEqualWithDifferentTypes() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(1),
            new CoreConstantExpression("1")
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualWithPointerValues() {
        Pointer pointer1 = new Pointer(new InfoSet(), 1);
        Pointer pointer2 = new Pointer(new InfoSet(), 2);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(pointer1),
            new CoreConstantExpression(pointer2)
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualWithCollectionValues() {
        List collection1 = Arrays.asList(1, 2, 3);
        List collection2 = Arrays.asList(1, 2, 3);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(collection1),
            new CoreConstantExpression(collection2)
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualWithEmptyCollections() {
        List collection1 = Collections.emptyList();
        List collection2 = Collections.emptyList();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(collection1),
            new CoreConstantExpression(collection2)
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualWithCollectionsDifferentElements() {
        List collection1 = Arrays.asList(1, 2, 3);
        List collection2 = Arrays.asList(1, 2, 4);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(collection1),
            new CoreConstantExpression(collection2)
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualWithIteratorValues() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2, 3);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(list1.iterator()),
            new CoreConstantExpression(list2.iterator())
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqualWithDifferentIteratorElements() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2, 4);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(list1.iterator()),
            new CoreConstantExpression(list2.iterator())
        );
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }
}