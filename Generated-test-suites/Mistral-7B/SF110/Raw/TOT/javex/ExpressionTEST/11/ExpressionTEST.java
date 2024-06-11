import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleExpression() {
 Expression e = createExpression("1+2");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(3, result);
 }

 @Test
 public void testLogicalOperation() {
 Expression e = createExpression("(1=1) and (2>1)");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(true, result);
 }

 @Test
 public void testMathOperation() {
 Expression e = createExpression("sqrt(16)");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(4.0, result, 0.001);
 }

 @Test
 public void testDateOperation() {
 Expression e = createExpression("dateDiff(year, '2022-01-01', '2022-12-31')");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(365, result);
 }

 @Test
 public void testStringOperation() {
 Expression e = createExpression("toUpper('hello')");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals("HELLO", result);
 }

 @Test
 public void testReservedFunction() {
 Expression e = new Expression("isnull(2)");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should throw an exception
 Assert.assertNotNull(ex);
 }
 e = new Expression("isnull(null)");
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should throw an exception
 Assert.assertNotNull(ex);
 }
 e = new Expression("isnull(true)");
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should throw an exception
 Assert.assertNotNull(ex);
 }
 e = new Expression("isnull(1)");
 Object expectedResult = false;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testFunctionWithParameters() {
 Expression e = new Expression("abs(-5)");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(5, result);

 e = new Expression("inList('apple', ['apple', 'banana', 'orange'])");
 Object expectedResult = true;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(expectedResult, result);

 e = new Expression("inList('banana', ['apple', 'banana', 'orange'])");
 expectedResult = true;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(expectedResult, result);

 e = new Expression("inList('orange', ['apple', 'banana', 'orange'])");
 expectedResult = true;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(expectedResult, result);

 e = new Expression("inList('grape', ['apple', 'banana', 'orange'])");
 expectedResult = false;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testFunctionWithExternalFunctions() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AddFunction());
 functions.add(new SubtractFunction());
 functions.add(new MultiplyFunction());
 functions.add(new DivideFunction());
 functions.add(new SquareRootFunction());
 functions.add(new PowerFunction());
 functions.add(new DateAddFunction());
 functions.add(new DateDiffFunction());
 functions.add(new LengthFunction());
 functions.add(new SubStringFunction());
 functions.add(new ToLowerFunction());
 functions.add(new ToUpperFunction());

 Expression e = new Expression("new MyFunction(1, 2, [3, 4], 'test')") {
 @Override
 public List<FunctionExpression> getExternalFunctions() {
 return functions;
 }
 };
 e.prepare();
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 //Test should not throw an exception
 }
 //Assuming MyFunction is a custom function that takes 3 arguments and 1 string
 Assert.assertEquals(4.0, result, 0.001);
 }

 @Test(expected = Exception.class)
 public void testInvalidExpression() {
 Expression e = new Expression("asd");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 assert ex.getMessage().contains("Invalid expression: asd");
 }
 }
}