import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.createJsNode(
        "for (var i in [1, 2, 3]) {}",
        null, null);
    assertTrue(isForIn(node));
  }

  @org.junit.Test
  public void testIsForIn_False() {
    Node node = Context.createJsNode(
        "if (true) {}",
        null, null);
    assertFalse(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = Context.createJsNode(
        "function foo() {}",
        0, null);
    assertTrue(isStatement(node));
  }

  @org.junit.Test
  public void testIsStatement_False() {
    Node node = Context.createJsNode(
        "function foo() {}",
        null, null);
    assertFalse(isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = Context.createJsNode(
        "function foo() {}",
        0, null);
    assertTrue(isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_False() {
    Node node = Context.createJsNode(
        "function foo() {}",
        null, null);
    assertFalse(isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = Context.createJsNode(
        "function foo() {}",
        0, null);
    Node parent = Context.createJsNode(
        "function bar() {}",
        0, null);
    Context ctx = Context.initArthur();
    ctx.setTopLevelObject(parent);
    node.setParent(ctx.globals());
    assertTrue(isHoistedFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = Context.createJsNode(
        "function foo() {}",
        0, null);
    Node parent = Context.createJsNode(
        "function bar() {}",
        0, null);
    Context ctx = Context.initArthur();
    node.setParent(parent);
    assertFalse(isHoistedFunctionDeclaration(node));
  }
}