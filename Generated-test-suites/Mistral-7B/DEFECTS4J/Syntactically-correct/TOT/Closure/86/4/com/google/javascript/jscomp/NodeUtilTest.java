import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TestCode;
import com.google.javascript.jscomp.testing.TypeChecker;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new TestCode().compile("for (var i in [1,2,3]) {}", "test").getRootNode();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new TestCode().compile("for (var i = 0; i < 3; i++) {}", "test").getRootNode();
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new TestCode().compile("if (true) {}", "test").getChild(0);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new TestCode().compile("1 + 1", "test").getRootNode();
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new TestCode().compile("function add(a, b) {}", "test").getChild(0);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new TestCode().compile("var add = function(a, b) {}", "test").getChild(0);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new TestCode().compile("function add() {}", "test").getChild(0);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new TestCode().compile("function foo() {\n"
        + "  function add() {}\n"
        + "}", "test").getChild(1);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationInScript = new TestCode().compile("function add() {}", "test").getRootNode();
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
  }

  @Test
  public void testIsFunctionDeclarationWithTypeChecker() {
    Node nodeFunctionDeclaration = new TestCode().compile("function add(a, b) {}", "test").getChild(0);
    TypeChecker checker = new TypeChecker();
    checker.checkType(nodeFunctionDeclaration, "function");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
  }
}