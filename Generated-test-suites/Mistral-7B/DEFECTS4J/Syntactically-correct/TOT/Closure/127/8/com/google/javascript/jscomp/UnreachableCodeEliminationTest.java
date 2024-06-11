import static com.google.javascript.jscomp.UnreachableCodeElimination.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testSimpleReturnWithDeadCode() {
    String input = "function test() { return; alert('dead code'); }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { return; }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterConditionalReturn() {
    String input = "function test(x) { if (x) { return; } alert('dead code'); }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test(x) { if (x) { return; } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterCatchBlock() {
    String input = "try { throw new Error('Test Error'); } catch (e) { return; alert('dead code'); }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { try { throw new Error('Test Error'); } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterForLoop() {
    String input = "for (var i = 0; i < 10; i++) { return; alert('dead code'); }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { for (var i = 0; i < 10; i++) { } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterWhileLoop() {
    String input = "while (true) { return; alert('dead code'); }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { while (true) { } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterIfStatement() {
    String input = "if (true) { return; alert('dead code'); } else { return; }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { if (true) { return; } else { return; } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterSwitchStatement() {
    String input = "switch (true) { case 0: return; alert('dead code'); } }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("switch (true) { case 0: return; } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterConditionalStatement() {
    String input = "if (true) { if (false) { return; } } else { return; }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { if (true) { if (false) { } } else { return; } }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterConditionalExpression() {
    String input = "var x = true ? return ; alert('dead code') : 1";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { var x = true ? return ; }", output.toSource());
  }

  @Test
  public void testDeadCodeAfterFunctionCall() {
    String input = "function test() { return; alert('dead code'); foo(); }";
    Node node = NodeFactory.compile(input);
    Compiler compiler = new Compiler();
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, node);
    Node output = node;
    assertEquals("function test() { return; foo(); }", output.toSource());
  }
}