import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.TokenType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function evil() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisNoDiagnostics() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function good() { return this.x; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisPrototype() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "var obj = { x: function() {} }; obj.prototype.evil = function() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisPrototypeDescent() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "var obj = { x: function() { this.y.useful = undefined; } };";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisFunctionCall() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function good() { return this.x(); } function x() { return 42; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisFunctionCallWithThisKeyword() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function good() { return this.x.call(this, 42); } function x() { return 42; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisFunctionCallPrototype() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function good() { return this.x.call(this.y, 42); } function x() { return 42; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisFunctionCallPrototypeDescendant() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function good() { return this.x.call(this.y.z, 42); } function x() { return 42; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function EvilConstructor() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisConstructorPrototype() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function EvilConstructor() {} EvilConstructor.prototype.evil = function() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisConstructorPrototypeDescendant() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function EvilConstructor() {} EvilConstructor.prototype.x = function() { this.y.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisFunctionWithThisAnnotation() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function @this evil() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisFunctionWithInterfaceAnnotation() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "interface IFoo {\n" +
        "  method(): void;\n" +
        "}\n" +
        "function @interface evil() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisFunctionWithOverrideAnnotation() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "class Foo {\n" +
        "  method(): void;\n" +
        "}\n" +
        "class Sub extends Foo {\n" +
        "  @override\n" +
        "  method(): void { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisFunctionWithThisAnnotationNested() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function @this Foo() {\n" +
        "  this.x = function() { this.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisWithGetProperty() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function evil() { this.x.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisWithGetPropertyDescendant() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function evil() { this.x.y.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(1, numDiagnostics);
    assertEquals("JSC_USED_GLOBAL_THIS", compiler.getDiagnostic(0).getCode());
  }

  @Test
  public void testCheckGlobalThisWithGetPropertyWithThisAnnotation() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function @this evil() { this.x.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testCheckGlobalThisWithGetPropertyWithThisAnnotationDescendant() {
    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String code = "function @this evil() { this.x.y.useful = undefined; }";
    Node node = AbstractCompiler.parseJs(compiler, code, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    node.traverse(checker);

    int numDiagnostics = compiler.getNumDiagnostics();
    assertEquals(0, numDiagnostics);
  }
}