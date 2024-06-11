import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.makeBlock;
import static com.google.javascript.rhino.NodeFactory.makeFunction;
import static com.google.javascript.rhino.NodeFactory.makeScript;
import static com.google.javascript.rhino.NodeFactory.makeLabel;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = NodeUtil.makeForInNode();
    assertTrue(NodeUtil.isForIn(node));

    node = NodeUtil.makeStatementNode();
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = NodeUtil.makeStatementNode();
    assertTrue(NodeUtil.isStatement(node));

    node = NodeUtil.makeExpressionNode();
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createFunctionDeclarationNode();
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = NodeUtil.makeFunctionExpressionNode();
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createHoistedFunctionDeclarationNode();
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = createNonHoistedFunctionDeclarationNode();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  private Node makeStatementNode() {
    Node parent = makeScript(makeBlock(makeLabel("label")));
    return makeStatementNode(parent);
  }

  private Node makeExpressionNode() {
    return makeFunctionExpressionNode();
  }

  private Node makeFunctionExpressionNode() {
    return makeFunction("functionName", new Node[0], makeBlock(makeLabel("label")), null, null);
  }

  private Node makeStatementNode(Node parent) {
    return makeFunction("functionName", new Node[0], makeBlock(makeLabel("label")), null, null).setParent(parent);
  }

  private Node makeForInNode() {
    return NodeFactory.makeForNode(Token.FOR, new Node[3]);
  }

  private Node createFunctionDeclarationNode() {
    Node parent = makeScript(makeBlock(makeLabel("label")));
    return makeFunction("functionName", new Node[0], makeBlock(makeLabel("label")), null, null).setParent(parent);
  }

  private Node createHoistedFunctionDeclarationNode() {
    Node script = makeScript(makeBlock(makeFunction("functionName", new Node[0], makeBlock(makeLabel("label")), null, null)));
    return script.getFirstChild();
  }

  private Node createNonHoistedFunctionDeclarationNode() {
    Node parent = makeBlock(makeLabel("label"));
    Node functionNode = makeFunction("functionName", new Node[0], makeBlock(makeLabel("label")), null, null);
    return functionNode.setParent(parent);
  }
}