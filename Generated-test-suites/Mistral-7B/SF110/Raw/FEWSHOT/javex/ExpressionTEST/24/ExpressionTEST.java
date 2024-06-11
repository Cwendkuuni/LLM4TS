package org.javex.test;

import static org.junit.Assert.*;

import org.javex.utils.*;
import org.javex.functions.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testIsReservedFunction() {
 FunctionExpression[] functions = {
 new AndFunction(),
 new BetweenFunction(),
 new ContainsFunction(),
 new EndsWithFunction(),
 new EqualsFunction(),
 new GreaterEqThanFunction(),
 new GreaterThanFunction(),
 new IsEmptyFunction(),
 new IsFunction(),
 new IsNullFunction(),
 new LessEqThanFunction(),
 new LessThanFunction(),
 new NotEqualsFunction(),
 new NotFunction(),
 new NotCharFunction(),
 new OrFunction(),
 new StartsWithFunction(),
 new MatchesFunction(),
 new InListFunction(),
 new AbsFunction(),
 new DivideFunction(),
 new MultiplyFunction(),
 new PlusFunction(),
 new SqrtFunction(),
 new SubstractFunction(),
 new PowerFunction(),
 new PowerCharFunction(),
 new DateAddFunction(),
 new DateDiffFunction(),
 new LengthFunction(),
 new SubStringFunction(),
 new ToLowerFunction(),
 new ToUpperFunction()
 };
 for (FunctionExpression function : functions) {
 assertTrue(Expression.class.getDeclaredMethod("isReservedFunction", String.class).invoke(null, function.getSimbol()));
 }
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 FunctionExpression function = new FunctionExpression("invalidFunction", FunctionExpression.LOGICAL);
 Expression expression = new Expression("and(1, 2, or(3, 4))");
 Expression e = createExpression(expression.expression);
 List functions = e.getExternalFunctions();
 FunctionExpression externalAndFunction = new FunctionExpression("and", FunctionExpression.LOGICAL, functions);
 FunctionExpression externalOrFunction = new FunctionExpression("or", FunctionExpression.LOGICAL, functions);
 assertTrue(e.isLogicalFunction("and"));
 assertTrue(e.isLogicalFunction("or"));
 assertFalse(e.isLogicalFunction("invalidFunction"));
 assertFalse(e.isLogicalFunction("!"));
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression absFunction = new AbsFunction();
 FunctionExpression addFunction = new AddFunction();
 FunctionExpression subtractFunction = new SubtractFunction();
 FunctionExpression multiplyFunction = new MultiplyFunction();
 FunctionExpression divideFunction = new DivideFunction();
 FunctionExpression powerFunction = new PowerFunction();
 FunctionExpression powerCharFunction = new PowerCharFunction();
 FunctionExpression sqrtFunction = new SqrtFunction();
 Expression expression = new Expression("abs(2) + 3 * 4 / 2 ^ 2 - sqrt(16) ^ 2");
 Expression e = createExpression(expression.expression);
 List functions = e.getExternalFunctions();
 FunctionExpression externalAbsFunction = new FunctionExpression("abs", FunctionExpression.MATH, functions);
 FunctionExpression externalAddFunction = new FunctionExpression("+", FunctionExpression.MATH, functions);
 FunctionExpression externalSubtractFunction = new FunctionExpression("-", FunctionExpression.MATH, functions);
 FunctionExpression externalMultiplyFunction = new FunctionExpression("*", FunctionExpression.MATH, functions);
 FunctionExpression externalDivideFunction = new FunctionExpression("/", FunctionExpression.MATH, functions);
 FunctionExpression externalPowerFunction = new FunctionExpression("^", FunctionExpression.MATH, functions);
 FunctionExpression externalSqrtFunction = new FunctionExpression("sqrt", FunctionExpression.MATH, functions);
 assertTrue(e.isMathFunction("abs"));
 assertTrue(e.isMathFunction("+"));
 assertTrue(e.isMathFunction("*"));
 assertTrue(e.isMathFunction("/"));
 assertTrue(e.isMathFunction("^"));
 assertTrue(e.isMathFunction("sqrt"));
 assertFalse(e.isMathFunction("!"));
 assertFalse(e.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();
 Expression expression = new Expression("dateAdd(now(), 1) > dateDiff(date('2022-01-01'), now())");
 Expression e = createExpression(expression.expression);
 List functions = e.getExternalFunctions();
 FunctionExpression externalDateAddFunction = new FunctionExpression("dateAdd", FunctionExpression.DATE, functions);
 FunctionExpression externalDateDiffFunction = new FunctionExpression("dateDiff", FunctionExpression.DATE, functions);
 assertTrue(e.isDateFunction("dateAdd"));
 assertTrue(e.isDateFunction("dateDiff"));
 assertFalse(e.isDateFunction("!"));
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 Expression expression = new Expression("length(upper('JAVEX')) = length(lower('JAVEX'))");
 Expression e = createExpression(expression.expression);
 List functions = e.getExternalFunctions();
 FunctionExpression externalLengthFunction = new FunctionExpression("length", FunctionExpression.STRING, functions);
 FunctionExpression externalSubStringFunction = new FunctionExpression("substring", FunctionExpression.STRING, functions);
 FunctionExpression externalToUpperFunction = new FunctionExpression("toUpper", FunctionExpression.STRING, functions);
 FunctionExpression externalToLowerFunction = new FunctionExpression("toLower", FunctionExpression.STRING, functions);
 assertTrue(e.isStringFunction("length"));
 assertTrue(e.isStringFunction("substring"));
 assertTrue(e.isStringFunction("toUpper"));
 assertTrue(e.isStringFunction("toLower"));
 assertFalse(e.isStringFunction("!"));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 FunctionExpression function = new FunctionExpression("invalidFunction", FunctionExpression.LOGICAL);
 Expression expression = new Expression("and(1, 2, or(3, 4))");
 Expression e = createExpression(expression.expression);
 List functions = e.getExternalFunctions();
 FunctionExpression externalAndFunction = new FunctionExpression("and", FunctionExpression.LOGICAL, functions);
 FunctionExpression externalOrFunction = new FunctionExpression("or", FunctionExpression.LOGICAL, functions);
 FunctionExpression externalInvalidFunction = new FunctionExpression("invalidFunction", FunctionExpression.LOGICAL, functions);
 assertEquals(andFunction, Expression.class.getDeclaredMethod("getFunction", String.class).invoke(e, "and"));
 assertEquals(orFunction, Expression.class.getDeclaredMethod("getFunction", String.class).invoke(e, "or"));
 assertNull(Expression.class.getDeclaredMethod("getFunction", String.class).invoke(e, "invalidFunction"));
 }

 @Test
 public void testTokenize() {
 String expression = "and(1, 2, or(3, 4), 'test')";
 Expression e = createExpression(expression);
 ExpressionStack stack = e.getStackCopy();
 List expected = Arrays.asList("and", "1", "2", "or", "3", "4", "test");
 List actual = new ArrayList();
 for (String s : stack) {
 actual.add(s.trim());
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testPrepare() {
 String expression = "and(1, 2, or(3, 4), 'test')";
 Expression e = createExpression(expression);
 List functions = e.getExternalFunctions();
 assertEquals(0, functions.size());
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 // Ignore
 }
 assertNotNull(result);
 }

}