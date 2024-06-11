import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class NodeUtilTest {

  public static class IsForInTest {

    @Test
    public void testIsForIn() {
      Node forNode = createNode(Token.FOR);
      Node forNodeWithThreeChildren = createNode(Token.FOR, createNode(Token.IDENT, "for"), createNode(Token.ASSIGN), createNode(Token.IDENT, "in"), createNode(Token.LEFT_PAREN), createNode(Token.IDENT, "array"), createNode(Token.RIGHT_PAREN));
      assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
      assertFalse(NodeUtil.isForIn(forNode));
    }

    @Test(expected = NullPointerException.class)
    public void testIsForInWithNullInput() {
      assertFalse(NodeUtil.isForIn(null));
    }

    @Test
    public void testIsForInWithEmptyNode() {
      Node emptyNode = createNode();
      assertFalse(NodeUtil.isForIn(emptyNode));
    }
  }

  public static class IsStatementTest {

    @Test
    public void testIsStatement() {
      Node statementNode = createNode(Token.PRINT, createNode(Token.STRING_LITERAL, "Hello, World!"));
      assertTrue(NodeUtil.isStatement(statementNode));
    }

    @Test
    public void testIsStatementWithStatementNodeWithUnexpectedParent() {
      Node parentNode = createNode(Token.IF);
      Node statementNode = createNode(Token.PRINT, createNode(Token.STRING_LITERAL, "Hello, World!"));
      Node compositeNode = createNode(Token.SEQUENCE, parentNode, statementNode);
      assertFalse(NodeUtil.isStatement(statementNode));
    }

    @Test(expected = NullPointerException.class)
    public void testIsStatementWithNullInput() {
      assertFalse(NodeUtil.isStatement(null));
    }
  }

  public static class IsFunctionDeclarationTest {

    @Test
    public void testIsFunctionDeclarationWithFunctionNodeWithName() {
      Node functionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionName"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsFunctionDeclarationWithFunctionNodeWithoutName() {
      Node functionNode = createNode(Token.FUNCTION);
      assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test(expected = NullPointerException.class)
    public void testIsFunctionDeclarationWithNullInput() {
      assertFalse(NodeUtil.isFunctionDeclaration(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFunctionDeclarationWithInvalidNode() {
      Node invalidNode = createNode(Token.NUMBER, 42);
      assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
    }
  }

  public static class IsHoistedFunctionDeclarationTest {

    @Test
    public void testIsHoistedFunctionDeclarationWithFunctionDeclarationInScopeRoot() {
      Node scriptNode = createNode(Token.SCRIPT);
      Node hoistedFunctionDeclaration = createNode(Token.FUNCTION, createNode(Token.IDENT, "hoistedFunction"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      Node functionDeclarationInFunction = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionInFunction"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      Node hoistedFunctionDeclarationInFunction = createNode(Token.SEQUENCE, scriptNode, hoistedFunctionDeclaration, functionDeclarationInFunction);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithFunctionDeclarationNotInScopeRoot() {
      Node functionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "function"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      Node statementNode = createNode(Token.PRINT, createNode(Token.STRING_LITERAL, "Hello, World!"));
      Node scriptNode = createNode(Token.SCRIPT);
      Node functionDeclarationInFunction = createNode(Token.SEQUENCE, scriptNode, functionNode, statementNode);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test(expected = NullPointerException.class)
    public void testIsHoistedFunctionDeclarationWithNullInput() {
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithFunctionDeclarationNotDirectlyUnderAScriptOrFunctionNode() {
      Node scriptNode = createNode(Token.SCRIPT);
      Node functionDeclarationInFunction = createNode(Token.FUNCTION, createNode(Token.IDENT, "function"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      Node functionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionInFunction"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      Node hoistedFunctionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "hoistedFunction"), createNode(Token.LEFT_PAREN), createNode(Token.RIGHT_PAREN), createNode(Token.RETURN, createNode(Token.VOID)));
      Node compositeNode = createNode(Token.SEQUENCE, hoistedFunctionNode, functionDeclarationInFunction, functionNode);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
  }
}