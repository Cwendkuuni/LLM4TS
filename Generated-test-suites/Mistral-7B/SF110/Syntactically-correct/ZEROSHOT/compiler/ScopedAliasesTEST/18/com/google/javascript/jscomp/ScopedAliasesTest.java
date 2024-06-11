import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.ScopeCall;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createString("goog.scope(function() {" +
          "var dom = goog.dom;" +
          "var DIV = dom.TagName.DIV;" +
          "dom.createElement(DIV);" +
          "goog.scope(function() {" +
          "  var dom2 = goog.dom;" +
          "  Dom2.createElement(Dom2.TagName.DIV);" +
          "});" +
          "});");

    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(2, scopeCalls.size());

    ScopeCall firstScopeCall = scopeCalls.get(0);
    assertEquals(SourcePosition.UNKNOWN, firstScopeCall.getSourceRegion());

    ScopeCall secondScopeCall = scopeCalls.get(1);
    assertEquals(new SourcePosition(1, 18, 1, 44), secondScopeCall.getSourceRegion());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());

    AliasUsage firstAliasUsage = aliasUsages.get(0);
    Node firstAliasNode = firstAliasUsage.getNode();
    Node firstAliasDefinition = firstAliasUsage.getAliasDefinition();
    assertEquals("DIV", firstAliasNode.getString());
    assertEquals("DomName.DIV", firstAliasDefinition.getString());
    firstAliasUsage.applyAlias();
    assertEquals("DomName.DIV", firstAliasNode.getString());

    AliasUsage secondAliasUsage = aliasUsages.get(1);
    Node secondAliasNode = secondAliasUsage.getNode();
    Node secondAliasDefinition = secondAliasUsage.getAliasDefinition();
    assertEquals("Dom2", secondAliasNode.getString());
    assertEquals("DomName", secondAliasDefinition.getString());
    secondAliasUsage.applyAlias();
    assertEquals("DomName", secondAliasNode.getString());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());

    Node firstAliasDefinitionNode = aliasDefinitionsInOrder.get(0);
    assertEquals("DomName", firstAliasDefinitionNode.getString());
    Node firstAliasDefinitionValue = firstAliasDefinitionNode.getFirstChild();
    assertEquals("DomName.DIV", firstAliasDefinitionValue.getString());

    Node secondAliasDefinitionNode = aliasDefinitionsInOrder.get(1);
    assertEquals("DomName", secondAliasDefinitionNode.getString());
    Node secondAliasDefinitionValue = secondAliasDefinitionNode.getFirstChild();
    assertEquals("DomName.DOM2", secondAliasDefinitionValue.getString());

    assertFalse(scopedAliases.getTraversal().hasErrors());
  }

  @Test
  public void testScopedAliasesErrors() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.createString("goog.scope(function() {" +
          "this.x = 1;" +
          "goog.scope(function() {" +
          "  return 2;" +
          "});" +
          "});");

    scopedAliases.process(null, root);

    List<ScopeCall> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());

    ScopeCall scopeCall = scopeCalls.get(0);
    assertEquals(new SourcePosition(1, 5, 1, 10), scopeCall.getSourceRegion());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(0, aliasUsages.size());

    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());

    assertTrue(scopedAliases.getTraversal().hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(5, diagnostics.size());

    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostic.getType());
    assertEquals("The body of a goog.scope function cannot reference 'this'.", diagnostic.getMessage());

    diagnostic = diagnostics.get(1);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getType());
    assertEquals("The body of a goog.scope function cannot use 'return'.", diagnostic.getMessage());

    diagnostic = diagnostics.get(2);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getType());
    assertEquals("The body of a goog.scope function cannot use 'throw'.", diagnostic.getMessage());

    diagnostic = diagnostics.get(3);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType());
    assertEquals("The alias 'x' is assigned a value more than once.", diagnostic.getMessage());

    diagnostic = diagnostics.get(4);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType());
    assertEquals("The call to goog.scope must be alone in a single statement.", diagnostic.getMessage());
  }
}

interface AliasTransformationHandler {
  AliasTransformation logAliasTransformation(String fileName, SourcePosition sourceRegion);
}

class MockAliasTransformationHandler implements AliasTransformationHandler {
  @Override
  public AliasTransformation logAliasTransformation(String fileName, SourcePosition sourceRegion) {
    return new AliasTransformation() {};
  }
}