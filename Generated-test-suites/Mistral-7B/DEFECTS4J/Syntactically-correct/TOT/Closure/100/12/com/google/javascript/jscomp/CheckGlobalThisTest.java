import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() {}";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testPrototypeMethod() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() { this.bar = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testConstructorFunction() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() { this.bar = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    Node constructorNode = tree.getFirstChild();
    constructorNode.setType(Token.FUNCTION);
    constructorNode.addAttribute(new JSDocInfo("@constructor"));
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testThisAnnotation() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo@this() { this.bar = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testAssignmentToThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() { var self = this; self.bar = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testAssignmentToGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() { this = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertTrue(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testPropertyAccessThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() { console.log(this.bar); }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertTrue(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testThisInPrototype() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() {} Foo.prototype.bar = function() { this.baz = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertTrue(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() {} Foo.prototype.bar = function() { this = 42; }";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testAssignmentToPrototypeSubproperty() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() {} Foo.prototype.bar = { baz: function() { this = 42; } };";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }

  @Test
  public void testAssignmentToPrototypeSubpropertyWithThisAnnotation() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    String source = "function Foo() {} Foo.prototype.bar = { baz: function@this() { this = 42; } };";
    Node tree = AbstractCompiler.parseJavaScript(source, null, null);
    NodeTraversal traversal = new NodeTraversal(tree, checker);
    traversal.traverse();
    compiler.process(tree, null, null);
    assertFalse(compiler.getDiagnostics().hasError());
  }
}