import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expr;
 private ExpressionStack stack;

 @Before
 public void setUp() {
 expr = new Expression("5 + 3 * (7 - 2) and (10 > 5) and (5 < 10)");
 stack = expr.getStackCopy();
 expr.prepare();
 }

 @Test
 public void testIsReservedFunction() {
 // Test for reserved functions
 assertEquals(true, expr.isReservedFunction("and"));
 assertEquals(true, expr.isReservedFunction("or"));
 assertEquals(true, expr.isReservedFunction("not"));
 assertEquals(true, expr.isReservedFunction("is"));
 assertEquals(true, expr.isReservedFunction("isnull"));
 // Test for math functions
 assertEquals(true, expr.isMathFunction("abs"));
 assertEquals(true, expr.isMathFunction("divide"));
 assertEquals(true, expr.isMathFunction("multiply"));
 assertEquals(true, expr.isMathFunction("plus"));
 assertEquals(true, expr.isMathFunction("sqrt"));
 assertEquals(true, expr.isMathFunction("subtract"));
 // Test for date functions
 assertEquals(true, expr.isDateFunction("dateadd"));
 assertEquals(true, expr.isDateFunction("datediff"));
 // Test for string functions
 assertEquals(true, expr.isStringFunction("length"));
 assertEquals(true, expr.isStringFunction("substring"));
 assertEquals(true, expr.isStringFunction("tolower"));
 assertEquals(true, expr.isStringFunction("toupper"));
 // Test for logical functions
 assertEquals(true, expr.isLogicalFunction("equals"));
 assertEquals(true, expr.isLogicalFunction("greaterthan"));
 assertEquals(true, expr.isLogicalFunction("lessthan"));
 assertEquals(true, expr.isLogicalFunction("greaterthanorequal"));
 assertEquals(true, expr.isLogicalFunction("lessthanorequal"));
 assertEquals(true, expr.isLogicalFunction("notequals"));
 }

 @Test
 public void testIsFunction() {
 // Test for custom functions
 Expression customFunc = new Expression("myFunction(5, 3)");
 customFunc.setExternalFunctions(Arrays.asList(new LogicalFunction("myFunction", new Object[0][], (args) -> (args[0] > args[1]))));
 assertEquals(true, customFunc.isFunction("myFunction"));
 }

 @Test
 public void testGetFunction() {
 // Test for getting functions
 assertEquals(LogicalFunction.class, expr.getFunction("and").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("or").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("not").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("equals").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("greaterthan").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("lessthan").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("greaterthanorequal").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("lessthanorequal").getClass());
 assertEquals(LogicalFunction.class, expr.getFunction("notequals").getClass());
 assertEquals(MathFunction.class, expr.getFunction("abs").getClass());
 assertEquals(MathFunction.class, expr.getFunction("divide").getClass());
 assertEquals(MathFunction.class, expr.getFunction("multiply").getClass());
 assertEquals(MathFunction.class, expr.getFunction("plus").getClass());
 assertEquals(MathFunction.class, expr.getFunction("sqrt").getClass());
 assertEquals(MathFunction.class, expr.getFunction("subtract").getClass());
 assertEquals(DateFunction.class, expr.getFunction("dateadd").getClass());
 assertEquals(DateFunction.class, expr.getFunction("datediff").getClass());
 assertEquals(StringFunction.class, expr.getFunction("length").getClass());
 assertEquals(StringFunction.class, expr.getFunction("substring").getClass());
 assertEquals(StringFunction.class, expr.getFunction("tolower").getClass());
 assertEquals(StringFunction.class, expr.getFunction("toupper").getClass());
 }

 @Test
 public void testIsLogicalFunction() {
 // Test for logical functions
 assertEquals(true, expr.isLogicalFunction("and"));
 assertEquals(true, expr.isLogicalFunction("or"));
 assertEquals(true, expr.isLogicalFunction("not"));
 assertEquals(true, expr.isLogicalFunction("equals"));
 assertEquals(true, expr.isLogicalFunction("greaterthan"));
 assertEquals(true, expr.isLogicalFunction("lessthan"));
 assertEquals(true, expr.isLogicalFunction("greaterthanorequal"));
 assertEquals(true, expr.isLogicalFunction("lessthanorequal"));
 assertEquals(true, expr.isLogicalFunction("notequals"));
 }

 @Test
 public void testIsMathFunction() {
 // Test for math functions
 assertEquals(true, expr.isMathFunction("abs"));
 assertEquals(true, expr.isMathFunction("divide"));
 assertEquals(true, expr.isMathFunction("multiply"));
 assertEquals(true, expr.isMathFunction("plus"));
 assertEquals(true, expr.isMathFunction("sqrt"));
 assertEquals(true, expr.isMathFunction("subtract"));
 }

 @Test
 public void testIsDateFunction() {
 // Test for date functions
 assertEquals(true, expr.isDateFunction("dateadd"));
 assertEquals(true, expr.isDateFunction("datediff"));
 }

 @Test
 public void testIsStringFunction() {
 // Test for string functions
 assertEquals(true, expr.isStringFunction("length"));
 assertEquals(true, expr.isStringFunction("substring"));
 assertEquals(true, expr.isStringFunction("tolower"));
 assertEquals(true, expr.isStringFunction("toupper"));
 }

 @Test
 public void testIsFunctionWithExternalFunctions() {
 // Test for functions with external functions
 Expression customFuncExpr = new Expression("myFunction(5, 3)");
 customFuncExpr.setExternalFunctions(Arrays.asList(new LogicalFunction("myFunction", new Object[0][], (args) -> (args[0] > args[1]))));
 assertEquals(true, customFuncExpr.isFunction("myFunction"));
 }

 @Test
 public void testGetExternalFunction() {
 // Test for getting external functions
 FunctionExpression customFunc = new LogicalFunction("myFunction", new Object[0][], (args) -> (args[0] > args[1]));
 Expression customFuncExpr = new Expression("myFunction(5, 3)");
 customFuncExpr.setExternalFunctions(Arrays.asList(customFunc));
 assertEquals(customFunc, customFuncExpr.getExternalFunction("myFunction"));
 }

 @Test
 public void testIsExternalFunction() {
 // Test for external functions
 Expression customFuncExpr = new Expression("myFunction(5, 3)");
 customFuncExpr.setExternalFunctions(Arrays.asList(new LogicalFunction("myFunction", new Object[0][], (args) -> (args[0] > args[1]))));
 assertEquals(true, customFuncExpr.isExternalFunction("myFunction"));
 }

 @Test(expected = Exception.class)
 public void testEvaluateWithInvalidExpression() {
 Expression invalidExpr = new Expression("5 + 3 * (7 - 2) and (10 > 5) and (5 < 10) x");
 stack = invalidExpr.getStackCopy();
 invalidExpr.prepare();
 invalidExpr.evaluate();
 }
}