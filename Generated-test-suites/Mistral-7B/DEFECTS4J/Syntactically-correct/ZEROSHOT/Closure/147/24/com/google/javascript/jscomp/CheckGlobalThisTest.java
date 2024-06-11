import static org.junit.Assert.*;
import static com.google.javascript.jscomp. JaneUtils.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.Check;
import com.google.javascript.jscomp.CompilerOptions.SourceMap;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo() { this.a = 1; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUseOfThisAsFunctionReturnValue() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo() { return this; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getId());
  }

  @Test
  public void testUnsafeUseOfThisInPropertyAssignment() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("var obj = {}; obj.globalThis = this;");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getId());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypePropertyAssignment() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo() { Object.prototype.globalThis = this; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getId());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeMethod() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo() {}\nFoo.prototype.globalThis = function() { return this; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testSafeUseOfThisInConstructor() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo() { this.a = 1; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithAtConstructor() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("@constructor\nfunction Foo() { this.a = 1; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithAtThis() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo(@this {a: number}) { this.a = this.a + 1; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithNoThisAnnotations() {
    CompilerOptions options = new CompilerOptions();
    options.setCheck(Check.TYPECHECK);
    options.setSourceMap(SourceMap.NONE);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = parseJs("function Foo() { this.a = 1; }");
    new CheckGlobalThis(compiler, CheckLevel.WARNING).process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  private Node parseJs(String code) {
    Node ast = new Parser().parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(new Compiler(new CompilerOptions()));
    traversal.traverse(ast);
    return ast;
  }
}