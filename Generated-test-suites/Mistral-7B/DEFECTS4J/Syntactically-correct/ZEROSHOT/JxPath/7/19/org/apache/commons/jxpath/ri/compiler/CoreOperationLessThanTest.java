package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.util.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

 /**
 * Test constructor when arguments are provided.
 */
 @Test
 public void testConstructorWithArguments() {
 Expression arg1 = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return InfoSetUtil.createDoubleInfoSet(2.5);
 }
 };
 Expression arg2 = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return InfoSetUtil.createDoubleInfoSet(5.0);
 }
 };
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
 assertEquals(2, coreOperationLessThan.getArity());
 assertEquals(new Expression[] { arg1, arg2 }, coreOperationLessThan.getArgs());
 }

 /**
 * Test computeValue method when the left argument is less than the right argument.
 */
 @Test
 public void testComputeValueWhenLeftArgumentIsLessThanRightArgument() {
 InfoSet leftInfoSet = InfoSetUtil.createDoubleInfoSet(2.5);
 InfoSet rightInfoSet = InfoSetUtil.createDoubleInfoSet(5.0);
 Expression arg1 = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return leftInfoSet;
 }
 };
 Expression arg2 = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return rightInfoSet;
 }
 };
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
 EvalContext context = new EvalContext(new Path(""));
 Object result = coreOperationLessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 /**
 * Test computeValue method when the left argument is greater than or equal to the right argument.
 */
 @Test
 public void testComputeValueWhenLeftArgumentIsGreaterThanOrEqualRightArgument() {
 InfoSet leftInfoSet = InfoSetUtil.createDoubleInfoSet(5.0);
 InfoSet rightInfoSet = InfoSetUtil.createDoubleInfoSet(2.5);
 Expression arg1 = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return leftInfoSet;
 }
 };
 Expression arg2 = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return rightInfoSet;
 }
 };
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
 EvalContext context = new EvalContext(new Path(""));
 Object result = coreOperationLessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 /**
 * Test getSymbol method.
 */
 @Test
 public void testGetSymbol() {
 CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(null, null);
 assertEquals("<", coreOperationLessThan.getSymbol());
 }
}