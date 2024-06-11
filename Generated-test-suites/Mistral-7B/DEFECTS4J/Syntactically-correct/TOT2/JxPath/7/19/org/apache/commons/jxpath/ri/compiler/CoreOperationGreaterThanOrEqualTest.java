// CoreOperationGreaterThanOrEqualTest1.javaimport org.apache.commons.jxpath.ri.EvalContext;import org.apache.commons.jxpath.ri.InfoSet;import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest1 {

 @Test
 public void testCoreOperationGreaterThanOrEqual_Double_Double_True() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(5.0), new CoreConstantExpression(5.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 assert (coreOperationGreaterThanOrEqual.computeValue(context) instanceof Boolean && ((Boolean) coreOperationGreaterThanOrEqual.computeValue(context)).booleanValue());
 }

 @Test(expected = ClassCastException.class)
 public void testCoreOperationGreaterThanOrEqual_Double_String_Exception() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(5.0), new CoreStringConstantExpression("5.0"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 coreOperationGreaterThanOrEqual.computeValue(context);
 }

 @Test
 public void testCoreOperationGreaterThanOrEqual_Double_Double_False() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(4.0), new CoreConstantExpression(5.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 assert (coreOperationGreaterThanOrEqual.computeValue(context) instanceof Boolean && !((Boolean) coreOperationGreaterThanOrEqual.computeValue(context)).booleanValue());
 }

 @Test
 public void testCoreOperationGreaterThanOrEqual_Double_Double_Zero_Zero() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(0.0), new CoreConstantExpression(0.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 assert (coreOperationGreaterThanOrEqual.computeValue(context) instanceof Boolean && ((Boolean) coreOperationGreaterThanOrEqual.computeValue(context)).booleanValue());
 }
}