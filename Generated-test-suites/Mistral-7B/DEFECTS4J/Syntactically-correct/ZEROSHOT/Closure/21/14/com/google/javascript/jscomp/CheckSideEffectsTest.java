package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeType;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  private static AbstractCompiler createMockCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput());
    return compiler;
  }

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = createMockCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = createTestNode();
    checkSideEffects.process(null, root);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    assertEquals(NodeType.IDENT, problemNode.getType());
    assertEquals("s", problemNode.getString());
  }

  @Test
  public void testCheckSideEffectsProtectSideEffectFreeCode() {
    AbstractCompiler compiler = createMockCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = createTestNode();
    checkSideEffects.process(null, root);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(0, problemNodes.size());

    checkSideEffects.protectSideEffects();

    NodeProtector nodeProtector = new NodeProtector();
    nodeProtector.process(null, root);

    Node problemNode = findNodeWithProtection(root, "JSCOMPILER_PRESERVE");
    assertNotNull(problemNode);
  }

  @Test
  public void testCheckSideEffectsDoesNotRemoveVoidStatements() {
    AbstractCompiler compiler = createMockCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = createTestNodeWithVoidStatements();
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsDoesNotRemoveExpressionsInsideForLoop() {
    AbstractCompiler compiler = createMockCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = createTestNodeWithExpressionInsideForLoop();
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsDoesNotRemoveSimpleOperators() {
    AbstractCompiler compiler = createMockCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = createTestNodeWithSimpleOperators();
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = createMockCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = createTestNodeWithProtectedNodes();
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();

    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, root);

    NodeProtector nodeProtector = new NodeProtector();
    nodeProtector.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(3, problemNodes.size());

    for (Node node : problemNodes) {
      Node problemNode = findNodeWithProtection(root, PROTECTOR_FN);
      assertNotNull(problemNode);
      assertEquals(node, problemNode);
    }
  }

  private Node createTestNode() {
    Node root = new Node();
    root.appendChild(new NodeType(Token.BLOCK));
    Node child1 = new NodeType(Token.VAR);
    child1.setString("s");
    Node assign = new NodeType(Token.ASSIGN);
    assign.setRight(new NodeType(Token.STRING_LITERAL).setString("this string is "));
    child1.setChildren(assign);
    root.appendChild(child1);
    Node child2 = new NodeType(Token.ASSIGN);
    child2.setLeft(new NodeType(Token.IDENT).setString("x"));
    child2.setRight(new NodeType(Token.NAME).setString("foo"));
    root.appendChild(child2);
    Node child3 = new NodeType(Token.EXPR_RESULT);
    root.appendChild(child3);
    Node child4 = new NodeType(Token.EXPR_RESULT);
    root.appendChild(child4);
    Node child5 = new NodeType(Token.EXPR_RESULT);
    root.appendChild(child5);
    return root;
  }

  private Node createTestNodeWithVoidStatements() {
    Node root = new Node();
    root.appendChild(new NodeType(Token.BLOCK));
    Node child1 = new NodeType(Token.BLOCK);
    root.appendChild(child1);
    Node child1Statement1 = new NodeType(Token.VOID);
    child1.appendChild(child1Statement1);
    Node child1Statement2 = new NodeType(Token.VOID);
    child1.appendChild(child1Statement2);
    return root;
  }

  private Node createTestNodeWithExpressionInsideForLoop() {
    Node root = new Node();
    root.appendChild(new NodeType(Token.BLOCK));
    Node forNode = new NodeType(Token.FOR);
    root.appendChild(forNode);
    Node init = new NodeType(Token.VAR);
    init.setString("i");
    Node assign = new NodeType(Token.ASSIGN);
    assign.setLeft(init);
    assign.setRight(new NodeType(Token.INT_LITERAL).setInt(0));
    forNode.setInit(assign);
    Node condition = new NodeType(Token.BOOL_LITERAL).setBoolean(true);
    forNode.setCondition(condition);
    Node increment = new NodeType(Token.ASSIGN);
    increment.setLeft(init);
    increment.setRight(new NodeType(Token.ADD));
    Node incrementRight = new NodeType(Token.INT_LITERAL).setInt(1);
    increment.getLastChild().setRight(incrementRight);
    forNode.setIncrement(increment);
    Node body = new NodeType(Token.BLOCK);
    forNode.setBody(body);
    root.appendChild(forNode);
    Node bodyStatement = new NodeType(Token.EXPR_RESULT);
    bodyStatement.setRight(new NodeType(Token.NAME).setString("i"));
    body.appendChild(bodyStatement);
    return root;
  }

  private Node createTestNodeWithSimpleOperators() {
    Node root = new Node();
    root.appendChild(new NodeType(Token.BLOCK));
    Node child1 = new NodeType(Token.VAR);
    child1.setString("x");
    root.appendChild(child1);
    Node child2 = new NodeType(Token.EXPR_RESULT);
    Node child2Left = new NodeType(Token.ADD);
    child2Left.setLeft(child1);
    child2Left.setRight(new NodeType(Token.INT_LITERAL).setInt(1));
    child2.setRight(new NodeType(Token.MINUS));
    Node child2Right = new NodeType(Token.INT_LITERAL).setInt(2);
    child2.getLastChild().setLeft(child2Right);
    root.appendChild(child2);
    return root;
  }

  private Node createTestNodeWithProtectedNodes() {
    Node root = new Node();
    root.appendChild(new NodeType(Token.BLOCK));
    Node child1 = new NodeType(Token.VAR);
    child1.setString("s");
    root.appendChild(child1);
    Node child1Assignment = new NodeType(Token.ASSIGN);
    child1Assignment.setLeft(child1);
    root.appendChild(child1Assignment);
    Node child1AssignmentExpression = new NodeType(Token.EXPR_RESULT);
    child1AssignmentExpression.setRight(new NodeType(Token.STRING_LITERAL).setString("this string is "));
    child1Assignment.setRight(child1AssignmentExpression);

    Node child2 = new NodeType(Token.ASSIGN);
    child2.setLeft(new NodeType(Token.IDENT).setString("x"));
    Node child2Expression = new NodeType(Token.NAME).setString("foo");
    child2.setRight(child2Expression);
    root.appendChild(child2);

    Node child3 = new NodeType(Token.EXPR_RESULT);
    root.appendChild(child3);

    Node child4 = new NodeType(Token.EXPR_RESULT);
    root.appendChild(child4);

    Node child5 = new NodeType(Token.EXPR_RESULT);
    root.appendChild(child5);

    Node protectionNode = new NodeType(Token.CALL);
    protectionNode.setFirstChild(new NodeType(Token.NAME).setString(PROTECTOR_FN));
    protectionNode.setLastChild(child1AssignmentExpression);
    child1Assignment.setRight(protectionNode);

    protectionNode = new NodeType(Token.CALL);
    protectionNode.setFirstChild(new NodeType(Token.NAME).setString(PROTECTOR_FN));
    protectionNode.setLastChild(child2Expression);
    child2.setRight(protectionNode);

    Node protectionNode3 = new NodeType(Token.CALL);
    protectionNode3.setFirstChild(new NodeType(Token.NAME).setString(PROTECTOR_FN));
    protectionNode3.setLastChild(child3);
    child3.setParent(protectionNode3);
    protectionNode3.setParent(child3.getParent());
    child3.getParent().replaceChild(child3, protectionNode3);

    Node protectionNode4 = new NodeType(Token.CALL);
    protectionNode4.setFirstChild(new NodeType(Token.NAME).setString(PROTECTOR_FN));
    protectionNode4.setLastChild(child4);
    child4.setParent(protectionNode4);
    protectionNode4.setParent(child4.getParent());
    child4.getParent().replaceChild(child4, protectionNode4);

    Node protectionNode5 = new NodeType(Token.CALL);
    protectionNode5.setFirstChild(new NodeType(Token.NAME).setString(PROTECTOR_FN));
    protectionNode5.setLastChild(child5);
    child5.setParent(protectionNode5);
    protectionNode5.setParent(child5.getParent());
    child5.getParent().replaceChild(child5, protectionNode5);

    return root;
  }

  private Node findNodeWithProtection(Node node, String protectionFn) {
    if (node.isCall()) {
      Node target = node.getFirstChild();
      if (target.isName() && target.getString().equals(protectionFn)) {
        return node;
      }
      return findNodeWithProtection(node.getLastChild(), protectionFn);
    }
    return null;
  }

  private static class NodeProtector extends AbstractPostOrderCallback
      implements CompilerPass {

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal.traverse(compiler, root, this);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getFirstChild().isName()
          && n.getFirstChild().getString().equals(PROTECTOR_FN)) {
        Node protectionNode = n;
        for (Node child = protectionNode.getFirstChild(); child != null; child = child.getNext()) {
          if (child.isExpressionStatement()) {
            Node problemNode = child;
            Node replacement = new NodeType(Token.EXPR_RESULT);
            Node replacementParent = problemNode.getParent();
            replacementParent.replaceChild(problemNode, replacement);
            replacement.addChildToBack(problemNode);
            problemNode.setParent(replacement);
          }
        }
      }
    }
  }

  private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";
}