import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.UnexpectedTypeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationLessThan operation;
 private InfoSet infoSet;
 private EvalContext context;

 @Test
 public void testLessThanDoublePositiveAndPositive() {
 arg1 = new LiteralExpression("2.5");
 arg2 = new LiteralExpression("3.0");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanDoubleNegativeAndNegative() {
 arg1 = new LiteralExpression("-2.5");
 arg2 = new LiteralExpression("-3.0");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanDoublePositiveAndZero() {
 arg1 = new LiteralExpression("2.5");
 arg2 = new LiteralExpression("0.0");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanDoubleZeroAndPositive() {
 arg1 = new LiteralExpression("0.0");
 arg2 = new LiteralExpression("2.5");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanDoublePositiveAndNaN() {
 arg1 = new LiteralExpression("2.5");
 arg2 = new LiteralExpression("NaN");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test(expected = UnexpectedTypeException.class)
 public void testLessThanStringAndString() {
 arg1 = new LiteralExpression("'apple'");
 arg2 = new LiteralExpression("'banana'");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 operation.computeValue(context);
 }

 @Test
 public void testLessThanStringAndNumber() {
 arg1 = new LiteralExpression("'apple'");
 arg2 = new LiteralExpression("2.5");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanNumberAndString() {
 arg1 = new LiteralExpression("2.5");
 arg2 = new LiteralExpression("'apple'");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanNullAndNull() {
 arg1 = new LiteralExpression(null);
 arg2 = new LiteralExpression(null);
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanNullAndNumber() {
 arg1 = new LiteralExpression(null);
 arg2 = new LiteralExpression("2.5");
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanNumberAndNull() {
 arg1 = new LiteralExpression("2.5");
 arg2 = new LiteralExpression(null);
 operation = new CoreOperationLessThan(arg1, arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 assertTrue(operation.computeValue(context).booleanValue());
 }
}