import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Test case 1: Basic functionality
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALL);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);

    String rootCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = NodeUtil.parseTree(rootCode);
    scopedAliasesPass.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(NodeUtil.createQualifiedName("dom", "TagName"),
        aliasDefinitionsInOrder.get(0).getLastChild().getQualifiedName());

    List<Node> scopeCalls = scopedAliasesPass.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());

    // Test case 2: Edge case - Multiple alias definitions
    String rootCodeMultiAlias = "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "  });\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node rootMultiAlias = NodeUtil.parseTree(rootCodeMultiAlias);
    scopedAliasesPass.process(null, rootMultiAlias);

    aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(NodeUtil.createQualifiedName("dom", "TagName"),
        aliasDefinitionsInOrder.get(0).getLastChild().getQualifiedName());
    assertEquals(NodeUtil.createQualifiedName("dom", "TagName"),
        aliasDefinitionsInOrder.get(1).getLastChild().getQualifiedName());

    // Test case 3: Error cases
    Compiler compilerError = new Compiler();
    CompilerOptions optionsError = new CompilerOptions();
    optionsError.setAliasTransformation(ALL);
    HotSwapCompilerPass scopedAliasesPassError =
        new ScopedAliases(compilerError, null, null);

    String rootCodeError = "goog.scope(function() {\n" +
        "  goog.scope(function() {});\n" + // improperly used goog.scope
        "  function() {}\n" + // goog.scope cannot contain functions
        "  return {};\n" + // goog.scope cannot use return
        "  throw 'Error';\n" + // goog.scope cannot use throw
        "});";

    Node rootCodeErrorNode = NodeUtil.parseTree(rootCodeError);
    scopedAliasesPassError.process(null, rootCodeErrorNode);

    // Verify that the compiler reports errors
    List<DiagnosticType> compilationErrors = compilerError.getErrors();
    assertEquals(5, compilationErrors.size());

    // Test case 4: Nested scopes and alias redefinition
    String rootCodeNested = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.scope(function() {\n" +
        "    var dom = com.google.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "  });\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node rootCodeNestedNode = NodeUtil.parseTree(rootCodeNested);
    scopedAliasesPass.process(null, rootCodeNestedNode);

    aliasDefinitionsInOrder = scopedAliasesPass.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(NodeUtil.createQualifiedName("dom", "TagName"),
        aliasDefinitionsInOrder.get(0).getLastChild().getQualifiedName());
    assertEquals(NodeUtil.createQualifiedName("com.google.dom", "TagName"),
        aliasDefinitionsInOrder.get(1).getLastChild().getQualifiedName());
  }
}