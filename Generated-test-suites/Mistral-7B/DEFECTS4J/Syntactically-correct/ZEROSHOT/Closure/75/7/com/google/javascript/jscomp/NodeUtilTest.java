import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createScript;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = NodeFactory.createToken(Token.FOR);
    Node[] children = new Node[3];
    children[0] = NodeFactory.createToken(Token.IDENT, "forVar");
    children[1] = NodeFactory.createToken(Token.KEYWORD, "in");
    children[2] = NodeFactory.createToken(Token.IDENT, "collection");

    forNode.setChildren(children);

    assertTrue(NodeUtil.isForIn(forNode));

    Node invalidForNode = NodeFactory.createToken(Token.FOR);
    assertFalse(NodeUtil.isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = NodeFactory.createScript();
    Node labelNode = NodeFactory.createLabel();
    Node blockNode = NodeFactory.createBlock();

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(blockNode));

    Node invalidStatementNode = NodeFactory.createToken(Token.IDENT, "invalid");

    assertFalse(NodeUtil.isStatement(invalidStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createFunction("func", new Node[0], null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = NodeFactory.createToken(Token.FUNCTION, null, new Node[0], null);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = NodeFactory.createFunction("hoistedFunc", new Node[0], null);
    Node hoistedFunctionParentScript = NodeFactory.createScript();
    Node hoistedFunctionParentFunction = NodeFactory.createFunction("parentFunc", new Node[0], null);

    hoistedFunctionParentFunction.setChildren(new Node[] { hoistedFunctionParentScript, hoistedFunctionNode });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

    Node nonHoistedFunctionNode = NodeFactory.createFunction("nonHoistedFunc", new Node[0], null);
    Node nonHoistedFunctionParentBlock = NodeFactory.createBlock();

    nonHoistedFunctionParentBlock.setChildren(new Node[] { nonHoistedFunctionNode });

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));
  }
}