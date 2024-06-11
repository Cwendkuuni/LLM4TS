import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.SCRIPT, null);
    scopedAliases.process(externs, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();

    // Test for no errors
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertNull(traversal.getTransformation());
    assertFalse(traversal.hasErrors());

    // Test for errors
    root.setString("function foo() {" +
        "goog.scope(function() {" +
        "  var x = goog.x;" + // GOOG_SCOPE_ALIAS_REDEFINED
        "  var y = goog.y;" + // GOOG_SCOPE_ALIAS_REDEFINED
        "  var goog = {'x': 'x'};" + // GOOG_SCOPE_NON_ALIAS_LOCAL
        "  var z = this;" + // GOOG_SCOPE_REFERENCES_THIS
        "  return function() {" + // GOOG_SCOPE_USES_RETURN
        "    throw new Error('test');" + // GOOG_SCOPE_USES_THROW
        "  };" +
        "});" +
        "}");

    scopedAliases.process(externs, root);

    assertEquals(4, traversal.getAliasUsages().size());
    assertEquals(4, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertNotNull(traversal.getTransformation());
    assertTrue(traversal.hasErrors());
  }
}