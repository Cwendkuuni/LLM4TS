import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.CallbackFlag.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    traversal = new NodeTraversal(compiler);
  }

  @Test
  public void testGlobalThisUse() {
    String source = "function evil() { this.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(1, checker.compiler.getNumErrors());
  }

  @Test
  public void testPrototypeMethod() {
    String source = "function Foo() {} Foo.prototype.bar = function() { this.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testConstructor() {
    String source = "function Foo() { this.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testThisAnnotatedFunction() {
    String source = "@this class Foo { this.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
    String source = "function evil() { this.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testFunctionInsideObjectLiteral() {
    String source = "({property: function() { this.property = 1; }})";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(1, checker.compiler.getNumErrors());
  }

  @Test
  public void testFunctionLentToPrototype() {
    String source = "function Foo() {} Foo.prototype.evil = function() { this.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    String source = "function Foo() { this.property = {}; Foo.prototype.property.evil = function() { this.property = 1; } }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(1, checker.compiler.getNumErrors());
  }

  @Test
  public void testAssignmentToPrototypeSubProperty() {
    String source = "function Foo() { this.property = {evil: function() { this.property.subProperty = 1; }}; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(1, checker.compiler.getNumErrors());
  }

  @Test
  public void testFunctionWithParentAssign() {
    String source = "var a = {}; function evil() { a.property = 1; } a = this;";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testFunctionWithParentCall() {
    String source = "function Foo() {} function evil() { Foo.prototype.property = 1; }";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }

  @Test
  public void testFunctionWithParentBlock() {
    String source = "function Foo() {} (function() { Foo.prototype.property = 1; })();";
    TestSourceFile tf = new TestSourceFile("TestFile.js", source);
    Node root = compiler.processString(tf, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler, checker, null);
    traverser.traverse(root);

    assertEquals(0, checker.compiler.getNumErrors());
  }
}