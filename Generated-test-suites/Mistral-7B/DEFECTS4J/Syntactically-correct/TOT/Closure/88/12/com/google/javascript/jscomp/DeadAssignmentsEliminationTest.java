package com.google.javascript.jscomp. DeadAssignmentsEliminationTest;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.testtype.ErrorType;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static com.google.javascript.jscomp.NodeUtil.has;
import static com.google.javascript.jscomp.NodeUtil.isAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private TestCompiler testCompiler;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    testCompiler = new TestCompiler(compiler, true, true);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function foo() {\n" +
            "  var a = 1;\n" +
            "  var b = 1;\n" +
            "  a = 2;\n" +
            "  b = a;\n" +
            "  return a;\n" +
            "}";

    Node root = testCompiler.parseAndCompile(jsCode).getRootNode();

    DeadAssignmentsElimination eliminator =
        new DeadAssignmentsElimination(compiler);
    eliminator.process(testCompiler.getExterns(), root);

    Node expected = new Node(TOKEN_RETURN, new Node(TOKEN_NUMBER, 2));

    List<Node> expectedNodes = Arrays.asList(expected);
    List<Node> actualNodes = NodeTraversal.traverse(compiler, root, new NodeOperations.GetFirstChildRecursiveFunction());

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    String jsCode = "function foo() {\n" +
            "  function bar() {\n" +
            "    var a = 1;\n" +
            "    a = 2;\n" +
            "  }\n" +
            "  return bar;\n" +
            "}";

    Node root = testCompiler.parseAndCompile(jsCode).getRootNode();

    DeadAssignmentsElimination eliminator =
        new DeadAssignmentsElimination(compiler);
    eliminator.process(testCompiler.getExterns(), root);

    Node expected = new Node(TOKEN_FUNCTION, new Node(TOKEN_IDENTIFIER, "bar"),
        ImmutableList.<Node>of(),
        new Node(TOKEN_RETURN, new Node(TOKEN_VOID)));

    List<Node> expectedNodes = Arrays.asList(expected);
    List<Node> actualNodes = NodeTraversal.traverse(compiler, root, new NodeOperations.GetFirstChildRecursiveFunction());

    assertEquals(expectedNodes, actualNodes);
  }

  @Test(expected = ErrorType.RuntimeError.class)
  public void testDeadAssignmentsEliminationWithAssignmentBeforeConditional() {
    String jsCode = "function foo() {\n" +
            "  var a = 1;\n" +
            "  if (a = 2) {\n" +
            "    return true;\n" +
            "  } else {\n" +
            "    return false;\n" +
            "  }\n" +
            "}";

    Node root = testCompiler.parseAndCompile(jsCode).getRootNode();

    DeadAssignmentsElimination eliminator =
        new DeadAssignmentsElimination(compiler);
    eliminator.process(testCompiler.getExterns(), root);

    // Assignment before condition should not be eliminated
    List<Node> actualNodes = NodeTraversal.traverse(compiler, root, new NodeOperations.GetFirstChildRecursiveFunction());
    Node assignNode = null;
    for (Node node : actualNodes) {
      if (isAssignmentOp(node) && NodeUtil.isName(node.getFirstChild()) &&
          node.getFirstChild().getString().equals("a")) {
        assignNode = node;
        break;
      }
    }
    assertNotNull(assignNode);
  }

  @Test
  public void testDeadAssignmentsEliminationWithForLoop() {
    String jsCode = "function foo() {\n" +
            "  for (var i = 0; i < 10; i++) {\n" +
            "    var j = i;\n" +
            "    j = i + 1;\n" +
            "  }\n" +
            "}"
            ;

    Node root = testCompiler.parseAndCompile(jsCode).getRootNode();

    DeadAssignmentsElimination eliminator =
        new DeadAssignmentsElimination(compiler);
    eliminator.process(testCompiler.getExterns(), root);

    // The second assignment to j is dead and should be eliminated
    Node forNode = NodeTraversal.findFirstMatchingNode(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == TOKEN_FOR;
      }
    });

    List<Node> actualNodes = NodeTraversal.traverse(compiler, forNode, new NodeOperations.GetFirstChildRecursiveFunction());
    Node secondAssignNode = null;
    Node firstAssignNode = null;
    for (Node node : actualNodes) {
      if (isAssignmentOp(node) && NodeUtil.isName(node.getFirstChild()) &&
          node.getFirstChild().getString().equals("j")) {
        if (secondAssignNode == null) {
          secondAssignNode = node;
        } else {
          firstAssignNode = node;
          break;
        }
      }
    }
    assertNotNull(firstAssignNode);
    assertNotNull(secondAssignNode);

    if (firstAssignNode != null && secondAssignNode != null) {
      // The second assignment should be removed
      assertTrue(firstAssignNode.getParent().getChildren().contains(secondAssignNode));
      assertTrue(firstAssignNode.getParent().getChildren().size() == 1);
      assertFalse(firstAssignNode.getParent().getChildren().contains(secondAssignNode));
    } else {
      fail("Could not find the nodes.");
    }
  }

  @Test(expected = ErrorType.ProgramError.class)
  public void testDeadAssignmentsEliminationWithForInLoop() {
    String jsCode = "function foo() {\n" +
            "  for (var i in []) {\n" +
            "    var j = i;\n" +
            "    j = 1;\n" +
            "  }\n" +
            "}"
            ;

    Node root = testCompiler.parseAndCompile(jsCode).getRootNode();

    DeadAssignmentsElimination eliminator =
        new DeadAssignmentsElimination(compiler);
    eliminator.process(testCompiler.getExterns(), root);

    // The second assignment to j should not be eliminated
    Node forNode = NodeTraversal.findFirstMatchingNode(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return n.getType() == TOKEN_FOR && isForIn(n);
      }
    });

    List<Node> actualNodes = NodeTraversal.traverse(compiler, forNode, new NodeOperations.GetFirstChildRecursiveFunction());
    Node secondAssignNode = null;
    Node firstAssignNode = null;
    for (Node node : actualNodes) {
      if (isAssignmentOp(node) && NodeUtil.isName(node.getFirstChild()) &&
          node.getFirstChild().getString().equals("j")) {
        if (secondAssignNode == null) {
          secondAssignNode = node;
        } else {
          firstAssignNode = node;
          break;
        }
      }
    }
    assertNotNull(firstAssignNode);
    assertNotNull(secondAssignNode);

    if (firstAssignNode != null && secondAssignNode != null) {
      // The second assignment should not be removed
      assertFalse(firstAssignNode.getParent().getChildren().contains(secondAssignNode));
    } else {
      fail("Could not find the nodes.");
    }
  }

  @Test(expected = ErrorType.ProgramError.class)
  public void testDeadAssignmentsEliminationWithDeclaration() {
    String jsCode = "function foo() {\n" +
            "  var a = 1;\n" +
            "  a = 2;\n" +
            "}"
            ;

    Node root = testCompiler.parseAndCompile(jsCode).getRootNode();

    DeadAssignmentsElimination eliminator =
        new DeadAssignmentsElimination(compiler);
    eliminator.process(testCompiler.getExterns(), root);

    // The assignment to a should not be eliminated because it is a VAR
    // declaration and initialization
    Node assignNode = NodeTraversal.findFirstMatchingNode(root, new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        return isAssignmentOp(n);
      }
    });

    assertNotNull(assignNode);
    Node lhs = assignNode.getFirstChild();
    assertNotNull(lhs);
    assertEquals("a", lhs.getString());

    // There should be no error
  }
}