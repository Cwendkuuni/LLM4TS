import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil methods.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForStatement("for (i in arr) {}");
    Node nodeIf = Node.newIfStatement(Node.newBooleanLiteral(true));

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript("var x = 1;");
    Node nodeIf = Node.newIfStatement(Node.newBooleanLiteral(true));

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(
        "myFunction", new Node[]{Node.newName("arg")}, Node.newBlock(null));
    Node nodeCall = Node.newName("myFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeCall));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript("function myFunction() {}");
    Node nodeFunction = Node.newFunction(
        "myFunction", new Node[]{Node.newName("arg")}, Node.newBlock(null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunction = Node.newFunction(
        "myFunction", new Node[]{Node.newName("arg")}, Node.newBlock(null));

    Preconditions.checkArgument(nodeFunction.getParent() != null,
        "Node must have a parent.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNoParentScript() {
    Node nodeFunction = Node.newFunction(
        "myFunction", new Node[]{Node.newName("arg")}, Node.newBlock(null));

    Preconditions.checkArgument(nodeFunction.getParent().getType() == Token.SCRIPT,
        "Parent node must be a script node.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNoParentFunction() {
    Node nodeFunction = Node.newFunction(
        "myFunction", new Node[]{Node.newName("arg")}, Node.newBlock(null));

    Preconditions.checkArgument(nodeFunction.getParent().getParent() != null,
        "Parent node of the parent node must be a function node.");
  }
}