package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JsdTestLoader;
import com.google.javascript.rhino.NodeUtil;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExits() throws Exception {
    String jsCode = "function test() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i % 2 === 0) continue;"
        + "    if (i === 5) return;"
        + "    console.log(i);"
        + "  }"
        + "}";

    Compiler compiler = new Compiler();
    Node rootNode = ParserTestUtil.parse(compiler, jsCode);

    MinimizeExitPoints minExitPoints = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, rootNode, minExitPoints);

    IR testIR = compiler.getIR();
    Node testFuncNode = NodeUtil.getFunctionDeclaration(rootNode, "test").getBody();
    Node testFuncBody = testFuncNode.getLastChild();

    Node expectedExitPoint = testFuncBody.getFirstChild();
    Node actualExitPoint = null;
    for (Node node = testFuncBody; node != null; node = node.getNext()) {
      if (node.isReturn()) {
        actualExitPoint = node;
        break;
      }
    }

    assertEquals("Expected return statement to be at the same position.",
        expectedExitPoint, actualExitPoint);
  }

  @Test
  public void testMinimizeExitsWithLabel() throws Exception {
    String jsCode = "label1: function test() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i % 2 === 0) break label1;"
        + "    if (i === 5) continue label1;"
        + "    console.log(i);"
        + "  }"
        + "}";

    Compiler compiler = new Compiler();
    Node rootNode = ParserTestUtil.parse(compiler, jsCode);

    MinimizeExitPoints minExitPoints = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, rootNode, minExitPoints);

    IR testIR = compiler.getIR();
    Node testFuncNode = NodeUtil.getFunctionDeclaration(rootNode, "test").getBody();
    Node testFuncBody = testFuncNode.getLastChild();

    Node expectedExitPoint = testFuncBody.getFirstChild();
    Node actualExitPoint = null;
    for (Node node = testFuncBody; node != null; node = node.getNext()) {
      if (node.isLabel()) {
        String labelName = node.getString();
        if (labelName.equals("label1")) {
          Node labelBlock = node.getLastChild();
          Node labelBlockLastChild = labelBlock.getLastChild();

          if (labelBlockLastChild.isReturn()) {
            actualExitPoint = labelBlockLastChild;
            break;
          }
        }
      }
    }

    assertEquals("Expected return statement to be at the same position.",
        expectedExitPoint, actualExitPoint);
  }

  @Test
  public void testMinimizeExitsInsideTryCatchBlock() throws Exception {
    String jsCode = "function test() {"
        + "  try {"
        + "    if (Math.random() < 0.5) throw new Error('Random error');"
        + "  } catch (e) {"
        + "    if (e.message === 'Random error') {"
        + "      break;"
        + "    } else {"
        + "      console.log('Caught other error.');"
        + "    }"
        + "  }"
        + "}";

    Compiler compiler = new Compiler();
    Node rootNode = ParserTestUtil.parse(compiler, jsCode);

    MinimizeExitPoints minExitPoints = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, rootNode, minExitPoints);

    IR testIR = compiler.getIR();
    Node testFuncNode = NodeUtil.getFunctionDeclaration(rootNode, "test").getBody();
    Node testFuncBody = testFuncNode.getLastChild();

    Node expectedExitPoint = null;
    Node actualExitPoint = null;
    for (Node node = testFuncBody; node != null; node = node.getNext()) {
      if (node.isLabel()) {
        String labelName = node.getString();
        if (labelName.equals("default")) {
          Node labelBlock = node.getLastChild();
          Node labelBlockLastChild = labelBlock.getLastChild();

          if (labelBlockLastChild.isReturn()) {
            expectedExitPoint = labelBlockLastChild;
          }

          if (node.getPrevious() != null
              && node.getPrevious().isCatch()
              && node.getPrevious().getLastChild().isBlock()) {
            Node catchBlock = node.getPrevious().getLastChild();
            Node catchBlockLastChild = catchBlock.getLastChild();
            if (catchBlockLastChild.isBreak()) {
              actualExitPoint = catchBlockLastChild;
            }
          }
        }
      }
    }

    assertEquals("Expected return statement to be replaced by break.",
        expectedExitPoint, actualExitPoint);
  }
}