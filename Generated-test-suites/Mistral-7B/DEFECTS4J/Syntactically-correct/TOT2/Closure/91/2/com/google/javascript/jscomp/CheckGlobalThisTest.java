import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.DiagnosticType.JSC_USED_GLOBAL_THIS;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testSafeMethod() {
    Node node = newNode("function safeMethod() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, node, null);
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testUnsafeMethod() {
    Node node = newNode("function unsafeMethod() { this.property = 1; }");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, node, null);
    assertNotNull(traversal.getFirstError());
  }

  @Test
  public void testConstructor() {
    Node node = newNode("function MyConstructor() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, node, null);
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testPrototypeMethod() {
    Node node = newNode("function MyObject() {} MyObject.prototype.myMethod = function() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, newNode("MyObject.myMethod"), null);
    assertFalse(traversal.hasErrors());
  }

  @Test(expected = IllegalStateException.class)
  public void testNullParent() {
    Node node = newNode("function unsafeMethod() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, node, null);
    assertNotNull(traversal.getFirstError());
    checker.visit(traversal, node, null); // should throw an exception
  }

  @Test
  public void testGetPropertyWithPrototype() {
    Node node = newNode("var obj = {}; Object.defineProperty(obj, 'prototype', { get: function() { return this; } }); obj.prototype.myMethod = function() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, newNode("obj.prototype.myMethod"), null);
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testGetPropertyWithPrototypeChain() {
    Node node = newNode("var obj = {}; Object.defineProperty(obj, 'prototype', { get: function() { return this; } }); Object.defineProperty(obj, 'grandPrototype', { get: function() { return this; } }); obj.grandPrototype.myMethod = function() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, newNode("obj.grandPrototype.myMethod"), null);
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testFunctionWithJsDoc() {
    Node node = newNode(
        "/**" +
        " * @constructor" +
        " */" +
        "function MyConstructor() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, newNode("MyConstructor"), null);
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = newNode(
        "/**" +
        " * @this {MyType}" +
        " */" +
        "function myFunction() {}");
    NodeTraversal traversal = createTraversal(node);
    checker.visit(traversal, newNode("myFunction"), null);
    assertFalse(traversal.hasErrors());
  }

  private NodeTraversal createTraversal(Node node) {
    NodeTraversal traversal = new NodeTraversal(compiler, node);
    checker.traverse(traversal, node, null);
    return traversal;
  }

  private Node newNode(String code) {
    return new Node(code);
  }
}