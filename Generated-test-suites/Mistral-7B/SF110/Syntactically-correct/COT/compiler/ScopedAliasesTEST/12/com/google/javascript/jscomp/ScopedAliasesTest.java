import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    

    // Test basic functionality
    AbstractCompiler compiler = new AbstractCompiler();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node rootNode = Node.newTree("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "goog.scope(function() {" +
        "  var x = goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});" +
        "});");

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    // Check that expected nodes are present
    assertTrue(aliasDefinitionsInOrder.size() == 2);
    assertTrue(aliasUsages.size() == 2);
    assertTrue(scopeCalls.size() == 2);

    // Check that expected nodes have the correct values
    Node firstAliasDefinition = aliasDefinitionsInOrder.get(0);
    Node secondAliasDefinition = aliasDefinitionsInOrder.get(1);
    Node firstAliasUsage = aliasUsages.get(0);
    Node secondAliasUsage = aliasUsages.get(1);
    Node firstScopeCall = scopeCalls.get(0);
    Node secondScopeCall = scopeCalls.get(1);

    Node domTagName = Node.newTree("goog.dom.TagName.DIV");
    Node firstAlias = Node.newTree("goog.dom");
    Node secondAlias = Node.newTree("dom");

    // Check alias definitions
    assertTrue(firstAliasDefinition.getString().equals("goog.dom"));
    assertTrue(firstAliasDefinition.getParent().getLastChild().toString().equals(domTagName.toString()));

    assertTrue(secondAliasDefinition.getString().equals("dom"));
    assertTrue(secondAliasDefinition.getParent().getLastChild().toString().equals(domTagName.toString()));

    // Check alias uses
    assertTrue(firstAliasUsage.getAliasDefinition().toString().equals(firstAliasDefinition.toString()));
    assertTrue(secondAliasUsage.getAliasDefinition().toString().equals(secondAliasDefinition.toString()));

    // Check scope calls
    assertTrue(firstScopeCall.getLastChild().getLastChild().toString().equals(aliasDefinitionsInOrder.get(0).toString()));
    assertTrue(secondScopeCall.getLastChild().getLastChild().toString().equals(aliasDefinitionsInOrder.get(1).toString()));

    // Test errors
    Node invalidScopeCall = Node.newTree("goog.scope(function() {" +
        "var x = 5;" +
        "});");
    scopedAliases.process(null, invalidScopeCall);

    assertFalse(scopedAliases.hasErrors());
    scopedAliases.process(null, rootNode);
    assertFalse(scopedAliases.hasErrors());

    // Test forbidden locals
    Node forbiddenLocal = Node.newTree("goog.scope(function() {" +
        "var goog = {dom: {}};" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      createElement: function() {}" +
        "    }" +
        "  };" +
        "  var x = 5;" +
        "});" +
        "});");

    scopedAliases.process(null, forbiddenLocal);
    assertFalse(scopedAliases.hasErrors());

    // Test invalid alias usage
    Node invalidAliasUsage = Node.newTree("goog.scope(function() {" +
        "goog.scope(function() {" +
        "  var x = goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  var y = goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  x = y;" +
        "});" +
        "});");

    scopedAliases.process(null, invalidAliasUsage);
    assertTrue(scopedAliases.hasErrors());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertTrue(diagnostics.size() == 1);
    assertTrue(diagnostics.get(0).getCode().equals("JSC_GOOG_SCOPE_ALIAS_REDEFINED"));
    String expectedMessage = "The alias 'dom' is assigned a value more than once.";
    assertTrue(diagnostics.get(0).getMessage().equals(expectedMessage));

    
  }
}