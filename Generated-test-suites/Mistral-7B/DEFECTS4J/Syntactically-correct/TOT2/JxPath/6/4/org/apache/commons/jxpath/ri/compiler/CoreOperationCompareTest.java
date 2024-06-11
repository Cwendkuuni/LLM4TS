
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    private static EvalContext context = new EvalContext();

    @Test
    public void testEqual() {
        // Test case from Expert 1
        CoreOperationCompare compare = new CoreOperationCompare(
            new StringExpression("5"),
            new StringExpression("5")
        );
        assertEquals(true, compare.evaluate(context));

        // Test case from Expert 1
        compare = new CoreOperationCompare(
            new NumberExpression(5.0),
            new NumberExpression(5.0)
        );
        assertEquals(true, compare.evaluate(context));

        // Test case from Expert 1
        compare = new CoreOperationCompare(
            new StringExpression("Hello"),
            new StringExpression("Hello")
        );
        assertEquals(true, compare.evaluate(context));

        // Test case from Expert 1
        compare = new CoreOperationCompare(
            new NumberExpression(5.0),
            new NumberExpression(6.0)
        );
        assertEquals(false, compare.evaluate(context));

        // Test case from Expert 1
        compare = new CoreOperationCompare(
            new StringExpression("Hello"),
            new StringExpression("World")
        );
        assertEquals(false, compare.evaluate(context));

        // Test case for NaN
        compare = new CoreOperationCompare(
            new NumberExpression(Double.NaN),
            new NumberExpression(Double.NaN)
        );
        assertEquals(true, compare.evaluate(context));

        // Test case for infinities
        compare = new CoreOperationCompare(
            new NumberExpression(Double.POSITIVE_INFINITY),
            new NumberExpression(Double.POSITIVE_INFINITY)
        );
        assertEquals(true, compare.evaluate(context));

        compare = new CoreOperationCompare(
            new NumberExpression(Double.POSITIVE_INFINITY),
            new NumberExpression(Double.NEGATIVE_INFINITY)
        );
        assertEquals(false, compare.evaluate(context));

        // Test case for empty strings
        compare = new CoreOperationCompare(
            new StringExpression(""),
            new StringExpression("")
        );
        assertEquals(true, compare.evaluate(context));

        compare = new CoreOperationCompare(
            new StringExpression(""),
            new StringExpression(" ")
        );
        assertEquals(false, compare.evaluate(context));

        // Test case from Expert 1 for contains(Iterator, Object)
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        CoreOperationCompare compareContains = new CoreOperationCompare(
            new IteratorExpression(list1.iterator()),
            createNumberConst(4)
        );
        assertEquals(true, compareContains.evaluate(context));

        // Test case from Expert 1 for findMatch(Iterator, Iterator)
        List<Integer> list1_1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 3, 5, 4, 2);

        CoreOperationCompare compareFindMatch = new CoreOperationCompare(
            new IteratorExpression(list1_1.iterator()),
            new IteratorExpression(list2.iterator())
        );
        assertEquals(true, compareFindMatch.evaluate(context));

        list1_1 = Arrays.asList(1, 2, 3, 4, 5);
        list2 = Arrays.asList(2, 3, 5, 4, 1);

        compareFindMatch = new CoreOperationCompare(
            new IteratorExpression(list1_1.iterator()),
            new IteratorExpression(list2.iterator())
        );
        assertEquals(true, compareFindMatch.evaluate(context));

        list1_1 = Arrays.asList(1, 2, 3, 4, 5);
        list2 = Arrays.asList(1, 3, 5, 6, 2);

        compareFindMatch = new CoreOperationCompare(
            new IteratorExpression(list1_1.iterator()),
            new IteratorExpression(list2.iterator())
        );
        assertEquals(false, compareFindMatch.evaluate(context));

        list1_1 = Arrays.asList(1, 2, 3, 4, 5);
        list2 = Collections.emptyList();

        compareFindMatch = new CoreOperationCompare(
            new IteratorExpression(list1_1.iterator()),
            new IteratorExpression(list2.iterator())
        );
        assertEquals(false, compareFindMatch.evaluate(context));

        // Test case from Expert 3 for equal(Object, Object) with null arguments
        CoreOperationCompare compareEqualNull = new CoreOperationCompare(
            null,
            createNumberConst(5)
        );
        assertEquals(false, compareEqualNull.evaluate(context));

        compareEqualNull = new CoreOperationCompare(
            createStringConst("5"),
            null
        );
        assertEquals(false, compareEqualNull.evaluate(context));

        // Test case from Expert 3 for equal(Object, Object) with different classes
        CoreOperationCompare compareEqualDifferentClasses = new CoreOperationCompare(
            createNumberConst(5.0),
            createStringConst("5")
        );
        assertEquals(false, compareEqualDifferentClasses.evaluate(context));

        compareEqualDifferentClasses = new CoreOperationCompare(
            createStringConst("5"),
            createNumberConst(5)
        );
        assertEquals(false, compareEqualDifferentClasses.evaluate(context));
    }

}
