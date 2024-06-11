package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationCompare compareOp;
 private EvaluationContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 compareOp = Mockito.spy(new CoreOperationCompare(arg1, arg2));
 context = Mockito.spy(new EvaluationContext() {
 @Override
 public Pointer getSingleNodePointer() {
 return Mockito.mock(Pointer.class);
 }
 });
 }

 @Test
 public void testEqualBooleanValues() {
 Mockito.when(arg1.compute(context)).thenReturn(true);
 Mockito.when(arg2.compute(context)).thenReturn(true);
 assertTrue(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualStringValues() {
 Mockito.when(arg1.compute(context)).thenReturn("test");
 Mockito.when(arg2.compute(context)).thenReturn("test");
 assertTrue(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualNumbers() {
 Mockito.when(arg1.compute(context)).thenReturn(1.0);
 Mockito.when(arg2.compute(context)).thenReturn(1.0);
 assertTrue(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualObjects() {
 Mockito.when(arg1.compute(context)).thenReturn(new Object());
 Mockito.when(arg2.compute(context)).thenReturn(new Object());
 assertTrue(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualCollections() {
 List<Object> list1 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 List<Object> list2 = new ArrayList<>();
 list2.add(1);
 list2.add(2);
 Mockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertTrue(compareOp.findMatch(list1.iterator(), list2.iterator()));
 }

 @Test
 public void testEqualInitialContexts() {
 Mockito.when(arg1.compute(context)).thenReturn(new InitialContext());
 Mockito.when(arg2.compute(context)).thenReturn(new InitialContext());
 assertTrue(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualSelfContexts() {
 Mockito.when(arg1.compute(context)).thenReturn(new SelfContext());
 Mockito.when(arg2.compute(context)).thenReturn(new SelfContext());
 assertTrue(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testNotEqualBooleanValues() {
 Mockito.when(arg1.compute(context)).thenReturn(true);
 Mockito.when(arg2.compute(context)).thenReturn(false);
 assertFalse(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testNotEqualStringValues() {
 Mockito.when(arg1.compute(context)).thenReturn("test1");
 Mockito.when(arg2.compute(context)).thenReturn("test2");
 assertFalse(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testNotEqualNumbers() {
 Mockito.when(arg1.compute(context)).thenReturn(1.0);
 Mockito.when(arg2.compute(context)).thenReturn(2.0);
 assertFalse(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testNotEqualObjects() {
 Mockito.when(arg1.compute(context)).thenReturn(new Object());
 Mockito.when(arg2.compute(context)).thenReturn(new Object());
 assertFalse(compareOp.equal(context, arg1, arg2));
 }

 @Test
 public void testNotEqualCollections() {
 List<Object> list1 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 List<Object> list2 = new ArrayList<>();
 list2.add(3);
 list2.add(4);
 Mockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertFalse(compareOp.findMatch(list1.iterator(), list2.iterator()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testExceptionWhenComparingNotIteratorObjects() {
 Mockito.when(arg1.compute(context)).thenReturn("test");
 Mockito.when(arg2.compute(context)).thenReturn("test");
 compareOp.equal(context, arg1, arg2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testExceptionWhenComparingNotIteratorCollections() {
 Mockito.when(arg1.compute(context)).thenReturn(new ArrayList<>());
 Mockito.when(arg2.compute(context)).thenReturn(new ArrayList<>());
 compareOp.findMatch(Mockito.mock(Iterator.class), Mockito.mock(Iterator.class));
 }

}