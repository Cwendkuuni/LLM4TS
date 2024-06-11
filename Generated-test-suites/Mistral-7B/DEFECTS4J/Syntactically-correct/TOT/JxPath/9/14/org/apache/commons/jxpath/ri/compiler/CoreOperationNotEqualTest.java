import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.context.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualBooleanWithBooleanTrue() {
 Expression arg1 = new CoreLiteralExpression(true);
 Expression arg2 = new CoreLiteralExpression(true);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected Boolean.FALSE, but got " + result);
 } catch (EvaluationException e) {
 // Expected
 }
 assertTrue(Boolean.FALSE.equals(notEqual.getResult()));
 }

 @Test
 public void testNotEqualBooleanWithBooleanFalse() {
 Expression arg1 = new CoreLiteralExpression(false);
 Expression arg2 = new CoreLiteralExpression(false);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected Boolean.TRUE, but got " + result);
 } catch (EvaluationException e) {
 // Expected
 }
 assertTrue(Boolean.TRUE.equals(notEqual.getResult()));
 }

 @Test
 public void testNotEqualBooleanWithInteger() {
 Expression arg1 = new CoreLiteralExpression(true);
 Expression arg2 = new CoreLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected Boolean.FALSE, but got " + result);
 } catch (ClassCastException e) {
 // Expected, since Integer can not be compared to Boolean directly
 }
 assertFalse(notEqual.isSuccess());
 }

 @Test
 public void testNotEqualBooleanWithString() {
 Expression arg1 = new CoreLiteralExpression(true);
 Expression arg2 = new CoreLiteralExpression("true");
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected Boolean.FALSE, but got " + result);
 } catch (ClassCastException e) {
 // Expected, since String can not be compared to Boolean directly
 }
 assertFalse(notEqual.isSuccess());
 }

 @Test(expected = EvaluationException.class)
 public void testNotEqualInvalidArguments() {
 Expression arg1 = new CoreOperationAdd();
 Expression arg2 = new CoreLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 fail("Not expected to reach this point, since computeValue should throw an exception");
 }

 @Test
 public void testNotEqualNullWithBoolean() {
 Expression arg1 = new CoreLiteralExpression(null);
 Expression arg2 = new CoreLiteralExpression(true);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected EvaluationException, but got " + result);
 } catch (EvaluationException e) {
 // Expected
 }
 assertFalse(notEqual.isSuccess());
 }

 @Test
 public void testNotEqualNullWithNull() {
 Expression arg1 = new CoreLiteralExpression(null);
 Expression arg2 = new CoreLiteralExpression(null);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 } catch (EvaluationException e) {
 fail("Not expected to throw an exception");
 }
 }

 @Test
 public void testNotEqualNullWithInteger() {
 Expression arg1 = new CoreLiteralExpression(null);
 Expression arg2 = new CoreLiteralExpression(1);
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected EvaluationException, but got " + result);
 } catch (ClassCastException e) {
 // Expected, since Integer can not be compared to null directly
 }
 assertFalse(notEqual.isSuccess());
 }

 @Test
 public void testNotEqualNullWithString() {
 Expression arg1 = new CoreLiteralExpression(null);
 Expression arg2 = new CoreLiteralExpression("true");
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 Object result = notEqual.computeValue(context);
 fail("Expected EvaluationException, but got " + result);
 } catch (ClassCastException e) {
 // Expected, since String can not be compared to null directly
 }
 assertFalse(notEqual.isSuccess());
 }
}