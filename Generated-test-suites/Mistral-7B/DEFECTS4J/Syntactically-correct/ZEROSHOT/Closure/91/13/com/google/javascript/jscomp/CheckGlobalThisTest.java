import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Test file for CheckGlobalThis class.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testCheckGlobalThis() {
    Node ast = parseSource(
        "function evil() { this.useful = undefined; }", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    Node ast = parseSource(
        "function Evil() { this.useful = undefined; }", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisPrototype() {
    Node ast = parseSource(
        "var MyObject = function() {}; MyObject.prototype.evil = function() { this.useful = undefined; };", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisFunctionAnnotation() {
    Node ast = parseSource(
        "@this function evil() { this.useful = undefined; }", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisFunctionWithNoAnnotation() {
    Node ast = parseSource(
        "function evil() { this.useful = undefined; }", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, ERROR), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisFunctionWithVariableDeclaration() {
    Node ast = parseSource(
        "var evil = function() { this.useful = undefined; };", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, ERROR), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisFunctionWithVariableDeclarationAssignedToProperty() {
    Node ast = parseSource(
        "var evil = function() {}; var MyObject = {evil: evil};", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, ERROR), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisFunctionWithVariableDeclarationAssignedToPrototype() {
    Node ast = parseSource(
        "var evil = function() {}; var MyObject = function() { this.evil = evil };", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, ERROR), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisFunctionWithVariableDeclarationAssignedToPrototypeWithLhsChild() {
    Node ast = parseSource(
        "function Foo() { this.a = this.b = function() {}; }", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, ERROR), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertEquals(1, checker.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisFunctionWithVariableDeclarationAssignedToPrototypeWithLhsChildAndNoError() {
    Node ast = parseSource(
        "function Foo() { this.a = this.b = {}; }", nodeFactory);

    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING), null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  private Node parseSource(String source, NodeFactory nodeFactory) {
    Parser parser = new Parser(nodeFactory);
    return parser.parseProgram(source);
  }
}