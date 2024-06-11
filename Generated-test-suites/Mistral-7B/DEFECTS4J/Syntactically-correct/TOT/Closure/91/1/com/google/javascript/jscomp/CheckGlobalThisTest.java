import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.parser.Parser;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    this.compiler = new Compiler(options);
    this.traversal = new NodeTraversal(compiler);
  }

  @Test
  public void testSafeUseOfThis() {
    String source = "function Foo() { this.bar = 42; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testUnsafeUseOfThisAsProperty() {
    String source = "var a = {}; a.this = {}; a.this.bar = 42;";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testUnsafeUseOfThisAsFunction() {
    String source = "function Foo() { return this; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void testSafeUseOfThisInFunctionParameter() {
    String source = "function Foo(thisArg) { thisArg.bar = 42; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testSafeUseOfThisInConstructor() {
    String source = "function Foo() { this.bar = 42; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testSafeUseOfThisInPrototypeMethod() {
    String source = "function Foo() {} Foo.prototype.bar = function() { this.baz = 42; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithAtConstructor() {
    String source = "@constructor\nfunction Foo() { this.bar = 42; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithAtThis() {
    String source = "@this\nfunction Foo() { this.bar = 42; }";
    TestSourceFile testSourceFile = new TestSourceFile("Foo.js", source);
    this.compiler.compile(testSourceFile, null);
    Node node = this.traversal.traverse(testSourceFile.ast);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.visit(this.traversal, node, null);
    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }
}