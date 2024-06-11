import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TypeChecker;
import com.google.javascript.rhino.Node;

/**
 * Tests for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "function Foo() {}";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertFalse(typeChecker.hasErrors());
  }

  @Test
  public void testUnsafeUseOfThisAsProperty() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "var x = function() { this.property = 1; }";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertTrue(typeChecker.hasErrors("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testUnsafeUseOfThisAsAssignment() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "var x = this;";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertTrue(typeChecker.hasErrors("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testSafeUseOfThisInPrototypeMethod() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "function Foo() {} Foo.prototype.bar = function() { this.property = 1; }";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertFalse(typeChecker.hasErrors());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithAtConstructor() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "/** @constructor */ function Foo() {}";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertFalse(typeChecker.hasErrors());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotatedWithAtThis() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "/** @this {Object} */ function Foo() {}";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertFalse(typeChecker.hasErrors());
  }

  @Test
  public void testSafeUseOfThisInFunctionWhereThereIsNoLogicalPlaceToPutAThisAnnotation() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "function Foo() { this.property = 1 }";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertFalse(typeChecker.hasErrors());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeAccess() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "function Foo() { this.prototype.property = 1; }";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertTrue(typeChecker.hasErrors("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeAccessThroughGetProperty() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "function Foo() { this.prototype.property = 1; } Foo.prototype.getX = function() { return this.prototype; }";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertTrue(typeChecker.hasErrors("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithPrototypeAccessThroughGetPropertyChain() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(options, new CheckerFactory());
    TypeChecker typeChecker = compiler.getTypeChecker();

    String code = "function Foo() { this.prototype.property = 1; } Foo.prototype.getX = function() { return this.getY().prototype; }";
    Node node = TypeChecker.parseCompilationUnit(code, null, null, null).getRootNode();
    NodeTraversal traversal = new NodeTraversal(typeChecker, new CheckGlobalThis(compiler, WARNING), node);
    traversal.traverse();
    assertTrue(typeChecker.hasErrors("JSC_USED_GLOBAL_THIS"));
  }
}