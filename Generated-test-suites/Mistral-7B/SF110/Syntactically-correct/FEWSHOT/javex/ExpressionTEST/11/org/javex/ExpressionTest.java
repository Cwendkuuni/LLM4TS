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
import org.javex.FunctionExpression;
import java.util.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testIsReservedFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 FunctionExpression equalsFunction = new EqualsFunction();
 FunctionExpression greaterEqThanFunction = new GreaterEqThanFunction();
 FunctionExpression greaterThanFunction = new GreaterThanFunction();
 FunctionExpression notFunction = new NotFunction();
 FunctionExpression notCharFunction = new NotCharFunction();
 FunctionExpression betweenFunction = new BetweenFunction();
 FunctionExpression containsFunction = new ContainsFunction();
 FunctionExpression endsWithFunction = new EndsWithFunction();
 FunctionExpression isFunction = new IsFunction();
 FunctionExpression isEmptyFunction = new IsEmptyFunction();
 FunctionExpression isNullFunction = new IsNullFunction();
 FunctionExpression lessEqThanFunction = new LessEqThanFunction();
 FunctionExpression lessThanFunction = new LessThanFunction();
 FunctionExpression notEqualsFunction = new NotEqualsFunction();
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();

 assertEquals(true, Expression.isReservedFunction("and"));
 assertEquals(true, Expression.isReservedFunction("or"));
 assertEquals(true, Expression.isReservedFunction("equals"));
 assertEquals(true, Expression.isReservedFunction(">="));
 assertEquals(true, Expression.isReservedFunction(">"));
 assertEquals(true, Expression.isReservedFunction("not"));
 assertEquals(true, Expression.isReservedFunction("not char"));
 assertEquals(true, Expression.isReservedFunction("between"));
 assertEquals(true, Expression.isReservedFunction("contains"));
 assertEquals(true, Expression.isReservedFunction("endsWith"));
 assertEquals(true, Expression.isReservedFunction("is"));
 assertEquals(true, Expression.isReservedFunction("isEmpty"));
 assertEquals(true, Expression.isReservedFunction("is null"));
 assertEquals(true, Expression.isReservedFunction("<="));
 assertEquals(true, Expression.isReservedFunction("<"));
 assertEquals(true, Expression.isReservedFunction("!="));
 assertEquals(true, Expression.isReservedFunction("dateAdd"));
 assertEquals(true, Expression.isReservedFunction("dateDiff"));
 assertEquals(true, Expression.isReservedFunction("length"));
 assertEquals(true, Expression.isReservedFunction("substring"));
 assertEquals(true, Expression.isReservedFunction("tolower"));
 assertEquals(true, Expression.isReservedFunction("toupper"));

 assertEquals(false, Expression.isReservedFunction("myFunction"));
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();

 assertEquals(true, Expression.isLogicalFunction("and"));
 assertEquals(true, Expression.isLogicalFunction("or"));
 assertEquals(false, Expression.isLogicalFunction("+"));
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression absFunction = new AbsFunction();
 FunctionExpression divideFunction = new DivideFunction();
 FunctionExpression multiplyFunction = new MultiplyFunction();
 FunctionExpression plusFunction = new PlusFunction();
 FunctionExpression sqrtFunction = new SqrtFunction();
 FunctionExpression subtractFunction = new SubtractFunction();
 FunctionExpression powerFunction = new PowerFunction();
 FunctionExpression powerCharFunction = new PowerCharFunction();

 assertEquals(true, Expression.isMathFunction("abs"));
 assertEquals(true, Expression.isMathFunction("divide"));
 assertEquals(true, Expression.isMathFunction("multiply"));
 assertEquals(true, Expression.isMathFunction("plus"));
 assertEquals(true, Expression.isMathFunction("sqrt"));
 assertEquals(true, Expression.isMathFunction("subtract"));
 assertEquals(true, Expression.isMathFunction("power"));
 assertEquals(true, Expression.isMathFunction("power char"));

 assertEquals(false, Expression.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();

 assertEquals(true, Expression.isDateFunction("dateAdd"));
 assertEquals(true, Expression.isDateFunction("dateDiff"));

 assertEquals(false, Expression.isDateFunction("and"));
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();

 assertEquals(true, Expression.isStringFunction("length"));
 assertEquals(true, Expression.isStringFunction("substring"));
 assertEquals(true, Expression.isStringFunction("tolower"));
 assertEquals(true, Expression.isStringFunction("toupper"));

 assertEquals(false, Expression.isStringFunction("and"));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 FunctionExpression equalsFunction = new EqualsFunction();
 FunctionExpression greaterEqThanFunction = new GreaterEqThanFunction();
 FunctionExpression greaterThanFunction = new GreaterThanFunction();
 FunctionExpression notFunction = new NotFunction();
 FunctionExpression notCharFunction = new NotCharFunction();
 FunctionExpression betweenFunction = new BetweenFunction();
 FunctionExpression containsFunction = new ContainsFunction();
 FunctionExpression endsWithFunction = new EndsWithFunction();
 FunctionExpression isFunction = new IsFunction();
 FunctionExpression isEmptyFunction = new IsEmptyFunction();
 FunctionExpression isNullFunction = new IsNullFunction();
 FunctionExpression lessEqThanFunction = new LessEqThanFunction();
 FunctionExpression lessThanFunction = new LessThanFunction();
 FunctionExpression notEqualsFunction = new NotEqualsFunction();
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();
 FunctionExpression myFunction = new MyFunction();

 assertEquals(andFunction, Expression.getFunction("and"));
 assertEquals(orFunction, Expression.getFunction("or"));
 assertEquals(equalsFunction, Expression.getFunction("equals"));
 assertEquals(greaterEqThanFunction, Expression.getFunction(">="));
 assertEquals(greaterThanFunction, Expression.getFunction(">"));
 assertEquals(notFunction, Expression.getFunction("not"));
 assertEquals(notCharFunction, Expression.getFunction("not char"));
 assertEquals(betweenFunction, Expression.getFunction("between"));
 assertEquals(containsFunction, Expression.getFunction("contains"));
 assertEquals(endsWithFunction, Expression.getFunction("endsWith"));
 assertEquals(isFunction, Expression.getFunction("is"));
 assertEquals(isEmptyFunction, Expression.getFunction("isEmpty"));
 assertEquals(isNullFunction, Expression.getFunction("is null"));
 assertEquals(lessEqThanFunction, Expression.getFunction("<="));
 assertEquals(lessThanFunction, Expression.getFunction("<"));
 assertEquals(notEqualsFunction, Expression.getFunction("!="));
 assertEquals(dateAddFunction, Expression.getFunction("dateAdd"));
 assertEquals(dateDiffFunction, Expression.getFunction("dateDiff"));
 assertEquals(lengthFunction, Expression.getFunction("length"));
 assertEquals(subStringFunction, Expression.getFunction("substring"));
 assertEquals(toLowerFunction, Expression.getFunction("tolower"));
 assertEquals(toUpperFunction, Expression.getFunction("toupper"));
 assertEquals(null, Expression.getFunction("myFunction"));
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression myFunction = new MyFunction();
 Expression expression = new Expression("myFunction(1, 2)");
 expression.setExternalFunctions(Arrays.asList(myFunction));
 assertEquals(myFunction, expression.getFunction("myFunction"));
 }

 @Test
 public void testPrepare() {
 Expression expression = new Expression("1 + 2 * 3 > 4 and a = b");
 expression.prepare();

 List<String> stack = expression.getStackCopy();
 List<String> expectedStack = Arrays.asList("1", "2", "*", "3", "+", "4", ">", ">", "and", "a", "b");
 assertEquals(expectedStack, stack);
 }

}

 class MyFunction implements FunctionExpression {
 @Override
 public String getSimbol() {
 return "myFunction";
 }

 @Override
 public int getType() {
 return FunctionExpression.CUSTOM;
 }
 }