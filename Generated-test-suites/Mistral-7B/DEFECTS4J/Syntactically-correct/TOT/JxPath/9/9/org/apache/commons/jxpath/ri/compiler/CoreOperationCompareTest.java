package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

public class CoreOperationCompareTest {

 @Test
 public void testEquals() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(5), new LiteralExpression(5)
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 assertEquals(true, compare.evaluate(context));
 }

 @Test
 public void testNotEquals() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(5), new LiteralExpression(6)
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 assertEquals(false, compare.evaluate(context));
 }

 @Test
 public void testEqualsNull() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(5), new LiteralExpression(null)
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 assertEquals(false, compare.evaluate(context));
 }

 @Test
 public void testEqualsDifferentNodes() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(new Object()), new LiteralExpression(new Object())
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node1 = context.setNode(new Object());
 Node node2 = context.setNode(new Object());
 context.setContextItem(node1);
 assertEquals(false, compare.evaluate(context));
 }

 @Test
 public void testFindMatch() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new Object[] { 1, 2, 3 }),
 new CollectionExpression(new Object[] { 1, 3 })
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 assertTrue(compare.evaluate(context));
 }

 @Test
 public void testFindNoMatch() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new Object[] { 1, 2, 3 }),
 new CollectionExpression(new Object[] { 4, 5 })
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 assertFalse(compare.evaluate(context));
 }

 @Test
 public void testFindMatchWithNull() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new CollectionExpression(new Object[] { 1, 2, 3 }),
 new LiteralExpression(null)
 );
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 assertFalse(compare.evaluate(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArguments() {
 CoreOperationCompare compare = new CoreOperationCompare(null, null);
 EvaluationContext context = JXPathContextFactory.newContext();
 Node node = context.setNode(new Object());
 context.setContextItem(node);
 // This test should throw an IllegalArgumentException
 compare.evaluate(context);
 }
}