import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new TestSourceFile("testIsForIn.js", "{for (var i in [1, 2, 3]) {}}").getAST();
    assertTrue(isForIn(node));

    node = new TestSourceFile("testIsForInFalse.js", "{for (var i = 0; i < 3; i++) {}}").getAST();
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new TestSourceFile("testIsStatement.js", "function foo() {}").getAST();
    assertFalse(isStatement(node));

    node = new TestSourceFile("testIsStatementTrue.js", "{}").getAST();
    assertTrue(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new TestSourceFile("testIsFunctionDeclaration.js", "function foo() {}").getAST();
    assertTrue(isFunctionDeclaration(node));

    node = new TestSourceFile("testIsFunctionExpression.js", "(function foo() {})()").getAST();
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new TestSourceFile("testIsHoistedFunctionDeclaration.js", "function foo() {}").getAST();
    assertFalse(isHoistedFunctionDeclaration(node));

    node = new TestSourceFile("testIsHoistedFunctionDeclarationTrue.js", "function foo() {} function bar() {}").getAST();
    assertTrue(isHoistedFunctionDeclaration(node));
  }
}