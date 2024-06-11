import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil1() { this.useful = undefined; } " +
                     "function evil2() { this.useful = undefined; } " +
                     "function safe1() { var a = this; a.useful = undefined; } " +
                     "function safe2() { var a = this; a.useful = undefined; } " +
                     "function unsafeProto() { Object.defineProperty(Object.prototype, 'evilProp', { get: function() { return this; } }); }";

    Compiler compiler = new Compiler();
    Node rootNode = parser(jsCode).getRootNode();
    NodeTraversal nodeTraversal = new NodeTraversal(new CheckGlobalThis(compiler), rootNode);
    nodeTraversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("Expected 2 warnings, but got " + numDiagnostics, 2, numDiagnostics);

    // Check that the correct nodes were flagged
    Node evil1 = findNodeByLineAndColumn(rootNode, 1, 10);
    Node evil2 = findNodeByLineAndColumn(rootNode, 2, 10);
    Node[] safeNodes = new Node[] { findNodeByLineAndColumn(rootNode, 3, 2), findNodeByLineAndColumn(rootNode, 4, 2) };

    for (Node node : new Node[] { evil1, evil2 }) {
      assertThat(node.getClassName(), node.getType(), is(THIS));
      assertThat(node.getParent().getClassName(), node.getParent().getType(), is(FUNCTION));
      JSDocInfo jsDoc = node.getJSDocInfo();
      assertNull("Expected no JSDoc info for evil functions", jsDoc);
    }

    for (Node node : safeNodes) {
      assertThat(node.getClassName(), node.getType(), not(is(THIS)));
      JSDocInfo jsDoc = node.getJSDocInfo();
      if (jsDoc != null) {
        // Check that the safe functions have JSDoc info
        assertTrue("Expected JSDoc info for safe functions", jsDoc != null);
        assertFalse("Expected no 'constructor' JSDoc tag for safe functions", jsDoc.isConstructor());
        assertFalse("Expected no '@this' JSDoc tag for safe functions", jsDoc.hasThisType());
      }
    }
  }

  @Test
  public void testCheckGlobalThisWithConstructor() {
    String jsCode = "function EvilConstructor() { this.useful = undefined; }";

    Compiler compiler = new Compiler();
    Node rootNode = parser(jsCode).getRootNode();
    NodeTraversal nodeTraversal = new NodeTraversal(new CheckGlobalThis(compiler), rootNode);
    nodeTraversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("Expected 1 warning, but got " + numDiagnostics, 1, numDiagnostics);

    Node constructorNode = findNodeByLineAndColumn(rootNode, 1, 1);
    Node constructorJsDoc = constructorNode.getJSDocInfo();

    // Check that the constructor was flagged
    assertThat(constructorNode.getClassName(), constructorNode.getType(), is(FUNCTION));
    assertThat(constructorNode.getClassName(), constructorNode.getChildren()[0].getType(), is(THIS));
    assertTrue("Expected constructor JSDoc tag for constructor", constructorJsDoc != null);
    assertTrue("Expected 'constructor' JSDoc tag for constructor", constructorJsDoc.isConstructor());
  }

  @Test
  public void testCheckGlobalThisWithThisAnnotation() {
    String jsCode = "function EvilFunction(@this) { this.useful = undefined; }";

    Compiler compiler = new Compiler();
    Node rootNode = parser(jsCode).getRootNode();
    NodeTraversal nodeTraversal = new NodeTraversal(new CheckGlobalThis(compiler), rootNode);
    nodeTraversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("Expected 0 warnings, but got " + numDiagnostics, 0, numDiagnostics);

    Node functionNode = findNodeByLineAndColumn(rootNode, 1, 1);
    Node functionJsDoc = functionNode.getJSDocInfo();

    // Check that the function was not flagged
    assertThat(functionNode.getClassName(), functionNode.getType(), is(FUNCTION));
    assertThat(functionNode.getClassName(), functionNode.getChildren()[0].getType(), not(is(THIS)));
    assertTrue("Expected 'this' JSDoc tag for function", functionJsDoc != null);
    assertTrue("Expected '@this' JSDoc tag for function", functionJsDoc.hasThisType());
  }

  @Test
  public void testCheckGlobalThisWithNoAnnotation() {
    String jsCode = "function EvilFunction() { this.useful = undefined; }";

    Compiler compiler = new Compiler();
    Node rootNode = parser(jsCode).getRootNode();
    NodeTraversal nodeTraversal = new NodeTraversal(new CheckGlobalThis(compiler), rootNode);
    nodeTraversal.traverse();

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals("Expected 1 warning, but got " + numDiagnostics, 1, numDiagnostics);

    Node functionNode = findNodeByLineAndColumn(rootNode, 1, 1);
    Node functionJsDoc = functionNode.getJSDocInfo();

    // Check that the function was flagged
    assertThat(functionNode.getClassName(), functionNode.getType(), is(FUNCTION));
    assertThat(functionNode.getClassName(), functionNode.getChildren()[0].getType(), is(THIS));
    assertNull("Expected no JSDoc info for function", functionJsDoc);
  }
}