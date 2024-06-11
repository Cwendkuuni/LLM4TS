package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.FunctionContext;
import org.apache.commons.jxpath.FunctionContextFactory;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.junit.Test;

public class CoreOperationCompareTest extends CoreOperationTest {

    @Test
    public void testEqual() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createNumber(1);
        Expression arg2 = createNumber(1);

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", arg1, arg2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequal() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createNumber(1);
        Expression arg2 = createNumber(2);

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);

        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualIterator() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(1, 2, 3).iterator();

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", it1, it2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalIterator() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(1, 2, 4).iterator();

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", it1, it2);

        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualString() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createString("hello");
        Expression arg2 = createString("hello");

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", arg1, arg2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalString() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createString("hello");
        Expression arg2 = createString("world");

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);

        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualCollection() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", createCollection(list1), createCollection(list2));

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalCollection() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(4);

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", createCollection(list1), createCollection(list2));

        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualSelfContext() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();
        EvalContext evalContext = new EvalContext(context);

        SelfContext selfContext1 = new SelfContext(evalContext, new Pointer(null, 0));
        SelfContext selfContext2 = new SelfContext(evalContext, new Pointer(null, 0));

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", selfContext1, selfContext2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalSelfContext() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();
        EvalContext evalContext = new EvalContext(context);

        SelfContext selfContext1 = new SelfContext(evalContext, new Pointer(null, 0));
        SelfContext selfContext2 = new SelfContext(evalContext, new Pointer(null, 1));

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", selfContext1, selfContext2);

        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testUnequalNullAndString() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createNull();
        Expression arg2 = createString("hello");

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalStringAndNull() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createString("hello");
        Expression arg2 = createNull();

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalNullAndNull() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createNull();
        Expression arg2 = createNull();

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualNullAndNull() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createNull();
        Expression arg2 = createNull();

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", arg1, arg2);

        assertTrue(compare.evaluate(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComparePointerWithNull() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = new Pointer(null, 0);
        Expression arg2 = createNull();

        new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNullWithPointer() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();

        Expression arg1 = createNull();
        Expression arg2 = new Pointer(null, 0);

        new CoreOperationCompareFactory().createCompareOperation("!=", arg1, arg2);
    }

    @Test
    public void testEqualPointerWithPointer() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();
        EvalContext evalContext = new EvalContext(context);

        Pointer pointer1 = new Pointer(null, 0);
        Pointer pointer2 = new Pointer(null, 0);

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("=", pointer1, pointer2);

        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testUnequalPointerWithPointer() {
        FunctionContextFactory factory = new FunctionContextFactory();
        FunctionContext context = factory.createFunctionContext();
        EvalContext evalContext = new EvalContext(context);

        Pointer pointer1 = new Pointer(null, 0);
        Pointer pointer2 = new Pointer(null, 1);

        CoreOperationCompare compare = new CoreOperationCompareFactory().createCompareOperation("!=", pointer1, pointer2);

        assertFalse(compare.evaluate(context));
    }

    private Expression createCollection(Collection<?> collection) {
        InitialContext context = new InitialContext(Collections.EMPTY_MAP, new Pointer(null, 0), collection.iterator());
        return context;
    }

    private Expression createString(String string) {
        return new StringExpression(string);
    }

    private Expression createNumber(Number number) {
        return new NumberExpression(number);
    }

    private Expression createNull() {
        return new NullExpression();
    }

}