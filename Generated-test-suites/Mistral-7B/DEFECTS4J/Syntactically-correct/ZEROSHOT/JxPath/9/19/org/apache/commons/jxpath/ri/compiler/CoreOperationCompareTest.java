import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setup() {
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        context = Mockito.mock(EvalContext.class);

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testGetPrecedence() {
        assertTrue(coreOperationCompare.getPrecedence() == 2);
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqualWithInitialContext() {
        Mockito.when(arg1.compute(context)).thenReturn(new InitialContext());
        Mockito.when(arg2.compute(context)).thenReturn(new InitialContext());

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithSelfContext() {
        Mockito.when(arg1.compute(context)).thenReturn(new SelfContext());
        Mockito.when(arg2.compute(context)).thenReturn(new SelfContext());

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithCollection_SameIterator() {
        Object[] collection1 = {1, 2, 3};
        Iterator iterator1 = Arrays.asList(collection1).iterator();

        Object[] collection2 = {1, 2, 3};
        Iterator iterator2 = Arrays.asList(collection2).iterator();

        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithCollection_DifferentOrder() {
        Object[] collection1 = {1, 2, 3};
        Iterator iterator1 = Arrays.asList(collection1).iterator();

        Object[] collection2 = {3, 2, 1};
        Iterator iterator2 = Arrays.asList(collection2).iterator();

        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithCollection_DiffrentElements() {
        Object[] collection1 = {1, 2, 3};
        Iterator iterator1 = Arrays.asList(collection1).iterator();

        Object[] collection2 = {1, 2, 4};
        Iterator iterator2 = Arrays.asList(collection2).iterator();

        Mockito.when(arg1.compute(context)).thenReturn(iterator1);
        Mockito.when(arg2.compute(context)).thenReturn(iterator2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithSingleValue() {
        Object value1 = 1;
        Object value2 = 1;

        Mockito.when(arg1.compute(context)).thenReturn(value1);
        Mockito.when(arg2.compute(context)).thenReturn(value2);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithDifferentValues() {
        Object value1 = 1;
        Object value2 = 2;

        Mockito.when(arg1.compute(context)).thenReturn(value1);
        Mockito.when(arg2.compute(context)).thenReturn(value2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        Object[] collection = {1, 2, 3};
        Iterator iterator = Arrays.asList(collection).iterator();

        Mockito.when(arg1.compute(context)).thenReturn(iterator);

        assertTrue(coreOperationCompare.contains(iterator, 1));
        assertTrue(coreOperationCompare.contains(iterator, 2));
        assertTrue(coreOperationCompare.contains(iterator, 3));

        assertFalse(coreOperationCompare.contains(iterator, 4));
    }

    @Test
    public void testFindMatch() {
        Object[] collection1 = {1, 2, 3};
        Iterator iterator1 = Arrays.asList(collection1).iterator();

        Object[] collection2 = {1, 2, 3};
        Iterator iterator2 = Arrays.asList(collection2).iterator();

        HashSet set = new HashSet();
        while (iterator1.hasNext()) {
            set.add(iterator1.next());
        }

        assertTrue(coreOperationCompare.findMatch(iterator2, set));

        Object[] collection3 = {1, 4, 3};
        Iterator iterator3 = Arrays.asList(collection3).iterator();

        assertFalse(coreOperationCompare.findMatch(iterator2, set));
        assertFalse(coreOperationCompare.findMatch(iterator3, set));
    }

    @Test
    public void testEqualWithPointer() {
        Object pointerValue = new Object();
        Pointer pointer1 = Mockito.mock(Pointer.class);
        Mockito.when(pointer1.getValue()).thenReturn(pointerValue);

        Pointer pointer2 = Mockito.mock(Pointer.class);
        Mockito.when(pointer2.getValue()).thenReturn(pointerValue);

        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(pointer2);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithDifferentPointers() {
        Object pointerValue1 = new Object();
        Pointer pointer1 = Mockito.mock(Pointer.class);
        Mockito.when(pointer1.getValue()).thenReturn(pointerValue1);

        Object pointerValue2 = new Object();
        Pointer pointer2 = Mockito.mock(Pointer.class);
        Mockito.when(pointer2.getValue()).thenReturn(pointerValue2);

        Mockito.when(arg1.compute(context)).thenReturn(pointer1);
        Mockito.when(arg2.compute(context)).thenReturn(pointer2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithNull() {
        Mockito.when(arg1.compute(context)).thenReturn(null);
        Mockito.when(arg2.compute(context)).thenReturn(null);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithBooleanTrue() {
        Mockito.when(arg1.compute(context)).thenReturn(Boolean.TRUE);
        Mockito.when(arg2.compute(context)).thenReturn(Boolean.TRUE);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithBooleanFalse() {
        Mockito.when(arg1.compute(context)).thenReturn(Boolean.FALSE);
        Mockito.when(arg2.compute(context)).thenReturn(Boolean.FALSE);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithBooleanTrueAndFalse() {
        Mockito.when(arg1.compute(context)).thenReturn(Boolean.TRUE);
        Mockito.when(arg2.compute(context)).thenReturn(Boolean.FALSE);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithNumber() {
        double number1 = 1.0;
        Mockito.when(arg1.compute(context)).thenReturn(number1);

        double number2 = 1.0;
        Mockito.when(arg2.compute(context)).thenReturn(number2);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithDifferentNumber() {
        double number1 = 1.0;
        Mockito.when(arg1.compute(context)).thenReturn(number1);

        double number2 = 2.0;
        Mockito.when(arg2.compute(context)).thenReturn(number2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithString() {
        String string1 = "test";
        Mockito.when(arg1.compute(context)).thenReturn(string1);

        String string2 = "test";
        Mockito.when(arg2.compute(context)).thenReturn(string2);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithDifferentString() {
        String string1 = "test";
        Mockito.when(arg1.compute(context)).thenReturn(string1);

        String string2 = "test2";
        Mockito.when(arg2.compute(context)).thenReturn(string2);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }
}