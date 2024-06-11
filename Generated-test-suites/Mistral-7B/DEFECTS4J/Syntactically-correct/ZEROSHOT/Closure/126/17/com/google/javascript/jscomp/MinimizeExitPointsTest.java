package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private MinimizeExitPoints minimizer;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getSingleton();
    minimizer = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_SimpleCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { if (true) return; }");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testMinimizeExitPoints_LabelCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { label L: if (true) break L; }");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testMinimizeExitPoints_LoopCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { for (var i = 0; i < 10; i++) { if (true) return; } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testMinimizeExitPoints_IfElseCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { if (true) return; else {} }");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testMinimizeExitPoints_IfElseIfCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() {"
            + " if (true) return;"
            + " else if (true) return;"
            + " else {}"
            + "}");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testMinimizeExitPoints_TryCatchFinallyCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() {"
            + " try {"
            + " if (true) return;"
            + " } catch (e) {"
            + " if (true) return;"
            + " } finally {"
            + " if (true) return;"
            + " }"
            + "}");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }

  @Test
  public void testMinimizeExitPoints_LabeledBreakCase() {
    Node root = NodeUtil.createNodeFromString(
        nodeFactory, "function f() {"
            + " label L: if (true) break L;"
            + "}");
    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    minimizer.process(null, root);
    NodeUtil.traverse(traversal);
    Node result = root.getFirstChild();

    Node expected = NodeUtil.createNodeFromString(
        nodeFactory, "function f() { }");
    assertEquals(expected.toString(), result.toString());
  }
}