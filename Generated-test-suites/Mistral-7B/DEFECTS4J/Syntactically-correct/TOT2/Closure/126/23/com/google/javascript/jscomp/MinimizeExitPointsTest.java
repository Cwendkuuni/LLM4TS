import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MinimizeExitPoints.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  Compiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testMinimizeExitPointsBasic() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "if (true) return; "
            + "if (false) return; "
            + "if (true) return 1; "
            + "if (false) return 2; "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "if (true); "
            + "if (false) { } "
            + "if (true) { return 1; } "
            + "if (false) { } "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithElse() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "if (true) return; "
            + "if (false) {"
            + "  return 1;"
            + "} "
            + "if (true) return 2; "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "if (true); "
            + "if (false) { } "
            + "if (true) { return 2; } "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithBreak() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  if (true) break outer;"
            + "  if (false) return; "
            + "  if (true) return; "
            + "  if (false) break outer;"
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  if (true); "
            + "  if (false) { } "
            + "  if (true) { break outer; } "
            + "  if (false) { } "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithContinue() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  for (var i = 0; i < 10; i++) {"
            + "    if (i % 2 == 0) continue outer;"
            + "    if (i == 5) return; "
            + "  } "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  for (var i = 0; i < 10; i++) {"
            + "    if (i % 2 == 0) continue; "
            + "    if (i == 5) break outer; "
            + "  } "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithLabeledBreak() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  for (var i = 0; i < 10; i++) {"
            + "    if (i % 2 == 0) {"
            + "      label inner:"
            + "      if (i == 5) break inner;"
            + "    } "
            + "    if (i == 5) break outer;"
            + "  } "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  for (var i = 0; i < 10; i++) {"
            + "    if (i % 2 == 0) {"
            + "      label inner:"
            + "      if (i == 5) continue inner;"
            + "    } "
            + "    if (i == 5) break outer; "
            + "  } "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithLabeledContinue() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  for (var i = 0; i < 10; i++) {"
            + "    if (i % 2 == 0) continue outer;"
            + "    if (i == 5) break outer;"
            + "  } "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "label outer:"
            + "  for (var i = 0; i < 10; i++) {"
            + "    if (i % 2 == 0) continue; "
            + "    if (i == 5) break outer; "
            + "  } "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithThrow() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "try {"
            + "  throw new Error('Exception');"
            + "  return; "
            + "} catch (e) {}"
            + "return; "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "try {"
            + "  throw new Error('Exception');"
            + "} catch (e) {}"
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithReturnInTry() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "try {"
            + "  return; "
            + "} catch (e) {"
            + "  return; "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "try {"
            + "  return; "
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithFinally() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "try {"
            + "  return; "
            + "} finally {"
            + "  return; "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "try {"
            + "} finally {"
            + "}");
    assertEquals(expected, node);
  }

  @Test
  public void testMinimizeExitPointsWithComplexControlFlow() {
    // Set up test case
    Node node = NodeUtil.createTestNode(
        "function test() {"
            + "if (true) {"
            + "  if (true) {"
            + "    if (true) break; "
            + "  } "
            + "  if (true) return; "
            + "  if (false) continue; "
            + "} "
            + "if (false) {"
            + "  if (true) break; "
            + "  if (true) return; "
            + "  if (false) continue; "
            + "} "
            + "label outer:"
            + "for (var i = 0; i < 10; i++) {"
            + "  if (i == 5) continue outer;"
            + "  if (i % 2 == 0) break outer;"
            + "  if (i % 2 == 1) return; "
            + "} "
            + "}");

    // Run MinimizeExitPoints
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, node);

    // Check result
    Node expected = NodeUtil.createTestNode(
        "function test() {"
            + "if (true) {"
            + "  if (true) {"
            + "    if (true) break; "
            + "  } "
            + "  if (true) {"
            + "    if (false) continue; "
            + "  } "
            + "  if (true) return; "
            + "  if (false) break; "
            + "} "
            + "if (false) {"
            + "  if (true) {"
            + "    if (false) continue; "
            + "  } "
            + "  if (true) return; "
            + "  if (true) break; "
            + "} "
            + "label outer:"
            + "for (var i = 0; i < 10; i++) {"
            + "  if (i == 5) continue outer;"
            + "  if (i % 2 == 0) break; "
            + "  if (i % 2 != 0) break; "
            + "} "
            + "}");
    assertEquals(expected, node);
  }
}