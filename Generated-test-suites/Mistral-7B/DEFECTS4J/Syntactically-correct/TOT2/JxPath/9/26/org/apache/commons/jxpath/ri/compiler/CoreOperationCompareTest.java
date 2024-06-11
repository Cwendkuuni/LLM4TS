package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.jupiter.api.*;

class CoreOperationCompareTest {

    @Test
    void testConstructor() {
        // Test constructor with valid arguments
        Expression arg1 = new Expression(new Constant(1));
        Expression arg2 = new Expression(new Constant(2));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    // Add test methods for each public method as proposed above

    @Test
    void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(1), new Constant(2));
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(1), new Constant(2));
        boolean isSymmetric = coreOperationCompare.isSymmetric();
        assertTrue(isSymmetric);
    }

    @Test
    void testEqualPrimitiveTypes() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(1);
        Expression right = new Constant(1);
        boolean result = new CoreOperationCompare(left, right).equal(context, left, right);
        assertTrue(result);
    }

    @Test
    void testEqualObjects() {
        EvalContext context = new EvalContext();
        Expression left = new Constant("hello");
        Expression right = new Constant("hello");
        boolean result = new CoreOperationCompare(left, right).equal(context, left, right);
        assertTrue(result);
    }

    @Test
    void testEqualIterators() {
        EvalContext context = new EvalContext();
        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(1, 2, 3).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(it1), new Constant(it2));
        boolean result = coreOperationCompare.equal(context, new Constant(it1), new Constant(it2));
        assertTrue(result);
    }

    @Test
    void testContains() {
        List<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3, 4));
        Iterator<Integer> it = list.iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(it), new Constant(1));
        boolean result = coreOperationCompare.contains(it, new Constant(1));
        assertTrue(result);
    }

    @Test
    void testFindMatch() {
        List<Integer> list1 = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new LinkedList<>(Arrays.asList(5, 6, 7, 4, 1));
        Iterator<Integer> it1 = list1.iterator();
        Iterator<Integer> it2 = list2.iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(it1), new Constant(it2));
        boolean result = coreOperationCompare.findMatch(it1, it2);
        assertTrue(result);
    }

    @Test
    void testEqualNullValues() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(null);
        Expression right = new Constant(null);
        boolean result = new CoreOperationCompare(left, right).equal(context, left, right);
        assertTrue(result);
    }

    @Test
    void testEqualDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression left = new Constant("hello");
        Expression right = new Constant(1);
        boolean result = new CoreOperationCompare(left, right).equal(context, left, right);
        assertFalse(result);
    }

    @Test
    void testEqualNaNValues() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(Double.NaN);
        Expression right = new Constant(Double.NaN);
        boolean result = new CoreOperationCompare(left, right).equal(context, left, right);
        assertFalse(result);
    }

    @Test
    void testContainsEmptyCollections() {
        Iterator<Integer> it = Collections.emptyIterator().iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Constant(it), new Constant(1));
        boolean result = coreOperationCompare.contains(it, new Constant(1));
        assertFalse(result);
    }

    @AfterEach
    void resetContext() {
        EvalContext context = new EvalContext();
        context.reset();
    }
}