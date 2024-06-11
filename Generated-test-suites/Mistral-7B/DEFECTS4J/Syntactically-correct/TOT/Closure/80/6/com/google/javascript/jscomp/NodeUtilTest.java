import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().mkNode("for (var i in arr) {}", Token.FOR);
    assertTrue(isForIn(node));

    node = Context.getCurrentContext().mkNode("for (var i = 0; i < arr.length; i++) {}", Token.FOR);
    assertFalse(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = Context.getCurrentContext().mkBlock("{}");
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().mkNode("5 + 2");
    assertFalse(isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = Context.getCurrentContext().mkNode("function foo(x) {}", Token.FUNCTION);
    assertTrue(isFunctionDeclaration(node));

    node = Context.getCurrentContext().mkNode("5 + function bar(x) {}", Token.ADD, Context.getCurrentContext().mkFunction("bar", Token.FUNCTION, new Node[0]));
    assertFalse(isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().mkBlock("function foo() {}", Token.SCRIPT);
    Node functionNode = scriptNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node blockNode = Context.getCurrentContext().mkNode("function foo() {}", Token.BLOCK);
    functionNode = blockNode.getChild(0);
    assertFalse(isHoistedFunctionDeclaration(functionNode));

    Node functionInFunctionNode = Context.getCurrentContext().mkNode("function foo() { function bar() {} }", Token.FUNCTION);
    Node functionBarNode = functionInFunctionNode.getChild(1);
    assertTrue(isHoistedFunctionDeclaration(functionBarNode));
  }
}