import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    NodeUtil.initialize(forNode); // initialize the node
    assertTrue(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT);
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(statementNode);
    NodeUtil.initialize(statementNode); // initialize the node
    NodeUtil.initialize(parent); // initialize the parent
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setProperty("id", "testFunction");
    Node parent = new Node(Token.SCRIPT);
    parent.addChild(functionNode);
    NodeUtil.initialize(functionNode); // initialize the node
    NodeUtil.initialize(parent); // initialize the parent
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setProperty("id", "testFunction");
    Node rootNode = new Node(Token.SCRIPT);
    rootNode.addChild(functionNode);
    NodeUtil.initialize(functionNode); // initialize the node
    NodeUtil.initialize(rootNode); // initialize the parent
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}