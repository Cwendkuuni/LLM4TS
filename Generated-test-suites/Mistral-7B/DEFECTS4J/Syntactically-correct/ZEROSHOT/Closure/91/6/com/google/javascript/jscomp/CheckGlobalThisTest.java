import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckerFactory checkerFactory;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    checkerFactory = new CheckerFactory();
    compiler = checkerFactory.newCompiler();
    diagnostics = new Diagnostics();
  }

  @Test
  public void testSafeUsesOfThis() {
    String code = "function Foo() {} "
        + "Foo.prototype.bar = function() {}; "
        + "function Foo$1() {} "
        + "var x = {foo: function() {}}; ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 0);
  }

  @Test
  public void testUnsafeUseOfThisAsProperty() {
    String code = "this.prop = 42;";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisAsLeftHandSideOfAssignment() {
    String code = "this = {foo: 42};";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisAsLeftHandSideOfAssignment2() {
    String code = "(function() { this.x = 42; })();";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisAsLeftHandSideOfAssignmentInConstructor() {
    String code = "function Foo() { this.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisAnnotatedWithThisAnnotation() {
    String code = "/** @this {Foo} */ function Foo() { this.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisAnnotatedWithNoAnnotation() {
    String code = "function Foo() { this.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithNoParent() {
    String code = "function evil() { this.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithParent() {
    String code = "function evil() { this.x = 42; } "
        + "function good() { evil(); } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeProperty() {
    String code = "function evil() { this.prototype.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeSubProperty() {
    String code = "function evil() { this.prototype.sub.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisInFunctionWithPrototypeProperty() {
    String code = "function Foo() {} "
        + "Foo.prototype.bar = function() { this.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisInFunctionWithPrototypeSubProperty() {
    String code = "function Foo() {} "
        + "Foo.prototype.bar = function() { this.sub.x = 42; } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeFunction() {
    String code = "function Foo() {"
        + "  function evil() { this.x = 42; } "
        + "} "
        + "Foo.prototype.bar = evil; ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisInFunctionWithPrototypeFunction2() {
    String code = "function Foo() {"
        + "  function evil() { this.x = 42; } "
        + "} "
        + "Foo.prototype.bar = function() { evil(); } ";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeClass() {
    String code = "class Foo {"
        + "  constructor() {"
        + "    this.x = 42;"
        + "  } "
        + "  evil() {"
        + "    this.x = 43;"
        + "  } "
        + "} ";

    SourceFile source = new SourceFile("Test.js", code, null, Token.ECMASCRIPT6);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertTrue(diagnostics.getNumErrors(), 1);
  }

  @Test
  public void testSafeUseOfThisInFunctionWithPrototypeClass2() {
    String code = "class Foo {"
        + "  constructor() {"
        + "    this.x = 42;"
        + "  } "
        + "  bar() {"
        + "    this.x = 43;"
        + "  } "
        + "} ";

    SourceFile source = new SourceFile("Test.js", code, null, Token.ECMASCRIPT6);
    Node root = Parser.parse(source, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertFalse(diagnostics.getNumErrors(), 1);
  }
}