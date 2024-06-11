import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.JsParser;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private Compiler compiler;
  private Checker checker;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checker = new Checker(new JsParser(compiler));
    checker.setCompiler(compiler);
  }

  @Test
  public void testCheckGlobalThis() {
    String sourceCode = "function evil() { this.useful = undefined; }";
    Node node = NodeFactory.createFromString(sourceCode);
    NodeTraversal traversal = new NodeTraversal(checker, node, null);
    checker.process(node, null);
    traversal.traverse();
    assertNull(compiler.getDiagnostics().findErrorByMessage("JSC_USED_GLOBAL_THIS"));

    sourceCode = "function evil() { this.useful = undefined }\n"
        + "var a = this; a.useful = undefined;";
    node = NodeFactory.createFromString(sourceCode);
    traversal = new NodeTraversal(checker, node, null);
    checker.process(node, null);
    traversal.traverse();
    assertNull(compiler.getDiagnostics().findErrorByMessage("JSC_USED_GLOBAL_THIS"));

    sourceCode = "function evil() { this.useful = undefined }\n"
        + "var a = {} \n"
        + "a.evil = function() { this.useful = undefined; }";
    node = NodeFactory.createFromString(sourceCode);
    traversal = new NodeTraversal(checker, node, null);
    checker.process(node, null);
    traversal.traverse();
    assertNull(compiler.getDiagnostics().findErrorByMessage("JSC_USED_GLOBAL_THIS"));

    sourceCode = "function evil() { this.useful = undefined }\n"
        + "var a = { useable: function() {}} \n"
        + "a.evil = function() { this.useful = undefined; }";
    node = NodeFactory.createFromString(sourceCode);
    traversal = new NodeTraversal(checker, node, null);
    checker.process(node, null);
    traversal.traverse();
    assertNull(compiler.getDiagnostics().findErrorByMessage("JSC_USED_GLOBAL_THIS"));

    sourceCode = "function evil() { this.useful = undefined }\n"
        + "var a = { useable: function() {}} \n"
        + "a.evil = function() { this.useful = undefined; } \n"
        + "this.useful = 42";
    node = NodeFactory.createFromString(sourceCode);
    traversal = new NodeTraversal(checker, node, null);
    checker.process(node, null);
    traversal.traverse();
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().getErrors().size() == 1);
    assertTrue(compiler.getDiagnostics().getError(0).getErrorType() == DiagnosticType.WARNING);
    assertTrue(compiler.getDiagnostics().getError(0).getMessage().equals("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testCheckGlobalThisAssignedToVariable() {
    String sourceCode = "function evil() { this.useful = undefined } \n"
        + "var a = this;";
    Node node = NodeFactory.createFromString(sourceCode);
    NodeTraversal traversal = new NodeTraversal(checker, node, null);
    checker.process(node, null);
    traversal.traverse();
    assertNull(compiler.getDiagnostics().findErrorByMessage("JSC_USED_GLOBAL_THIS"));
  }
}