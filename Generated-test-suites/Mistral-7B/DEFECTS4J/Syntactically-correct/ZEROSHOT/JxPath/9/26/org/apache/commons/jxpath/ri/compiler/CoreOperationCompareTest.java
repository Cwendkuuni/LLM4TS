package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Test cases for CoreOperationCompare class.
 */
public class CoreOperationCompareTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationCompare() {
        // Test case for equal method with simple types
        CoreOperationCompare compare1 = new CoreOperationCompare(
                new Constant("1"), new Constant("1"));
        EvalContext context = new EvalContext();
        JXPathContext jxpathContext = JXPathContext.newContext(context);
        assertTrue(compare1.evaluate(jxpathContext).booleanValue());

        // Test case for equal method with complex types - iterators
        Expression expr1 = new Variable("list1");
        Expression expr2 = new Variable("list2");
        Collection list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        Collection list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        CoreOperationCompare compare2 = new CoreOperationCompare(expr1, expr2);
        context.setValues(new Object[] { list1, list2 });
        jxpathContext = JXPathContext.newContext(context);
        assertTrue(compare2.evaluate(jxpathContext).booleanValue());

        // Test case for equal method with complex types - different collections
        Collection list3 = new ArrayList();
        list3.add(new Integer(1));
        list3.add(new Integer(3));
        context.setValues(new Object[] { list1, list3 });
        assertFalse(compare2.evaluate(jxpathContext).booleanValue());

        // Test case for equal method with complex types - null value
        Expression expr3 = new Variable("list1");
        Collection list4 = null;
        context.setValues(new Object[] { list1, list4 });
        assertFalse(compare2.evaluate(jxpathContext).booleanValue());

        // Test case for equals method with pointer types
        Pointer pointer1 = new Pointer("/test[1]/name");
        Pointer pointer2 = new Pointer("/test[1]/name");
        CoreOperationCompare compare3 = new CoreOperationCompare(pointer1, pointer2);
        JXPathContext jxpathContext1 = JXPathContext.newContext(context);
        jxpathContext1.setNode(new Object() {
            @Override
            public Iterator iterator() {
                return new Iterator() {
                    private Pointer current = pointer1;

                    @Override
                    public boolean hasNext() {
                        return current != null;
                    }

                    @Override
                    public Object next() {
                        Pointer result = current;
                        current = current != null ? current.getNextSibling() : null;
                        return result;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        });
        assertTrue(compare3.evaluate(jxpathContext1).booleanValue());

        // Test case for equals method with pointer types - different pointers
        Pointer pointer3 = new Pointer("/test[2]/name");
        CoreOperationCompare compare4 = new CoreOperationCompare(pointer1, pointer3);
        JXPathContext jxpathContext2 = JXPathContext.newContext(context);
        jxpathContext2.setNode(new Object() {
            @Override
            public Iterator iterator() {
                return new Iterator() {
                    private Pointer current = pointer1;

                    @Override
                    public boolean hasNext() {
                        return current != null;
                    }

                    @Override
                    public Object next() {
                        Pointer result = current;
                        current = current != null ? current.getNextSibling() : null;
                        return result;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        });
        assertFalse(compare4.evaluate(jxpathContext2).booleanValue());
    }

}