import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.CallbackFlag.SYNCHRONOUS;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(new Diagnostics());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    AbstractCompiler compiler = new AbstractCompiler(options);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    // Test case 1: Valid function
    Node node1 = createFunctionNode("function validFunction() { }");
    NodeTraversal traversal1 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal1.traverse(node1);
    assertTrue(traversal1.getDiagnostics().isEmpty());

    // Test case 2: Function with @constructor JSDoc annotation
    Node node2 = createFunctionNode("/** @constructor */ function Constructor() { }");
    NodeTraversal traversal2 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal2.traverse(node2);
    assertTrue(traversal2.getDiagnostics().isEmpty());

    // Test case 3: Function with @this JSDoc annotation
    Node node3 = createFunctionNode("function withThisAnnotation() { '@this': {} }");
    NodeTraversal traversal3 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal3.traverse(node3);
    assertTrue(traversal3.getDiagnostics().isEmpty());

    // Test case 4: Function in a valid context
    Node node4 = createFunctionNode("var validContext = function validContext() {};");
    NodeTraversal traversal4 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal4.traverse(node4);
    assertTrue(traversal4.getDiagnostics().isEmpty());

    // Test case 5: Function in an invalid context
    Node node5 = createFunctionNode("function invalidContext() { this.property = 'test'; }");
    NodeTraversal traversal5 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal5.traverse(node5);
    assertFalse(traversal5.getDiagnostics().isEmpty());

    // Test case 6: Function getting lent to a prototype property
    Node node6 = createFunctionNode("function getLentToPrototype() { Object.prototype.property = function() {}; }");
    NodeTraversal traversal6 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal6.traverse(node6);
    assertFalse(traversal6.getDiagnostics().isEmpty());

    // Test case 7: Function with no logical place to put a this annotation
    Node node7 = createFunctionNode("function noThisAnnotation() { this.property = 'test'; }");
    NodeTraversal traversal7 = new NodeTraversal(compiler, null, checkGlobalThis, SYNCHRONOUS);
    traversal7.traverse(node7);
    assertTrue(traversal7.getDiagnostics().isEmpty());
  }

  private Node createFunctionNode(String code) {
    Node n = new Node(Token.SCRIPT);
    Node script = new Node(Token.BLOCK);
    Node scriptFn = new Node(Token.FUNCTION);
    scriptFn.setJSDocInfo(new JSDocInfo());
    scriptFn.setSource(code);
    script.appendChild(scriptFn);
    n.appendChild(script);
    return n;
  }
}