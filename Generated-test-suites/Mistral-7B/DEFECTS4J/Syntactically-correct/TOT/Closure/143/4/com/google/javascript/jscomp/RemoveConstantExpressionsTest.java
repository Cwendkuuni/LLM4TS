import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test.AbstractCompilerTestCase;

public class RemoveConstantExpressionsTest extends AbstractCompilerTestCase {

 private Compiler compiler;
 private RemoveConstantExpressions removeConstantExpressions;

 @Before
 public void setUp() throws Exception {
 compiler = newCompiler();
 removeConstantExpressions = new RemoveConstantExpressions(compiler);
 }

 @Test
 public void testSimpleConstantExpression() {
 String input = "1 + 2 + 3";
 String expectedOutput = "1 + 2 + 3";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for simple constant expression"));
 }

 @Test
 public void testConstantExpressionWithSideEffect() {
 String input = "1 + foo() + bar()";
 String expectedOutput = "foo();bar()";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with side effect"));
 }

 @Test
 public void testConstantExpressionWithMultipleSideEffects() {
 String input = "1 + foo() + bar() + baz()";
 String expectedOutput = "foo();bar();baz()";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with multiple side effects"));
 }

 @Test
 public void testConstantExpressionWithParentSideEffect() {
 String input = "if (true) {1 + foo() + bar()};";
 String expectedOutput = "if (true) {foo();bar();}";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with parent side effect"));
 }

 @Test
 public void testComplexConstantExpression() {
 String input = "a = 1 + 2 + 3 * 4 + 5;";
 String expectedOutput = "a = 3 * 4 + 5;";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for complex constant expression"));
 }

 @Test
 public void testConstantExpressionWithConditionSideEffect() {
 String input = "if (condition) {1 + foo() + bar()} else {1 + baz() + qux()};";
 String expectedOutput = "if (condition) {foo();bar()} else {baz();qux()}";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with condition side effect"));
 }

 @Test
 public void testConstantExpressionWithLoopSideEffect() {
 String input = "for (int i = 0; i < 10; i++) {1 + i};";
 String expectedOutput = "for (int i = 0; i < 10; i++) {}";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with loop side effect"));
 }

 @Test
 public void testConstantExpressionWithFunctionCallSideEffect() {
 String input = "foo(1 + 2 + 3);";
 String expectedOutput = "foo(1 + 2);foo(3);";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with function call side effect"));
 }

 @Test
 public void testConstantExpressionWithAssignmentSideEffect() {
 String input = "x = 1 + 2 + 3;";
 String expectedOutput = "x = 1 + 2;x = 3;";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with assignment side effect"));
 }

 @Test
 public void testConstantExpressionWithNegationSideEffect() {
 String input = "! (1 + 2 + 3);";
 String expectedOutput = "!(1 + 2);!(1 + 3);";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with negation side effect"));
 }

 @Test
 public void testConstantExpressionWithBitwiseOperatorsSideEffect() {
 String input = "1 | 2 | 3 << 1 | 4 << 1 | 5";
 String expectedOutput = "1 | 2 | 3 << 1 | 4 << 1";
 Node node = parseTree(input);
 Node result = removeConstantExpressions.process(null, node);
 assertTrue(compareTrees(node, result, "Test for constant expression with bitwise operators side effect"));
 }
}