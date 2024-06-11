package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.IterateConstants;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private EvalContext context;

 @Before
 public void setUp() {
 context = new EvalContext() {
 @Override
 public Iterate iterate(Axis axis, Nodes nodes) {
 return new Iterate() {
 @Override
 public Object currentNode() {
 return new Object() {};
 }

 @Override
 public int getPosition() {
 return IterateConstants.POSITION_UNDEFINED;
 }

 @Override
 public void setPosition(int position) {
 // Do nothing
 }

 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public void next() {
 // Do nothing
 }
 };
 }
 };
 }

 @Test
 public void testGreaterThanExpression() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant("5"),
 new CoreOperationConstant("3")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 Object left = new Integer(5);
 Object right = new Integer(3);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant("5"),
 new CoreOperationConstant("3")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 Object left = new Integer(5);
 Object right = new Integer(3);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 left = new Integer(3);
 right = new Integer(3);

 result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanExpression() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant("3"),
 new CoreOperationConstant("5")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };

 Object left = new Integer(3);
 Object right = new Integer(5);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqualExpression() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant("3"),
 new CoreOperationConstant("5")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };

 Object left = new Integer(3);
 Object right = new Integer(5);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 left = new Integer(3);
 right = new Integer(3);

 result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComparisonWithDoubleValues() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant("5.5"),
 new CoreOperationConstant("4.5")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 Object left = new Double(5.5);
 Object right = new Double(4.5);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComparisonWithDifferentTypes() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant("5"),
 new CoreOperationConstant("3.0")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare != 0;
 }
 };

 Object left = new Integer(5);
 Object right = new Double(3.0);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComparisonWithNull() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant(null),
 new CoreOperationConstant("5")
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare != 0;
 }
 };

 Object left = null;
 Object right = new Integer(5);

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComparisonWithEmptyCollection() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant(InfoSet.EMPTY_NODE_ITERATOR),
 new CoreOperationConstant(InfoSet.EMPTY_NODE_ITERATOR)
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };

 Object left = InfoSet.EMPTY_NODE_ITERATOR;
 Object right = InfoSet.EMPTY_NODE_ITERATOR;

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComparisonWithSingleNodeIterators() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant(context.getSingleNodePointer()),
 new CoreOperationConstant(context.getSingleNodePointer())
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare == 0;
 }
 };

 Object left = context.getSingleNodePointer();
 Object right = context.getSingleNodePointer();

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComparisonWithDifferentNodeIterators() {
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {
 new CoreOperationConstant(context.getSingleNodePointer()),
 new CoreOperationConstant(new Iterator() {})
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare != 0;
 }
 };

 Object left = context.getSingleNodePointer();
 Object right = new Iterator() {};

 Object result = expression.computeValue(context);

 assertNotNull(result);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }
}