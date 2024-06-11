import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationGreaterThanOrEqualTest {

 private CoreOperationGreaterThanOrEqual operation;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.mock(EvalContext.class);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 InfoSet infoSet = Mockito.mock(InfoSet.class);
 Mockito.when(arg1.computeValue(context)).thenReturn(5.0);
 Mockito.when(arg2.computeValue(context)).thenReturn(5.0);
 Mockito.when(context.getInfoSet()).thenReturn(infoSet);

 Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(5.0);
 Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(5.0);

 assertEquals(Boolean.TRUE, operation.computeValue(context));
 }

 @Test
 public void testComputeValueFalse() {
 InfoSet infoSet = Mockito.mock(InfoSet.class);
 Mockito.when(arg1.computeValue(context)).thenReturn(4.0);
 Mockito.when(arg2.computeValue(context)).thenReturn(5.0);
 Mockito.when(context.getInfoSet()).thenReturn(infoSet);

 Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(4.0);
 Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(5.0);

 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testComputeValueMixedTypes() {
 InfoSet infoSet = Mockito.mock(InfoSet.class);
 Mockito.when(arg1.computeValue(context)).thenReturn(5.0);
 Mockito.when(arg2.computeValue(context)).thenReturn("5.1");
 Mockito.when(context.getInfoSet()).thenReturn(infoSet);

 Mockito.when(InfoSetUtil.doubleValue(arg1.computeValue(context))).thenReturn(5.0);
 Mockito.when(InfoSetUtil.doubleValue(arg2.computeValue(context))).thenReturn(5.1);

 assertFalse(operation.computeValue(context).booleanValue());
 }

 @Test
 public void testGetSymbol() {
 assertEquals(">=", operation.getSymbol());
 }
}