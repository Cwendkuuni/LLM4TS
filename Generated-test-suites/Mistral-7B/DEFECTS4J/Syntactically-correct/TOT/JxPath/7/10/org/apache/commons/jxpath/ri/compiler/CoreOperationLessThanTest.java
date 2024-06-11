import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.PathParser;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.apache.commons.jxpath.ri.expr.Variable;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {

 private Expression arg1, arg2;
 private CoreOperationLessThan lessThan;

 @Before
 public void setUp() {
 Variable x = new Variable("x");
 Variable y = new Variable("y");
 arg1 = new VariableRef(0, x);
 arg2 = new VariableRef(0, y);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 }

 @Test
 public void testLessThan() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("x", 5.0);
 context.setVariable("y", 10.0);
 Object result = lessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 context.setVariable("x", 2.0);
 context.setVariable("y", 5.0);
 result = lessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanWithNaN() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("x", Double.NaN);
 context.setVariable("y", 5.0);
 Object result = lessThan.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessThanWithNull() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("x", Double.NaN);
 context.setVariable("y", null);
 Object result = lessThan.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessThanWithNegativeNumbers() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("x", -3.0);
 context.setVariable("y", -2.0);
 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 context.setVariable("x", -3.0);
 context.setVariable("y", -4.0);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithZero() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("x", 0.0);
 context.setVariable("y", 5.0);
 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);

 context.setVariable("x", 0.0);
 context.setVariable("y", 0.0);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);

 context.setVariable("x", 0.0);
 context.setVariable("y", -1.0);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }
}