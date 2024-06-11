import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    checker = new Checker(compiler, new CompilerOptions());
    nodeFactory = new NodeFactory();
  }

  @After
  public void teardown() {
    compiler = null;
    checker = null;
    nodeFactory = null;
  }

  /**
   * Test for safe usage of 'this' keyword in a function annotated with
   * '@constructor'.
   */
  @Test
  public void testConstructorFunction() {
    String jsCode = "function MyFunction() { this.myProperty = 42; }";
    SourceFile source = new SourceFile("MyFunction.js", jsCode);
    Node rootNode = checker.check(source, null);
    Node constructorNode = rootNode.getFirstChild();
    CheckGlobalThis checker = new CheckGlobalThis(checker);
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertFalse(checker.hasDiagnostics());
  }

  /**
   * Test for safe usage of 'this' keyword in a prototype method.
   */
  @Test
  public void testPrototypeMethod() {
    String jsCode = "function MyObject() {} MyObject.prototype.myMethod = function() { this.myProperty = 42; }";
    SourceFile source = new SourceFile("MyObject.js", jsCode);
    Node rootNode = checker.check(source, null);
    Node myMethodNode = rootNode.getFirstChild().getLastChild();
    CheckGlobalThis checker = new CheckGlobalThis(checker);
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertFalse(checker.hasDiagnostics());
  }

  /**
   * Test for safe usage of 'this' keyword in a function annotated with '@this'.
   */
  @Test
  public void testThisAnnotatedFunction() {
    String jsCode = "@this MyFunction() { this.myProperty = 42; }";
    SourceFile source = new SourceFile("MyFunction.js", jsCode);
    Node rootNode = checker.check(source, null);
    Node rootFunctionNode = rootNode.getFirstChild();
    CheckGlobalThis checker = new CheckGlobalThis(checker);
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertFalse(checker.hasDiagnostics());
  }

  /**
   * Test for safe usage of 'this' keyword where there is no logical place
   * to put a '@this' annotation.
   */
  @Test
  public void testSafeUsageOfThis() {
    String jsCode = "function MyFunction() { this.myProperty = 42; } MyFunction.prototype.myMethod = function() { this.myProperty = 43; }";
    SourceFile source = new SourceFile("MyFunction.js", jsCode);
    Node rootNode = checker.check(source, null);
    CheckGlobalThis checker = new CheckGlobalThis(checker);
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertFalse(checker.hasDiagnostics());
  }

  /**
   * Test for unsafe usage of 'this' keyword on the left side of an
   * assignment.
   */
  @Test
  public void testUnsafeAssignment() {
    String jsCode = "var a = this; a.myProperty = 42;";
    SourceFile source = new SourceFile("Test.js", jsCode);
    Node rootNode = checker.check(source, null);
    Node assignNode = rootNode.getFirstChild();
    CheckGlobalThis checker = new CheckGlobalThis(checker);
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertTrue(checker.hasDiagnostics());
    assertTrue(checker.getDiagnostics().size() == 1);
    assertEquals("dangerous use of the global 'this' object", checker.getDiagnostics().get(0).getMessage());
  }

  /**
   * Test for unsafe usage of 'this' keyword on the left side of a property
   * access.
   */
  @Test
  public void testUnsafePropertyAccess() {
    String jsCode = "var a = { prop: this }; a.prop.myProperty = 42;";
    SourceFile source = new SourceFile("Test.js", jsCode);
    Node rootNode = checker.check(source, null);
    Node assignNode = rootNode.getFirstChild();
    CheckGlobalThis checker = new CheckGlobalThis(checker);
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertTrue(checker.hasDiagnostics());
    assertTrue(checker.getDiagnostics().size() == 1);
    assertEquals("dangerous use of the global 'this' object", checker.getDiagnostics().get(0).getMessage());
  }
}