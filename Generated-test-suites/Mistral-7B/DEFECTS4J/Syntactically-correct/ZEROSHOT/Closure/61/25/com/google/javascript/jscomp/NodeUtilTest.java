import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, 0);
    nodeForIn.setChild(0, new Node(Identifier, "for"));
    nodeForIn.setChild(1, new Node(Assign, 0));
    nodeForIn.setChild(2, new Node(Identifier, "i"));
    nodeForIn.setChild(3, new Node(Identifier, "arr"));

    Node nodeNotForIn = new Node(If, 0);
    nodeNotForIn.setChild(0, new Node(Identifier, "x"));
    nodeNotForIn.setChild(1, new Node(Token.LTE, 0));
    nodeNotForIn.setChild(2, new Node(NumberLiteral, 5));

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Print, 0);
    nodeStatement.setChild(0, new Node(Identifier, "x"));

    Node nodeExpression = new Node(Plus, 0);
    nodeExpression.setChild(0, nodeStatement);
    nodeExpression.setChild(1, new Node(NumberLiteral, 5));

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateThrowsErrorWhenParentIsNull() {
    Node node = new Node(Identifier, "x");
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Function, 0);
    nodeFunctionDeclaration.setChild(0, new Node(Identifier, "func"));
    nodeFunctionDeclaration.setChild(1, new Node(Block, 0));

    Node nodeFunctionExpression = new Node(Call, 0);
    nodeFunctionExpression.setChild(0, new Node(Identifier, "func"));
    nodeFunctionExpression.setChild(1, new Node(List, 0));

    Node nodeFunctionNotDeclaration = new Node(Print, 0);
    nodeFunctionNotDeclaration.setChild(0, new Node(Identifier, "func"));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionNotDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Function, 0);
    nodeFunctionDeclaration.setChild(0, new Node(Identifier, "func"));
    nodeFunctionDeclaration.setChild(1, new Node(Block, 0));

    Node nodeScript = new Node(Script, 0);
    nodeScript.setChild(0, nodeFunctionDeclaration);

    Node nodeFunctionInFunction = new Node(Function, 0);
    nodeFunctionInFunction.setChild(0, new Node(Identifier, "func2"));
    nodeFunctionInFunction.setChild(1, new Node(Block, 0));
    nodeFunctionInFunction.setParent(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}