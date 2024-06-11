import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.token.TokenType.ASSIGN;
import static com.google.javascript.rhino.token.TokenType.FUNCTION;
import static com.google.javascript.rhino.token.TokenType.THIS;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private static SourceFile compile(String jsCode) {
    Parser parser = new Parser();
    SourceFile source = parser.parse(jsCode, null, null);
    return source;
  }

  private AbstractCompiler createCompiler() {
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Compiler underlyingCompiler = new Compiler(new CompilerOptions());
    Mockito.when(compiler.getOptions()).thenReturn(underlyingCompiler.getOptions());
    Mockito.when(compiler.report).thenCallRealMethod();
    return compiler;
  }

  @Test
  public void testSafeUsesOfThis() {
    SourceFile source = compile("function Foo() { this.x = 1; }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    assertThat(diagnostics).isEmpty();
  }

  @Test
  public void testUnsafeUseOfThisInFunction() {
    SourceFile source = compile("function evil() { this.x = 1; }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getCode()).isEqualTo(DiagnosticType.JSC_USED_GLOBAL_THIS);
    assertThat(diagnostic.getSourceName()).isEqualTo(source.getName());
    assertThat(diagnostic.getStartColumn()).isEqualTo(2);
    assertThat(diagnostic.getStartLine()).isEqualTo(1);
  }

  @Test
  public void testUnsafeUseOfThisInPropertyAccess() {
    SourceFile source = compile("function Foo() { console.log(this.x); }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getCode()).isEqualTo(DiagnosticType.JSC_USED_GLOBAL_THIS);
    assertThat(diagnostic.getSourceName()).isEqualTo(source.getName());
    assertThat(diagnostic.getStartColumn()).isEqualTo(8);
    assertThat(diagnostic.getStartLine()).isEqualTo(1);
  }

  @Test
  public void testSafeUseOfThisInPrototypeMethod() {
    SourceFile source = compile("function Foo() {} Foo.prototype.bar = function() { this.x = 1; }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    assertThat(diagnostics).isEmpty();
  }

  @Test
  public void testSafeUseOfThisInConstructor() {
    SourceFile source = compile("function Foo() { this.x = 1; }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getCode()).isEqualTo(DiagnosticType.JSC_IS_CONSTRUCTOR);
    assertThat(diagnostic.getMessage()).contains("constructor");
  }

  @Test
  public void testSafeUseOfThisWithThisTypeJSDoc() {
    SourceFile source = compile("/** @this {Type} */ function Foo() { this.x = 1; }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    assertThat(diagnostics).isEmpty();
  }

  @Test
  public void testUnsafeUseOfThisInAssignment() {
    SourceFile source = compile("function evil() { a = this; }");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getCode()).isEqualTo(DiagnosticType.JSC_USED_GLOBAL_THIS);
    assertThat(diagnostic.getSourceName()).isEqualTo(source.getName());
    assertThat(diagnostic.getStartColumn()).isEqualTo(5);
    assertThat(diagnostic.getStartLine()).isEqualTo(1);
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeProperty() {
    SourceFile source = compile("function Foo() {} Foo.prototype.bar = function() { this.x = 1; } Foo.prototype.prototype = { z: function() { this.y = 1; } };");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getCode()).isEqualTo(DiagnosticType.JSC_USED_GLOBAL_THIS);
    assertThat(diagnostic.getSourceName()).isEqualTo(source.getName());
    assertThat(diagnostic.getStartColumn()).isEqualTo(16);
    assertThat(diagnostic.getStartLine()).isEqualTo(3);
  }

  @Test
  public void testUnsafeUseOfThisInPropertyAccessOfPrototypeProperty() {
    SourceFile source = compile("function Foo() {} Foo.prototype.bar = function() { console.log(this.x); } Foo.prototype.prototype = { z: function() { console.log(this.y); } };");
    AbstractCompiler compiler = createCompiler();
    NodeTraversal traversal = new NodeTraversal(source, compiler, null);
    new CheckGlobalThis(compiler, WARNING).traverse(traversal, source.getTree(), null);
    List<DiagnosticPosition> diagnostics = Lists.newArrayList(compiler.getDiagnostics());
    DiagnosticPosition diagnostic1 = diagnostics.get(0);
    assertThat(diagnostic1.getCode()).isEqualTo(DiagnosticType.JSC_USED_GLOBAL_THIS);
    assertThat(diagnostic1.getSourceName()).isEqualTo(source.getName());
    assertThat(diagnostic1.getStartColumn()).isEqualTo(16);
    assertThat(diagnostic1.getStartLine()).isEqualTo(3);

    DiagnosticPosition diagnostic2 = diagnostics.get(1);
    assertThat(diagnostic2.getCode()).isEqualTo(DiagnosticType.JSC_USED_GLOBAL_THIS);
    assertThat(diagnostic2.getSourceName()).isEqualTo(source.getName());
    assertThat(diagnostic2.getStartColumn()).isEqualTo(19);
    assertThat(diagnostic2.getStartLine()).isEqualTo(4);
  }
}