// NodeUtilTest.java - Expert 1
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(); // Create a For-in node
    nodeForIn.setType(Token.FOR);
    nodeForIn.setChildCount(3);

    assertTrue(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(); // Create a statement node
    statementNode.setParent(new Node()); // Set parent to simulate being part of a script, block or label
    statementNode.setType(Token.IF); // Set type to if statement

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(); // Create a function declaration node
    functionNode.setType(Token.FUNCTION);
    functionNode.setParent(new Node()); // Set parent to simulate being part of a script, block or label

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = new Node(); // Create a hoisted function declaration node
    hoistedFunctionNode.setType(Token.FUNCTION);
    hoistedFunctionNode.setParent(new Node(Token.SCRIPT)); // Set parent to a script node

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
  }
}