import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameVarsTest {

 @Test
 public void testRenameVars() {
 // Given a simple Java Script code snippet
 String code = "function test() {" +
 "var a = 1, b = 2, c = function() {}, d = a + b; " +
 "return a + c();" +
 "}";

 // When compiling the code with RenameVars
 RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse(code, externs, null);
 renameVars.process(externs, root);

 // Then the variable names should be renamed
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 Node nameNodeA = traversal.getFirstChild(root, Node.SCOPED_VAR);
 Node nameNodeB = traversal.getFirstChild(root, Node.SCOPED_VAR, nameNodeA);
 Node nameNodeC = traversal.getFirstChild(root, Node.FUNCTION, null, Node.NAME);
 Node nameNodeD = traversal.getFirstChild(root, Node.BINARY_OP, null, Node.ADD,
 nameNodeA, nameNodeB);

 // Check that the variables a, b and the function c have been renamed
 String expectedRenamedNameA = "a$0";
 String expectedRenamedNameB = "b$1";
 String expectedRenamedNameC = "c$2";

 assertEquals(expectedRenamedNameA, nameNodeA.getString());
 assertEquals(expectedRenamedNameB, nameNodeB.getString());
 assertEquals(expectedRenamedNameC, nameNodeC.getString());

 // Check that the variable d has not been renamed since it's an expression and not a variable
 assertEquals("d", nameNodeD.getString());

 // Check that the renamed variables can be used in an expression
 Node functionNode = traversal.getFirstChild(root, Node.FUNCTION);
 Node expressionNode = traversal.getFirstChild(functionNode, Node.BLOCK);
 Node expressionA = traversal.getFirstChild(expressionNode, Node.BINARY_OP, null, Node.ADD,
 nameNodeA, nameNodeB);
 Node expressionC = traversal.getFirstChild(expressionNode, Node.NAME, null, nameNodeC);
 Node returnNode = traversal.getFirstChild(expressionNode, Node.RETURN);

 assertEquals(expectedRenamedNameA, nameNodeA.getParent().getName());
 assertEquals(expectedRenamedNameB, nameNodeB.getParent().getName());
 assertEquals(expectedRenamedNameC, nameNodeC.getParent().getName());

 // Check that the returned value is correct
 assertEquals(3, traversal.getNumberValue(returnNode));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenameVarsWithNullCompilerPass() {
 // Given a null CompilerPass
 RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);

 // When trying to process the code
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse("function test() {}", externs, null);
 renameVars.process(externs, root);
 }

 @Test(expected = NullPointerException.class)
 public void testRenameVarsWithNullNode() {
 // Given a null Node
 RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = null;
 renameVars.process(externs, root);
 }

 @Test
 public void testRenameVarsWithEmptyCode() {
 // Given an empty code snippet
 String code = "";

 // When compiling the code with RenameVars
 RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse(code, externs, null);
 renameVars.process(externs, root);

 // Then the code should remain empty
 assertNull(root.getFirstChild());
 }

 @Test
 public void testRenameVarsWithNoVariables() {
 // Given a code snippet without any variables
 String code = "function test() {" +
 "return 1 + 2;" +
 "}";

 // When compiling the code with RenameVars
 RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse(code, externs, null);
 renameVars.process(externs, root);

 // Then the code should remain unchanged
 Node nameNode = traversal.getFirstChild(root, Node.NAME);
 assertNull(nameNode);
 }

 @Test
 public void testRenameVarsWithLocalRenamingOnly() {
 // Given a code snippet with local variables only
 String code = "function test(a, b) {" +
 "var c = a + b;" +
 "var d = function() {}" +
 "return a + c();" +
 "}";

 // When compiling the code with RenameVars with localRenamingOnly set to true
 RenameVars renameVars = new RenameVars(null, "", true, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse(code, externs, null);
 renameVars.process(externs, root);

 // Then the local variables should be renamed
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 Node nameNodeA = traversal.getFirstChild(root, Node.FORMAL_PARAM);
 Node nameNodeB = traversal.getFirstChild(root, Node.FORMAL_PARAM, nameNodeA);
 Node nameNodeC = traversal.getFirstChild(root, Node.SCOPED_VAR);
 Node nameNodeD = traversal.getFirstChild(root, Node.FUNCTION, null, Node.NAME);
 Node nameNodeE = traversal.getFirstChild(root, Node.FUNCTION, null, Node.BLOCK, null,
 Node.VAR_DECL, null, Node.NAME, null, nameNodeD);
 Node nameNodeF = traversal.getFirstChild(root, Node.RETURN);

 // Check that the variables a, b, c and the function d have been renamed
 String expectedRenamedNameA = "a$0";
 String expectedRenamedNameB = "b$1";
 String expectedRenamedNameC = "c$2";
 String expectedRenamedNameD = "d$3";
 String expectedRenamedNameE = "e$4";

 assertEquals(expectedRenamedNameA, nameNodeA.getString());
 assertEquals(expectedRenamedNameB, nameNodeB.getString());
 assertEquals(expectedRenamedNameC, nameNodeC.getString());
 assertEquals(expectedRenamedNameD, nameNodeD.getString());
 assertEquals(expectedRenamedNameE, nameNodeE.getString());

 // Check that the renamed variables can be used in an expression
 assertEquals(expectedRenamedNameA, nameNodeA.getParent().getName());
 assertEquals(expectedRenamedNameB, nameNodeB.getParent().getName());
 assertEquals(expectedRenamedNameC, nameNodeC.getParent().getName());
 assertEquals(expectedRenamedNameD, nameNodeD.getParent().getName());

 // Check that the returned value is correct
 assertEquals(3, traversal.getNumberValue(nameNodeF));
 }

 @Test
 public void testRenameVarsWithPreserveAnonymousFunctionNames() {
 // Given a code snippet with anonymous functions
 String code = "function test() {" +
 "function anonymous() {}" +
 "return anonymous();" +
 "}";

 // When compiling the code with RenameVars with preserveAnonymousFunctionNames set to true
 RenameVars renameVars = new RenameVars(null, "", false, true, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse(code, externs, null);
 renameVars.process(externs, root);

 // Then the anonymous function should not be renamed
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 Node nameNodeAnonymous = traversal.getFirstChild(root, Node.FUNCTION, null, Node.NAME, null, Node.BLOCK);
 assertNotNull(nameNodeAnonymous);
 assertEquals("anonymous$0", nameNodeAnonymous.getString());

 // Check that the renamed variables can be used in an expression
 Node nameNodeFunction = traversal.getFirstChild(root, Node.FUNCTION);
 Node nameNodeReturn = traversal.getFirstChild(nameNodeFunction, Node.RETURN);
 assertEquals("anonymous$0", nameNodeAnonymous.getParent().getName());
 assertEquals("anonymous$0", nameNodeReturn.getParent().getName());

 // Check that the returned value is correct
 assertEquals(undefined, traversal.getUndefinedValue(nameNodeReturn));
 }

 @Test
 public void testRenameVarsWithGeneratePseudoNames() {
 // Given a code snippet with many variables
 String code = "function test() {" +
 "var a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;" +
 "return a + b + c + d + e + f + g + h + i + j + k + l + m + n + o + p + q + r + s + t + u + v + w + x + y + z;" +
 "}";

 // When compiling the code with RenameVars with generatePseudoNames set to true
 RenameVars renameVars = new RenameVars(null, "", false, false, true, null, null, null);
 Compiler compiler = new Compiler();
 compiler.init();
 Node externs = new Node(Token.PROGRAM, 0);
 Node root = NodeUtil.parse(code, externs, null);
 renameVars.process(externs, root);

 // Then the variables should be renamed with pseudo names
 NodeTraversal traversal = new NodeTraversal(compiler, root);
 List<Node> nameNodes = new ArrayList<>();
 for (int i = 0; i < 26; ++i) {
 nameNodes.add(traversal.getFirstChild(root, Node.SCOPED_VAR, null, Node.NAME));
 }

 for (int i = 0; i < 26; ++i) {
 String expectedRenamedName = "$" + (char) ('a' + i);
 assertEquals(expectedRenamedName, nameNodes.get(i).getString());
 }

 // Check that the renamed variables can be used in an expression
 Node nameNodeFunction = traversal.getFirstChild(root, Node.FUNCTION);
 Node nameNodeReturn = traversal.getFirstChild(nameNodeFunction, Node.RETURN);
 for (int i = 0; i < 26; ++i) {
 String expectedRenamedName = "$" + (char) ('a' + i);
 assertEquals(expectedRenamedName, nameNodes.get(i).getParent().getName());
 }

 // Check that the returned value is correct
 assertEquals(27 * 26, traversal.getNumberValue(nameNodeReturn));
 }
}