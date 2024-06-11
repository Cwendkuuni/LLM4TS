import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestUtil.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link ScopedAliases} class.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.script.*"})
public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    // Given
    String sourceCode =
        "goog.scope(function() {\n" +
            "  var goog = {\n" +
            "    dom: function() {\n" +
            "      return {\n" +
            "        createElement: function(tagName) {\n" +
            "          return document.createElement(tagName);\n" +
            "        }\n" +
            "      };\n" +
            "    }\n" +
            "  };\n" +
            "  goog.dom.createElement('DIV');\n" +
            "});\n";

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(
        PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler =
        Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node rootNode = parseTree(sourceCode);
    scopedAliases.process(null, rootNode);

    // Then
    List<Node> expectedScopeCalls = Lists.newArrayList(
        findChildByType(rootNode, Node.CALL),
        findChildByType(rootNode, Node.CALL)
    );

    List<Node> actualScopeCalls = Lists.newArrayList();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    for (Node n : traversal.getScopeCalls()) {
      actualScopeCalls.add(n);
    }

    assertEquals("Unexpected number of scope calls.", expectedScopeCalls.size(),
        actualScopeCalls.size());
    for (int i = 0; i < expectedScopeCalls.size(); i++) {
      Node expectedScopeCall = expectedScopeCalls.get(i);
      Node actualScopeCall = actualScopeCalls.get(i);
      assertEquals("Unexpected scope call.", expectedScopeCall, actualScopeCall);
    }
  }

  @Test
  public void testHotSwapScript() {
    // Given
    String originalSourceCode =
        "goog.scope(function() {\n" +
            "  var goog = {\n" +
            "    dom: function() {\n" +
            "      return {\n" +
            "        createElement: function(tagName) {\n" +
            "          return document.createElement(tagName);\n" +
            "        }\n" +
            "      };\n" +
            "    }\n" +
            "  };\n" +
            "  goog.dom.createElement('DIV');\n" +
            "});\n";

    String modifiedSourceCode =
        "goog.scope(function() {\n" +
            "  var goog = {\n" +
            "    dom: function() {\n" +
            "      return {\n" +
            "        createElement: function(tagName) {\n" +
            "          return document.createElement(tagName.toUpperCase());\n" +
            "        }\n" +
            "      };\n" +
            "    }\n" +
            "  };\n" +
            "  goog.dom.createElement('DIV');\n" +
            "});\n";

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(
        PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler =
        Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node originalRootNode = parseTree(originalSourceCode);
    Node modifiedRootNode = parseTree(modifiedSourceCode);

    // When
    scopedAliases.hotSwapScript(originalRootNode, null);
    scopedAliases.hotSwapScript(modifiedRootNode, originalRootNode);

    // Then
    List<Node> expectedScopeCalls = Lists.newArrayList(
        findChildByType(modifiedRootNode, Node.CALL),
        findChildByType(modifiedRootNode, Node.CALL)
    );

    List<Node> actualScopeCalls = Lists.newArrayList();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, modifiedRootNode, traversal);
    for (Node n : traversal.getScopeCalls()) {
      actualScopeCalls.add(n);
    }

    assertEquals("Unexpected number of scope calls.", expectedScopeCalls.size(),
        actualScopeCalls.size());
    for (int i = 0; i < expectedScopeCalls.size(); i++) {
      Node expectedScopeCall = expectedScopeCalls.get(i);
      Node actualScopeCall = actualScopeCalls.get(i);
      assertEquals("Unexpected scope call.", expectedScopeCall, actualScopeCall);
    }
  }

  @Test
  public void testApplyAlias() {
    // Given
    String sourceCode =
        "goog.scope(function() {\n" +
            "  var goog = {\n" +
            "    dom: function() {\n" +
            "      return {\n" +
            "        createElement: function(tagName) {\n" +
            "          return document.createElement(tagName);\n" +
            "        }\n" +
            "      };\n" +
            "    }\n" +
            "  };\n" +
            "  var x = goog.x;\n" +
            "});\n";

    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = Mockito.mock(
        PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler =
        Mockito.mock(AliasTransformationHandler.class);

    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = parseTree(sourceCode);

    // When
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    Node aliasUsageNode = traversal.getAliasUsages().get(0);
    scopedAliases.applyAlias(aliasUsageNode);

    // Then
    Node expectedAliasDefinitionNode =
        findChildByType(rootNode, Node.ASSIGN, "x", "goog.x");
    Node actualAliasDefinitionNode = findChildByType(rootNode, Node.ASSIGN, "x");

    assertEquals("Unexpected alias definition node.", expectedAliasDefinitionNode,
        actualAliasDefinitionNode);
  }
}