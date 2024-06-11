import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory factory;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = compiler.getCheckers().get(Checker.class.getName());
    factory = compiler.getFactory();
    options = new CompilerOptions(null);
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    String code = "function Foo() { this.x = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String code = "function Foo() {}; Foo.prototype.x = function() { this.y = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String code = "function Foo() { @this }; function Foo.prototype.x = function() { this.y = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNoThisAnnotation() {
    String code = "function Foo() { this.x = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, ERROR);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    DiagnosticType diagnostic = checker.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithAssignment() {
    String code = "var a = function() { this.x = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, ERROR);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithVariableDeclaration() {
    String code = "function Foo() { var a = this; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, ERROR);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(0, checker.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNestedAssignment() {
    String code = "function Foo() { var a = this; a.x = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType diagnostic = checker.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNestedAssignmentAndPropertyAccess() {
    String code = "function Foo() { var a = this; a.x.y = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType diagnostic = checker.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithPrototypePropertyAccess() {
    String code = "function Foo() { this.prototype.x.y = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(1, checker.getDiagnostics().size());
    DiagnosticType diagnostic = checker.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithPrototypePropertyAccessAndAssignment() {
    String code = "function Foo() { this.prototype.x.y = 1; this.x = 1; }";
    Node ast = Parser.parse(code, null, null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker, null);
    checker.visit(traversal, ast, null);

    assertEquals(2, checker.getDiagnostics().size());
    DiagnosticType diagnostic1 = checker.getDiagnostics().get(0);
    DiagnosticType diagnostic2 = checker.getDiagnostics().get(1);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic1.getCode());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic2.getCode());
    assertEquals("dangerous use of the global 'this' object", diagnostic1.getMessage());
    assertEquals("dangerous use of the global 'this' object", diagnostic2.getMessage());
  }
}