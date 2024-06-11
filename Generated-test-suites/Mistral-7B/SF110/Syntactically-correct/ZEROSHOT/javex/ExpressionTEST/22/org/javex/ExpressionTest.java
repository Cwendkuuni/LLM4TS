package org.javex.expression;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javex.Expression;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Test;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression expr = new Expression(expression);
 expr.prepare();
 return expr;
 }

 @Test
 public void testPrepare() throws Exception {
 // Test empty expression
 Expression expr = new Expression("");
 expr.prepare();
 List<Object> stack = expr.getStackCopy();
 assertEquals(0, stack.size());

 // Test simple expression
 expr = createExpression("1 + 2");
 stack = expr.getStackCopy();
 assertEquals(1, stack.size());
 assertEquals(3.0, stack.get(0));

 // Test expression with functions
 expr = createExpression("Abs(1) + Sqrt(2) * 3");
 stack = expr.getStackCopy();
 assertEquals(1, stack.size());
 assertEquals(7.0, stack.get(0));

 // Test expression with logical functions
 expr = createExpression("(1 = 1) and (2 > 1)");
 stack = expr.getStackCopy();
 assertEquals(1, stack.size());
 assertEquals(true, stack.get(0));

 // Test expression with date functions
 expr = createExpression("DateDiff(day, DateAdd(day, 1, DateDiff(day, CurrentDate(), 0)), DateAdd(day, 2, CurrentDate()))");
 stack = expr.getStackCopy();
 assertEquals(1, stack.size());
 assertEquals(3L, stack.get(0));

 // Test expression with string functions
 expr = createExpression("ToUpper(\"Hello World\")");
 stack = expr.getStackCopy();
 assertEquals(1, stack.size());
 assertEquals("HELLO WORLD", stack.get(0));
 }

 @Test
 public void testGetFunction() {
 // Test reserved functions
 FunctionExpression function = Expression.getReservedFunctions("abs");
 assertEquals(AbsFunction.class, function.getClass());
 function = Expression.getReservedFunctions("and");
 assertEquals(AndFunction.class, function.getClass());
 function = Expression.getReservedFunctions("contains");
 assertEquals(ContainsFunction.class, function.getClass());
 function = Expression.getReservedFunctions("between");
 assertEquals(null, function);

 // Test external functions
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AddFunction());
 functions.add(new MultiplyFunction());
 Expression expr = new Expression("") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 expr.prepare();
 function = expr.getFunction("add");
 assertEquals(AddFunction.class, function.getClass());
 function = expr.getFunction("multiply");
 assertEquals(MultiplyFunction.class, function.getClass());
 function = expr.getFunction("length"); // should return null
 assertEquals(null, function);
 }

 @Test
 public void testIsFunction() {
 // Test reserved functions
 assertTrue(Expression.getInstance().isFunction("abs"));
 assertTrue(Expression.getInstance().isFunction("and"));
 assertTrue(Expression.getInstance().isFunction("contains"));
 assertFalse(Expression.getInstance().isFunction("between"));

 // Test external functions
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AddFunction());
 functions.add(new MultiplyFunction());
 Expression expr = new Expression("") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 expr.prepare();
 assertTrue(expr.isFunction("add"));
 assertTrue(expr.isFunction("multiply"));
 assertFalse(expr.isFunction("length"));
 }

 @Test
 public void testIsLogicalFunction() {
 // Test reserved functions
 assertTrue(Expression.getInstance().isLogicalFunction("and"));
 assertTrue(Expression.getInstance().isLogicalFunction("or"));
 assertFalse(Expression.getInstance().isLogicalFunction("abs"));

 // Test external functions
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AndLogicalFunction());
 functions.add(new OrLogicalFunction());
 Expression expr = new Expression("") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 expr.prepare();
 assertTrue(expr.isLogicalFunction("and"));
 assertTrue(expr.isLogicalFunction("or"));
 assertFalse(expr.isLogicalFunction("abs"));
 }

 @Test
 public void testIsMathFunction() {
 // Test reserved functions
 assertFalse(Expression.getInstance().isMathFunction("and"));
 assertFalse(Expression.getInstance().isMathFunction("or"));
 assertTrue(Expression.getInstance().isMathFunction("abs"));

 // Test external functions
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AddFunction());
 functions.add(new MultiplyFunction());
 Expression expr = new Expression("") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 expr.prepare();
 assertFalse(expr.isMathFunction("and"));
 assertFalse(expr.isMathFunction("or"));
 assertTrue(expr.isMathFunction("add"));
 assertTrue(expr.isMathFunction("multiply"));
 }

 @Test
 public void testIsDateFunction() {
 // Test reserved functions
 assertFalse(Expression.getInstance().isDateFunction("and"));
 assertFalse(Expression.getInstance().isDateFunction("or"));
 assertTrue(Expression.getInstance().isDateFunction("dateadd"));

 // Test external functions
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new DateAddFunction());
 Expression expr = new Expression("") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 expr.prepare();
 assertFalse(expr.isDateFunction("and"));
 assertFalse(expr.isDateFunction("or"));
 assertTrue(expr.isDateFunction("dateadd"));
 }

 @Test
 public void testIsStringFunction() {
 // Test reserved functions
 assertFalse(Expression.getInstance().isStringFunction("and"));
 assertFalse(Expression.getInstance().isStringFunction("or"));
 assertTrue(Expression.getInstance().isStringFunction("tolower"));

 // Test external functions
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new ToLowerFunction());
 Expression expr = new Expression("") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 expr.prepare();
 assertFalse(expr.isStringFunction("and"));
 assertFalse(expr.isStringFunction("or"));
 assertTrue(expr.isStringFunction("tolower"));
 }

 @Test
 public void testEvaluate() throws Exception {
 // Test simple expression
 Expression expr = createExpression("1 + 2");
 assertEquals(3.0, expr.evaluate());

 // Test expression with functions
 expr = createExpression("Abs(1) + Sqrt(2) * 3");
 assertEquals(7.0, expr.evaluate());

 // Test expression with logical functions
 expr = createExpression("(1 = 1) and (2 > 1)");
 assertEquals(true, expr.evaluate());

 // Test expression with date functions
 Map<String, Object> dateMap = new HashMap<>();
 dateMap.put("CurrentDate", new java.util.Date());
 expr = createExpression("DateDiff(day, DateAdd(day, 1, DateDiff(day, CurrentDate(), 0)), DateAdd(day, 2, CurrentDate()))");
 assertEquals(3L, expr.evaluate(dateMap));

 // Test expression with string functions
 expr = createExpression("ToUpper(\"Hello World\")");
 assertEquals("HELLO WORLD", expr.evaluate());
 }

 @Test
 public void testIsFunctionWithInvalidSymbol() {
 assertFalse(Expression.getInstance().isFunction("ab"));
 }
}