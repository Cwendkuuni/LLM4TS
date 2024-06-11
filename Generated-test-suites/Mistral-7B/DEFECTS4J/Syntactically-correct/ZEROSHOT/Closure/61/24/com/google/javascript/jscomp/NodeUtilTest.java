import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 1});

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 1});
    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{0});
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 1, 2});

    Node parentStatement = new Node(Token.BLOCK, new int[]{}, new int[]{0, 1});
    parentStatement.addChildToEnd(nodeStatement);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(parentStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration =
        new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node nodeFunctionExpression =
        new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2, 3});

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot =
        new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node nodeFunctionDeclarationInFunction =
        new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});
    Node nodeFunctionDeclarationInFunction2 =
        new Node(Token.FUNCTION, new int[]{"name"}, new int[]{0, 1, 2});

    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 1, 2});
    parentScript.addChildToEnd(nodeFunctionDeclarationInRoot);

    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    parentFunction.addChildToEnd(nodeFunctionDeclarationInFunction);
    Node parentFunction2 = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 1, 2});
    parentFunction2.addChildToEnd(nodeFunctionDeclarationInFunction2);

    parentFunction.addChildToEnd(parentScript);
    parentFunction2.addChildToEnd(parentFunction);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction2));
  }
}