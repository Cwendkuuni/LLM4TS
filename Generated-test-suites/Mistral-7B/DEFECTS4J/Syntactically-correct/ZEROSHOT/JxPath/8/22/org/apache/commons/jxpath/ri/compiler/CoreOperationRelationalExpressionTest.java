package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.IteratorsTestHelper;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGT;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 private EvalContext context;
 private InfoSet infoSet;
 private Axis axis;

 @Before
 public void setUp() {
 infoSet = new InfoSet();
 context = new EvalContext(new SelfContext(Axis.SELF, null), infoSet);
 axis = new Axis();
 }

 @Test
 public void testGT() {
 CoreOperationGT gt = new CoreOperationGT(new CoreIntLiteral(1), new CoreIntLiteral(2));
 assertEquals(Boolean.FALSE, gt.computeValue(context));
 context.setNodePointer(IteratorsTestHelper.createNodeIterator(new Object[]{"1", "2", "3", "4"}));
 Iterators it = Iterators.iterate(axis, new Object[]{context, infoSet, null});
 gt.setContext(context);
 gt.setAxis(axis);
 assertTrue(gt.computeValue(context));
 it.next();
 it.next();
 assertFalse(gt.computeValue(context));
 }

 @Test
 public void testGTE() {
 CoreOperationRelationalExpression gte = new CoreOperationGTE(new CoreIntLiteral(1), new CoreIntLiteral(2));
 assertEquals(Boolean.FALSE, gte.computeValue(context));
 context.setNodePointer(IteratorsTestHelper.createNodeIterator(new Object[]{"1", "2", "3", "4"}));
 Iterators it = Iterators.iterate(axis, new Object[]{context, infoSet, null});
 gte.setContext(context);
 gte.setAxis(axis);
 it.next();
 it.next();
 assertTrue(gte.computeValue(context));
 it.next();
 assertFalse(gte.computeValue(context));
 }

 @Test
 public void testLT() {
 CoreOperationRelationalExpression lt = new CoreOperationLT(new CoreIntLiteral(1), new CoreIntLiteral(2));
 assertEquals(Boolean.FALSE, lt.computeValue(context));
 context.setNodePointer(IteratorsTestHelper.createNodeIterator(new Object[]{"1", "2", "3", "4"}));
 Iterators it = Iterators.iterate(axis, new Object[]{context, infoSet, null});
 lt.setContext(context);
 lt.setAxis(axis);
 assertFalse(lt.computeValue(context));
 it.next();
 it.next();
 assertTrue(lt.computeValue(context));
 }

 @Test
 public void testLTE() {
 CoreOperationRelationalExpression lte = new CoreOperationLTE(new CoreIntLiteral(1), new CoreIntLiteral(2));
 assertEquals(Boolean.FALSE, lte.computeValue(context));
 context.setNodePointer(IteratorsTestHelper.createNodeIterator(new Object[]{"1", "2", "3", "4"}));
 Iterators it = Iterators.iterate(axis, new Object[]{context, infoSet, null});
 lte.setContext(context);
 lte.setAxis(axis);
 assertFalse(lte.computeValue(context));
 it.next();
 it.next();
 assertTrue(lte.computeValue(context));
 it.next();
 assertFalse(lte.computeValue(context));
 }

 @Test
 public void testDoubleValueComparison() {
 CoreOperationRelationalExpression gte = new CoreOperationGTE(new CoreDoubleLiteral(1.0), new CoreDoubleLiteral(2.0));
 assertEquals(Boolean.FALSE, gte.computeValue(context));
 gte = new CoreOperationGTE(new CoreDoubleLiteral(1.0), new CoreDoubleLiteral(1.0));
 assertTrue(gte.computeValue(context));
 gte = new CoreOperationGTE(new CoreDoubleLiteral(1.0), new CoreDoubleLiteral(1.1));
 assertFalse(gte.computeValue(context));
 gte = new CoreOperationGTE(new CoreDoubleLiteral(1.1), new CoreDoubleLiteral(1.0));
 assertFalse(gte.computeValue(context));
 gte = new CoreOperationGTE(new CoreDoubleLiteral(1.1), new CoreDoubleLiteral(1.1));
 assertTrue(gte.computeValue(context));
 }

 @Test
 public void testIteratorComparison() {
 CoreOperationRelationalExpression gte = new CoreOperationGTE(IteratorsTestHelper.createIterator(new Object[]{1, 2, 3}), IteratorsTestHelper.createIterator(new Object[]{2, 3, 4}));
 assertFalse(gte.computeValue(context));
 gte = new CoreOperationGTE(IteratorsTestHelper.createIterator(new Object[]{1, 2, 3}), IteratorsTestHelper.createIterator(new Object[]{1, 2, 3}));
 assertTrue(gte.computeValue(context));
 gte = new CoreOperationGTE(IteratorsTestHelper.createIterator(new Object[]{1, 2, 3}), IteratorsTestHelper.createIterator(new Object[]{1, 2, 3, 4}));
 assertFalse(gte.computeValue(context));
 gte = new CoreOperationGTE(IteratorsTestHelper.createIterator(new Object[]{1, 2, 3, 4}), IteratorsTestHelper.createIterator(new Object[]{1, 2, 3}));
 assertTrue(gte.computeValue(context));
 }

}