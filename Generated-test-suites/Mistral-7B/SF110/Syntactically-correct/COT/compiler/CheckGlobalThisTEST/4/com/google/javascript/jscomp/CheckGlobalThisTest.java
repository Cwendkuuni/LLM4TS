import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeType.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checker = new Checker(compiler, new AbstractCompiler.CheckFactory() {
      @Override
      public Callback create(AbstractCompiler compiler, NodeTraversal t) {
        return new CheckGlobalThis(compiler);
      }
    });
    traversal = new NodeTraversal(new SourceFile(null, null, "", null), checker);
  }

  @Test
  public void testSafeUsesOfThis() {
    Node node = new Node("function foo() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeAssignments() {
    Node node = new Node("function evil() { this = {}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeAssignmentsWithAssignments() {
    Node node = new Node("function evil() { this = {}; a = {}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeAssignmentsWithPropertyAccess() {
    Node node = new Node("function evil() { this.prototype.foo = {}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeAssignmentsWithPrototypeAccess() {
    Node node = new Node("function evil() { this.prototype.prototype.foo = {}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeAssignmentsWithPrototypeAccessViaGet() {
    Node node = new Node("function evil() { var p = this.prototype; p.foo = {}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testConstructorFunctions() {
    Node node = new Node("function Foo() {}");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testConstructorFunctionsWithThis() {
    Node node = new Node("function Foo() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testPrototypeMethods() {
    Node node = new Node("function Foo() {} Foo.prototype.bar = function() {};");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testPrototypeMethodsWithThis() {
    Node node = new Node("function Foo() {} Foo.prototype.bar = function() { this.baz = 42; }");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testArbitraryFunctionWithThis() {
    Node node = new Node("function evil() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node node = new Node("/** @this {type: 'Foo'} */ function evil() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithNoThisAnnotation() {
    Node node = new Node("function evil() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithThisAnnotationAndFunctionAsProperty() {
    Node node = new Node("function Foo() {} /** @this {type: 'Foo'} */ Foo.prototype.evil = function() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithNoThisAnnotationAndFunctionAsProperty() {
    Node node = new Node("function Foo() {} Foo.prototype.evil = function() { this.bar = 42; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithThisAnnotationAndVariableAsProperty() {
    Node node = new Node("function Foo() {} /** @this {type: 'Foo'} */ Foo.prototype.evil = function() { this.bar = this.foo; }");
    traversal.traverse(node);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithNoThisAnnotationAndVariableAsProperty() {
    Node node = new Node("function Foo() {} Foo.prototype.evil = function() { this.bar = this.foo; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithThisAnnotationAndFunctionReturningAnObject() {
    Node node = new Node("function Foo() { return {evil: function() { this.bar = 42; }}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithNoThisAnnotationAndFunctionReturningAnObject() {
    Node node = new Node("function Foo() { return {evil: function() { this.bar = 42; }}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithThisAnnotationAndFunctionReturningAnObjectViaGet() {
    Node node = new Node("function Foo() { return {'evil': function() { this.bar = 42; }}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testFunctionWithNoThisAnnotationAndFunctionReturningAnObjectViaGet() {
    Node node = new Node("function Foo() { return {'evil': function() { this.bar = 42; }}; }");
    traversal.traverse(node);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }
}