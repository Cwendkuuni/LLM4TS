import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    options = new CompilerOptions(Collections.emptyMap());
    compiler = new AbstractCompiler(options);
    traversal = new NodeTraversal(compiler);
  }

  @Test
  public void testCheckGlobalThis() {
    String sourceCode = "function evil() { this.useful = undefined; }";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testCheckGlobalThisWithFunctionDeclaration() {
    String sourceCode = "function evil() { function inner() { this.useful = undefined; } }";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithFunctionExpression() {
    String sourceCode = "function evil() { var inner = function() { this.useful = undefined; }; }";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithFunctionConstructor() {
    String sourceCode = "function Evil() {} Evil.prototype.useful = undefined;";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeProperty() {
    String sourceCode = "function Evil() {} Evil.prototype.prototype.useful = undefined;";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithPropertyAccess() {
    String sourceCode = "function evil() { Object.prototype.useful = undefined; }";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testCheckGlobalThisWithFunctionExpressionInsideAssignment() {
    String sourceCode = "var a = function() { this.useful = undefined; };";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisWithFunctionDeclarationInsideAssignment() {
    String sourceCode = "var a = function evil() { this.useful = undefined; };";

    TestSourceFile testSourceFile = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);

    compiler.compile(Collections.singletonList(testSourceFile), null, null);

    Node ast = testSourceFile.getAst();
    traversal.traverse(ast, new CheckGlobalThis(compiler, WARNING));

    assertEquals(0, compiler.getDiagnostics().size());
  }
}