import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.AST_CALL_EXPR;
import static com.google.javascript.rhino.NodeType.AST_EXPR_RESULT;
import static com.google.javascript.rhino.NodeType.AST_IDENT;
import static com.google.javascript.rhino.NodeType.AST_LITERAL;
import static com.google.javascript.rhino.NodeType.AST_LOGICAL_OR_EXPR;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressions() {
    String input = "function foo() { return 1; } function bar() { return 2; } "
        + "function baz() { return 3; } "
        + "function qux(x) { return x + foo() + bar() + baz(); }";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node externs = new Node(Token.PROGRAM, null);
    Node root = new Node(Token.BLOCK, null);

    Node program = new Node(Token.PROGRAM, null);
    program.addChild(new Node(Token.DECLARATIONS, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "foo"), null, null, null, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "bar"), null, null, null, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "baz"), null, null, null, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "qux"),
        new Node(Token.FORMAL_PARAMETERS, null),
        new Node(Token.FUNCTION_BODY, null), null));

    Node declarations = new Node(Token.DECLARATIONS, null);
    Node body = new Node(Token.BLOCK, null);
    Node statements = new Node(Token.STATEMENTS, null);

    Node quxFunc = program.getLastChild();
    Node functionBody = quxFunc.getLastChild();

    body.addChild(statements);
    declarations.addChild(body);
    program.getLastChild().addChild(declarations);

    Node inputNode = new Node(Token.PROGRAM, null);
    inputNode.addChild(program);

    Node rootCopy = new Node(Token.BLOCK, null);
    Node copyDeclarations = new Node(Token.DECLARATIONS, null);
    Node copyBody = new Node(Token.BLOCK, null);
    Node copyStatements = new Node(Token.STATEMENTS, null);

    traverse(null, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.PROGRAM) {
          rootCopy.addChild(copyDeclarations);
          copyDeclarations.addChild(copyBody);
        }
        if (node.getType() == Token.DECLARATIONS) {
          copyDeclarations.addChild(node.getFirstChild());
        }
        if (node.getType() == Token.BLOCK) {
          copyBody.addChild(node.getFirstChild());
        }
        if (node.getType() == Token.STATEMENTS) {
          copyStatements.addChild(node.getFirstChild());
        }
      }
    });

    compiler.compile(externs, inputNode, root, options);

    List<Node> originalSideEffectNodes = Lists.newArrayList();
    Node quxCall = getCallExpression(copyStatements, "qux");
    Node quxCallBody = quxCall.getLastChild();

    traverse(null, quxCallBody, new GatherSideEffectNodesCallback(
        originalSideEffectNodes));

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(externs, inputNode);

    List<Node> simplifiedSideEffectNodes = Lists.newArrayList();
    Node simplifiedQuxCallBody = getCallExpression(copyStatements, "qux")
        .getLastChild();

    traverse(null, simplifiedQuxCallBody, new GatherSideEffectNodesCallback(
        simplifiedSideEffectNodes));

    assertTrue(originalSideEffectNodes.size() > 0);
    assertTrue(simplifiedSideEffectNodes.size() == 0);
  }

  @Test
  public void testNoSideEffectExpressionsRemoved() {
    String input = "function foo() { return 1; } function bar() { return 2; } "
        + "function qux(x) { return x + foo() + bar(); }";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    Node externs = new Node(Token.PROGRAM, null);
    Node root = new Node(Token.BLOCK, null);

    Node program = new Node(Token.PROGRAM, null);
    program.addChild(new Node(Token.DECLARATIONS, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "foo"), null, null, null, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "bar"), null, null, null, null));
    program.addChild(new Node(Token.FUNCTION_DECLARATION, null,
        new Node(Token.FUNCTION_NAME, "qux"),
        new Node(Token.FORMAL_PARAMETERS, null),
        new Node(Token.FUNCTION_BODY, null), null));

    Node declarations = new Node(Token.DECLARATIONS, null);
    Node body = new Node(Token.BLOCK, null);
    Node statements = new Node(Token.STATEMENTS, null);

    Node quxFunc = program.getLastChild();
    Node functionBody = quxFunc.getLastChild();

    body.addChild(statements);
    declarations.addChild(body);
    program.getLastChild().addChild(declarations);

    Node inputNode = new Node(Token.PROGRAM, null);
    inputNode.addChild(program);

    Node rootCopy = new Node(Token.BLOCK, null);
    Node copyDeclarations = new Node(Token.DECLARATIONS, null);
    Node copyBody = new Node(Token.BLOCK, null);
    Node copyStatements = new Node(Token.STATEMENTS, null);

    traverse(null, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.PROGRAM) {
          rootCopy.addChild(copyDeclarations);
          copyDeclarations.addChild(copyBody);
        }
        if (node.getType() == Token.DECLARATIONS) {
          copyDeclarations.addChild(node.getFirstChild());
        }
        if (node.getType() == Token.BLOCK) {
          copyBody.addChild(node.getFirstChild());
        }
        if (node.getType() == Token.STATEMENTS) {
          copyStatements.addChild(node.getFirstChild());
        }
      }
    });

    compiler.compile(externs, inputNode, root, options);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(externs, inputNode);

    Node simplifiedQuxCallBody = getCallExpression(copyStatements, "qux")
        .getLastChild();

    assertFalse(NodeUtil.hasSideEffect(simplifiedQuxCallBody));
  }

  private Node getCallExpression(Node node, String functionName) {
    for (Node child = node; child != null; child = child.getPreviousSibling()) {
      if (child.getType() == AST_CALL_EXPR
          && NodeUtil.isIdentifier(child.getFirstChild(), functionName)) {
        return child;
      }
    }
    return null;
  }

  static class GatherSideEffectNodesCallback extends AbstractPostOrderCallback {
    private final List<Node> sideEffectNodes;

    GatherSideEffectNodesCallback(List<Node> sideEffectNodes) {
      this.sideEffectNodes = sideEffectNodes;
    }

    GatherSideEffectNodesCallback(List<Node> sideEffectNodes,
        List<Node> copySideEffectNodes) {
      this.sideEffectNodes = sideEffectNodes;
      this.copySideEffectNodes = copySideEffectNodes;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
        sideEffectNodes.add(node);
        if (copySideEffectNodes != null) {
          copySideEffectNodes.add(node);
        }
      }
    }

    List<Node> copySideEffectNodes;
  }
}