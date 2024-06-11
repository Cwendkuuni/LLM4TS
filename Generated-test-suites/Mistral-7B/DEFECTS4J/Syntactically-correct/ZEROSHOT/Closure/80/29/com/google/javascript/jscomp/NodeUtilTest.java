import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;

@RunWith(MockitoJUnitRunner.class)
public class NodeUtilTest {

  private NodeFactory nodeFactory = NodeFactory.getFactory();

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, 0, null);
    Node forNodeWithThreeChildren = createNode(Token.FOR, 0, new Node[] {
        createNode(Token.IDENT, 0, "i"), createNode(Token.KEYWORD, 0, "in"),
        createNode(Token.IDENT, 0, "arr")
    });

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode(Token.SCRIPT, 0, null);
    Node functionNode = createNode(Token.FUNCTION, 0, new Node[] {
        createNode(Token.IDENT, 0, "f"), createNode(Token.LEFT_BRACE, 0, null)
    });

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(functionNode));

    Node expressionNode = createNode(Token.IDENT, 0, "x");

    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(Token.FUNCTION, 0, new Node[] {
        createNode(Token.IDENT, 0, "f"), createNode(Token.LEFT_BRACE, 0, null)
    });

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = createNode(Token.FUNCTION, 0, new Node[] {
        createNode(Token.IDENT, 0, "x"), createNode(Token.LEFT_BRACE, 0, null)
    });

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(Token.SCRIPT, 0, new Node[] {
        createNode(Token.FUNCTION, 0, new Node[] {
            createNode(Token.IDENT, 0, "foo"), createNode(Token.LEFT_BRACE, 0, null)
        })
    });

    Node hoistedFunctionDeclarationNode = createNode(Token.FUNCTION, 0, new Node[] {
        createNode(Token.IDENT, 0, "bar"), createNode(Token.LEFT_BRACE, 0, null)
    });

    Node parentOfHoistedFunctionDeclarationNode = createNode(Token.FUNCTION, 0, new Node[] {
        createNode(Token.IDENT, 0, "bar"),
        hoistedFunctionDeclarationNode, createNode(Token.RIGHT_BRACE, 0, null)
    });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentOfHoistedFunctionDeclarationNode));
  }

  private Node createNode(int type, int lineNumber, Node... children) {
    Preconditions.checkArgument(children == null || children.length >= 0);

    Node node = nodeFactory.createNode(type, lineNumber);
    if (children != null) {
      for (Node child : children) {
        node.addChild(child);
      }
    }

    return node;
  }
}