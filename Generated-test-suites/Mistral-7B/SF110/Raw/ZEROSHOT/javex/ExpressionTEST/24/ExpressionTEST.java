package org.javex.Expression;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.EqualsFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.functions.logical.NotFunction;
import org.javex.functions.logical.NotCharFunction;
import org.javex.functions.logical.AndFunctionExpression;
import org.javex.functions.logical.OrFunctionExpression;
import org.javex.functions.logical.LogicalFunctionExpression;
import org.javex.functions.math.MathFunctionExpression;
import org.javex.functions.date.DateFunctionExpression;
import org.javex.functions.string.StringFunctionExpression;
import org.javex.Expression;
import org.javex.ExpressionStack;
import java.util.*;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;
 private List<FunctionExpression> externalFunctions;

 @Before
 public void setUp() {
 expression = new Expression("1 + 2 * (3 + 4) and (5 > 3 and a = 'John') and (startsWith(x, 'Hello') or endsWith(y, 'World'))");
 stack = new ExpressionStack();
 externalFunctions = new ArrayList<>();
 externalFunctions.add(new AndFunctionExpression("and"));
 externalFunctions.add(new OrFunctionExpression("or"));
 externalFunctions.add(new AndFunction());
 externalFunctions.add(new OrFunction());
 externalFunctions.add(new ContainsFunction("contains"));
 externalFunctions.add(new EndsWithFunction("endsWith"));
 externalFunctions.add(new StartsWithFunction("startsWith"));
 externalFunctions.add(new EqualsFunction("="));
 externalFunctions.add(new ToLowerFunction("tolower"));
 externalFunctions.add(new ToUpperFunction("toupper"));
 }

 @After
 public void tearDown() {
 expression = null;
 stack = null;
 externalFunctions = null;
 }

 @Test
 public void testIsFunction() {
 Assert.assertTrue(expression.isFunction("+"));
 Assert.assertTrue(expression.isFunction("*"));
 Assert.assertTrue(expression.isFunction("("));
 Assert.assertTrue(expression.isFunction(")"));
 Assert.assertTrue(expression.isFunction("and"));
 Assert.assertTrue(expression.isFunction("or"));
 Assert.assertTrue(expression.isFunction("="));
 Assert.assertTrue(expression.isFunction(">"));
 Assert.assertTrue(expression.isFunction("<"));
 Assert.assertTrue(expression.isFunction("is"));
 Assert.assertTrue(expression.isFunction("not"));
 Assert.assertTrue(expression.isFunction("contains"));
 Assert.assertTrue(expression.isFunction("startsWith"));
 Assert.assertTrue(expression.isFunction("endsWith"));
 Assert.assertFalse(expression.isFunction("=")); // Check for different symbol
 }

 @Test
 public void testGetFunction() {
 Assert.assertEquals(new AndFunctionExpression("and"), expression.getFunction("and"));
 Assert.assertEquals(new OrFunctionExpression("or"), expression.getFunction("or"));
 Assert.assertEquals(new AndFunction(), expression.getFunction("and"));
 Assert.assertEquals(new OrFunction(), expression.getFunction("or"));
 Assert.assertEquals(new ContainsFunction(), expression.getFunction("contains"));
 Assert.assertEquals(new EndsWithFunction(), expression.getFunction("endsWith"));
 Assert.assertEquals(new StartsWithFunction(), expression.getFunction("startsWith"));
 Assert.assertEquals(new EqualsFunction(), expression.getFunction("="));
 Assert.assertNull(expression.getFunction("abc"));
 }

 @Test
 public void testPrepare() {
 expression.prepare();
 Expression preparedExpression = new Expression(expression.getStackCopy().toString());
 preparedExpression.prepare();
 Assert.assertEquals(preparedExpression.getStack(), expression.getStack());
 }

 @Test
 public void testEvaluate() throws Exception {
 expression.prepare();
 Assert.assertEquals(new Integer(5), expression.evaluate());
 }

 @Test(expected = Exception.class)
 public void testInvalidExpression() {
 expression = new Expression("1 + 2 * (3 + 4) and (5 > 3 and a = 'John') and (startsWith(x, 'Hello') or endsWith(y, 'World') z");
 expression.prepare();
 }

 @Test
 public void testIsLogicalFunction() {
 Assert.assertTrue(expression.isLogicalFunction("and"));
 Assert.assertTrue(expression.isLogicalFunction("or"));
 Assert.assertFalse(expression.isLogicalFunction("+"));
 Assert.assertFalse(expression.isLogicalFunction("="));
 }

 @Test
 public void testIsMathFunction() {
 Assert.assertTrue(expression.isMathFunction("+"));
 Assert.assertTrue(expression.isMathFunction("-"));
 Assert.assertTrue(expression.isMathFunction("*"));
 Assert.assertTrue(expression.isMathFunction("/"));
 Assert.assertTrue(expression.isMathFunction("sqrt"));
 Assert.assertTrue(expression.isMathFunction("abs"));
 Assert.assertFalse(expression.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 Assert.assertTrue(expression.isDateFunction("dateadd"));
 Assert.assertTrue(expression.isDateFunction("datediff"));
 Assert.assertFalse(expression.isDateFunction("and"));
 }

 @Test
 public void testIsStringFunction() {
 Assert.assertTrue(expression.isStringFunction("contains"));
 Assert.assertTrue(expression.isStringFunction("startsWith"));
 Assert.assertTrue(expression.isStringFunction("endsWith"));
 Assert.assertTrue(expression.isStringFunction("tolower"));
 Assert.assertTrue(expression.isStringFunction("toupper"));
 Assert.assertFalse(expression.isStringFunction("and"));
 }

 @Test
 public void testIsExternalFunction() {
 Assert.assertTrue(expression.isExternalFunction("and"));
 Assert.assertTrue(expression.isExternalFunction("or"));
 Assert.assertFalse(expression.isExternalFunction("+"));
 }

 @Test
 public void testIsReservedFunction() {
 Assert.assertTrue(expression.isReservedFunction("and"));
 Assert.assertTrue(expression.isReservedFunction("or"));
 Assert.assertFalse(expression.isReservedFunction("+"));
 }

 @Test
 public void testGetExternalFunctions() {
 FunctionExpression andFunction = expression.getFunction("and");
 FunctionExpression orFunction = expression.getFunction("or");
 Assert.assertEquals(2, expression.getExternalFunctions().size());
 Assert.assertTrue(expression.getExternalFunctions().contains(andFunction));
 Assert.assertTrue(expression.getExternalFunctions().contains(orFunction));
 }
}