package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

 // Test case 1: Testing with two equal objects
 @Test
 public void testEqualObjects() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 // Test case 2: Testing with two different objects of the same class
 @Test
 public void testDifferentObjectsSameClass() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 // Test case 3: Testing with two different objects of different classes
 @Test(expected = ClassCastException.class)
 public void testDifferentObjectsDifferentClasses() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression("Hello"), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 fail("Should have thrown ClassCastException");
 }

 // Test case 4: Testing with null values
 @Test
 public void testNullValues() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(null));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 // Test case 5: Testing with a null value and an object
 @Test
 public void testNullValueObject() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 // Test case 6: Testing with an object and a null value
 @Test
 public void testObjectNullValue() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(null));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 // Test case 7: Testing with two integers with the same value but different types (byte and short)
 @Test
 public void testSameValueDifferentTypes() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression((byte) 1), new ConstantExpression((short) 1));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 // Test case 8: Testing with two strings
 @Test
 public void testStrings() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression("Hello"), new ConstantExpression("Hello"));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 // Test case 9: Testing with two different strings
 @Test
 public void testDifferentStrings() {
 CoreOperationEqual operation = new CoreOperationEqual(new ConstantExpression("Hello"), new ConstantExpression("World"));
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }
}