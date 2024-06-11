package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IteratingIterator;
import org.apache.commons.jxpath.ri.Axes.Iterators.InitialContext;
import org.apache.commons.jxpath.ri.Axes.Iterators.SelfContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperationRelationalExpression gtExpression = new CoreOperationGreaterThanExpression(new CoreLiteralExpression(5), new CoreLiteralExpression(6));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = gtExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression gteExpression = new CoreOperationGreaterThanOrEqualExpression(new CoreLiteralExpression(5), new CoreLiteralExpression(6));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = gteExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithInvalidData = new EvalContext(sampleInfoSet);
 Object resultWithInvalidData = gteExpression.computeValue(contextWithInvalidData);
 assertFalse(resultWithInvalidData instanceof Boolean);
 }

 @Test
 public void testLessThanExpression() {
 CoreOperationRelationalExpression ltExpression = new CoreOperationLessThanExpression(new CoreLiteralExpression(5), new CoreLiteralExpression(6));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = ltExpression.computeValue(context);
 assertFalse(result instanceof Boolean);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithValidData = new EvalContext(sampleInfoSet);
 Object valueToTest = new Integer(6);
 contextWithValidData.setNode(new Integer(5), new SelfContext());
 contextWithValidData.setNode(valueToTest, new SelfContext());
 Object resultWithValidData = ltExpression.computeValue(contextWithValidData);
 assertTrue(resultWithValidData instanceof Boolean);
 assertFalse((Boolean) resultWithValidData);
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperationRelationalExpression lteExpression = new CoreOperationLessThanOrEqualExpression(new CoreLiteralExpression(5), new CoreLiteralExpression(6));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lteExpression.computeValue(context);
 assertFalse(result instanceof Boolean);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithValidData = new EvalContext(sampleInfoSet);
 Object valueToTest = new Integer(5);
 contextWithValidData.setNode(new Integer(5), new SelfContext());
 contextWithValidData.setNode(valueToTest, new SelfContext());
 Object resultWithValidData = lteExpression.computeValue(contextWithValidData);
 assertTrue(resultWithValidData instanceof Boolean);
 assertTrue((Boolean) resultWithValidData);

 InfoSet sampleInfoSetWithInvalidData = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithInvalidData = new EvalContext(sampleInfoSetWithInvalidData);
 Object resultWithInvalidData = lteExpression.computeValue(contextWithInvalidData);
 assertFalse(resultWithInvalidData instanceof Boolean);
 }

 @Test
 public void testStringGreaterThanExpression() {
 CoreOperationRelationalExpression gtExpression = new CoreOperationGreaterThanExpression(new CoreLiteralExpression("A"), new CoreLiteralExpression("Z"));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = gtExpression.computeValue(context);
 assertFalse(result instanceof Boolean);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithValidData = new EvalContext(sampleInfoSet);
 context.setNode("Apple", new SelfContext());
 context.setNode("Banana", new SelfContext());
 Object resultWithValidData = gtExpression.computeValue(context);
 assertTrue(resultWithValidData instanceof Boolean);
 assertFalse((Boolean) resultWithValidData);
 }

 @Test
 public void testStringGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression gteExpression = new CoreOperationGreaterThanOrEqualExpression(new CoreLiteralExpression("A"), new CoreLiteralExpression("Z"));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = gteExpression.computeValue(context);
 assertFalse(result instanceof Boolean);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithValidData = new EvalContext(sampleInfoSet);
 context.setNode("Apple", new SelfContext());
 context.setNode("Apple", new SelfContext());
 Object resultWithValidData = gteExpression.computeValue(context);
 assertTrue(resultWithValidData instanceof Boolean);
 assertTrue((Boolean) resultWithValidData);

 InfoSet sampleInfoSetWithInvalidData = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithInvalidData = new EvalContext(sampleInfoSetWithInvalidData);
 Object resultWithInvalidData = gteExpression.computeValue(contextWithInvalidData);
 assertFalse(resultWithInvalidData instanceof Boolean);
 }

 @Test
 public void testStringLessThanExpression() {
 CoreOperationRelationalExpression ltExpression = new CoreOperationLessThanExpression(new CoreLiteralExpression("Z"), new CoreLiteralExpression("A"));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = ltExpression.computeValue(context);
 assertFalse(result instanceof Boolean);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithValidData = new EvalContext(sampleInfoSet);
 context.setNode("Apple", new SelfContext());
 context.setNode("Orange", new SelfContext());
 Object resultWithValidData = ltExpression.computeValue(context);
 assertTrue(resultWithValidData instanceof Boolean);
 assertTrue((Boolean) resultWithValidData);
 }

 @Test
 public void testStringLessThanOrEqualExpression() {
 CoreOperationRelationalExpression lteExpression = new CoreOperationLessThanOrEqualExpression(new CoreLiteralExpression("Z"), new CoreLiteralExpression("A"));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lteExpression.computeValue(context);
 assertFalse(result instanceof Boolean);

 InfoSet sampleInfoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithValidData = new EvalContext(sampleInfoSet);
 context.setNode("Apple", new SelfContext());
 context.setNode("Apple", new SelfContext());
 Object resultWithValidData = lteExpression.computeValue(context);
 assertTrue(resultWithValidData instanceof Boolean);
 assertTrue((Boolean) resultWithValidData);

 InfoSet sampleInfoSetWithInvalidData = InfoSetUtil.createSampleInfoSet();
 EvalContext contextWithInvalidData = new EvalContext(sampleInfoSetWithInvalidData);
 Object resultWithInvalidData = lteExpression.computeValue(contextWithInvalidData);
 assertFalse(resultWithInvalidData instanceof Boolean);
 }

 @Test
 public void testStringEqualExpressionWithSelfContext() {
 CoreOperationRelationalExpression equalsExpression = new CoreOperationEqualsExpression(new CoreLiteralExpression("Apple"), new CoreLiteralExpression("Apple"));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("Apple", new SelfContext());
 Object result = equalsExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testStringNotEqualExpressionWithSelfContext() {
 CoreOperationRelationalExpression notEqualsExpression = new CoreOperationNotEqualsExpression(new CoreOperationEqualsExpression(new CoreLiteralExpression("Apple"), new CoreLiteralExpression("Apple")));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("Apple", new SelfContext());
 Object result = notEqualsExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testCollectionGreaterThanExpression() {
 CoreOperationRelationalExpression gtExpression = new CoreOperationGreaterThanExpression(new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("myCollection"))), new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("anotherCollection"))));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("myCollection", new InitialContext(infoSet));
 context.setNode("anotherCollection", new InitialContext(infoSet));
 IteratingIterator myCollection = (IteratingIterator) context.getNodes().get("myCollection");
 IteratingIterator anotherCollection = (IteratingIterator) context.getNodes().get("anotherCollection");
 myCollection.add(new Integer(1));
 myCollection.add(new Integer(2));
 anotherCollection.add(new Integer(1));
 anotherCollection.add(new Integer(2));
 anotherCollection.add(new Integer(3));
 Object result = gtExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testCollectionGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression gteExpression = new CoreOperationGreaterThanOrEqualExpression(new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("myCollection"))), new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("anotherCollection"))));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("myCollection", new InitialContext(infoSet));
 context.setNode("anotherCollection", new InitialContext(infoSet));
 IteratingIterator myCollection = (IteratingIterator) context.getNodes().get("myCollection");
 IteratingIterator anotherCollection = (IteratingIterator) context.getNodes().get("anotherCollection");
 myCollection.add(new Integer(1));
 myCollection.add(new Integer(2));
 anotherCollection.add(new Integer(1));
 anotherCollection.add(new Integer(2));
 anotherCollection.add(new Integer(3));
 anotherCollection.add(new Integer(1));
 Object result = gteExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testCollectionLessThanExpression() {
 CoreOperationRelationalExpression ltExpression = new CoreOperationLessThanExpression(new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("myCollection"))), new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("anotherCollection"))));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("myCollection", new InitialContext(infoSet));
 context.setNode("anotherCollection", new InitialContext(infoSet));
 IteratingIterator myCollection = (IteratingIterator) context.getNodes().get("myCollection");
 IteratingIterator anotherCollection = (IteratingIterator) context.getNodes().get("anotherCollection");
 myCollection.add(new Integer(1));
 myCollection.add(new Integer(2));
 anotherCollection.add(new Integer(1));
 anotherCollection.add(new Integer(2));
 anotherCollection.add(new Integer(3));
 anotherCollection.add(new Integer(1));
 anotherCollection.add(new Integer(4));
 Object result = ltExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testCollectionLessThanOrEqualExpression() {
 CoreOperationRelationalExpression lteExpression = new CoreOperationLessThanOrEqualExpression(new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("myCollection"))), new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("anotherCollection"))));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("myCollection", new InitialContext(infoSet));
 context.setNode("anotherCollection", new InitialContext(infoSet));
 IteratingIterator myCollection = (IteratingIterator) context.getNodes().get("myCollection");
 IteratingIterator anotherCollection = (IteratingIterator) context.getNodes().get("anotherCollection");
 myCollection.add(new Integer(1));
 myCollection.add(new Integer(2));
 anotherCollection.add(new Integer(1));
 anotherCollection.add(new Integer(2));
 anotherCollection.add(new Integer(3));
 anotherCollection.add(new Integer(1));
 anotherCollection.add(new Integer(4));
 anotherCollection.add(new Integer(2));
 Object result = lteExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testCollectionContainsExpression() {
 CoreOperationRelationalExpression containsExpression = new CoreOperationContainsExpression(new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("myCollection"))), new CoreLiteralExpression(new Integer(2)));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("myCollection", new InitialContext(infoSet));
 IteratingIterator myCollection = (IteratingIterator) context.getNodes().get("myCollection");
 myCollection.add(new Integer(1));
 myCollection.add(new Integer(2));
 Object result = containsExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testCollectionDoesNotContainExpression() {
 CoreOperationRelationalExpression notContainsExpression = new CoreOperationNotContainsExpression(new CoreOperationContainsExpression(new CoreCollectionExpression(new CoreIterateExpression(new CoreVariableExpression("myCollection"))), new CoreLiteralExpression(new Integer(2))));
 InfoSet infoSet = InfoSetUtil.createSampleInfoSet();
 EvalContext context = new EvalContext(infoSet);
 context.setNode("myCollection", new InitialContext(infoSet));
 IteratingIterator myCollection = (IteratingIterator) context.getNodes().get("myCollection");
 myCollection.add(new Integer(1));
 myCollection.add(new Integer(2));
 Object result = notContainsExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

}