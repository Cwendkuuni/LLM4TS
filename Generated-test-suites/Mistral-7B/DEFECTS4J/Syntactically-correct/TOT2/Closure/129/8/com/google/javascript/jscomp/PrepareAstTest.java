import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testPrepareAst_NormalizeNodeTypes() {
    // Test normalizeNodeTypes method with a simple Node
    Node root = nodeFactory.literal(true);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    assertTrue(root.isBoolean());

    // Test normalizeNodeTypes method with a complex Node structure
    Node complexRoot = nodeFactory.program(new Node[] {
        nodeFactory.variable("x"),
        nodeFactory.statement(
            nodeFactory.assignment(
                nodeFactory.name("x"),
                nodeFactory.literal(1),
                nodeFactory.literal(1)
            )
        ),
        nodeFactory.statement(
            nodeFactory.assignment(
                nodeFactory.name("x"),
                nodeFactory.call(
                    nodeFactory.member("Math", "pow"),
                    nodeFactory.literal(2),
                    nodeFactory.literal(3)
                )
            )
        )
    });

    prepareAst.process(null, complexRoot);

    assertTrue(complexRoot.isProgram());
    assertTrue(complexRoot.getFirstChild().isVariable());
    assertTrue(complexRoot.getLastChild().isCall());

    Node x = complexRoot.getFirstChild();
    Node powCall = complexRoot.getLastChild();

    // Test that EXPR_VOID is converted to EXPR_RESULT
    Node powResult = powCall.getFirstChild();
    assertTrue(powResult.isCall());
    assertTrue(powResult.getFirstChild().isName());
    assertTrue(powResult.getFirstChild().getType() == Token.NAME);
    assertTrue(powCall.isExpressionStatement());
    assertSame(powCall.getLastChild(), powResult);

    // Test that blocks are added to control structures
    Node assignment = complexRoot.getChildren()[1];
    assertTrue(assignment.isStatement());
    assertTrue(assignment.getFirstChild().isExpressionStatement());
    assertTrue(assignment.getLastChild().isBlock());

    // Test direct eval handling
    Node evalCall = nodeFactory.call(
        nodeFactory.member("eval", nodeFactory.literal(0)),
        nodeFactory.literal("console.log('Hello, World!')")
    );
    Node evalBlock = nodeFactory.block();
    evalBlock.addChildToEnd(evalCall);

    Node rootWithEval = nodeFactory.program(new Node[] {
        complexRoot,
        evalBlock
    });

    prepareAst.process(null, rootWithEval);

    assertTrue(rootWithEval.isProgram());
    assertTrue(rootWithEval.size() == 2);
  }

  @Test
  public void testPrepareAst_PrepareAnnotations() {
    Node root = nodeFactory.program(new Node[] {
        nodeFactory.statement(nodeFactory.expressionStatement(
            nodeFactory.call(nodeFactory.member("console.log", null), nodeFactory.literal("Hello, World!"))
        ))
    });

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && "console.log".equals(n.getString())) {
          getTest().assertNotNull(n.getJSDocInfo());
          return false;
        }
        return super.shouldTraverse(t, n, parent);
      }
    };

    NodeTraversal.traverse(compiler, root, traversal);

    // Test that JsDoc annotations are copied to the called function
    Node call = root.getFirstChild();
    assertTrue(call.isStatement());
    assertTrue(call.getFirstChild().isExpressionStatement());
    Node logCall = call.getLastChild();
    assertTrue(logCall.isCall());
    assertNull(logCall.getJSDocInfo());
    assertTrue(call.getJSDocInfo() != null);
    assertEquals("Logs a message to the console", call.getJSDocInfo().getDescription());
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAst_CheckState() {
    Node root = nodeFactory.program(new Node[] {});

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);
  }
}