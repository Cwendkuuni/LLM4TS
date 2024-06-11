import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperation left = new CoreNumberExpression(1);
 CoreOperation right = new CoreNumberExpression(2);
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);
 assertTrue(expression.evaluate(new Context()));
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperation left = new CoreNumberExpression(1);
 CoreOperation right = new CoreNumberExpression(1);
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(left, right);
 assertTrue(expression.evaluate(new Context()));

 CoreOperation left2 = new CoreNumberExpression(1);
 CoreOperation right2 = new CoreNumberExpression(2);
 CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThanOrEqual(left2, right2);
 assertFalse(expression2.evaluate(new Context()));
 }

 @Test
 public void testLessThan() {
 CoreOperation left = new CoreNumberExpression(2);
 CoreOperation right = new CoreNumberExpression(1);
 CoreOperationRelationalExpression expression = new CoreOperationLessThan(left, right);
 assertFalse(expression.evaluate(new Context()));
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperation left = new CoreNumberExpression(2);
 CoreOperation right = new CoreNumberExpression(2);
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(left, right);
 assertTrue(expression.evaluate(new Context()));

 CoreOperation left3 = new CoreNumberExpression(2);
 CoreOperation right3 = new CoreNumberExpression(1);
 CoreOperationRelationalExpression expression3 = new CoreOperationLessThanOrEqual(left3, right3);
 assertFalse(expression3.evaluate(new Context()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgument1() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null, new CoreNumberExpression(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgument2() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(1), null);
 }
}