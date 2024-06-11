import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil methods.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 1, 1}, new Node[] {
        Node.newNode(Token.IDENT, "array"),
        Node.newNode(Token.KEYWORD, "in"),
        Node.newNode(Token.IDENT, "item")
    });
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FOR, new int[] {0, 1}, new Node[] {
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.KEYWORD, "="),
        Node.newNode(Token.NUMBER, 0)
    });
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.PRINT, null, new Node[] {
        Node.newNode(Token.STRING, "Hello World")
    });
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IDENT, "x");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1}, new Node[] {
        Node.newNode(Token.IDENT, "myFunction"),
        Node.newNode(Token.BLOCK, null, new Node[0])
    });
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.IDENT, "myFunction", new Node[] {
        Node.newNode(Token.FUNCTION, new int[] {0, 1}, new Node[] {
            Node.newNode(Token.IDENT, "x"),
            Node.newNode(Token.BLOCK, null, new Node[] {
                Node.newNode(Token.RETURN, null, new Node[] {Node.newNode(Token.NUMBER, 42)})
            })
        })
    });
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0}, new Node[] {
        Node.newNode(Token.IDENT, "myFunction")
    });
    Node parentScript = Node.newNode(Token.SCRIPT, null, new Node[0]);
    Node grandParentFunction = Node.newNode(Token.FUNCTION, new int[] {0}, new Node[] {
        Node.newNode(Token.BLOCK, null, new Node[] {parentScript})
    });
    nodeHoistedFunctionDeclaration.setParent(grandParentFunction);
    parentScript.setParent(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInBlock = Node.newNode(Token.FUNCTION, new int[] {0, 1}, new Node[] {
        Node.newNode(Token.IDENT, "myFunction"),
        Node.newNode(Token.BLOCK, null, new Node[0])
    });
    Node parentBlock = Node.newNode(Token.BLOCK, null, new Node[0]);
    Node script = Node.newNode(Token.SCRIPT, null, new Node[] {parentBlock});
    nodeFunctionDeclarationInBlock.setParent(parentBlock);
    parentBlock.setParent(script);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }
}