import static org.junit.Assert.*;
import static com.google.javascript.jscomp.AliasesTestUtil.compile;
import static com.google.javascript.jscomp.AliasesTestUtil.getTransformation;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticListener;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private DiagnosticListener diagnostics;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    diagnostics = new DiagnosticListener() {};
    compiler = new AbstractCompiler(diagnostics) {};
    scopedAliases = new ScopedAliases(compiler, null, null);
  }

  @Test
  public void testScopedAliases() {
    String code =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  var x = goog.x;" +
            "  goog.scope(function() {" +
            "    goog.dom.createElement(DIV);" +
            "    goog.createElement(DIV);" +
            "    goog.x = 123;" +
            "  });" +
            "  goog.dom.createElement(DIV);" +
            "  goog.x = 456;" +
            "});";

    SourceFile sourceFile = new SourceFile("Test.java", code, null);
    Node rootNode = compiler.parseTree(sourceFile);
    List<Node> errors = compile(compiler, rootNode);

    assertFalse(errors.isEmpty());

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals(2, scopeCalls.size());
    assertEquals(4, aliasUsages.size());

    AliasUsage aliasUsage1 = aliasUsages.get(0);
    Node aliasDefinition1 = new Node(Token.IDENT, "dom", null);
    ((AliasedNode) aliasUsage1).applyAlias();
    Node aliasedNode1 = aliasDefinition1.getParent();
    assertEquals("dom", aliasedNode1.getString());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    Node aliasDefinition2 = new Node(Token.IDENT, "DIV", null);
    ((AliasedNode) aliasUsage2).applyAlias();
    Node aliasedNode2 = aliasDefinition2.getParent();
    assertEquals("goog.dom.TagName.DIV", aliasedNode2.getQualifiedName());

    AliasUsage aliasUsage3 = aliasUsages.get(2);
    Node aliasDefinition3 = new Node(Token.IDENT, "x", null);
    ((AliasedNode) aliasUsage3).applyAlias();
    Node aliasedNode3 = aliasDefinition3.getParent();
    assertEquals("goog.x", aliasedNode3.getQualifiedName());

    Node scopeCall1 = scopeCalls.get(0);
    Node anonymousFnNode1 = scopeCall1.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode1.getChildCount());
    Node bodyNode1 = anonymousFnNode1.getChildAtIndex(0);
    assertEquals(0, bodyNode1.getChildCount());

    Node scopeCall2 = scopeCalls.get(1);
    Node anonymousFnNode2 = scopeCall2.getChildAtIndex(1);
    assertEquals(2, anonymousFnNode2.getChildCount());
    Node bodyNode2 = anonymousFnNode2.getChildAtIndex(0);
    assertEquals(1, bodyNode2.getChildCount());
    Node statementNode2 = bodyNode2.getChildAtIndex(0);
    assertEquals("goog.x = 123", statementNode2.getString());

    Node aliasDefinition4 = new Node(Token.IDENT, "x", null);
    aliasDefinition4.setInt(456);
    aliasUsages.get(3).applyAlias();
    Node aliasedNode4 = aliasDefinition4.getParent();
    assertEquals("x", aliasedNode4.getString());
    assertEquals(456, aliasedNode4.getInt());
  }

  @Test
  public void testScopedAliasesError() {
    String code =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.scope(function() {" +
            "    goog.dom.createElement(DIV);" +
            "    this.createElement(DIV);" + // This should error.
            "  });" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = new SourceFile("Test.java", code, null);
    Node rootNode = compiler.parseTree(sourceFile);
    List<Node> errors = compile(compiler, rootNode);

    assertFalse(errors.isEmpty());

    DiagnosticType diagnostic = getTransformation(errors, 0).getDiagnosticType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostic);
  }

  @Test
  public void testScopedAliasesError2() {
    String code =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.scope(function() {" +
            "    goog.dom.createElement(DIV);" +
            "    return;" + // This should error.
            "  });" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = new SourceFile("Test.java", code, null);
    Node rootNode = compiler.parseTree(sourceFile);
    List<Node> errors = compile(compiler, rootNode);

    assertFalse(errors.isEmpty());

    DiagnosticType diagnostic = getTransformation(errors, 0).getDiagnosticType();
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostic);
  }

  @Test
  public void testScopedAliasesError3() {
    String code =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.scope(function() {" +
            "    try {" +
            "      goog.dom.createElement(DIV);" +
            "      throw new Error('test');" + // This should error.
            "    } catch (e) {" +
            "      console.log(e.message);" +
            "    }" +
            "  });" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = new SourceFile("Test.java", code, null);
    Node rootNode = compiler.parseTree(sourceFile);
    List<Node> errors = compile(compiler, rootNode);

    assertFalse(errors.isEmpty());

    DiagnosticType diagnostic = getTransformation(errors, 0).getDiagnosticType();
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostic);
  }
}