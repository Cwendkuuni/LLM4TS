import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.GLOBAL;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JavaScriptCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Node rootNode;
  private Compiler compiler;

  @Before
  public void setup() {
    rootNode = NodeUtil.createNodeFromString(
        "function foo() {" +
            "if (false) {" +
            "  alert('should be removed');" +
            "}" +
            "return; " +
            "alert('should not be removed');" +
            "}");

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    compiler = new JavaScriptCompiler(options);
  }

  @Test
  public void testUnreachableCodeElmination() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertEquals("function foo() {return;}", NodeUtil.toString(rootNode));
  }

  @Test
  public void testUnreachableCodeWithFunctionCall() {
    rootNode = NodeUtil.createNodeFromString(
        "function foo() {" +
            "if (false) {" +
            "  return alert('should be removed');" +
            "}" +
            "return function bar() {};" +
            "}");

    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertEquals("function foo() {return function bar() {};}", NodeUtil.toString(rootNode));
  }

  @Test
  public void testUnreachableCodeWithConditionalBranch() {
    rootNode = NodeUtil.createNodeFromString(
        "function foo() {" +
            "if (false) {" +
            "  return; " +
            "}" +
            "if (true) {" +
            "  return; " +
            "}" +
            "return; " +
            "alert('should not be removed');" +
            "}");

    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertEquals("function foo() {return;}", NodeUtil.toString(rootNode));
  }

  @Test
  public void testUnreachableCodeWithLoop() {
    rootNode = NodeUtil.createNodeFromString(
        "function foo() {" +
            "for (var i = 0; i < 10; i++) {" +
            "  if (false) {" +
            "    break;" +
            "  }" +
            "}" +
            "return; " +
            "alert('should not be removed');" +
            "}");

    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertEquals("function foo() {return;}", NodeUtil.toString(rootNode));
  }

  @Test
  public void testUnreachableCodeWithTryCatch() {
    rootNode = NodeUtil.createNodeFromString(
        "function foo() {" +
            "try {" +
            "  if (false) {" +
            "    throw new Error('should be removed');" +
            "  }" +
            "}" +
            "catch (e) {" +
            "  alert('should not be removed');" +
            "}" +
            "}");

    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertEquals("function foo() {}", NodeUtil.toString(rootNode));
  }

  @Test
  public void testUnreachableCodeWithSwitch() {
    rootNode = NodeUtil.createNodeFromString(
        "function foo() {" +
            "var i = 1;" +
            "switch (i) {" +
            "  case 1:" +
            "    if (false) {" +
            "      return; " +
            "    }" +
            "    break;" +
            "  case 2:" +
            "    return; " +
            "}" +
            "alert('should not be removed');" +
            "}");

    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertEquals("function foo() {}", NodeUtil.toString(rootNode));
  }
}