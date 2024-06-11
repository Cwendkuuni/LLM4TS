import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[]{0, 1, 2});
    assertTrue(isForIn(node));

    Node nodeWithWrongType = new Node(Token.IF, new int[]{0, 1, 2});
    assertFalse(isForIn(nodeWithWrongType));

    Node nodeWithWrongChildCount = new Node(Token.FOR, new int[]{0, 1});
    assertFalse(isForIn(nodeWithWrongChildCount));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = new Node(Token.IF, new int[]{0, 1, 2});
    Node parent = new Node(Token.SCRIPT, new int[]{0});
    parent.addChild(node);
    assertTrue(isStatement(node));

    Node nodeWithWrongParentType = new Node(Token.IF, new int[]{0, 1, 2});
    parent = new Node(Token.NUMBER, new int[]{0});
    parent.addChild(nodeWithWrongParentType);
    assertFalse(isStatement(nodeWithWrongParentType));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    functionDeclaration.setProperty("id", "testFunction");
    assertTrue(isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{0});
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    functionDeclaration.setProperty("id", "testFunction");
    scriptNode.addChild(functionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));

    Node functionNodeInsideFunction = new Node(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    Node functionNodeParent = new Node(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    functionNodeParent.addChild(functionNodeInsideFunction);
    assertFalse(isHoistedFunctionDeclaration(functionNodeInsideFunction));
  }
}