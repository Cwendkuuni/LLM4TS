import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{-1, -1, -1});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{-1, -1, -1});
    Node parent = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    parent.addChildToEnd(nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new int[]{}, new int[]{-1, -1, -1});
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createFunctionNode("function foo() {}");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createFunctionNode("var foo = function() {}");
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createFunctionNode("function foo() {}");
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    scriptNode.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = createFunctionNode("function foo() {}", new Node(Token.VAR, new int[]{}, new int[]{-1, -1, -1}));
    assertFalse(isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }

  private Node createFunctionNode(String source) {
    RhinoCommonInitializer.initRhino(null);
    NodeFactory factory = new NodeFactory();
    return factory.compileStringToNode(source);
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckState() {
    Node node = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    NodeUtil.isStatement(node);
    Preconditions.checkState(node.getParent() == null);
  }
}