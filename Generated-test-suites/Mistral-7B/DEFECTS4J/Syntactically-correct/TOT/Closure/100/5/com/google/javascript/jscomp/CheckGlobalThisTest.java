import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUsage() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String sourceCode = "function Foo() {}";
    Node node = JavaScriptCore.compileStringToNode(sourceCode, null, options);

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testPrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String sourceCode = "function Foo() {}\nFoo.prototype.bar = function() { this.x = 5; };";
    Node node = JavaScriptCore.compileStringToNode(sourceCode, null, options);

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String sourceCode = "function Foo@this() {}";
    Node node = JavaScriptCore.compileStringToNode(sourceCode, null, options);

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testFunctionWithNoThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String sourceCode = "function Foo() { this.x = 5; }";
    Node node = JavaScriptCore.compileStringToNode(sourceCode, null, options);

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getTypeName());
  }

  @org.junit.Test
  public void testAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String sourceCode = "var x = this;";
    Node node = JavaScriptCore.compileStringToNode(sourceCode, null, options);

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testAssignmentOnPrototype() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String sourceCode = "function Foo() {} Foo.prototype.x = this;";
    Node node = JavaScriptCore.compileStringToNode(sourceCode, null, options);

    NodeTraversal traversal = new NodeTraversal(node, checker);
    traversal.traverse();

    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getTypeName());
  }
}