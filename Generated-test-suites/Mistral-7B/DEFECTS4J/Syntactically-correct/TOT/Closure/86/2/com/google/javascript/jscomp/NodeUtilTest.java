import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newEmpty();
    assertFalse(isForIn(nodeNotForIn));

    Node nodeFunction = Node.newFunction("myFunction");
    assertFalse(isForIn(nodeFunction));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newStatement(Node.newExpressionStatement(Node.newIdentifier("x")));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newNumberLiteral(5));
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("myFunction");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionFunction(Node.newIdentifier("myFunction"), Node.newList());
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariable = Node.newVariable("myFunction", Node.newFunctionType(Node.newList(), Node.newList()));
    assertFalse(isFunctionDeclaration(nodeVariable));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newBlock(Node.newList(Node.newFunction("myFunction", Node.newList(), Node.newList())));
    assertTrue(isHoistedFunctionDeclaration(nodeScript.getFirstChild()));

    Node nodeFunction = Node.newFunction("myFunction", Node.newList(), Node.newList());
    Node nodeBlock = Node.newBlock(Node.newList(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeBlock.getFirstChild()));
  }
}