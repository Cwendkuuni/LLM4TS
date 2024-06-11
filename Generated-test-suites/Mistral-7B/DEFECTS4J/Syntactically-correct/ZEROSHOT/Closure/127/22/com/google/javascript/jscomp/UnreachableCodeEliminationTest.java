import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractShallowCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    Logger logger = mock(Logger.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    ControlFlowGraph<Node> cfg = cfa.getCfg();

    Node root = Node.newBlock("test");
    root.addChild(Node.newReturn(null)); // return;
    root.addChild(Node.newStatement(Node.newExpressionStatement(
        Node.newCall(null, "alert", Node.newString("unreachable"))))); // alert("unreachable");

    NodeTraversal.traverse(compiler, root, pass);

    DiGraphNode<Node, Branch> rootNode = cfg.getDirectedGraphNode(root);
    assertNull(rootNode);

    ArgumentCaptor<Node> capturedNode = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<AbstractShallowCallback> capturedCallback =
        ArgumentCaptor.forClass(AbstractShallowCallback.class);

    when(compiler.reportCodeChange()).thenReturn(true);

    pass.process(null, root);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isStatement()) {
          assertNull(n);
        }
        if (n.isReturn()) {
          assertFalse(n.hasChildren());
        }
      }
    });

    // Verify that the callback was called with the correct arguments.
    when(compiler.getTraversal()).thenReturn(NodeTraversal.instance());
    NodeTraversal.traverseChangedFunctions(compiler, capturedCallback.capture());
    NodeTraversal nodeTraversal = capturedCallback.getValue();
    Node functionNode = Node.newFunction("test", null, null, null, null);
    Node lastChild = functionNode.getLastChild();
    nodeTraversal.traverse(compiler, root, nodeTraversal);
    List<Node> functionChildren = functionNode.getChildren();
    List<Node> functionLastChildren = functionChildren.get(functionChildren.size() - 1).getChildren();

    // The original root node should have been replaced by the function node.
    Node functionRoot = functionLastChildren.get(0);
    assertFalse(functionRoot.isBlock());
    assertFalse(functionRoot.isStatement());
    assertFalse(functionRoot.isReturn());
    assertFalse(functionRoot.isCall());

    // The return statement and alert statement should have been removed.
    Node expectedReturn = Node.newReturn(null);
    Node expectedAlert = Node.newExpressionStatement(
        Node.newCall(null, "alert", Node.newString("unreachable")));
    List<Node> expectedChildren = new ArrayList<>();
    expectedChildren.add(expectedReturn);
    Node expectedFunctionLastChildren = Node.newBlock("", null, null, null, expectedChildren);

    List<Node> functionChildrenExpected = new ArrayList<>();
    functionChildrenExpected.add(Node.newFunction("test", null, null, null, null));
    functionChildrenExpected.add(expectedFunctionLastChildren);

    Node actualFunctionLastChildren = functionLastChildren.get(1);
    List<Node> actualFunctionLastChildrenChildren = actualFunctionLastChildren.getChildren();

    // The root node should have been replaced by the function node with no
    // children.
    Node actualRoot = actualFunctionLastChildrenChildren.get(0);
    assertFalse(actualRoot.isBlock());
    assertFalse(actualRoot.isStatement());
    assertFalse(actualRoot.isReturn());
    assertFalse(actualRoot.isCall());
    assertArrayEquals(functionChildrenExpected.toArray(new Node[0]),
        actualFunctionLastChildrenChildren.toArray(new Node[0]));
  }

  @Test
  public void testRemoveNoOpStatements() {
    Logger logger = mock(Logger.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock("test");
    root.addChild(Node.newStatement(Node.newExpressionStatement(
        Node.newCall(null, "a.b.MyClass.prototype.propertyName")))); // a.b.MyClass.prototype.propertyName;
    root.addChild(Node.newStatement(Node.newExpressionStatement(Node.newBoolean(true)))); // true;

    NodeTraversal.traverse(compiler, root, pass);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isStatement()) {
          if (n.isCall()) {
            assertNull(n.getFirstChild());
            assertFalse(n.isReturn());
          }
          assertFalse(n.isExpressionStatement());
          assertFalse(n.isBlock());
          assertFalse(n.isIfStatement());
          assertFalse(n.isForStatement());
          assertFalse(n.isWhileStatement());
          assertFalse(n.isDoWhileStatement());
          assertFalse(n.isSwitchStatement());
        }
      }
    });

    Node expectedRoot = Node.newBlock("test");
    expectedRoot.addChild(Node.newStatement(Node.newCall(null, "a.b.MyClass.prototype.propertyName")));

    Node actualRoot = root;
    List<Node> expectedChildren = new ArrayList<>();
    expectedChildren.add(Node.newStatement(Node.newCall(null, "a.b.MyClass.prototype.propertyName")));
    Node expectedFunctionLastChildren = Node.newBlock("", null, null, null, expectedChildren);

    List<Node> functionChildrenExpected = new ArrayList<>();
    functionChildrenExpected.add(Node.newFunction("test", null, null, null, null));
    functionChildrenExpected.add(expectedFunctionLastChildren);

    Node functionNode = Node.newFunction("test", null, null, null, null);
    Node lastChild = functionNode.getLastChild();

    List<Node> functionLastChildren = lastChild.getChildren();

    // The original root node should have been replaced by the function node.
    Node functionRoot = functionLastChildren.get(0);
    assertFalse(functionRoot.isBlock());
    assertFalse(functionRoot.isStatement());
    assertFalse(functionRoot.isReturn());
    assertFalse(functionRoot.isCall());
    assertArrayEquals(expectedChildren.toArray(new Node[0]),
        functionLastChildren.toArray(new Node[0]));
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    Logger logger = mock(Logger.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Node.newBlock("test");
    Node ifStatement = Node.newIfStatement(Node.newBoolean(true), Node.newBlock(null), Node.newBlock(null));
    root.addChild(ifStatement);
    root.addChild(Node.newStatement(Node.newExpressionStatement(
        Node.newCall(null, "break", Node.newIdentifier("label"))))); // break label;
    root.addChild(Node.newLabel("label"));

    NodeTraversal.traverse(compiler, root, pass);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isLabel()) {
          assertNull(n.getNext());
        }
      }
    });

    Node expectedRoot = Node.newBlock("test");
    expectedRoot.addChild(ifStatement);

    List<Node> expectedChildren = new ArrayList<>();
    expectedChildren.add(Node.newIfStatement(Node.newBoolean(true), Node.newBlock(null), Node.newBlock(null)));
    Node expectedFunctionLastChildren = Node.newBlock("", null, null, null, expectedChildren);

    List<Node> functionChildrenExpected = new ArrayList<>();
    functionChildrenExpected.add(Node.newFunction("test", null, null, null, null));
    functionChildrenExpected.add(expectedFunctionLastChildren);

    Node functionNode = Node.newFunction("test", null, null, null, null);
    Node lastChild = functionNode.getLastChild();

    List<Node> functionLastChildren = lastChild.getChildren();

    // The original root node should have been replaced by the function node.
    Node functionRoot = functionLastChildren.get(0);
    assertFalse(functionRoot.isBlock());
    assertFalse(functionRoot.isStatement());
    assertFalse(functionRoot.isReturn());
    assertFalse(functionRoot.isCall());
    assertArrayEquals(expectedChildren.toArray(new Node[0]),
        functionLastChildren.toArray(new Node[0]));
  }
}