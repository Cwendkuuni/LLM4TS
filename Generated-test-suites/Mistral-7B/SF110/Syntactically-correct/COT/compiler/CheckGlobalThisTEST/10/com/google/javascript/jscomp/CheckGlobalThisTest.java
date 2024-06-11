import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  // Test case for method: shouldTraverse(NodeTraversal, Node, Node)
  @Test
  public void testShouldTraverse() {
    Node root = AbstractCompiler.parseCompilationUnit("function foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: visit(NodeTraversal, Node, Node)
  @Test
  public void testVisit() {
    Node root = AbstractCompiler.parseCompilationUnit("function foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.visit(traversal, null, null);
    Node thisNode = root.getFirstChild();
    DiagnosticType diagnosticType = checkGlobalThis.GLOBAL_THIS;
    assertEquals(diagnosticType, traversal.getLastDiagnostic());
  }

  // Test case for method: shouldReportThis(Node)
  @Test
  public void testShouldReportThis() {
    Node root = AbstractCompiler.parseCompilationUnit("function foo() { this.bar = 1; }");
    Node thisNode = root.getFirstChild();
    assertTrue(checkGlobalThis.shouldReportThis(thisNode));
  }

  // Test case for method: getFunctionJsDocInfo(Node)
  @Test
  public void testGetFunctionJsDocInfo() {
    Node root = AbstractCompiler.parseCompilationUnit("function foo() {}");
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(root);
    assertNull(jsDocInfo);

    root = AbstractCompiler.parseCompilationUnit("function foo() {}");
    root.setFirstChild(new Node(TOKEN_VAR, "x", null, null));
    root.getFirstChild().setNext(root);
    root.getFirstChild().getLastChild().setNext(root.getFirstChild(1));
    root.getFirstChild(1).setType(TOKEN_FUNCTION);
    root.getFirstChild(1).setName("foo");
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(root.getFirstChild(1));
    assertNull(jsDocInfo);

    root.getFirstChild(1).setFirstChild(new Node(TOKEN_KEYWORD, "function", null, null));
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(root.getFirstChild(1));
    assertNull(jsDocInfo);

    root.getFirstChild(1).setFirstChild(new Node(TOKEN_VAR, "x", null, null));
    root.getFirstChild(1).getFirstChild().setNext(new Node(TOKEN_EQUALS, null, null, null));
    root.getFirstChild(1).getFirstChild().getNext().setType(TOKEN_FUNCTION);
    root.getFirstChild(1).getFirstChild().getNext().setName("foo");
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(root.getFirstChild(1).getFirstChild().getNext());
    assertNull(jsDocInfo);

    root.getFirstChild(1).getFirstChild().getNext().setFirstChild(new Node(TOKEN_KEYWORD, "function", null, null));
    jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(root.getFirstChild(1).getFirstChild().getNext());
    assertNotNull(jsDocInfo);
  }

  // Test case for method: shouldTraverse with prototype property
  @Test
  public void testShouldTraversePrototypeProperty() {
    Node root = AbstractCompiler.parseCompilationUnit("function Foo() {} Foo.prototype.bar = function() { this.baz = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: shouldTraverse with object literal
  @Test
  public void testShouldTraverseObjectLiteral() {
    Node root = AbstractCompiler.parseCompilationUnit("{ foo: function() { this.bar = 1; } }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: shouldTraverse with constructor function
  @Test
  public void testShouldTraverseConstructorFunction() {
    Node root = AbstractCompiler.parseCompilationUnit("function Foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: shouldTraverse with interface function
  @Test
  public void testShouldTraverseInterfaceFunction() {
    Node root = AbstractCompiler.parseCompilationUnit("interface Foo { bar: number; } function Foo() {}");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: shouldTraverse with @this annotation
  @Test
  public void testShouldTraverseWithThisAnnotation() {
    Node root = AbstractCompiler.parseCompilationUnit("/** @this {foo:number} */ function Foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: shouldTraverse with logical place for @this annotation
  @Test
  public void testShouldTraverseWithLogicalPlaceForThisAnnotation() {
    Node root = AbstractCompiler.parseCompilationUnit("function Foo() { var a = function() { this.bar = 1; }; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: shouldTraverse with non-global context
  @Test(expected = AssertionError.class)
  public void testShouldTraverseWithNonGlobalContext() {
    Node root = AbstractCompiler.parseCompilationUnit("function Foo() { this.bar = 1; } function Bar() { } Bar.prototype.baz = function() { this.bar = 2; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.shouldTraverse(traversal, root, null);
  }

  // Test case for method: visit with @constructor annotation
  @Test
  public void testVisitWithConstructorAnnotation() {
    Node root = AbstractCompiler.parseCompilationUnit("/** @constructor */ function Foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.visit(traversal, null, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: visit with prototype method
  @Test
  public void testVisitWithPrototypeMethod() {
    Node root = AbstractCompiler.parseCompilationUnit("function Foo() {} Foo.prototype.bar = function() { this.baz = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.visit(traversal, null, null);
    Node prototypeMethod = traversal.getLastNode();
    DiagnosticType diagnosticType = checkGlobalThis.GLOBAL_THIS;
    assertEquals(diagnosticType, traversal.getLastDiagnostic());
  }

  // Test case for method: visit with method annotated with @this
  @Test
  public void testVisitWithThisAnnotation() {
    Node root = AbstractCompiler.parseCompilationUnit("/** @this {foo:number} */ function Foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.visit(traversal, null, null);
    assertTrue(traversal.isCompleted());
  }

  // Test case for method: visit with method where there's no logical place to put a @this annotation
  @Test
  public void testVisitWithNoLogicalPlaceForThisAnnotation() {
    Node root = AbstractCompiler.parseCompilationUnit("function Foo() { var a = function() { this.bar = 1; }; }");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    checkGlobalThis.visit(traversal, null, null);
    assertTrue(traversal.isCompleted());
  }
}