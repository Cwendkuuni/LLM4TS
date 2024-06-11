import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"forVar"}),
      Node.newNode(Token.KEYWORD, new String[]{"in"}),
      Node.newNode(Token.IDENT, new String[]{"array"})
    });
    assertTrue(isForIn(forNode));

    Node notForNode = Node.newNode(Token.IF, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"condition"})
    });
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"variable"}),
      Node.newNode(Token.NUMBER, new String[]{"10"})
    });
    Node expressionNode = Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"variable"}),
      Node.newNode(Token.OP_PLUS, new int[]{}, new Node[]{
        Node.newNode(Token.NUMBER, new String[]{"5"}),
        Node.newNode(Token.NUMBER, new String[]{"5"})
      })
    });

    Node statementParent = Node.newNode(Token.BLOCK, new int[]{}, new Node[]{statementNode});
    Node expressionParent = Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"variable"}),
      expressionNode
    });

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(statementParent));
    assertFalse(isStatement(expressionNode));
    assertFalse(isStatement(expressionParent));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"functionName"}),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[0])
    });
    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"functionName"}),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[]{
        Node.newNode(Token.RETURN, new int[]{}, new Node[]{
          Node.newNode(Token.NUMBER, new String[]{"10"})
        })
      })
    });

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"functionName"}),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[0])
    });
    Node notHoistedFunctionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"functionName"}),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[0])
    });
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{hoistedFunctionNode});
    Node functionParentNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, new String[]{"outerFunctionName"}),
      Node.newNode(Token.BLOCK, new int[]{}, new Node[]{hoistedFunctionNode})
    });

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionNode));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionNode));
    assertFalse(isHoistedFunctionDeclaration(scriptNode));
    assertTrue(isHoistedFunctionDeclaration(functionParentNode));
  }
}