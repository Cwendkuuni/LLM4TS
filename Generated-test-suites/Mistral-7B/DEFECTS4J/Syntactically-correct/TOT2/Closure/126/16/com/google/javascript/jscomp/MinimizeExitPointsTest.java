import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MinimizeExitPoints.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testMinimizeExitPointsSimple() {
    String source = "function f() {" +
        "  if (true) return;" +
        "  if (false) return;" +
        "  alert('Hello');" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  if (true);" +
        "  if (false);" +
        "  alert('Hello');" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithElse() {
    String source = "function f() {" +
        "  if (true) return;" +
        "  else if (false) return;" +
        "  alert('Hello');" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  if (true);" +
        "  else {" +
        "    alert('Hello');" +
        "  }" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithFunction() {
    String source = "function f() {" +
        "  if (true) return function() {};" +
        "  alert('Hello');" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  if (true);" +
        "  alert('Hello');" +
        "  function g() {}" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    String source = "function f() {" +
        "  myLabel: for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) break myLabel;" +
        "    alert('Hello');" +
        "  }" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  myLabel: for (var i = 0, j = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      j = 1;" +
        "    }" +
        "    if (j === 0) {" +
        "      alert('Hello');" +
        "      break;" +
        "    }" +
        "  }" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithContinue() {
    String source = "function f() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) continue;" +
        "    alert('Hello');" +
        "  }" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) continue;" +
        "  }" +
        "  alert('Hello');" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithBreak() {
    String source = "function f() {" +
        "  myLabel: for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) break myLabel;" +
        "    alert('Hello');" +
        "  }" +
        "}"
        + "function main() {" +
        "  f();" +
        "  alert('World');" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  myLabel: for (var i = 0, j = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      j = 1;" +
        "      continue myLabel;" +
        "    }" +
        "    alert('Hello');" +
        "  }" +
        "}"
        + "function main() {" +
        "  f();" +
        "  alert('World');" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyBlock() {
    String source = "function f() {" +
        "  if (true) return;" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyIfBlock() {
    String source = "function f() {" +
        "  if (true) {" +
        "  }" +
        "  return;" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyElseBlock() {
    String source = "function f() {" +
        "  if (true) {" +
        "  }" +
        "  else {" +
        "  }" +
        "  return;" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  if (true)" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyElseIfBlock() {
    String source = "function f() {" +
        "  if (true) {" +
        "  }" +
        "  else if (false) {" +
        "  }" +
        "  return;" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  if (true)" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyTryCatchBlock() {
    String source = "function f() {" +
        "  try {" +
        "  }" +
        "  catch (e) {" +
        "  }" +
        "  return;" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  try {" +
        "  }" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyFinallyBlock() {
    String source = "function f() {" +
        "  try {" +
        "  }" +
        "  finally {" +
        "  }" +
        "  return;" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  try {" +
        "  }" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyForLoop() {
    String source = "function f() {" +
        "  for (var i = 0; i < 1; i++) {" +
        "    if (true) return;" +
        "  }" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  for (var i = 0; i < 1; i++) {}" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyWhileLoop() {
    String source = "function f() {" +
        "  while (true) {" +
        "    if (true) return;" +
        "  }" +
        "}" +
        "function main() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  while (true) {}" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyDoWhileLoop() {
    String source = "function f() {" +
        "  do {" +
        "    if (true) return;" +
        "  } while (false);" +
        "}" +
        "function main() {" +
        "  f();" +
        "}" +
        "function main2() {" +
        "  f();" +
        "}" +
        "function main3() {" +
        "  f();" +
        "}" +
        "function main4() {" +
        "  f();" +
        "}" +
        "function main5() {" +
        "  f();" +
        "}" +
        "function main6() {" +
        "  f();" +
        "}" +
        "function main7() {" +
        "  f();" +
        "}" +
        "function main8() {" +
        "  f();" +
        "}" +
        "function main9() {" +
        "  f();" +
        "}" +
        "function main10() {" +
        "  f();" +
        "}" +
        "function main11() {" +
        "  f();" +
        "}" +
        "function main12() {" +
        "  f();" +
        "}" +
        "function main13() {" +
        "  f();" +
        "}" +
        "function main14() {" +
        "  f();" +
        "}" +
        "function main15() {" +
        "  f();" +
        "}";

    Node root = NodeUtil.parseJs(source, null, compiler);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    Node expected = nodeFactory.parse("function f() {" +
        "  do {" +
        "  } while (false);" +
        "}")[0];

    assertCodeEquals(expected, root);
  }

  private void assertCodeEquals(Node expected, Node actual) {
    String expectedCode = NodeUtil.printNode(expected);
    String actualCode = NodeUtil.printNode(actual);

    assertEquals(expectedCode, actualCode);
  }
}