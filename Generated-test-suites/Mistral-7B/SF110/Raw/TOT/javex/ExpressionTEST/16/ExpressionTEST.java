import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.utils.JavexUtils;
import org.javex.ExpressionStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression expression;
 private List<FunctionExpression> externalFunctions;

 @Before
 public void setup() {
 expression = new Expression("(5 > 3) and (2 + 3 = 5)");
 externalFunctions = new ArrayList<>();
 externalFunctions.add(new AndFunction());
 externalFunctions.add(new AddFunction());
 externalFunctions.add(new GreaterThanFunction());
 }

 @Test
 public void testSimpleLogicalExpression() {
 expression.setExternalFunctions(externalFunctions);
 expression.prepare();
 Assert.assertTrue(expression.evaluate());
 }

 @Test(expected = Exception.class)
 public void testInvalidLogicalExpression() {
 expression = new Expression("(5 > 3) and (2 + 3 = 5) (1 + 1 = 3)");
 expression.setExternalFunctions(externalFunctions);
 expression.prepare();
 }

 @Test
 public void testLogicalAndFunction() {
 expression = new Expression("and(true, false)");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new AndFunction());
 }});
 expression.prepare();
 Assert.assertFalse(expression.evaluate());
 }

 @Test
 public void testLogicalOrFunction() {
 expression = new Expression("or(true, false)");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new OrFunction());
 }});
 expression.prepare();
 Assert.assertTrue(expression.evaluate());
 }

 @Test
 public void testMathAddFunction() {
 expression = new Expression("2 + 3");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new AddFunction());
 }});
 expression.prepare();
 Assert.assertEquals(5.0, expression.evaluate(), 0.001);
 }

 @Test
 public void testMathSubtractFunction() {
 expression = new Expression("5 - 2");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new SubtractFunction());
 }});
 expression.prepare();
 Assert.assertEquals(3.0, expression.evaluate(), 0.001);
 }

 @Test
 public void testMathMultiplyFunction() {
 expression = new Expression("2 * 3");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new MultiplyFunction());
 }});
 expression.prepare();
 Assert.assertEquals(6.0, expression.evaluate(), 0.001);
 }

 @Test
 public void testMathDivideFunction() {
 expression = new Expression("10 / 3");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new DivideFunction());
 }});
 expression.prepare();
 Assert.assertEquals(3.3333333333333335, expression.evaluate(), 0.001);
 }

 @Test
 public void testStringLengthFunction() {
 expression = new Expression("length(\"Hello World\")");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new LengthFunction());
 }});
 expression.prepare();
 Assert.assertEquals(11, expression.evaluate());
 }

 @Test
 public void testDateAddFunction() {
 expression = new Expression("dateAdd(new Date(2022, 0, 1), 3, \"DAY\")");
 expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
 add(new DateAddFunction());
 }});
 expression.prepare();
 Assert.assertEquals(new Date(2022, 0, 4), expression.evaluate());
 }
}