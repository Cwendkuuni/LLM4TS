import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeCoverageUtil.*;
import static com.google.javascript.rhino.NodeTag.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal.AbstractCallback;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testBasicFunction() {
    String jsCode = "function f() {" +
                     "  return;" +
                     "  alert('should not be called');" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(1, getFunctionParameterList(result).size());
    assertEquals(1, getNumberOfStatements(result));
  }

  @Test
  public void testReachableCode() {
    String jsCode = "function f() {" +
                     "  return 1;" +
                     "  alert('should be called');" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(1, getFunctionParameterList(result).size());
    assertEquals(2, getNumberOfStatements(result));
  }

  @Test
  public void testDeadCodeAfterReturn() {
    String jsCode = "function f() {" +
                     "  if (true) {" +
                     "    return;" +
                     "    alert('should not be called');" +
                     "  }" +
                     "  return 1;" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(0, getNumberOfStatements(result));
  }

  @Test
  public void testDeadCodeInFunctionCall() {
    String jsCode = "function f() {" +
                     "  function g() {" +
                     "    return;" +
                     "    alert('should not be called');" +
                     "  }" +
                     "  g();" +
                     "  return 1;" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(1, getNumberOfStatements(result));
  }

  @Test
  public void testEdgeCaseWithEmptyFunctionBody() {
    String jsCode = "function f() {}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(0, getNumberOfStatements(result));
  }

  @Test
  public void testEdgeCaseWithBreakStatement() {
    String jsCode = "function f() {" +
                     "  for (var i = 0; i < 10; i++) {" +
                     "    if (i == 5) {" +
                     "      break;" +
                     "    }" +
                     "    alert('should be called');" +
                     "  }" +
                     "  return;" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(5, getNumberOfStatements(result));
  }

  @Test
  public void testEdgeCaseWithContinueStatement() {
    String jsCode = "function f() {" +
                     "  for (var i = 0; i < 10; i++) {" +
                     "    if (i % 2 == 0) {" +
                     "      continue;" +
                     "    }" +
                     "    alert('should be called');" +
                     "  }" +
                     "  return;" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(5, getNumberOfStatements(result));
  }

  @Test(expected = AssertionError.class)
  public void testThrowsException() {
    String jsCode = "function f() {" +
                     "  throw new Error('Test Error');" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    // This statement throws an AssertionError
    assertEquals(1, getNumberOfStatements(result));
  }

  @Test
  public void testNoOpStatements() {
    String jsCode = "function f() {" +
                     "  a.b.MyClass.prototype.propertyName;" +
                     "  true;" +
                     "  return 1;" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(1, getNumberOfStatements(result));
  }

  @Test
  public void testEdgeCaseWithMultipleReturnStatements() {
    String jsCode = "function f() {" +
                     "  if (true) {" +
                     "    return 1;" +
                     "  }" +
                     "  if (false) {" +
                     "    return 2;" +
                     "  }" +
                     "  return 3;" +
                     "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(3, getNumberOfStatements(result));
  }

  @Test
  public void testEdgeCaseWithEmptyBlock() {
    String jsCode = "function f() {" +
                     "}" +
                     "return 1;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(1, getNumberOfStatements(result));
  }

  @Test
  public void testEdgeCaseWithEmptyFunction() {
    String jsCode = "function f() {}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAllowAllIdentifiers(true);
    options.setWarningLevel(Compiler.WarningLevel.QUIRKS);

    Node externs = Node.parse(getTestSource("test_externs.js"));
    Node root = Node.parse(jsCode);

    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(externs, root);

    Node result = root;
    assertTrue(isFunctionDeclaration(result));
    assertEquals(0, getNumberOfStatements(result));
  }

  private boolean isFunctionDeclaration(Node node) {
    return node != null && node.isFunction() && node.getFirstChild().getTag() == FUNCTION;
  }

  private List<Node> getFunctionParameterList(Node node) {
    NodeList parameters = node.getParameters();
    return Lists.newArrayList(parameters);
  }

  private int getNumberOfStatements(Node node) {
    if (node == null) {
      return 0;
    }

    if (node.isFunction() || node.isBlock()) {
      return node.getLastChild().getNumberOfChildren();
    }

    return node.getNumberOfChildren();
  }
}