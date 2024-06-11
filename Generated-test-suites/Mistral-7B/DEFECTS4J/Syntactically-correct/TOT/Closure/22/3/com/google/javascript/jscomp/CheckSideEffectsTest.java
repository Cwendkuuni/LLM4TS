import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.RewriterUtils;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function foo() {} var x = 1, y = 2; x = foo();";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler();

    CompilerInput input = new CompilerInput(new SourceFile("test.js", sourceCode));
    input.setOptions(options);
    Node root = Parser.parse(input, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertFalse("No warning expected", warnings.isEmpty());
    Node warning = warnings.get(0);
    assertEquals("Expected warning message",
        "Suspicious code. This code lacks side-effects. Is there a bug?",
        warning.getFirstChild().getString());

    // Test that the warning is reported at the correct location
    Node expectedLocation = warning.getParent().getChild(1);
    assertEquals("Expected warning to be at this location",
        "x = foo();", expectedLocation.getString());
  }

  @Test
  public void testCheckSideEffectsNoWarning() {
    String sourceCode = "function foo() {} var x = 1, y = 2; x += foo().length;";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler();

    CompilerInput input = new CompilerInput(new SourceFile("test.js", sourceCode));
    input.setOptions(options);
    Node root = Parser.parse(input, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertNull("No warning expected", checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testCheckSideEffectsProtectedCode() {
    String sourceCode = "function foo() {} JSCOMPILER_PRESERVE(JSCOMPILER_PRESERVE(x = foo()))";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler();

    CompilerInput input = new CompilerInput(new SourceFile("test.js", sourceCode));
    input.setOptions(options);
    Node root = Parser.parse(input, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertTrue("Expected no warning", warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsProtectedCodeRemoval() {
    String sourceCode = "function foo() {} JSCOMPILER_PRESERVE(JSCOMPILER_PRESERVE(x = foo()))";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler();

    CompilerInput input = new CompilerInput(new SourceFile("test.js", sourceCode));
    input.setOptions(options);
    Node root = Parser.parse(input, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertFalse("Expected warning", warnings.isEmpty());
    Node warning = warnings.get(0);

    // Check that the warning is reported at the protected code location
    Node protectedCode = warning.getFirstChild().getNext();
    assertEquals("Expected warning to be at this location",
        "JSCOMPILER_PRESERVE(x = foo())", protectedCode.getString());

    // Check that the protected code is removed
    NodeTraversal traversal = new NodeTraversal(compiler, null);
    NodeTraversal.traverse(compiler, root, new StripProtection(compiler));

    assertNull("Expected protected code to be removed",
        root.lookupVariable("x"));
  }

  /**
   * A helper class to remove side-effect sync functions that were added as a side-effect of the
   * protection mechanism.
   */
  static class StripProtection extends AbstractPostOrderCallback implements CompilerPass {

    private final AbstractCompiler compiler;

    StripProtection(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal.traverse(compiler, root, this);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall()) {
        Node target = n.getFirstChild();
        if (target.isName() && target.getString().equals("JSCOMPILER_PRESERVE")) {
          Node expr = n.getLastChild();
          n.detachChildren();
          parent.replaceChild(n, expr);
        }
      }
    }
  }
}