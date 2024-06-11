import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression opGT = new CoreOperationRelationalExpression(new Expression[] {new ConstantExpression(1), new ConstantExpression(2) }) {
 @Override
 public String getOperatorSymbol() {
 return ">";
 }
 };
 JXPathContext ctx = new JXPathContext(new Object[] {1.0f, 2.0f});
 assertTrue(opGT.evaluate(ctx));
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression opGE = new CoreOperationRelationalExpression(new Expression[] {new ConstantExpression(1), new ConstantExpression(2) }) {
 @Override
 public String getOperatorSymbol() {
 return ">=";
 }
 };
 JXPathContext ctx = new JXPathContext(new Object[] {1.0f, 2.0f});
 assertTrue(opGE.evaluate(ctx));

 JXPathContext ctx2 = new JXPathContext(new Object[] {1.0f, 1.0f});
 assertTrue(opGE.evaluate(ctx2));
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression opLT = new CoreOperationRelationalExpression(new Expression[] {new ConstantExpression(2), new ConstantExpression(1) }) {
 @Override
 public String getOperatorSymbol() {
 return "<";
 }
 };
 JXPathContext ctx = new JXPathContext(new Object[] {1.0f, 2.0f});
 assertFalse(opLT.evaluate(ctx));
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression opLE = new CoreOperationRelationalExpression(new Expression[] {new ConstantExpression(2), new ConstantExpression(1) }) {
 @Override
 public String getOperatorSymbol() {
 return "<=";
 }
 };
 JXPathContext ctx = new JXPathContext(new Object[] {1.0f, 2.0f});
 assertFalse(opLE.evaluate(ctx));

 JXPathContext ctx2 = new JXPathContext(new Object[] {2.0f, 2.0f});
 assertTrue(opLE.evaluate(ctx2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgs() {
 CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new Expression[0]);
 }
}