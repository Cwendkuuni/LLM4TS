import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpressionFactory;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testEqualString() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createString("Hello");
 Expression right = factory.createString("Hello");
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new String[]{"Hello", "World"}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertTrue(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testEqualNumbers() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createNumber(1.0);
 Expression right = factory.createNumber(1.0);
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{1.0, 2.0}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertTrue(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testUnequalStrings() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createString("Hello");
 Expression right = factory.createString("World");
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new String[]{"Hello", "World"}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testUnequalNumbers() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createNumber(1.0);
 Expression right = factory.createNumber(2.0);
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{1.0, 2.0}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testUnequalStringAndNumber() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createString("Hello");
 Expression right = factory.createNumber(1.0);
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{"Hello", 1.0}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testNullAndString() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createNumber(null);
 Expression right = factory.createString("Hello");
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{null, "Hello"}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testNullAndNumber() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createNumber(null);
 Expression right = factory.createNumber(1.0);
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{null, 1.0}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSelfContextComparison() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createCurrentNode();
 Expression right = factory.createNumber(1.0);
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{"1"}, null);
 try {
 Axis axis = ctx.getAxis(Axis.SELF);
 axis.setCurrentNode(((Pointer) ctx.getCurrentNode()).getFirstChild());
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testEmptyCollectionComparison() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createString("[]");
 Expression right = factory.createString("Hello");
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{"[]"}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSameCollectionComparison() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createString("[1, 2]");
 Expression right = factory.createString("[1, 2]");
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{"[1, 2]", "[1, 2]"}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertTrue(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testDifferentCollectionComparison() {
 ExpressionFactory factory = new ExpressionFactory();
 Expression left = factory.createString("[1, 2]");
 Expression right = factory.createString("[1, 3]");
 CoreOperationCompare op = new CoreOperationCompare(left, right);
 JXPathContext ctx = new JXPathContext(new Object[]{"[1, 2]", "[1, 3]"}, null);
 try {
 boolean res = op.evaluateToBoolean(ctx);
 assertFalse(res);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }
}