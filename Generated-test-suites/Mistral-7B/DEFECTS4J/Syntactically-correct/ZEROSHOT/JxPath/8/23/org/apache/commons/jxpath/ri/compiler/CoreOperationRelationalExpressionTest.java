import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreConstantExpression(1), new CoreConstantExpression(2));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public Object next() {
 return null;
 }
 });
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreConstantExpression(1), new CoreConstantExpression(2));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public Object next() {
 return null;
 }
 });
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));

 infoSet = new InfoSet();
 context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 2;
 }
 });
 result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));

 infoSet = new InfoSet();
 context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 });
 result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));
 }

 @Test
 public void testLessThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreConstantExpression(2), new CoreConstantExpression(1));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public Object next() {
 return null;
 }
 });
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));

 infoSet = new InfoSet();
 context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 });
 result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(new CoreConstantExpression(2), new CoreConstantExpression(1));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public Object next() {
 return null;
 }
 });
 Object result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));

 infoSet = new InfoSet();
 context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 1;
 }
 });
 result = expression.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testGreaterThanExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new InitialContext(infoSet, 1);
 }
 });
 Iterator left = ((Iterate) context.getVariableValue("x")).iterator();
 Iterator right = ((Iterate) context.getVariableValue("y")).iterator();
 boolean result = expression.compute(left, right);
 assertTrue(result);

 left = ((Iterate) context.getVariableValue("x")).iterator();
 right = ((Iterate) context.getVariableValue("y")).iterator();
 left.next();
 left.next();
 result = expression.compute(left, right);
 assertFalse(result);
 }

 @Test
 public void testGreaterThanOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new InitialContext(infoSet, 1);
 }
 });
 Iterator left = ((Iterate) context.getVariableValue("x")).iterator();
 Iterator right = ((Iterate) context.getVariableValue("y")).iterator();
 boolean result = expression.compute(left, right);
 assertFalse(result);

 left = ((Iterate) context.getVariableValue("x")).iterator();
 left.next();
 result = expression.compute(left, right);
 assertTrue(result);
 }

 @Test
 public void testLessThanExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new InitialContext(infoSet, 1);
 }
 });
 Iterator left = ((Iterate) context.getVariableValue("x")).iterator();
 Iterator right = ((Iterate) context.getVariableValue("y")).iterator();
 boolean result = expression.compute(left, right);
 assertFalse(result);

 left = ((Iterate) context.getVariableValue("x")).iterator();
 left.next();
 left.next();
 result = expression.compute(left, right);
 assertTrue(result);
 }

 @Test
 public void testLessThanOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet, new Iterate() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new InitialContext(infoSet, 1);
 }
 });
 Iterator left = ((Iterate) context.getVariableValue("x")).iterator();
 Iterator right = ((Iterate) context.getVariableValue("y")).iterator();
 boolean result = expression.compute(left, right);
 assertFalse(result);

 left = ((Iterate) context.getVariableValue("x")).iterator();
 left.next();
 result = expression.compute(left, right);
 assertTrue(result);
 }
}