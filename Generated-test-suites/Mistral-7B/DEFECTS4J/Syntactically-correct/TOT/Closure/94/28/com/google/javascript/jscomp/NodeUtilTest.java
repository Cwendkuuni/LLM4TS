import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createToken;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = NodeFactory.createNode(createToken(Token.FOR), null);
    Node arrayNode = NodeFactory.createNode(createToken(Token.IDENTIFIER, "arr"), null);
    Node statementNode = NodeFactory.createNode(createToken(Token.ASSIGN, null), null);

    Node forNodeWithThreeChildren = NodeFactory.createNode(
        NodeFactory.createNode(createToken(Token.FOR), null), arrayNode, statementNode);

    assertTrue(isForIn(forNodeWithThreeChildren));
    assertFalse(isForIn(NodeFactory.createNode(createToken(Token.FOR), null)));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = NodeFactory.createNode(createToken(Token.SCRIPT), null);
    Node functionNode = NodeFactory.createFunction(createToken(Token.FUNCTION, "foo"), null, null);
    Node statementNode = NodeFactory.createNode(createToken(Token.ASSIGN, null), null);

    Node blockNode = NodeFactory.createNode(createToken(Token.BLOCK), null);
    Node labelNode = NodeFactory.createNode(createToken(Token.LABEL, "label"), null);

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(functionNode));
    assertTrue(isStatement(blockNode));
    assertFalse(isStatement(scriptNode));
    assertFalse(isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createFunction("foo", null, null);

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(NodeFactory.createNode(createToken(Token.FUNCTION, null), null)));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = NodeFactory.createNode(createToken(Token.SCRIPT), null);
    Node functionDeclaration = NodeFactory.createFunction("foo", null, null);

    Node hoistedFunctionDeclaration = NodeFactory.createNode(functionDeclaration, null);
    Node functionDeclarationInFunctionNode = NodeFactory.createNode(functionDeclaration,
        NodeFactory.createNode(createToken(Token.FUNCTION, null), null));

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }
}