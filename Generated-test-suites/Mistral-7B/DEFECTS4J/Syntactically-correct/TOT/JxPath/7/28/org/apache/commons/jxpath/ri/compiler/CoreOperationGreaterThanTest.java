import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.util.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThanPositive() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(5.0)),
 new ConstantExpression(new Double(3.0)));
 assertTrue(expression.computeValue(new EvalContext(TestUtils.getTestInfoSet())));
 }

 @Test
 public void testGreaterThanZero() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(0.0)),
 new ConstantExpression(new Double(5.0)));
 assertFalse(expression.computeValue(new EvalContext(TestUtils.getTestInfoSet())));
 }

 @Test
 public void testGreaterThanEqualZero() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(0.0)),
 new ConstantExpression(new Double(0.0)));
 assertTrue(expression.computeValue(new EvalContext(TestUtils.getTestInfoSet())));
 }

 @Test
 public void testGreaterThanNegative() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(-5.0)),
 new ConstantExpression(new Double(-3.0)));
 assertTrue(expression.computeValue(new EvalContext(TestUtils.getTestInfoSet())));
 }

 @Test
 public void testGreaterThanZeroAndNaN() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(5.0)),
 new ConstantExpression(new Double(Double.NaN)));
 assertFalse(expression.computeValue(new EvalContext(TestUtils.getTestInfoSet())));
 }

 @Test
 public void testGreaterThanNaNAndZero() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(Double.NaN)),
 new ConstantExpression(new Double(0.0)));
 assertFalse(expression.computeValue(new EvalContext(TestUtils.getTestInfoSet())));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanNullArg1() {
 new CoreOperationGreaterThan(null, new ConstantExpression(new Double(3.0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanNullArg2() {
 new CoreOperationGreaterThan(new ConstantExpression(new Double(5.0)), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanMixedTypes() {
 new CoreOperationGreaterThan(
 new ConstantExpression(new Double(5.0)),
 new ConstantExpression(new Integer(3))
 );
 }
}