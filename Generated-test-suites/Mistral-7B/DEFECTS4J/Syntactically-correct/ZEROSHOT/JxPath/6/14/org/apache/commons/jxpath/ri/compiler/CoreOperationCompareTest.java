package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.axis.Axis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.StringExpression;
import org.apache.commons.jxpath.ri.compiler.NumberExpression;
import org.apache.commons.jxpath.ri.compiler.BooleanExpression;
import org.apache.commons.jxpath.ri.iter.IteratorSequence;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEquals() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new StringExpression("'1'"),
 new StringExpression("'1'")
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEquals() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new StringExpression("'1'"),
 new StringExpression("'2'")
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithNull() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new StringExpression("'1'"),
 new StringExpression(null)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithSameInstance() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new StringExpression("'1'"),
 new StringExpression("'1'")
 );
 CoreOperationCompare compareInstance = compare;
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compareInstance.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithSameValueDifferentExpressions() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new NumberExpression(1.0),
 new NumberExpression(1.0)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithDifferentValues() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new NumberExpression(1.0),
 new NumberExpression(2.0)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithBooleanTrueTrue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new BooleanExpression(true),
 new BooleanExpression(true)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithBooleanFalseFalse() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new BooleanExpression(false),
 new BooleanExpression(false)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithBooleanTrueFalse() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new BooleanExpression(true),
 new BooleanExpression(false)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithBooleanFalseTrue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new BooleanExpression(false),
 new BooleanExpression(true)
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithIterators() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new IteratorSequence(new Object[] { 1, 2, 3 }),
 new IteratorSequence(new Object[] { 1, 2, 3 })
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithIteratorsDifferentSize() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new IteratorSequence(new Object[] { 1, 2, 3 }),
 new IteratorSequence(new Object[] { 1, 2 })
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithIteratorsDifferentElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new IteratorSequence(new Object[] { 1, 2, 3 }),
 new IteratorSequence(new Object[] { 1, 2, 4 })
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithIteratorsMatchingElement() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new IteratorSequence(new Object[] { 1, 2, 3 }),
 new IteratorSequence(new Object[] { 3, 2, 1 })
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualsWithIteratorsNoMatchingElement() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new IteratorSequence(new Object[] { 1, 2, 3 }),
 new IteratorSequence(new Object[] { 4, 2, 1 })
 );
 EvaluationContext context = new JXPathContext() {
 @Override
 public Iterator<?> iterateSelf() {
 throw new UnsupportedOperationException();
 }

 @Override
 public Iterator<?> iterateAxis(Axis axis) {
 throw new UnsupportedOperationException();
 }
};
 assertFalse(compare.evaluate(context).booleanValue());
 }

}