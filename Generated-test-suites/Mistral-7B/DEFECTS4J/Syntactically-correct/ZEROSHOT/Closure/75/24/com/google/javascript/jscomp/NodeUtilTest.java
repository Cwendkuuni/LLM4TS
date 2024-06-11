import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nFor = Node.newForLoop();
    Node nIf = Node.newIfStatement();

    assertTrue(isForIn(nFor));
    assertFalse(isForIn(nIf));
  }

  @Test
  public void testIsStatement() {
    Node nFunction = Node.newFunction(null, null, null, null, 0);
    Node nExpression = Node.newName("expression");

    assertTrue(isStatement(nFunction));
    assertFalse(isStatement(nExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nFunction = Node.newFunction(null, null, null, null, 0);
    Node nFunctionExpression = Node.newFunctionExpression(null, null, null, null);

    assertTrue(isFunctionDeclaration(nFunction));
    assertFalse(isFunctionDeclaration(nFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newBlock(1);
    Node functionDeclaration = Node.newFunction(null, null, null, null, 0);
    Node hoistedFunctionDeclaration = Node.newFunction(null, null, null, null, 0);
    Node functionExpression = Node.newFunctionExpression(null, null, null, null);

    scriptNode.addChild(functionDeclaration);
    hoistedFunctionDeclaration.setParent(scriptNode);
    scriptNode.addChild(hoistedFunctionDeclaration);
    scriptNode.addChild(functionExpression);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionExpression));
  }
}