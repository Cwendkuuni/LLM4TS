import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private Checker checker;
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    checker = CheckerFactory.getChecker("--warn_unused_require", "--warn_circle", "--check_types", "--allow_all_access", "--no_emit", options);
    compiler = new AbstractCompiler(checker);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSafeUsesOfThis() {
    Node node = createFunctionNode("function safeThis() { this.x = 5; }");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, INFO).traverse(traversal, node, null);
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testUnsafeUsesOfThisAsFunctionName() {
    Node node = createFunctionNode("function unsafeThis() { return this; }");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, node, null);
    assertEquals(1, checker.getNumDiagnostics());
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.equals(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testUnsafeUsesOfThisAsPropertyAccess() {
    Node node = createFunctionNode("function test() { this.x = 5; } test.y = 3;");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, node, null);
    assertEquals(1, checker.getNumDiagnostics());
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.equals(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testConstructorFunction() {
    Node node = createFunctionNode("function Constructor() { this.x = 5; }");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, INFO).traverse(traversal, node, null);
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = createFunctionNode("@this\nfunction test() { this.x = 5; }");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, INFO).traverse(traversal, node, null);
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testFunctionWithFunctionAnnotation() {
    Node node = createFunctionNode("function test() { @constructor }\nthis.x = 5;");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, INFO).traverses(traversal, node, null);
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    Node node = createFunctionNode("function test() { this.x = 5; }");
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, node, null);
    assertEquals(1, checker.getNumDiagnostics());
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.equals(CheckGlobalThis.GLOBAL_THIS));
  }

  private Node createFunctionNode(String code) {
    return nodeFactory.parseTree(code).getFirstChild().getFirstChild();
  }
}