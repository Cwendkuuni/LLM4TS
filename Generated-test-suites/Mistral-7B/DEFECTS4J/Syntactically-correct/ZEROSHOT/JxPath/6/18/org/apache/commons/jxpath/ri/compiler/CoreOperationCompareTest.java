package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    private CoreOperationCompare op = null;

    @org.junit.Before
    public void setUp() throws Exception {
        Expression arg1 = new LiteralExpression("1");
        Expression arg2 = new LiteralExpression("1");
        this.op = new CoreOperationCompare(arg1, arg2);
    }

    @org.junit.Test
    public void testEqual_SameValues() {
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(this.op.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_DifferentObjects() {
        EvalContext context = new EvalContext(new InitialContext());
        Expression arg1 = new LiteralExpression("1");
        Expression arg2 = new LiteralExpression("2");
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertFalse(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_SameCollection() {
        EvalContext context = new EvalContext(new InitialContext());
        Collection coll1 = new ArrayList();
        Collection coll2 = new ArrayList();
        coll1.add(new Integer(1));
        coll2.add(new Integer(1));
        Expression arg1 = new CollectionExpression(coll1);
        Expression arg2 = new CollectionExpression(coll2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertTrue(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_DifferentCollections() {
        EvalContext context = new EvalContext(new InitialContext());
        Collection coll1 = new ArrayList();
        Collection coll2 = new ArrayList();
        coll1.add(new Integer(1));
        coll2.add(new Integer(2));
        Expression arg1 = new CollectionExpression(coll1);
        Expression arg2 = new CollectionExpression(coll2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertFalse(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_SameIterators() {
        EvalContext context = new EvalContext(new InitialContext());
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        Iterator it1 = list.iterator();
        Iterator it2 = list.iterator();
        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertTrue(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_DifferentIterators() {
        EvalContext context = new EvalContext(new InitialContext());
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        list1.add(new Integer(1));
        list2.add(new Integer(2));
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertFalse(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testContains() {
        EvalContext context = new EvalContext(new InitialContext());
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        Iterator it = list.iterator();
        Expression arg = new IteratorExpression(it);
        Expression value = new LiteralExpression("1");
        CoreOperationCompare compareOp = new CoreOperationCompare(arg, value);
        assertTrue(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testContains_False() {
        EvalContext context = new EvalContext(new InitialContext());
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        Iterator it = list.iterator();
        Expression arg = new IteratorExpression(it);
        Expression value = new LiteralExpression("3");
        CoreOperationCompare compareOp = new CoreOperationCompare(arg, value);
        assertFalse(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new InitialContext());
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        list2.add(new Integer(1));
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertTrue(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testFindMatch_False() {
        EvalContext context = new EvalContext(new InitialContext());
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        list2.add(new Integer(3));
        list2.add(new Integer(2));
        list2.add(new Integer(1));
        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Expression arg1 = new IteratorExpression(it1);
        Expression arg2 = new IteratorExpression(it2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertFalse(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_SamePointers() {
        EvalContext context = new EvalContext(new InitialContext());
        Pointer p1 = new Pointer("1");
        Pointer p2 = new Pointer("1");
        Expression arg1 = new PointerExpression(p1);
        Expression arg2 = new PointerExpression(p2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertTrue(compareOp.evaluate(context).getBooleanValue());
    }

    @org.junit.Test
    public void testEqual_DifferentPointers() {
        EvalContext context = new EvalContext(new InitialContext());
        Pointer p1 = new Pointer("1");
        Pointer p2 = new Pointer("2");
        Expression arg1 = new PointerExpression(p1);
        Expression arg2 = new PointerExpression(p2);
        CoreOperationCompare compareOp = new CoreOperationCompare(arg1, arg2);
        assertFalse(compareOp.evaluate(context).getBooleanValue());
    }

}