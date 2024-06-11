import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.ScopedAliases;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = createTestNode("goog.scope(function() {\n" +
                              "  var dom = goog.dom;\n" +
                              "  var DIV = dom.TagName.DIV;\n" +
                              "  dom.createElement(DIV);\n" +
                              "});");

    scopedAliases.process(null, root);

    // Then
    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom.TagName.DIV", aliasDefinitionsInOrder.get(0).getString());

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      assertEquals("goog.dom.TagName.DIV", aliasedNode.aliasDefinition.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
      assertEquals("goog.dom.TagName", aliasedTypeNode.aliasDefinition.getString());
      assertEquals("DIV", aliasedTypeNode.aliasName);
    }

    // Given
    root = createTestNode("goog.scope(function() {\n" +
                          "  var dom = goog.dom;\n" +
                          "  var DIV = dom.TagName.DIV;\n" +
                          "  var x = goog.x;\n" +
                          "  dom.createElement(DIV);\n" +
                          "});");

    // When & Then
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.getTraversal().hasErrors());

    // Given
    root = createTestNode("goog.scope(function() {\n" +
                          "  var dom = goog.dom;\n" +
                          "  var DIV = dom.TagName.DIV;\n" +
                          "  var x = goog.x;\n" +
                          "  var y = goog.x;\n" +
                          "  dom.createElement(DIV);\n" +
                          "});");

    // When & Then (Error)
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));

    // Given
    root = createTestNode("goog.scope(function() {\n" +
                          "  function foo() {}\n" +
                          "  var x = goog.x;\n" +
                          "});");

    // When & Then (Error)
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));

    // Given
    root = createTestNode("goog.scope(function() {\n" +
                          "  this.x = goog.x;\n" +
                          "});");

    // When & Then (Error)
    assertThrows(DiagnosticType.error.class, () -> scopedAliases.process(null, root));

    // Given
    root = createTestNode("goog.scope(function() {\n" +
                          "  return goog.x;\n" +
                          "});");

    // When & Then (Error)
    assertThrows(DiagnosticType.error.class, () -> scopedAliases.process(null, root));

    // Given
    root = createTestNode("goog.scope(function() {\n" +
                          "  throw new Error('error');\n" +
                          "});");

    // When & Then (Error)
    assertThrows(DiagnosticType.error.class, () -> scopedAliases.process(null, root));
  }

  // Helper method to create test nodes
  private Node createTestNode(String testCode) {
    Node externalSourceNode = new Node();
    externalSourceNode.setType(Token.SOURCE_FILE);
    externalSourceNode.setString(TestHelper.TEST_FILE);

    Node root = new Node();
    root.setType(Token.PROGRAM);
    root.setChildren(new Node[]{externalSourceNode});

    Node sourceNode = new Node();
    sourceNode.setType(Token.SOURCE_INPUT);
    sourceNode.setString(testCode);
    root.getLastChild().setChildren(new Node[]{sourceNode});

    return root;
  }
}