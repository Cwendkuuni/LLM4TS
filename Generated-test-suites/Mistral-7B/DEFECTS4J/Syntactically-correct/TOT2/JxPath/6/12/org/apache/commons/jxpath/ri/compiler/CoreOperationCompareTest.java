// CoreOperationCompareTest.java

package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Iterator;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 @Test
 public void testEqualBasicUseCase() {
 // Expert 1: test case for method equal() with basic use case
 CoreOperationCompare op = new CoreOperationCompare(new Const(1), new Const(1));
 EvaluationContext context = new EvaluationContext();
 assertTrue(op.evaluate(context).booleanValue());
 }

 // ... Add other test cases from Experts 1, 2, and 3 here ...

 @Test
 public void testFindMatchEmptyIterators() {
 // Expert 3: test case for method findMatch() with empty Iterators
 Iterator lit = new EmptyIterator();
 Iterator rit = new EmptyIterator();
 CoreOperationCompare op = new CoreOperationCompare(lit, rit);
 EvaluationContext context = new EvaluationContext();
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testContainsEmptyIterator() {
 // Expert 2: test case for method contains() with empty Iterator
 CoreOperationCompare op = new CoreOperationCompare(new EmptyIterator(), new Const(1));
 EvaluationContext context = new EvaluationContext();
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testContainsNullValue() {
 // Expert 3: test case for method contains() with null value
 Iterator it = new ArrayIterator(new Object[] { 1, 2, null });
 CoreOperationCompare op = new CoreOperationCompare(it, new Const(null));
 EvaluationContext context = new EvaluationContext();
 // This test case should throw a NullPointerException
 //op.evaluate(context);
 // Replace with an assertion to check for the expected exception
 assertThrows(NullPointerException.class, () -> op.evaluate(context));
 }

 @Test
 public void testFindMatchNullIterator() {
 // Expert 3: test case for method findMatch() with null Iterator
 Iterator lit = null;
 Iterator rit = new ArrayIterator(new int[] { 1, 2, 3 });
 CoreOperationCompare op = new CoreOperationCompare(lit, rit);
 EvaluationContext context = new EvaluationContext();
 // This test case should throw a NullPointerException
 //op.evaluate(context);
 // Replace with an assertion to check for the expected exception
 assertThrows(NullPointerException.class, () -> op.evaluate(context));
 }

 @Test
 public void testEqualNonComparableTypes() {
 // Expert 3: test case for method equal() with non-comparable types
 CoreOperationCompare op = new CoreOperationCompare(new StringValue("hello"), new NumberValue(1));
 EvaluationContext context = new EvaluationContext();
 // This test case should throw a ClassCastException
 //op.evaluate(context);
 // Replace with an assertion to check for the expected exception
 assertThrows(ClassCastException.class, () -> op.evaluate(context));
 }

}