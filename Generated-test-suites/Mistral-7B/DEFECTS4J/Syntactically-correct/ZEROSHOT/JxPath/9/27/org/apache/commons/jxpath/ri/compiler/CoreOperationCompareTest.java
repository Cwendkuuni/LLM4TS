import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(1),
 new LiteralExpression(1)
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(1),
 new LiteralExpression(2)
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualCollections() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ListExpression(new Object[] { 1, 2, 3 }),
 new ListExpression(new Object[] { 1, 2, 3 })
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualCollections() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ListExpression(new Object[] { 1, 2, 3 }),
 new ListExpression(new Object[] { 1, 2, 4 })
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualIterators() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ListExpression(new Object[] { 1, 2, 3 }).iterator(),
 new ListExpression(new Object[] { 1, 2, 3 }).iterator()
 );
 EvalContext context = new EvalContext();
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualIterators() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ListExpression(new Object[] { 1, 2, 3 }).iterator(),
 new ListExpression(new Object[] { 1, 2, 4 }).iterator()
 );
 EvalContext context = new EvalContext();
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testEqualPointers() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new VariableExpression("$x"),
 new VariableExpression("$x")
 );
 EvalContext context = new EvalContext();
 context.setVariable("x", new Pointer(1));
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEqualPointers() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new VariableExpression("$x"),
 new VariableExpression("$y")
 );
 EvalContext context = new EvalContext();
 context.setVariable("x", new Pointer(1));
 context.setVariable("y", new Pointer(2));
 assertFalse(compare.evaluate(context).booleanValue());
 }
}