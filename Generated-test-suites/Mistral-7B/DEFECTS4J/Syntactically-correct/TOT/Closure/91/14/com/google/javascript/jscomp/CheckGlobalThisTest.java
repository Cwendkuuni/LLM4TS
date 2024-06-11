import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

/**
 * Tests for {@link CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evil() { this.useful = undefined; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.equals(CheckGlobalThis.GLOBAL_THIS));
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function MyConstructor() { }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeMethod() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "var MyObject = function() { }; MyObject.prototype.myMethod = function() { this.useful = undefined; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evilFunction() { this.useful = undefined; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evilFunction@this() { this.useful = undefined; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithPrototypeProperty() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "var MyObject = function() { this.myProperty = {} }; MyObject.prototype.myMethod = function() { this.useful = undefined; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithPrototypeSubProperty() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "var MyObject = function() { this.myProperty = {} }; MyObject.prototype.myMethod = function() { this.myProperty.subProperty.useful = undefined; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithAssignLeftSide() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evilFunction() { var a = this; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithAssignRightSide() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "function evilFunction() { this.useful = a; }";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithAssignmentToPrototypeProperty() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "var MyObject = function() { }; MyObject.prototype.myProperty = function() { this.useful = undefined; }; MyObject.prototype.myProperty = a;";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithAssignmentToPrototypeSubProperty() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addProcessor(new CheckGlobalThis(compiler, WARNING));

    String sourceCode = "var MyObject = function() { }; MyObject.prototype.myProperty = function() { this.subProperty.useful = undefined; }; MyObject.prototype.myProperty.subProperty = a;";

    Node node = AbstractNode.parseTree(sourceCode);
    NodeTraversal traversal = new NodeTraversal(compiler, node, null);
    traversal.traverse();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}