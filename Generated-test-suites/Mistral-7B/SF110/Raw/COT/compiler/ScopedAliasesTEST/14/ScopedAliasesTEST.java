import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() throws Exception {
    // Given
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = Mockito.mock(AliasTransformationHandler.class);

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.parse(
        "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  var y = goog.y;" +
            "  goog.scope(function() {" +
            "    var x = 1;" +
            "  }).x = 2;" +
            "});");

    scopedAliases.process(null, root);

    // Then
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(3, aliasUsages.size());
    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());

    // Test aliasUsages
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        assertEquals("goog.x", aliasedNode.aliasReference.getString());
        assertEquals("x", aliasedNode.aliasDefinition.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        assertEquals("goog", aliasedTypeNode.typeReference.getString());
        assertEquals("goog", aliasedTypeNode.aliasDefinition.getQualifiedName());
      }
    }

    // Test aliasDefinitionsInOrder
    for (int i = 0; i < aliasDefinitionsInOrder.size(); i++) {
      Node aliasDefinition = aliasDefinitionsInOrder.get(i);
      Node aliasNode = aliasDefinition.getLastChild();
      assertEquals("x", aliasDefinition.getString());
      assertEquals("x", aliasNode.getString());
      assertEquals(Node.VAR, aliasDefinition.getType());
      assertEquals(Node.ASSIGN, aliasNode.getType());
    }

    // Test scopeCalls
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node anonymousFn = scopeCall.getChildAtIndex(1);
    assertEquals(Node.FUNCTION, anonymousFn.getType());
    assertEquals(0, anonymousFn.getChildCount());
  }

  @Test
  public void testScopedAliasesWithErrors() throws Exception {
    // Given
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = Mockito.mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.parse(
        "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  var y = goog.y;" +
            "});" +
            "goog.scope(function() {" +
            "  this.x = 1;" +
            "});" +
            "goog.scope(function() {" +
            "  return 1;" +
            "});" +
            "goog.scope(function() {" +
            "  throw new Error('Error');" +
            "});" +
            "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  var x = goog.x;" +
            "});");

    NodeTraversal traversal = PowerMockito.mock(NodeTraversal.class);
    NodeTraversal.traverse(compiler, root, traversal);

    // Then
    PowerMockito.verifyStatic();
    AbstractCompiler.reportCodeChange();

    // Verify error messages
    List<Node> errors = scopedAliases.getErrors();
    assertEquals(4, errors.size());

    for (Node error : errors) {
      DiagnosticType diagnosticType = DiagnosticType.fromCode(error.getFirstChild().getInt32());
      String message = error.getLastChild().getString();

      switch (diagnosticType) {
        case GOOG_SCOPE_USED_IMPROPERLY:
          assertEquals("The call to goog.scope must be alone in a single statement.", message);
          break;
        case GOOG_SCOPE_HAS_BAD_PARAMETERS:
          assertEquals("The call to goog.scope must take only a single parameter. It must be an anonymous function that itself takes no parameters.", message);
          break;
        case GOOG_SCOPE_REFERENCES_THIS:
          assertEquals("The body of a goog.scope function cannot reference 'this'", message);
          break;
        case GOOG_SCOPE_USES_RETURN:
          assertEquals("The body of a goog.scope function cannot use 'return'", message);
          break;
        case GOOG_SCOPE_USES_THROW:
          assertEquals("The body of a goog.scope function cannot use 'throw'", message);
          break;
        case GOOG_SCOPE_ALIAS_REDEFINED:
          assertEquals("The alias x is assigned a value more than once.", message);
          break;
        default:
          fail("Unexpected error type: " + diagnosticType);
      }
    }
  }
}