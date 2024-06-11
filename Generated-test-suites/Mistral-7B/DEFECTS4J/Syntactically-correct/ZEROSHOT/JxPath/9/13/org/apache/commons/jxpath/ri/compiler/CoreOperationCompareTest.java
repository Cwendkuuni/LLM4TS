package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompareTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest extends CoreOperationTest {

    private CoreOperationCompare op;

    @Before
    public void setUp() {
        Expression arg1 = new LiteralExpression(new Integer(1));
        Expression arg2 = new LiteralExpression(new Integer(1));
        op = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNotEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Expression arg1 = new LiteralExpression(new Integer(1));
        Expression arg2 = new LiteralExpression(new Integer(2));
        CoreOperationCompare opNE = new CoreOperationCompare(arg1, arg2);
        assertFalse(opNE.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualCollections() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        List<Integer> list1 = new ArrayList<>();
        list1.add(new Integer(1));
        List<Integer> list2 = new ArrayList<>();
        list2.add(new Integer(1));
        Expression arg1 = new CollectionExpression(list1);
        Expression arg2 = new CollectionExpression(list2);
        op = new CoreOperationCompare(arg1, arg2);
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNotEqualCollections() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        List<Integer> list1 = new ArrayList<>();
        list1.add(new Integer(1));
        List<Integer> list2 = new ArrayList<>();
        list2.add(new Integer(2));
        Expression arg1 = new CollectionExpression(list1);
        Expression arg2 = new CollectionExpression(list2);
        CoreOperationCompare opNE = new CoreOperationCompare(arg1, arg2);
        assertFalse(opNE.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualSets() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Set<Integer> set1 = new HashSet<>();
        set1.add(new Integer(1));
        Set<Integer> set2 = new HashSet<>();
        set2.add(new Integer(1));
        Expression arg1 = new CollectionExpression(set1);
        Expression arg2 = new CollectionExpression(set2);
        op = new CoreOperationCompare(arg1, arg2);
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNotEqualSets() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Set<Integer> set1 = new HashSet<>();
        set1.add(new Integer(1));
        Set<Integer> set2 = new HashSet<>();
        set2.add(new Integer(2));
        Expression arg1 = new CollectionExpression(set1);
        Expression arg2 = new CollectionExpression(set2);
        CoreOperationCompare opNE = new CoreOperationCompare(arg1, arg2);
        assertFalse(opNE.evaluate(context).booleanValue());
    }

    @Test
    public void testEqualPointers() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Pointer p1 = new Pointer(new InitialContext(), "a/b[1]/c[1]/d[1]/e[1]", null);
        Pointer p2 = new Pointer(new InitialContext(), "a/b[1]/c[1]/d[1]/e[1]", null);
        op = new CoreOperationCompare(new LiteralExpression(p1), new LiteralExpression(p2));
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNotEqualPointers() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Pointer p1 = new Pointer(new InitialContext(), "a/b[1]/c[1]/d[1]/e[1]", null);
        Pointer p2 = new Pointer(new InitialContext(), "a/b[1]/c[2]/d[1]/e[1]", null);
        CoreOperationCompare opNE = new CoreOperationCompare(new LiteralExpression(p1), new LiteralExpression(p2));
        assertFalse(opNE.evaluate(context).booleanValue());
    }

    @Test
    public void testNullEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(null);
        op = new CoreOperationCompare(arg1, arg2);
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNullNotEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Integer(1));
        CoreOperationCompare opNE = new CoreOperationCompare(arg1, arg2);
        assertFalse(opNE.evaluate(context).booleanValue());
    }

    @Test
    public void testEmptyAndNonEmptyIteratorEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Iterator<Integer> emptyIterator = Collections.emptyIterator();
        Iterator<Integer> nonEmptyIterator = new ArrayList<Integer>().iterator();
        Expression arg1 = new IteratorExpression(emptyIterator);
        Expression arg2 = new IteratorExpression(nonEmptyIterator);
        op = new CoreOperationCompare(arg1, arg2);
        assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEmptyAndEmptyIteratorEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Iterator<Integer> emptyIterator1 = Collections.emptyIterator();
        Iterator<Integer> emptyIterator2 = Collections.emptyIterator();
        Expression arg1 = new IteratorExpression(emptyIterator1);
        Expression arg2 = new IteratorExpression(emptyIterator2);
        op = new CoreOperationCompare(arg1, arg2);
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testEmptyAndEmptySetEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Set<Integer> emptySet1 = Collections.emptySet();
        Set<Integer> emptySet2 = Collections.emptySet();
        Expression arg1 = new CollectionExpression(emptySet1);
        Expression arg2 = new CollectionExpression(emptySet2);
        op = new CoreOperationCompare(arg1, arg2);
        assertTrue(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNullAndEmptySetEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Set<Integer> emptySet = Collections.emptySet();
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new CollectionExpression(emptySet);
        op = new CoreOperationCompare(arg1, arg2);
        assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNullAndEmptyIteratorEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Iterator<Integer> emptyIterator = Collections.emptyIterator();
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new IteratorExpression(emptyIterator);
        op = new CoreOperationCompare(arg1, arg2);
        assertFalse(op.evaluate(context).booleanValue());
    }

    @Test
    public void testNullAndNonEmptyIteratorEqual() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Iterator<Integer> nonEmptyIterator = new ArrayList<Integer>().iterator();
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new IteratorExpression(nonEmptyIterator);
        op = new CoreOperationCompare(arg1, arg2);
        assertFalse(op.evaluate(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLeftOperand() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Integer(1));
        new CoreOperationCompare(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRightOperand() {
        EvalContext context = new EvalContext(new InfoSet(), null);
        Expression arg1 = new LiteralExpression(new Integer(1));
        Expression arg2 = new LiteralExpression(null);
        new CoreOperationCompare(arg1, arg2);
    }
}