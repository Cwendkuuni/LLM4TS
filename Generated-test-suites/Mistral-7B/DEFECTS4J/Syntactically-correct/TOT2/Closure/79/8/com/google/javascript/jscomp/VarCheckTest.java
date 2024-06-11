import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;

public class VarCheckTest {

  @Test
  public void testValidVariableDeclaration() {
    Compiler compiler = new Compiler();
    CompilerPass varCheck = new VarCheck(compiler);

    Node root = Node.newText("var x = 5;");
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root), varCheck);

    // No errors or warnings should have been reported.
    assertFalse(compiler.getErrors().hasErrors());
  }

  @Test
  public void testUndefinedVariable() {
    Compiler compiler = new Compiler();
    CompilerPass varCheck = new VarCheck(compiler);

    Node root = Node.newText("var x; x = 5;");
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root), varCheck);

    List<DiagnosticPosition> errors = compiler.getErrors().getAllErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_UNDEFINED_VARIABLE", errors.get(0).getType().getName());
    assertEquals("x", errors.get(0).getPosition().getName());
  }

  @Test
  public void testMissingModuleDependency() {
    Compiler compiler = new Compiler();
    CompilerPass varCheck = new VarCheck(compiler);

    Node root1 = Node.newText("var x = require('missingModule').y;");
    Node root2 = Node.newText("var y = 5;");

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root1, root2), varCheck);

    List<DiagnosticPosition> errors = compiler.getErrors().getAllErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", errors.get(0).getType().getName());
    assertEquals("missingModule", errors.get(0).getPosition().getName());
  }

  @Test
  public void testStrictModuleDependency() {
    Compiler compiler = new Compiler();
    CompilerPass varCheck = new VarCheck(compiler, true);

    Node root1 = Node.newText("var x = require('module1').y;");
    Node root2 = Node.newText("var y = 5;");
    Node root3 = Node.newText("// module1.js requires module2.js before module1.js");
    Node root4 = Node.newText("(function() {\n" +
                              "  var module2 = {\n" +
                              "    getName: function() {\n" +
                              "      return 'module2';\n" +
                              "    }\n" +
                              "  };\n" +
                              "  module1 = module2;\n" +
                              "})();");

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(root1, root2, root3, root4), varCheck);

    List<DiagnosticPosition> errors = compiler.getErrors().getAllErrors();
    assertEquals(1, errors.size());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", errors.get(0).getType().getName());
    assertEquals("module1", errors.get(0).getPosition().getName());
  }
}