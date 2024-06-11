import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  private static AbstractCompiler createCompiler() {
    return new Compiler();
  }

  private static Node createNode(int type, Node... children) {
    CompilerInput input = new CompilerInput(NodeFactory.createScriptSource("test", ""));
    NodeFactory factory = input.parser.getFactory();
    Node node = factory.createToken(type);
    if (children != null) {
      Node parent = NodeUtil.makeBlock(factory, children);
      parent.setParent(node);
      node.addChild(parent);
    }
    return node;
  }

  @org.junit.Test
  public void testSimpleAssignments() {
    AbstractCompiler compiler = createCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y"))
    );

    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(0, checker.problemNodes.size());

    Node expectedError = createNode(Token.ERROR, "JSC_USELESS_CODE",
        "Suspicious code. This code lacks side-effects. Is there a bug?");
    Node rootWithError = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y")),
        expectedError
    );

    NodeTraversal.traverse(compiler, rootWithError, checker);
    assertEquals(1, checker.problemNodes.size());
    assertEquals(expectedError, checker.problemNodes.get(0));

    compiler.process();
  }

  @org.junit.Test
  public void testSimpleOperations() {
    AbstractCompiler compiler = createCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ADD, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.NUMBER, 42))
    );

    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(0, checker.problemNodes.size());

    Node expectedError = createNode(Token.ERROR, "JSC_USELESS_CODE",
        "The result of the '+' operator is not being used.");
    Node rootWithError = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ADD, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.NUMBER, 42)),
        expectedError
    );

    NodeTraversal.traverse(compiler, rootWithError, checker);
    assertEquals(1, checker.problemNodes.size());
    assertEquals(expectedError, checker.problemNodes.get(0));

    compiler.process();
  }

  @org.junit.Test
  public void testSideEffectFreeCodeIsProtected() {
    AbstractCompiler compiler = createCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y"))
    );

    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(0, checker.problemNodes.size());

    compiler.process();
    checker.protectSideEffects();

    Node expectedError = createNode(Token.ERROR, "JSC_USELESS_CODE",
        "Suspicious code. This code lacks side-effects. Is there a bug?");
    Node rootWithError = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y")),
        expectedError
    );

    NodeTraversal.traverse(compiler, rootWithError, checker);
    assertEquals(1, checker.problemNodes.size());
    assertEquals(expectedError, checker.problemNodes.get(0));

    Node replacement = checker.problemNodes.get(0).getLastChild();
    Node name = replacement.getFirstChild();
    Node call = replacement.getNext();

    Node expectedCall = createNode(Token.CALL, name, NodeUtil.makeList(NodeUtil.makeList(rootWithError.getFirstChild())));
    assertEquals(expectedCall, call);

    compiler.process();
    assertEquals(0, checker.problemNodes.size());
  }

  @org.junit.Test
  public void testProtectedCodeIsRemoved() {
    AbstractCompiler compiler = createCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y")),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.NUMBER, 42))
    );

    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(1, checker.problemNodes.size());

    checker.protectSideEffects();
    assertEquals(1, checker.problemNodes.size());

    compiler.process();
    assertEquals(0, checker.problemNodes.size());

    Node expectedCode = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y")),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.NUMBER, 42))
    );
    Node expectedError = createNode(Token.ERROR, "JSC_PROTECTOR_REMOVED",
        "Side-effect free code was protected, but removed during compile.");
    Node expectedErrorLocation = createNode(Token.EXPR_RESULT, NodeUtil.makeList(expectedCode));

    NodeTraversal.traverse(compiler, root, new CheckSideEffects.StripProtection(compiler));
    Node actualCode = root.getFirstChild();
    Node actualError = actualCode.getLastChild();

    assertEquals(expectedCode, actualCode);
    assertEquals(expectedError, actualError);
    assertEquals("JSC_PROTECTOR_REMOVED", actualError.getClass().getName().substring(12));
  }

  @org.junit.Test
  public void testProtectedCodeIsNotRemoved() {
    AbstractCompiler compiler = createCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y")),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.NUMBER, 42)),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y"))
    );

    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(1, checker.problemNodes.size());

    checker.protectSideEffects();
    assertEquals(1, checker.problemNodes.size());

    compiler.process();
    assertEquals(1, checker.problemNodes.size());

    Node expectedCode = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y")),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.NUMBER, 42)),
        createNode(Token.ASSIGN, NodeUtil.makeQualifiedName(factory, "x"), createNode(Token.IDENT, "y"))
    );

    Node expectedProtectedCall = createNode(Token.CALL, createNode(Token.NAME, "JSCOMPILER_PRESERVE"),
        NodeUtil.makeList(NodeUtil.makeList(expectedCode.getFirstChild(), expectedCode.getLastChild())));
    Node expectedProtectedCode = NodeUtil.makeBlock(
        NodeFactory.getFactory(),
        expectedCode.getFirstChild(),
        expectedProtectedCall,
        expectedCode.getLastChild()
    );

    Node actualCode = root.getFirstChild();
    assertFalse(actualCode.isCall());

    Node actualProtectedCall = null;
    Node actualLastChild = actualCode;
    while (actualLastChild != null) {
      if (actualLastChild.isCall() && actualLastChild.getFirstChild().isName() && actualLastChild.getFirstChild().getString().equals("JSCOMPILER_PRESERVE")) {
        actualProtectedCall = actualLastChild;
        break;
      }
      actualLastChild = actualLastChild.getPreviousSibling();
    }

    assertTrue(actualProtectedCall != null);
    assertEquals(expectedProtectedCode, actualCode);
  }

  /**
   * A compiler that creates an empty CompilerInput to avoid having to write
   * a CompilerInputTest for this test class.
   */
  private static class Compiler implements AbstractCompiler {
    @Override
    public CompilerInput getSynthesizedExternsInput() {
      return new CompilerInput(NodeFactory.createScriptSource("test", ""));
    }

    @Override
    public void report(Diagnostic diag) {
    }

    @Override
    public void reportCodeChange() {
    }
  }
}