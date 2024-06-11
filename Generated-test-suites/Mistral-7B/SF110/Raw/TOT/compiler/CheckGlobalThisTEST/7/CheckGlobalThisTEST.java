import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(true);
    options.setWarningLevel(CompilerOptions.WarningLevel.VERBOSE);
    compiler = new AbstractCompiler(options);
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testConstructor() {
    String code = "function Foo() {}";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for constructor", compiler.hasErrors());
  }

  @Test
  public void testPrototypeMethod() {
    String code = "function Foo() {} Foo.prototype.bar = function() {};";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for prototype method", compiler.hasErrors());
  }

  @Test
  public void testFunctionWithThis() {
    String code = "function Foo() { this.bar = 42; }";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for function with this", compiler.hasErrors());
  }

  @Test
  public void testFunctionAnnotatedWithThis() {
    String code = "/** @this {Object} */ function Foo() { this.bar = 42; }";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for function annotated with @this", compiler.hasErrors());
  }

  @Test
  public void testFunctionNoThis() {
    String code = "function Foo() { this.bar = 42; }";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    CheckGlobalThis checkerWithNoGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checkerWithNoGlobalThis);
    traversal.traverse();

    assertTrue("One warning expected for function without @this", compiler.hasErrors());
  }

  @Test
  public void testVariableAssignment() {
    String code = "var a = function() { this.bar = 42; }";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for variable assignment", compiler.hasErrors());
  }

  @Test
  public void testNestedAssignments() {
    String code = "(a = this).bar = 42;";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for nested assignments", compiler.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    String code = "Object.prototype.foo = function() { this.bar = 42; }";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for assignment to prototype property", compiler.hasErrors());
  }

  @Test
  public void testAssignmentToPrototypeSubproperty() {
    String code = "Object.prototype.foo.bar = function() { this.baz = 42; }";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for assignment to prototype subproperty", compiler.hasErrors());
  }

  @Test
  public void testLooseFunctionAssignment() {
    String code = "this.bar = function() {}";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    CheckGlobalThis checkerWithNoGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checkerWithNoGlobalThis);
    traversal.traverse();

    assertFalse("No warnings expected for loose function assignment", compiler.hasErrors());
  }

  @Test
  public void testFunctionWithJSDoc() {
    String code = "/** @constructor */ function Foo() {}";
    SourceFile source = Parser.parseJavaScript(code, "Foo.js");
    Node root = source.getTree();

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertFalse("No warnings expected for constructor with JSDoc", compiler.hasErrors());
  }
}