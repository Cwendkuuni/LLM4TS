import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new CompilerWithOptions();
    rootNode = new Node("function main() {}");
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases =
        new ScopedAliases(compiler, null, new IdentityAliasTransformationHandler());
    scopedAliases.process(null, rootNode);

    // Check that no errors were reported.
    assertTrue(compiler.getDiagnostics().isEmpty());

    // Check that the alias for "goog.dom" was defined and applied.
    Node googDom = rootNode.findFirstDescendant("goog.dom");
    assertEquals("goog.dom", googDom.getString());

    // Check that the alias for "DIV" was defined and applied.
    Node divNode = rootNode.findFirstDescendant("goog.dom.createElement(goog.dom.TagName.DIV)");
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", divNode.getString());
    assertEquals("goog.dom.createElement(Div)", divNode.getLastChild().getString());

    // Check that the alias for "Div" was not defined.
    Node divNode2 = rootNode.findFirstDescendant("goog.dom.createElement(Div)");
    assertNull(divNode2);

    // Check that the original "goog.dom" reference was removed.
    Node originalGoogDom = rootNode.findFirstDescendant("goog.dom");
    assertNull(originalGoogDom);

    // Check that the "goog.scope" function was removed.
    Node googScope = rootNode.findFirstDescendant("goog.scope");
    assertNull(googScope);

    // Check that the order of alias definitions matches the order of usage.
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    Map<Node, Integer> aliasDefinitionIndexMap = ImmutableMap.of();
    for (int i = 0; i < aliasDefinitions.size(); i++) {
      Node aliasDefinition = aliasDefinitions.get(i);
      Node aliasUsage = aliasUsages.get(i);
      assertEquals(aliasDefinition, aliasUsage.getAliasDefinition());
      aliasDefinitionIndexMap.put(aliasDefinition, i);
    }

    // Check that all alias usages are replaced with their corresponding definitions.
    for (Node aliasUsage : aliasUsages) {
      Node aliasDefinition = aliasUsage.getAliasDefinition();
      Node aliasReference = aliasUsage.getAliasReference();
      assertEquals(aliasDefinition, aliasReference.getParent());
      assertEquals(aliasDefinition, aliasReference.getReplacedWith());
    }

    // Check that the "goog.scope" function calls are removed.
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    for (Node scopeCall : scopeCalls) {
      Node scopeCallParent = scopeCall.getParent();
      assertEquals("goog.scope", scopeCallParent.getString());
      Node scopeCallChild = scopeCall.getLastChild();
      assertEquals("(anonymous)", scopeCallChild.getFirstChild().getQualifiedName());
      Node scopeClosureBlock = scopeCallChild.getLastChild();
      assertEquals(1, scopeClosureBlock.getChildCount());
      assertEquals(scopeCallParent, scopeClosureBlock.getParent());
      Node scopeCallFunction = scopeClosureBlock.getFirstChild();
      assertEquals(1, scopeCallFunction.getChildCount());
      assertEquals("return", scopeCallFunction.getFirstChild().getString());
      assertEquals(null, scopeCallFunction.getLastChild());
      assertNull(scopeCallChild.getParent());
    }

    // Check that the "goog.scope" function is replaced with its body.
    Node googScopeFunction = rootNode.findFirstDescendant("goog.scope");
    Node googScopeFunctionBody = googScopeFunction.getLastChild();
    assertEquals(googScopeFunctionBody, googScopeFunction.getReplacedWith());
  }

  private static class IdentityAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(
        String fileName, SourcePosition position) {
      return new IdentityAliasTransformation(fileName, position);
    }
  }

  private static class IdentityAliasTransformation implements AliasTransformation {
    private final String fileName;
    private final SourcePosition position;

    IdentityAliasTransformation(String fileName, SourcePosition position) {
      this.fileName = fileName;
      this.position = position;
    }

    @Override
    public void recordAlias(String name, String expandedName) {}

    @Override
    public String getFileName() {
      return fileName;
    }

    @Override
    public SourcePosition getPosition() {
      return position;
    }

    @Override
    public void reportError(DiagnosticType errorType, String... arguments) {
      fail("Should not occur.");
    }
  }
}

class CompilerWithOptions extends CompilerOptions {
  @Override
  public AliasTransformationHandler getAliasTransformationHandler() {
    return new IdentityAliasTransformationHandler();
  }
}

class HotSwapCompilerPassTestUtil {
  static Node findFirstDescendant(Node n, String name) {
    Node current = n;
    while (current != null) {
      if (current.isName() && current.getString().equals(name)) {
        return current;
      }
      current = current.getNext();
    }
    return null;
  }
}