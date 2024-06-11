package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationType;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(3), CoreOperationType.NUMBER),
 ">", CoreOperationType.GT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 ">=", CoreOperationType.GTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThan() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(3), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 "<", CoreOperationType.LT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 "<=", CoreOperationType.LTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanWithNaN() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(Double.NaN), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 ">", CoreOperationType.GT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testGreaterThanOrEqualWithNaN() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(Double.NaN), CoreOperationType.NUMBER),
 ">=", CoreOperationType.GTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testLessThanWithNaN() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(Double.NaN), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 "<", CoreOperationType.LT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testLessThanOrEqualWithNaN() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(Double.NaN), CoreOperationType.NUMBER),
 "<=", CoreOperationType.LTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testGreaterThanWithNull() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(null), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 ">", CoreOperationType.GT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testGreaterThanOrEqualWithNull() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(null), CoreOperationType.NUMBER),
 ">=", CoreOperationType.GTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testLessThanWithNull() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(null), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 "<", CoreOperationType.LT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testLessThanOrEqualWithNull() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(null), CoreOperationType.NUMBER),
 "<=", CoreOperationType.LTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertFalse(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testGreaterThanWithZero() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(0), CoreOperationType.NUMBER),
 ">", CoreOperationType.GT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqualWithZero() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(0), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(5), CoreOperationType.NUMBER),
 ">=", CoreOperationType.GTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithZero() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(3), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(0), CoreOperationType.NUMBER),
 "<", CoreOperationType.LT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualWithZero() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(0), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(3), CoreOperationType.NUMBER),
 "<=", CoreOperationType.LTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanWithNegativeNumber() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(-3), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(-2), CoreOperationType.NUMBER),
 ">", CoreOperationType.GT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanOrEqualWithNegativeNumber() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(-2), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(-3), CoreOperationType.NUMBER),
 ">=", CoreOperationType.GTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanWithNegativeNumber() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(-2), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(-3), CoreOperationType.NUMBER),
 "<", CoreOperationType.LT);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualWithNegativeNumber() {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation(ExpressionFactory.createNumber(-3), CoreOperationType.NUMBER),
 new CoreOperation(ExpressionFactory.createNumber(-2), CoreOperationType.NUMBER),
 "<=", CoreOperationType.LTEQ);
 Object result = relationalExpression.computeValue(new EvalContext());
 assertEquals(Boolean.FALSE, result);
 }
}