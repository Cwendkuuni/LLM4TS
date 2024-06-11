import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, new Node[] {}, new Node[] {}, null, null, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"), null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"), null);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(Node.newCall(Node.newName("Math"), "sqrt", Node.newNumber(2.0)), null);
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), new Node[] {}, Node.newBlock(null, null), null, null);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(null, new Node[] {}, Node.newBlock(null, null), null, null), null);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), new Node[] {}, Node.newBlock(null, null), null, Node.newScript(null, null));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = Node.newFunction(Node.newName("myFunction"), new Node[] {}, Node.newBlock(null, null), null, Node.newExpressionStatement(Node.newCall(Node.newName("console"), "log", Node.newString("test")), null));
    assertFalse(isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }
}