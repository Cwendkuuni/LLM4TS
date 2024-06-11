import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.FunctionMap;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationEqual coreOperationEqual;
 private EvalContext context;

 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.spy(new EvalContext());
 coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testEqualTrue() {
 Object leftValue = 5;
 Object rightValue = 5;
 Mockito.when(arg1.computeValue(context)).thenReturn(leftValue);
 Mockito.when(arg2.computeValue(context)).thenReturn(rightValue);
 Mockito.when(context.getFunctionMap()).thenReturn(new FunctionMap(Functions.getInstance()));

 Boolean result = coreOperationEqual.computeValue(context);
 assertTrue(result);
 }

 @Test
 public void testEqualFalse() {
 Object leftValue = 5;
 Object rightValue = 7;
 Mockito.when(arg1.computeValue(context)).thenReturn(leftValue);
 Mockito.when(arg2.computeValue(context)).thenReturn(rightValue);
 Mockito.when(context.getFunctionMap()).thenReturn(new FunctionMap(Functions.getInstance()));

 Boolean result = coreOperationEqual.computeValue(context);
 assertFalse(result);
 }

 @Test
 public void testEvaluateNullValues() {
 Object leftValue = null;
 Object rightValue = null;
 Mockito.when(arg1.computeValue(context)).thenReturn(leftValue);
 Mockito.when(arg2.computeValue(context)).thenReturn(rightValue);
 Mockito.when(context.getFunctionMap()).thenReturn(new FunctionMap(Functions.getInstance()));

 Boolean result = coreOperationEqual.computeValue(context);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateInvalidArg1() {
 arg1 = null;
 arg2 = Mockito.mock(Expression.class);
 Mockito.when(arg2.computeValue(context)).thenReturn(5);
 Mockito.when(context.getFunctionMap()).thenReturn(new FunctionMap(Functions.getInstance()));

 try {
 coreOperationEqual.computeValue(context);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateInvalidArg2() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = null;
 Mockito.when(arg1.computeValue(context)).thenReturn(5);
 Mockito.when(context.getFunctionMap()).thenReturn(new FunctionMap(Functions.getInstance()));

 try {
 coreOperationEqual.computeValue(context);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}