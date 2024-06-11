import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserTestingUtility;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

 private static Node createNode(String code) {
 NodeFactory factory = ParserTestingUtility.createNodeFactory();
 Node root = factory.parse(code);
 return root;
 }

 @Test
 public void testNoSideEffectWarning() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("var x = 1;");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());

 root = createNode("var x = 1;\n" +
     "// some comment\n" +
     "x = foo();");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: This code lacks side-effects. Is there a bug?",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());
 }

 @Test
 public void testNoWarningForFunctionCall() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("foo();");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testNoWarningForVoidStatement() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode(";");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testNoWarningForExpressionResult() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("(1 + 2)");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testNoWarningForBlockWithReturnStatement() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("{return 1;}");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testNoWarningForSimpleOperator() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("var x = 1 + 2;");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testNoWarningForSideEffectFreeStatementsProtectedByChecker() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("JSCOMPILER_PRESERVE x = 1;");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testWarningForUselessExpression() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("var x = 0 + 1 * 1;");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: The result of the '+' operator is not being used.",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());
 }

 @Test
 public void testNoWarningForStringLiteral() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("var s = 'this string is continued on the next line but you forgot the +'");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testWarningForAssignmentToFunctionCall() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("var x = foo();");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: Suspicious code. Assignment to function call",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());
 }

 @Test
 public void testWarningForEmptyStatement() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode(";");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: Suspicious code. Empty statement",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());
 }

 @Test
 public void testNoWarningForStatementsInsideForLoop() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("for (var i = 0; i < 10; i++) { x = i; }");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testNoWarningForSideEffectCode() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("document.write('Hello World');");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: Suspicious code. Side effect",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());
 }

 @Test
 public void testNoWarningForSideEffectCodeProtectedByChecker() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("JSCOMPILER_PRESERVE document.write('Hello World');");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testWarningForSimpleOperatorWithSideEffect() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

 Node root = createNode("var x = Math.random();");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: The result of the 'Math.random()' operator is not being used.",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());
 }

 @Test
 public void testWarningForSideEffectFreeCodeAfterOptimization() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setSourceFile("test.js");
 options.setCheckSideEffects(CheckLevel.WARNING);
 CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
 CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);

 Node root = createNode("JSCOMPILER_PRESERVE var x = Math.random();");
 NodeTraversal.traverse(compiler, root, checker);
 assertEquals(1, checker.problemNodes.size());
 assertEquals("JSC_USELESS_CODE: The result of the 'Math.random()' operator is not being used.",
     checker.problemNodes.get(0).getFirstChild().getError().getMessage());

 NodeTraversal.traverse(compiler, root, stripper);
 assertEquals(0, checker.problemNodes.size());
 }
}