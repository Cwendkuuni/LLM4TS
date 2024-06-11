import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.expression.Constant;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_SameValue() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant("1"), new Constant("1")
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_DifferentObjects() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant("1"), new Constant("2")
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqual_SamePointer() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Pointer(new Object()), new Pointer(new Object())
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_InvalidArguments() {
        CoreOperationCompare compare = new CoreOperationCompare(null, new Constant("1"));

        EvalContext context = new EvalContext();
        compare.evaluate(context);
    }

    @Test
    public void testEqual_BooleanTrueTrue() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(true), new Constant(true)
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_BooleanTrueFalse() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(true), new Constant(false)
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqual_BooleanFalseTrue() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(false), new Constant(true)
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqual_BooleanFalseFalse() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(false), new Constant(false)
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_Number() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(1.0), new Constant(1.0)
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_String() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant("test"), new Constant("test")
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_Null() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(null), new Constant(null)
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_DifferentTypes() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(1), new Constant("1")
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_InvalidArguments2() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null);

        EvalContext context = new EvalContext();
        compare.evaluate(context);
    }

    @Test
    public void testEqual_NullvsNull() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(null), new Constant(null)
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_StringvsNumber() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant("test"), new Constant(1)
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqual_StringvsStringDifferent() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant("test1"), new Constant("test2")
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testEqual_NumbervsString() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(1), new Constant("1")
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_EmptyCollectionvsEmptyCollection() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Expression[] {
                new CoreOperation(new Expression[] {
                    new Constant("1")
                }),
                new CoreOperation(new Expression[] {
                    new Constant("[]")
                })
            }
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testEqual_EmptyCollectionvsNonEmptyCollection() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Expression[] {
                new CoreOperation(new Expression[] {
                    new Constant("1")
                }),
                new CoreOperation(new Expression[] {
                    new Constant("2")
                })
            }
        );

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testContains_IteratorWithValue() {
        Object[] values = {1, 2, 3, 4};
        Iterator it = Arrays.asList(values).iterator();

        CoreOperationCompare compare = new CoreOperationCompare(it, new Constant(3));

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testContains_IteratorWithoutValue() {
        Object[] values = {1, 2, 3, 4};
        Iterator it = Arrays.asList(values).iterator();

        CoreOperationCompare compare = new CoreOperationCompare(it, new Constant(5));

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testFindMatch() {
        Object[] values1 = {1, 2, 3, 4};
        Iterator it1 = Arrays.asList(values1).iterator();

        Object[] values2 = {1, 3, 4, 2};
        Iterator it2 = Arrays.asList(values2).iterator();

        CoreOperationCompare compare = new CoreOperationCompare(it1, it2);

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testFindMatch_EmptyIterators() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Expression[] {
                new CoreOperation(new Expression[] {
                    new Constant("[]")
                }),
                new CoreOperation(new Expression[] {
                    new Constant("[]")
                })
            }
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }

    @Test
    public void testFindMatch_OneEmptyIterator() {
        Object[] values1 = {1, 2, 3, 4};
        Iterator it1 = Arrays.asList(values1).iterator();

        CoreOperationCompare compare = new CoreOperationCompare(it1, new Constant("[]"));

        EvalContext context = new EvalContext();
        assertEquals(false, compare.evaluate(context));
    }

    @Test
    public void testFindMatch_OneEmptyIterator_EmptyCollection() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Expression[] {
                new CoreOperation(new Expression[] {
                    new Constant("[]")
                }),
                new Constant("[]")
            }
        );

        EvalContext context = new EvalContext();
        assertEquals(true, compare.evaluate(context));
    }
}