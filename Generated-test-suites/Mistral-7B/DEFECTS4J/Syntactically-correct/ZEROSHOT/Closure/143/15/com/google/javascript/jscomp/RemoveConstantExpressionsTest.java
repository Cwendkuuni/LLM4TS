import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Iteration.BOTTOM;
import static com.google.javascript.rhino.Node.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest {
  private Compiler compiler;
  private GraphBuilder graphBuilder;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    graphBuilder = new GraphBuilder(compiler.getFrontEnd(), null, null);
  }

  @Test
  public void testRemoveConstantExpressions() {
    String sourceCode = "function foo() { return 1; } function bar() { return 2; } "
        + "function test() { return 1 + foo() + bar(); }";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = graphBuilder.build(sourceFile).getRootNode();

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    pass.process(null, rootNode);

    AbstractPostOrderCallback callback = new AssertSideEffectNodesCallback();
    NodeTraversal.traverse(null, rootNode, callback, BOTTOM);

    List<Node> expectedSideEffectNodes = Lists.newArrayList(
        expr(Token.CALL, foo, null),
        expr(Token.CALL, bar, null)
    );

    assertEquals(expectedSideEffectNodes.size(), callback.sideEffectNodes.size());
    for (int i = 0; i < callback.sideEffectNodes.size(); i++) {
      Node expectedNode = expectedSideEffectNodes.get(i);
      Node actualNode = callback.sideEffectNodes.get(i);
      assertEquals(expectedNode.getClass(), actualNode.getClass());
      assertEquals(expectedNode.getFirstChild().getText(), actualNode.getFirstChild().getText());
    }
  }

  static class AssertSideEffectNodesCallback extends AbstractPostOrderCallback {
    public List<Node> sideEffectNodes = Lists.newArrayList();

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
        sideEffectNodes.add(node);
      }
    }
  }
}