import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationAdd;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(1), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 assertTrue(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqual() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(1), new ConstantExpression(2));
 EvalContext context = new EvalContext();
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNullLeft() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(null), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNullRight() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(1), new ConstantExpression(null));
 EvalContext context = new EvalContext();
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualCollections() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantCollectionExpression(new Integer[] { 1, 2, 3 }),
 new ConstantCollectionExpression(new Integer[] { 1, 2, 3 }));
 EvalContext context = new EvalContext();
 assertTrue(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualCollections() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantCollectionExpression(new Integer[] { 1, 2, 3 }),
 new ConstantCollectionExpression(new Integer[] { 1, 2, 4 }));
 EvalContext context = new EvalContext();
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualIterators() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new IteratorExpression(new ConstantCollectionExpression(new Integer[] { 1, 2, 3 }).iterator()),
 new IteratorExpression(new ConstantCollectionExpression(new Integer[] { 1, 2, 3 }).iterator()));
 EvalContext context = new EvalContext();
 assertTrue(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualIterators() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new IteratorExpression(new ConstantCollectionExpression(new Integer[] { 1, 2, 3 }).iterator()),
 new IteratorExpression(new ConstantCollectionExpression(new Integer[] { 1, 2, 4 }).iterator()));
 EvalContext context = new EvalContext();
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualInitialContexts() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(new InitialContext(new Object())),
 new ConstantExpression(new InitialContext(new Object())));
 EvalContext context = new EvalContext();
 assertTrue(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualInitialContexts() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(new InitialContext(new Object())),
 new ConstantExpression(new InitialContext(new Object())));
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Object());
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualSelfContexts() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(new SelfContext()),
 new ConstantExpression(new SelfContext()));
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Object());
 assertTrue(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualSelfContexts() {
 CoreOperationCompare operation = new CoreOperationCompare(
 new ConstantExpression(new SelfContext()),
 new ConstantExpression(new SelfContext()));
 EvalContext context = new EvalContext();
 context.setCurrentNode(new Object());
 context.pushCurrentNode(new Object());
 assertFalse(operation.evaluate(context).booleanValue());
 }

 @Test
 public void testAdditionWithComparison() {
 Expression left = new CoreOperationAdd(
 new ConstantExpression(1),
 new ConstantExpression(2));
 Expression right = new ConstantExpression(3);
 CoreOperationCompare operation = new CoreOperationCompare(left, right);
 EvalContext context = new EvalContext();
 assertFalse(operation.evaluate(context).booleanValue());
 }
}