import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticListener;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VarCheckTest {

  @Mock private DiagnosticListener diagnosticListener;
  @Mock private AbstractCompiler compiler;
  @Mock private NodeFactory nodeFactory;
  @Mock private CompilerInput input;
  @Mock private Node root;
  @Mock private Scope scope;

  private VarCheck varCheck;
  private Node externs;

  @Before
  public void setUp() {
    Mockito.when(compiler.newCompilerPass(Mockito.any(AbstractCompiler.class)))
        .thenReturn(varCheck);
    Mockito.when(compiler.getModuleGraph()).thenReturn(Mockito.mock(JSModuleGraph.class));
    Mockito.when(compiler.newExternInput(Mockito.anyString())).thenReturn(input);

    varCheck = new VarCheck(compiler);
    externs = Node.newBlock(Token.BLOCK, Lists.newArrayList(
        Node.newVar(Token.VAR, Node.newName(Token.NAME, "foo"), null),
        Node.newReturn(Token.RETURN, Node.newName(Token.NAME, "foo"))));
  }

  @Test
  public void testDeclaredVariable() {
    Mockito.when(input.getAstRoot(compiler)).thenReturn(root);
    Mockito.when(scope.getVar(Mockito.eq("foo"))).thenReturn(new Scope.Var("foo", root, null, null));

    varCheck.process(externs, root);

    // No diagnostics should have been emitted.
    Mockito.verifyNoMoreInteractions(diagnosticListener);
  }

  @Test
  public void testUndefinedVariable() {
    Mockito.when(input.getAstRoot(compiler)).thenReturn(root);
    Mockito.when(scope.getVar(Mockito.eq("foo"))).thenReturn(null);

    varCheck.process(externs, root);

    Mockito.verify(diagnosticListener).report(Mockito.eq(externs),
        Mockito.eq(UNDEFINED_VAR_ERROR), Mockito.eq("foo"));
  }

  @Test
  public void testModuleDependency() {
    JSModule moduleA = Mockito.mock(JSModule.class);
    JSModule moduleB = Mockito.mock(JSModule.class);
    JSModuleGraph moduleGraph = Mockito.mock(JSModuleGraph.class);
    Mockito.when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    Mockito.when(moduleGraph.dependsOn(Mockito.eq(moduleA), Mockito.eq(moduleB)))
        .thenReturn(true);
    Mockito.when(input.getModule()).thenReturn(moduleA);

    Mockito.when(input.getAstRoot(compiler)).thenReturn(root);
    Mockito.when(scope.getVar(Mockito.eq("foo"))).thenReturn(new Scope.Var("foo", root, null, null));
    Mockito.when(root.getFirstChild()).thenReturn(Node.newName(Token.NAME, "moduleB"));

    varCheck.process(externs, root);

    Mockito.verify(diagnosticListener).report(Mockito.eq(root),
        Mockito.eq(VIOLATED_MODULE_DEP_ERROR),
        Mockito.eq(moduleB.getName()), Mockito.eq(moduleA.getName()), "foo");
  }
}