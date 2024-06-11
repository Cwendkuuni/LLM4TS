import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Context.getCurrentContext().mkToken(Token.FOR, 1, 1, 1);
    Node nodeWithThreeChildren = Context.getCurrentContext().mkToken(Token.FOR, 1, 1, 3);
    Node nodeNotForIn = Context.getCurrentContext().mkToken(Token.IF, 1, 1, 1);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeWithThreeChildren)); // FOR with more than 3 children is not a valid FOR-IN
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = Context.getCurrentContext().mkToken(Token.SCRIPT, 1, 1, 1);
    Node labelNode = Context.getCurrentContext().mkToken(Token.LABEL, 1, 1, 1);
    Node functionNode = Context.getCurrentContext().mkToken(Token.FUNCTION, 1, 1, 1);
    Node expressionNode = Context.getCurrentContext().mkToken(Token.IF, 1, 1, 1);

    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(labelNode));
    assertFalse(isStatement(functionNode)); // A function node can be either part of an expression or a statement, this test checks for statement
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Context.getCurrentContext().mkToken(
        Token.FUNCTION, 1, 1, 1, "myFunction".intern(), Node.NO_ARGS, null);
    Node functionExpression = Context.getCurrentContext().mkToken(
        Token.FUNCTION, 1, 1, 1, null, Node.NO_ARGS, null);

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().mkToken(Token.SCRIPT, 1, 1, 1);
    Node functionDeclaration = Context.getCurrentContext().mkToken(
        Token.FUNCTION, 1, 1, 1, "myFunction".intern(), Node.NO_ARGS, null);
    Node functionDeclarationInsideFunction = Context.getCurrentContext().mkToken(
        Token.FUNCTION, 2, 1, 1, "myFunction".intern(), Node.NO_ARGS, null);

    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInsideFunction));
  }
}