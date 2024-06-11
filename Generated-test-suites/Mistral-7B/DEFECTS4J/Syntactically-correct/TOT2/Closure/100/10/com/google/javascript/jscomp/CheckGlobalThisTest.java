import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.ScriptSource;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsage() {
    Checker checker = new Checker();
    AbstractCompiler compiler = new AbstractCompiler(checker);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function SafeUsage() { this.useful = 42; }";
    ScriptSource script = new ScriptSource(code);
    Node root = NodeTraversal.traverse(script, check);

    Node functionNode = root.getFirstChild();
    assertTrue(check.shouldTraverse(functionNode, null));
    check.visit(null, null, null);

    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testUnsafeAssignment() {
    Checker checker = new Checker();
    AbstractCompiler compiler = new AbstractCompiler(checker);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "var a = function() { this.useful = 42; }";
    ScriptSource script = new ScriptSource(code);
    Node root = NodeTraversal.traverse(script, check);

    Node functionNode = root.getFirstChild();
    Node assignmentNode = functionNode.getLastChild();

    assertTrue(check.shouldTraverse(functionNode, null));
    assertFalse(check.shouldTraverse(assignmentNode, functionNode));

    check.visit(null, null, null);

    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testUnsafePropertyAccess() {
    Checker checker = new Checker();
    AbstractCompiler compiler = new AbstractCompiler(checker);
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);

    String code = "function UnsafePropertyAccess() { this.prototype.useful = 42; }";
    ScriptSource script = new ScriptSource(code);
    Node root = NodeTraversal.traverse(script, check);

    Node functionNode = root.getFirstChild();
    Node getPropNode = functionNode.getFirstChild();

    assertTrue(check.shouldTraverse(functionNode, null));
    assertTrue(check.shouldTraverse(getPropNode, functionNode));

    check.visit(null, null, null);

    assertTrue(compiler.hasErrors());
  }
}