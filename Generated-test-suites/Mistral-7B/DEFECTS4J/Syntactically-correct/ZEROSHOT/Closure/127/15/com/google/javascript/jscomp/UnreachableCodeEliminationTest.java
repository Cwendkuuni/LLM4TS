package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.JsParser;
import com.google.javascript.jscomp.test.TestUtils;

import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private static final Logger logger = Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    JsParser parser = new JsParser(options);

    String code1 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('reachable');" +
                    "}";
    String code2 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('unreachable');" +
                    "}";
    String code3 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('reachable');" +
                    "  return;" +
                    "}";
    String code4 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('reachable');" +
                    "  return 5;" +
                    "}";
    String code5 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('reachable');" +
                    "  break; // breaks out of a switch statement" +
                    "}";

    Node node1 = parser.parse(code1, null, null);
    Node node2 = parser.parse(code2, null, null);
    Node node3 = parser.parse(code3, null, null);
    Node node4 = parser.parse(code4, null, null);
    Node node5 = parser.parse(code5, null, null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);

    CompilerPassManager manager = new CompilerPassManager(compiler, null);
    manager.addPass(pass);

    manager.runPasses(node1);
    Node actual1 = node1;
    Node expected1 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-1.js");
    TestUtils.assertTreeEquals(logger, expected1, actual1);

    manager.runPasses(node2);
    Node actual2 = node2;
    Node expected2 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-2.js");
    TestUtils.assertTreeEquals(logger, expected2, actual2);

    manager.runPasses(node3);
    Node actual3 = node3;
    Node expected3 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-3.js");
    TestUtils.assertTreeEquals(logger, expected3, actual3);

    manager.runPasses(node4);
    Node actual4 = node4;
    Node expected4 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-4.js");
    TestUtils.assertTreeEquals(logger, expected4, actual4);

    manager.runPasses(node5);
    Node actual5 = node5;
    Node expected5 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-5.js");
    TestUtils.assertTreeEquals(logger, expected5, actual5);

    // Testing for dead code elimination after returns with side effects.
    String code6 = "function foo() {" +
                    "  if (true) {" +
                    "    return 5;" +
                    "  }" +
                    "  alert('unreachable');" +
                    "}";

    Node node6 = parser.parse(code6, null, null);
    manager.runPasses(node6);
    Node actual6 = node6;
    Node expected6 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-6.js");
    TestUtils.assertTreeEquals(logger, expected6, actual6);

    // Testing for dead code elimination with side effect statements.
    String code7 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  var x = 1;" +
                    "  alert('unreachable');" +
                    "}";

    Node node7 = parser.parse(code7, null, null);
    manager.runPasses(node7);
    Node actual7 = node7;
    Node expected7 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-7.js");
    TestUtils.assertTreeEquals(logger, expected7, actual7);

    // Testing for dead code elimination with no op statements.
    String code8 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  true;" +
                    "  alert('unreachable');" +
                    "}";

    Node node8 = parser.parse(code8, null, null);
    manager.runPasses(node8);
    Node actual8 = node8;
    Node expected8 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-8.js");
    TestUtils.assertTreeEquals(logger, expected8, actual8);

    // Testing for dead code elimination with no op statements and side effects.
    String code9 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  true;" +
                    "  var x = 1;" +
                    "  alert('unreachable');" +
                    "}";

    Node node9 = parser.parse(code9, null, null);
    manager.runPasses(node9);
    Node actual9 = node9;
    Node expected9 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-9.js");
    TestUtils.assertTreeEquals(logger, expected9, actual9);

    // Testing for dead code elimination with conditional branches.
    String code10 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  if (false) {" +
                    "    alert('unreachable');" +
                    "  }" +
                    "}";

    Node node10 = parser.parse(code10, null, null);
    manager.runPasses(node10);
    Node actual10 = node10;
    Node expected10 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-10.js");
    TestUtils.assertTreeEquals(logger, expected10, actual10);

    // Testing for dead code elimination with unconditional branches.
    String code11 = "function foo() {" +
                    "  if (true) {" +
                    "    return;" +
                    "  }" +
                    "  alert('unreachable');" +
                    "  return;" +
                    "}";

    Node node11 = parser.parse(code11, null, null);
    manager.runPasses(node11);
    Node actual11 = node11;
    Node expected11 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-11.js");
    TestUtils.assertTreeEquals(logger, expected11, actual11);

    // Testing for dead code elimination with nested unconditional branches.
    String code12 = "function foo() {" +
                    "  if (true) {" +
                    "    if (true) {" +
                    "      return;" +
                    "    }" +
                    "    alert('unreachable');" +
                    "  }" +
                    "  return;" +
                    "}";

    Node node12 = parser.parse(code12, null, null);
    manager.runPasses(node12);
    Node actual12 = node12;
    Node expected12 = TestUtils.getExpectedNode("UnreachableCodeEliminationTest-12.js");
    TestUtils.assertTreeEquals(logger, expected12, actual12);
  }
}