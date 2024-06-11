package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.iter.AbstractIterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

 private static final Object[][] TEST_CASES = new Object[][] {
 // Test case 1: Comparing two numbers
 new Object[] {new CoreAdd(new CoreNumber(3.5), new CoreNumber(2.5)), Boolean.TRUE},
 new Object[] {new CoreAdd(new CoreNumber(3.5), new CoreNumber(2.6)), Boolean.FALSE},
 new Object[] {new CoreAdd(new CoreNumber(3.5), new CoreNumber(3.5)), Boolean.TRUE},
 new Object[] {new CoreAdd(new CoreNumber(3.5), new CoreNumber(3.4)), Boolean.TRUE},

 // Test case 2: Comparing an Iterator with a number
 new Object[] {new CoreOperationGreater(new CoreVariable("myIter"), new CoreNumber(3)), Boolean.FALSE},
 new Object[] {new CoreOperationGreater(new CoreVariable("myIter"), new CoreNumber(5)), Boolean.FALSE},
 new Object[] {new CoreOperationGreater(new CoreVariable("myIter"), new CoreNumber(3)), Boolean.TRUE},

 // Test case 3: Comparing two Iterators
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), new CoreVariable("it2")), Boolean.FALSE},
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), new CoreVariable("it2")), Boolean.TRUE},
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), new CoreVariable("it3")), Boolean.FALSE},

 // Test case 4: Comparing an InitialContext with a number
 new Object[] {new CoreOperationGreater(new CoreInitialContext(), new CoreNumber(3)), Boolean.FALSE},

 // Test case 5: Comparing an InitialContext with an Iterator
 new Object[] {new CoreOperationGreater(new CoreInitialContext(), new CoreVariable("it1")), Boolean.FALSE},

 // Test case 6: Comparing two InitialContexts
 new Object[] {new CoreOperationGreater(new CoreInitialContext(), new CoreInitialContext()), Boolean.FALSE},

 // Test case 7: Comparing an Iterator with an Iterator
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), new CoreVariable("it2")), Boolean.FALSE},

 // Test case 8: Comparing a null object with a number
 new Object[] {new CoreOperationGreater(null, new CoreNumber(3)), Boolean.FALSE},

 // Test case 9: Comparing a null object with a null object
 new Object[] {new CoreOperationGreater(null, null), Boolean.FALSE},

 // Test case 10: Comparing a null object with an Iterator
 new Object[] {new CoreOperationGreater(null, new CoreVariable("it1")), Boolean.FALSE},

 // Test case 11: Comparing a null object with an InitialContext
 new Object[] {new CoreOperationGreater(null, new CoreInitialContext()), Boolean.FALSE},

 // Test case 12: Comparing an empty Iterator with a number
 new Object[] {new CoreOperationGreater(new CoreEmptyIterator(), new CoreNumber(3)), Boolean.FALSE},

 // Test case 13: Comparing an empty Iterator with an empty Iterator
 new Object[] {new CoreOperationGreater(new CoreEmptyIterator(), new CoreEmptyIterator()), Boolean.FALSE},

 // Test case 14: Comparing an empty Iterator with an InitialContext
 new Object[] {new CoreOperationGreater(new CoreEmptyIterator(), new CoreInitialContext()), Boolean.FALSE},

 // Test case 15: Comparing an InitialContext with an empty Iterator
 new Object[] {new CoreOperationGreater(new CoreInitialContext(), new CoreEmptyIterator()), Boolean.FALSE},

 // Test case 16: Comparing an Iterator with an empty Iterator
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), new CoreEmptyIterator()), Boolean.FALSE},

 // Test case 17: Comparing an Iterator with a null Iterator
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), null), Boolean.FALSE},

 // Test case 18: Comparing an Iterator with itself
 new Object[] {new CoreOperationGreater(new CoreVariable("it1"), new CoreVariable("it1")), Boolean.FALSE}
 };

 @Test
 public void testCoreOperationRelationalExpression() {
 for (Object[] testCase : TEST_CASES) {
 EvalContext context = new EvalContext();
 CoreOperationRelationalExpression expression = (CoreOperationRelationalExpression) testCase[0];
 Object result = expression.compute(context);
 assertTrue(evaluateCompare(expression, testCase[1], result));
 }
 }

 private boolean evaluateCompare(CoreOperationRelationalExpression expression, Object[] expected, Object actual) {
 boolean result = expression.evaluateCompare(expression.compute(contextForTest()).getPrecedence());
 assertEquals(expected[1], result);
 return result;
 }

 private EvalContext contextForTest() {
 EvalContext context = new EvalContext();
 context.setInfoSet(new InfoSet() {
 @Override
 public Iterate iterate() {
 return new AbstractIterator() {};
 }
 });
 return context;
 }
}