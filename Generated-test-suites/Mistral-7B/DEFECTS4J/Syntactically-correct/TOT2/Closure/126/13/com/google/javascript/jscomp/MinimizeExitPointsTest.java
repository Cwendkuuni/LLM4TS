import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  private MinimizeExitPoints minimizer;
  private Node root;

  @Before
  public void setUp() {
    minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
    root = Node.parse("function test() {"
        + "if (true) return;"
        + "for (var i = 0; i < 5; i++) {"
        + "if (i > 3) break;"
        + "}"
        + "}");
  }

  @Test
  public void testMinimizeExits() {
    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("function test() {"
        + "if (true) {"
        + "}"
        + "for (var i = 0; i < 5; i++) {"
        + "if (i > 3)"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_ifBlockWithElse() {
    root = Node.parse("function test() {"
        + "if (true) {"
        + "  if (false) return;"
        + "  alert('This will not be called');"
        + "}"
        + "else {"
        + "  alert('This will be called');"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("function test() {"
        + "if (true)"
        + "{"
        + "}"
        + "else {"
        + "  alert('This will be called');"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_label() {
    root = Node.parse("myLabel: function test() {"
        + "myLabel: {"
        + "  if (true) return;"
        + "  alert('This will not be called');"
        + "}"
        + "}"
        + "test();");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("myLabel: function test() {"
        + "myLabel: {"
        + "}"
        + "}"
        + "test();");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_labelWithBreak() {
    root = Node.parse("myLabel: function test() {"
        + "myLabel: {"
        + "  if (true) {"
        + "    break myLabel;"
        + "  }"
        + "  alert('This will not be called');"
        + "}"
        + "}"
        + "test();");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("myLabel: function test() {"
        + "myLabel: {"
        + "}"
        + "}"
        + "test();");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_throw() {
    root = Node.parse("function test() {"
        + "try {"
        + "  throw new Error('Boom');"
        + "} catch (e) {"
        + "  return; // this should be removed"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("function test() {"
        + "try {"
        + "  throw new Error('Boom');"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_finally() {
    root = Node.parse("function test() {"
        + "try {"
        + "  throw new Error('Boom');"
        + "} finally {"
        + "  return; // this should be removed"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("function test() {"
        + "try {"
        + "  throw new Error('Boom');"
        + "}"
        + "} finally {}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_returnInLoop() {
    root = Node.parse("function test() {"
        + "for (var i = 0; i < 5; i++) {"
        + "  if (i > 3) return;"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("function test() {"
        + "for (var i = 0; i < 5; i++) {"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_continueInLoop() {
    root = Node.parse("function test() {"
        + "for (var i = 0; i < 5; i++) {"
        + "  if (i > 2) continue;"
        + "  alert(i);"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("function test() {"
        + "for (var i = 0; i < 5; i++) {"
        + "  if (i > 2)"
        + "}"
        + "  alert(i);"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_continueWithLabel() {
    root = Node.parse("outer: function test() {"
        + "outer: for (var i = 0; i < 5; i++) {"
        + "  if (i > 2) continue outer;"
        + "  alert(i);"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("outer: function test() {"
        + "outer: for (var i = 0; i < 5; i++) {"
        + "  if (i > 2)"
        + "}"
        + "  alert(i);"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_breakWithLabel() {
    root = Node.parse("outer: function test() {"
        + "outer: for (var i = 0; i < 5; i++) {"
        + "  if (i > 2) break outer;"
        + "  alert(i);"
        + "}"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    Node expected = Node.parse("outer: function test() {"
        + "outer: for (var i = 0; i < 5; i++) {"
        + "  if (i > 2)"
        + "}"
        + "}"
        + "}");

    assertEquals(expected, root);
  }

  @Test
  public void testMinimizeExits_emptyBlock() {
    root = Node.parse("function test() { }");

    NodeTraversal.traverse(minimizer, root, null);

    assertTrue(root.isBlock());
    assertTrue(root.isEmpty());
  }

  @Test
  public void testMinimizeExits_noExits() {
    root = Node.parse("function test() {"
        + "alert('Hello');"
        + "}");

    NodeTraversal.traverse(minimizer, root, null);

    assertTrue(root.isFunctionDeclaration());
    assertFalse(root.hasChildren());
  }

  private void assertEquals(Node expected, Node actual) {
    String expectedStr = expected.toSource();
    String actualStr = actual.toSource();
    assertEquals(expectedStr, actualStr);
  }
}

class NoOpCompilerPass implements CompilerPass {
  @Override
  public void process(Node externs, Node root) {}

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {}
}