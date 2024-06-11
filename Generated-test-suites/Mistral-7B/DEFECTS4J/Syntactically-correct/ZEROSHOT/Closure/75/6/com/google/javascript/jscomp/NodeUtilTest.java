import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null);
    nodeForIn.setChildCount(3);
    Node child1 = new Node(IDENT, "for");
    Node child2 = new Node(ASSIGN, null);
    Node child3 = new Node(VAR, null);
    child2.setChild(0, child1);
    child2.setChild(1, child3);
    nodeForIn.setChild(0, child2);
    nodeForIn.setChild(1, new Node(IDENT, "in"));
    nodeForIn.setChild(2, new Node(IDENT, "array"));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(IDENT, "for");
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(IF, null);
    Node parentScript = new Node(SCRIPT, null);
    parentScript.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(IDENT, "for");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null);
    nodeFunctionDeclaration.setChild(0, new Node(IDENT, "myFunction"));
    Node parentBlock = new Node(BLOCK, null);
    parentBlock.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION, null);
    nodeFunctionExpression.setChild(0, new Node(IDENT, "myFunction"));
    Node parentExpression = new Node(ASSIGN, null);
    parentExpression.setChild(0, new Node(IDENT, "x"));
    parentExpression.setChild(1, nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(FUNCTION, null);
    nodeHoistedFunctionDeclaration.setChild(0, new Node(IDENT, "myFunction"));
    Node parentScript = new Node(SCRIPT, null);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, null);
    nodeFunctionDeclarationInFunction.setChild(0, new Node(IDENT, "myFunction"));
    Node parentFunction = new Node(FUNCTION, null);
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}