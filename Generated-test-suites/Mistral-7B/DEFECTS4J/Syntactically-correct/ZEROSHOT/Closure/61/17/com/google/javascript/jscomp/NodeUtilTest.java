import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(ASSIGN, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(PRINT, new int[]{}, new int[1]);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(IDENT, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[]{"foo"}, new int[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION, new int[]{}, new int[1]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        FUNCTION, new int[]{"foo"}, new int[0]);
    nodeHoistedFunctionDeclaration.setParent(new Node(SCRIPT, new int[0],
        new int[0]));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node(FUNCTION, new int[]{"foo"},
        new int[0]);
    nodeNotHoistedFunctionDeclaration.setParent(new Node(BLOCK, new int[0],
        new int[0]));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}