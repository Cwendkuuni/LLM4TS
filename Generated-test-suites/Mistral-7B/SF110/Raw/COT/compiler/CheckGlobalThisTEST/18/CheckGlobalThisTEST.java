import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.CallbackFlag.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    Node node = Node.newFunction("myFunction");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node node = Node.newFunction("function MyConstructor() { }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = Node.newFunction("@this class MyClass { }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoContext() {
    Node node = Node.newFunction("myFunction");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionInBlockContext() {
    Node node = Node.newBlock("function myFunction() { }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionInFunctionContext() {
    Node node = Node.newFunction("function outerFunction() { function myFunction() { } }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseFunctionInPrototypeContext() {
    Node node = Node.newBlock("var MyObject = { __proto__: { myFunction: function() { } } }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseFunctionInPrototypePropertyContext() {
    Node node = Node.newBlock("var MyObject = { myProperty: { myFunction: function() { } } }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTraverse(traversal, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionGettingLentToPrototype() {
    Node node = Node.newBlock("var MyObject = { __proto__: { get myFunction() { } } }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    boolean result = checker.shouldTravere(traversal, node, null);
    assertTrue(result);
  }

  @Test(expected = AssertionError.class)
  public void testInvalidFunction() {
    Node node = Node.newBlock("function myFunction() { } function myFunction2() { }");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    checker.shouldTraverse(traversal, node, null);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node leftSide = Node.newIdentifier("this");
    Node node = Node.newAssign(leftSide, Node.newIdentifier("myVariable"));
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    checker.visit(traversal, null, node);
    assertTrue(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node leftSide = Node.newIdentifier("myVariable");
    Node node = Node.newAssign(Node.newIdentifier("this"), leftSide);
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    checker.visit(traversal, null, node);
    assertFalse(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node node = Node.newGetProp(Node.newIdentifier("this"), "myProperty");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    checker.visit(traversal, null, node);
    assertTrue(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithPrototype() {
    Node node = Node.newGetProp(Node.newIdentifier("this"), "prototype");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    checker.visit(traversal, null, node);
    assertFalse(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithPrototypeChain() {
    Node node = Node.newGetProp(Node.newIdentifier("this"), "prototype", "myProperty");
    CheckGlobalThis checker = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, null);
    checker.visit(traversal, null, node);
    assertFalse(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldReportThisWithDiagnostics() {
    AbstractCompiler compiler = new CompilerOptions();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler, checker, null, null, null);
    Node node = Node.newBlock("function evilFunction() { this.myProperty = 1; }");
    checker.visit(traversal, null, node);
    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(compiler.hasDiagnostics(diagnosticType));
  }
}