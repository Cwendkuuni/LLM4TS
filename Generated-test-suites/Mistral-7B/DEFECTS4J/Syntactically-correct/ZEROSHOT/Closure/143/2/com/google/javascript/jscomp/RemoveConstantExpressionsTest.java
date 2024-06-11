import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractPostOrder;
import static com.google.javascript.rhino.NodeFactory.createBinaryOperator;
import static com.google.javascript.rhino.NodeFactory.createExpressionStatement;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createIntLiteral;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createStatementList;
import static com.google.javascript.rhino.NodeFactory.createUnaryOperator;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeOps;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    String jsCode = "1 + foo() + bar();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createExpressionStatement(createIdentifier("foo")),
        createExpressionStatement(createIdentifier("bar"))
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.EXPR_STATEMENT) {
            actualNodes.add(statement);
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithNegation() {
    String jsCode = "!1 + foo() + bar();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createExpressionStatement(createUnaryOperator(Node.UNARY_NOT, createIntLiteral(1))),
        createExpressionStatement(createIdentifier("foo")),
        createExpressionStatement(createIdentifier("bar"))
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.EXPR_STATEMENT) {
            actualNodes.add(statement);
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithNoSideEffectSubExpressions() {
    String jsCode = "1 + foo() * bar() + baz();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createExpressionStatement(createIdentifier("foo")),
        createExpressionStatement(createIdentifier("bar")),
        createExpressionStatement(createIdentifier("baz"))
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.EXPR_STATEMENT) {
            actualNodes.add(statement);
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffectSubExpressions() {
    String jsCode = "++x + foo() + bar();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createExpressionStatement(createIdentifier("x")),
        createExpressionStatement(createIdentifier("foo")),
        createExpressionStatement(createIdentifier("bar"))
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.EXPR_STATEMENT) {
            actualNodes.add(statement);
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithMixedSideEffectSubExpressions() {
    String jsCode = "++x + foo() * bar() + baz();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createExpressionStatement(createIdentifier("x")),
        createExpressionStatement(createIdentifier("foo")),
        createExpressionStatement(createIdentifier("bar")),
        createExpressionStatement(createIdentifier("baz"))
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.EXPR_STATEMENT) {
            actualNodes.add(statement);
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithMultipleStatements() {
    String jsCode = "x = 1 + foo() + bar(); y = 2 + foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createStatementList(
            createExpressionStatement(createIdentifier("x")),
            createExpressionStatement(createIdentifier("foo")),
            createExpressionStatement(createIdentifier("bar"))),
        createStatementList(createExpressionStatement(createIdentifier("y")), createIdentifier("foo"))
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.STATEMENT_LIST) {
            actualNodes.add(statement);
            for (Node child : NodeOps.asList(statement.getFirstChild())) {
              if (child.getType() == Token.EXPR_STATEMENT) {
                actualNodes.add(child);
              }
            }
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    assertEquals(expectedNodes, actualNodes);
  }

  @Test
  public void testRemoveConstantExpressionsWithNestedStatements() {
    String jsCode = "if (x > 0) { x = 1 + foo() + bar(); } else { y = 2 + foo(); }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = Parser.parse(new Parser.JavaScriptSource("externs") {});
    Node root = Parser.parse(new Parser.JavaScriptSource(jsCode), externs);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    NodeTraversal.traverse(abstractPostOrder(pass), root, null);

    List<Node> expectedNodes = Lists.newArrayList(
        createStatementList(
            createIfStatement(
                createBinaryOperator(Token.REL_OP, createIdentifier("x"), createIntLiteral(0), Node.CONDITIONAL_AND),
                createStatementList(
                    createExpressionStatement(createIdentifier("x")),
                    createExpressionStatement(createIdentifier("foo")),
                    createExpressionStatement(createIdentifier("bar"))),
                createStatementList(
                    createIdentifier("else"),
                    createStatementList(
                        createExpressionStatement(createIdentifier("y")),
                        createExpressionStatement(createIdentifier("foo")))
                )
            )
        )
    );

    List<Node> actualNodes = Lists.newArrayList();
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.STATEMENT_LIST) {
        for (Node statement : NodeOps.asList(currentNode.getFirstChild())) {
          if (statement.getType() == Token.IF_STATEMENT) {
            actualNodes.add(statement);
            Node condition = statement.getFirstChild();
            Node thenBranch = statement.getLastChild();
            Node elseBranch = null;
            if (statement.getNext() != null) {
              elseBranch = statement.getNext();
            }
            Node conditionChildren = Lists.newArrayList(condition);
            Node thenBranchChildren = NodeOps.getChildren(thenBranch);
            Node elseBranchChildren = NodeOps.getChildren(elseBranch);
            assertEquals(conditionChildren, NodeOps.getChildren(statement.getFirstChild()));
            assertEquals(thenBranchChildren, NodeOps.getChildren(statement.getLastChild().getFirstChild()));
            assertEquals(elseBranchChildren, NodeOps.getChildren(elseBranch));
          }
          currentNode = currentNode.getNext();
        }
      }
    }

    assertEquals(expectedNodes, actualNodes);
  }

  private Node createIfStatement(Node condition, Node thenBranch, Node elseBranch) {
    Node statementList = createStatementList(condition, thenBranch, elseBranch);
    return createStatementList(createKeyword("if"), statementList).getFirstChild();
  }

  private Node createKeyword(String keyword) {
    return createNode(Token.KEYWORD, keyword);
  }

  private Node createStatementList(Node... nodes) {
    List<Node> nodeList = Lists.newArrayList(nodes);
    return createNode(Token.STATEMENT_LIST, createStatementList(nodeList.toArray(new Node[0])));
  }

  private Node createStatementList(List<Node> nodes) {
    Node node = createNode(Token.STATEMENT_LIST, null);
    for (Node nodeToAdd : nodes) {
      Node currentNode = node;
      while (currentNode.getLastChild() != null) {
        currentNode = currentNode.getLastChild();
      }
      currentNode.setLastChild(nodeToAdd);
    }
    return node;
  }
}