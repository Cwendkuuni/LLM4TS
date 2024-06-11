import static com.google.javascript.jscomp.MinimizeExitPoints.TernaryValue.*;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setup() {
    compiler = new Compiler();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_NoExplicitExit() {
    String sourceCode = "function test() {" +
        "  if (true) {" +
        "    // some code here" +
        "  } else {" +
        "    // some code here" +
        "  }" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code does not contain any explicit exits.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    assertFalse(containsExitPoint(optimizedCode, Token.CONTINUE));
    assertFalse(containsExitPoint(optimizedCode, Token.RETURN));
  }

  @Test
  public void testMinimizeExitPoints_SingleExit_Label() {
    String sourceCode = "function test() {" +
        "  loop1:" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) break loop1;" +
        "    // some code here" +
        "  }" +
        "  // some code here" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code does not contain the explicit exit.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
  }

  @Test
  public void testMinimizeExitPoints_SingleExit_NoLabel() {
    String sourceCode = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) break;" +
        "    // some code here" +
        "  }" +
        "  // some code here" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code does not contain the explicit exit.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
  }

  @Test
  public void testMinimizeExitPoints_MultipleExits_Label() {
    String sourceCode = "function test() {" +
        "  loop1:" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) {" +
        "      if (i === 2) break loop1;" +
        "      // some code here" +
        "    } else {" +
        "      if (i === 5) break loop1;" +
        "      // some code here" +
        "    }" +
        "  }" +
        "  // some code here" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code does not contain the explicit exits.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
  }

  @Test
  public void testMinimizeExitPoints_MultipleExits_NoLabel() {
    String sourceCode = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) {" +
        "      if (i === 2) break;" +
        "      // some code here" +
        "    } else {" +
        "      if (i === 5) break;" +
        "      // some code here" +
        "    }" +
        "  }" +
        "  // some code here" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code does not contain the explicit exits.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
  }

  @Test
  public void testMinimizeExitPoints_SingleExit_Return() {
    String sourceCode = "function test() {" +
        "  if (someCondition) return;" +
        "  // some code here" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
    // Verify the optimized code does not contain any explicit exits.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    assertFalse(containsExitPoint(optimizedCode, Token.CONTINUE));
    assertFalse(containsExitPoint(optimizedCode, Token.RETURN));
  }

  @Test
  public void testMinimizeExitPoints_MultipleExitPoints() {
    String sourceCode = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      if (someCondition) return;" +
        "      continue;" +
        "    }" +
        "    // some code here" +
        "  }" +
        "}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
    // Verify the optimized code does not contain any explicit exits.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    assertFalse(containsExitPoint(optimizedCode, Token.CONTINUE));
    assertFalse(containsExitPoint(optimizedCode, Token.RETURN));
  }

  @Test
  public void testMinimizeExitPoints_EmptyFunction() {
    String sourceCode = "function test() {}";

    Node root = Parser.parse(sourceCode);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = root;
    // Verify the optimized code has only the implied exit.
    assertTrue(implicitExitPoint(optimizedCode));
    // Verify the optimized code does not contain any explicit exits.
    assertFalse(containsExitPoint(optimizedCode, Token.BREAK));
    assertFalse(containsExitPoint(optimizedCode, Token.CONTINUE));
    assertFalse(containsExitPoint(optimizedCode, Token.RETURN));
  }

  private boolean implicitExitPoint(Node node) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.isBlock() && child.getLastChild().isReturn()) {
        return true;
      }
    }
    return false;
  }

  private boolean containsExitPoint(Node node, int exitType) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == exitType) {
        return true;
      }
    }
    return false;
  }
}