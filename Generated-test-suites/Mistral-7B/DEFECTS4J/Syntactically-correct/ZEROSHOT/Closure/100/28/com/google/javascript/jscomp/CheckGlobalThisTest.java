import static org.junit.Assert.assertThat;
import static com.google.common.collect.Iterators.getOnlyElement;
import static com.google.common.collect.Iterables.find;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Iterable;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions(null);
    compiler = new TestCompiler(options);
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInFunction() {
    String jsCode = "function x() { this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInFunctionConstructor() {
    String jsCode = "function X() { this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypeMethod() {
    String jsCode = "function X() {} X.prototype.x = function() { this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 3, 2,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInNestedFunction() {
    String jsCode = "function x() { function y() { this.a = 1; } }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 3, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInFunctionWithThisAnnotation() {
    String jsCode = "function @this x() { this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInFunctionWithNoAnnotation() {
    String jsCode = "function x() { this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInVariableDeclaration() {
    String jsCode = "function x() { var this = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInVariableDeclarationWithAnnotation() {
    String jsCode = "function x() { var @this this = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInAssignment() {
    String jsCode = "var x = this;";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, 1, 5, DiagnosticType.WARNING,
        "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInObjectPropertyAccess() {
    String jsCode = "this.prototype.x = 1;";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 3, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypePropertyAccess() {
    String jsCode = "this.prototype.prototype.x = 1;";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 7, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypeChainAccess() {
    String jsCode = "this.prototype.constructor.prototype.x = 1;";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 15, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypeChainAccessWithThisAnnotation() {
    String jsCode = "function X() {} X.prototype.constructor.prototype.x = function() { @this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 15, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypeChainAccessWithThisAnnotationOnMethod() {
    String jsCode = "function X() {} X.prototype.constructor.prototype.x = function @this() { this.a = 1; }";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 15, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypeChainAccessWithThisAnnotationOnFunction() {
    String jsCode = "function X() { @this function x() { this.a = 1; } } X.prototype.constructor.prototype.x = x;";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 15, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  @Test
  public void testCheckGlobalThis_GlobalThisInPrototypeChainAccessWithThisAnnotationOnFunctionWithPrototype() {
    String jsCode = "function X() { @this function x() { this.a = 1; } } X.prototype = { constructor: X }; X.prototype.constructor.prototype.x = x;";
    Node node = compiler.compileStringToNode(jsCode);

    NodeTraversal traversal = new NodeTraversal(compiler, null);
    new CheckGlobalThis(compiler, CheckLevel.WARNING).traverse(traversal, node, null);

    Iterable<Node> diagnostics = traversal.getDiagnostics();
    assertDiagnostics(diagnostics, jsCode, jsCode.length() - 15, 5,
        DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS");
  }

  private void assertDiagnostics(Iterable<Node> diagnostics, String jsCode, int start, int end,
      DiagnosticType expectedType, String expectedMessage) {
    int numDiagnostics = getOnlyElement(diagnostics).getFirstChild().getNumChildren();
    assert(numDiagnostics == 1);

    Node diagnostic = getOnlyElement(diagnostics);
    Node messageNode = diagnostic.getLastChild();
    String actualMessage = messageNode.getString();

    assert(actualMessage.contains(expectedMessage));
    assert(diagnostic.getFirstChild().getSourceFile().getCode() == jsCode.toCharArray());
    assert(diagnostic.getFirstChild().getStartPosition().getLine() == start / compiler.getLineLength());
    assert(diagnostic.getFirstChild().getStartPosition().getColumn() == end);
  }
}