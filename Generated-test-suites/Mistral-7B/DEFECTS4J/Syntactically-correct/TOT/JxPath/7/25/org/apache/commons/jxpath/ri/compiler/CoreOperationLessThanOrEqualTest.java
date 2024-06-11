import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.apache.commons.jxpath.ri.nodes.Variable;
import org.apache.commons.jxpath.ri.util.JxPathContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 private CoreOperationLessThanOrEqual operation;
 private EvalContext context;
 private InfoSet infoSet;

 @Before
 public void setUp() {
 context = new EvalContext();
 infoSet = new InfoSet();
 infoSet.setDocument(new SimpleNode(context, "root", null, new Variable("x"), new Variable("y")));
 }

 @Test
 public void testLessThanOrEqualTrue() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression(3.0));
 InfoSetUtil.setDoubleValue(context, "x", 2.5);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualFalse() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression(2.5));
 InfoSetUtil.setDoubleValue(context, "x", 3.0);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualEqual() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression(2.5));
 InfoSetUtil.setDoubleValue(context, "x", 2.5);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNaN() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression(Double.NaN));
 InfoSetUtil.setDoubleValue(context, "x", Double.NaN);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNull() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression(null));
 InfoSetUtil.setDoubleValue(context, "x", 2.5);
 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualMixedTypes() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression("2.5"));
 InfoSetUtil.setDoubleValue(context, "x", 2.5);
 assertTrue(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNullvsNull() {
 operation = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new ConstantExpression(null));
 InfoSetUtil.setDoubleValue(context, "x", null);
 assertNull(operation.computeValue(context));
 }
}