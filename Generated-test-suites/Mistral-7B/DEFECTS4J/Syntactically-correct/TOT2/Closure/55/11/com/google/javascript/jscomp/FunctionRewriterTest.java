import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node rootNode = functionRewriter.parseSyntheticCode("test", "function() {}");
    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, rootNode);

    Node actual = rootNode;
    Node expected = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_emptyFn"),
        null);

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String identityFunctionSource = "function(value) {return value}";
    Node identityFunctionNode = functionRewriter.parseSyntheticCode("test", identityFunctionSource);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, identityFunctionNode);

    Node actual = identityFunctionNode.getLastChild();
    Node expected = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_identityFn"),
        null);

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testConstantFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String constantFunctionSource = "function() {return 1}";
    Node constantFunctionNode = functionRewriter.parseSyntheticCode("test", constantFunctionSource);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, constantFunctionNode);

    Node actual = constantFunctionNode.getLastChild();
    Node expected = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_returnArg"),
        new Node(Token.INT_LITERAL, 1)
    );

    assertEquals(expected.toString(), actual.toString());
  }

  @Test(expected = AssertionError.class)
  public void testNonReducibleFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    String nonReducibleFunctionSource = "function(a, b) {}";
    Node nonReducibleFunctionNode = functionRewriter.parseSyntheticCode("test", nonReducibleFunctionSource);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, nonReducibleFunctionNode);

    fail("Expected an AssertionError, but no error was thrown.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullParam() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node rootNode = functionRewriter.parseSyntheticCode("test", "function() {}");

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, null);

    fail("Expected an IllegalArgumentException, but no error was thrown.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, null);

    fail("Expected an IllegalArgumentException, but no error was thrown.");
  }

  @Test
  public void testGetters() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String getterSource = "function getA() {return this.a_}";
    Node getterNode = functionRewriter.parseSyntheticCode("test", getterSource);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, getterNode);

    Node actual = getterNode.getLastChild();
    Node expected = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_get"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testSetters() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String setterSource = "function setA(value) {this.a_ = value}";
    Node setterNode = functionRewriter.parseSyntheticCode("test", setterSource);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, setterNode);

    Node actual = setterNode.getLastChild();
    Node expected = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testSettersWithDifferentPropertyNames() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String setterSource = "function setB(value) {this.b_ = value}";
    Node setterNode = functionRewriter.parseSyntheticCode("test", setterSource);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, setterNode);

    Node actual = setterNode.getLastChild();
    Node expected = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "b_")
    );

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testSettersWithSamePropertyNameButDifferentContext() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String setterSource1 = "function C.prototype.setA(value) {this.a_ = value}";
    Node setterNode1 = functionRewriter.parseSyntheticCode("test", setterSource1);

    String setterSource2 = "function D.prototype.setA(value) {this.a_ = value}";
    Node setterNode2 = functionRewriter.parseSyntheticCode("test", setterSource2);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, setterNode1);
    rewriter.process(null, setterNode2);

    Node actual1 = setterNode1.getLastChild();
    Node expected1 = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    Node actual2 = setterNode2.getLastChild();
    Node expected2 = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    assertEquals(expected1.toString(), actual1.toString());
    assertEquals(expected2.toString(), actual2.toString());
  }

  @Test
  public void testSettersWithSamePropertyNameButDifferentContextsInDifferentOrders() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String setterSource1 = "function C.prototype.setA(value) {this.a_ = value}";
    Node setterNode1 = functionRewriter.parseSyntheticCode("test", setterSource1);

    String setterSource2 = "function D.prototype.setA(value) {this.a_ = value}";
    Node setterNode2 = functionRewriter.parseSyntheticCode("test", setterSource2);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, setterNode2);
    rewriter.process(null, setterNode1);

    Node actual1 = setterNode1.getLastChild();
    Node expected1 = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    Node actual2 = setterNode2.getLastChild();
    Node expected2 = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    assertEquals(expected1.toString(), actual1.toString());
    assertEquals(expected2.toString(), actual2.toString());
  }

  @Test
  public void testSettersWithSamePropertyNameButDifferentContextsInSameOrder() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);

    String setterSource1 = "function C.prototype.setA(value) {this.a_ = value}";
    Node setterNode1 = functionRewriter.parseSyntheticCode("test", setterSource1);

    String setterSource2 = "function D.prototype.setA(value) {this.a_ = value}";
    Node setterNode2 = functionRewriter.parseSyntheticCode("test", setterSource2);

    FunctionRewriter rewriter = new FunctionRewriter(null);
    rewriter.process(null, setterNode1);
    rewriter.process(null, setterNode2);

    Node actual1 = setterNode1.getLastChild();
    Node expected1 = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    Node actual2 = setterNode2.getLastChild();
    Node expected2 = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, "JSCompiler_set"),
        new Node(Token.STRING_LITERAL, "a_")
    );

    fail("Expected the same reduction result for both setters, but they are different.");
  }
}