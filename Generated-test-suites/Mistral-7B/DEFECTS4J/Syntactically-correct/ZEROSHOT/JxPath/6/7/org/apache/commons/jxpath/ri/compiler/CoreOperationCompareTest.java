package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new LiteralExpression(new InitialContext(), new String("1"));
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("1"));
        CoreOperationCompare coreOperationCompare = new EqualExpression(arg1, arg2);
        EvalContext context = new EvalContext(new MockInfoSet());

        Assert.assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testNotEqual() {
        Expression arg1 = new LiteralExpression(new InitialContext(), new String("1"));
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("2"));
        CoreOperationCompare coreOperationCompare = new NotEqualExpression(arg1, arg2);
        EvalContext context = new EvalContext(new MockInfoSet());

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testEqual_Collection() {
        Collection collection = new ArrayList();
        collection.add(new String("1"));
        collection.add(new String("1"));

        Expression arg1 = new VariableExpression(new InitialContext(), "collection");
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("1"));
        CoreOperationCompare coreOperationCompare = new EqualExpression(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet()) {
            @Override
            public Object getSingleNodePointer() {
                return collection;
            }
        };

        Assert.assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testNotEqual_Collection() {
        Collection collection = new ArrayList();
        collection.add(new String("1"));
        collection.add(new String("2"));

        Expression arg1 = new VariableExpression(new InitialContext(), "collection");
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("1"));
        CoreOperationCompare coreOperationCompare = new NotEqualExpression(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet()) {
            @Override
            public Object getSingleNodePointer() {
                return collection;
            }
        };

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testEqual_Iterator() {
        Collection collection = new ArrayList();
        collection.add(new String("1"));
        collection.add(new String("1"));

        Expression arg1 = new VariableExpression(new InitialContext(), "collection");
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("1"));
        CoreOperationCompare coreOperationCompare = new EqualExpression(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet()) {
            @Override
            public Iterator getSingleNodeIterator() {
                return collection.iterator();
            }
        };

        Assert.assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testNotEqual_Iterator() {
        Collection collection = new ArrayList();
        collection.add(new String("1"));
        collection.add(new String("2"));

        Expression arg1 = new VariableExpression(new InitialContext(), "collection");
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("1"));
        CoreOperationCompare coreOperationCompare = new NotEqualExpression(arg1, arg2);

        EvalContext context = new EvalContext(new MockInfoSet()) {
            @Override
            public Iterator getSingleNodeIterator() {
                return collection.iterator();
            }
        };

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testEqual_SelfContext() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();

        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new MockInfoSet());

        Assert.assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testNotEqual_SelfContext() {
        Expression left = new SelfContext();
        Expression right = new InitialContext();

        CoreOperationCompare coreOperationCompare = new NotEqualExpression(left, right);
        EvalContext context = new EvalContext(new MockInfoSet());

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testEqual_Pointer() {
        Expression left = new VariableExpression(new InitialContext(), "pointer");
        Expression right = new LiteralExpression(new InitialContext(), new String("1"));

        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new MockInfoSet()) {
            @Override
            public Pointer getSingleNodePointer() {
                return new Pointer(new String("path"), new String("value"));
            }
        };

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testNotEqual_Pointer() {
        Expression left = new VariableExpression(new InitialContext(), "pointer");
        Expression right = new LiteralExpression(new InitialContext(), new String("2"));

        CoreOperationCompare coreOperationCompare = new NotEqualExpression(left, right);
        EvalContext context = new EvalContext(new MockInfoSet()) {
            @Override
            public Pointer getSingleNodePointer() {
                return new Pointer(new String("path"), new String("value"));
            }
        };

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testEqual_DifferentTypes() {
        Expression arg1 = new LiteralExpression(new InitialContext(), new Integer(1));
        Expression arg2 = new LiteralExpression(new InitialContext(), new String("1"));
        CoreOperationCompare coreOperationCompare = new EqualExpression(arg1, arg2);
        EvalContext context = new EvalContext(new MockInfoSet());

        Assert.assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testEqual_Null() {
        Expression arg1 = new LiteralExpression(new InitialContext(), null);
        Expression arg2 = new LiteralExpression(new InitialContext(), null);
        CoreOperationCompare coreOperationCompare = new EqualExpression(arg1, arg2);
        EvalContext context = new EvalContext(new MockInfoSet());

        Assert.assertTrue(coreOperationCompare.compute(context));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEqual_UnsupportedOperation() {
        Expression arg1 = new LiteralExpression(new InitialContext(), new String("value"));
        Expression arg2 = new LiteralExpression(new InitialContext(), new Integer(1));
        CoreOperationCompare coreOperationCompare = new EqualExpression(arg1, arg2);
        EvalContext context = new EvalContext(new MockInfoSet());

        coreOperationCompare.compute(context);
    }
}

class MockInfoSet {
}