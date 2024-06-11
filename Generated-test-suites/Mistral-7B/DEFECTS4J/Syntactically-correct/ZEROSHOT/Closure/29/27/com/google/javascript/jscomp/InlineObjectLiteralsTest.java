import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassManager.PassAttemptBailoutException;
import com.google.javascript.jscomp.CompilerUtils;
import com.google.javascript.jscomp.Context;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private SourceFile sourceFile;
  private Node rootNode;
  private Scope globalScope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    sourceFile = mock(SourceFile.class);
    rootNode = Node.ROOT();
    globalScope = new Scope(null);
    globalScope.setRootNode(rootNode);
    when(passManager.getCompiler()).thenReturn(compiler);
    when(sourceFile.getScope()).thenReturn(globalScope);
  }

  @Test
  public void testInlineObjectLiterals() throws Exception {
    InlineObjectLiterals testee = new InlineObjectLiterals(compiler, () -> "test");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());

    Node externs = Node.objectLiteral();
    Node inlineObject = Node.objectLiteral(
        Node.property("a", Node.numberLiteral(1)),
        Node.property("b", Node.objectLiteral(
            Node.property("c", Node.numberLiteral(3)),
            Node.property("d", Node.numberLiteral(4)))));

    Node input = Node.sequence(
        Node.assign(Node.name("x"), inlineObject),
        Node.assign(Node.name("y"), Node.name("x.b.c")),
        Node.assign(Node.name("z"), Node.name("x.b.d")),
        Node.assign(Node.name("a"), Node.numberLiteral(5)),
        Node.assign(Node.name("x.a"), Node.numberLiteral(6))
    );

    when(compiler.getSourceFile()).thenReturn(sourceFile);
    when(compiler.getCodingConvention()).thenReturn(Mockito.mock(CompilerUtils.CodingConvention.class));

    testee.process(externs, input);

    CompilerPassManager.PassAttempt passAttempt = Mockito.mock(CompilerPassManager.PassAttempt.class);
    when(passManager.getNextPassAttempt()).thenReturn(passAttempt);

    try {
      testee.process(externs, input);
      fail("Expected exception");
    } catch (PassAttemptBailoutException e) {
      // Expected
    }

    // Verify that variables "x", "y", and "z" have been replaced with
    // individual variables
    List<Node> children = Lists.newArrayList(rootNode.getLastChild());
    for (int i = 0; i < 3; i++) {
      Node varDecl = children.get(i);
      Node name = varDecl.getFirstChild();
      Node initializer = varDecl.getLastChild();
      Node value = null;
      if (initializer != null) {
        value = initializer.getFirstChild();
      }

      Node objectLiteral = null;
      if (i == 0) {
        objectLiteral = Node.objectLiteral(
            Node.property("a", Node.numberLiteral(1)),
            Node.property("b", Node.objectLiteral(
                Node.property("c", Node.numberLiteral(3)),
                Node.property("d", Node.numberLiteral(4)))));
      }

      Node expectedValue = null;
      if (i < 3) {
        expectedValue = Node.name("JSCompiler_object_inline_c_" + testee.VAR_PREFIX);
      }

      assertThat(name.getString()).isEqualTo("JSCompiler_object_inline_x_" + testee.VAR_PREFIX);
      if (objectLiteral != null) {
        Node assignedValue = name.getParent().getLastChild();
        assertThat(assignedValue).isInstanceOf(Node.Assign.class);
        assertThat(assignedValue.getAssignedValue()).isSameInstanceAs(objectLiteral);
      }
      if (value != null && expectedValue != null) {
        assertThat(value).isSameInstanceAs(expectedValue);
      }
    }
  }

  private static class InliningBehavior extends ReferenceCollectingCallback.Behavior {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      super.afterExitScope(t, referenceMap);
      for (Var var : t.getScope().getVars()) {
        if (InlineObjectLiteralsTest.this.staleVars.contains(var)) {
          InlineObjectLiteralsTest.this.staleVars.remove(var);
          break;
        }
      }
    }
  }
}