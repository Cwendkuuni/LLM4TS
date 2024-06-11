import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.Iterator;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CoreOperationRelationalExpressionTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression expression = mock(CoreOperationRelationalExpression.class);
 Object leftValue = new InitialContext();
 Object rightValue = new SelfContext();
 when(expression.computeValue(any(EvalContext.class))).thenCallRealMethod();
 when(leftValue.getClass()).thenReturn(InitialContext.class);
 when(rightValue.getClass()).thenReturn(SelfContext.class);

 EvalContext context = mock(EvalContext.class);
 when(context.getSingleNodePointer()).thenReturn(rightValue);

 Object result = expression.computeValue(context);
 verify(expression).compute(leftValue, rightValue);
 assertTrue(result instanceof Boolean);
 verify(expression, times(1)).compute(any(Object.class), any(Object.class));
 }

 @Test
 public void testComputeWithBooleanValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(true), new CoreOperationConstant(false));

 Object left = new Boolean(true);
 Object right = new Boolean(false);

 Object result = expression.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeWithSameDoubleValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(5.5), new CoreOperationConstant(5.5));

 Object left = 5.5;
 Object right = 5.5;

 Object result = expression.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeWithDifferentDoubleValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(5.5), new CoreOperationConstant(3.0));

 Object left = 5.5;
 Object right = 3.0;

 Object result = expression.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeWithDifferentDoubleValuesTrue() {
 CoreOperationRelationalExpression expression = new CoreOperationGte(new CoreOperationConstant(5.5), new CoreOperationConstant(6.0));

 Object left = 5.5;
 Object right = 6.0;

 Object result = expression.computeValue(new EvalContext());
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndSameValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(5), new CoreOperationConstant(5));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(5);

 Object result = expression.computeValue(new EvalContext());
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndDifferentValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(5), new CoreOperationConstant(6));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(6);

 Object result = expression.computeValue(new EvalContext());
 verify(left, times(1)).hasNext();
 verify(right, times(1)).hasNext();
 verify(left, times(1)).next();
 verify(right, times(1)).next();
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndMatchingValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(5), new CoreOperationConstant(5));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);
 when(left.next()).thenReturn(6);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(5);
 when(right.next()).thenReturn(5);

 Object result = expression.computeValue(new EvalContext());
 verify(left, times(2)).hasNext();
 verify(left, times(2)).next();
 verify(right, times(2)).hasNext();
 verify(right, times(2)).next();
 verify(left, times(1)).hasNext();
 verify(right, times(1)).hasNext();
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndMatchingValuesTrue() {
 CoreOperationRelationalExpression expression = new CoreOperationGte(new CoreOperationConstant(5), new CoreOperationConstant(5));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);
 when(left.next()).thenReturn(6);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(5);
 when(right.next()).thenReturn(5);

 Object result = expression.computeValue(new EvalContext());
 verify(left, times(2)).hasNext();
 verify(left, times(2)).next();
 verify(right, times(2)).hasNext();
 verify(right, times(2)).next();
 verify(left, times(1)).hasNext();
 verify(right, times(1)).hasNext();
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndNoMatchingValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGt(new CoreOperationConstant(5), new CoreOperationConstant(5));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);
 when(left.next()).thenReturn(6);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(7);

 Object result = expression.computeValue(new EvalContext());
 verify(left, times(2)).hasNext();
 verify(left, times(2)).next();
 verify(right, times(2)).hasNext();
 verify(right, times(2)).next();
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndContainingValues() {
 CoreOperationRelationalExpression expression = new CoreOperationContains(new CoreOperationConstant(5), new CoreOperationConstant(new Integer[] { 5, 6, 7 }));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(5);
 when(right.next()).thenReturn(6);

 Object result = expression.computeValue(new EvalContext());
 verify(left, times(1)).hasNext();
 verify(left, times(1)).next();
 verify(right, times(2)).hasNext();
 verify(right, times(2)).next();
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComputeWithIteratorAndNotContainingValues() {
 CoreOperationRelationalExpression expression = new CoreOperationContains(new CoreOperationConstant(5), new CoreOperationConstant(new Integer[] { 5, 6, 7 }));

 Iterator left = mock(Iterator.class);
 when(left.hasNext()).thenReturn(true);
 when(left.next()).thenReturn(5);

 Iterator right = mock(Iterator.class);
 when(right.hasNext()).thenReturn(true);
 when(right.next()).thenReturn(8);

 Object result = expression.computeValue(new EvalContext());
 verify(left, times(1)).hasNext();
 verify(left, times(1)).next();
 verify(right, times(1)).hasNext();
 verify(right, times(1)).next();
 verifyNoMoreInteractions(left, right);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

}