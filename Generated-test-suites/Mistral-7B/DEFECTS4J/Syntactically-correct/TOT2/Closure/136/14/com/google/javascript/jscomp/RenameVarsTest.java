import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;

public class RenameVarsTest {
  @Test
  public void testRenameVars() {
    // Given: A simple Java script with some variables and functions
    String source = "function foo(a, b) {" +
                    "  var c = 1;" +
                    "  var d = 2;" +
                    "  function e() {" +
                    "    var f = 3;" +
                    "  }" +
                    "  return a + b;" +
                    "}";

    // When: Applying RenameVars to the script
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass renamePass = new RenameVars(compiler, "", false, false, false, null, null, null);
    Node externs = NodeFactory.createRootNode(source);
    Node root = NodeFactory.createRootNode(NodeFactory.createRootNode(NodeFactory.createProgramNode()));
    renamePass.process(externs, root);

    // Then: The renamed script should have shorter variable names
    String expectedOutput = "function foo(a, b) {" +
                            "  var aa = 1;" +
                            "  var bb = 2;" +
                            "  function e() {" +
                            "    var cc = 3;" +
                            "  }" +
                            "  return a + b;" +
                            "}";

    // Verify: The output of RenameVars is as expected
    assertEquals(expectedOutput, compiler.getSource().getCode());

    // Edge case: Test renaming of global variables when localRenamingOnly is false
    source = "var a = 1; function foo() { var b = 2; }";
    compiler.reset();
    renamePass = new RenameVars(compiler, "", false, false, false, null, null, null);
    Node externsEdge = NodeFactory.createRootNode(source);
    Node rootEdge = NodeFactory.createRootNode(NodeFactory.createRootNode(NodeFactory.createProgramNode()));
    renamePass.process(externsEdge, rootEdge);
    String actualOutputEdge = compiler.getSource().getCode();
    assertEquals("var aa = 1; function foo() { var bb = 2; }", actualOutputEdge);

    // Error scenario: Test renaming of reserved words
    source = "function foo() { var if = 1; }";
    compiler.reset();
    renamePass = new RenameVars(compiler, "", false, false, false, null, null, null);
    Node externsError = NodeFactory.createRootNode(source);
    Node rootError = NodeFactory.createRootNode(NodeFactory.createRootNode(NodeFactory.createProgramNode()));
    try {
      renamePass.process(externsError, rootError);
      fail("Expected an error when trying to rename a reserved word");
    } catch (AssertionError e) {
      // Expected
    }
  }
}