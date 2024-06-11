import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String source = "function evil() { this.useful = undefined; }";
    AbstractCompiler compiler = new Compiler();
    Node root = new Parser(compiler).parse(source, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(checker.compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; } (function() { this.useful = undefined; })();";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().size() == 1);

    source = "function evil() { this.useful = undefined; }.prototype.x = function() {};";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertFalse(compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; }.prototype.x = function x() {};";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertFalse(compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; }.prototype.x = function() { this.y = undefined; }";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertFalse(compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; function x() {} }";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; Object.prototype.x = function() {} }";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; var x = function() {} }";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());

    source = "function evil() { this.useful = undefined; var x = { y: function() {} } }";
    compiler.resetErrors();
    root = new Parser(compiler).parse(source, null, null);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionConstructor() {
    String source = "function EvilConstructor() { this.useful = undefined; }";
    AbstractCompiler compiler = new Compiler();
    Node root = new Parser(compiler).parse(source, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionPrototype() {
    String source = "function EvilFunction() {} EvilFunction.prototype.evil = function() { this.useful = undefined; }";
    AbstractCompiler compiler = new Compiler();
    Node root = new Parser(compiler).parse(source, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJSDoc() {
    String source = "/** @constructor */\nfunction EvilFunction() { this.useful = undefined; }";
    AbstractCompiler compiler = new Compiler();
    Node root = new Parser(compiler).parse(source, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String source = "function EvilFunction() { /** @this {Integer} */ this.useful = undefined; }";
    AbstractCompiler compiler = new Compiler();
    Node root = new Parser(compiler).parse(source, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    String source = "function EvilFunction() { this.useful = undefined; }";
    AbstractCompiler compiler = new Compiler();
    Node root = new Parser(compiler).parse(source, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertTrue(compiler.getErrors().isEmpty());
  }
}