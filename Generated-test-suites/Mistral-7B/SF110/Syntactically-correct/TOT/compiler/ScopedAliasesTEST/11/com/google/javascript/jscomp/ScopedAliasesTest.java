import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testValidScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = new Node(Token.obiliteral, null, null);
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.appendChild(new Node(Token.NAME, "goog.scope", null));
    scopeCall.appendChild(new Node(Token.LP, null, null));
    scopeCall.appendChild(new Node(Token.FN, null, null));
    scopeCall.appendChild(new Node(Token.RP, null, null));
    root.appendChild(scopeCall);
    root.appendChild(new Node(Token.NAME, "dom", null));
    root.appendChild(new Node(Token.DOT, null, null));
    root.appendChild(new Node(Token.NAME, "createElement", null));
    root.appendChild(new Node(Token.LP, null, null));
    root.appendChild(new Node(Token.STRING, "DIV", null));
    root.appendChild(new Node(Token.RP, null, null));

    NodeTraversal traversal = new NodeTraversal(root, compiler, null);
    scopedAliases.process(null, root);
    traversal.traverse(scopedAliases);

    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        NodeUtil.toSource(root));
  }

  @Test
  public void testImproperlyUsedGoogScope() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = new Node(Token.OBLOCK, null, null);
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.appendChild(new Node(Token.NAME, "goog.scope", null));
    scopeCall.appendChild(new Node(Token.LP, null, null));
    scopeCall.appendChild(new Node(Token.FN, null, null));
    scopeCall.appendChild(new Node(Token.RP, null, null));
    root.appendChild(scopeCall);
    root.appendChild(new Node(Token.NAME, "dom", null));
    root.appendChild(new Node(Token.DOT, null, null));
    root.appendChild(new Node(Token.NAME, "createElement", null));
    root.appendChild(new Node(Token.LP, null, null));
    root.appendChild(new Node(Token.STRING, "DIV", null));
    root.appendChild(new Node(Token.RP, null, null));
    root.appendChild(new Node(Token.SEMICOLON, null, null));
    root.appendChild(new Node(Token.NAME, "myFunction", null));
    root.appendChild(new Node(Token.LP, null, null));
    root.appendChild(new Node(Token.RP, null, null));

    NodeTraversal traversal = new NodeTraversal(root, compiler, null);
    scopedAliases.process(null, root);
    traversal.traverse(scopedAliases);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testGoogScopeWithBadParameters() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = new Node(Token.OBLOCK, null, null);
    Node scopeCall = new Node(Token.CALL, null, null);
    scopeCall.appendChild(new Node(Token.NAME, "goog.scope", null));
    scopeCall.appendChild(new Node(Token.LP, null, null));
    scopeCall.appendChild(new Node(Token.FN, null, null));
    scopeCall.appendChild(new Node(Token.LP, null, null));
    scopeCall.appendChild(new Node(Token.NAME, "param1", null));
    scopeCall.appendChild(new Node(Token.COMMA, null, null));
    scopeCall.appendChild(new Node(Token.NAME, "param2", null));
    scopeCall.appendChild(new Node(Token.RP, null, null));
    root.appendChild(scopeCall);

    NodeTraversal traversal = new NodeTraversal(root, compiler, null);
    scopedAliases.process(null, root);
    traversal.traverse(scopedAliases);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The call to goog.scope must take only a single parameter.  It must" +
                 " be an anonymous function that itself takes no parameters.",
        traversal.getDiagnostics().get(0).getMessage());
  }
}