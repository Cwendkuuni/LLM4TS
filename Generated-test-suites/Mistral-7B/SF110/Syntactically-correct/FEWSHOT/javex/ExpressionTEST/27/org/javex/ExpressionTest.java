package org.javex;

import static org.junit.Assert.*;
import org.junit.Test;
import org.javex.functions.*;
import org.javex.utils.JavexUtils;
import org.javex.utils.ExpressionStack;
import org.javex.utils.FunctionExpression;
import org.javex.utils.TreeMap;
import org.javex.utils.GenericFunctionExpression;

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
 FunctionExpression notFunction = new NotFunction();
 FunctionExpression notCharFunction = new NotCharFunction();
 FunctionExpression eqFunction = new EqualsFunction();
 FunctionExpression neFunction = new NotEqualsFunction();
 FunctionExpression gtFunction = new GreaterThanFunction();
 FunctionExpression geFunction = new GreaterEqThanFunction();
 FunctionExpression ltFunction = new LessThanFunction();
 FunctionExpression leFunction = new LessEqThanFunction();
 FunctionExpression isEmptyFunction = new IsEmptyFunction();
 FunctionExpression isFunction = new IsFunction();
 FunctionExpression isNullFunction = new IsNullFunction();
 FunctionExpression absFunction = new AbsFunction();
 FunctionExpression addFunction = new AddFunction();
 FunctionExpression subFunction = new SubtractFunction();
 FunctionExpression mulFunction = new MultiplyFunction();
 FunctionExpression divFunction = new DivideFunction();
 FunctionExpression powFunction = new PowerFunction();
 FunctionExpression powCharFunction = new PowerCharFunction();
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();
 FunctionExpression inListFunction = new InListFunction();
 FunctionExpression matchesFunction = new MatchesFunction();
 FunctionExpression andLogicalFunction = new AndLogicalFunction();
 FunctionExpression orLogicalFunction = new OrLogicalFunction();
 Map<FunctionExpression, FunctionExpression> reservedFunctions = new TreeMap<>();
 Expression.putFunction(andFunction, reservedFunctions);
 Expression.putFunction(orFunction, reservedFunctions);
 Expression.putFunction(notFunction, reservedFunctions);
 Expression.putFunction(notCharFunction, reservedFunctions);
 Expression.putFunction(eqFunction, reservedFunctions);
 Expression.putFunction(neFunction, reservedFunctions);
 Expression.putFunction(gtFunction, reservedFunctions);
 Expression.putFunction(geFunction, reservedFunctions);
 Expression.putFunction(ltFunction, reservedFunctions);
 Expression.putFunction(leFunction, reservedFunctions);
 Expression.putFunction(isEmptyFunction, reservedFunctions);
 Expression.putFunction(isFunction, reservedFunctions);
 Expression.putFunction(isNullFunction, reservedFunctions);
 Expression.putFunction(absFunction, reservedFunctions);
 Expression.putFunction(addFunction, reservedFunctions);
 Expression.putFunction(subFunction, reservedFunctions);
 Expression.putFunction(mulFunction, reservedFunctions);
 Expression.putFunction(divFunction, reservedFunctions);
 Expression.putFunction(powFunction, reservedFunctions);
 Expression.putFunction(powCharFunction, reservedFunctions);
 Expression.putFunction(dateAddFunction, reservedFunctions);
 Expression.putFunction(dateDiffFunction, reservedFunctions);
 Expression.putFunction(lengthFunction, reservedFunctions);
 Expression.putFunction(subStringFunction, reservedFunctions);
 Expression.putFunction(toLowerFunction, reservedFunctions);
 Expression.putFunction(toUpperFunction, reservedFunctions);
 Expression.putFunction(inListFunction, reservedFunctions);
 Expression.putFunction(matchesFunction, reservedFunctions);
 Expression.putFunction(andLogicalFunction, reservedFunctions);
 Expression.putFunction(orLogicalFunction, reservedFunctions);

 for (FunctionExpression function : new FunctionExpression[]{andFunction, orFunction, notFunction, notCharFunction, eqFunction, neFunction, gtFunction, geFunction, ltFunction, leFunction, isEmptyFunction, isFunction, isNullFunction, absFunction, addFunction, subFunction, mulFunction, divFunction, powFunction, powCharFunction, dateAddFunction, dateDiffFunction, lengthFunction, subStringFunction, toLowerFunction, toUpperFunction, inListFunction, matchesFunction, andLogicalFunction, orLogicalFunction}) {
 String symbol = function.getSimbol();
 assertTrue(Expression.instance().isReservedFunction(symbol));
 }
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression andFunction = new AndLogicalFunction();
 FunctionExpression orFunction = new OrLogicalFunction();
 FunctionExpression notFunction = new NotFunction();
 FunctionExpression notCharFunction = new NotCharFunction();
 FunctionExpression andLogicalFunction = new AndLogicalFunction();
 FunctionExpression orLogicalFunction = new OrLogicalFunction();

 for (FunctionExpression function : new FunctionExpression[]{andFunction, orFunction, notFunction, notCharFunction, andLogicalFunction, orLogicalFunction}) {
 String symbol = function.getSimbol();
 assertTrue(Expression.instance().isLogicalFunction(symbol));
 }
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression absFunction = new AbsFunction();
 FunctionExpression addFunction = new AddFunction();
 FunctionExpression subFunction = new SubtractFunction();
 FunctionExpression mulFunction = new MultiplyFunction();
 FunctionExpression divFunction = new DivideFunction();
 FunctionExpression powFunction = new PowerFunction();
 FunctionExpression powCharFunction = new PowerCharFunction();

 for (FunctionExpression function : new FunctionExpression[]{absFunction, addFunction, subFunction, mulFunction, divFunction, powFunction, powCharFunction}) {
 String symbol = function.getSimbol();
 assertTrue(Expression.instance().isMathFunction(symbol));
 }
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();

 for (FunctionExpression function : new FunctionExpression[]{dateAddFunction, dateDiffFunction}) {
 String symbol = function.getSimbol();
 assertTrue(Expression.instance().isDateFunction(symbol));
 }
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();
 FunctionExpression inListFunction = new InListFunction();
 FunctionExpression matchesFunction = new MatchesFunction();

 for (FunctionExpression function : new FunctionExpression[]{lengthFunction, subStringFunction, toLowerFunction, toUpperFunction, inListFunction, matchesFunction}) {
 String symbol = function.getSimbol();
 assertTrue(Expression.instance().isStringFunction(symbol));
 }
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression myFunction = new MyFunction();
 Expression expression = new Expression("myFunction(1, 2)");
 expression.setExternalFunctions(Arrays.asList(myFunction));
 String symbol = "myFunction";
 FunctionExpression result = Expression.instance().getExternalFunction(symbol);
 assertEquals(myFunction, result);
 }

 @Test
 public void testIsExternalFunction() {
 FunctionExpression myFunction = new MyFunction();
 Expression expression = new Expression("myFunction(1, 2)");
 expression.setExternalFunctions(Arrays.asList(myFunction));
 String symbol = "myFunction";
 assertTrue(Expression.instance().isExternalFunction(symbol));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 FunctionExpression notFunction = new NotFunction();
 FunctionExpression notCharFunction = new NotCharFunction();
 FunctionExpression eqFunction = new EqualsFunction();
 FunctionExpression neFunction = new NotEqualsFunction();
 FunctionExpression gtFunction = new GreaterThanFunction();
 FunctionExpression geFunction = new GreaterEqThanFunction();
 FunctionExpression ltFunction = new LessThanFunction();
 FunctionExpression leFunction = new LessEqThanFunction();
 FunctionExpression isEmptyFunction = new IsEmptyFunction();
 FunctionExpression isFunction = new IsFunction();
 FunctionExpression isNullFunction = new IsNullFunction();
 FunctionExpression absFunction = new AbsFunction();
 FunctionExpression addFunction = new AddFunction();
 FunctionExpression subFunction = new SubtractFunction();
 FunctionExpression mulFunction = new MultiplyFunction();
 FunctionExpression divFunction = new DivideFunction();
 FunctionExpression powFunction = new PowerFunction();
 FunctionExpression powCharFunction = new PowerCharFunction();
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 FunctionExpression toLowerFunction = new ToLowerFunction();
 FunctionExpression toUpperFunction = new ToUpperFunction();
 FunctionExpression inListFunction = new InListFunction();
 FunctionExpression matchesFunction = new MatchesFunction();
 FunctionExpression andLogicalFunction = new AndLogicalFunction();
 FunctionExpression orLogicalFunction = new OrLogicalFunction();
 FunctionExpression unknownFunction = new UnknownFunction();

 for (FunctionExpression function : new FunctionExpression[]{andFunction, orFunction, notFunction, notCharFunction, eqFunction, neFunction, gtFunction, geFunction, ltFunction, leFunction, isEmptyFunction, isFunction, isNullFunction, absFunction, addFunction, subFunction, mulFunction, divFunction, powFunction, powCharFunction, dateAddFunction, dateDiffFunction, lengthFunction, subStringFunction, toLowerFunction, toUpperFunction, inListFunction, matchesFunction, andLogicalFunction, orLogicalFunction, unknownFunction}) {
 String symbol = function.getSimbol();
 FunctionExpression result = Expression.instance().getFunction(symbol);
 assertEquals(function, result);
 }
 }

 @Test
 public void testPrepare() {
 String expression = "1 + 2 * (3 + 4)";
 Expression e = createExpression(expression);
 ExpressionStack stack = e.getStackCopy();
 assertEquals(expression, e.getExpression());
 assertFalse(e.isPrepared());
 e.prepare();
 assertTrue(e.isPrepared());
 assertEquals(Arrays.asList("1", "+", "2", "*", "(", "3", "+", "4", ")", ""), stack.getExpressions());
 }

 @Test
 public void testEvaluate() throws Exception {
 String expression = "1 + 2 * (3 + 4)";
 Expression e = createExpression(expression);
 Object result = e.evaluate();
 assertEquals(7, result);
 }

 @Test(expected = Exception.class)
 public void testEvaluateWithError() throws Exception {
 Expression e = new Expression("1 + 2 * (3 + x)");
 e.prepare();
 try {
 e.evaluate();
 fail();
 } catch (Exception ex) {
 assertEquals("Error: Unknown function x", ex.getMessage());
 }
 }
}

class MyFunction extends FunctionExpression {
 MyFunction() {
 super("myFunction", FunctionExpression.USER_DEFINED);
 }
}

class UnknownFunction extends FunctionExpression {
 UnknownFunction() {
 super("x", FunctionExpression.USER_DEFINED);
 }
}