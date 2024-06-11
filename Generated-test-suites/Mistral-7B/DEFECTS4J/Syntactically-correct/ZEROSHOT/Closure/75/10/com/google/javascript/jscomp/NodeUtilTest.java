import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(FOR, new int[]{}, new Node[3]);
    assertTrue(NodeUtil.isForIn(node));

    Node nodeWithoutThreeChildren = Node.newNode(FOR, new int[]{}, new Node[2]);
    assertFalse(NodeUtil.isForIn(nodeWithoutThreeChildren));

    Node nodeWithWrongType = Node.newNode(IF, new int[]{}, new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeWithWrongType));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode(SCRIPT, new int[]{}, new Node[0]);
    assertTrue(NodeUtil.isStatement(node));

    Node nodeWithParentTypeScript = Node.newNode(IF, new int[]{}, new Node[0]);
    Node parent = Node.newNode(SCRIPT, new int[]{}, new Node[] {nodeWithParentTypeScript});
    assertTrue(NodeUtil.isStatement(nodeWithParentTypeScript));
    assertTrue(NodeUtil.isStatement(node));

    Node nodeWithParentBlock = Node.newNode(BLOCK, new int[]{}, new Node[0]);
    parent = Node.newNode(SCRIPT, new int[]{}, new Node[] {nodeWithParentBlock});
    assertTrue(NodeUtil.isStatement(nodeWithParentBlock));
    assertTrue(NodeUtil.isStatement(node));

    Node nodeWithParentLabel = Node.newNode(LABEL, new int[]{}, new Node[0]);
    parent = Node.newNode(SCRIPT, new int[]{}, new Node[] {nodeWithParentLabel});
    assertTrue(NodeUtil.isStatement(nodeWithParentLabel));
    assertTrue(NodeUtil.isStatement(node));

    Node nodeWithParentFunction = Node.newNode(FUNCTION, new int[]{}, new Node[0]);
    parent = Node.newNode(SCRIPT, new int[]{}, new Node[] {nodeWithParentFunction});
    assertFalse(NodeUtil.isStatement(nodeWithParentFunction));
    assertTrue(NodeUtil.isStatement(node));

    Node nodeWithNullParent = Node.newNode(IF, new int[]{}, new Node[0]);
    assertFalse(NodeUtil.isStatement(nodeWithNullParent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(FUNCTION, new int[] {ID, ID}, new Node[] {Node.newNode(ID, new int[] {ID}, null), Node.newNode(ID, new int[] {ID}, null), Node.newNode(BLOCK, new int[] {}, new Node[0])});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newNode(FUNCTION, new int[] {}, new Node[] {Node.newNode(ID, new int[] {ID}, null), Node.newNode(ID, new int[] {ID}, null), Node.newNode(BLOCK, new int[] {}, new Node[0])});
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));

    Node nodeWithoutName = Node.newNode(FUNCTION, new int[] {}, new Node[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeWithoutName));

    Node nodeWithParentFunction = Node.newNode(FUNCTION, new int[] {}, new Node[0]);
    Node parentFunction = Node.newNode(FUNCTION, new int[] {ID}, new Node[] {Node.newNode(ID, new int[] {ID}, null), Node.newNode(BLOCK, new int[] {}, new Node[0])});
    Node parent = Node.newNode(SCRIPT, new int[] {}, new Node[] {parentFunction});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeWithParentFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newNode(FUNCTION, new int[] {ID}, new Node[] {Node.newNode(ID, new int[] {ID}, null), Node.newNode(BLOCK, new int[] {}, new Node[0])});
    Node parentScript = Node.newNode(SCRIPT, new int[] {}, new Node[0]);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node hoistedFunctionDeclarationInFunction = Node.newNode(FUNCTION, new int[] {ID}, new Node[] {Node.newNode(ID, new int[] {ID}, null), Node.newNode(BLOCK, new int[] {}, new Node[0])});
    Node functionNode = Node.newNode(FUNCTION, new int[] {}, new Node[0]);
    Node parentFunction = Node.newNode(SCRIPT, new int[] {}, new Node[] {functionNode});
    Node parent = Node.newNode(SCRIPT, new int[] {}, new Node[] {parentFunction});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunction));

    Node notFunctionNode = Node.newNode(IF, new int[] {}, new Node[0]);
    Node parentFunction2 = Node.newNode(SCRIPT, new int[] {}, new Node[] {Node.newNode(FUNCTION, new int[] {}, new Node[0]), notFunctionNode});
    Node parent2 = Node.newNode(SCRIPT, new int[] {}, new Node[] {parentFunction2});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }
}