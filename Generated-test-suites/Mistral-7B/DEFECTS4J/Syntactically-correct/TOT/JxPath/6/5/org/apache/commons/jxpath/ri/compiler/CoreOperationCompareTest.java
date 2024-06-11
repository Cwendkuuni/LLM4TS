package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(5), new LiteralExpression(5));
 EvalContext context = new EvalContext(new InitialContext());
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_false() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(5), new LiteralExpression(6));
 EvalContext context = new EvalContext(new InitialContext());
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(null), new LiteralExpression(5));
 EvalContext context = new EvalContext(new InitialContext());
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_null_null() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(null), new LiteralExpression(null));
 EvalContext context = new EvalContext(new InitialContext());
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_iterators() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers"), new LiteralExpression(5));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers", new LiteralExpression("[1,2,3,4,5]"));
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_iterators_not_equal() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers1"), new VariableExpression("numbers2"));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers1", new LiteralExpression("[1,2,3,4,5]"));
 context.setVariables().declareVariable("numbers2", new LiteralExpression("[1,2,3,4,6]"));
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_iterators_empty() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers1"), new VariableExpression("numbers2"));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers1", new LiteralExpression("()"));
 context.setVariables().declareVariable("numbers2", new LiteralExpression("[1,2,3,4,5]"));
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testEqual_iterators_empty_empty() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers1"), new VariableExpression("numbers2"));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers1", new LiteralExpression("()"));
 context.setVariables().declareVariable("numbers2", new LiteralExpression("()"));
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testContains() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers"), new LiteralExpression(5));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers", new LiteralExpression("[1,2,3,4,5]"));
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testContains_false() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers"), new LiteralExpression(6));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers", new LiteralExpression("[1,2,3,4,5]"));
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testContains_empty_collection() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers"), new LiteralExpression(5));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers", new LiteralExpression("()"));
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testFindMatch() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers1"), new VariableExpression("numbers2"));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers1", new LiteralExpression("[1,2,3,4,5]"));
 context.setVariables().declareVariable("numbers2", new LiteralExpression("[5,2,3,4,5]"));
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testFindMatch_no_match() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableExpression("numbers1"), new VariableExpression("numbers2"));
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariables(new Variables());
 context.setVariables().declareVariable("numbers1", new LiteralExpression("[1,2,3,4,5]"));
 context.setVariables().declareVariable("numbers2", new LiteralExpression("[1,2,3,4,6]"));
 assertFalse(op.evaluate(context).booleanValue());
 }
}