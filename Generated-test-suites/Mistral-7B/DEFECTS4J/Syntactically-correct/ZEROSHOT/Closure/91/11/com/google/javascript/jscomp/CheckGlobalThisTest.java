import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

/**
 * Test class for CheckGlobalThis.java
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING);
    compiler = new AbstractCompiler();
    checker = new Checker(compiler, options);
  }

  @Test
  public void testSafeUsesOfThis() {
    String code = "function SafeFunction() { this.prop = 'value'; }";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    String code = "window.unsafeFunction = function() { this.prop = 'value'; }";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals(diagnosticType, checker.getDiagnostics().get(0).getType());
  }

  @Test
  public void testUnsafeAssignmentsOfThis() {
    String code = "window.assignFunction = function() { this = {}; };";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals(diagnosticType, checker.getDiagnostics().get(0).getType());
  }

  @Test
  public void testUnsafeAssignmentsOfThisToVariables() {
    String code = "window.assignFunction = function() { var a = this; };";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testSafeUsesOfThisInPrototypeMethods() {
    String code = "function Foo() {} Foo.prototype.prop = function() { this.prop2 = 'value'; };";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testSafeUsesOfThisInConstructors() {
    String code = "function Foo() { this.prop = 'value'; }";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testSafeUsesOfThisInAnnotatedFunctions() {
    String code = "@this\nfunction Foo() { this.prop = 'value'; }";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testSafeUsesOfThisInFunctionsWithNoAnnotation() {
    String code = "function Foo() { this.prop = 'value'; }";
    Node root = new Node(Token.SCRIPT, null, null);
    Node tree = Parser.parse(code, null, null, root);
    NodeTraversal traversal = new NodeTraversal(checker, tree, null);
    CheckGlobalThis check = new CheckGlobalThis(checker, WARNING);
    traversal.traverse(check);
    assertEquals(0, checker.getDiagnostics().size());
  }
}