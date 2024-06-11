import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.*;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testSafeUsesOfThis() {
    String source = "function Foo() { this.x = 1; }";
    Node node = RhinoUtils.parseStringToNode(source);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse("No diagnostic reported for safe use of 'this'.", checker.hasErrors());
  }

  @Test
  public void testPrototypeMethod() {
    String source = "function Foo() {}\nFoo.prototype.x = function() { this.y = 1; }";
    Node node = RhinoUtils.parseStringToNode(source);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse("No diagnostic reported for safe use of 'this' in prototype method.", checker.hasErrors());
  }

  @Test
  public void testConstructor() {
    String source = "function Foo() { this.x = 1; }";
    Node node = RhinoUtils.parseStringToNode(source);
    Node constructorNode = NodeUtil.findFunctionInNode(node, "Foo");
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse("No diagnostic reported for constructor.", checker.hasErrors());

    // Test error reporting for invalid constructor usage
    NodeTraversal traversal2 = new NodeTraversal(node, checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        if (n.isFunction() && n.getJsDocInfo().isConstructor()) {
          return false;
        }
        return super.shouldTraverse(t, n, parent);
      }
    };
    NodeTraversal.traverse(node, traversal2);
    assertTrue("Diagnostic reported for constructor.", checker.hasErrors());
  }

  @Test
  public void testThisAnnotation() {
    String source = "/** @this {Int} */ function Foo() { this.x = 1; }";
    Node node = RhinoUtils.parseStringToNode(source);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse("No diagnostic reported for safe use of 'this' annotated with @this.", checker.hasErrors());
  }

  @Test
  public void testNoNeedForThisAnnotation() {
    String source = "function Foo() { this.x = 1; }";
    Node node = RhinoUtils.parseStringToNode(source);
    NodeTraversal traversal = new NodeTraversal(node, checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        if (n.isFunction()) {
          JSDocInfo jsDoc = n.getJSDocInfo();
          if (jsDoc != null && !jsDoc.isConstructor()) {
            return false;
          }
        }
        return super.shouldTraverse(t, n, parent);
      }
    };
    NodeTraversal.traverse(node, traversal);
    assertTrue("Diagnostic reported for function without @this annotation.", checker.hasErrors());
  }

  @Test
  public void testAssignmentToVariableOrObject() {
    String source = "function evil() { var a = this; }";
    Node node = RhinoUtils.parseStringToNode(source);
    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();
    assertFalse("No diagnostic reported for assignment to a variable.", checker.hasErrors());
  }

  @Test
  public void testAssignmentToPrototype() {
    String source = "var Foo = function() { this.x = 1; }";
    String source2 = "Foo.prototype.y = function() { this.x = 2; }";
    Node node = RhinoUtils.parseStringToNode(source);
    Node node2 = RhinoUtils.parseStringToNode(source2);
    NodeList nodes = NodeList.fromArray(node, node2);
    NodeTraversal traversal = new NodeTraversal(nodes, checker);
    traversal.traverse();
    assertFalse("No diagnostic reported for assignment to prototype.", checker.hasErrors());
  }
}