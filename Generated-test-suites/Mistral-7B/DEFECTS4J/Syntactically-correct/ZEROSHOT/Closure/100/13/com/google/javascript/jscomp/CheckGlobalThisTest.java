import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  private static Node createFunctionNode(String functionCode, CheckLevel level) {
    Parser parser = new Parser(null);
    Node root = parser.parse(functionCode, null, null);
    AbstractCompiler compiler = new AbstractCompiler(root, null, null, null);
    compiler.setLevel(level);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse();
    return root;
  }

  @Test
  public void testSimpleFunction() {
    String code = "function evil() { this.useful = 42; }";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() > 0);
  }

  @Test
  public void testFunctionWithJsDoc() {
    String code = "/** @constructor */ function GoodConstructor() {}";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() == 0);
  }

  @Test
  public void testFunctionWithJsDocAndThisAnnotation() {
    String code = "/** @constructor @this {Object} */ function GoodConstructor() {}";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() == 0);
  }

  @Test
  public void testFunctionWithNoJsDocOrThisAnnotation() {
    String code = "function GoodFunction() {}";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() == 0);
  }

  @Test
  public void testFunctionWithAssignment() {
    String code = "function evil() { var a = this; }";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() == 0);
  }

  @Test
  public void testFunctionWithPropertyAccess() {
    String code = "function evil() { this.prototype.property = 42; }";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() > 0);
  }

  @Test
  public void testFunctionWithPrototypePropertyAccess() {
    String code = "function evil() { Object.prototype.property = 42; }";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() > 0);
  }

  @Test
  public void testFunctionWithNamespacePropertyAccess() {
    String code = "function evil() { window.namespace.property = 42; }";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() > 0);
  }

  @Test
  public void testFunctionWithFunctionAssignment() {
    String code = "function evil() { this.useful = function() {}; }";
    Node node = createFunctionNode(code, WARNING);
    assertTrue(node.getDiagnosticMessages().size() > 0);
  }

  @Test
  public void testFunctionWithVariableDeclaration() {
    String code = "function evil() { var a = this; }";
    Node node = createFunctionNode(code, ERROR);
    assertTrue(node.getDiagnosticMessages().size() > 0);
  }
}