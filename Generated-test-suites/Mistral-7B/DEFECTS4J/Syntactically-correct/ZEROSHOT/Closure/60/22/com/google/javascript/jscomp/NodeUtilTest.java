import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = NodeFactory.createToken(FOR, null, null);
    Node child1 = NodeFactory.createToken(IDENT, "for", null);
    Node child2 = NodeFactory.createToken(ASSIGN, ":", null);
    Node child3 = NodeFactory.createToken(IDENT, "i", null);
    node.setChildren(child1, child2, child3);

    assertTrue(NodeUtil.isForIn(node));

    node = NodeFactory.createToken(FUNCTION, null, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = NodeFactory.createToken(SCRIPT, null, null);
    assertTrue(NodeUtil.isStatement(node));

    node = NodeFactory.createToken(IDENT, "x", null);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = NodeFactory.createFunction(null, null, null, null, createBlock(null));
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    node = createFunction(createLabel("foo"), new Object[]{}, createBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = createScript(null);
    Node functionNode = createFunction(createLabel("foo"), new Object[]{}, createBlock(null));
    script.setChildren(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node block = NodeFactory.createBlock(null);
    Node functionNodeInBlock = createFunction(createLabel("foo"), new Object[]{}, block);
    script.setChildren(block);
    block.setChildren(functionNodeInBlock);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInBlock));

    Node functionNodeInFunction = createFunction(NodeFactory.createLabel("bar"), new Object[]{}, null);
    Node functionNodeInFunctionBlock = createFunction(createLabel("foo"), new Object[]{}, createBlock(null));
    Node functionNodeInFunctionParent = createFunction(createLabel("bar"), new Object[]{}, null);
    Node functionNodeInFunctionParentBlock = createBlock(null);
    Node functionNodeInFunctionParentParent = createScript(null);

    functionNodeInFunctionParentBlock.setChildren(functionNodeInFunctionBlock);
    functionNodeInFunctionParent.setChildren(functionNodeInFunctionParentBlock);
    functionNodeInFunctionParent.setParent(functionNodeInFunctionParentParent);
    functionNodeInFunction.setParent(functionNodeInFunctionParent);
    functionNodeInFunctionBlock.setChildren(functionNodeNodeInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
  }
}