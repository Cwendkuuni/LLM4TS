import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual_Arg1_Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual_Arg2_Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

 @Test
 public void testEqual_WhenBothArgumentsAreSame_ThenResultShouldBeTrue() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(1),
 new CoreOperationEqual_Arg2_Expression(1)
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testEqual_WhenFirstArgumentIsGreaterThanSecond_ThenResultShouldBeFalse() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(2),
 new CoreOperationEqual_Arg2_Expression(1)
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqual_WhenFirstArgumentIsLessThanSecond_ThenResultShouldBeFalse() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(1),
 new CoreOperationEqual_Arg2_Expression(2)
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqual_WhenFirstArgumentIsEqualToSecond_ThenResultShouldBeTrue() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(1),
 new CoreOperationEqual_Arg2_Expression(1)
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqual_WhenBothArgumentsAreNull_ThenAnIllegalArgumentExceptionShouldBeThrown() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(null),
 new CoreOperationEqual_Arg2_Expression(null)
 );
 EvalContext context = new EvalContext();
 operation.computeValue(context); // This line should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqual_WhenFirstArgumentIsANullAndSecondIsANumber_ThenAnIllegalArgumentExceptionShouldBeThrown() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(null),
 new CoreOperationEqual_Arg2_Expression(1)
 );
 EvalContext context = new EvalContext();
 operation.computeValue(context); // This line should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqual_WhenFirstArgumentIsANumberAndSecondIsANull_ThenAnIllegalArgumentExceptionShouldBeThrown() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(1),
 new CoreOperationEqual_Arg2_Expression(null)
 );
 EvalContext context = new EvalContext();
 operation.computeValue(context); // This line should throw an IllegalArgumentException
 }

 @Test
 public void testEqual_WhenFirstArgumentIsANumberAndSecondIsAnotherNumber_ThenResultShouldBeFalse() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression(1),
 new CoreOperationEqual_Arg2_Expression(2)
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqual_WhenFirstArgumentIsAStringAndSecondIsAnotherString_ThenResultShouldBeFalse() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression("hello"),
 new CoreOperationEqual_Arg2_Expression("world")
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testEqual_WhenFirstArgumentIsAStringAndSecondIsTheSameString_ThenResultShouldBeTrue() {
 CoreOperationEqual operation = new CoreOperationEqual(
 new CoreOperationEqual_Arg1_Expression("hello"),
 new CoreOperationEqual_Arg2_Expression("hello")
 );
 EvalContext context = new EvalContext();
 Object result = operation.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }
}