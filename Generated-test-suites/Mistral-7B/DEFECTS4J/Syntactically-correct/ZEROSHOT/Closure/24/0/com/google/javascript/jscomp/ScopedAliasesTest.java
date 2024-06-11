import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Symbol;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.jscomp.Traversal.ScopeCallback;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation(true, null, null));
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
  }

  @Test
  public void testScopedAliases() {
    String sourceCode =
        "package test;\n" +
            "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "});";

    Node root = NodeUtil.parseJsFile(this.getClass().getResource(sourceCode), null);
    Node externs = NodeUtil.makeRoot(NodeUtil.makeJsSourceFile("test", "test.js"));

    scopedAliases.process(externs, root);

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    Node googScopeCall = scopeCall.getFirstChild();
    assertEquals("goog.scope", googScopeCall.getString());

    Node anonymousFunction = scopeCall.getLastChild();
    assertEquals(Token.FUNCTION, anonymousFunction.getType());
    assertEquals(0, anonymousFunction.getChildCount());

    Node body = anonymousFunction.getBody();
    assertEquals(Token.BLOCK, body.getType());

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());

    Node domAliasDefinition = aliasDefinitions.get(0);
    assertEquals(Token.VAR, domAliasDefinition.getType());
    Var domAlias = domAliasDefinition.getVar();
    assertEquals("dom", domAlias.getName());
    assertEquals("goog.dom", domAlias.getInitialValue().getString());

    Node tagNameAliasDefinition = aliasDefinitions.get(1);
    assertEquals(Token.VAR, tagNameAliasDefinition.getType());
    Var tagNameAlias = tagNameAliasDefinition.getVar();
    assertEquals("DIV", tagNameAlias.getName());
    assertEquals("goog.dom.TagName.DIV", tagNameAlias.getInitialValue().getString());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(2, aliasUsages.size());

    AliasUsage domAliasUsage = aliasUsages.get(0);
    Node domAliasUsageNode = domAliasUsage.getNode();
    assertEquals("dom", domAliasUsageNode.getString());
    ((AliasedNode) domAliasUsage).applyAlias();
    assertEquals("goog.dom", domAliasUsageNode.getParent().getChildAtIndex(0).getString());

    AliasUsage tagNameAliasUsage = aliasUsages.get(1);
    Node tagNameAliasUsageNode = tagNameAliasUsage.getNode();
    assertEquals("DIV", tagNameAliasUsageNode.getString());
    ((AliasedTypeNode) tagNameAliasUsage).applyAlias();
    assertEquals("goog.dom.TagName.DIV", tagNameAliasUsageNode.getParent().getChildAtIndex(1).getString());

    Collection<Node> errors = compiler.getDiagnostics();
    assertTrue(errors.isEmpty());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    String sourceCode =
        "package test;\n" +
            "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  this.dom = dom;\n" + // This should cause an error.
            "  return function() {\n" + // This should cause an error.
            "    throw new Error();\n" + // This should cause an error.
            "  };\n" +
            "  dom.createElement(DIV);\n" +
            "});";

    Node root = NodeUtil.parseJsFile(this.getClass().getResource(sourceCode), null);
    Node externs = NodeUtil.makeRoot(NodeUtil.makeJsSourceFile("test", "test.js"));

    scopedAliases.process(externs, root);

    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    Node googScopeCall = scopeCall.getFirstChild();
    assertEquals("goog.scope", googScopeCall.getString());

    Node anonymousFunction = scopeCall.getLastChild();
    assertEquals(Token.FUNCTION, anonymousFunction.getType());
    assertEquals(0, anonymousFunction.getChildCount());

    Node body = anonymousFunction.getBody();
    assertEquals(Token.BLOCK, body.getType());

    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());

    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(0, aliasUsages.size());

    Collection<Node> errors = compiler.getDiagnostics();
    assertEquals(4, errors.size());

    Node firstError = errors.iterator().next();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", firstError.getSourceName());

    Node secondError = errors.iterator().next();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", secondError.getSourceName());

    Node thirdError = errors.iterator().next();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", thirdError.getSourceName());

    Node fourthError = errors.iterator().next();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", fourthError.getSourceName());
  }
}