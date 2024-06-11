import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        "myFunction",
        new Node[0],
        Node.SCOPE_GLOBAL,
        Node.newBlock(new Node[0])
    );
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(
            null,
            new Node[0],
            Node.SCOPE_GLOBAL,
            Node.newBlock(new Node[0])
        )
    );
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        "myFunction",
        new Node[0],
        Node.SCOPE_GLOBAL,
        Node.newBlock(new Node[0])
    );
    Node parent = Node.newScript(new Node[0]);
    parent.appendChild(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(
        "myFunction",
        new Node[0],
        Node.SCOPE_GLOBAL,
        Node.newBlock(new Node[0])
    );
    parent = Node.newExpressionStatement(nodeNotHoistedFunctionDeclaration);
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithArguments() {
    Node nodeFunctionDeclarationWithArguments = Node.newFunction(
        "myFunction",
        new Node[]{Node.newIdentifier("arg1"), Node.newIdentifier("arg2")},
        Node.SCOPE_GLOBAL,
        Node.newBlock(new Node[0])
    );
    assertTrue(isFunctionDeclaration(nodeFunctionDeclarationWithArguments));
    assertTrue(nodeFunctionDeclarationWithArguments.getChild(0).getType() == Token.IDENTIFIER);
    assertEquals("arg1", nodeFunctionDeclarationWithArguments.getChild(0).asIdentifier().getName());
    assertTrue(nodeFunctionDeclarationWithArguments.getChild(1).getType() == Token.IDENTIFIER);
    assertEquals("arg2", nodeFunctionDeclarationWithArguments.getChild(1).asIdentifier().getName());
  }
}