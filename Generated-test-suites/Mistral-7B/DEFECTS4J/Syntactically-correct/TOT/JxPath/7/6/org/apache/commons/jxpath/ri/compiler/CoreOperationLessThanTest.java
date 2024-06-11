import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan_BothNumbers_LessThan() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new ConstantExpression(new Double(5.0)),
 new ConstantExpression(new Double(3.0))
 );
 EvalContext context = new EvalContext(new InfoSet());
 Path path = ExpressionUtil.parseExpression("/test", new String[]{"test", "5.0", "3.0"});
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThan_BothNumbers_GreaterThanOrEqual() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new ConstantExpression(new Double(5.0)),
 new ConstantExpression(new Double(5.0))
 );
 EvalContext context = new EvalContext(new InfoSet());
 Path path = ExpressionUtil.parseExpression("/test", new String[]{"test", "5.0", "5.0"});
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testLessThan_OneNumber_Invalid() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new ConstantExpression(new Double(5.0))
 );
 EvalContext context = new EvalContext(new InfoSet());
 Path path = ExpressionUtil.parseExpression("/test", new String[]{"test", "5.0", null});
 try {
 Object result = expression.computeValue(context);
 fail("Expected a JXPathException");
 } catch (Exception e) {
 assertTrue(e instanceof JXPathException);
 }
 }

 @Test
 public void testLessThan_OneNumber_Number() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new ConstantExpression(new Double(5.0))
 );
 EvalContext context = new EvalContext(new InfoSet());
 Path path = ExpressionUtil.parseExpression("/test", new String[]{"test", "5.0", new Double(5.0)});
 Object result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testLessThan_String_InvalidComparison() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new ConstantExpression(new String("5.0")),
 new ConstantExpression(new String("apple"))
 );
 EvalContext context = new EvalContext(new InfoSet());
 Path path = ExpressionUtil.parseExpression("/test", new String[]{"test", "5.0", "apple"});
 try {
 Object result = expression.computeValue(context);
 fail("Expected a JXPathException");
 } catch (Exception e) {
 assertTrue(e instanceof JXPathException);
 }
 }
}