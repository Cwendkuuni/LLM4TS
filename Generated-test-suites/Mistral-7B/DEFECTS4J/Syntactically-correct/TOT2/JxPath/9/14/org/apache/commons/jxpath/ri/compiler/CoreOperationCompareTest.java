import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;
    private Iterator it1;
    private Iterator it2;
    private List list1;
    private List list2;

    @Before
    public void setup() {
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        context = mock(EvalContext.class);
        it1 = mock(Iterator.class);
        it2 = mock(Iterator.class);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
    }

    @Test
    public void testConstructor() {
        // No testing required
    }

    @Test
    public void testGetPrecedence_ValidPrecedence() {
        when(context.getPrecedence(coreOperationCompare)).thenReturn(2);
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testGetPrecedence_NegativePrecedence() {
        when(context.getPrecedence(coreOperationCompare)).thenReturn(-1);
        assertFalse(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testIsSymmetric_SymmetricOperation() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testIsSymmetric_NonSymmetricOperation() {
        assertFalse(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual_ValidValues() {
        when(arg1.compute(context)).thenReturn(1);
        when(arg2.compute(context)).thenReturn(1);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_NonEqualValues() {
        when(arg1.compute(context)).thenReturn(1);
        when(arg2.compute(context)).thenReturn(2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_NullValues() {
        when(arg1.compute(context)).thenReturn(null);
        when(arg2.compute(context)).thenReturn(null);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_DifferentDataTypes() {
        when(arg1.compute(context)).thenReturn("Test String");
        when(arg2.compute(context)).thenReturn(1.5);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_Iterators() {
        when(arg1.compute(context)).thenReturn(it1);
        when(arg2.compute(context)).thenReturn(it2);
        when(it1.hasNext()).thenReturn(true);
        when(it1.next()).thenReturn(1);
        when(it2.hasNext()).thenReturn(true);
        when(it2.next()).thenReturn(1);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_Collections() {
        when(arg1.compute(context)).thenReturn(list1);
        when(arg2.compute(context)).thenReturn(list2);
        list1.add(1);
        list2.add(1);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_EmptyCollections() {
        when(arg1.compute(context)).thenReturn(Collections.emptyList());
        when(arg2.compute(context)).thenReturn(Collections.emptyList());
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_NullCollections() {
        when(arg1.compute(context)).thenReturn(null);
        when(arg2.compute(context)).thenReturn(null);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains_ValidIterator() {
        when(arg1.compute(context)).thenReturn(it1);
        when(it1.hasNext()).thenReturn(true);
        when(it1.next()).thenReturn(1);
        when(arg2.compute(context)).thenReturn(1);
        assertTrue(coreOperationCompare.contains(it1, arg2));
    }

    @Test
    public void testContains_EmptyIterator() {
        when(arg1.compute(context)).thenReturn(it1);
        when(it1.hasNext()).thenReturn(false);
        when(arg2.compute(context)).thenReturn(1);
        assertFalse(coreOperationCompare.contains(it1, arg2));
    }

    @Test
    public void testContains_NullIterator() {
        when(arg1.compute(context)).thenReturn(it1);
        when(arg2.compute(context)).thenReturn(null);
        assertFalse(coreOperationCompare.contains(it1, arg2));
    }

    @Test
    public void testFindMatch_ValidIterators() {
        when(arg1.compute(context)).thenReturn(it1);
        when(it1.hasNext()).thenReturn(true);
        when(it1.next()).thenReturn(1);
        when(arg2.compute(context)).thenReturn(it2);
        when(it2.hasNext()).thenReturn(true);
        when(it2.next()).thenReturn(1);
        assertTrue(coreOperationCompare.findMatch(it1, it2));
    }

    @Test
    public void testFindMatch_EmptyIterators() {
        when(arg1.compute(context)).thenReturn(it1);
        when(it1.hasNext()).thenReturn(false);
        when(arg2.compute(context)).thenReturn(it2);
        when(it2.hasNext()).thenReturn(false);
        assertTrue(coreOperationCompare.findMatch(it1, it2));
    }

    @Test
    public void testFindMatch_NullIterators() {
        when(arg1.compute(context)).thenReturn(it1);
        when(arg2.compute(context)).thenReturn(null);
        assertFalse(coreOperationCompare.findMatch(it1, it2));
    }

}