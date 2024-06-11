import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelTestingUtils;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Context context = Context.newContext();
    Node n = context.mkToken(Token.FOR, "for (var i = 0; i < 10; i++) { }");
    assertTrue(isForIn(n));

    n = context.mkToken(Token.IF, "if (true) { for (var i = 0; i < 10; i++) { } }");
    assertFalse(isForIn(n));

    n = context.mkToken(Token.VAR, "for (var i in ['a', 'b']) { }");
    assertTrue(isForIn(n));

    n = context.mkToken(Token.RETURN, "function foo() { for (var i in ['a', 'b']) { } }");
    assertFalse(isForIn(n));
  }

  @org.junit.Test
  public void testIsStatement() {
    Context context = Context.newContext();
    Node n = context.mkToken(Token.FOR, "for (var i = 0; i < 10; i++) { }");
    assertTrue(isStatement(n));

    n = context.mkToken(Token.FUNCTION, "function foo() { }");
    assertTrue(isStatement(n));

    n = context.mkToken(Token.IF, "if (true) { }");
    assertTrue(isStatement(n));

    n = context.mkToken(Token.RETURN, "return 42;");
    assertFalse(isStatement(n));

    n = context.mkToken(Token.VAR, "var x = 42;");
    assertFalse(isStatement(n));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Context context = Context.newContext();
    Node n = context.mkToken(Token.FUNCTION, "function foo() {}");
    assertTrue(isFunctionDeclaration(n));

    n = context.mkToken(Token.FUNCTION, "function foo(x) {}");
    assertTrue(isFunctionDeclaration(n));

    n = context.mkToken(Token.IF, "if (true) function foo() {}", TopLevelTestingUtils.TOP_LEVEL_CONTEXT);
    assertFalse(isFunctionDeclaration(n));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Context context = Context.newContext();
    Node n = context.mkToken(Token.FUNCTION, "function foo() {}", TopLevelTestingUtils.TOP_LEVEL_CONTEXT);
    assertFalse(isHoistedFunctionDeclaration(n));

    n = context.mkToken(Token.FUNCTION, "function foo() {}", Context.newBlockContext(null, TopLevelTestingUtils.TOP_LEVEL_CONTEXT).getParent());
    assertTrue(isHoistedFunctionDeclaration(n));

    n = context.mkToken(Token.FUNCTION, "function foo(x) {}", TopLevelTestingUtils.TOP_LEVEL_CONTEXT);
    assertFalse(isHoistedFunctionDeclaration(n));

    n = context.mkToken(Token.FUNCTION, "function foo(x) {}", Context.newBlockContext(null, TopLevelTestingUtils.TOP_LEVEL_CONTEXT).getParent());
    assertTrue(isHoistedFunctionDeclaration(n));
  }
}