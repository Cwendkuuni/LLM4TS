import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.bare.BareFunctionDeclaration;
import com.google.javascript.jscomp.parser.bare.BareFunctionExpression;
import com.google.javascript.jscomp.parser.bare.BareVariableDeclaration;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

/**
 * Test class for CheckGlobalThis class.
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test valid use of 'this'.
    Node validNode = createNode(
        "function valid() { this.property = 'value'; }");
    NodeTraversal traversal = createTraversal(validNode);
    checker.traverse(traversal, validNode, null);
    assertThat(traversal.getErrors().isEmpty(), notNullValue());

    // Test invalid use of 'this'.
    Node invalidNode = createNode(
        "function invalid() { this.property = 'value'; }");
    NodeTraversal invalidTraversal = createTraversal(invalidNode);
    checker.traverse(invalidTraversal, invalidNode, null);
    DiagnosticType diagnosticType = invalidTraversal.getErrors().get(0)
        .getType();
    assertThat(diagnosticType, is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThis_constructor() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test valid use of 'this' in constructor.
    Node validNode = createNode(
        "function ValidConstructor() {}");
    NodeTraversal traversal = createTraversal(validNode);
    checker.traverse(traversal, validNode, null);
    assertThat(traversal.getErrors().isEmpty(), notNullValue());

    // Test invalid use of 'this' in constructor.
    Node invalidNode = createNode(
        "function InvalidConstructor() { this.property = 'value'; }");
    NodeTraversal invalidTraversal = createTraversal(invalidNode);
    checker.traverse(invalidTraversal, invalidNode, null);
    DiagnosticType diagnosticType = invalidTraversal.getErrors().get(0)
        .getType();
    assertThat(diagnosticType, is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThis_prototype() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test valid use of 'this' in prototype.
    Node validNode = createNode(
        "function ValidFunction() {}" +
        "ValidFunction.prototype.property = 'value';");
    NodeTraversal traversal = createTraversal(validNode);
    checker.traverse(traversal, validNode, null);
    assertThat(traversal.getErrors().isEmpty(), notNullValue());

    // Test invalid use of 'this' in prototype.
    Node invalidNode = createNode(
        "function InvalidFunction() {}" +
        "InvalidFunction.prototype.property = 'value';" +
        "function InvalidFunction() { this.property = 'value'; }");
    NodeTraversal invalidTraversal = createTraversal(invalidNode);
    checker.traverse(invalidTraversal, invalidNode, null);
    DiagnosticType diagnosticType = invalidTraversal.getErrors().get(0)
        .getType();
    assertThat(diagnosticType, is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThis_functionWithNoAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test valid use of 'this' in a function with no annotation.
    Node validNode = createNode(
        "function valid() { this.property = 'value'; }");
    NodeTraversal traversal = createTraversal(validNode);
    checker.traverse(traversal, validNode, null);
    assertThat(traversal.getErrors().isEmpty(), notNullValue());
  }

  @Test
  public void testCheckGlobalThis_simpleAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test valid use of assignment.
    Node validNode = createNode(
        "function valid() { this.property = c; }");
    NodeTraversal traversal = createTraversal(validNode);
    checker.traverse(traversal, validNode, null);
    assertThat(traversal.getErrors().isEmpty(), notNullValue());

    // Test invalid use of assignment.
    Node invalidNode = createNode(
        "function invalid() { this.property = this.property; }");
    NodeTraversal invalidTraversal = createTraversal(invalidNode);
    checker.traverse(invalidTraversal, invalidNode, null);
    DiagnosticType diagnosticType = invalidTraversal.getErrors().get(0)
        .getType();
    assertThat(diagnosticType, is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThis_getProp() {
    CheckGlobalThis checker = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test valid use of getProp.
    Node validNode = createNode(
        "function valid() { this.property.property = 'value'; }");
    NodeTraversal traversal = createTraversal(validNode);
    checker.traverse(traversal, validNode, null);
    assertThat(traversal.getErrors().isEmpty(), notNullValue());

    // Test invalid use of getProp.
    Node invalidNode = createNode(
        "function invalid() { this.property = this.property; }");
    NodeTraversal invalidTraversal = createTraversal(invalidNode);
    checker.traverse(invalidTraversal, invalidNode, null);
    DiagnosticType diagnosticType = invalidTraversal.getErrors().get(0)
        .getType();
    assertThat(diagnosticType, is(CheckGlobalThis.GLOBAL_THIS));
  }

  private Node createNode(String code) {
    AbstractCompiler compiler = new CompilerOptions()
        .getChecker("none");
    return compiler.parse(code, null, null).getTree();
  }

  private NodeTraversal createTraversal(Node node) {
    AbstractCompiler compiler = new CompilerOptions()
        .getChecker("none");
    return new NodeTraversal(compiler, node);
  }

  private boolean is(DiagnosticType expected, DiagnosticType actual) {
    return expected == actual;
  }
}