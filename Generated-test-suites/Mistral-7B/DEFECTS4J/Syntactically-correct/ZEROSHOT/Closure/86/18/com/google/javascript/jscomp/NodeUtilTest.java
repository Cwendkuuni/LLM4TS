import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nFor = Node.newForStatement("for (var i in arr) {...}");
    assertTrue(NodeUtil.isForIn(nFor));

    Node nIf = Node.newIfStatement("if (cond) {...}");
    assertFalse(NodeUtil.isForIn(nIf));

    Node nFunc = Node.newFunction("func", new Node[0], Node.newBlock(new Node[0]));
    assertFalse(NodeUtil.isForIn(nFunc));
  }

  @Test
  public void testIsStatement() {
    Node nScript = Node.newScript("function foo() {}");
    assertTrue(NodeUtil.isStatement(nScript));

    Node nFuncExpr = Node.newExpressionStatement(Node.newFunction("func", new Node[0], Node.newBlock(new Node[0])));
    assertTrue(NodeUtil.isStatement(nFuncExpr));

    Node nVariable = Node.newVariable("x");
    assertFalse(NodeUtil.isStatement(nVariable));

    Node nIf = Node.newIfStatement("if (cond) {...}");
    assertTrue(NodeUtil.isStatement(nIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nFuncDecl = Node.newFunction("foo", new Node[0], Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nFuncDecl));

    Node nFuncExpr = Node.newExpressionStatement(Node.newFunction("func", new Node[0], Node.newBlock(new Node[0])));
    assertFalse(NodeUtil.isFunctionDeclaration(nFuncExpr));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nScript = Node.newScript("function foo() {}");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nScript));

    Node nRoot = Node.newScript("function foo() {}");
    Node fn = nRoot.getChild(0);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(fn));

    Node nFuncInFunction = Node.newFunction("function bar() { function foo() {} }", new Node[0], Node.newBlock(new Node[0]));
    Node fnInFunction = nFuncInFunction.getChild(1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(fnInFunction));
  }
}