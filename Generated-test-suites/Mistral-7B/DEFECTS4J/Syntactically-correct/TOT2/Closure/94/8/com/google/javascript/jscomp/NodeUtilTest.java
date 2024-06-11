import static org.junit.Assert. assertFalse;
import static org.junit.Assert. assertTrue;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(); // Create a Node instance for testing
    nodeForIn.setType(Token.FOR);
    nodeForIn.setChildCount(3);

    assertTrue(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsForIn_InvalidNodeType() {
    Node nodeInvalidType = new Node(); // Create a Node instance for testing
    nodeInvalidType.setType(Token.IF);

    assertFalse(NodeUtil.isForIn(nodeInvalidType));
  }

  @Test
  public void testIsForIn_InvalidChildCount() {
    Node nodeInvalidChildCount = new Node(); // Create a Node instance for testing
    nodeInvalidChildCount.setType(Token.FOR);
    nodeInvalidChildCount.setChildCount(2);

    assertFalse(NodeUtil.isForIn(nodeInvalidChildCount));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(); // Create a Node instance for testing
    nodeStatement.setType(Token.IF); // It's a statement type

    assertTrue(NodeUtil.isStatement(nodeStatement));
  }

  @Test
  public void testIsStatement_InvalidNodeType() {
    Node nodeInvalidType = new Node(); // Create a Node instance for testing
    nodeInvalidType.setType(Token.FOR);

    assertFalse(NodeUtil.isStatement(nodeInvalidType));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(); // Create a Node instance for testing
    nodeFunctionDeclaration.setType(Token.FUNCTION);
    Node statementParent = new Node(); // Create a statement parent node
    statementParent.setType(Token.BLOCK);
    statementParent.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclaration_InvalidNodeType() {
    Node nodeInvalidType = new Node(); // Create a Node instance for testing
    nodeInvalidType.setType(Token.IF);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeInvalidType));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(); // Create a Node instance for testing
    nodeFunctionDeclaration.setType(Token.FUNCTION);
    Node scriptParent = new Node(); // Create a script parent node
    scriptParent.setType(Token.SCRIPT);
    scriptParent.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_InvalidNodeType() {
    Node nodeInvalidType = new Node(); // Create a Node instance for testing
    nodeInvalidType.setType(Token.IF);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInvalidType));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node nodeFunctionDeclaration = new Node(); // Create a Node instance for testing
    nodeFunctionDeclaration.setType(Token.FUNCTION);
    Node blockParent = new Node(); // Create a block parent node
    blockParent.setType(Token.BLOCK);
    blockParent.addChild(nodeFunctionDeclaration);
    Node scriptParent = new Node(); // Create a script parent node
    scriptParent.setType(Token.SCRIPT);
    scriptParent.addChild(blockParent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}