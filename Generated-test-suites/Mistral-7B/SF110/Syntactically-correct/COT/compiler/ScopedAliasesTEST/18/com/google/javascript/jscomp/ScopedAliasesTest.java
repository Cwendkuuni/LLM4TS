import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

/**
 * Test class for the ScopedAliases class.
 */
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
  public void testScopedAliases() {
    // Create a new instance of ScopedAliases with mock dependencies.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test with a valid "goog.scope" call.
    Node root = Node.newNode(Token.BLOCK, null, Collections.emptyList());
    Node googScopeCall = Node.newNode(Token.CALL, null,
        new Node[]{
          Node.newNode(Token.IDENT, "goog"),
          Node.newNode(Token.IDENT, "scope")
        });
    Node anonymousFunction = Node.newNode(Token.FUNCTION, null,
        new Node[]{Node.newNode(Token.LPAREN, null)},
        null,
        new Node[]{Node.newNode(Token.RPAREN, null)});
    Node body = Node.newNode(Token.BLOCK, null, Collections.emptyList());

    root.setChildAtIndex(0, googScopeCall);
    root.setChildAtIndex(1, anonymousFunction);
    anonymousFunction.setChildAtIndex(0, body);

    scopedAliases.process(null, root);

    // Verify that the call to goog.scope was logged as an alias transformation.
    ArgumentCaptor<AliasTransformation> aliasTransformationCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    verify(transformationHandler).logAliasTransformation(
        any(String.class), any(SourcePosition.class));
    verify(transformationHandler).logAliasTransformation(
        eq("goog"), eq(SourcePosition.unknown()));

    // Test with an invalid "goog.scope" call.
    Node invalidRoot = Node.newNode(Token.BLOCK, null, Collections.emptyList());
    Node invalidGoogScopeCall = Node.newNode(Token.CALL, null,
        new Node[]{
          Node.newNode(Token.IDENT, "goog"),
          Node.newNode(Token.IDENT, "scope")
        });
    Node invalidAnonymousFunction = Node.newNode(Token.FUNCTION, null,
        new Node[]{Node.newNode(Token.LPAREN, null)},
        null,
        new Node[]{Node.newNode(Token.RPAREN, null)});
    Node invalidBody = Node.newNode(Token.BLOCK, null,
        new Node[]{Node.newNode(Token.IDENT, "return")});

    invalidRoot.setChildAtIndex(0, invalidGoogScopeCall);
    invalidRoot.setChildAtIndex(1, invalidAnonymousFunction);
    invalidAnonymousFunction.setChildAtIndex(0, invalidBody);

    scopedAliases.process(null, invalidRoot);

    // Verify that an error was reported for the invalid "goog.scope" call.
    verify(compiler).report(any(DiagnosticType.class), any(Node.class), any(Object[].class));
    verify(compiler).report(any(DiagnosticType.class), any(Node.class),
        eq("JSC_GOOG_SCOPE_USED_IMPROPERLY"));
  }

  @Test
  public void testApplyAlias() {
    // Create a new instance of ScopedAliases with mock dependencies.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create a mock Node that represents an alias reference.
    Node aliasReference = mock(Node.class);

    // Create a mock Node that represents an alias definition.
    Node aliasDefinition = mock(Node.class);

    // Create a test case for an AliasedNode.
    scopedAliases.new AliasedNode(aliasReference, aliasDefinition).applyAlias();

    // Verify that the alias reference was replaced with the alias definition.
    verify(aliasReference.getParent()).replaceChild(
        aliasReference, aliasDefinition);
  }

  @Test
  public void testApplyAliasToTypeNode() {
    // Create a new instance of ScopedAliases with mock dependencies.
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Create a mock Node that represents a type node.
    Node typeNode = Node.newNode(Token.IDENT, "goog.dom");

    // Create a mock Node that represents an alias definition.
    Node aliasDefinition = Node.newNode(Token.IDENT, "dom");

    // Create a test case for an AliasedTypeNode.
    scopedAliases.new AliasedTypeNode(typeNode, aliasDefinition, "goog").applyAlias();

    // Verify that the type node was updated with the alias definition.
    verify(typeNode).setString("dom");
  }
}