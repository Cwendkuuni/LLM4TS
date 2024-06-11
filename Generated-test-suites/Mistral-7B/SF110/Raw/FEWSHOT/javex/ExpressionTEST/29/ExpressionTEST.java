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
 Expression e = createExpression(function.getSimbol());
 assertTrue(e.isReservedFunction(function.getSimbol()));
 }
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 Expression e = createExpression("and");
 assertTrue(e.isLogicalFunction("and"));
 e = createExpression("or");
 assertTrue(e.isLogicalFunction("or"));
 assertFalse(e.isLogicalFunction(""));
 assertFalse(e.isLogicalFunction("abc"));
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression absFunction = new AbsFunction();
 FunctionExpression divideFunction = new DivideFunction();
 FunctionExpression plusFunction = new PlusFunction();
 Expression e = createExpression("abs");
 assertTrue(e.isMathFunction("abs"));
 e = createExpression("divide");
 assertTrue(e.isMathFunction("divide"));
 e = createExpression("plus");
 assertTrue(e.isMathFunction("plus"));
 assertFalse(e.isMathFunction(""));
 assertFalse(e.isMathFunction("abc"));
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();
 Expression e = createExpression("dateAdd");
 assertTrue(e.isDateFunction("dateAdd"));
 e = createExpression("dateDiff");
 assertTrue(e.isDateFunction("dateDiff"));
 assertFalse(e.isDateFunction(""));
 assertFalse(e.isDateFunction("abc"));
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();
 Expression e = createExpression("length");
 assertTrue(e.isStringFunction("length"));
 e = createExpression("subString");
 assertTrue(e.isStringFunction("subString"));
 assertFalse(e.isStringFunction(""));
 assertFalse(e.isStringFunction("abc"));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 Expression e = createExpression("and");
 assertEquals(andFunction, e.getFunction("and"));
 e = createExpression("or");
 assertEquals(orFunction, e.getFunction("or"));
 assertNull(e.getFunction(""));
 assertNull(e.getFunction("abc"));
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression addFunction = new AddFunction();
 Expression e = new Expression("");
 e.setExternalFunctions(Arrays.asList(addFunction));
 assertEquals(addFunction, e.getExternalFunction("add"));
 assertNull(e.getExternalFunction(""));
 assertNull(e.getExternalFunction("abc"));
 }

 @Test
 public void testEvaluate() throws Exception {
 Expression e = createExpression("1 + 2 * 3");
 assertEquals(7.0, e.evaluate());
 e = createExpression("3 / 2 + 1");
 assertEquals(3.5, e.evaluate());
 e = createExpression("1 + (2 * 3)");
 assertEquals(7.0, e.evaluate());
 e = createExpression("(1 + 2) * 3");
 assertEquals(9.0, e.evaluate());
 e = createExpression("1 + (2 + 3)");
 assertEquals(6.0, e.evaluate());
 e = createExpression("(1 + 2) + 3");
 assertEquals(6.0, e.evaluate());
 }

 @Test
 public void testPrepare() {
 Expression e = new Expression("");
 e.prepare();
 assertEquals(new ExpressionStack(), e.getStack());
 e.setExpression("1 + 2 * 3");
 e.prepare();
 ExpressionStack stack = e.getStack();
 assertEquals(5, stack.size());
 Stack<String> operands = stack.getOperands();
 List<String> expectedOperands = Arrays.asList("1", "2", "*", "3", "+");
 assertEquals(expectedOperands, operands);
 }

 @Test
 public void testTokenize() {
 Expression e = new Expression("");
 List<String> tokens = new ArrayList<>();
 e.tokenize();
 assertTrue(tokens.isEmpty());
 e.setExpression("1 + 2 * 3");
 tokens.clear();
 e.tokenize();
 List<String> expectedTokens = Arrays.asList("1", "+", "2", "*", "3");
 assertEquals(expectedTokens, tokens);
 e.setExpression("(1 + 2) * 3");
 tokens.clear();
 e.tokenize();
 List<String> expectedTokens2 = Arrays.asList("(", "1", "+", "2", ")", "*", "3");
 assertEquals(expectedTokens2, tokens);
 }
}