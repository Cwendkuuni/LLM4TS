import org.apache.commons.jxpath.EvaluationException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

 @Test
 public void testCoreOperationGreaterThanPositive() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Const(10.5), new Const(5.3));
 JXPathContext context = new JXPathContext();
 ContextFactory factory = new EmptyContextFactory();
 context.setContextItem(factory.makeContext(new InfoSet()));
 EvalContext evalContext = new EvalContext(context, null);

 Object result = expression.computeValue(evalContext);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationGreaterThanZero() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Const(0), new Const(5.3));
 JXPathContext context = new JXPathContext();
 ContextFactory factory = new EmptyContextFactory();
 context.setContextItem(factory.makeContext(new InfoSet()));
 EvalContext evalContext = new EvalContext(context, null);

 Object result = expression.computeValue(evalContext);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCoreOperationGreaterThanEqual() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Const(5.3), new Const(5.3));
 JXPathContext context = new JXPathContext();
 ContextFactory factory = new EmptyContextFactory();
 context.setContextItem(factory.makeContext(new InfoSet()));
 EvalContext evalContext = new EvalContext(context, null);

 Object result = expression.computeValue(evalContext);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationGreaterThanNaN() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Const(Double.NaN), new Const(Double.NaN));
 JXPathContext context = new JXPathContext();
 ContextFactory factory = new EmptyContextFactory();
 context.setContextItem(factory.makeContext(new InfoSet()));
 EvalContext evalContext = new EvalContext(context, null);

 Object result = expression.computeValue(evalContext);
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationGreaterThanNumberFormatException() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Const("a"), new Const(5.3));
 JXPathContext context = new JXPathContext();
 ContextFactory factory = new EmptyContextFactory();
 context.setContextItem(factory.makeContext(new InfoSet()));
 EvalContext evalContext = new EvalContext(context, null);

 Object result = expression.computeValue(evalContext);
 }
}