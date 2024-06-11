import static com.google.javascript.jscomp.NodeUtil.assertNodesEqual;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

 private Compiler compiler;
 private SourceFile sourceFile;

 @Before
 public void setUp() {
 compiler = new Compiler();
 sourceFile = new SourceFile("test.js", "test");
 compiler.setOptions(new CompilerOptions());
 }

 @Test
 public void testCollapseSimpleVariableDeclarations() {
 Node root = createNode(
 "var a;\n" +
 "var b = 1;\n" +
 "var c = 2;\n");
 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(sourceFile.getExterns(), root);
 Node expected = createNode(
 "var a, b = 1, c = 2;\n");
 assertNodesEqual(expected, root);
 }

 @Test
 public void testCollapseVariableAssignments() {
 Node root = createNode(
 "var a = 1;\n" +
 "var a = 2;\n");
 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(sourceFile.getExterns(), root);
 Node expected = createNode(
 "var a = 2;\n");
 assertNodesEqual(expected, root);
 }

 @Test
 public void testCollapseVariableDeclarationsAndAssignments() {
 Node root = createNode(
 "var a = 1;\n" +
 "var b;\n" +
 "b = 3;\n" +
 "var c = 4;\n");
 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(sourceFile.getExterns(), root);
 Node expected = createNode(
 "var a = 1, b = 3, c = 4;\n");
 assertNodesEqual(expected, root);
 }

 @Test
 public void testCollapseMultipleVariableDeclarationsInFunction() {
 Node root = createNode(
 "function foo() {\n" +
 "  var a;\n" +
 "  var b = 1;\n" +
 "  var c = 2;\n" +
 "  var d = 3;\n" +
 "}\n");
 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(sourceFile.getExterns(), root);
 Node expected = createNode(
 "function foo() {\n" +
 "  var a, b = 1, c = 2, d = 3;\n" +
 "}\n");
 assertNodesEqual(expected, root);
 }

 @Test
 public void testCollapseAndReuseSameVariable() {
 Node root = createNode(
 "function foo() {\n" +
 "  var a = 1;\n" +
 "  var b = a;\n" +
 "  var a = 2;\n" +
 "}\n");
 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 collapseVariableDeclarations.process(sourceFile.getExterns(), root);
 Node expected = createNode(
 "function foo() {\n" +
 "  var a = 1;\n" +
 "  var b = a;\n" +
 "  a = 2;\n" +
 "}\n");
 assertNodesEqual(expected, root);
 }

 @Test
 public void testCollapseThrowsErrorOnInvalidCollapse() {
 Node root = createNode(
 "function foo() {\n" +
 "  var a = 1;\n" +
 "  var b = a + 1;\n" +
 "  var a = 3;\n" +
 "}\n");
 CollapseVariableDeclarations collapseVariableDeclarations =
 new CollapseVariableDeclarations(compiler);
 try {
 collapseVariableDeclarations.process(sourceFile.getExterns(), root);
 fail("Expected an error to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 private Node createNode(String code) {
 Node root = new Node(Token.PROGRAM);
 Node externs = new Node(Token.PROGRAM);
 Node script = new Node(Token.SCRIPT, new Node(Token.TEXT, code), null);
 externs.addChildToBack(script);
 root.addChildToBack(externs);
 return root;
}
}