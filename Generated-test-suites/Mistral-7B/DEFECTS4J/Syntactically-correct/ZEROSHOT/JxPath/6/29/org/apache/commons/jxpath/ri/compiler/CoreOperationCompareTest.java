package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Pointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CoreOperationCompare.class})
public class CoreOperationCompareTest {

    private EvalContext context;
    private CoreOperationCompare compare;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        context = Mockito.spy(new EvalContext() {
            @Override
            public InfoSet getInfoSet() {
                return Mockito.mock(InfoSet.class);
            }

            @Override
            public boolean isContextSet() {
                return false;
            }

            @Override
            public void setContext(String name, Object value) {

            }

            @Override
            public void setContext(String name, Iterator iterator) {

            }

            @Override
            public void setContext(String name, NodeInfo nodeInfo) {

            }
        });
        arg1 = Mockito.spy(new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        });
        arg2 = Mockito.spy(new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        });
        compare = PowerMockito.spy(new CoreOperationCompare(arg1, arg2));
    }

    @Test
    public void testEqual() {
        Pointer pointer1 = Mockito.mock(Pointer.class);
        Pointer pointer2 = Mockito.mock(Pointer.class);
        Object nullValue = null;
        boolean result;

        // test with pointers
        PowerMockito.when(pointer1.getValue()).thenReturn(new Object());
        PowerMockito.when(pointer2.getValue()).thenReturn(new Object());
        PowerMockito.when(compare.equal(context, arg1, arg2)).thenCallRealMethod();
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        PowerMockito.when(pointer1.getValue()).thenReturn(pointer1);
        PowerMockito.when(pointer2.getValue()).thenReturn(pointer2);
        PowerMockito.when(compare.equal(context, arg1, arg2)).thenCallRealMethod();
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        PowerMockito.when(compare.equal(context, arg1, arg2)).thenReturn(true);
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        PowerMockito.when(compare.equal(context, arg1, arg2)).thenReturn(false);
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        // test with null values
        PowerMockito.when(compare.equal(context, arg1, arg2)).thenCallRealMethod();
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        PowerMockito.when(arg1.compute(context)).thenReturn(nullValue);
        PowerMockito.when(arg2.compute(context)).thenReturn(nullValue);
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        PowerMockito.when(arg1.compute(context)).thenReturn(nullValue);
        PowerMockito.when(arg2.compute(context)).thenReturn(new Object());
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        PowerMockito.when(arg1.compute(context)).thenReturn(new Object());
        PowerMockito.when(arg2.compute(context)).thenReturn(nullValue);
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        // test with collections
        List<Object> collection1 = new ArrayList<>();
        List<Object> collection2 = new ArrayList<>();
        collection1.add(new Object());
        collection2.add(new Object());

        PowerMockito.when(arg1.compute(context)).thenReturn(collection1.iterator());
        PowerMockito.when(arg2.compute(context)).thenReturn(collection2.iterator());
        PowerMockito.when(compare.contains((Iterator) Mockito.any(), Mockito.any())).thenCallRealMethod();
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        collection1.add(new Object());
        collection2.clear();
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        collection2.add(new Object());
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        collection1.clear();
        collection2.clear();
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        // test with iterators
        Iterator<Object> iterator1 = new ArrayList<>(Collections.singletonList(new Object())).iterator();
        Iterator<Object> iterator2 = new ArrayList<>(Collections.singletonList(new Object())).iterator();

        PowerMockito.when(arg1.compute(context)).thenReturn(iterator1);
        PowerMockito.when(arg2.compute(context)).thenReturn(iterator2);
        PowerMockito.when(compare.findMatch((Iterator) Mockito.any(), (Iterator) Mockito.any())).thenCallRealMethod();
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        iterator1 = new ArrayList<>(Collections.singletonList(new Object())).iterator();
        iterator2 = Collections.emptyIterator();
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        iterator1 = Collections.emptyIterator();
        iterator2 = new ArrayList<>(Collections.singletonList(new Object())).iterator();
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);

        iterator1 = new ArrayList<>(Collections.singletonList(new Object())).iterator();
        iterator2 = new ArrayList<>(Collections.singletonList(new Object())).iterator();

        PowerMockito.when(compare.findMatch((Iterator) Mockito.any(), (Iterator) Mockito.any())).thenReturn(true);
        result = compare.equal(context, arg1, arg2);
        Assert.assertTrue(result);

        PowerMockito.when(compare.findMatch((Iterator) Mockito.any(), (Iterator) Mockito.any())).thenReturn(false);
        result = compare.equal(context, arg1, arg2);
        Assert.assertFalse(result);
    }

}