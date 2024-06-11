import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsage() {
    String source = "function Foo() {}";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    assertThat(compiler.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testUnsafePrototypePropertyAccess() {
    String source = "function Foo() {} Foo.prototype.x = function() { this.y = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    DiagnosticType globalThisDiagnostic = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertThat(compiler.getDiagnostics().stream()
        .filter(diagnostic -> diagnostic.getType() == globalThisDiagnostic)
        .count(), equalTo(1));
  }

  @Test
  public void testUnsafeAssignment() {
    String source = "var a = function() { this.x = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    assertThat(compiler.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testUnsafeAssignmentWithPrototypePropertyAccess() {
    String source = "function Foo() {}; Foo.prototype.x = function() { var a = this; this.y = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    DiagnosticType globalThisDiagnostic = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertThat(compiler.getDiagnostics().stream()
        .filter(diagnostic -> diagnostic.getType() == globalThisDiagnostic)
        .count(), equalTo(1));
  }

  @Test
  public void testSafeConstructor() {
    String source = "function Foo() { this.x = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    Node constructorNode = root.getFirstChild();
    constructorNode.setJSDocInfo(JSDocInfo.createConstructor());

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    assertThat(compiler.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testSafeMethod() {
    String source = "function Foo() {} Foo.prototype.m = function() { this.x = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    assertThat(compiler.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testSafeFunctionExpression() {
    String source = "function Foo() {}; var x = function() { this.x = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    assertThat(compiler.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testSafeFunctionExpressionWithPrototypePropertyAccess() {
    String source = "function Foo() {}; Foo.prototype.x = function() { var a = this; this.y = 5; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    DiagnosticType globalThisDiagnostic = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertThat(compiler.getDiagnostics().stream()
        .filter(diagnostic -> diagnostic.getType() == globalThisDiagnostic)
        .count(), equalTo(0));
  }

  @Test
  public void testUnsafeFunctionExpressionWithAssignment() {
    String source = "var a = function() { this.x = 5; }; this.y = 10";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);
    Parser parser = new Parser(new SourceFile("test.js", source));
    Node root = parser.parse();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAll();

    compiler.process(root, null, null);

    DiagnosticType globalThisDiagnostic = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertThat(compiler.getDiagnostics().stream()
        .filter(diagnostic -> diagnostic.getType() == globalThisDiagnostic)
        .count(), equalTo(1));
  }
}