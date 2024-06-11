import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Setup
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test empty
    Node root = Node.ROOT();
    scopedAliases.process(null, root);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    // Test null compiler
    scopedAliases = new ScopedAliases(null, preprocessorSymbolTable, transformationHandler);
    assertNull(scopedAliases.transformationHandler);

    // Test null preprocessorSymbolTable
    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    assertNull(scopedAliases.preprocessorSymbolTable);

    // Test null transformationHandler
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    assertNull(scopedAliases.transformationHandler);

    // Test valid
    Node rootNode = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});");

    scopedAliases.process(null, rootNode);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());

    ArgumentCaptor<AliasTransformation> captor = ArgumentCaptor.forClass(AliasTransformation.class);
    when(transformationHandler.logAliasTransformation(
        rootNode.getSourceFileName(),
        SourcePosition.ANY_POSITION)).thenReturn(captor.capture());

    assertEquals("goog.dom", captor.getValue().getAliasName());
    assertEquals("dom", captor.getValue().getAliasExpanded());

    Node scopeCall = scopeCalls.get(0);
    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals("function() {...}", anonymousFnNode.getString());

    Node aliasUsage = aliasUsages.get(0);
    Node aliasReference = aliasUsage.getNode();
    Node aliasDefinition = aliasDefinitionForNode(aliasReference, rootNode);

    assertEquals("DOM", aliasDefinition.getQualifiedName());
    assertEquals("dom", aliasReference.getString());
  }

  private Node aliasDefinitionForNode(Node aliasReference, Node root) {
    Node currentNode = root;
    while (currentNode.hasChildren()) {
      currentNode = currentNode.getChildAtIndex(0);
      if (currentNode.isVar() && currentNode.hasOneChild() &&
          currentNode.getLastChild().isQualifiedName() &&
          currentNode.getLastChild().getString().equals("goog.scope")) {
        for (Node child = currentNode.getLastChild().getLastChild().getFirstChild();
             child != null; child = child.getNext()) {
          if (child.isVar() && child.getName().equals(aliasReference.getString())) {
            return child;
          }
        }
      }
      currentNode = currentNode.getNext();
    }
    throw new AssertionError("Failed to find alias definition.");
  }
}