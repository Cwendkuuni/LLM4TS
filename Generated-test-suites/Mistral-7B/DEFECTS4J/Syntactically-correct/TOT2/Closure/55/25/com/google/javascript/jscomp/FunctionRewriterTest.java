import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the FunctionRewriter class.
 */
public class FunctionRewriterTest {

  private Compiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testEmptyFunctionReduction() {
    SourceFile input = new SourceFile("Test.js", "function test() {}\n");
    Node root = compiler.parseSingleFile(input, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedResult = new Node(Token.FUNCTION, 1, 0, "emptyFn");
    Node actualResult = findFunctionByName("test", root);

    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testIdentityFunctionReduction() {
    SourceFile input = new SourceFile(
        "Test.js",
        "function test(x) {return x;}\n");
    Node root = compiler.parseSingleFile(input, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedResult = new Node(Token.FUNCTION, 1, 0, "identityFn");
    Node actualResult = findFunctionByName("test", root);

    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testReturnConstantReduction() {
    SourceFile input = new SourceFile(
        "Test.js",
        "function test() {return 42;}\n");
    Node root = compiler.parseSingleFile(input, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedResult = new Node(Token.CALL, 1, 0, "returnconst", 42);
    Node actualResult = findFunctionByName("test", root);

    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testGetterReduction() {
    SourceFile input = new SourceFile(
        "Test.js",
        "function test() {return this.a;}\n");
    Node root = compiler.parseSingleFile(input, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedResult = new Node(Token.CALL, 1, 0, "getter", "a");
    Node actualResult = findFunctionByName("test", root);

    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test
  public void testSetterReduction() {
    SourceFile input = new SourceFile(
        "Test.js",
        "function test(x) {this.a = x;}\n");
    Node root = compiler.parseSingleFile(input, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedResult = new Node(Token.CALL, 1, 0, "setter", "a");
    Node actualResult = findFunctionByName("test", root);

    assertEquals(expectedResult.toString(), actualResult.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidFunctionReduction() {
    SourceFile input = new SourceFile(
        "Test.js",
        "function test() {}\nfunction test2() {}\n");
    Node root = compiler.parseSingleFile(input, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);
  }

  private Node findFunctionByName(String name, Node root) {
    for (int i = 0; i < root.getNumChildren(); i++) {
      Node child = root.getChild(i);
      if (child.getType() == Token.FUNCTION &&
          child.getString().equals(name)) {
        return child;
      }
    }
    throw new IllegalStateException("Function not found: " + name);
  }
}