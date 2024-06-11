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

 private Expression createExpression(String expr) {
 Expression e = new Expression(expr);
 e.prepare();
 return e;
 }

 @Test
 public void testIsReservedFunction() {
 Expression e = createExpression("and(1, 2)");
 assertTrue(e.isReservedFunction("and"));
 }

 @Test
 public void testIsReservedFunctionWithInvalidSymbol() {
 Expression e = createExpression("and(1, 2)");
 assertFalse(e.isReservedFunction("z"));
 }

 @Test
 public void testIsLogicalFunction() {
 Expression e = createExpression("and(1, 2)");
 AndFunction andFunction = new AndFunction();
 assertTrue(e.isLogicalFunction(andFunction.getSimbol()));
 }

 @Test
 public void testIsLogicalFunctionWithInvalidSymbol() {
 Expression e = createExpression("and(1, 2)");
 assertFalse(e.isLogicalFunction("z"));
 }

 @Test
 public void testIsMathFunction() {
 Expression e = createExpression("abs(1)");
 Assert.assertEquals(FunctionExpression.MATH, e.getFunction("abs").getType());
 }

 @Test
 public void testIsMathFunctionWithInvalidSymbol() {
 Expression e = createExpression("abs(1)");
 assertFalse(e.isMathFunction("z"));
 }

 @Test
 public void testIsDateFunction() {
 Expression e = createExpression("dateAdd(now(), interval '1 day')");
 DateAddFunction dateAddFunction = new DateAddFunction();
 assertTrue(e.isDateFunction(dateAddFunction.getSimbol()));
 }

 @Test
 public void testIsDateFunctionWithInvalidSymbol() {
 Expression e = createExpression("dateAdd(now(), interval '1 day')");
 assertFalse(e.isDateFunction("z"));
 }

 @Test
 public void testIsStringFunction() {
 Expression e = createExpression("upper('Hello World')");
 ToUpperFunction toUpperFunction = new ToUpperFunction();
 assertTrue(e.isStringFunction(toUpperFunction.getSimbol()));
 }

 @Test
 public void testIsStringFunctionWithInvalidSymbol() {
 Expression e = createExpression("upper('Hello World')");
 assertFalse(e.isStringFunction("z"));
 }

 @Test
 public void testIsFunction() {
 Expression e = createExpression("and(1, 2)");
 assertTrue(e.isFunction("and"));
 }

 @Test
 public void testIsFunctionWithInvalidSymbol() {
 Expression e = createExpression("and(1, 2)");
 assertFalse(e.isFunction("z"));
 }

 @Test
 public void testGetFunction() {
 Expression e = createExpression("and(1, 2)");
 AndFunction andFunction = new AndFunction();
 FunctionExpression function = e.getFunction("and");
 assertEquals(andFunction, function);
 }

 @Test
 public void testGetFunctionWithInvalidSymbol() {
 Expression e = createExpression("and(1, 2)");
 FunctionExpression function = e.getFunction("z");
 assertNull(function);
 }

 @Test
 public void testIsExternalFunction() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new MyFunction("myFunction"));
 Expression e = new Expression("myFunction(1, 2)");
 e.setExternalFunctions(functions);
 e.prepare();
 assertTrue(e.isExternalFunction("myFunction"));
 }

 @Test
 public void testIsExternalFunctionWithInvalidSymbol() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new MyFunction("myFunction"));
 Expression e = new Expression("myFunction(1, 2)");
 e.setExternalFunctions(functions);
 e.prepare();
 assertFalse(e.isExternalFunction("z"));
 }

 @Test
 public void testGetExternalFunction() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new MyFunction("myFunction"));
 Expression e = new Expression("myFunction(1, 2)");
 e.setExternalFunctions(functions);
 e.prepare();
 FunctionExpression function = e.getExternalFunction("myFunction");
 assertEquals(functions.get(0), function);
 }

 @Test
 public void testGetExternalFunctionWithInvalidSymbol() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new MyFunction("myFunction"));
 Expression e = new Expression("myFunction(1, 2)");
 e.setExternalFunctions(functions);
 e.prepare();
 assertNull(e.getExternalFunction("z"));
 }

 @Test
 public void testTokenize() {
 Expression e = new Expression("1 + 2 * (3 - 4) / 5");
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("+");
 expectedStack.push("2");
 expectedStack.push("*");
 expectedStack.push("(");
 expectedStack.push("3");
 expectedStack.push("-");
 expectedStack.push("4");
 expectedStack.push(")");
 expectedStack.push("/");
 expectedStack.push("5");
 Stack<String> stack = e.tokenize();
 for (int i = 0; i < stack.size(); i++) {
 String expected = (i < expectedStack.size()) ? expectedStack.get(i) : "";
 String actual = stack.get(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testTokenizeWithQuotes() {
 Expression e = new Expression("1 + 2 * (3 - \"4\") / 5");
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("+");
 expectedStack.push("2");
 expectedStack.push("*");
 expectedStack.push("(");
 expectedStack.push("3");
 expectedStack.push("-");
 expectedStack.push("4");
 expectedStack.push("\"");
 expectedStack.push("4");
 expectedStack.push("\"");
 expectedStack.push(")");
 expectedStack.push("/");
 expectedStack.push("5");
 Stack<String> stack = e.tokenize();
 for (int i = 0; i < stack.size(); i++) {
 String expected = (i < expectedStack.size()) ? expectedStack.get(i) : "";
 String actual = stack.get(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testTokenizeWithSpaces() {
 Expression e = new Expression("1 + 2 * ( 3 - 4 ) / 5");
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("+");
 expectedStack.push("2");
 expectedStack.push("*");
 expectedStack.push("(");
 expectedStack.push("3");
 expectedStack.push("-");
 expectedStack.push("4");
 expectedStack.push(")");
 expectedStack.push("/");
 expectedStack.push("5");
 Stack<String> stack = e.tokenize();
 for (int i = 0; i < stack.size(); i++) {
 String expected = (i < expectedStack.size()) ? expectedStack.get(i) : "";
 String actual = stack.get(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testTokenizeWithReservedCharacters() {
 Expression e = new Expression("1 + 2 * ( 3 - and(4, 5) ) / 5");
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("+");
 expectedStack.push("2");
 expectedStack.push("*");
 expectedStack.push("(");
 expectedStack.push("3");
 expectedStack.push("-");
 expectedStack.push("and");
 expectedStack.push("(");
 expectedStack.push("4");
 expectedStack.push(",");
 expectedStack.push("5");
 expectedStack.push(")");
 expectedStack.push(")");
 expectedStack.push("/");
 expectedStack.push("5");
 Stack<String> stack = e.tokenize();
 for (int i = 0; i < stack.size(); i++) {
 String expected = (i < expectedStack.size()) ? expectedStack.get(i) : "";
 String actual = stack.get(i);
 assertEquals(expected, actual);
 }
 }

}

class MyFunction extends FunctionExpression {
 String simbol;

 public MyFunction(String symbol) {
 super(FunctionExpression.CUSTOM, symbol);
 this.simbol = symbol;
 }

 @Override
 public String toString() {
 return simbol;
 }
}