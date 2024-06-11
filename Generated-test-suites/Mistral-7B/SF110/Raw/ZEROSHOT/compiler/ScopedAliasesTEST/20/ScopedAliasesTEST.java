import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode = ""
        + "function foo() {"
        + "  var dom = goog.dom;"
        + "  var DIV = dom.TagName.DIV;"
        + "  dom.createElement(DIV);"
        + "}"
        + "function bar() {"
        + "  goog.scope(function() {"
        + "    var dom = goog.dom;"
        + "    var DIV = dom.TagName.DIV;"
        + "    goog.dom.createElement(DIV);"
        + "  });"
        + "}"
        + "function baz() {"
        + "  goog.scope(function() {"
        + "    var dom = goog.dom;"
        + "    var DIV = dom.TagName.DIV;"
        + "  });"
        + "  goog.dom.createElement(goog.dom.TagName.DIV);"
        + "}"
        + "";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.RENAME);
    options.setAliasTransformations(
        new AliasTransformation[] {new AliasTransformation("dom", "goog.dom")});

    AbstractCompiler compiler = new Compiler();
    HotSwapCompilerPass compilerPass = new ScopedAliases(compiler, null, null);

    Node rootNode = Node.parse(sourceCode);
    NodeTraversal nodeTraversal = new NodeTraversal();
    nodeTraversal.setScopedCallback(compilerPass);
    nodeTraversal.traverse(compiler, rootNode);

    Traversal traversal = (Traversal) compilerPass;

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertTrue(aliasDefinitionsInOrder.size() == 3);
    assertTrue(aliasUsages.size() == 5);
    assertTrue(scopeCalls.size() == 3);

    Node fooFunction = aliasDefinitionsInOrder.get(0);
    Node barFunction = aliasDefinitionsInOrder.get(1);
    Node bazFunction = aliasDefinitionsInOrder.get(2);

    Node fooDomVariable = aliasUsages.get(0).getNode();
    Node barDomVariable = aliasUsages.get(1).getNode();
    Node bazDomVariable = aliasUsages.get(2).getNode();
    Node bazDomElementCreation = aliasUsages.get(3).getNode();
    Node bazDomElementCreationInScope = aliasUsages.get(4).getNode();

    assertEquals("function foo() {", fooFunction.toString());
    assertEquals("var dom =", fooDomVariable.getParent().toString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV);",
        bazDomElementCreation.toString());

    assertEquals("function bar() {", barFunction.toString());
    assertEquals("goog.scope(function() {", barFunction.getChildAtIndex(0).toString());
    assertEquals("var dom =", barDomVariable.getParent().toString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV);",
        bazDomElementCreationInScope.toString());

    assertEquals("function baz() {", bazFunction.toString());
    assertEquals("goog.scope(function() {", bazFunction.getChildAtIndex(0).toString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV);",
        bazDomVariable.getParent().getChildAtIndex(1).toString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV);",
        bazDomElementCreation.toString());

    SourcePosition position = fooDomVariable.getSourcePosition();
    assertEquals("foo.js", position.getFileName());
    assertEquals(1, position.getLineNumber());
    assertEquals(4, position.getCharacterNumber());
  }
}