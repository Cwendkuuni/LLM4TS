import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.Type.*;

import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasUsage;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "goog.scope(function() {\n" +
        "  var a = goog.a; // aliases goog.a\n" +
        "  var b = goog.b; // aliases goog.b\n" +
        "  var c = goog.c; // aliases goog.c\n" +
        "  var d = goog.dom; // aliases goog.dom\n" +
        "  var e = d.createElement(goog.dom.TagName.DIV); // should be\n" +
        "  // goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  var f = goog.g; // should not alias goog.g\n" +
        "  function g() {}; // should not alias function g\n" +
        "});");

    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnostics(true);
    options.setEmitWarningOnErrors(true);
    options.setAllowAllTypes(true);
    options.setEmitPureCompilations(false);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(null, null, null);
    Node root = sourceFile.getAST();
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliasesPass, root, traversal);

    // Check that all alias definitions were found and applied.
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(6, aliasUsages.size());
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        Node aliasDefinition = aliasedNode.aliasDefinition;
        assertEquals(aliasDefinition.getQualifiedName(),
            aliasedNode.aliasReference.getString());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        Node aliasDefinition = aliasedTypeNode.aliasDefinition;
        Node typeReference = aliasedTypeNode.typeReference;
        String aliasName = aliasedTypeNode.aliasName;
        String typeName = typeReference.getString();
        String expandedAliasName = typeName.substring(0, typeName.indexOf(aliasName));
        assertEquals(expandedAliasName,
            aliasDefinition.getQualifiedName().substring(0,
                aliasDefinition.getQualifiedName().lastIndexOf(".")));
      }
    }

    // Check that all alias definitions were removed.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitions.isEmpty());

    // Check that all goog.scope calls were replaced.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertNotNull(scopeCall);
    Node googScopeCall = scopeCall.getFirstChild();
    assertEquals("goog.scope", googScopeCall.getString());
    Node anonymousFunction = googScopeCall.getLastChild();
    assertEquals(1, anonymousFunction.getChildCount());
    Node body = anonymousFunction.getChildAtIndex(0);
    assertNotNull(body);
    Node variableDeclaration = body.getFirstChild();
    assertEquals("var", variableDeclaration.getString());
    Node alias = variableDeclaration.getLastChild();
    assertEquals("a", alias.getString());
  }

  @Test
  public void testScopedAliasesError1() {
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "goog.scope(function() {\n" +
        "  var a = goog.a; // aliases goog.a\n" +
        "  var b = goog.b; // aliases goog.b\n" +
        "  var c = goog.c; // aliases goog.c\n" +
        "  var d = goog.dom; // aliases goog.dom\n" +
        "  var e = goog.dom.createElement(goog.dom.TagName.DIV); // should be\n" +
        "  // goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  var f = goog.g; // should not alias goog.g\n" +
        "  function g() {}; // should not alias function g\n" +
        "  var h = this; // should not alias 'this'\n" +
        "});");

    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnostics(true);
    options.setEmitWarningOnErrors(true);
    options.setAllowAllTypes(true);
    options.setEmitPureCompilations(false);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(null, null, null);
    Node root = sourceFile.getAST();
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliasesPass, root, traversal);

    List<Node> errors = traversal.getDiagnostics();
    assertEquals(2, errors.size());

    Node error1 = errors.get(0);
    String errorMessage1 = error1.getString();
    assertTrue(errorMessage1.contains(GOOG_SCOPE_REFERENCES_THIS.getMessage()));

    Node error2 = errors.get(1);
    String errorMessage2 = error2.getString();
    assertTrue(errorMessage2.contains(GOOG_SCOPE_USES_RETURN.getMessage()));
  }

  @Test
  public void testScopedAliasesError2() {
    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "goog.scope(function() {\n" +
        "  function a() {}\n" +
        "  var b = goog.a; // should alias function a\n" +
        "});");

    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnostics(true);
    options.setEmitWarningOnErrors(true);
    options.setAllowAllTypes(true);
    options.setEmitPureCompilations(false);

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(null, null, null);
    Node root = sourceFile.getAST();
    scopedAliasesPass.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliasesPass, root, traversal);

    List<Node> errors = traversal.getDiagnostics();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    String errorMessage = error.getString();
    assertTrue(errorMessage.contains(GOOG_SCOPE_ALIAS_REDEFINED.getMessage()));
  }
}