import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setChildCount(3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, null);
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDecl = new Node(Token.FUNCTION, null);
    nodeFunctionDecl.setProperty("id", "testFunction");
    Node parentBlock = new Node(Token.BLOCK, null);
    parentBlock.setChild(0, nodeFunctionDecl);
    assertTrue(isFunctionDeclaration(nodeFunctionDecl));

    Node nodeFunctionExp = new Node(Token.FUNCTION, null);
    nodeFunctionExp.setProperty("id", "testFunction");
    Node parentExpression = new Node(Token.IDENT, null);
    parentExpression.setChild(0, nodeFunctionExp);
    assertFalse(isFunctionDeclaration(nodeFunctionExp));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDecl = new Node(Token.FUNCTION, null);
    nodeFunctionDecl.setProperty("id", "testFunction");
    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, nodeFunctionDecl);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDecl));

    Node nodeFunctionDeclInFunction = new Node(Token.FUNCTION, null);
    nodeFunctionDeclInFunction.setProperty("id", "testFunction");
    Node parentFunction = new Node(Token.FUNCTION, null);
    parentFunction.setChild(0, nodeFunctionDeclInFunction);
    Node parentScript2 = new Node(Token.SCRIPT, null);
    parentScript2.setChild(0, parentFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclInFunction));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckState() {
    Node node = new Node(Token.IF, null);
    Preconditions.checkState(node.getParent() == null);
  }
}