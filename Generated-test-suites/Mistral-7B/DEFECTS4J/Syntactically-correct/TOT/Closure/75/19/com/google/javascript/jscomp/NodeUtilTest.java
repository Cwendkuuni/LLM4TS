import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{-1, -1, -1}, null);
    assertFalse("Not a FOR-IN loop", isForIn(forNode));

    Node validForInNode = new Node(Token.FOR, new int[]{0, 2, 1}, null);
    assertTrue("FOR-IN loop", isForIn(validForInNode));
  }

  @Test
  public void testIsStatement() {
    Node expressionNode = new Node(Token.IDENT, new int[]{-1, -1}, null);
    assertFalse("Not a statement", isStatement(expressionNode));

    Node statementNode = new Node(Token.IF, new int[]{0, 1, 2}, null);
    Node ifParent = new Node(Token.SCRIPT, new int[]{0}, new Node[]{statementNode});
    assertTrue("Statement", isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new int[]{-1, -1, -1}, null);
    Node functionParent = new Node(Token.SCRIPT, new int[]{0}, new Node[]{functionNode});
    assertFalse("Not a function declaration", isFunctionDeclaration(functionNode));

    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{0, 1, 2}, new Node[]{
        new Node(Token.IDENT, new int[]{-1, -1}, null),
        new Node(Token.BLOCK, new int[]{-1}, null)
    });
    Node functionDeclarationParent = new Node(Token.SCRIPT, new int[]{0}, new Node[]{functionDeclarationNode});
    assertTrue("Function declaration", isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{0, 1, 2}, new Node[]{
        new Node(Token.IDENT, new int[]{-1, -1}, null),
        new Node(Token.BLOCK, new int[]{-1}, null)
    });
    Node hoistedFunctionDeclarationParent = new Node(Token.SCRIPT, new int[]{0}, new Node[]{
        hoistedFunctionDeclarationNode
    });
    assertTrue("Hoisted function declaration", isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{0, 1, 2}, new Node[]{
        new Node(Token.IDENT, new int[]{-1, -1}, null),
        new Node(Token.BLOCK, new int[]{-1}, null)
    });
    Node functionParent = new Node(Token.SCRIPT, new int[]{0}, new Node[]{
        new Node(Token.VAR, new int[]{2, 0}, null),
        nonHoistedFunctionDeclarationNode
    });
    assertFalse("Not a hoisted function declaration", isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}