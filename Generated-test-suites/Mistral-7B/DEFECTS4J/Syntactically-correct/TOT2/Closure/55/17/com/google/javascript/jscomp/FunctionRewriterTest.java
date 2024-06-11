import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new Compiler();
    compiler.init();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testEmptyFunctionReducer() {
    String input = "function test() {}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
    String output = compiler.printTree(inputNode);
    String expectedOutput = "function test() {emptyFn()}";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testIdentityFunctionReducer() {
    String input = "function test(a) {return a}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
    String output = compiler.printTree(inputNode);
    String expectedOutput = "function test(a) {identityFn(a)}";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testReturnConstantReducer() {
    String input = "function test() {return 10}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
    String output = compiler.printTree(inputNode);
    String expectedOutput = "function test() {returnconst(10)}";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testGetterReducer() {
    String input = "function test() {return this.a}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
    String output = compiler.printTree(inputNode);
    String expectedOutput = "function test() {getter(\"a\")}";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSetterReducer() {
    String input = "function test(value) {this.a = value}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
    String output = compiler.printTree(inputNode);
    String expectedOutput = "function test(value) {setter(\"a\")(value)}";
    assertEquals(expectedOutput, output);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalFunctionReduction() {
    String input = "function test(a, b) {}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
  }

  @Test
  public void testNoReduction() {
    String input = "function test() {return 1 + 1}";
    Node inputNode = compiler.parseTree(input).getRoot();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, inputNode);
    String output = compiler.printTree(inputNode);
    String expectedOutput = "function test() {return 1 + 1}";
    assertEquals(expectedOutput, output);
  }
}