import org.junit.Before;
import org.junit.Test;
import org.javex.Expression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.javex.functions.StringFunction;
import org.javex.utils.JavexUtils;
import org.javex.functions.FunctionExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression expression;
 private LogicalFunction andFunction;
 private LogicalFunction orFunction;
 private MathFunction addFunction;
 private MathFunction subtractFunction;
 private DateFunction dateAddFunction;
 private DateFunction dateDiffFunction;
 private StringFunction lengthFunction;
 private StringFunction substrFunction;
 private StringFunction toUpperFunction;
 private StringFunction toLowerFunction;
 private FunctionExpression nullFunction;

 @Before
 public void setUp() {
 expression = new Expression("(1 + 2) * (3 and 4) and (not (5 > 6)) or (contains(substr(toUpper(\"Test\"), 1, 3), \"ABC\") and (dateAdd(new Date(2022, 11, 30), interval '1 day') = dateDiff(new Date(2022, 11, 31), new Date(2022, 11, 30), 'day')))");
 andFunction = new AndFunction();
 orFunction = new OrFunction();
 addFunction = new AddFunction();
 subtractFunction = new SubtractFunction();
 dateAddFunction = new DateAddFunction();
 dateDiffFunction = new DateDiffFunction();
 lengthFunction = new LengthFunction();
 substrFunction = new SubStringFunction();
 toUpperFunction = new ToUpperFunction();
 toLowerFunction = new ToLowerFunction();
 nullFunction = new GenericFunctionExpression("null");

 expression.prepare();
 }

 @Test
 public void testEvaluateSimpleAddition() {
 final Object result = expression.evaluate();
 final Object expected = addFunction.evaluate("3", "5");
 assertTrue(result.equals(expected));
 }

 @Test
 public void testEvaluateSimpleMultiplication() {
 final Object result = expression.evaluate();
 final Object expected = addFunction.evaluate(addFunction.evaluate("3", "2"), "2");
 assertTrue(result.equals(expected));
 }

 @Test
 public void testEvaluateSimpleAndOperation() {
 final Object result = expression.evaluate();
 final Object expected = andFunction.evaluate(true, true);
 assertTrue(result.equals(expected));
 }

 @Test
 public void testEvaluateSimpleOrOperation() {
 final Object result = expression.evaluate();
 final Object expected = orFunction.evaluate(true, false);
 assertTrue(result.equals(expected));
 }

 @Test
 public void testEvaluateSimpleNotOperation() {
 final Object result = expression.evaluate();
 final Object expected = new Boolean(false);
 expression.stack.push("5");
 expression.stack.push(">");
 expression.stack.push("6");
 expression.prepare();
 assertTrue(result.equals(expected));
 }

 @Test
 public void testEvaluateContainsFunction() {
 final Object result = expression.evaluate();
 final Object expected = toUpperFunction.evaluate("TEST").toString().contains("ABC") ? 1 : 0;
 assertTrue(result.equals(expected));
 }

 @Test
 public void testEvaluateDateFunctions() {
 expression.stack.clear();
 expression.stack.push(new Date(2022, 11, 30).toString());
 expression.stack.push("interval");
 expression.stack.push("'1 day'");
 expression.stack.push("dateAdd");
 expression.prepare();
 final Object result1 = expression.evaluate();
 final Object expected1 = new Date(2022, 11, 31).getTime();
 expression.stack.clear();
 expression.stack.push(new Date(2022, 11, 31).toString());
 expression.stack.push("new Date");
 expression.stack.push("(2022, 11, 30)");
 expression.stack.push("dateDiff");
 expression.prepare();
 final Object result2 = expression.evaluate();
 final Object expected2 = dateDiffFunction.evaluate(expected1, new Date(2022, 11, 30).getTime(), "day");
 assertTrue(result1.equals(expected1 + 86400000L) && result2.equals(expected2));
 }

 @Test
 public void testEvaluateStringFunctions() {
 final Object result = expression.evaluate();
 final Object expected = lengthFunction.evaluate("Test").equals("Test") ? lengthFunction.evaluate("Test").intValue() : 0;
 assertTrue(result.equals(expected));
 }

 @Test(expected = Exception.class)
 public void testEvaluateNullFunction() {
 expression.stack.clear();
 expression.stack.push(nullFunction.getSimbol());
 expression.prepare();
 expression.evaluate();
 }

 @Test(expected = Exception.class)
 public void testEvaluateWrongFunction() {
 expression.stack.clear();
 expression.stack.push("unknownFunction");
 expression.prepare();
 expression.evaluate();
 }
}