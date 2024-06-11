import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String source = "function evil() { this.useful = undefined; }";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertTrue(compiler.getDiagnostics().length > 0);
    DiagnosticType diagnostic = compiler.getDiagnostics()[0].getType();
    assertTrue(diagnostic.getCode() == DiagnosticType.WARNING);
    assertTrue(diagnostic.getMessage().contains("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String source = "function Evil() { this.useful = undefined; }";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertFalse(compiler.getDiagnostics().length > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotations() {
    String source = "/** @this {myClass} */ function Evil() { this.useful = undefined; }";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertFalse(compiler.getDiagnostics().length > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    String source = "function Evil() { this.useful = undefined; }";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertTrue(compiler.getDiagnostics().length > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionCall() {
    String source = "function evil() { this.useful = undefined; } function test() { evil(); }";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertFalse(compiler.getDiagnostics().length > 0);
  }

  @Test
  public void testCheckGlobalThis_PrototypeProperty() {
    String source = "function Evil() {} Evil.prototype.evil = function() { this.useful = undefined; }";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertFalse(compiler.getDiagnostics().length > 0);
  }

  @Test
  public void testCheckGlobalThis_PrototypeSubProperty() {
    String source = "function Evil() {} Evil.prototype.x = { evil: function() { this.useful = undefined; } };";

    Parser parser = new Parser(ParserOptions.standard());
    AbstractCompiler compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    Node ast = parser.parse(source, null, null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);

    new CheckGlobalThis(compiler).visit(traversal, ast, null);

    assertFalse(compiler.getDiagnostics().length > 0);
  }
}