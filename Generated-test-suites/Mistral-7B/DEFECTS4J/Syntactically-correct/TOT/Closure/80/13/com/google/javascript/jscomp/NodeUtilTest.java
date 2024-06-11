import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new TestSourceFile("forInStatement.js", "for (var i in array) {}")
        .getTree();
    assertTrue(isForIn(node.lookup("for")));

    node = new TestSourceFile("forStatement.js", "for (var i = 0; i < 10; i++) {}")
        .getTree();
    assertFalse(isForIn(node.lookup("for")));
  }

  @Test
  public void testIsStatement() {
    Node node = new TestSourceFile("statement.js", "if (true) {}")
        .getTree();
    assertTrue(isStatement(node.getChild(0)));

    node = new TestSourceFile("expression.js", "x = 5 + 3")
        .getTree();
    assertFalse(isStatement(node.getChild(0)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new TestSourceFile("functionDeclaration.js", "function foo() {}")
        .getTree();
    assertTrue(isFunctionDeclaration(node));

    node = new TestSourceFile("functionExpression.js", "var foo = function() {}")
        .getTree();
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new TestSourceFile("hoistedFunctionDeclaration.js", "function foo() {}")
        .getTree();
    assertTrue(isHoistedFunctionDeclaration(node));

    node = new TestSourceFile("nonHoistedFunctionDeclaration.js", "function bar() {} if (false) { function bar() {} }")
        .getTree();
    assertFalse(isHoistedFunctionDeclaration(node));
  }
}